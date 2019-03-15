package com.view;

import com.pepshaker.DataHandler;
import com.pepshaker.PSFileImporter;
import com.compomics.util.examples.BareBonesBrowserLaunch;
import com.csf.DAL.ReactomeDatabase;
import com.csf.handler.Handler;
import com.pepshaker.util.ProgressDialogParent;
import com.pepshaker.util.beans.ExperimentBean;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.SwingWorker;
import sun.security.util.Password;

/**
 *
 * @author Yehia Farag
 */
public class GUI extends javax.swing.JFrame implements ProgressDialogParent {

    private ExperimentBean exp;
    private String database_name = "csf_pr_v3_final";
//    private final String executeCmd;
    private final String backupFileUrl = "D:\\CSF-PR-full-06-01-2017\\backup.sql"; // "/home/probe/user/CSF-PR-FILES/backup.sql";             //"D:\\backups\\sqlQuant18-8\\backup-quant.sql";                        //   
    private String processUrl = "C:\\AppServ\\MySQL\\bin\\mysqldump.exe"; ///usr/bin/mysqldump";
    private String mySqlDBPath = "C:\\AppServ\\MySQL\\bin\\mysql.exe";

    //         "C:\\AppServ\\MySQL\\bin\\mysqldump.exe";//"C:\\AppServ\\MySQL\\bin\\mysqldump.exe"           ///usr/bin/mysqldump   

    /* Creates new form GUI */
    public GUI() {
        initComponents();
        reactomeDatabaseBackupFormBtn.setText("Backup Reactome Database");
        jProgressBar1.setMaximum(100);
        System.out.println("System.getProperty(\"os.name\") " + System.getProperty("os.name"));
        if (System.getProperty("os.name").contains("Windows")) {
            processUrl = "C:\\AppServ\\MySQL\\bin\\mysqldump.exe";
            mySqlDBPath = "C:\\AppServ\\MySQL\\bin\\mysql.exe";

        } else {
            processUrl = "/usr/bin/mysqldump";
            mySqlDBPath = "/usr/bin/mysql";
        }

        this.setActivateIdentification(true);
        jRadioButton1.setSelected(true);
        restoreDbBtn.setEnabled(false);
        backupDbBtn.setEnabled(false);

        diseaseGroupsFullNameFileTextFieald.setText("");

        jLabel13.setText("");
        jLabel13.setFont(new Font("Serif", Font.BOLD, 12));
        jLabel13.setForeground(Color.RED);
        jTextField1.setText("root");
        jPasswordField1.setText("qwe1212qwe1212");

        jTextField13.setText("localhost");
        jTextField13.setToolTipText("database url");

        jTextField3.setText("");
        jTextField4.setText("");
        jTextField2.setText("");
        jTextField2.setToolTipText("Experiment Name");
        jTextArea1.setText("");
        jTextArea1.setToolTipText("Description");
        jTextField8.setText("");
        jTextField8.setToolTipText("Frag Mode");

        jButton4.setText("");
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/1.jpg"))); // NOI18N
        jButton4.setToolTipText("Proteomics Unit at the University of Bergen");
        jButton4.setBorder(null);
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);

        jButton5.setText("");
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/2.jpg"))); // NOI18N
        jButton5.setToolTipText("The University of Bergen");
        jButton5.setBorder(null);
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/icons/4.png")).getImage());

        jButton6.setText("");
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/3.jpg"))); // NOI18N
        jButton6.setToolTipText("The Kristian Gerhard Jebsen Foundation");
        jButton6.setBorder(null);
        jButton6.setBorderPainted(false);
        jButton6.setContentAreaFilled(false);

        jTextField5.setText("");
        jTextField5.setToolTipText("Species");

        jTextField11.setText("");
        jTextField11.setToolTipText("Instrument Type");

        jTextField9.setText("");
        jTextField9.setToolTipText("Sample Type");

        jTextField10.setText("");
        jTextField10.setToolTipText("Sample Processing");

        setResizable(false);
        jTextField12.setText("admin@csf.no");
        jTextField12.setToolTipText("Email");
        jTextField12.setEditable(false);

        jTextField6.setText("");
        jTextField6.setToolTipText("Publication Link");

        jButton1.setText("Choose");
        idProcessBtn.setText("Process");
        jButton3.setText("Choose");
        jButton7.setText("Choose");
        jTextField14.setText("");
        this.setTitle("CSF-PR File Reader");

        mysqldumpText.setText(processUrl);

        jRadioButton2.setSelected(true);
        jRadioButton3.setSelected(false);
        restoreDbBtn.setEnabled(true);
        backupDbBtn.setEnabled(false);

