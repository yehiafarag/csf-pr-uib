/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.database;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetInitialInformationObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;
import no.uib.probe.csf.pr.touch.view.smallscreen.OverviewInfoBean;

/**
 *
 * @author Yehia Farag
 *
 * this class is an abstract for the database layer this class interact with
 * logic layer this class contains all the database queries
 */
public class DataBaseLayer implements Serializable {

    private static final long serialVersionUID = 1L;
    private Connection conn = null;
//    private Connection conn_i = null;
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
    public DataBaseLayer(String url, String dbName, String driver, String userName, String password) {
        this.url = url;
        this.dbName = dbName;
        this.driver = driver;
        this.userName = userName;
        this.password = password;
    }

    /**
     * this method responsible for getting the resource overview information
     *
     * @return OverviewInfoBean resource information bean
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

        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            System.err.println("at error " + this.getClass().getName() + "  line 152  " + e.getLocalizedMessage());
        }

        return infoBean;

    }

    /**
     * this method responsible for getting initial publication information
     *
     * @return list of publications available in the the resource
     */
    public List<Object[]> getPublicationList() {

        List<Object[]> publicationList = new ArrayList<>();
        String selectStat = "SELECT * FROM  `publication_table` WHERE `active`='true' ORDER BY  `publication_table`.`year` DESC ,`publication_table`.`author` ";
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
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 184  " + e.getLocalizedMessage());
        }
        System.gc();
        return publicationList;

    }

    /**
     * this method responsible for getting initial datasets information
     *
     * @return set of datasets information available in the the resource
     */
    public Set<QuantDatasetObject> getQuantDatasetList() {
        Set<QuantDatasetObject> dsObjects = new TreeSet<>();

        Map<String, QuantDatasetInitialInformationObject> diseaseCategoriesMap = getQuantDatasetInitialInformationObject();
        diseaseCategoriesMap.values().stream().forEach((qi) -> {
            dsObjects.addAll(qi.getQuantDatasetsList().values());
        });

        return dsObjects;

    }

    /**
     * Get available quantification datasets initial information object that
     * contains the available datasets list and the active columns (to hide them
     * if they are empty)
     *
     * @return QuantDatasetInitialInformationObject
     */
    public Map<String, QuantDatasetInitialInformationObject> getQuantDatasetInitialInformationObject() {
        Map<String, QuantDatasetInitialInformationObject> diseaseCategoriesMap = new LinkedHashMap<>();
        try {
            PreparedStatement selectStudiesStat;
            String selectStudies = "SELECT * FROM  `quant_dataset_table` ";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectStudiesStat = conn.prepareStatement(selectStudies);
            try (ResultSet rs = selectStudiesStat.executeQuery()) {
                while (rs.next()) {
                    String disease_category = rs.getString("disease_category");
                    if (!diseaseCategoriesMap.containsKey(disease_category)) {
                        boolean[] activeHeaders = new boolean[27];
                        Set<String> diseaseCategories = new LinkedHashSet<>();
                        QuantDatasetInitialInformationObject datasetObject = new QuantDatasetInitialInformationObject();
                        Map<Integer, QuantDatasetObject> updatedQuantDatasetObjectMap = new LinkedHashMap<>();
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
            }
            return diseaseCategoriesMap;
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.err.println("at error line 431 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }
        System.gc();

        return null;

    }

    /**
     * Get the current available disease category list
     *
     * @return set of disease category objects that has all disease category
     * information and styling information
     */
    public Set<DiseaseCategoryObject> getDiseaseCategorySet() {

        LinkedHashSet<DiseaseCategoryObject> availableDiseaseCategory = new LinkedHashSet<>();

        String selectStat = "SELECT COUNT( * ) AS  `Rows` ,  `disease_category` FROM  `quant_dataset_table` GROUP BY  `disease_category`ORDER BY  `Rows` DESC ";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            PreparedStatement st = conn.prepareStatement(selectStat);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                DiseaseCategoryObject diseaseObject = new DiseaseCategoryObject();
                diseaseObject.setDiseaseName(rs.getString("disease_category"));
                diseaseObject.setDatasetNumber(rs.getInt("Rows"));
                availableDiseaseCategory.add(diseaseObject);
            }
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 470  " + e.getLocalizedMessage());
        }
        return availableDiseaseCategory;
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

}
