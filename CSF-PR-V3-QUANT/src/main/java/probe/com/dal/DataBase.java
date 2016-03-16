package probe.com.dal;

import com.vaadin.server.Page;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import probe.com.model.beans.OverviewInfoBean;

import probe.com.model.beans.identification.IdentificationDatasetBean;
import probe.com.model.beans.identification.IdentificationFractionBean;
import probe.com.model.beans.identification.IdentificationPeptideBean;
import probe.com.model.beans.identification.IdentificationProteinBean;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.model.beans.identification.StandardIdentificationFractionPlotProteinBean;
import probe.com.model.beans.quant.QuantDatasetInitialInformationObject;
import probe.com.model.beans.quant.QuantPeptide;
import probe.com.model.beans.User;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.popupcomponents.DatasetInformationOverviewLayout;

/**
 * @author Yehia Farag
 *
 * abstraction for database queries
 */
public class DataBase implements Serializable {

    private static final long serialVersionUID = 1L;
    private Connection conn = null;
    private Connection conn_i = null;
    private final String url, dbName, driver, userName, password;
    private final DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
    private DecimalFormat df = null;

    /**
     * Initialize Database abstraction layer
     *
     * @param url database url
     * @param dbName database name
     * @param driver database driver
     * @param userName database username
     * @param password database password
     *
     */
    public DataBase(String url, String dbName, String driver, String userName, String password) {
        this.url = url;
        this.dbName = dbName;
        this.driver = driver;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Create database tables if not exist
     *
     * @return test boolean (successful process)
     *
     */
    public boolean cleanUnusedDatabases()//create CSF the database tables if not exist
    {
        try {
            if (conn_i == null || conn_i.isClosed()) {
                Class.forName(driver).newInstance();
                conn_i = DriverManager.getConnection(url + "mysql", userName, password);
            }
            Statement statement = conn_i.createStatement();

            String sqlDataBase = "SHOW DATABASES ;";
            ResultSet rs1 = statement.executeQuery(sqlDataBase);
            Set<String> datasetnames = new HashSet<String>();
            while (rs1.next()) {
                String db = rs1.getString("Database");
                datasetnames.add(db);
                System.err.println("db is " + db);

            }
            Statement statement2 = conn_i.createStatement();

            for (String db : datasetnames) {
                if (db.contains("csf") && !db.equals(dbName)) {
//                    statement2.executeUpdate("DROP DATABASE " + db + " ;");
                }

            }
            conn_i.close();
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 102 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error line 104 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error line 106 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error line 108 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        }

        return true;
    }

    /**
     * Get the available identification datasets
     *
     * @return identification datasetsList
     */
    public Map<Integer, IdentificationDatasetBean> getIdentificationDatasetsList() {
        PreparedStatement selectDatasetListStat;
        Map<Integer, IdentificationDatasetBean> datasetList = new HashMap<Integer, IdentificationDatasetBean>();
        Map<Integer, IdentificationDatasetBean> tempDatasetList = new HashMap<Integer, IdentificationDatasetBean>();
        String selectselectDatasetList = "SELECT * FROM `experiments_table` ;";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectDatasetListStat = conn.prepareStatement(selectselectDatasetList);
            ResultSet rs = selectDatasetListStat.executeQuery();
            while (rs.next()) {
                IdentificationDatasetBean dataset = new IdentificationDatasetBean();
                int fractionsNumber = rs.getInt("fractions_number");
                dataset.setFractionsNumber(fractionsNumber);
                String uploadedBy = rs.getString("uploaded_by");
                dataset.setUploadedByName(uploadedBy);
                String name = updateStringFormat(rs.getString("name"));
                dataset.setName(name);
                String species = rs.getString("species");
                dataset.setSpecies(species);
                String sampleType = rs.getString("sample_type");
                dataset.setSampleType(sampleType);
                String sampleProcessing = rs.getString("sample_processing");
                dataset.setSampleProcessing(sampleProcessing);
                String instrumentType = rs.getString("instrument_type");
                dataset.setInstrumentType(instrumentType);
                String fragMode = rs.getString("frag_mode");
                dataset.setFragMode(fragMode);
                int proteinsNumber = rs.getInt("proteins_number");
                dataset.setProteinsNumber(proteinsNumber);
                String email = rs.getString("email");
                dataset.setEmail(email);
                String publicationLink = rs.getString("pblication_link");
                dataset.setPublicationLink(publicationLink);
                int peptidesInclude = rs.getInt("peptide_file");
                dataset.setPeptidesInclude(peptidesInclude);
                int peptidesNumber = rs.getInt("peptides_number");
                dataset.setPeptidesNumber(peptidesNumber);
                int datasetId = rs.getInt("exp_id");
                dataset.setDatasetId(datasetId);
                String desc = rs.getString("description");
                dataset.setDescription(desc);
                int datasetType = rs.getInt("exp_type");
                dataset.setDatasetType(datasetType);
                dataset.setNumberValidProt(rs.getInt("valid_prot"));
                datasetList.put(dataset.getDatasetId(), dataset);
            }
            rs.close();

            Map<Integer, List<Integer>> datasetFractionsMap = this.getFullIdentificationProteinFractionIdsList();

            for (int datasetId : datasetList.keySet()) {
                IdentificationDatasetBean dataset = datasetList.get(datasetId);
                if (datasetFractionsMap.containsKey(datasetId)) {
                    List<Integer> fractionIds = datasetFractionsMap.get(datasetId);
                    dataset.setFractionIds(fractionIds);
                }
                tempDatasetList.put(datasetId, dataset);

            }
            datasetList.clear();
            datasetList.putAll(tempDatasetList);
            tempDatasetList.clear();

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 987 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException e) {
            System.err.println("at error line 992 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return null;
        } catch (InstantiationException e) {
            System.err.println("at error line 994 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return null;
        } catch (SQLException e) {
            System.err.println("at error line 996 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return null;
        }
        System.gc();
        return datasetList;

    }

    /**
     * Get selected identification dataset
     *
     * @param datasetId
     * @return dataset
     */
    public IdentificationDatasetBean retriveIdentficationDataset(int datasetId) {
        IdentificationDatasetBean dataset = new IdentificationDatasetBean();
        dataset.setDatasetId(datasetId);
        dataset = this.getIdentificationDatasetDetails(dataset);
        System.gc();
        return dataset;
    }

    /**
     * Get identification fractions Ids list for a dataset
     *
     * @return list of fraction datasets map
     */
    private Map<Integer, List<Integer>> getFullIdentificationProteinFractionIdsList() {
        PreparedStatement selectDatasetFractionStat;
        String selectDatasetFraction = "SELECT `fraction_id` , `exp_id` FROM `experiment_fractions_table` ;";

        Map<Integer, List<Integer>> datasetFractionsMap = new HashMap<Integer, List<Integer>>();
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectDatasetFractionStat = conn.prepareStatement(selectDatasetFraction);
            ResultSet rs = selectDatasetFractionStat.executeQuery();
            while (rs.next()) {
                int dsId = rs.getInt("exp_id");
                if (!datasetFractionsMap.containsKey(dsId)) {
                    List<Integer> fractionList = new ArrayList<Integer>();
                    datasetFractionsMap.put(dsId, fractionList);

                }
                List<Integer> fractionList = datasetFractionsMap.get(dsId);
                fractionList.add(rs.getInt("fraction_id"));
                datasetFractionsMap.put(dsId, fractionList);

            }
            rs.close();

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 1045 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error line 1047 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error line 1049 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error line 1053 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }
        return datasetFractionsMap;
    }

    /**
     * Get identification dataset details
     *
     * @param dataset
     * @return list of fraction Id's list
     */
    private IdentificationDatasetBean getIdentificationDatasetDetails(IdentificationDatasetBean dataset) {
        PreparedStatement selectDatasetStat;
        String selectDataset = "SELECT * FROM `experiments_table` WHERE `exp_id`=? ;";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectDatasetStat = conn.prepareStatement(selectDataset);
            selectDatasetStat.setInt(1, dataset.getDatasetId());
            ResultSet rs = selectDatasetStat.executeQuery();
            while (rs.next()) {
                int fractionsNumber = rs.getInt("fractions_number");
                dataset.setFractionsNumber(fractionsNumber);
                String uploadedBy = rs.getString("uploaded_by");
                dataset.setUploadedByName(uploadedBy);
                String name = rs.getString("name");
                dataset.setName(name);
                String species = rs.getString("species");
                dataset.setSpecies(species);
                String sampleType = rs.getString("sample_type");
                dataset.setSampleType(sampleType);
                String sampleProcessing = rs.getString("sample_processing");
                dataset.setSampleProcessing(sampleProcessing);
                String instrumentType = rs.getString("instrument_type");
                dataset.setInstrumentType(instrumentType);
                String fragMode = rs.getString("frag_mode");
                dataset.setFragMode(fragMode);
                int proteinsNumber = rs.getInt("proteins_number");
                dataset.setProteinsNumber(proteinsNumber);
                String email = rs.getString("email");
                dataset.setEmail(email);
                String publicationLink = rs.getString("pblication_link");
                dataset.setPublicationLink(publicationLink);
                int peptidesInclude = rs.getInt("peptide_file");
                dataset.setPeptidesInclude(peptidesInclude);
                int peptidesNumber = rs.getInt("peptides_number");
                dataset.setPeptidesNumber(peptidesNumber);
                String dec = rs.getString("description");
                dataset.setDescription(dec);
                int DatasetType = rs.getInt("exp_type");
                dataset.setDatasetType(DatasetType);
                dataset.setNumberValidProt(rs.getInt("valid_prot"));
            }
            rs.close();

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 1112 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error line 1115 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error line 1118 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error line 1121 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }
        return dataset;

    }

    /**
     * Get identification dataset fractions list
     *
     * @param datasetId
     * @param accession
     * @param otherAccession
     * @return fractions list for the selected dataset
     */
    public Map<Integer, IdentificationProteinBean> getIdentificationProteinsGelFractionsList(int datasetId, String accession, String otherAccession) {
        try {
            PreparedStatement selectFractsStat;
            String selectFract = "SELECT * FROM `fractions_table` where `exp_id` = ? AND `prot_accession` = ?  ORDER BY `fraction_id`";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectFractsStat = conn.prepareStatement(selectFract);
            selectFractsStat.setInt(1, datasetId);
            selectFractsStat.setString(2, accession.toUpperCase() + "," + otherAccession.toUpperCase());
            ResultSet rs = selectFractsStat.executeQuery();
            Map<Integer, IdentificationProteinBean> proteinList = new HashMap<Integer, IdentificationProteinBean>();
            otherSymbols.setGroupingSeparator('.');
            df = new DecimalFormat("#.##", otherSymbols);

            int x = 0;
            while (rs.next()) {
                IdentificationProteinBean pb = new IdentificationProteinBean();//fraction_id		  			
                pb.setAccession(rs.getString("prot_accession"));
                pb.setNumberOfPeptidePerFraction(rs.getInt("number_peptides"));
                pb.setNumberOfSpectraPerFraction(rs.getInt("number_spectra"));
                pb.setAveragePrecursorIntensityPerFraction(Double.valueOf(df.format(rs.getDouble("average_ precursor_intensity"))));
                pb.setFrcationId(rs.getInt("fraction_id"));
                proteinList.put(x++, pb);
            }
            rs.close();
            conn.close();
            return proteinList;

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 1166 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error line 1170 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error line 1173 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (NumberFormatException e) {
            System.err.println("at error line 1176 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            System.err.println("at error line 1177 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error line 1180 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }

        System.gc();
        return null;

    }

    /**
     * Get identification peptides list for specific identification dataset
     *
     * @param identificationDatasetId
     * @param valid
     * @return dataset peptide List
     */
    @SuppressWarnings("SleepWhileInLoop")
    public Map<Integer, IdentificationPeptideBean> getAllIdentificationDatasetPeptidesList(int identificationDatasetId, boolean valid) {
        Map<Integer, IdentificationPeptideBean> identificationPeptidesList = null;
        try {
            //get fractions id list
            PreparedStatement selectPeptideListStat;
            String selectPeptideList;
            if (valid) {
                selectPeptideList = "SELECT * FROM `proteins_peptides_table` WHERE `exp_id`= ? AND `validated` =1;";
            } else {
                selectPeptideList = "SELECT * FROM `proteins_peptides_table` WHERE `exp_id`= ?;";
            }
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectPeptideListStat = conn.prepareStatement(selectPeptideList);
            selectPeptideListStat.setInt(1, identificationDatasetId);
            ResultSet rs = selectPeptideListStat.executeQuery();
            identificationPeptidesList = fillPeptideInformation(rs);
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 1217 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error line 1220 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error line 1223 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error line 1226 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }
        System.gc();
        return identificationPeptidesList;
    }

    /**
     * Get all peptides number for specific dataset
     *
     * @param datasetId
     * @param validated
     * @return identification peptides number
     */
    public int getAllIdentificationDatasetPeptidesNumber(int datasetId, boolean validated) {

        try {
            PreparedStatement selectPeptideListStat;
            String selectPeptideList;
            if (validated) {
                selectPeptideList = "SELECT * FROM `proteins_peptides_table` WHERE `exp_id`= ? AND `validated` =1;";
            } else {
                selectPeptideList = "SELECT * FROM `proteins_peptides_table` WHERE `exp_id`= ?;";
            }
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectPeptideListStat = conn.prepareStatement(selectPeptideList);
            selectPeptideListStat.setInt(1, datasetId);
            ResultSet rs = selectPeptideListStat.executeQuery();
            return rs.getRow();

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 1260 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error line 1263 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error line 1266 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error line 1269 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }
        System.gc();
        return 0;
    }

    /**
     * Get identification proteins map for especial dataset
     *
     * @param datasetId
     * @return identification proteins list
     */
    public Map<String, IdentificationProteinBean> getIdentificationProteinsList(int datasetId) {
        Map<String, IdentificationProteinBean> proteinDatasetList = new HashMap<String, IdentificationProteinBean>();
        try {
            PreparedStatement selectProtDatasetStat;
            String selectProtDataset = "SELECT * FROM `experiment_protein_table` WHERE `exp_id`=? ;";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProtDatasetStat = conn.prepareStatement(selectProtDataset);
            selectProtDatasetStat.setInt(1, datasetId);
            ResultSet rs = selectProtDatasetStat.executeQuery();
            while (rs.next()) {
                IdentificationProteinBean temPb = new IdentificationProteinBean();
                temPb.setAccession(rs.getString("prot_accession"));
                temPb.setOtherProteins(rs.getString("other_protein(s)"));
                temPb.setProteinInferenceClass(rs.getString("protein_inference_class"));
                temPb.setSequenceCoverage(rs.getDouble("sequence_coverage(%)"));
                temPb.setObservableCoverage(rs.getDouble("observable_coverage(%)"));
                temPb.setConfidentPtmSites(rs.getString("confident_ptm_sites"));
                temPb.setNumberConfident(rs.getString("number_confident"));
                temPb.setOtherPtmSites(rs.getString("other_ptm_sites"));
                temPb.setNumberOfOther(rs.getString("number_other"));
                temPb.setNumberValidatedPeptides(rs.getInt("number_validated_peptides"));
                temPb.setNumberValidatedSpectra(rs.getInt("number_validated_spectra"));
                temPb.setEmPai(rs.getDouble("em_pai"));
                temPb.setNsaf(rs.getDouble("nsaf"));
                temPb.setMw_kDa(rs.getDouble("mw_(kDa)"));
                temPb.setScore(rs.getDouble("score"));
                temPb.setConfidence(rs.getDouble("confidence"));
                temPb.setStarred(Boolean.valueOf(rs.getString("starred")));
                temPb.setNonEnzymaticPeptides(Boolean.valueOf(rs.getString("non_enzymatic_peptides").toUpperCase()));
                temPb.setSpectrumFractionSpread_lower_range_kDa(rs.getString("spectrum_fraction_spread_lower_range_kDa"));
                temPb.setSpectrumFractionSpread_upper_range_kDa(rs.getString("spectrum_fraction_spread_upper_range_kDa"));
                temPb.setPeptideFractionSpread_lower_range_kDa(rs.getString("peptide_fraction_spread_lower_range_kDa"));
                temPb.setPeptideFractionSpread_upper_range_kDa(rs.getString("peptide_fraction_spread_upper_range_kDa"));

                temPb.setGeneName(rs.getString("gene_name"));
                temPb.setChromosomeNumber(rs.getString("chromosome_number"));

                temPb.setValidated(Boolean.valueOf(rs.getString("valid")));
                temPb.setDescription(rs.getString("description").trim());
                if (temPb.getOtherProteins() == null || temPb.getOtherProteins().equals("")) {
                    proteinDatasetList.put(temPb.getAccession(), temPb);
                } else {
                    proteinDatasetList.put(temPb.getAccession() + "," + temPb.getOtherProteins(), temPb);
                }
            }
            rs.close();

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 1333 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error line 1336 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error line 1339 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error line 1340 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }
        System.gc();
        return proteinDatasetList;
    }

    /**
     * Remove identification dataset from the database
     *
     * @param datasetId
     * @return boolean successful process
     */
    public boolean removeIdentificationDataset(int datasetId) {

        PreparedStatement remDatasetStat;
        PreparedStatement getFractDatasetStat;
        PreparedStatement remFractStat;
        PreparedStatement remFractDatasetStat;
        PreparedStatement getPepDatasetStat;
        PreparedStatement remPepDatasetStat;
        PreparedStatement remPeptStat;
        PreparedStatement remProtStat;

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            this.removeIdentificationStandarPlotProteins(datasetId);
            String remDataset = "DELETE FROM `experiments_table`  WHERE  `exp_id`=? ";

            String remFract = "DELETE FROM `" + dbName + "`.`fractions_table`   WHERE  `fraction_id` =? ";

            remDatasetStat = conn.prepareStatement(remDataset);
            remDatasetStat.setInt(1, datasetId);
            remDatasetStat.executeUpdate();

            String selectPeptideList = "SELECT `pep_id` FROM `experiment_peptides_table` WHERE `exp_id` = ?;";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            getPepDatasetStat = conn.prepareStatement(selectPeptideList);
            getPepDatasetStat.setInt(1, datasetId);
            ResultSet rs = getPepDatasetStat.executeQuery();
            ArrayList<Integer> peptideIdList = new ArrayList<Integer>();
            while (rs.next()) {
                int peptideId = rs.getInt("pep_id");
                peptideIdList.add(peptideId);

            }

            rs.close();
            String selectPeptide = "DELETE FROM `proteins_peptides_table` WHERE  `peptide_id`=? ;";

            for (int pepId : peptideIdList) {

                if (conn == null || conn.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                }
                remPeptStat = conn.prepareStatement(selectPeptide);
                remPeptStat.setInt(1, pepId);
                remPeptStat.executeUpdate();

            }

            String removePeptide = "DELETE FROM `experiment_peptides_table` WHERE  `exp_id` = ? ;";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            remPepDatasetStat = conn.prepareStatement(removePeptide);
            remPepDatasetStat.setInt(1, datasetId);
            remPepDatasetStat.executeUpdate();

            String remDatasetPro = "DELETE FROM `" + dbName + "`.`experiment_protein_table`  WHERE  `exp_id`=? ";
            remProtStat = conn.prepareStatement(remDatasetPro);
            remProtStat.setInt(1, datasetId);
            remProtStat.executeUpdate();

            String selectFractList = "SELECT `fraction_id` FROM `experiment_fractions_table` where `exp_id` = ?";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            getFractDatasetStat = conn.prepareStatement(selectFractList);
            getFractDatasetStat.setInt(1, datasetId);
            rs = getFractDatasetStat.executeQuery();
            ArrayList<Integer> fractionIdList = new ArrayList<Integer>();
            while (rs.next()) {
                int fraction_id = rs.getInt("fraction_id");
                fractionIdList.add(fraction_id);

            }
            rs.close();

            for (int fb : fractionIdList) {
                remFractStat = conn.prepareStatement(remFract);
                remFractStat.setInt(1, fb);
                remFractStat.executeUpdate();
            }

            String removeFraction = "DELETE FROM `experiment_fractions_table`   WHERE `exp_id` = ? ;";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            remFractDatasetStat = conn.prepareStatement(removeFraction);
            remFractDatasetStat.setInt(1, datasetId);
            remFractDatasetStat.executeUpdate();

            return true;
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 1457 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return false;
        } catch (IllegalAccessException e) {
            System.err.println("at error line 1461 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return false;
        } catch (InstantiationException e) {
            System.err.println("at error line 1465 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return false;
        } catch (SQLException e) {
            System.err.println("at error line 1469 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return false;
        }
    }

    /**
     * Search for proteins by accession keywords
     *
     * @param accession array of query words
     * @param datasetId
     * @param validatedOnly only validated proteins results
     * @return dataset Proteins Searching List
     */
    public Map<Integer, IdentificationProteinBean> searchIdentificationProteinByAccession(String accession, int datasetId, boolean validatedOnly) {
        PreparedStatement selectProStat;
        String selectPro;

        String[] queryWordsArr = accession.split(",");
        StringBuilder sb = new StringBuilder();

        Set<String> searchSet = new HashSet<String>();
        for (String str : queryWordsArr) {
            searchSet.add(str.trim());
        }

        for (int x = 0; x < searchSet.size(); x++) {
            if (x > 0) {
                sb.append(" OR ");
            }
            sb.append("`prot_key` LIKE(?) ");

        }

        if (validatedOnly) {
            selectPro = "SELECT * FROM `experiment_protein_table` Where `exp_id` = ? AND  " + (sb.toString()) + " AND `valid`=?;";
        } else {
            selectPro = "SELECT * FROM `experiment_protein_table` Where `exp_id` = ? AND  " + (sb.toString());
        }

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProStat = conn.prepareStatement(selectPro);
            int index = 1;
            selectProStat.setInt(index++, datasetId);
            for (String str : searchSet) {
                selectProStat.setString(index++, "%" + str + "%");
            }

            if (validatedOnly) {
                selectProStat.setString(index, "TRUE");
            }
            ResultSet rs = selectProStat.executeQuery();

            Map<Integer, IdentificationProteinBean> proteinsList = fillProteinInformation(rs);
            System.gc();
            return proteinsList;

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 1531 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException e) {
            System.err.println("at error line 1534 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return null;
        } catch (InstantiationException e) {
            System.err.println("at error line 1538 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return null;
        } catch (SQLException e) {
            System.err.println("at error line 1542 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return null;
        }

    }

    /**
     * Search for identification proteins by accession keywords
     *
     * @param searchSet set of query words
     * @param validatedOnly only validated proteins results
     * @return dataset Proteins Searching List
     */
    public Map<Integer, IdentificationProteinBean> searchIdentificationProteinAllDatasetsByAccession(Set<String> searchSet, boolean validatedOnly) {
        PreparedStatement selectProStat;
        String selectPro;

        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < searchSet.size(); x++) {
            if (x > 0) {
                sb.append(" OR ");
            }
            sb.append("`prot_key` LIKE (?)");

        }

        if (validatedOnly) {
            selectPro = "SELECT * FROM `experiment_protein_table` Where  " + (sb.toString()) + " AND `valid`=?;";
        } else {
            selectPro = "SELECT * FROM `experiment_protein_table` Where  " + (sb.toString());
        }
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProStat = conn.prepareStatement(selectPro);
            int index = 1;
            for (String str : searchSet) {
                selectProStat.setString(index++, "%" + str + "%");
            }
            if (validatedOnly) {
                selectProStat.setString(index, "TRUE");
            }

            ResultSet rs = selectProStat.executeQuery();

            Map<Integer, IdentificationProteinBean> proteinsList = fillProteinInformation(rs);
            System.gc();
            return proteinsList;

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 1595 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return new HashMap<Integer, IdentificationProteinBean>();
        } catch (IllegalAccessException e) {
            System.err.println("at error line 1598 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return new HashMap<Integer, IdentificationProteinBean>();
        } catch (InstantiationException e) {
            System.err.println("at error line 1602 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return new HashMap<Integer, IdentificationProteinBean>();
        } catch (SQLException e) {
            System.err.println("at error line 1606 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return new HashMap<Integer, IdentificationProteinBean>();
        }

    }

    /**
     * Get identification peptides list for giving ids
     *
     * @param accession peptides IDs
     * @param otherAcc peptides IDs
     * @param datasetId peptides IDs
     * @return peptides list
     */
    public Map<Integer, IdentificationPeptideBean> getIdentificationPeptidesList(String accession, String otherAcc, int datasetId) {

        ResultSet rs;
        StringBuilder sb = new StringBuilder();
        sb.append("`protein` = ?");
        if (otherAcc != null && !otherAcc.equalsIgnoreCase("")) {
            sb.append(" AND ");
            sb.append("`other_protein(s)` = ?");
        }
        try {

            PreparedStatement selectPeptidesStat;
            String selectPeptide = "SELECT * FROM `proteins_peptides_table` WHERE  `exp_id` = ? AND " + (sb.toString()) + ";";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectPeptidesStat = conn.prepareStatement(selectPeptide);
            selectPeptidesStat.setInt(1, datasetId);
            selectPeptidesStat.setString(2, accession);
            if (otherAcc != null && !otherAcc.equalsIgnoreCase("")) {
                selectPeptidesStat.setString(3, otherAcc);
            }
            rs = selectPeptidesStat.executeQuery();

            Map<Integer, IdentificationPeptideBean> peptideList = fillPeptideInformation(rs);
            String missingPepStr = "SELECT * FROM  `proteins_peptides_table` WHERE  `exp_id` =? AND  `proteins_peptides_table`.`peptide_protein(s)` LIKE (?)";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectPeptidesStat = conn.prepareStatement(missingPepStr);
            selectPeptidesStat.setInt(1, datasetId);
            selectPeptidesStat.setString(2, "%" + accession + "%");

            rs = selectPeptidesStat.executeQuery();
            peptideList.putAll(fillPeptideInformation(rs));
            return peptideList;
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 1659 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error line 1662 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error line 1665 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error line 1668 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }
        System.gc();
        return null;
    }

    /**
     * Get identification proteins fractions average list
     *
     * @param accession
     * @param datasetId
     * @return dataset peptide List
     */
    public Map<Integer, IdentificationFractionBean> getIdentificationProteinFractionList(String accession, int datasetId) {
        Map<Integer, IdentificationFractionBean> fractionsList = new HashMap<Integer, IdentificationFractionBean>();
        try {
            //get fractions id list
            PreparedStatement selectFractsListStat;
            int index = 0;
            String selectFractList = "SELECT `fraction_id`,`min_range` ,`max_range`,`index` FROM `experiment_fractions_table` where `exp_id` = ?";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectFractsListStat = conn.prepareStatement(selectFractList);
            selectFractsListStat.setInt(1, datasetId);
            ResultSet rs = selectFractsListStat.executeQuery();
            ArrayList<Integer> fractionIdList = new ArrayList<Integer>();
            while (rs.next()) {
                int fraction_id = rs.getInt("fraction_id");
                index = rs.getInt("index");
                fractionIdList.add(fraction_id);

            }
            rs.close();
            //get fractions 
            PreparedStatement selectFractsStat;
            String selectFract = "SELECT * FROM `fractions_table` where `fraction_id` = ? AND UPPER(`prot_accession`) LIKE UPPER(?) ";
            for (int fractId : fractionIdList) {
                IdentificationFractionBean fb = new IdentificationFractionBean();
                fb.setFractionId(fractId);
                fb.setFractionIndex(index);
                if (conn == null || conn.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                }
                selectFractsStat = conn.prepareStatement(selectFract);
                selectFractsStat.setInt(1, fractId);
                selectFractsStat.setString(2, accession);
                rs = selectFractsStat.executeQuery();
                Map<String, IdentificationProteinBean> proteinList = new HashMap<String, IdentificationProteinBean>();
                otherSymbols.setGroupingSeparator('.');
                df = new DecimalFormat("#.##", otherSymbols);
                while (rs.next()) {
                    IdentificationProteinBean pb = new IdentificationProteinBean();//fraction_id		  			
                    pb.setAccession(rs.getString("prot_accession"));
                    pb.setNumberOfPeptidePerFraction(rs.getInt("number_peptides"));
                    pb.setNumberOfSpectraPerFraction(rs.getInt("number_spectra"));
                    pb.setAveragePrecursorIntensityPerFraction(Double.valueOf(df.format(rs.getDouble("average_ precursor_intensity"))));
                    proteinList.put(pb.getAccession(), pb);
                }

                fb.setProteinList(proteinList);
                fractionsList.put(fb.getFractionId(), fb);
                rs.close();

            }

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 1738 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error line 1741 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error line 1744 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (NumberFormatException e) {
            System.err.println("at error line 1747 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error line 1750 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }
        System.gc();
        return fractionsList;

    }

    /**
     * Search for identification proteins by protein description keywords
     *
     * @param protSearchKeyword array of query words
     * @param datasetId dataset Id
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public Map<Integer, IdentificationProteinBean> searchIdentificationProteinByName(String protSearchKeyword, int datasetId, boolean validatedOnly) {
        PreparedStatement selectProStat;
        String selectPro;
        String[] queryWordsArr = protSearchKeyword.split(",");
        Set<String> searchSet = new HashSet<String>();
        for (String str : queryWordsArr) {
            searchSet.add(str.trim());
        }
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < searchSet.size(); x++) {
            if (x > 0) {
                sb.append(" OR ");
            }
            sb.append("`description` LIKE(?)");

        }

        if (validatedOnly) {
            selectPro = "SELECT * FROM `experiment_protein_table` WHERE " + (sb.toString()) + " AND `exp_id`=? AND `valid`=?;";
        } else {
            selectPro = "SELECT * FROM `experiment_protein_table` WHERE " + (sb.toString()) + " AND `exp_id`=? ";
        }
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProStat = conn.prepareStatement(selectPro);
            int index = 1;
            for (String str : searchSet) {
                selectProStat.setString(index++, "%" + str + "%");
            }
            selectProStat.setInt(index++, datasetId);
            if (validatedOnly) {
                selectProStat.setString(index, "TRUE");
            }
            ResultSet rs = selectProStat.executeQuery();
            Map<Integer, IdentificationProteinBean> proteinsList = fillProteinInformation(rs);
            System.gc();
            return proteinsList;
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 1809 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error line 1812 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error line 1815 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error line 1818 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }

        System.gc();
        return null;
    }

    /**
     * Search for identification proteins by protein description keywords
     *
     * @param protSearchKeyword array of query words
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public Map<Integer, IdentificationProteinBean> searchIdentificationProteinAllDatasetsByName(String protSearchKeyword, boolean validatedOnly) {
        PreparedStatement selectProStat;
        String selectPro;
        String[] queryWordsArr = protSearchKeyword.split("\n");
        Set<String> searchSet = new HashSet<String>();
        for (String str : queryWordsArr) {
            if (str.trim().length() > 3) {
                searchSet.add(str.trim());
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < searchSet.size(); x++) {
            if (x > 0) {
                sb.append(" OR ");
            }
            sb.append("`description` LIKE(?)");

        }
        if (validatedOnly) {
            selectPro = "SELECT * FROM `experiment_protein_table` WHERE " + (sb.toString()) + " AND `valid`=?;";
        } else {
            selectPro = "SELECT * FROM `experiment_protein_table` WHERE " + (sb.toString());
        }
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            int index = 1;
            selectProStat = conn.prepareStatement(selectPro);
            for (String str : searchSet) {
                selectProStat.setString(index++, "%" + str + "%");
            }
            if (validatedOnly) {
                selectProStat.setString(index, "TRUE");
            }

            ResultSet rs = selectProStat.executeQuery();
            Map<Integer, IdentificationProteinBean> proteinsList = fillProteinInformation(rs);
            System.gc();
            return proteinsList;
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 1875 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error line 1878 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error line 1881 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error line 1884 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }

        System.gc();
        return new HashMap<Integer, IdentificationProteinBean>();
    }

    /**
     * Fill identification peptides information from the result set
     *
     * @param resultSet results set to fill identification peptides data
     * @param validatedOnly only validated proteins results
     * @return identification peptides list
     */
    private Map<Integer, IdentificationPeptideBean> fillPeptideInformation(ResultSet resultSet) {
        Map<Integer, IdentificationPeptideBean> peptidesList = new HashMap<Integer, IdentificationPeptideBean>();
        try {
            while (resultSet.next()) {
                IdentificationPeptideBean pepb = new IdentificationPeptideBean();
                pepb.setProtein(resultSet.getString("protein"));
                pepb.setOtherProteins(resultSet.getString("other_protein(s)"));
                pepb.setPeptideProteins((resultSet.getString("peptide_protein(s)")));
                pepb.setOtherProteinDescriptions(resultSet.getString("other_protein_description(s)"));
                pepb.setPeptideProteinsDescriptions(resultSet.getString("peptide_proteins_description(s)"));
                pepb.setAaBefore(resultSet.getString("aa_before"));
                pepb.setAaAfter(resultSet.getString("aa_after"));
                pepb.setSequence(resultSet.getString("sequence"));

                pepb.setPeptideEnd(resultSet.getString("peptide_end"));
                pepb.setPeptideStart(resultSet.getString("peptide_start"));

                pepb.setVariableModification(resultSet.getString("variable_modification"));

                pepb.setLocationConfidence(resultSet.getString("location_confidence"));
                pepb.setPrecursorCharges(resultSet.getString("precursor_charge(s)"));
                pepb.setNumberOfValidatedSpectra(resultSet.getInt("number_of_validated_spectra"));
                pepb.setScore(resultSet.getDouble("score"));
                pepb.setConfidence(resultSet.getDouble("confidence"));
                pepb.setPeptideId(resultSet.getInt("peptide_id"));

                pepb.setFixedModification(resultSet.getString("fixed_modification"));

                pepb.setProteinInference(resultSet.getString("protein_inference"));
                pepb.setSequenceTagged(resultSet.getString("sequence_tagged"));
                pepb.setEnzymatic(Boolean.valueOf(resultSet.getString("enzymatic")));
                pepb.setValidated(resultSet.getDouble("validated"));
                pepb.setStarred(Boolean.valueOf(resultSet.getString("starred")));
                pepb.setGlycopatternPositions(resultSet.getString("glycopattern_position(s)"));
                String str = resultSet.getString("deamidation_and_glycopattern");
                if (str != null && !str.equals("")) {
                    pepb.setDeamidationAndGlycopattern(Boolean.valueOf(str));
                }
                pepb.setLikelyNotGlycosite(Boolean.valueOf(resultSet.getString("likelyNotGlycosite")));

                peptidesList.put(pepb.getPeptideId(), pepb);
            }
            resultSet.close();

        } catch (Exception exp) {
            System.err.println("at error line 1078 " + this.getClass().getName() + "   " + exp.getLocalizedMessage());
        }
        return peptidesList;
    }

    /**
     * Fill identification proteins information from the result set
     *
     * @param resultSet results set to fill identification peptides data
     * @return datasetProteinsList
     *
     */
    private Map<Integer, IdentificationProteinBean> fillProteinInformation(ResultSet rs) {
        Map<Integer, IdentificationProteinBean> proteinsList = new HashMap<Integer, IdentificationProteinBean>();
        try {
            while (rs.next()) {
                IdentificationProteinBean temPb = new IdentificationProteinBean();
                temPb.setDatasetId(rs.getInt("exp_id"));
                temPb.setAccession(rs.getString("prot_accession"));
                temPb.setDescription(rs.getString("description").trim());
                temPb.setOtherProteins(rs.getString("other_protein(s)"));
                temPb.setProteinInferenceClass(rs.getString("protein_inference_class"));
                temPb.setSequenceCoverage(rs.getDouble("sequence_coverage(%)"));
                temPb.setObservableCoverage(rs.getDouble("observable_coverage(%)"));
                temPb.setConfidentPtmSites(rs.getString("confident_ptm_sites"));
                temPb.setNumberConfident(rs.getString("number_confident"));
                temPb.setOtherPtmSites(rs.getString("other_ptm_sites"));
                temPb.setNumberOfOther(rs.getString("number_other"));
                temPb.setNumberValidatedPeptides(rs.getInt("number_validated_peptides"));
                temPb.setNumberValidatedSpectra(rs.getInt("number_validated_spectra"));
                temPb.setEmPai(rs.getDouble("em_pai"));
                temPb.setNsaf(rs.getDouble("nsaf"));
                temPb.setMw_kDa(rs.getDouble("mw_(kDa)"));
                temPb.setScore(rs.getDouble("score"));
                temPb.setConfidence(rs.getDouble("confidence"));
                temPb.setStarred(Boolean.valueOf(rs.getString("starred")));
                temPb.setNonEnzymaticPeptides(Boolean.valueOf(rs.getString("non_enzymatic_peptides").toUpperCase()));

                temPb.setSpectrumFractionSpread_lower_range_kDa(rs.getString("spectrum_fraction_spread_lower_range_kDa"));
                temPb.setSpectrumFractionSpread_upper_range_kDa(rs.getString("spectrum_fraction_spread_upper_range_kDa"));
                temPb.setPeptideFractionSpread_lower_range_kDa(rs.getString("peptide_fraction_spread_lower_range_kDa"));
                temPb.setPeptideFractionSpread_upper_range_kDa(rs.getString("peptide_fraction_spread_upper_range_kDa"));

                temPb.setGeneName(rs.getString("gene_name"));
                temPb.setChromosomeNumber(rs.getString("chromosome_number"));
                temPb.setValidated(Boolean.valueOf(rs.getString("valid")));
                temPb.setProtGroupId(rs.getInt("prot_group_id"));
                proteinsList.put(temPb.getProtGroupId(), temPb);

            }
            rs.close();
        } catch (SQLException sqlExcp) {
            System.err.println("at error line 1996 " + this.getClass().getName() + "   " + sqlExcp.getLocalizedMessage());
        }
        return proteinsList;

    }

    /**
     * Search for identification proteins by peptide sequence keywords
     *
     * @param peptideSequenceKeyword array of query words
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public Map<Integer, IdentificationProteinBean> SearchIdentificationProteinAllDatasetsByPeptideSequence(String peptideSequenceKeyword, boolean validatedOnly) {
        PreparedStatement selectPepIdStat;
        Map<Integer, IdentificationProteinBean> proteinsList;
        Map<Integer, IdentificationProteinBean> filteredProteinsList = new HashMap<Integer, IdentificationProteinBean>();

        String[] queryWordsArr = peptideSequenceKeyword.split("\n");
        Set<String> searchSet = new HashSet<String>();
        for (String str : queryWordsArr) {
            if (!str.trim().equalsIgnoreCase("")) {
                searchSet.add(str.trim());
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < searchSet.size(); x++) {
            if (x > 0) {
                sb.append(" OR ");
            }
            sb.append(" `sequence` LIKE(?) ");

        }

        Set<String> protAccessionQuerySet = new LinkedHashSet<String>();
        Set<Integer> expIds = new HashSet<Integer>();
        String selectProtAccession = "SELECT  `protein` ,  `other_protein(s)` ,  `peptide_protein(s)` , `exp_id` FROM  `proteins_peptides_table`  WHERE " + (sb.toString()) + " ;";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            selectPepIdStat = conn.prepareStatement(selectProtAccession);
            int index = 1;
            for (String str : searchSet) {

                selectPepIdStat.setString(index++, "%" + str.trim() + "%");
            }
            ResultSet rs = selectPepIdStat.executeQuery();
            while (rs.next()) {
                String prot = rs.getString("protein");
                if (prot != null && !prot.equalsIgnoreCase("") && !prot.equalsIgnoreCase("SHARED PEPTIDE")) {
                    protAccessionQuerySet.add(prot);
                }
                String otherProt = rs.getString("other_protein(s)");
                if (otherProt != null && !otherProt.equalsIgnoreCase("")) {
                    protAccessionQuerySet.addAll(Arrays.asList(otherProt.split(",")));
                }
                String peptideProt = rs.getString("peptide_protein(s)");
                if (peptideProt != null && !peptideProt.equalsIgnoreCase("")) {
                    protAccessionQuerySet.addAll(Arrays.asList(peptideProt.split(",")));
                }
                expIds.add(rs.getInt("exp_id"));

            }

            rs.close();
            proteinsList = this.searchIdentificationProteinAllDatasetsByAccession(protAccessionQuerySet, validatedOnly);
            if (proteinsList == null) {
                return new HashMap<Integer, IdentificationProteinBean>();
            }
            for (int key : proteinsList.keySet()) {
                IdentificationProteinBean pb = proteinsList.get(key);
                if (expIds.contains(pb.getDatasetId())) {
                    filteredProteinsList.put(key, pb);
                }
            }
            System.gc();
            return filteredProteinsList;
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 2087 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error line 2081 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error line 2074 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error line 2078 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }

        System.gc();
        return null;
    }

    /**
     * Search for identification proteins by peptide sequence keywords
     *
     * @param peptideSequenceKeyword array of query words
     * @param datasetId dataset Id
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public Map<Integer, IdentificationProteinBean> SearchIdentificationProteinByPeptideSequence(String peptideSequenceKeyword, int datasetId, boolean validatedOnly) {
        PreparedStatement selectPepIdStat;
        Map<Integer, IdentificationProteinBean> proteinsList;
        Map<Integer, IdentificationProteinBean> filteredProteinsList = new HashMap<Integer, IdentificationProteinBean>();

        String[] queryWordsArr = peptideSequenceKeyword.split("\n");
        StringBuilder sb = new StringBuilder();

        Set<String> searchSet = new HashSet<String>();
        for (String str : queryWordsArr) {
            searchSet.add(str.trim());
        }

        for (int x = 0; x < searchSet.size(); x++) {
            if (x > 0) {
                sb.append(" OR ");
            }
            sb.append("`sequence` = ?");

        }
        Set<String> protAccessionQuerySet = new HashSet<String>();
        Set<Integer> expIds = new HashSet<Integer>();
        String selectProtAccession = "SELECT  `protein` ,  `other_protein(s)` ,  `peptide_protein(s)` , `exp_id`  "
                + "FROM  `proteins_peptides_table`  WHERE " + (sb.toString()) + " AND `exp_id` = ? ;";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            selectPepIdStat = conn.prepareStatement(selectProtAccession);
            int index = 1;
            for (String str : searchSet) {
                selectPepIdStat.setString(index++, str);
            }
            selectPepIdStat.setInt(index, datasetId);
            ResultSet rs = selectPepIdStat.executeQuery();
            while (rs.next()) {
                String prot = rs.getString("protein");
                if (prot != null && !prot.equalsIgnoreCase("") && !prot.equalsIgnoreCase("SHARED PEPTIDE")) {
                    protAccessionQuerySet.add(prot);
                }
                String otherProt = rs.getString("other_protein(s)");
                if (otherProt != null && !otherProt.equalsIgnoreCase("")) {
                    protAccessionQuerySet.addAll(Arrays.asList(otherProt.split(",")));
                }
                String peptideProt = rs.getString("peptide_protein(s)");
                if (peptideProt != null && !peptideProt.equalsIgnoreCase("")) {
                    protAccessionQuerySet.addAll(Arrays.asList(peptideProt.split(",")));
                }
                expIds.add(rs.getInt("exp_id"));

            }
            rs.close();
            proteinsList = this.searchIdentificationProteinAllDatasetsByAccession(protAccessionQuerySet, validatedOnly);
            if (proteinsList == null || proteinsList.isEmpty()) {
                return null;
            }
            for (int key : proteinsList.keySet()) {
                IdentificationProteinBean pb = proteinsList.get(key);
                if (expIds.contains(pb.getDatasetId())) {

                    filteredProteinsList.put(key, pb);
                }
            }
            System.gc();
            return filteredProteinsList;
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 2171 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error line 2175 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error line 2178 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error line 2181 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }

        System.gc();
        return null;
    }

    /**
     * Store new user details
     *
     * @param username user username
     * @param userPassword
     * @param admin user is admin or not
     * @param email user email
     * @return test successful process
     */
    public boolean storeNewUser(String username, String userPassword, boolean admin, String email) {
        PreparedStatement regUserStat;
        boolean test = false;
        String insertUser = "INSERT INTO  `" + dbName + "`.`users_table`(`user_name`,`password`,`admin`,`email`) VALUES (?,?,?,?);";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            regUserStat = conn.prepareStatement(insertUser);
            regUserStat.setString(1, username.toUpperCase());
            regUserStat.setString(2, userPassword);
            regUserStat.setString(3, "" + admin);
            regUserStat.setString(4, email);
            int rs = regUserStat.executeUpdate();
            if (rs > 0) {
                test = true;
            }

        } catch (ClassNotFoundException e) {
        } catch (IllegalAccessException e) {
        } catch (InstantiationException e) {
        } catch (SQLException e) {
        }

        return test;
    }

    /**
     * Check user username is available
     *
     * @param email user email
     *
     * @return test successful process
     */
    public boolean validateUsername(String email) {
        try {

            //get username
            PreparedStatement selectUserStat;
            String selectuser = "SELECT * FROM `users_table` WHERE `email` = ?";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectUserStat = conn.prepareStatement(selectuser);
            selectUserStat.setString(1, email.toUpperCase());
            ResultSet rs = selectUserStat.executeQuery();
            while (rs.next()) {
                return false;//valid username

            }
            rs.close();
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 2252 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error line 2255 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error line 2257 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error line 2259 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        }
        return true;//not valid
    }

    /**
     * Authenticate username and password for user
     *
     * @param email user email
     *
     * @return password
     */
    public String authenticate(String email) {
        //get password 
        PreparedStatement selectUserStat;
        try {
            String selectuser = "SELECT `password` FROM `users_table` WHERE `email` =  ?";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectUserStat = conn.prepareStatement(selectuser);
            selectUserStat.setString(1, email.toUpperCase());
            ResultSet rs = selectUserStat.executeQuery();
            while (rs.next()) {
                return rs.getString("password");//valid username
            }
            rs.close();
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 2288 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error line 2290 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error line 2292 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error line 2294 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        }
        return null;//not valid
    }

    /**
     * Get user details
     *
     * @param email user email
     * @return user details of user
     */
    public User getUser(String email) {
        PreparedStatement selectUserStat;

        try {
            String selectuser = "SELECT * FROM `users_table` WHERE `email` =  ?";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectUserStat = conn.prepareStatement(selectuser);
            selectUserStat.setString(1, email.toUpperCase());
            ResultSet rs = selectUserStat.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setAdmin(Boolean.valueOf(rs.getString("admin")));
                user.setUsername(rs.getString("user_name"));
                user.setEmail(rs.getString("email"));
                return user;

            }
            rs.close();
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 2328 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error line 2330 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error line 2332 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error line 2334 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        }
        return null;//not valid
    }

    /**
     * Get users list
     *
     * @return list of users
     */
    public Map<Integer, String> getUsersList() {
        Map<Integer, String> usersList = new HashMap<Integer, String>();
        PreparedStatement selectUsersStat;

        try {
            String selectusers = "SELECT * FROM `users_table` WHERE `admin` = 'false'; ";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectUsersStat = conn.prepareStatement(selectusers);
            ResultSet rs = selectUsersStat.executeQuery();
            while (rs.next()) {

                usersList.put(rs.getInt("id"), rs.getString("user_name"));

            }
            rs.close();
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 2363 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error line 2365 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error line 2367 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error line 2369 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        }
        return usersList;//not valid
    }

    /**
     * Remove username from the database
     *
     * @param username
     *
     * @return test boolean successful process
     */
    public boolean removeUser(String username) {
        try {

            PreparedStatement removeUserStat;
            String removeuser = "DELETE  FROM `users_table` WHERE `user_name` = ?;";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            removeUserStat = conn.prepareStatement(removeuser);
            removeUserStat.setString(1, username.toUpperCase());
            int rs = removeUserStat.executeUpdate();
            if (rs > 0) {
                return true;//valid username

            }
            removeUserStat.clearParameters();
            removeUserStat.close();
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 2400 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error line 2402 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error line 2404 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error line 2406 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        }
        return false;//not valid
    }

    /**
     * update user password in the database
     *
     * @param username
     * @param newpassword
     * @return test boolean successful process
     */
    public boolean updateUserPassword(String username, String newpassword) {

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String updateProtDesc = "UPDATE  `" + dbName + "`.`users_table` SET `password`= ? WHERE `user_name` = ? ;";
            PreparedStatement updateProtDescStat = conn.prepareStatement(updateProtDesc);
            updateProtDescStat.setString(1, newpassword);
            updateProtDescStat.setString(2, username.toUpperCase());
            int test = updateProtDescStat.executeUpdate();
            updateProtDescStat.clearParameters();
            updateProtDescStat.close();
            if (test > 0) {
                return true;
            }
        } catch (ClassNotFoundException e2) {
            System.err.println(e2.getLocalizedMessage());
        } catch (IllegalAccessException e2) {
            System.err.println(e2.getLocalizedMessage());
        } catch (InstantiationException e2) {
            System.err.println(e2.getLocalizedMessage());
        } catch (SQLException e2) {
            System.err.println(e2.getLocalizedMessage());
        }

        return false;
    }

    /**
     * read and store standard plot files in the database
     *
     * @param dataset dataset bean (in case of update existing dataset)
     * @return test boolean
     */
    public boolean updateIdentificationStandardPlotProteins(IdentificationDatasetBean dataset) {
        removeIdentificationStandarPlotProteins(dataset.getDatasetId());
        for (StandardIdentificationFractionPlotProteinBean spb : dataset.getStanderdPlotProt()) {
            insertStandardPlotProtein(dataset.getDatasetId(), spb);
        }
        System.gc();
        return true;
    }

    /**
     * Store standard plot bean in the database
     *
     * @param datasetId dataset id
     * @param standardProteinBean
     * @return test boolean
     */
    public boolean insertStandardPlotProtein(int datasetId, StandardIdentificationFractionPlotProteinBean standardProteinBean) {
        int check;
        try {

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String insertStandPlotQ = "INSERT INTO  `" + dbName + "`.`standard_plot_proteins`(`exp_id` ,`mw_(kDa)`,`name`,`lower`,`upper`,`color`)VALUES (?,?,?,?,?,?);";
            PreparedStatement insertStandPlotStat = conn.prepareStatement(insertStandPlotQ);
            insertStandPlotStat.setInt(1, datasetId);
            insertStandPlotStat.setDouble(2, standardProteinBean.getMW_kDa());
            insertStandPlotStat.setString(3, standardProteinBean.getName().toUpperCase());
            insertStandPlotStat.setInt(4, standardProteinBean.getLowerFraction());
            insertStandPlotStat.setInt(5, standardProteinBean.getUpperFraction());
            insertStandPlotStat.setString(6, standardProteinBean.getColor().toUpperCase());
            check = insertStandPlotStat.executeUpdate();
            insertStandPlotStat.close();

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 2493 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return false;
        } catch (IllegalAccessException e) {
            System.err.println("at error line 3496 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return false;
        } catch (InstantiationException e) {
            System.err.println("at error line 2499 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return false;
        } catch (SQLException e) {
            System.err.println("at error line 2502 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return false;
        }
        return check > 0;

    }

    /**
     * Remove standard plot data in the database
     *
     * @param datasetId dataset id
     * @return test boolean
     */
    public boolean removeIdentificationStandarPlotProteins(int datasetId) {
        int x;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String StandarPlot = "DELETE FROM `standard_plot_proteins`  WHERE  `exp_id`=? ";

            PreparedStatement remDatasetStat = conn.prepareStatement(StandarPlot);
            remDatasetStat.setInt(1, datasetId);
            x = remDatasetStat.executeUpdate();
            System.gc();
        } catch (ClassNotFoundException e) {
            return false;
        } catch (IllegalAccessException e) {
            return false;
        } catch (InstantiationException e) {
            return false;
        } catch (SQLException e) {
            return false;
        }
        return x > 0;
    }

    /**
     * Retrieve standard proteins data for fraction plot
     *
     * @param datasetId
     * @return Standard Identification Fraction Plot Proteins list
     */
    public List<StandardIdentificationFractionPlotProteinBean> getStandardIdentificationFractionProteinsList(int datasetId) {
        List<StandardIdentificationFractionPlotProteinBean> standardPlotList = new ArrayList<StandardIdentificationFractionPlotProteinBean>();
        try {

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String selectPeptide = "SELECT * FROM `standard_plot_proteins` where `exp_id` = ?;";
            PreparedStatement selectPeptideStat = conn.prepareStatement(selectPeptide);
            selectPeptideStat.setInt(1, datasetId);
            ResultSet rs = selectPeptideStat.executeQuery();
            while (rs.next()) {
                StandardIdentificationFractionPlotProteinBean spb = new StandardIdentificationFractionPlotProteinBean();
                spb.setMW_kDa(rs.getDouble("mw_(kDa)"));
                spb.setLowerFraction(rs.getInt("lower"));
                spb.setUpperFraction(rs.getInt("upper"));
                spb.setName(rs.getString("name"));
                spb.setColor(rs.getString("color"));
                standardPlotList.add(spb);
            }
            rs.close();
        } catch (ClassNotFoundException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (InstantiationException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (SQLException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        }
        System.gc();
        return standardPlotList;
    }

    /**
     * Update identification dataset information
     *
     * @param dataset updated dataset object
     * @return test boolean
     */
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public boolean updateIdentificationDatasetInformation(IdentificationDatasetBean dataset) {
        String updateExp = "UPDATE  `" + dbName + "`.`experiments_table`  SET `name`=?,`ready`=? ,`uploaded_by`=?,`species`=?,`sample_type`=?,`sample_processing`=?,`instrument_type`=?,`frag_mode` =?,`proteins_number` = ? ,	`email` =?,`pblication_link`=?,`description`=?,`peptides_number` =?  WHERE `exp_id` = ? ;";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            PreparedStatement updateExpStat = conn.prepareStatement(updateExp, Statement.RETURN_GENERATED_KEYS);
            updateExpStat.setString(1, dataset.getName().toUpperCase());
            updateExpStat.setInt(2, 2);
            updateExpStat.setString(3, dataset.getUploadedByName().toUpperCase());
            updateExpStat.setString(4, dataset.getSpecies());
            updateExpStat.setString(5, dataset.getSampleType());
            updateExpStat.setString(6, dataset.getSampleProcessing());
            updateExpStat.setString(7, dataset.getInstrumentType());
            updateExpStat.setString(8, dataset.getFragMode());
            updateExpStat.setInt(9, dataset.getProteinsNumber());
            updateExpStat.setString(10, dataset.getEmail().toUpperCase());
            if (dataset.getPublicationLink() != null) {
                updateExpStat.setString(11, dataset.getPublicationLink());
            } else {
                updateExpStat.setString(11, "NOT AVAILABLE");
            }
            updateExpStat.setString(12, dataset.getDescription());
            updateExpStat.setInt(13, dataset.getPeptidesNumber());
            updateExpStat.setInt(14, dataset.getDatasetId());
            int test = updateExpStat.executeUpdate();
            System.gc();
            if (test > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("at error line 2626 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 2629 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return false;
        } catch (InstantiationException e) {
            System.err.println("at error line 2632 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return false;
        } catch (IllegalAccessException e) {
            System.err.println("at error line 2635 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return false;
        }

        return false;
    }

    // ==================================================  quant data  ===================================================
    /**
     * Get available quantification datasets initial information object that
     * contains the available datasets list and the active columns (to hide them
     * if they are empty)
     *
     * @return QuantDatasetInitialInformationObject
     */
    public Map<String, QuantDatasetInitialInformationObject> getQuantDatasetInitialInformationObject() {
        Map<String, QuantDatasetInitialInformationObject> diseaseCategoriesMap = new LinkedHashMap<String, QuantDatasetInitialInformationObject>();
        try {
            PreparedStatement selectStudiesStat;
            String selectStudies = "SELECT * FROM  `quant_dataset_table` ";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectStudiesStat = conn.prepareStatement(selectStudies);
            ResultSet rs = selectStudiesStat.executeQuery();
            while (rs.next()) {
                String disease_category = rs.getString("disease_category");
                if (!diseaseCategoriesMap.containsKey(disease_category)) {
                    boolean[] activeHeaders = new boolean[27];
                    Set<String> diseaseCategories = new LinkedHashSet<String>();
                    QuantDatasetInitialInformationObject datasetObject = new QuantDatasetInitialInformationObject();
                    Map<Integer, QuantDatasetObject> updatedQuantDatasetObjectMap = new LinkedHashMap<Integer, QuantDatasetObject>();
                    datasetObject.setQuantDatasetsList(updatedQuantDatasetObjectMap);
                    datasetObject.setActiveHeaders(activeHeaders);
                    datasetObject.setDiseaseCategories(diseaseCategories);
                    diseaseCategoriesMap.put(disease_category, datasetObject);

                }
                QuantDatasetInitialInformationObject datasetObject = diseaseCategoriesMap.get(disease_category);
                boolean[] activeHeaders = datasetObject.getActiveHeaders();
                Map<Integer, QuantDatasetObject> updatedQuantDatasetObjectMap = datasetObject.getQuantDatasetsList();
                Set<String> diseaseCategories = datasetObject.getDiseaseCategories();

                QuantDatasetObject ds = new QuantDatasetObject();
                String author = rs.getString("author");
                if (!activeHeaders[0] && author != null && !author.equalsIgnoreCase("Not Available")) {
                    activeHeaders[0] = true;
                }
                ds.setAuthor(author);
                int year = rs.getInt("year");
                if (!activeHeaders[1] && year != 0) {
                    activeHeaders[1] = true;
                }
                ds.setYear(year);
                int identified_proteins_num = rs.getInt("identified_proteins_number");
                if (!activeHeaders[2] && identified_proteins_num != -1 && identified_proteins_num != 0) {
                    activeHeaders[2] = true;
                }
                ds.setIdentifiedProteinsNumber(identified_proteins_num);

                int quantified_proteins_number = rs.getInt("quantified_proteins_number");
                if (!activeHeaders[3] && quantified_proteins_number != -1) {
                    activeHeaders[3] = true;
                }
                ds.setQuantifiedProteinsNumber(quantified_proteins_number);

                String analytical_method = rs.getString("analytical_method");
                if (!activeHeaders[4] && analytical_method != null && !analytical_method.equalsIgnoreCase("Not Available")) {
                    activeHeaders[4] = true;
                }
                ds.setAnalyticalMethod(analytical_method);

                String raw_data_available = rs.getString("raw_data_available");
                if (!activeHeaders[5] && raw_data_available != null && !raw_data_available.equalsIgnoreCase("Not Available")) {
                    activeHeaders[5] = true;
                }
                ds.setRawDataUrl(raw_data_available);

                int files_num = rs.getInt("files_num");
                if (!activeHeaders[6] && files_num != -1) {
                    activeHeaders[6] = true;
                }
                ds.setFilesNumber(files_num);

                String type_of_study = rs.getString("type_of_study");
                if (!activeHeaders[7] && type_of_study != null && !type_of_study.equalsIgnoreCase("Not Available")) {
                    activeHeaders[7] = true;
                }
                ds.setTypeOfStudy(type_of_study);

                String sample_type = rs.getString("sample_type");
                if (!activeHeaders[8] && sample_type != null && !sample_type.equalsIgnoreCase("Not Available")) {
                    activeHeaders[8] = true;
                }
                ds.setSampleType(sample_type);

                String sample_matching = rs.getString("sample_matching");
                if (!activeHeaders[9] && sample_matching != null && !sample_matching.equalsIgnoreCase("Not Available")) {
                    activeHeaders[9] = true;
                }
                ds.setSampleMatching(sample_matching);

                String shotgun_targeted = rs.getString("shotgun_targeted");
                if (!activeHeaders[10] && shotgun_targeted != null && !shotgun_targeted.equalsIgnoreCase("Not Available")) {
                    activeHeaders[10] = true;
                }
                ds.setShotgunTargeted(shotgun_targeted);

                String technology = rs.getString("technology");
                if (!activeHeaders[11] && technology != null && !technology.equalsIgnoreCase("Not Available")) {
                    activeHeaders[11] = true;
                }
                ds.setTechnology(technology);

                String analytical_approach = rs.getString("analytical_approach");
                if (!activeHeaders[12] && analytical_approach != null && !analytical_approach.equalsIgnoreCase("Not Available")) {
                    activeHeaders[12] = true;
                }
                ds.setAnalyticalApproach(analytical_approach);

                String enzyme = rs.getString("enzyme");
                if (!activeHeaders[13] && enzyme != null && !enzyme.equalsIgnoreCase("Not Available")) {
                    activeHeaders[13] = true;
                }
                ds.setEnzyme(enzyme);

                String quantification_basis = rs.getString("quantification_basis");
                if (!activeHeaders[14] && quantification_basis != null && !quantification_basis.equalsIgnoreCase("Not Available")) {
                    activeHeaders[14] = true;
                }

                ds.setQuantificationBasis(quantification_basis);

                String quant_basis_comment = rs.getString("quant_basis_comment");
                if (!activeHeaders[15] && quant_basis_comment != null && !quant_basis_comment.equalsIgnoreCase("Not Available")) {
                    activeHeaders[15] = true;
                }
                ds.setQuantBasisComment(quant_basis_comment);

                int id = rs.getInt("index");
                ds.setDsKey(id);

                String normalization_strategy = rs.getString("normalization_strategy");
                if (!activeHeaders[16] && normalization_strategy != null && !normalization_strategy.equalsIgnoreCase("Not Available")) {
                    activeHeaders[16] = true;
                }
                ds.setNormalizationStrategy(normalization_strategy);

                String pumed_id = rs.getString("pumed_id");
                if (!activeHeaders[17] && pumed_id != null && !pumed_id.equalsIgnoreCase("Not Available")) {
                    activeHeaders[17] = true;
                }
                ds.setPumedID(pumed_id);

                String patient_group_i = rs.getString("patient_group_i");
                if (!activeHeaders[18] && patient_group_i != null && !patient_group_i.equalsIgnoreCase("Not Available")) {
                    activeHeaders[18] = true;
                }
                ds.setPatientsGroup1(patient_group_i + "\n" + disease_category.replace(" ", "_").replace("'", "-") + "_Disease");

                int patients_group_i_number = rs.getInt("patients_group_i_number");
                if (!activeHeaders[19] && patients_group_i_number != -1) {
                    activeHeaders[19] = true;
                }
                ds.setPatientsGroup1Number(patients_group_i_number);

                String patient_gr_i_comment = rs.getString("patient_gr_i_comment");
                if (!activeHeaders[20] && patient_gr_i_comment != null && !patient_gr_i_comment.equalsIgnoreCase("Not Available")) {
                    activeHeaders[20] = true;
                }
                ds.setPatientsGroup1Comm(patient_gr_i_comment);

                String patient_sub_group_i = rs.getString("patient_sub_group_i");
                if (!activeHeaders[21] && patient_sub_group_i != null && !patient_sub_group_i.equalsIgnoreCase("Not Available")) {
                    activeHeaders[21] = true;
                }
                ds.setPatientsSubGroup1(patient_sub_group_i + "\n" + disease_category.replace(" ", "_").replace("'", "-") + "_Disease");

                String patient_group_ii = rs.getString("patient_group_ii");
                if (!activeHeaders[22] && patient_group_ii != null && !patient_group_ii.equalsIgnoreCase("Not Available")) {
                    activeHeaders[22] = true;
                }
                ds.setPatientsGroup2(patient_group_ii + "\n" + disease_category.replace(" ", "_").replace("'", "-") + "_Disease");

                int patients_group_ii_number = rs.getInt("patients_group_ii_number");
                if (!activeHeaders[23] && patients_group_ii_number != -1) {
                    activeHeaders[23] = true;
                }
                ds.setPatientsGroup2Number(patients_group_ii_number);

                String patient_gr_ii_comment = rs.getString("patient_gr_ii_comment");
                if (!activeHeaders[24] && patient_gr_ii_comment != null && !patient_gr_ii_comment.equalsIgnoreCase("Not Available")) {
                    activeHeaders[24] = true;
                }
                ds.setPatientsGroup2Comm(patient_gr_ii_comment);

                String patient_sub_group_ii = rs.getString("patient_sub_group_ii");
                if (!activeHeaders[25] && patient_sub_group_ii != null && !patient_sub_group_ii.equalsIgnoreCase("Not Available")) {
                    activeHeaders[25] = true;
                }
                ds.setPatientsSubGroup2(patient_sub_group_ii + "\n" + disease_category.replace(" ", "_").replace("'", "-") + "_Disease");
                ds.setAdditionalcomments("Not Available");
                ds.setDiseaseCategory(rs.getString("disease_category"));

                ds.setTotalProtNum(rs.getInt("total_prot_num"));
                ds.setUniqueProtNum(rs.getInt("uniq_prot_num"));
                ds.setTotalPepNum(rs.getInt("total_pept_num"));
                ds.setUniqePepNum(rs.getInt("uniq_pept_num"));

                diseaseCategories.add(ds.getDiseaseCategory());
                activeHeaders[26] = false;
                updatedQuantDatasetObjectMap.put(ds.getDsKey(), ds);
                datasetObject.setQuantDatasetsList(updatedQuantDatasetObjectMap);
                datasetObject.setActiveHeaders(activeHeaders);
                datasetObject.setDiseaseCategories(diseaseCategories);
                diseaseCategoriesMap.put(disease_category, datasetObject);

            }
            rs.close();
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            return diseaseCategoriesMap;

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 2846 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error line 1849 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error line 2852 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error line 2855 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }
        System.gc();

        return null;

    }

    /**
     * Get active quantification pie charts filters (to hide them if they are
     * empty)
     *
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public Map<String, boolean[]> getActivePieChartQuantFilters() {

        Map<String, boolean[]> activePieChartQuantFiltersDiseaseCategoryMap = new LinkedHashMap<String, boolean[]>();
        List<String> disCatList = new LinkedList<String>();
        try {

            PreparedStatement selectPumed_idStat;
            String selectPumed_id = "SELECT  `pumed_id` ,  `disease_category` FROM  `quant_dataset_table` GROUP BY  `pumed_id` ,  `disease_category` ORDER BY  `pumed_id` ";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectPumed_idStat = conn.prepareStatement(selectPumed_id);
            ResultSet rs = selectPumed_idStat.executeQuery();
            int pumed_id_index = 0;
            while (rs.next()) {
                disCatList.add(pumed_id_index, rs.getString("disease_category"));
                pumed_id_index++;
            }
            rs.close();
            /// check the colums one by one 
            boolean[] activeFilters = new boolean[]{false, false, false, false, true, true, false, true, true, true, false, true, false, false, false, false, false, false};
            for (String str : disCatList) {
//                String[] columnArr = new String[]{"`identified_proteins_number`", "`quantified_proteins_number`", "`analytical_method`", "`raw_data_available`", "`year`", "`type_of_study`", "`sample_type`", "`sample_matching`", "`technology`", "`analytical_approach`", "`enzyme`", "`shotgun_targeted`", "`quantification_basis`", "`quant_basis_comment`", "`patients_group_i_number`", "`patients_group_ii_number`", "`normalization_strategy`"};

//                for (int index = 0; index < columnArr.length; index++) {
//                    String selectPumed_id1 = "SELECT  `pumed_id`  FROM  `quant_dataset_table` WHERE `disease_category`=? GROUP BY  `pumed_id`, " + columnArr[index] + " ORDER BY  `pumed_id` ";
//                    if (conn == null || conn.isClosed()) {
//                        Class.forName(driver).newInstance();
//                        conn = DriverManager.getConnection(url + dbName, userName, password);
//                    }
//                    selectPumed_idStat = conn.prepareStatement(selectPumed_id1);
//                    selectPumed_idStat.setString(1, str);
//                    rs = selectPumed_idStat.executeQuery();
//                    int pumed_id_com_index = 0;
//                    while (rs.next()) {
//                        pumed_id_com_index++;
//                    }
//                    rs.close();
//                    if (pumed_id_index != pumed_id_com_index) {
//                        activeFilters[index] = true;
//                    }
//
//                }
//                activeFilters[0] = false;
//                activeFilters[1] = false;
//                activeFilters[2] = false;
////                activeFilters[3] = true;
//                activeFilters[4] = true;
//                activeFilters[5] = true;
//                activeFilters[7] = true;
//                activeFilters[8] = true;
//                activeFilters[9] = true;
//                activeFilters[11] = true;
//                activeFilters[10] = false;
//                activeFilters[activeFilters.length - 2] = false;
//                activeFilters[activeFilters.length - 3] = false;
//                activeFilters[activeFilters.length - 4] = false;
                activePieChartQuantFiltersDiseaseCategoryMap.put(str, activeFilters);
            }
            activePieChartQuantFiltersDiseaseCategoryMap.put("All", activeFilters);

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 2912 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error line 2915 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error line 2918 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error line 2921 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        }
        System.gc();

        return activePieChartQuantFiltersDiseaseCategoryMap;

    }

    /**
     * Get active quantification pie charts filters within quant searching
     * proteins results (to hide them if they are empty)
     *
     * @param searchQuantificationProtList
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public Map<String, boolean[]> getActivePieChartQuantFilters(List<QuantProtein> searchQuantificationProtList) {

        Map<String, boolean[]> activePieChartQuantFiltersDiseaseCategoryMap = new LinkedHashMap<String, boolean[]>();
        List<String> disCatList = new LinkedList<String>();
        try {

            Set<Integer> QuantDatasetIds = new HashSet<Integer>();
            for (QuantProtein quantProt : searchQuantificationProtList) {
                QuantDatasetIds.add(quantProt.getDsKey());
            }
            StringBuilder sb = new StringBuilder();

            for (int index : QuantDatasetIds) {
                sb.append("  `index` = ").append(index);
                sb.append(" OR ");

            }

//            String stat = sb.toString().substring(0, sb.length() - 4);
            PreparedStatement selectPumed_idStat;
            String selectPumed_id = "SELECT  `pumed_id` ,  `disease_category` FROM  `quant_dataset_table` GROUP BY  `pumed_id` ,  `disease_category` ORDER BY  `pumed_id` ";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectPumed_idStat = conn.prepareStatement(selectPumed_id);
            ResultSet rs = selectPumed_idStat.executeQuery();
            int pumed_id_index = 0;
            while (rs.next()) {
                disCatList.add(pumed_id_index, rs.getString("disease_category"));
                pumed_id_index++;
            }
            rs.close();
            /// check the colums one by one 

            boolean[] activeFilters = new boolean[]{false, false, false, false, true, true, false, true, true, true, false, true, false, false, false, false, false, false};
            for (String str : disCatList) {
                activePieChartQuantFiltersDiseaseCategoryMap.put(str, activeFilters);
//                String[] columnArr = new String[]{"`identified_proteins_number`", "`quantified_proteins_number`", "`analytical_method`", "`raw_data_available`", "`year`", "`type_of_study`", "`sample_type`", "`sample_matching`", "`technology`", "`analytical_approach`", "`enzyme`", "`shotgun_targeted`", "`quantification_basis`", "`quant_basis_comment`", "`patients_group_i_number`", "`patients_group_ii_number`", "`normalization_strategy`"};
////                boolean[] activeFilters = new boolean[columnArr.length];
//                for (int index = 0; index < columnArr.length; index++) {
//                    String selectPumed_id1 = "SELECT  `pumed_id`  FROM  `quant_dataset_table` WHERE " + stat + " AND `disease_category`=? GROUP BY  `pumed_id`, " + columnArr[index] + " ORDER BY  `pumed_id` ";
//                    if (conn == null || conn.isClosed()) {
//                        Class.forName(driver).newInstance();
//                        conn = DriverManager.getConnection(url + dbName, userName, password);
//                    }
//                    selectPumed_idStat = conn.prepareStatement(selectPumed_id1);
//                    selectPumed_idStat.setString(1, str);
//                    rs = selectPumed_idStat.executeQuery();
//                    int pumed_id_com_index = 0;
//                    while (rs.next()) {
//                        pumed_id_com_index++;
//                    }
//                    rs.close();
//                    if (pumed_id_index != pumed_id_com_index) {
//                        activeFilters[index] = true;
//                    }
//
//                }
//                activeFilters[0] = false;
//                activeFilters[1] = false;
//                activeFilters[2] = false;
//                activeFilters[3] = true;
//                activeFilters[4] = true;
//                activeFilters[7] = false;
//                activeFilters[activeFilters.length - 2] = false;
//                activeFilters[activeFilters.length - 3] = false;
//                activeFilters[activeFilters.length - 4] = false;
//                activePieChartQuantFiltersDiseaseCategoryMap.put(str, activeFilters);
            }
            activePieChartQuantFiltersDiseaseCategoryMap.put("All", activeFilters);
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 2912 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error line 2915 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error line 2918 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error line 2921 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        }
        System.gc();
        return activePieChartQuantFiltersDiseaseCategoryMap;
    }

    /**
     * Get available quantification datasets initial information object within
     * quant searching proteins results that contains the available datasets
     * list and the active columns (to hide them if they are empty)
     *
     * @param searchQuantificationProtList
     * @return QuantDatasetInitialInformationObject
     */
    public Map<String, QuantDatasetInitialInformationObject> getQuantDatasetListObject(List<QuantProtein> searchQuantificationProtList) {
        Map<String, QuantDatasetInitialInformationObject> diseaseCategoriesMap = new LinkedHashMap<String, QuantDatasetInitialInformationObject>();
        Set<Integer> QuantDatasetIds = new HashSet<Integer>();
        for (QuantProtein quantProt : searchQuantificationProtList) {
            QuantDatasetIds.add(quantProt.getDsKey());
        }
        StringBuilder sb = new StringBuilder();

        for (int index : QuantDatasetIds) {
            sb.append("  `index` = ").append(index);
            sb.append(" OR ");

        }
        String stat;
        if (sb.length() > 4) {
            stat = sb.toString().substring(0, sb.length() - 4);
        } else {
            return diseaseCategoriesMap;
        }

        try {
            PreparedStatement selectStudiesStat;
            String selectStudies = "SELECT * FROM  `quant_dataset_table` WHERE  " + stat;
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectStudiesStat = conn.prepareStatement(selectStudies);
            ResultSet rs = selectStudiesStat.executeQuery();
            while (rs.next()) {
                String disease_category = rs.getString("disease_category");
                if (!diseaseCategoriesMap.containsKey(disease_category)) {
                    boolean[] activeHeaders = new boolean[27];
                    Set<String> diseaseCategories = new LinkedHashSet<String>();
                    QuantDatasetInitialInformationObject datasetObject = new QuantDatasetInitialInformationObject();
                    Map<Integer, QuantDatasetObject> updatedQuantDatasetObjectMap = new LinkedHashMap<Integer, QuantDatasetObject>();
                    datasetObject.setQuantDatasetsList(updatedQuantDatasetObjectMap);
                    datasetObject.setActiveHeaders(activeHeaders);
                    datasetObject.setDiseaseCategories(diseaseCategories);
                    diseaseCategoriesMap.put(disease_category, datasetObject);

                }
                QuantDatasetInitialInformationObject datasetObject = diseaseCategoriesMap.get(disease_category);
                boolean[] activeHeaders = datasetObject.getActiveHeaders();
                Map<Integer, QuantDatasetObject> updatedQuantDatasetObjectMap = datasetObject.getQuantDatasetsList();
                Set<String> diseaseCategories = datasetObject.getDiseaseCategories();

                QuantDatasetObject ds = new QuantDatasetObject();
                String author = rs.getString("author");
                if (!activeHeaders[0] && author != null && !author.equalsIgnoreCase("Not Available")) {
                    activeHeaders[0] = true;
                }
                ds.setAuthor(author);
                int year = rs.getInt("year");
                if (!activeHeaders[1] && year != 0) {
                    activeHeaders[1] = true;
                }
                ds.setYear(year);
                int identified_proteins_num = rs.getInt("identified_proteins_number");
                if (!activeHeaders[2] && identified_proteins_num != -1 && identified_proteins_num != 0) {
                    activeHeaders[2] = true;
                }
                ds.setIdentifiedProteinsNumber(identified_proteins_num);

                int quantified_proteins_number = rs.getInt("quantified_proteins_number");
                if (!activeHeaders[3] && quantified_proteins_number != -1) {
                    activeHeaders[3] = true;
                }
                ds.setQuantifiedProteinsNumber(quantified_proteins_number);

                String analytical_method = rs.getString("analytical_method");
                if (!activeHeaders[4] && analytical_method != null && !analytical_method.equalsIgnoreCase("Not Available")) {
                    activeHeaders[4] = true;
                }
                ds.setAnalyticalMethod(analytical_method);

                String raw_data_available = rs.getString("raw_data_available");
                if (!activeHeaders[5] && raw_data_available != null && !raw_data_available.equalsIgnoreCase("Not Available")) {
                    activeHeaders[5] = true;
                }
                ds.setRawDataUrl(raw_data_available);

                int files_num = rs.getInt("files_num");
                if (!activeHeaders[6] && files_num != -1) {
                    activeHeaders[6] = true;
                }
                ds.setFilesNumber(files_num);

                String type_of_study = rs.getString("type_of_study");
                if (!activeHeaders[7] && type_of_study != null && !type_of_study.equalsIgnoreCase("Not Available")) {
                    activeHeaders[7] = true;
                }
                ds.setTypeOfStudy(type_of_study);

                String sample_type = rs.getString("sample_type");
                if (!activeHeaders[8] && sample_type != null && !sample_type.equalsIgnoreCase("Not Available")) {
                    activeHeaders[8] = true;
                }
                ds.setSampleType(sample_type);

                String sample_matching = rs.getString("sample_matching");
                if (!activeHeaders[9] && sample_matching != null && !sample_matching.equalsIgnoreCase("Not Available")) {
                    activeHeaders[9] = true;
                }
                ds.setSampleMatching(sample_matching);

                String shotgun_targeted = rs.getString("shotgun_targeted");
                if (!activeHeaders[10] && shotgun_targeted != null && !shotgun_targeted.equalsIgnoreCase("Not Available")) {
                    activeHeaders[10] = true;
                }
                ds.setShotgunTargeted(shotgun_targeted);

                String technology = rs.getString("technology");
                if (!activeHeaders[11] && technology != null && !technology.equalsIgnoreCase("Not Available")) {
                    activeHeaders[11] = true;
                }
                ds.setTechnology(technology);

                String analytical_approach = rs.getString("analytical_approach");
                if (!activeHeaders[12] && analytical_approach != null && !analytical_approach.equalsIgnoreCase("Not Available")) {
                    activeHeaders[12] = true;
                }
                ds.setAnalyticalApproach(analytical_approach);

                String enzyme = rs.getString("enzyme");
                if (!activeHeaders[13] && enzyme != null && !enzyme.equalsIgnoreCase("Not Available")) {
                    activeHeaders[13] = true;
                }
                ds.setEnzyme(enzyme);

                String quantification_basis = rs.getString("quantification_basis");
                if (!activeHeaders[14] && quantification_basis != null && !quantification_basis.equalsIgnoreCase("Not Available")) {
                    activeHeaders[14] = true;
                }

                ds.setQuantificationBasis(quantification_basis);

                String quant_basis_comment = rs.getString("quant_basis_comment");
                if (!activeHeaders[15] && quant_basis_comment != null && !quant_basis_comment.equalsIgnoreCase("Not Available")) {
                    activeHeaders[15] = true;
                }
                ds.setQuantBasisComment(quant_basis_comment);

                int id = rs.getInt("index");
                ds.setDsKey(id);

                String normalization_strategy = rs.getString("normalization_strategy");
                if (!activeHeaders[16] && normalization_strategy != null && !normalization_strategy.equalsIgnoreCase("Not Available")) {
                    activeHeaders[16] = true;
                }
                ds.setNormalizationStrategy(normalization_strategy);

                String pumed_id = rs.getString("pumed_id");
                if (!activeHeaders[17] && pumed_id != null && !pumed_id.equalsIgnoreCase("Not Available")) {
                    activeHeaders[17] = true;
                }
                ds.setPumedID(pumed_id);

                String patient_group_i = rs.getString("patient_group_i");
                if (!activeHeaders[18] && patient_group_i != null && !patient_group_i.equalsIgnoreCase("Not Available")) {
                    activeHeaders[18] = true;
                }
                ds.setPatientsGroup1(patient_group_i + "\n" + disease_category.replace(" ", "_").replace("'", "-") + "_Disease");

                int patients_group_i_number = rs.getInt("patients_group_i_number");
                if (!activeHeaders[19] && patients_group_i_number != -1) {
                    activeHeaders[19] = true;
                }
                ds.setPatientsGroup1Number(patients_group_i_number);

                String patient_gr_i_comment = rs.getString("patient_gr_i_comment");
                if (!activeHeaders[20] && patient_gr_i_comment != null && !patient_gr_i_comment.equalsIgnoreCase("Not Available")) {
                    activeHeaders[20] = true;
                }
                ds.setPatientsGroup1Comm(patient_gr_i_comment);

                String patient_sub_group_i = rs.getString("patient_sub_group_i");
                if (!activeHeaders[21] && patient_sub_group_i != null && !patient_sub_group_i.equalsIgnoreCase("Not Available")) {
                    activeHeaders[21] = true;
                }
                ds.setPatientsSubGroup1(patient_sub_group_i + "\n" + disease_category.replace(" ", "_").replace("'", "-") + "_Disease");

                String patient_group_ii = rs.getString("patient_group_ii");
                if (!activeHeaders[22] && patient_group_ii != null && !patient_group_ii.equalsIgnoreCase("Not Available")) {
                    activeHeaders[22] = true;
                }
                ds.setPatientsGroup2(patient_group_ii + "\n" + disease_category.replace(" ", "_").replace("'", "-") + "_Disease");

                int patients_group_ii_number = rs.getInt("patients_group_ii_number");
                if (!activeHeaders[23] && patients_group_ii_number != -1) {
                    activeHeaders[23] = true;
                }
                ds.setPatientsGroup2Number(patients_group_ii_number);

                String patient_gr_ii_comment = rs.getString("patient_gr_ii_comment");
                if (!activeHeaders[24] && patient_gr_ii_comment != null && !patient_gr_ii_comment.equalsIgnoreCase("Not Available")) {
                    activeHeaders[24] = true;
                }
                ds.setPatientsGroup2Comm(patient_gr_ii_comment);

                String patient_sub_group_ii = rs.getString("patient_sub_group_ii");
                if (!activeHeaders[25] && patient_sub_group_ii != null && !patient_sub_group_ii.equalsIgnoreCase("Not Available")) {
                    activeHeaders[25] = true;
                }
                ds.setPatientsSubGroup2(patient_sub_group_ii + "\n" + disease_category.replace(" ", "_").replace("'", "-") + "_Disease");
                ds.setAdditionalcomments("Not Available");
                ds.setDiseaseCategory(rs.getString("disease_category"));

                ds.setTotalProtNum(rs.getInt("total_prot_num"));
                ds.setUniqueProtNum(rs.getInt("uniq_prot_num"));
                ds.setTotalPepNum(rs.getInt("total_pept_num"));
                ds.setUniqePepNum(rs.getInt("uniq_pept_num"));

                diseaseCategories.add(ds.getDiseaseCategory());
                activeHeaders[26] = false;
                updatedQuantDatasetObjectMap.put(ds.getDsKey(), ds);
                datasetObject.setQuantDatasetsList(updatedQuantDatasetObjectMap);
                datasetObject.setActiveHeaders(activeHeaders);
                datasetObject.setDiseaseCategories(diseaseCategories);
                diseaseCategoriesMap.put(disease_category, datasetObject);

            }
            rs.close();
            return diseaseCategoriesMap;

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 2846 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error line 1849 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error line 2852 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error line 2855 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }
        System.gc();
        return null;
    }

    /**
     * Store quant proteins list to database
     *
     * @param quantProeinstList
     * @return
     */
    public boolean storeQuantProt(List<QuantProtein> quantProeinstList) {
        boolean success = true;
        String insertQProt = "INSERT INTO  `" + dbName + "`.`quant_prot_table` (`pumed_id` ,`uniprot_accession` ,`uniprot_protein_name` ,`publication_acc_number` ,`publication_protein_name` ,`raw_data_available` ,`type_of_study` ,"
                + "`sample_type` ,`patient_group_i` ,`patient_sub_group_i` ,`patient_gr_i_comment` ,`patient_group_ii` ,`patient_sub_group_ii` ,`patient_gr_ii_comment` ,`sample_matching` ,`normalization_strategy` ,`technology`,`analytical_approach`,`enzyme`,`shotgun_targeted`,`quantification_basis`,`quant_basis_comment`,`additional_comments`,`q_peptide_key`,`peptide_sequance`,`peptide_modification`,`modification_comment` ,`string_fc_value`,`string_p_value`,`peptideId_number`,`quantified_peptides_number`,`patients_group_i_number`,`patients_group_ii_number`,`p_value`,`roc_auc`,`fc_value`,`peptide_prot`)VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,? , ? , ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

        PreparedStatement insertQProtStat;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            for (QuantProtein qprot : quantProeinstList) {
                insertQProtStat = conn.prepareStatement(insertQProt, Statement.RETURN_GENERATED_KEYS);
                insertQProtStat.setString(1, qprot.getPumedID().toUpperCase());
                insertQProtStat.setString(2, qprot.getUniprotAccession().toUpperCase());
                insertQProtStat.setString(3, qprot.getUniprotProteinName().toUpperCase());
                insertQProtStat.setString(4, qprot.getPublicationAccNumber().toUpperCase());
                insertQProtStat.setString(5, qprot.getPublicationProteinName().toUpperCase());
                insertQProtStat.setString(6, qprot.getRawDataAvailable().toUpperCase());
                insertQProtStat.setString(7, qprot.getTypeOfStudy().toUpperCase());
                insertQProtStat.setString(8, qprot.getSampleType().toUpperCase());
                insertQProtStat.setString(9, qprot.getPatientGroupI().toUpperCase());
                insertQProtStat.setString(10, qprot.getPatientSubGroupI().toUpperCase());
                insertQProtStat.setString(11, qprot.getPatientGrIComment().toUpperCase());
                insertQProtStat.setString(12, qprot.getPatientGroupII().toUpperCase());
                insertQProtStat.setString(13, qprot.getPatientSubGroupII().toUpperCase());
                insertQProtStat.setString(14, qprot.getPatientGrIIComment().toUpperCase());
                insertQProtStat.setString(15, qprot.getSampleMatching().toUpperCase());
                insertQProtStat.setString(16, qprot.getNormalizationStrategy().toUpperCase());
                insertQProtStat.setString(17, qprot.getTechnology().toUpperCase());
                insertQProtStat.setString(18, qprot.getAnalyticalApproach().toUpperCase());
                insertQProtStat.setString(19, qprot.getEnzyme().toUpperCase());
                insertQProtStat.setString(20, qprot.getShotgunOrTargetedQquant());
                insertQProtStat.setString(21, qprot.getQuantificationBasis());
                insertQProtStat.setString(22, qprot.getQuantBasisComment());
                insertQProtStat.setString(23, qprot.getAdditionalComments());

                insertQProtStat.setString(24, qprot.getqPeptideKey());
                insertQProtStat.setString(25, qprot.getPeptideSequence());
                insertQProtStat.setString(26, qprot.getPeptideModification());
                insertQProtStat.setString(27, qprot.getModificationComment());
                insertQProtStat.setString(28, qprot.getStringFCValue());
                insertQProtStat.setString(29, qprot.getStringPValue());

                Integer num = qprot.getQuantifiedProteinsNumber();

                insertQProtStat.setInt(30, num);

                num = qprot.getPeptideIdNumb();
                insertQProtStat.setInt(31, num);

                num = qprot.getQuantifiedPeptidesNumber();
                insertQProtStat.setInt(32, num);

                num = qprot.getPatientsGroupINumber();
                insertQProtStat.setInt(33, num);
                num = qprot.getPatientsGroupIINumber();

                insertQProtStat.setInt(34, num);

                Double dnum = qprot.getpValue();
                insertQProtStat.setDouble(35, dnum);

                dnum = qprot.getRocAuc();
                insertQProtStat.setDouble(36, dnum);

                dnum = qprot.getFcPatientGroupIonPatientGroupII();
                insertQProtStat.setDouble(37, dnum);
                insertQProtStat.setString(38, String.valueOf(qprot.isPeptideProt()));

                insertQProtStat.executeUpdate();

                insertQProtStat.clearParameters();
                insertQProtStat.close();

            }

        } catch (SQLException e) {
            System.err.println("at error line 3360 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            success = false;
        } catch (ClassNotFoundException exp) {
            System.err.println("at error line 3363 " + this.getClass().getName() + "   " + exp.getLocalizedMessage());
            success = false;
        } catch (InstantiationException exp) {
            System.err.println("at error line 3366 " + this.getClass().getName() + "   " + exp.getLocalizedMessage());
            success = false;
        } catch (IllegalAccessException exp) {
            System.err.println("at error line 3369 " + this.getClass().getName() + "   " + exp.getLocalizedMessage());
            success = false;
        }

        return success;

    }

    /**
     * Get quant proteins list for quant dataset
     *
     * @param quantDatasetId
     * @return quant proteins list
     */
    public Set<QuantProtein> getQuantificationProteins(int quantDatasetId) {
        Set<QuantProtein> quantProtList = new HashSet<QuantProtein>();
        try {

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String selectDsGroupNum = "SELECT `patients_group_i_number` , `patients_group_ii_number` FROM `quant_dataset_table` Where  `index`=?;";
            PreparedStatement selectselectDsGroupNumStat = conn.prepareStatement(selectDsGroupNum);
            selectselectDsGroupNumStat.setInt(1, quantDatasetId);
            ResultSet rs = selectselectDsGroupNumStat.executeQuery();
            int groupINum = 0;
            int groupIINum = 0;
            while (rs.next()) {
                groupINum = rs.getInt("patients_group_i_number");
                groupIINum = rs.getInt("patients_group_ii_number");
            }
            rs.close();

            String selectQuantProt = "SELECT * FROM `quantitative_proteins_table`  where `ds_ID` = ?;";
            PreparedStatement selectQuantProtStat = conn.prepareStatement(selectQuantProt);
            selectQuantProtStat.setInt(1, quantDatasetId);
            ResultSet rs1 = selectQuantProtStat.executeQuery();
            while (rs1.next()) {
                QuantProtein quantProt = new QuantProtein();
                quantProt.setPatientsGroupIINumber(groupIINum);
                quantProt.setPatientsGroupINumber(groupINum);
                quantProt.setProtKey(rs1.getInt("index"));
                quantProt.setDsKey(quantDatasetId);
                quantProt.setSequence(rs1.getString("sequance"));
                if (rs1.getString("uniprot_accession").equalsIgnoreCase("Not Available")) {
                    quantProt.setUniprotAccession(rs1.getString("publication_acc_number"));
                } else {
                    quantProt.setUniprotAccession(rs1.getString("uniprot_accession"));
                }
                quantProt.setUniprotProteinName(rs1.getString("uniprot_protein_name"));
                quantProt.setPublicationAccNumber(rs1.getString("publication_acc_number"));
                quantProt.setPublicationProteinName(rs1.getString("publication_protein_name"));
                quantProt.setQuantifiedPeptidesNumber(rs1.getInt("quantified_peptides_number"));
                quantProt.setIdentifiedProteinsNum(rs1.getInt("identified_peptides_number"));
                quantProt.setStringFCValue(rs1.getString("fold_change"));
                quantProt.setQuantBasisComment(rs1.getString("quant_bases_comments"));
                quantProtList.add(quantProt);
            }
            rs1.close();
        } catch (ClassNotFoundException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (InstantiationException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (SQLException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        }
        System.gc();
        return quantProtList;

    }

    /**
     * Get quant proteins list for a number of quant datasets
     *
     * @param quantDatasetIds
     * @return quant proteins list
     */
    public Set<QuantProtein> getQuantificationProteins(Object[] quantDatasetIds) {

        Set<QuantProtein> quantProtList = new HashSet<QuantProtein>();
        Set<QuantProtein> tquantProtList = new HashSet<QuantProtein>();
        try {
            StringBuilder sb = new StringBuilder();

            for (Object index : quantDatasetIds) {
                sb.append("  `index` = ").append(index);
                sb.append(" OR ");

            }
            String stat = sb.toString().substring(0, sb.length() - 4);
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String selectDsGroupNum = "SELECT `disease_category` ,`patients_group_i_number` , `patients_group_ii_number`,`patient_group_i`,`patient_group_ii`,`patient_sub_group_i`,`patient_sub_group_ii`,`index` FROM `quant_dataset_table` WHERE  " + stat + " ;";
            PreparedStatement selectselectDsGroupNumStat = conn.prepareStatement(selectDsGroupNum);
            ResultSet rs = selectselectDsGroupNumStat.executeQuery();
            Map<Integer, Object[]> datasetIdDesGrs = new HashMap<Integer, Object[]>();
            while (rs.next()) {
                datasetIdDesGrs.put(rs.getInt("index"), new Object[]{rs.getInt("patients_group_i_number"), rs.getInt("patients_group_ii_number"), rs.getString("patient_group_i").trim(), rs.getString("patient_group_ii").trim(), rs.getString("patient_sub_group_i").trim(), rs.getString("patient_sub_group_ii").trim(), rs.getString("disease_category")});
            }
            rs.close();
            sb = new StringBuilder();
            for (Object index : quantDatasetIds) {
                sb.append("  `ds_ID` = ").append(index);
                sb.append(" OR ");

            }
            stat = sb.toString().substring(0, sb.length() - 4);
            String selectQuantProt = "SELECT * FROM `quantitative_proteins_table`  WHERE  " + stat + " ;";
            PreparedStatement selectQuantProtStat = conn.prepareStatement(selectQuantProt);
            ResultSet rs1 = selectQuantProtStat.executeQuery();
            while (rs1.next()) {
                QuantProtein quantProt = new QuantProtein();
                quantProt.setProtKey(rs1.getInt("index"));
                quantProt.setDsKey(rs1.getInt("ds_ID"));
                quantProt.setSequence(rs1.getString("sequance"));
                quantProt.setUniprotAccession(rs1.getString("uniprot_accession"));
                quantProt.setUniprotProteinName(rs1.getString("uniprot_protein_name"));
                quantProt.setPublicationAccNumber(rs1.getString("publication_acc_number"));
                quantProt.setPublicationProteinName(rs1.getString("publication_protein_name"));
                quantProt.setQuantifiedPeptidesNumber(rs1.getInt("quantified_peptides_number"));
                quantProt.setIdentifiedProteinsNum(rs1.getInt("identified_peptides_number"));
                quantProt.setStringFCValue(rs1.getString("fold_change"));
                quantProt.setStringPValue(rs1.getString("string_p_value"));
                quantProt.setFcPatientGroupIonPatientGroupII(rs1.getDouble("log_2_FC"));
                quantProt.setRocAuc(rs1.getDouble("roc_auc"));
                quantProt.setpValue(rs1.getDouble("p_value"));
                quantProt.setPvalueComment(rs1.getString("p_value_comments"));
                quantProt.setPvalueSignificanceThreshold(rs1.getString("pvalue_significance_threshold"));
                quantProt.setAdditionalComments(rs1.getString("additional_comments"));
                quantProt.setQuantBasisComment(rs1.getString("quant_bases_comments"));
                quantProtList.add(quantProt);

            }
            rs1.close();
            System.gc();
            for (QuantProtein qp : quantProtList) {
                qp.setPatientsGroupIINumber((Integer) datasetIdDesGrs.get(qp.getDsKey())[1]);
                qp.setPatientsGroupINumber((Integer) datasetIdDesGrs.get(qp.getDsKey())[0]);
//                "\n" + disease_category.replace(" ","_").replace("'", "-")+"_Disease"
                qp.setPatientGroupI(datasetIdDesGrs.get(qp.getDsKey())[2].toString() + "\n" + datasetIdDesGrs.get(qp.getDsKey())[6].toString().replace(" ", "_").replace("'", "-") + "_Disease");//datasetIdDesGrs.get(qp.getDsKey())[2].toString() + "\n" + datasetIdDesGrs.get(qp.getDsKey())[6].toString());
                qp.setPatientGroupII(datasetIdDesGrs.get(qp.getDsKey())[3].toString() + "\n" + datasetIdDesGrs.get(qp.getDsKey())[6].toString().replace(" ", "_").replace("'", "-") + "_Disease");//datasetIdDesGrs.get(qp.getDsKey())[2].toString() + "\n" + datasetIdDesGrs.get(qp.getDsKey())[6].toString());

                qp.setPatientSubGroupI(datasetIdDesGrs.get(qp.getDsKey())[4].toString() + "\n" + datasetIdDesGrs.get(qp.getDsKey())[6].toString().replace(" ", "_").replace("'", "-") + "_Disease");
                qp.setPatientSubGroupII(datasetIdDesGrs.get(qp.getDsKey())[5].toString() + "\n" + datasetIdDesGrs.get(qp.getDsKey())[6].toString().replace(" ", "_").replace("'", "-") + "_Disease");

                qp.setDiseaseCategory(datasetIdDesGrs.get(qp.getDsKey())[6].toString());
//                qp.setPatientGroupI(datasetIdDesGrs.get(qp.getDsKey())[2].toString() + "\n" + datasetIdDesGrs.get(qp.getDsKey())[6].toString());
//                qp.setPatientGroupII(datasetIdDesGrs.get(qp.getDsKey())[3].toString() + "\n" + datasetIdDesGrs.get(qp.getDsKey())[6].toString());
//                qp.setPatientSubGroupI(datasetIdDesGrs.get(qp.getDsKey())[4].toString() + "\n" + datasetIdDesGrs.get(qp.getDsKey())[6].toString());
//                qp.setPatientSubGroupII(datasetIdDesGrs.get(qp.getDsKey())[5].toString() + "\n" + datasetIdDesGrs.get(qp.getDsKey())[6].toString());
                tquantProtList.add(qp);

            }

        } catch (ClassNotFoundException exp) {
            System.out.println("at error  " + this.getClass().getName() + "   line 3521   " + exp.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException exp) {
            System.out.println("at error  " + this.getClass().getName() + "   line 3524   " + exp.getLocalizedMessage());
            return null;
        } catch (InstantiationException exp) {
            System.out.println("at error  " + this.getClass().getName() + "   line 3527   " + exp.getLocalizedMessage());
            return null;
        } catch (SQLException exp) {
            System.out.println("at error  " + this.getClass().getName() + "   line 3530   " + exp.getLocalizedMessage());
            return null;
        }
        System.gc();
        return tquantProtList;

    }

    /**
     * Get quant peptides list for specific quant dataset
     *
     * @param quantDatasetId
     * @return quant peptides list
     */
    public Map<String, Set<QuantPeptide>> getQuantificationPeptides(int quantDatasetId) {
        Set<QuantPeptide> quantPeptidetList = new HashSet<QuantPeptide>();
        try {

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String selectQuantPeptides = "SELECT * FROM `quantitative_peptides_table` WHERE `DsKey`=?;";
            PreparedStatement selectQuantProtStat = conn.prepareStatement(selectQuantPeptides);
            selectQuantProtStat.setInt(1, quantDatasetId);
            ResultSet rs1 = selectQuantProtStat.executeQuery();
            while (rs1.next()) {
                QuantPeptide quantPeptide = new QuantPeptide();
                quantPeptide.setDsKey(quantDatasetId);
                quantPeptide.setProtIndex(rs1.getInt("prot_index"));
                quantPeptide.setUniqueId(rs1.getInt("index"));
                quantPeptide.setPeptideModification(rs1.getString("peptide_modification"));
                quantPeptide.setPeptideSequence(rs1.getString("peptide_sequance"));
                quantPeptide.setModification_comment(rs1.getString("modification_comment"));
                quantPeptide.setString_fc_value(rs1.getString("string_fc_value"));
                quantPeptide.setString_p_value(rs1.getString("string_p_value"));
                quantPeptide.setP_value(rs1.getDouble("p_value"));
                quantPeptide.setRoc_auc(rs1.getDouble("roc_auc"));
                quantPeptide.setFc_value(rs1.getDouble("fc_value"));
                quantPeptide.setP_value_comments(rs1.getString("p_value_comments"));
                quantPeptide.setUniprotProteinAccession(rs1.getString("proteinAccession"));
                quantPeptidetList.add(quantPeptide);
            }
            rs1.close();

            Map<String, Set<QuantPeptide>> quantProtPeptidetList = new HashMap<String, Set<QuantPeptide>>();
            for (QuantPeptide qp : quantPeptidetList) {
                if (!quantProtPeptidetList.containsKey("_" + qp.getUniprotProteinAccession() + "_" + qp.getProtIndex() + "_")) {
                    Set<QuantPeptide> quantPepProtSet = new HashSet<QuantPeptide>();
                    quantProtPeptidetList.put("_" + qp.getUniprotProteinAccession() + "_" + qp.getProtIndex() + "_", quantPepProtSet);

                }
                Set<QuantPeptide> quantPepProtSet = quantProtPeptidetList.get("_" + qp.getUniprotProteinAccession() + "_" + qp.getProtIndex() + "_");
                quantPepProtSet.add(qp);

                quantProtPeptidetList.put("_" + qp.getUniprotProteinAccession() + "_" + qp.getProtIndex() + "_", quantPepProtSet);

            }
            System.gc();
            return quantProtPeptidetList;
        } catch (ClassNotFoundException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (InstantiationException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (SQLException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Get quant peptides list for specific quant dataset
     *
     * @param quantDatasetIds
     * @return quant peptides list
     */
    public Map<String, Set<QuantPeptide>> getQuantificationPeptides(Object[] quantDatasetIds) {
        Set<QuantPeptide> quantPeptidetList = new HashSet<QuantPeptide>();
        try {
            StringBuilder sb = new StringBuilder();

            for (Object index : quantDatasetIds) {
                sb.append("  `DsKey` = ").append(index);
                sb.append(" OR ");

            }
            String stat = sb.toString().substring(0, sb.length() - 4);

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String selectQuantPeptides = "SELECT * FROM `quantitative_peptides_table` WHERE " + stat;
            PreparedStatement selectQuantProtStat = conn.prepareStatement(selectQuantPeptides);
            ResultSet rs1 = selectQuantProtStat.executeQuery();
            while (rs1.next()) {
                QuantPeptide quantPeptide = new QuantPeptide();
                quantPeptide.setDsKey(rs1.getInt("DsKey"));
                quantPeptide.setProtIndex(rs1.getInt("prot_index"));
                quantPeptide.setUniqueId(rs1.getInt("index"));
                quantPeptide.setPeptideModification(rs1.getString("peptide_modification"));
                quantPeptide.setPeptideSequence(rs1.getString("peptide_sequance"));
                quantPeptide.setModification_comment(rs1.getString("modification_comment"));
                quantPeptide.setString_fc_value(rs1.getString("string_fc_value"));
                quantPeptide.setString_p_value(rs1.getString("string_p_value"));
                quantPeptide.setP_value(rs1.getDouble("p_value"));
                quantPeptide.setRoc_auc(rs1.getDouble("roc_auc"));
                quantPeptide.setFc_value(rs1.getDouble("log_2_FC"));
                quantPeptide.setP_value_comments(rs1.getString("p_value_comments"));
                quantPeptide.setUniprotProteinAccession(rs1.getString("proteinAccession"));
                quantPeptide.setSequenceAnnotated(rs1.getString("sequence_annotated"));
                quantPeptide.setPvalueSignificanceThreshold(rs1.getString("pvalue_significance_threshold"));
                quantPeptide.setPeptideCharge(rs1.getInt("peptide_charge"));
                quantPeptide.setAdditionalComments(rs1.getString("additional_comments"));
                quantPeptide.setQuantBasisComment(rs1.getString("quant_bases_comments"));
                quantPeptidetList.add(quantPeptide);
            }
            rs1.close();

            Map<String, Set<QuantPeptide>> quantProtPeptidetList = new HashMap<String, Set<QuantPeptide>>();
            for (QuantPeptide qp : quantPeptidetList) {

                if (!quantProtPeptidetList.containsKey("_" + qp.getProtIndex() + "_" + qp.getDsKey() + "_")) {
                    Set<QuantPeptide> quantPepProtSet = new HashSet<QuantPeptide>();
                    quantProtPeptidetList.put("_" + qp.getProtIndex() + "_" + qp.getDsKey() + "_", quantPepProtSet);

                }
                Set<QuantPeptide> quantPepProtSet = quantProtPeptidetList.get("_" + qp.getProtIndex() + "_" + qp.getDsKey() + "_");
                quantPepProtSet.add(qp);
                quantProtPeptidetList.put("_" + qp.getProtIndex() + "__" + qp.getDsKey() + "__", quantPepProtSet);

            }
            System.gc();

            return quantProtPeptidetList;
        } catch (ClassNotFoundException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (InstantiationException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        } catch (SQLException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Fill quant proteins information from the result set
     *
     * @param resultSet results set to fill identification peptides data
     * @return quant proteins List
     */
    private List<QuantProtein> fillQuantProtData(ResultSet resultSet) {
        List<QuantProtein> quantProtResultList = new ArrayList<QuantProtein>();

        try {
            while (resultSet.next()) {

                QuantProtein quantProt = new QuantProtein();
                quantProt.setProtKey(resultSet.getInt("index"));
                quantProt.setDsKey(resultSet.getInt("ds_ID"));
                quantProt.setSequence(resultSet.getString("sequance"));
                quantProt.setUniprotAccession(resultSet.getString("uniprot_accession"));

                quantProt.setUniprotProteinName(resultSet.getString("uniprot_protein_name"));
                quantProt.setPublicationAccNumber(resultSet.getString("publication_acc_number"));
                quantProt.setPublicationProteinName(resultSet.getString("publication_protein_name"));
                quantProt.setQuantifiedPeptidesNumber(resultSet.getInt("quantified_peptides_number"));
                quantProt.setIdentifiedProteinsNum(resultSet.getInt("identified_peptides_number"));
                quantProt.setStringFCValue(resultSet.getString("fold_change"));
                quantProt.setStringPValue(resultSet.getString("string_p_value"));
                quantProt.setFcPatientGroupIonPatientGroupII(resultSet.getDouble("log_2_FC"));
                quantProt.setRocAuc(resultSet.getDouble("roc_auc"));
                quantProt.setpValue(resultSet.getDouble("p_value"));
                quantProt.setPvalueComment(resultSet.getString("p_value_comments"));
                quantProt.setPvalueSignificanceThreshold(resultSet.getString("pvalue_significance_threshold"));
                quantProt.setAdditionalComments(resultSet.getString("additional_comments"));
                quantProt.setQuantBasisComment(resultSet.getString("quant_bases_comments"));

                quantProtResultList.add(quantProt);

            }

        } catch (Exception exp) {
            System.err.println("at error line 2119 " + this.getClass().getName() + "   " + exp.getLocalizedMessage());
        }
        return quantProtResultList;
    }

    /**
     * Search for quant proteins by keywords
     *
     * @param query query object that has all query information
     * @param toCompare
     * @return Quant Proteins Searching List
     */
    public List<QuantProtein> searchQuantificationProteins(Query query, boolean toCompare) {

        StringBuilder sb = new StringBuilder();
        QueryConstractorHandler qhandler = new QueryConstractorHandler();
        PreparedStatement selectProStat;
        String selectPro;
        //main filters  
        if (query.getSearchKeyWords() != null && !query.getSearchKeyWords().equalsIgnoreCase("")) {
            String[] queryWordsArr = query.getSearchKeyWords().split("\n");
            HashSet<String> searchSet = new HashSet<String>();
            for (String str : queryWordsArr) {
                if (str.trim().equalsIgnoreCase("")) {
                    continue;
                }
                searchSet.add(str.trim());
            }
            if (query.getSearchBy().equalsIgnoreCase("Protein Accession")) {
                int x = 0;
                for (String str : searchSet) {
                    if (x > 0) {
                        sb.append(" OR ");
                    }
                    if (toCompare) {
                        sb.append("`uniprot_accession` = ?");
                        qhandler.addQueryParam("String", str);
                    } else {
                        sb.append("`uniprot_accession` = ? OR `publication_acc_number` = ?");
                        qhandler.addQueryParam("String", str);
                        qhandler.addQueryParam("String", str);
                    }

                    x++;
                }
            } else if (query.getSearchBy().equalsIgnoreCase("Protein Name")) {
                int x = 0;
                for (String str : searchSet) {
                    if (x > 0) {
                        sb.append(" OR ");
                    }
                    sb.append("`uniprot_protein_name` LIKE(?) ");
                    qhandler.addQueryParam("String", "%" + str + "%");
                    x++;
                }
            } else {  //peptide sequance
                int x = 0;
                for (String str : searchSet) {
                    if (x > 0) {
                        sb.append(" OR ");
                    }
                    sb.append("`sequance` LIKE (?)");//
                    qhandler.addQueryParam("String", "%" + str + "%");
                    x++;
                }
            }
        }
        selectPro = "SELECT * FROM   `quantitative_proteins_table`  Where " + (sb.toString());
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProStat = conn.prepareStatement(selectPro);
            selectProStat = qhandler.initStatment(selectProStat);

            ResultSet rs = selectProStat.executeQuery();
            List<QuantProtein> quantProtResultList = fillQuantProtData(rs);
            System.gc();

            Set<Integer> quantDatasetIds = new HashSet<Integer>();
            for (QuantProtein quantProt : quantProtResultList) {

                quantDatasetIds.add(quantProt.getDsKey());
            }
            if (quantDatasetIds.isEmpty()) {
                return new ArrayList<QuantProtein>();

            }
            sb = new StringBuilder();
            for (Object index : quantDatasetIds) {
                sb.append("  `index` = ").append(index);
                sb.append(" OR ");

            }

            String stat = sb.toString().substring(0, sb.length() - 4);
            String selectDsGroupNum = "SELECT `disease_category`,`pumed_id` ,`index` ,`patients_group_i_number` , `patients_group_ii_number`,`patient_group_i`,`patient_group_ii`,`patient_sub_group_i`,`patient_sub_group_ii` FROM `quant_dataset_table` Where  " + stat + ";"; //"SELECT `patients_group_i_number` , `patients_group_ii_number`,`patient_group_i`,`patient_group_ii`,`patient_sub_group_i`,`patient_sub_group_ii`,`index` FROM `quant_dataset_table` WHERE  " + stat + " ;";

            PreparedStatement selectselectDsGroupNumStat = conn.prepareStatement(selectDsGroupNum);
            rs = selectselectDsGroupNumStat.executeQuery();
            Map<Integer, Object[]> datasetIdDesGrs = new HashMap<Integer, Object[]>();
            while (rs.next()) {
                datasetIdDesGrs.put(rs.getInt("index"), new Object[]{rs.getInt("patients_group_i_number"), rs.getInt("patients_group_ii_number"), rs.getString("patient_group_i").trim(), rs.getString("patient_group_ii").trim(), rs.getString("patient_sub_group_i").trim(), rs.getString("patient_sub_group_ii").trim(), rs.getString("pumed_id"), rs.getString("disease_category")});
            }
            rs.close();

//            Map<Integer, int[]> dsDGrNumMap = new HashMap<Integer, int[]>();
//            for (int i : datasetsIds) {
//                selectselectDsGroupNumStat.setInt(1, i);
//                ResultSet dsIdRs = selectselectDsGroupNumStat.executeQuery();
//
//                while (dsIdRs.next()) {
//                    int[] grNumArr = new int[]{dsIdRs.getInt("patients_group_i_number"), dsIdRs.getInt("patients_group_ii_number")};
//                    dsDGrNumMap.put(i, grNumArr);
//                }
//                dsIdRs.close();
//            }
            List<QuantProtein> updatedQuantProtResultList = new ArrayList<QuantProtein>();
            for (QuantProtein quantProt : quantProtResultList) {

                if (datasetIdDesGrs.containsKey(quantProt.getDsKey())) {
                    Object[] grNumArr = datasetIdDesGrs.get(quantProt.getDsKey());
                    quantProt.setPatientsGroupINumber((Integer) grNumArr[0]);
                    quantProt.setPatientsGroupIINumber((Integer) grNumArr[1]);
                    quantProt.setPatientGroupI((String) grNumArr[2] + "\n" + grNumArr[7].toString().replace(" ", "_").replace("'", "-") + "_Disease");
                    quantProt.setPatientGroupII((String) grNumArr[3] + "\n" + grNumArr[7].toString().replace(" ", "_").replace("'", "-") + "_Disease");
                    quantProt.setPatientSubGroupI((String) grNumArr[4] + "\n" + grNumArr[7].toString().replace(" ", "_").replace("'", "-") + "_Disease");
                    quantProt.setPatientSubGroupII((String) grNumArr[5] + "\n" + grNumArr[7].toString().replace(" ", "_").replace("'", "-") + "_Disease");

                    quantProt.setDiseaseCategory(grNumArr[7].toString());

                    quantProt.setPumedID((String) grNumArr[6]);
                    updatedQuantProtResultList.add(quantProt);
                }
            }
            return updatedQuantProtResultList;

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 3654 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException e) {
            System.err.println("at error line 3657 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return null;
        } catch (InstantiationException e) {
            System.err.println("at error line 3660 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("at error line 3665 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return null;
        }

    }

    /**
     * Get all required informations for the resource statues
     *
     * @return OverviewInfoBean
     */
    public OverviewInfoBean getResourceOverviewInformation() {

        OverviewInfoBean infoBean = new OverviewInfoBean();

        try {

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String selectIdPublicationStudies = "SELECT COUNT(*) AS `Rows`, `pblication_link` FROM `experiments_table` GROUP BY `pblication_link` ORDER BY `pblication_link`";
            PreparedStatement selectIdPublicationStudiesStat = conn.prepareStatement(selectIdPublicationStudies);

            ResultSet rs = selectIdPublicationStudiesStat.executeQuery();
            int numStudies = 0;
            int numPublications = 0;

            while (rs.next()) {
                numStudies += rs.getInt("Rows");
                numPublications++;

            }
            infoBean.setNumberOfIdPublication(numPublications);
            infoBean.setNumberOfIdStudies(numStudies);

            rs.close();
//
//            String selectIdProteinsNumber = "SELECT COUNT( DISTINCT  `prot_accession` ) AS `Rows` FROM  `experiment_protein_table` where `valid`='TRUE'  ;";
//            PreparedStatement selectIdProteinsNumberStat = conn.prepareStatement(selectIdProteinsNumber);
//
//             rs = selectIdProteinsNumberStat.executeQuery();
            int numProteins;
//
//            while (rs.next()) {
//                numProteins += rs.getInt("Rows");
//            }
//            infoBean.setNumberOfIdProteins(numProteins);
//            rs.close();
//            
//            
//            String selectIdPeptidesNumber = "SELECT COUNT( DISTINCT  `sequence` ) AS `Rows` FROM  `proteins_peptides_table` ;";
//            PreparedStatement selectIdPeptidesNumberStat = conn.prepareStatement(selectIdPeptidesNumber);
//
//             rs = selectIdPeptidesNumberStat.executeQuery();
            int numPeptides;
//
//            while (rs.next()) {
//                numPeptides += rs.getInt("Rows");
//            }
//            infoBean.setNumberOfIdPeptides(numPeptides);
//            rs.close();
            infoBean.setNumberOfIdProteins(3081);
            infoBean.setNumberOfIdPeptides(28811);
            //quant data

            String selectQuantPublicationStudies = "SELECT COUNT( * ) AS  `Rows` ,  `pumed_id` FROM  `quant_dataset_table` GROUP BY  `pumed_id` ORDER BY  `pumed_id` ";
            PreparedStatement selectQuantPublicationStudiesStat = conn.prepareStatement(selectQuantPublicationStudies);

            rs = selectQuantPublicationStudiesStat.executeQuery();
            numStudies = 0;
            numPublications = 0;

            while (rs.next()) {
                numStudies += rs.getInt("Rows");
                numPublications++;

            }
            infoBean.setNumberOfQuantPublication(numPublications);
            infoBean.setNumberOfQuantStudies(numStudies);

            rs.close();

            String selectQuantProteinsNumber = "SELECT COUNT( DISTINCT  `publication_acc_number` ,  `uniprot_accession` ) AS  `Rows` FROM  `quantitative_proteins_table` ;";
            PreparedStatement selectQuantProteinsNumberStat = conn.prepareStatement(selectQuantProteinsNumber);

            rs = selectQuantProteinsNumberStat.executeQuery();
            numProteins = 0;

            while (rs.next()) {
                numProteins += rs.getInt("Rows");
            }
            infoBean.setNumberOfQuantProteins(numProteins);
            rs.close();

            String selectQuantPeptidesNumber = "SELECT COUNT( DISTINCT  `peptide_sequance` ) AS `Rows` FROM  `quantitative_peptides_table` ;";
            PreparedStatement selectQuantPeptidesNumberStat = conn.prepareStatement(selectQuantPeptidesNumber);

            rs = selectQuantPeptidesNumberStat.executeQuery();
            numPeptides = 0;

            while (rs.next()) {
                numPeptides += rs.getInt("Rows");
            }
            infoBean.setNumberOfQuantPeptides(numPeptides);
            rs.close();

        } catch (SQLException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 3143  " + e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 3143  " + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 3143  " + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 3143  " + e.getLocalizedMessage());
        }

        return infoBean;

    }

    /**
     * Get map for full disease name
     *
     * @return map of the short and long diseases names
     */
    public Map<String, String> getDiseaseFullNameMap() {
        Map diseaseFullNameMap = new HashMap<String, String>();
        String selectAllDiseFullName = "SELECT * FROM  `defin_disease_groups` ";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            PreparedStatement selectProStat = conn.prepareStatement(selectAllDiseFullName);

            ResultSet rs = selectProStat.executeQuery();
            System.gc();
            while (rs.next()) {
                diseaseFullNameMap.put(rs.getString("min").trim(), rs.getString("full").trim());
            }
        } catch (SQLException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 3167  " + e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 3167  " + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 3167  " + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 3167  " + e.getLocalizedMessage());
        }

        return diseaseFullNameMap;
    }

    private String updateStringFormat(String str) {
        str = str.toLowerCase();
        if (str.contains("csf ")) {
            str = str.replace("csf", "CSF");
        }
        str = str.replaceFirst(str.substring(0, 1), str.substring(0, 1).toUpperCase());

        return str;

    }

    /**
     * Get set of disease groups names for special disease category
     *
     * @param diseaseCat
     * @return map of the short and long diseases names
     */
    public Set<String> getDiseaseGroupNameMap(String diseaseCat) {
        Set<String> diseaseNames = new HashSet<String>();
        String selectPatient_sub_group_i = "SELECT `patient_sub_group_i` FROM `quant_dataset_table`  where `disease_category` = ? GROUP BY `patient_sub_group_i` ORDER BY `patient_sub_group_i` ";

        String selectPatient_sub_group_ii = "SELECT `patient_sub_group_ii` FROM `quant_dataset_table`  where `disease_category` = ? GROUP BY `patient_sub_group_ii` ORDER BY `patient_sub_group_ii` ";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            PreparedStatement selectProStat = conn.prepareStatement(selectPatient_sub_group_i);
            selectProStat.setString(1, diseaseCat);
            ResultSet rs = selectProStat.executeQuery();
            System.gc();
            while (rs.next()) {
                diseaseNames.add(rs.getString("patient_sub_group_i").trim());
            }
            rs.close();

            selectProStat = conn.prepareStatement(selectPatient_sub_group_ii);
            selectProStat.setString(1, diseaseCat);
            rs = selectProStat.executeQuery();
            System.gc();
            while (rs.next()) {
                diseaseNames.add(rs.getString("patient_sub_group_ii").trim());
            }

        } catch (SQLException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 3167  " + e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 3167  " + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 3167  " + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 3167  " + e.getLocalizedMessage());
        }

        return diseaseNames;
    }

    public List<Object[]> getPublicationList() {

        List<Object[]> publicationList = new ArrayList<Object[]>();
        String selectStat = "SELECT * FROM  `publication_table` WHERE `active`='true' ORDER BY  `publication_table`.`year` DESC";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            PreparedStatement st = conn.prepareStatement(selectStat);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                publicationList.add(new Object[]{rs.getString("pubmed_id"), rs.getString("author"), rs.getString("year"), rs.getString("title"), rs.getInt("uniq_prot_num"), rs.getInt("total_prot_num"), rs.getInt("uniq_pept_num"), rs.getInt("total_pept_num")});
            }
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (Exception e) {
            System.err.println("at error" + e.getLocalizedMessage());
        }
        System.gc();
        return publicationList;

    }

    public Set<QuantDatasetObject> getQuantDatasetList() {
        Set<QuantDatasetObject> dsObjects = new TreeSet<QuantDatasetObject>();

        Map<String, QuantDatasetInitialInformationObject> diseaseCategoriesMap = getQuantDatasetInitialInformationObject();
        for (QuantDatasetInitialInformationObject qi : diseaseCategoriesMap.values()) {
            dsObjects.addAll(qi.getQuantDatasetsList().values());

        }

        return dsObjects;

    }

}