        backupDBPanel.setVisible(false);
        restoreDbPanel.setVisible(true);
        quantPanel.getProcessBtn().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantProcessBtnActionPerformed();
            }
        });
        mysqlresText.setText(mySqlDBPath);

        jLabel33.setForeground(Color.red);
        databaseNameField.setText(database_name);

    }

    /* This method is called from within the constructor to
     * Initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * Always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        idPanel = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        reactomeDatabaseBackupFormBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel34 = new javax.swing.JLabel();
        databaseNameField = new javax.swing.JTextField();
        dbPanel = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        backupDBPanel = new javax.swing.JPanel();
        mysqldumpText = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        sqlFileText = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        restoreDbPanel = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        mysqlresText = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        mysqlFileRestText = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        restoreDbBtn = new javax.swing.JButton();
        backupDbBtn = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        diseaseGroupsFullNameFileTextFieald = new javax.swing.JTextField();
        jTextField19 = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        quantPanel = new com.view.QuantPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel5 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        idProcessBtn = new javax.swing.JButton();

        jToggleButton1.setText("jToggleButton1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(1, 0));

        idPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel15.setFont(new java.awt.Font("Tekton Pro", 1, 24)); // NOI18N
        jLabel15.setText("Identification Data Panel");

        jLabel3.setText("Dataset Name");

        jTextField2.setText("jTextField2");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel12.setText("Description");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel7.setText("Frag Mode");

        jTextField8.setText("jTextField8");
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jLabel4.setText("Species");

        jTextField5.setText("jTextField5");

        jLabel10.setText("Instrument Type");

        jTextField11.setText("jTextField11");

        jLabel5.setText("Sample Type");

        jTextField9.setText("jTextField9");
        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });

        jLabel6.setText("Sample Processing");

        jTextField10.setText("jTextField10");

        jLabel9.setText("Email");

        jTextField12.setText("jTextField12");

        jLabel11.setText("Publication Link");

        jTextField6.setText("jTextField6");

        jLabel8.setText("Process  Fractions");

        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel16.setText("CPS File");

        jLabel17.setText("Glycopeptide File");

        jLabel18.setText("Application Folder");

        jTextField3.setText("jTextField3");

        jTextField4.setText("jTextField4");
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jTextField14.setText("jTextField14");
        jTextField14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField14ActionPerformed(evt);
            }
        });

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("jButton3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton7.setText("jButton7");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout idPanelLayout = new javax.swing.GroupLayout(idPanel);
        idPanel.setLayout(idPanelLayout);
        idPanelLayout.setHorizontalGroup(
            idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(idPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(idPanelLayout.createSequentialGroup()
                        .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel15)
                    .addGroup(idPanelLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(idPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27)
                .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(idPanelLayout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, idPanelLayout.createSequentialGroup()
                        .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField9)
                            .addComponent(jTextField10, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                            .addComponent(jTextField12)
                            .addComponent(jTextField6)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, idPanelLayout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, idPanelLayout.createSequentialGroup()
                        .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField3)
                            .addComponent(jTextField14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(12, 12, 12))
        );
        idPanelLayout.setVerticalGroup(
            idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(idPanelLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel15)
                .addGap(18, 18, 18)
                .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField9)
                    .addGroup(idPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel3))
                    .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)))
                .addGap(18, 18, 18)
                .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(idPanelLayout.createSequentialGroup()
                        .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(idPanelLayout.createSequentialGroup()
                                .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))
                                .addGap(18, 18, 18)
                                .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jCheckBox1)))
                            .addComponent(jScrollPane1))
                        .addGap(18, 18, 18)
                        .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3)
                            .addComponent(jLabel7)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1)
                            .addComponent(jLabel4)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addGap(18, 18, 18)
                        .addGroup(idPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7)
                            .addComponent(jLabel10)
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))))
                .addGap(55, 55, 55))
        );

        reactomeDatabaseBackupFormBtn.setText("jButton16");
        reactomeDatabaseBackupFormBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reactomeDatabaseBackupFormBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(reactomeDatabaseBackupFormBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(reactomeDatabaseBackupFormBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setText("Database Username");

        jTextField1.setText("jTextField1");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel14.setText("URL");

        jTextField13.setText("jTextField13");
        jTextField13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField13ActionPerformed(evt);
            }
        });

        jLabel2.setText("Database Password");

        jPasswordField1.setText("jPasswordField1");
        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        jLabel34.setText("DB");

        databaseNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                databaseNameFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(databaseNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jPasswordField1)
                    .addComponent(jLabel1)
                    .addComponent(jLabel34)
                    .addComponent(databaseNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        dbPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel19.setFont(new java.awt.Font("Tekton Pro", 1, 24)); // NOI18N
        jLabel19.setText("Database Backup");

        jRadioButton2.setText("Restore");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jRadioButton3.setText("Backup");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        jButton2.setText("Choose");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel20.setText("MySQL Dump ");

        jLabel21.setText("SQL File");

        sqlFileText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sqlFileTextActionPerformed(evt);
            }
        });

        jButton8.setText("Choose");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout backupDBPanelLayout = new javax.swing.GroupLayout(backupDBPanel);
        backupDBPanel.setLayout(backupDBPanelLayout);
        backupDBPanelLayout.setHorizontalGroup(
            backupDBPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backupDBPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mysqldumpText, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sqlFileText, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton8)
                .addContainerGap())
        );
        backupDBPanelLayout.setVerticalGroup(
            backupDBPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backupDBPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(mysqldumpText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton2)
                .addComponent(jLabel20)
                .addComponent(jLabel21)
                .addComponent(sqlFileText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton8))
        );

        jLabel22.setText("MySQL Resource");

        jButton9.setText("Choose");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel23.setText("SQL File");

        mysqlFileRestText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mysqlFileRestTextActionPerformed(evt);
            }
        });

        jButton10.setText("Choose");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout restoreDbPanelLayout = new javax.swing.GroupLayout(restoreDbPanel);
        restoreDbPanel.setLayout(restoreDbPanelLayout);
        restoreDbPanelLayout.setHorizontalGroup(
            restoreDbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(restoreDbPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mysqlresText, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jButton9)
                .addGap(18, 18, 18)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mysqlFileRestText, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton10)
                .addGap(301, 301, 301))
        );
        restoreDbPanelLayout.setVerticalGroup(
            restoreDbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(restoreDbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel22)
                .addComponent(mysqlresText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(restoreDbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton9)
                .addComponent(jLabel23)
                .addComponent(mysqlFileRestText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton10))
        );

        restoreDbBtn.setText("Restore DB");
        restoreDbBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restoreDbBtnActionPerformed(evt);
            }
        });

        backupDbBtn.setText("Backup DB");
        backupDbBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backupDbBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(backupDbBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 1000, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(restoreDbBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(51, 51, 51))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(restoreDbBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(backupDbBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout dbPanelLayout = new javax.swing.GroupLayout(dbPanel);
        dbPanel.setLayout(dbPanelLayout);
        dbPanelLayout.setHorizontalGroup(
            dbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dbPanelLayout.createSequentialGroup()
                .addGroup(dbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(dbPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(dbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addGroup(dbPanelLayout.createSequentialGroup()
                                .addComponent(jRadioButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButton3))))
                    .addComponent(backupDBPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(restoreDbPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dbPanelLayout.setVerticalGroup(
            dbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dbPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addGap(18, 18, 18)
                .addGroup(dbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backupDBPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(restoreDbPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jRadioButton1.setText("Identification");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton4.setText("Quantification");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        jRadioButton5.setText("Database backup");
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton5ActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Tekton Pro", 1, 24)); // NOI18N
        jLabel24.setText("Quantitative Data Panel");

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel25.setText("Pubmed ID");

        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jLabel26.setText("Authors");

        jTextField15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField15ActionPerformed(evt);
            }
        });

        jLabel27.setText("Year");

        jTextField16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField16ActionPerformed(evt);
            }
        });

        jButton11.setText("Add");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jLabel29.setText("Title");

        jTextField17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField17ActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel28.setText("Insert/Update  Publication");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel27)
                                    .addComponent(jLabel26))
                                .addGap(36, 36, 36))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField17, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                            .addComponent(jTextField15, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField16)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(314, 314, 314))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(220, 220, 220)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton11)
                    .addComponent(jLabel33))
                .addContainerGap())
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        diseaseGroupsFullNameFileTextFieald.setText("jTextField18");

        jTextField19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField19ActionPerformed(evt);
            }
        });

        jButton12.setText("Choose");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setText("Add");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel31.setText("Diseases Groups Full Name (.csv)");

        jLabel32.setText("Disease Groups Remapping file (.txt)");

        jLabel30.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel30.setText("Supplementary  Files");

        jButton14.setText("Choose");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setText("Add");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(diseaseGroupsFullNameFileTextFieald, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE)
                            .addComponent(jTextField19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jButton12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jButton14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 20, 20))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addGap(950, 950, 950))))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30)
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(diseaseGroupsFullNameFileTextFieald, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12)
                    .addComponent(jButton13)
                    .addComponent(jLabel31))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(jButton14)
                    .addComponent(jButton15))
                .addContainerGap())
        );

        quantPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 1119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(quantPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(17, 17, 17))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(quantPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jLabel13.setText("jLabel13");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jButton6.setText("jButton6");
        jButton6.setAlignmentY(0.0F);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton5.setText("jButton5");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton4.setText("jButton4");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton6)
                    .addComponent(jButton4))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        idProcessBtn.setText("jButton2");
        idProcessBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idProcessBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(dbPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(53, 53, 53)
                                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(35, 35, 35)
                                    .addComponent(idProcessBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(267, 267, 267))
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(12, 12, 12))))
            .addGroup(layout.createSequentialGroup()
                .addComponent(idPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton4)
                            .addComponent(jRadioButton5))))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(idPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(dbPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(idProcessBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (jButton1.getText().equalsIgnoreCase("Select CPS File")) {
            JFileChooser chooser = new JFileChooser();

            FileFilter cpsfilter = new FileFilter() {
                @Override
                public boolean accept(File myFile) {
                    return myFile.getName().toLowerCase().endsWith("cps")
                            || myFile.isDirectory();
                }

                @Override
                public String getDescription() {
                    return "Supported formats: Peptide-Shaker(.cps)";
                }
            };
            FileFilter sqlfilter = new FileFilter() {
                @Override
                public boolean accept(File myFile) {
                    return myFile.getName().toLowerCase().endsWith("sql")
                            || myFile.isDirectory();
                }

                @Override
                public String getDescription() {
                    return "Supported formats: SQL (.sql)";
                }
            };

            if (jButton1.getText().equalsIgnoreCase("Select CPS File")) {
                chooser.addChoosableFileFilter(cpsfilter);
            } else {
                chooser.addChoosableFileFilter(sqlfilter);
            }
            int option = chooser.showOpenDialog(this); // parentComponent must a component like JFrame, JDialog...
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                String path = selectedFile.getAbsolutePath();
                jTextField3.setText(path);
            }
        } else {
            JFileChooser chooser = new JFileChooser("D:\\CSF-PR Quant data files\\21-01-2016");
            FileFilter textFilter = new FileFilter() {
                @Override
                public boolean accept(final File pathname) {
                    return pathname.getName().endsWith(".txt");
                }

                @Override
                public String getDescription() {
                    return "*.txt";
                }
            };
            chooser.addChoosableFileFilter(textFilter);
            int option = chooser.showOpenDialog(this); // parentComponent must a component like JFrame, JDialog...
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                String path = selectedFile.getAbsolutePath();
                jTextField3.setText(path);
            }

        }
    }//GEN-LAST:event_jButton1ActionPerformed
    private Handler exphandeler;
    private File resourcefolder;

    @SuppressWarnings("SleepWhileInLoop")
    private void quantProcessBtnActionPerformed() {

        String errorMessage = "Not .fasta file";
        String errorMessage1 = "Not .csv file";
        jTextField3.setForeground(Color.black);
        jTextField4.setForeground(Color.black);
        if (quantPanel.getjTextField4().getText() != null) {

            if (quantPanel.getjTextField4().getText().equalsIgnoreCase("") || !quantPanel.getjTextField4().getText().endsWith(".fasta")) {
                quantPanel.getjTextField4().setText(errorMessage);
                quantPanel.getjTextField4().setForeground(Color.red);
                return;

            }

        } else if (quantPanel.getQuantCsvTextField().getText() != null && quantPanel.getQuantCsvTextField().getText() == null) {
            if (quantPanel.getQuantCsvTextField().getText() == null || quantPanel.getQuantCsvTextField().getText().equalsIgnoreCase("") || !quantPanel.getQuantCsvTextField().getText().endsWith(".csv")) {
                quantPanel.getQuantCsvTextField().setText(errorMessage1);
                quantPanel.getQuantCsvTextField().setForeground(Color.red);
                return;

            }
            quantPanel.getQuantCsvTextField().setText(errorMessage);
            quantPanel.getQuantCsvTextField().setForeground(Color.red);

        }

        try {
            new Thread() {
                @Override
                public void run() {
                    jProgressBar1.setIndeterminate(true);
                    jProgressBar1.setVisible(true);
                    jLabel13.setText("Reading and storing quant data");
                    revalidate();
                    repaint();
                }
            }.start();
            new Thread("DisplayThread") {
                @Override
                public void run() {
                    jProgressBar1.setMaximum(100);

                    RunWorkerQuantProcess t = new RunWorkerQuantProcess();
                    t.execute();
                    while (!t.isDone()) {
                        try {
                            Thread.sleep(50);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    jProgressBar1.setValue(0);
                    jProgressBar1.setVisible(false);
                }
            }.start();

        } catch (Exception e) {
            //   e.printStackTrace();
        }

//        try {
//            new Thread() {
//                @Override
//                public void run() {
//                    jProgressBar1.setIndeterminate(true);
//
//                }
//            }.start();
//            Thread t = new Thread("DisplayThread") {
//                @Override
//                @SuppressWarnings("CallToPrintStackTrace")
//                public void run() {
//                    jLabel13.setText("reading and storing data");
//                    jProgressBar1.setVisible(true);
//                    jProgressBar1.setMaximum(100);
//                    if (exphandeler == null) {
//                        try {
//                            String password = "";
//                            for (char c : jPasswordField1.getPassword()) {
//                                password += c;
//                            }
//                            exphandeler = new Handler("jdbc:mysql://" + jTextField13.getText() + ":3306/", database_name, "com.mysql.jdbc.Driver", jTextField1.getText(), password);
//                        } catch (SQLException sqlE) {
//                            sqlE.printStackTrace();
//                        }
//                    }
//                    boolean processing = exphandeler.handelQuantPubData(quantPanel.getQuantCsvTextField().getText(), quantPanel.getjTextField4().getText());
//                    if (processing) {
//                        jLabel13.setText("done");
//                    } else {
//                        jLabel13.setText("error in storing data check the input file");
//                    }
//                }
//            };
//            t.start();
//            while (t.isAlive()) {
//                Thread.sleep(100);
//            }
//            jProgressBar1.setVisible(false);
//        } catch (Exception e) {
//            System.err.println("error: " + e.getLocalizedMessage());
//        }
    }

    @SuppressWarnings("SleepWhileInLoop")
    private void idProcessBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idProcessBtnActionPerformed
        if (correct) {
            if (exphandeler == null) {
                try {
                    String password = "";
                    for (char c : jPasswordField1.getPassword()) {
                        password += c;
                    }
                    if (databaseNameField.getText() != null && !databaseNameField.getText().trim().equalsIgnoreCase("")) {
                        database_name = databaseNameField.getText().trim();
                    }
                    exphandeler = new Handler("jdbc:mysql://" + jTextField13.getText() + ":3306/", database_name, "com.mysql.jdbc.Driver", jTextField1.getText(), password);
                } catch (SQLException sqlE) {
                    System.out.println("at error line 630 " + this.getClass().getName() + "   " + sqlE);
                }
            }
            exphandeler.correctProtInfo();
            return;
        }
        if (!jRadioButton1.isSelected()) // quant data processing
        {
//
//            String errorMessage = "Not .txt file";
//            String errorMessage1 = "Not .csv file";
//            jTextField3.setForeground(Color.black);
//            jTextField4.setForeground(Color.black);
//
//            if (quantPanel.getjTextField4().getText() != null) {
//
//                if (quantPanel.getjTextField4().getText().equalsIgnoreCase("") || !quantPanel.getjTextField4().getText().endsWith(".txt")) {
//                    quantPanel.getjTextField4().setText(errorMessage);
//                    quantPanel.getjTextField4().setForeground(Color.red);
//                    return;
//
//                }
//
//            } else if (quantPanel.getQuantCsvTextField().getText() != null && quantPanel.getQuantCsvTextField().getText() == null) {
//                if (quantPanel.getQuantCsvTextField().getText() == null || quantPanel.getQuantCsvTextField().getText().equalsIgnoreCase("") || !quantPanel.getQuantCsvTextField().getText().endsWith(".csv")) {
//                    quantPanel.getQuantCsvTextField().setText(errorMessage1);
//                    quantPanel.getQuantCsvTextField().setForeground(Color.red);
//                    return;
//
//                }
//
//                quantPanel.getQuantCsvTextField().setText(errorMessage);
//                quantPanel.getQuantCsvTextField().setForeground(Color.red);
//
//            }
//
//            try {
//                new Thread() {
//                    @Override
//                    public void run() {
//                        jProgressBar1.setIndeterminate(true);
//
//                    }
//                }.start();
//                Thread t = new Thread("DisplayThread") {
//                    @Override
//                    @SuppressWarnings("CallToPrintStackTrace")
//                    public void run() {
//                        jLabel13.setText("reading and storing data");
//                        jProgressBar1.setVisible(true);
//                        jProgressBar1.setMaximum(100);
//                        if (exphandeler == null) {
//                            try {
//                                String password = "";
//                                for (char c : jPasswordField1.getPassword()) {
//                                    password += c;
//                                }
//                                exphandeler = new Handler("jdbc:mysql://" + jTextField13.getText() + ":3306/", database_name, "com.mysql.jdbc.Driver", jTextField1.getText(), password);
//                            } catch (SQLException sqlE) {
//                                sqlE.printStackTrace();
//                            }
//                        }
//                        boolean processing = exphandeler.handelQuantPubData(quantPanel.getQuantCsvTextField().getText(), quantPanel.getjTextField4().getText());
//                        if (processing) {
//                            jLabel13.setText("done");
//                        } else {
//                            jLabel13.setText("error in storing data check the input file");
//                        }
//                    }
//                };
//                t.start();
//                while (t.isAlive()) {
//                    Thread.sleep(100);
//                }
//                jProgressBar1.setVisible(false);
//
//            } catch (Exception e) {
//                System.err.println("error: " + e.getLocalizedMessage());
//            }

        } else {
            boolean test = this.valiField();
            if (test) {
                try {
                    new Thread() {
                        @Override
                        public void run() {
                            jProgressBar1.setIndeterminate(true);
                            jProgressBar1.setVisible(true);
                            jLabel13.setText("Start Data Processing");
                        }
                    }.start();
                    new Thread("DisplayThread") {
                        @Override
                        public void run() {
                            exp = initExperiment();
                            idProcessBtn.setEnabled(false);
                            jProgressBar1.setMaximum(100);

                            final File cpsFile = new File(jTextField3.getText());
                            String res = jTextField14.getText();
                            resourcefolder = new File(res, "resources");
                            if (resourcefolder.exists()) {
                                for (File f : resourcefolder.listFiles()) {
                                    if (f.getName().equalsIgnoreCase("matches")) {
                                        for (File f2 : f.listFiles()) {
                                            f2.delete();
                                        }
                                    }
                                }

                            } else {
                                System.out.println("this file is not exist");
                            }
                            PSFileImporter fileImporter = new PSFileImporter(jProgressBar1);
                            RunWorkerIDReader t = new RunWorkerIDReader(fileImporter, cpsFile, res);
                            t.execute();
                            while (!t.isDone()) {
                                try {
                                    Thread.sleep(50);
                                } catch (Exception e) {
                                    //e.printStackTrace();
                                }
                            }
                            cleanFields();
                            idProcessBtn.setEnabled(true);
                            jProgressBar1.setValue(0);
                            jProgressBar1.setVisible(false);
                        }
                    }.start();

                } catch (Exception e) {
                    //   e.printStackTrace();
                }

            }
        }
    }//GEN-LAST:event_idProcessBtnActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JFileChooser chooser = new JFileChooser("D:\\CSF-PR Quant data files\\21-01-2016");
        FileFilter textFilter = new FileFilter() {
            @Override
            public boolean accept(final File pathname) {
                return pathname.getName().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "*.txt";
            }
        };
        FileFilter csvFilter = new FileFilter() {
            @Override
            public boolean accept(final File pathname) {
                return pathname.getName().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "*.csv";
            }
        };

        if (jButton3.getText().equalsIgnoreCase("Selecet Glycopeptide File")) {
            chooser.addChoosableFileFilter(textFilter);
        } else {
            chooser.addChoosableFileFilter(csvFilter);
        }
        int option = chooser.showOpenDialog(this); // parentComponent must a component like JFrame, JDialog...
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            jTextField4.setText(path);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    boolean correct = false;
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        correct = true;
//temp 
//        JFileChooser chooser = new JFileChooser();
//        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        int option = chooser.showOpenDialog(this); // parentComponent must a component like JFrame, JDialog...
//        if (option == JFileChooser.APPROVE_OPTION) {
//            File selectedFile = chooser.getSelectedFile();
//            String path = selectedFile.getAbsolutePath();
//            jTextField14.setText(path);
//        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void backupDbBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backupDbBtnActionPerformed
        // EXPORT CSF-PR DATABASE
        try {
            new Thread() {
                @Override
                public void run() {
                    jProgressBar1.setIndeterminate(true);
                    jProgressBar1.setVisible(true);
                    if (databaseNameField.getText() != null && !databaseNameField.getText().trim().equalsIgnoreCase("")) {
                        database_name = databaseNameField.getText().trim();
                    }

                    jLabel13.setText("Backing up ( " + database_name + " ) database\"database ...please wait");
                    revalidate();
                    repaint();
                }
            }.start();
            new Thread("DisplayThread") {
                @Override
                @SuppressWarnings("SleepWhileInLoop")
                public void run() {
                    jProgressBar1.setMaximum(100);

                    RunWorkerBackupDB t = new RunWorkerBackupDB();
                    t.execute();
                    while (!t.isDone()) {
                        try {
                            Thread.sleep(50);
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                    }
                    jProgressBar1.setValue(0);
                    jProgressBar1.setVisible(false);
                }
            }.start();

        } catch (Exception e) {
            //   e.printStackTrace();
        }

//        if (exphandeler == null) {
//            try {
//                String password = "";
//                for (char c : jPasswordField1.getPassword()) {
//                    password += c;
//                }
//                exphandeler = new Handler("jdbc:mysql://" + jTextField13.getText() + ":3306/", database_name, "com.mysql.jdbc.Driver", jTextField1.getText(), password);
//            } catch (SQLException sqlE) {
//                System.out.println("at error SQL Exception line 879 " + this.getClass().getName() + "  " + sqlE.getMessage());
//            }
//        }
//
//        processUrl = mysqldumpText.getText().trim();
//        if (processUrl == null || processUrl.trim().equalsIgnoreCase("") || sqlFileText.getText() == null || sqlFileText.getText().trim().equalsIgnoreCase("")) {
//            jLabel13.setText("Error please check input data");
//            return;
//        }
//        Thread t = new Thread(new Runnable() {
//
//            public void run() {
//                jLabel13.setText("Start backup process for (  " + database_name + " ) database");
//                jProgressBar1.setIndeterminate(true);
//                jProgressBar1.setVisible(true);
//            }
//        });
//        t.start();
//        try {
//            t.join();
//        } catch (InterruptedException ex) {
//            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        exphandeler.exportDataBase(processUrl, sqlFileText.getText());
//        jLabel13.setText("Done");
//        jProgressBar1.setVisible(false);
    }//GEN-LAST:event_backupDbBtnActionPerformed

    @SuppressWarnings("SleepWhileInLoop")
    private void restoreDbBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restoreDbBtnActionPerformed
//      

        try {
            new Thread() {
                @Override
                public void run() {
                    jProgressBar1.setIndeterminate(true);
                    jProgressBar1.setVisible(true);
                    jLabel13.setText("Restoring database...please wait");
                    revalidate();
                    repaint();
                }
            }.start();
            new Thread("DisplayThread") {
                @Override
                public void run() {
                    jProgressBar1.setMaximum(100);
                    RunWorkerRestoreDB t = new RunWorkerRestoreDB();
                    t.execute();
                    while (!t.isDone()) {
                        try {
                            Thread.sleep(50);
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                    }
                    jProgressBar1.setValue(0);
                    jProgressBar1.setVisible(false);
                }
            }.start();

        } catch (Exception e) {
            //   e.printStackTrace();
        }

// TODO add your handling code here:
//        if (exphandeler == null) {
//            try {
//                String password = "";
//                for (char c : jPasswordField1.getPassword()) {
//                    password += c;
//                }
//                exphandeler = new Handler("jdbc:mysql://" + jTextField13.getText() + ":3306/", database_name, "com.mysql.jdbc.Driver", jTextField1.getText(), password);
//            } catch (SQLException sqlE) {
//                System.out.println("at error SQL Exception line 896 " + this.getClass().getName() + "  " + sqlE.getMessage());
//            }
//        }
//        final String mysqldumpUrl = mysqlresText.getText();
//        final String sqlFileUrl = mysqlFileRestText.getText();
//        if (mysqldumpUrl == null || mysqldumpUrl.equalsIgnoreCase("") || sqlFileUrl == null || sqlFileUrl.equalsIgnoreCase("")) {
//            System.err.println("select file");
//
//        } else {
//            try {
//
//                new Thread(new Runnable() {
//
//                    public void run() {
//                        jProgressBar1.setIndeterminate(true);
//                        jProgressBar1.setVisible(true);
//                        jLabel13.setText("Start restoring database");
//                        revalidate();
//                        repaint();
//
//                    }
//                }).start();
//
//                Thread t1 = new Thread("DisplayThread") {
//
//                    public boolean success;
//
//                    @Override
//                    public void run() {
//                        revalidate();
//                        repaint();
//
//                        success = exphandeler.restoreDB(sqlFileUrl, mysqldumpUrl);//"C:\\Users\\y-mok_000\\Google Drive\\csf-pr-backup\\backup.sql");
//                        if (!success) {
//                            jProgressBar1.setVisible(false);
//                            jLabel13.setText("Faild to restore the DB");
//
//                        }
//
//                    }
//                };
//                javax.swing.SwingUtilities.invokeLater(t1);
//                System.out.println("t1 will start now   " + "  " + jLabel13.getText());
//
////                t1.start();
//                while (t1.isAlive()) {
//                    Thread.sleep(100);
//                }
//                if (!jLabel13.getText().equalsIgnoreCase("Faild to restore the DB")) {
//                    jLabel13.setText("Done");
//                }
//                jProgressBar1.setVisible(false);
//
//            } catch (Exception e) {
//                //   e.printStackTrace();
//            }
//
//        }
    }//GEN-LAST:event_restoreDbBtnActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        //identification mode
        setActivateIdentification(true);
        jRadioButton4.setSelected(false);
        jRadioButton5.setSelected(false);
//        restoreDbBtn.setEnabled(false);
//        backupDbBtn.setEnabled(false);
//        jButton1.setText("Select CPS File");

    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // quantification mode
        setActivateIdentification(false);
//        restoreDbBtn.setEnabled(false);
//        backupDbBtn.setEnabled(false);
        jRadioButton1.setSelected(false);
        jRadioButton5.setSelected(false);
//        jTextField2.setEnabled(false);
//        jButton1.setText("Select Uniprot Sequance File");
//        jButton1.setEnabled(true);
//        jTextField3.setEnabled(true);
//        jButton3.setText("Select CSV File");
//        jButton3.setEnabled(true);
//        idProcessBtn.setEnabled(true);
//        jTextField4.setEnabled(true);
//        jButton7.setEnabled(true);

    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton5ActionPerformed
        //db backup mode
//        setActivateIdentification(false);
        idPanel.setVisible(false);
        jPanel7.setVisible(false);
        dbPanel.setVisible(true);
        this.setSize(1150, 500);
        idProcessBtn.setVisible(false);

        jRadioButton4.setSelected(false);
        jRadioButton1.setSelected(false);
//        restoreDbBtn.setEnabled(true);
//        backupDbBtn.setEnabled(true);
//        jButton1.setEnabled(true);
//        jTextField3.setEnabled(true);
        sqlFileText.setText(backupFileUrl);
        mysqlFileRestText.setText(backupFileUrl);
//        jTextField4.setEnabled(true);
//        jTextField4.setText("///usr/bin/mysql ");
//        jButton3.setText("MySQL service path ");
//        jButton3.setEnabled(false);
//        jButton1.setText("Select SQL File");
    }//GEN-LAST:event_jRadioButton5ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField13ActionPerformed

    private void jTextField14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField14ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        BareBonesBrowserLaunch.openURL("http://www.stiftkgj.no/");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        BareBonesBrowserLaunch.openURL("http://www.uib.no/");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        BareBonesBrowserLaunch.openURL("http://probe.uib.no");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        jRadioButton3.setSelected(false);
        restoreDbBtn.setEnabled(true);
        restoreDbPanel.setVisible(true);
        backupDBPanel.setVisible(false);
        backupDbBtn.setEnabled(false);
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        jRadioButton2.setSelected(false);
        restoreDbBtn.setEnabled(false);
        restoreDbPanel.setVisible(false);
        backupDBPanel.setVisible(true);
        backupDbBtn.setEnabled(true);
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        JFileChooser chooser = new JFileChooser();
        int option = chooser.showOpenDialog(this); // parentComponent must a component like JFrame, JDialog...
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            mysqldumpText.setText(path);
        }


    }//GEN-LAST:event_jButton2ActionPerformed

    private void sqlFileTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sqlFileTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sqlFileTextActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        JFileChooser chooser = new JFileChooser();
        FileFilter sqlfilter = new FileFilter() {
            @Override
            public boolean accept(File myFile) {
                return myFile.getName().toLowerCase().endsWith("sql")
                        || myFile.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Supported formats: SQL (.sql)";
            }
        };

        chooser.addChoosableFileFilter(sqlfilter);

        int option = chooser.showOpenDialog(this); // parentComponent must a component like JFrame, JDialog...
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            sqlFileText.setText(path);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        JFileChooser chooser = new JFileChooser();

        int option = chooser.showOpenDialog(this); // parentComponent must a component like JFrame, JDialog...
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            mysqlresText.setText(path);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void mysqlFileRestTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mysqlFileRestTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mysqlFileRestTextActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        JFileChooser chooser = new JFileChooser();
        FileFilter sqlfilter = new FileFilter() {
            @Override
            public boolean accept(File myFile) {
                return myFile.getName().toLowerCase().endsWith("sql")
                        || myFile.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Supported formats: SQL (.sql)";
            }
        };

        chooser.addChoosableFileFilter(sqlfilter);

        int option = chooser.showOpenDialog(this); // parentComponent must a component like JFrame, JDialog...
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            mysqlFileRestText.setText(path);
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private final String errorEmptyText = "You need to enter valid value";
    private void jTextField15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField15ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed

        jLabel33.setText("");
        if (jTextField7.getText().isEmpty() || jTextField7.getText().trim().equalsIgnoreCase(errorEmptyText)) {
            jTextField7.setText(errorEmptyText);
            return;
        }
        if (jTextField15.getText().isEmpty() || jTextField15.getText().trim().equalsIgnoreCase(errorEmptyText)) {
            jTextField15.setText(errorEmptyText);
            return;
        }
        if (jTextField16.getText().isEmpty() || jTextField16.getText().trim().equalsIgnoreCase(errorEmptyText)) {
            jTextField16.setText(errorEmptyText);
            return;
        }
        try {
            Integer.valueOf(jTextField16.getText().trim());

        } catch (NumberFormatException nfe) {
            jTextField16.setText(errorEmptyText);
            return;
        }

        if (jTextField17.getText().isEmpty() || jTextField17.getText().trim().equalsIgnoreCase(errorEmptyText)) {
            jTextField17.setText(errorEmptyText);
            return;
        }
        if (exphandeler == null) {
            String password = "";
            for (char c : jPasswordField1.getPassword()) {
                password += c;
            }
            if (databaseNameField.getText() != null && !databaseNameField.getText().trim().equalsIgnoreCase("")) {
                database_name = databaseNameField.getText().trim();
            }
            try {
                exphandeler = new Handler("jdbc:mysql://" + jTextField13.getText() + ":3306/", database_name, "com.mysql.jdbc.Driver", jTextField1.getText(), password);
            } catch (SQLException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        boolean success = exphandeler.insertPublication(jTextField7.getText().trim(), jTextField15.getText().trim(), jTextField16.getText().trim(), jTextField17.getText().trim());
        if (success) {
            jTextField7.setText("");
            jTextField15.setText("");
            jTextField16.setText("");
            jTextField17.setText("");
            jLabel33.setText("done... :-) ");
        } else {
            jLabel33.setText("Error..try again");
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        if (exphandeler == null) {
            String password = "";
            for (char c : jPasswordField1.getPassword()) {
                password += c;
            }
            if (databaseNameField.getText() != null && !databaseNameField.getText().trim().equalsIgnoreCase("")) {
                database_name = databaseNameField.getText().trim();
            }
            try {
                exphandeler = new Handler("jdbc:mysql://" + jTextField13.getText() + ":3306/", database_name, "com.mysql.jdbc.Driver", jTextField1.getText(), password);
            } catch (SQLException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        if (diseaseGroupsFullNameFileTextFieald.getText() != null && !diseaseGroupsFullNameFileTextFieald.getText().trim().equalsIgnoreCase("")) {
            boolean test = exphandeler.updateDiseaseGroupsFullName(diseaseGroupsFullNameFileTextFieald.getText());
            if (!test) {
                jButton12ActionPerformed(evt);
            }
        } else {
            jButton12ActionPerformed(evt);
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        JFileChooser chooser = new JFileChooser("D:\\CSF-PR Quant data files\\02-02-2016");
        FileFilter txtfilter = new FileFilter() {
            @Override
            public boolean accept(File myFile) {
                return myFile.getName().toLowerCase().endsWith(".csv")
                        || myFile.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Supported formats: csv (.csv)";
            }
        };
        chooser.addChoosableFileFilter(txtfilter);

        int option = chooser.showOpenDialog(this); // parentComponent must a component like JFrame, JDialog...
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            diseaseGroupsFullNameFileTextFieald.setText(path);
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jTextField16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField16ActionPerformed

    private void jTextField17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField17ActionPerformed

    private void jTextField19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField19ActionPerformed

    private void databaseNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_databaseNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_databaseNameFieldActionPerformed

    private ReactomeDatabase reactomeDatabase;
    private void reactomeDatabaseBackupFormBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reactomeDatabaseBackupFormBtnActionPerformed
        if (reactomeDatabase == null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
            Date date = new Date();
            System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
            String password = "";
            for (char c : jPasswordField1.getPassword()) {
                password += c;
            }
            //"jdbc:mysql://" + jTextField13.getText() + ":3306/", database_name, "com.mysql.jdbc.Driver", jTextField1.getText(), password
            reactomeDatabase = new ReactomeDatabase("jdbc:mysql://localhost:3306/", "reactome_" + dateFormat.format(date), "com.mysql.jdbc.Driver", "root", password);

        }
        JFrame frame = new JFrame("update Reactome local Database");
        frame.setSize(450, 120);
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        final JPanel panel = new JPanel(null);
        panel.setSize(440, 80);
        frame.add(panel);
        frame.setVisible(true);
        panel.setLocation(10, 10);

        final JTextField inputpath = new JTextField();
        inputpath.setText("");
        inputpath.setToolTipText("Path to network graph file");
        inputpath.setSize(200, 30);
        inputpath.setLocation(10, 15);
        panel.add(inputpath);

        JButton chooseBtn = new JButton();
        chooseBtn.setText("Choose");
        chooseBtn.setSize(100, 30);
        chooseBtn.setLocation(220, 15);
        panel.add(chooseBtn);

        JButton processBtn = new JButton();
        processBtn.setText("Process");
        processBtn.setSize(100, 30);
        processBtn.setLocation(330, 15);
        panel.add(processBtn);

        final JLabel resultslabel = new JLabel();
        resultslabel.setText("");
        resultslabel.setSize(200, 20);
        resultslabel.setLocation(10, 50);
        panel.add(resultslabel);

        final JProgressBar progress = new JProgressBar();
        progress.setSize(210, 20);
        progress.setLocation(220, 50);
        progress.setIndeterminate(true);
        progress.setVisible(false);
        panel.add(progress);

        chooseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JFileChooser chooser = new JFileChooser("D:\\csf_db");
                FileFilter txtfilter = new FileFilter() {
                    @Override
                    public boolean accept(File myFile) {
                        return myFile.getName().toLowerCase().endsWith(".tsv")
                                || myFile.isDirectory();
                    }

                    @Override
                    public String getDescription() {
                        return "Supported formats: Tab-separated format (.tsv)";
                    }
                };
                chooser.addChoosableFileFilter(txtfilter);

                int option = chooser.showOpenDialog(GUI.this); // parentComponent must a component like JFrame, JDialog...
                if (option == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();
                    String path = selectedFile.getAbsolutePath();
                    inputpath.setText(path);
                }

            }
        });

        processBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String path = inputpath.getText();
                inputpath.setForeground(Color.BLACK);
                if (path == null || path.trim().equalsIgnoreCase("") || !path.toLowerCase().endsWith(".tsv")) {
                    resultslabel.setForeground(Color.RED);
                    inputpath.setText("File not valid");
                    resultslabel.setText("Please choose file");

                } else {
                    resultslabel.setForeground(Color.BLACK);
                    resultslabel.setText("Reactome database updating");
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            progress.setVisible(true);
                        }
                    });
                    t.start();

                    try {
                        t.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    Thread t1 = new Thread(new Runnable() {
                        public void run() {
                            boolean success = reactomeDatabase.updateDatabase(inputpath.getText());
                            if (success) {
                                resultslabel.setForeground(Color.GREEN.darker());
                                resultslabel.setText("Successfuly updated");
                            } else {
                                resultslabel.setForeground(Color.RED);
                                resultslabel.setText("Error..check your File");

                            }
                            progress.setVisible(false);
                        }
                    }
                    );
                    t1.start();
//                    start to process

//                    progress.setValue(0);
                }

            }
        });


    }//GEN-LAST:event_reactomeDatabaseBackupFormBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>


        /* Create and display the form */
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI gui = new GUI();
                gui.setVisible(true);
            }
        });
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                GUI gui = new GUI();
//                gui.setVisible(true);
//            }
//        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backupDBPanel;
    private javax.swing.JButton backupDbBtn;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JTextField databaseNameField;
    private javax.swing.JPanel dbPanel;
    private javax.swing.JTextField diseaseGroupsFullNameFileTextFieald;
    private javax.swing.JPanel idPanel;
    private javax.swing.JButton idProcessBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTextField mysqlFileRestText;
    private javax.swing.JTextField mysqldumpText;
    private javax.swing.JTextField mysqlresText;
    private com.view.QuantPanel quantPanel;
    private javax.swing.JButton reactomeDatabaseBackupFormBtn;
    private javax.swing.JButton restoreDbBtn;
    private javax.swing.JPanel restoreDbPanel;
    private javax.swing.JTextField sqlFileText;
    // End of variables declaration//GEN-END:variables

    @Override
    public void cancelProgress() {
        System.exit(0);
    }

    private ExperimentBean initExperiment() {

        ExperimentBean newExp = new ExperimentBean();
        newExp.setExpType(1);
        String expName = jTextField2.getText();
        String expSpecies = jTextField5.getText();
        String expSampleType = jTextField9.getText();
        String expSampleProcessing = jTextField10.getText();
        String expInstrumentType = jTextField11.getText();
        String expFragMode = jTextField8.getText();
        String expUploadedByName = "Admin";//jTextField7.getText();
        String expEmail = jTextField12.getText();
        String expPublicationLink = jTextField6.getText();
        String expDescription = jTextArea1.getText();

        newExp.setName(expName);
        newExp.setSpecies(expSpecies);
        newExp.setSampleType(expSampleType);
        newExp.setSampleProcessing(expSampleProcessing);
        newExp.setInstrumentType(expInstrumentType);
        newExp.setFragMode(expFragMode);
        newExp.setUploadedByName(expUploadedByName);
        newExp.setEmail(expEmail);
        newExp.setPublicationLink(expPublicationLink);
        newExp.setExpId(-1);
        newExp.setDescription(expDescription);

        return newExp;
    }

    private void cleanFields() {
        jTextField1.setForeground(Color.black);
        jTextField13.setForeground(Color.black);
        jPasswordField1.setForeground(Color.black);
        jTextField2.setForeground(Color.black);
        jTextField2.setText("");
        jTextField3.setForeground(Color.black);
        jTextField3.setText("");
        jTextField4.setText("");
        jTextArea1.setForeground(Color.black);
        jTextArea1.setText("");
        jTextField5.setForeground(Color.black);
        jTextField5.setText("");
        jTextField6.setText("");
//        jTextField7.setForeground(Color.black);
        jTextField8.setForeground(Color.black);
        jTextField8.setText("");
        jTextField11.setForeground(Color.black);
        jTextField11.setText("");
        jTextField9.setForeground(Color.black);
        jTextField9.setText("");
        jTextField10.setForeground(Color.black);
        jTextField10.setText("");
        jTextField12.setForeground(Color.black);
        jTextField14.setText("");
        jTextField14.setForeground(Color.black);

    }

    private boolean valiField() {
        boolean valid = true;
        String errorMessage = "This Field Can Not Be Empty";
        String errorName = "Not Valid Name Please choose Another Name";
        jTextField1.setForeground(Color.black);
        jPasswordField1.setForeground(Color.black);
        jTextField2.setForeground(Color.black);
        jTextField3.setForeground(Color.black);
        jTextArea1.setForeground(Color.black);
        jTextField5.setForeground(Color.black);
//        jTextField7.setForeground(Color.black);
        jTextField8.setForeground(Color.black);
        jTextField11.setForeground(Color.black);
        jTextField9.setForeground(Color.black);
        jTextField10.setForeground(Color.black);
        jTextField12.setForeground(Color.black);
        jTextField13.setForeground(Color.black);
        jTextField14.setForeground(Color.black);
        if (jTextField1.getText() == null || jTextField1.getText().equals("") || jTextField1.getText().equalsIgnoreCase(errorMessage)) {
            valid = false;
            jTextField1.setForeground(Color.red);
            jTextField1.setText(errorMessage);
        } else if (jTextField2.getText() == null || jTextField2.getText().equals("") || jTextField2.getText().equalsIgnoreCase(errorMessage) || jTextField2.getText().equalsIgnoreCase(errorName)) {
            valid = false;
            jTextField2.setForeground(Color.red);
            if (jTextField2.getText().equalsIgnoreCase(errorName)) {
                jTextField2.setText(errorName);
            } else {
                jTextField2.setText(errorMessage);
            }
        } else if (jTextArea1.getText() == null || jTextArea1.getText().equals("") || jTextArea1.getText().equalsIgnoreCase(errorMessage)) {
            valid = false;
            jTextArea1.setForeground(Color.red);
            jTextArea1.setText(errorMessage);
        } else if (jTextField8.getText() == null || jTextField8.getText().equals("") || jTextField8.getText().equalsIgnoreCase(errorMessage)) {
            valid = false;
            jTextField8.setForeground(Color.red);
            jTextField8.setText(errorMessage);
        } else if (jTextField13.getText() == null || jTextField13.getText().equals("") || jTextField13.getText().equalsIgnoreCase(errorMessage)) {
            valid = false;
            jTextField13.setForeground(Color.red);
            jTextField13.setText(errorMessage);

        } else if (jTextField5.getText() == null || jTextField5.getText().equals("") || jTextField5.getText().equalsIgnoreCase(errorMessage)) {
            valid = false;
            jTextField5.setForeground(Color.red);
            jTextField5.setText(errorMessage);
        } else if (jTextField11.getText() == null || jTextField11.getText().equals("") || jTextField11.getText().equalsIgnoreCase(errorMessage)) {
            valid = false;
            jTextField11.setForeground(Color.red);
            jTextField11.setText(errorMessage);
        } else if (jTextField9.getText() == null || jTextField9.getText().equals("") || jTextField9.getText().equalsIgnoreCase(errorMessage)) {
            valid = false;
            jTextField9.setForeground(Color.red);
            jTextField9.setText(errorMessage);
        } else if (jTextField10.getText() == null || jTextField10.getText().equals("") || jTextField10.getText().equalsIgnoreCase(errorMessage)) {
            valid = false;
            jTextField10.setForeground(Color.red);
            jTextField10.setText(errorMessage);
        } else if (jTextField12.getText() == null || jTextField12.getText().equals("") || jTextField12.getText().equalsIgnoreCase(errorMessage)) {
            valid = false;
            jTextField12.setForeground(Color.red);
            jTextField12.setText(errorMessage);
//        } else if (jTextField7.getText() == null || jTextField7.getText().equals("") || jTextField7.getText().equalsIgnoreCase(errorMessage)) {
//            valid = false;
//            jTextField7.setForeground(Color.red);
//            jTextField7.setText(errorMessage);
        } else if (jTextField3.getText() == null || jTextField3.getText().equals("") || jTextField3.getText().equalsIgnoreCase(errorMessage)) {
            valid = false;
            jTextField3.setForeground(Color.red);
            jTextField3.setText(errorMessage);
        } else if (jTextField14.getText() == null || jTextField14.getText().equals("") || jTextField14.getText().equalsIgnoreCase(errorMessage)) {
            valid = false;

            jTextField14.setForeground(Color.red);
            jTextField14.setText(errorMessage);
        } else {
            valid = true;
            File localResourcefolder = new File(jTextField14.getText(), "resources");
            if (localResourcefolder.exists()) {
                for (File f : localResourcefolder.listFiles()) {
                    if (f.getName().equalsIgnoreCase("matches")) {
                        for (File f2 : f.listFiles()) {
                            f2.delete();
                        }
                    }
                }

            } else {
                valid = false;
                jTextField14.setForeground(Color.red);
                jTextField14.setText(errorMessage);
            }
        }
        if (valid) {
            try {
                String password = "";
                for (char c : jPasswordField1.getPassword()) {
                    password += c;
                }
                if (databaseNameField.getText() != null && !databaseNameField.getText().trim().equalsIgnoreCase("")) {
                    database_name = databaseNameField.getText().trim();
                }
                exphandeler = new Handler("jdbc:mysql://" + jTextField13.getText() + ":3306/", database_name, "com.mysql.jdbc.Driver", jTextField1.getText(), password);
            } catch (SQLException sqlE) {
                valid = false;
                jTextField13.setText("Please check Database Params");
                jTextField13.setForeground(Color.red);
                return valid;
            }
            try {
                boolean checkName = exphandeler.checkName(jTextField2.getText());
                if (!checkName) {
                    System.out.println("database conn is faild");
                    valid = false;
                    jTextField2.setForeground(Color.red);
                    jTextField2.setText(errorName);
                }
            } catch (SQLException sqlExp) {
                jPasswordField1.setForeground(Color.red);
                valid = false;
            }
        }

        return valid;
    }

    private void setActivateIdentification(boolean activate) {
//        jLabel3.setText("Experiment Name:");
//        jButton3.setText("Selecet Glycopeptide File");
//        idProcessBtn.setEnabled(activate);
//        jButton1.setEnabled(activate);
//        jButton3.setEnabled(activate);
//        jCheckBox1.setEnabled(activate);
//        jButton7.setEnabled(activate);
//        jTextArea1.setEnabled(activate);
//        jTextField10.setEnabled(activate);
//        jTextField11.setEnabled(activate);
//        jTextField12.setEnabled(activate);
//        jTextField14.setEnabled(activate);
//        jTextField2.setEnabled(activate);
//        jTextField3.setEnabled(activate);
//        jTextField4.setEnabled(activate);
//        jTextField5.setEnabled(activate);
//        jTextField6.setEnabled(activate);
//        jTextField8.setEnabled(activate);
//        jTextField9.setEnabled(activate);
        if (activate) {
            this.setSize(1150, 710);

        } else {
            this.setSize(1150, 710);
        }
        idProcessBtn.setVisible(activate);
        idPanel.setVisible(activate);
        dbPanel.setVisible(false);
        jPanel7.setVisible(!activate);

    }

    class RunWorkerIDReader extends SwingWorker<Boolean, Boolean> {

        private final PSFileImporter fileImporter;
        private final File cpsFile;
        private final String resFold;

        public RunWorkerIDReader(PSFileImporter fileImporter, File cpsFile, String resFold) {
            this.fileImporter = fileImporter;
            this.cpsFile = cpsFile;
            this.resFold = resFold;
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            jLabel13.setText("Importing Data....");
            fileImporter.importPeptideShakerFile(cpsFile, resFold);
            DataHandler dataHandler = new DataHandler();
            exp = dataHandler.handelPeptideShakerProjectData(fileImporter, exp, jLabel13, jCheckBox1.isSelected());
            if (jTextField4.getText() != null && !jTextField4.getText().equalsIgnoreCase("")) {
                jLabel13.setText("Adding Glycopeptides Data....");
                File glycopeptide = new File(jTextField4.getText());
                exp = dataHandler.addGlicoPep(glycopeptide, exp);
            }
            jLabel13.setText("Start Storing Data....");
            boolean test = exphandeler.handelPeptideShakerProject(exp);
            if (test) {
                jLabel13.setText("Done!");
                if (resourcefolder.exists()) {
                    for (File f : resourcefolder.listFiles()) {
                        if (f.getName().equalsIgnoreCase("matches")) {
                            for (File f2 : f.listFiles()) {
                                f2.delete();
                            }
                        }
                    }

                }
                Thread.sleep(10000);
                System.gc();
                System.exit(0);
            } else {
                jLabel13.setText("Failed :(");
                if (resourcefolder.exists()) {
                    for (File f : resourcefolder.listFiles()) {
                        if (f.getName().equalsIgnoreCase("matches")) {
                            for (File f2 : f.listFiles()) {
                                f2.delete();
                            }
                        }
                    }

                }
            }
            System.gc();
            return test;
        }
    }

    class RunWorkerRestoreDB extends SwingWorker<Boolean, Boolean> {

        public RunWorkerRestoreDB() {

        }
        private boolean success;

        @Override
        protected Boolean doInBackground() throws Exception {
            // TODO add your handling code here:
            if (exphandeler == null) {
                try {
                    String password = "";
                    for (char c : jPasswordField1.getPassword()) {
                        password += c;
                    }
                    if (databaseNameField.getText() != null && !databaseNameField.getText().trim().equalsIgnoreCase("")) {
                        database_name = databaseNameField.getText().trim();
                    }
                    exphandeler = new Handler("jdbc:mysql://" + jTextField13.getText() + ":3306/", database_name, "com.mysql.jdbc.Driver", jTextField1.getText(), password);
                } catch (SQLException sqlE) {
                    System.out.println("at error SQL Exception line 896 " + this.getClass().getName() + "  " + sqlE.getMessage());
                }
            }
            final String mysqldumpUrl = mysqlresText.getText();
            final String sqlFileUrl = mysqlFileRestText.getText();
            if (mysqldumpUrl == null || mysqldumpUrl.equalsIgnoreCase("") || sqlFileUrl == null || sqlFileUrl.equalsIgnoreCase("")) {
                System.err.println("select file");

            } else {
                try {

                    restoreDbBtn.setEnabled(false);
                    success = exphandeler.restoreDB(sqlFileUrl, mysqldumpUrl);//"C:\\Users\\y-mok_000\\Google Drive\\csf-pr-backup\\backup.sql");
                    if (!success) {
                        jProgressBar1.setVisible(false);
                        jLabel13.setText("Faild to restore the DB");

                    }
                    if (!jLabel13.getText().equalsIgnoreCase("Faild to restore the DB")) {
                        jLabel13.setText("Done");
                    }
                    jProgressBar1.setVisible(false);
                    restoreDbBtn.setEnabled(true);

                } catch (Exception e) {
                    //   e.printStackTrace();
                }

            }

            return success;
        }
    }

    class RunWorkerBackupDB extends SwingWorker<Boolean, Boolean> {

        public RunWorkerBackupDB() {

        }
        private boolean success;

        @Override
        protected Boolean doInBackground() throws Exception {
            // TODO add your handling code here:
            if (exphandeler == null) {
                try {
                    String password = "";
                    for (char c : jPasswordField1.getPassword()) {
                        password += c;
                    }
                    if (databaseNameField.getText() != null && !databaseNameField.getText().trim().equalsIgnoreCase("")) {
                        database_name = databaseNameField.getText().trim();
                    }
                    System.out.println("database name is " + database_name);
                    exphandeler = new Handler("jdbc:mysql://" + jTextField13.getText() + ":3306/", database_name, "com.mysql.jdbc.Driver", jTextField1.getText(), password);
                } catch (SQLException sqlE) {
                    System.out.println("at error SQL Exception line 896 " + this.getClass().getName() + "  " + sqlE.getMessage());
                }
            }
            processUrl = mysqldumpText.getText().trim();
            if (processUrl == null || processUrl.trim().equalsIgnoreCase("") || sqlFileText.getText() == null || sqlFileText.getText().trim().equalsIgnoreCase("")) {
                jLabel13.setText("Error please check input data");
                return false;
            } else {
                try {

                    backupDbBtn.setEnabled(false);
                    success = exphandeler.exportDataBase(processUrl, sqlFileText.getText());

                    if (!success) {
                        jProgressBar1.setVisible(false);
                        jLabel13.setText("Faild to backup the DB");

                    }
                    if (!jLabel13.getText().equalsIgnoreCase("Faild to restore the DB")) {
                        jLabel13.setText("Back up compleate...Done");
                    }
                    jProgressBar1.setVisible(false);
                    backupDbBtn.setEnabled(true);

                } catch (Exception e) {
                }

            }

            return success;
        }
    }

    class RunWorkerQuantProcess extends SwingWorker<Boolean, Boolean> {

        public RunWorkerQuantProcess() {

        }
        private boolean success;

        @Override
        protected Boolean doInBackground() throws Exception {
            // TODO add your handling code here:
            if (exphandeler == null) {
                try {
                    String password = "";
                    for (char c : jPasswordField1.getPassword()) {
                        password += c;
                    }
                    if (databaseNameField.getText() != null && !databaseNameField.getText().trim().equalsIgnoreCase("")) {
                        database_name = databaseNameField.getText().trim();
                    }
                    exphandeler = new Handler("jdbc:mysql://" + jTextField13.getText() + ":3306/", database_name, "com.mysql.jdbc.Driver", jTextField1.getText(), password);
                } catch (SQLException sqlE) {
                    System.out.println("at error SQL Exception line 896 " + this.getClass().getName() + "  " + sqlE.getMessage());
                }
            }

            success = exphandeler.handelQuantPubData(quantPanel.getQuantCsvTextField().getText(), quantPanel.getjTextField4().getText(), quantPanel.getUnreviewedFilePath());

            try {

                quantPanel.getProcessBtn().setEnabled(false);

                if (!success) {
                    jProgressBar1.setVisible(false);
                    jLabel13.setText("Error in data storing ... check the input file");

                }
                if (!jLabel13.getText().equalsIgnoreCase("Error in data storing ... check the input file")) {
                    jLabel13.setText("Done :-D ");
                }
                jProgressBar1.setVisible(false);
                quantPanel.getProcessBtn().setEnabled(true);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return success;
        }
    }

}
