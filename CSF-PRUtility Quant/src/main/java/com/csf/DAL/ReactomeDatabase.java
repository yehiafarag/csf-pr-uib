/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csf.DAL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author y-mok
 */
public class ReactomeDatabase {

    private Connection conn = null;
    private final String url;
    private final String dbName;
    private final String driver;
    private final String userName;
    private final String password;

    public ReactomeDatabase(String url, String dbName, String driver, String userName, String password) {
        this.url = url;
        this.dbName = dbName;
        this.driver = driver;
        this.userName = userName;
        this.password = password;

    }
    private boolean successfulProcess;
    int size;

    public boolean updateDatabase(final String pathToNetworkFile) {
        try {
            successfulProcess = false;
            final Thread towait = new Thread(() -> {
                List<String> readingLineList = readFile(pathToNetworkFile);
                if (readingLineList != null) {
                    int storeCycles = (Math.round(readingLineList.size() / 500000)) + 1;
                    int start = 0;
                    successfulProcess = createDatabase();
                    if (successfulProcess) {
                        for (int current = 0;;) {
                            List<String> subList = readingLineList.subList(start, Math.min(start + 500000, readingLineList.size()));
                            start = start + subList.size();
                            List<ReactomeBean> reactomBeansList = prepareDataToStore(subList);
                            for (int subStart = 0;;) {
                                List<ReactomeBean> subReactomBeansList = reactomBeansList.subList(subStart, Math.min(subStart + 10000, reactomBeansList.size()));
                                subStart = subStart + subReactomBeansList.size();
                                successfulProcess = updateReactomDatabase(subReactomBeansList);
                                size += subReactomBeansList.size();
                                if (subStart >= reactomBeansList.size() - 1) {
                                    break;
                                }
                            }
                            if (start >= readingLineList.size() - 1) {
                                break;
                            }
                        }
                    }
                } else {
                    successfulProcess = false;
                }
                System.out.println("at full beans list " + size);

            });
            towait.start();
            while (towait.isAlive()) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return successfulProcess;

    }
    int index = 0;

    private synchronized List<String> readFile(String pathToFile) {
        List<String> readerSet = new ArrayList<>();

        try {
            File reactomeNetworkFile = new File(pathToFile);
            if (!reactomeNetworkFile.exists()) {
                return null;
            }
            FileReader fileReader = new FileReader(reactomeNetworkFile);
            String line;
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                readerSet.add(line);
            }
            System.gc();
            System.out.println("at readerset size " + readerSet.size());
            return readerSet;
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + "'");
        }
        return null;
    }

    private List<ReactomeBean> prepareDataToStore(List<String> readerSet) {

        index = 0;
        List<ReactomeBean> reactomBeansSet = new ArrayList<>();
        readerSet.stream().map((row) -> row.split("\t")).map((rowArr) -> {
            ReactomeBean bean = new ReactomeBean();
            bean.setId1(rowArr[0]);
            bean.setId2(rowArr[1]);
            bean.setContainer_id(rowArr[2]);
            bean.setType(rowArr[3]);
            bean.setRole1(rowArr[4]);
            bean.setRole2(rowArr[5]);
            index++;
            return bean;
        }).forEachOrdered((bean) -> {
            reactomBeansSet.add(bean);
            if (index == 1000000) {
                index = 0;
                System.gc();
            }
        });
        return reactomBeansSet;

    }

    private synchronized boolean createDatabase() {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                System.out.println("at conn " + url + "mysql");
                conn = DriverManager.getConnection(url + "mysql" + "?autoReconnect=true&?useUnicode=yes", userName, password);

            }

            //temp 
            Statement statement = conn.createStatement();
            String sqoDataBase = "SHOW DATABASES;";
            ResultSet rs1 = statement.executeQuery(sqoDataBase);
            Set<String> datasetnames = new HashSet<String>();
            while (rs1.next()) {
                String db = rs1.getString("Database");
                datasetnames.add(db);

            }
            Statement statement2 = conn.createStatement();//                
            for (String db : datasetnames) {
                if (db.contains("reactome_")) {
                    statement2.executeUpdate("DROP DATABASE " + db + " ;");
                }

            }
            statement = conn.createStatement();
            String csfSQL = "CREATE DATABASE IF NOT exists  " + dbName;
            statement.executeUpdate(csfSQL);

            Statement st = conn.createStatement();
            String statment = "CREATE TABLE " + dbName + ".`Edges` ( `reaction_index` INT NOT NULL AUTO_INCREMENT , `id1` VARCHAR(500) NOT NULL , `id2` VARCHAR(500) NOT NULL , `container_id` VARCHAR(500) NOT NULL , `type` VARCHAR(500) NOT NULL , `role1` VARCHAR(500) NOT NULL , `role2` VARCHAR(500) NOT NULL , INDEX (`reaction_index`)) ENGINE = InnoDB;";
            st.executeUpdate(statment);

            conn.close();
            return true;
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 102 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error line 104 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error line 106 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("at error line 108 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        }
        return false;

    }

    private synchronized boolean updateReactomDatabase(List<ReactomeBean> reactomBeans) {

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            String updateProtSeqStat = "INSERT INTO `edges` (`id1`, `id2`, `container_id`, `type`, `role1`, `role2`) VALUES ";
            String values = " ( ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatment = null;
            boolean test = false;

            for (ReactomeBean bean : reactomBeans) {
                updateProtSeqStat = updateProtSeqStat + " " + values + " , ";
            }
            updateProtSeqStat = updateProtSeqStat.substring(0, updateProtSeqStat.length() - 3) + " ;";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            insertStatment = conn.prepareStatement(updateProtSeqStat);
            int indexer = 1;
            for (ReactomeBean reactomeBean : reactomBeans) {
                insertStatment.setString(indexer++, reactomeBean.getId1());
                insertStatment.setString(indexer++, reactomeBean.getId2());
                insertStatment.setString(indexer++, reactomeBean.getContainer_id());
                insertStatment.setString(indexer++, reactomeBean.getType());
                insertStatment.setString(indexer++, reactomeBean.getRole1());
                insertStatment.setString(indexer++, reactomeBean.getRole2());

            }
            insertStatment.execute();
            insertStatment.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
