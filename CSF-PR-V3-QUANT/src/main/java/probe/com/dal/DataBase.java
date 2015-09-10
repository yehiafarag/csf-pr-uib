package probe.com.dal;

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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

/**
 * @author Yehia Farag abstraction for database queries
 */
public class DataBase implements Serializable {

    private static final long serialVersionUID = 1L;
    private Connection conn = null;
    private Connection conn_i = null;
    private final String url, dbName, driver, userName, password;
    private final DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
    private DecimalFormat df = null;

    /**
     * constructor
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
     * create database tables if not exist
     *
     * @return test boolean (successful process)
     *
     */
    public synchronized boolean createTables()//create CSF the database tables if not exist
    {
        try {
            try {
                if (conn_i == null || conn_i.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn_i = DriverManager.getConnection(url + "mysql", userName, password);
                }
                Statement statement = conn_i.createStatement();
                String csfSQL = "CREATE DATABASE IF NOT exists  " + dbName;
                statement.executeUpdate(csfSQL);

                //temp
//               fg String sqoDataBase = "SHOW DATABASES ;";
//                ResultSet rs1 = statement.executeQuery(sqoDataBase);
//               Set<String> datasetnames = new HashSet<String>();
//                while(rs1.next()){
//                    String db = rs1.getString("Database");
//                    datasetnames.add(db);
//                    System.err.println("db is "+db);
//                    
//                }
//                 Statement statement2 = conn_i.createStatement();
//                
//                for(String db:datasetnames)
//                {
//                    if(db.contains("csf")&&!db.equals(dbName))
//                        statement2.executeUpdate("DROP DATABASE "+ db+" ;");
//                
//                }
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
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            try {
                Statement st = conn.createStatement();

                String statment = "CREATE TABLE IF NOT EXISTS `combined_dataset_table` ("
                        + "  `additional_comments` varchar(700) NOT NULL default 'Not Available',"
                        + "  `pumed_id` varchar(30) NOT NULL default 'Not Available', "
                        + "  `files_num` int(255) NOT NULL default '-1', "
                        + "  `identified _proteins_num` int(255) NOT NULL default '-1', "
                        + "  `quantified_protein_num` int(255) NOT NULL default '-1', "
                        + "  `disease_group` varchar(300) NOT NULL default 'Not Available',  "
                        + "  `raw_data_url` varchar(500) NOT NULL default 'Raw Data Not Available', "
                        + "  `year` int(4) NOT NULL default '0', "
                        + "  `index` int(255) NOT NULL auto_increment, "
                        + "  `type_of_study` varchar(200) NOT NULL default 'Not Available', "
                        + "  `sample_type` varchar(200) NOT NULL default 'Not Available', "
                        + "  `sample_matching` varchar(300) NOT NULL default 'Not Available', "
                        + "  `technology` varchar(300) NOT NULL default 'Not Available', "
                        + "  `analytical_approach` varchar(300) NOT NULL default 'Not Available', "
                        + "  `enzyme` varchar(300) NOT NULL default 'Not Available', "
                        + "  `shotgun_targeted` varchar(200) NOT NULL default 'Not Available', "
                        + "  `quantification_basis` varchar(200) NOT NULL default 'Not Available', "
                        + "  `quant_basis_comment` varchar(500) NOT NULL default 'Not Available', "
                        + "  `patients_group_i_number` int(255) NOT NULL default '-1000000000', "
                        + "  `patients_group_ii_number` int(255) NOT NULL default '-1000000000', "
                        + "  `normalization_strategy` varchar(600) NOT NULL default 'Not Available', "
                        + "  `author` varchar(300) NOT NULL default 'John Smith', "
                        + "  `patient_group_i` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_gr_i_comment` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_sub_group_i` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_group_ii` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_sub_group_ii` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_gr_ii_comment` varchar(700) NOT NULL default 'Not Available', "
                        + "  KEY `index` (`index`) "
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1; ";
                st.executeUpdate(statment);

                statment = " CREATE TABLE IF NOT EXISTS `combined_quant_table`("
                        + "  `author` varchar(500) NOT NULL,"
                        + "  `year` int(255) NOT NULL,"
                        + "  `pumed_id` varchar(150) NOT NULL default 'Not Available',"
                        + "  `uniprot_accession` varchar(150) NOT NULL default 'Not Available', "
                        + "  `uniprot_protein_name` varchar(700) NOT NULL default 'Not Available', "
                        + "  `publication_acc_number` varchar(150) NOT NULL default 'Not Available',  "
                        + "  `publication_protein_name` varchar(700) NOT NULL default 'Not Available', "
                        + "  `raw_data_available` varchar(700) NOT NULL default 'Not Available', "
                        + "  `type_of_study` varchar(150) NOT NULL default 'Not Available', "
                        + "  `sample_type` varchar(150) NOT NULL default 'Not Available',  "
                        + "  `patient_group_i` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_sub_group_i` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_gr_i_comment` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_group_ii` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_sub_group_ii` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_gr_ii_comment` varchar(700) NOT NULL default 'Not Available', "
                        + "  `sample_matching` varchar(500) NOT NULL default 'Not Available', "
                        + "  `normalization_strategy` varchar(500) NOT NULL default 'Not Available', "
                        + "  `technology` varchar(500) NOT NULL default 'Not Available', "
                        + "  `analytical_approach` varchar(500) NOT NULL default 'Not Available', "
                        + "  `enzyme` varchar(500) NOT NULL default 'Not Available', "
                        + "  `shotgun_targeted` varchar(100) NOT NULL default 'Not Available', "
                        + "  `quantification_basis` varchar(500) NOT NULL default 'Not Available', "
                        + "  `quant_basis_comment` varchar(700) NOT NULL default 'Not Available', "
                        + "  `additional_comments` varchar(700) NOT NULL default 'Not Available', "
                        + "  `q_peptide_key` varchar(700) NOT NULL default 'Not Available', "
                        + "  `peptide_sequance` varchar(700) NOT NULL default 'Not Available', "
                        + "  `peptide_modification` varchar(700) NOT NULL default 'Not Available', "
                        + "  `modification_comment` varchar(700) NOT NULL default 'Not Available', "
                        + "  `string_fc_value` varchar(200) NOT NULL default 'Not Available', "
                        + "  `string_p_value` varchar(200) NOT NULL default 'Not Available', "
                        + "  `quantified_proteins_number` int(255) NOT NULL default '-1', "
                        + "  `identified _proteins_num` int(255) NOT NULL default '-1', "
                        + "  `peptideId_number` int(255) NOT NULL default '-1', "
                        + "  `quantified_peptides_number` int(255) NOT NULL default '-1', "
                        + "  `patients_group_i_number` int(255) NOT NULL default '-1', "
                        + "  `patients_group_ii_number` int(255) NOT NULL default '-1', "
                        + "  `p_value` double NOT NULL default '-1000000000', "
                        + "  `roc_auc` double NOT NULL default '-1000000000', "
                        + "  `fc_value` double NOT NULL default '-1000000000', "
                        + "  `peptide_prot` varchar(5) NOT NULL default 'False', "
                        + "  `index` int(255) NOT NULL auto_increment, "
                        + "  PRIMARY KEY  (`index`) "
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ; ";

                st.executeUpdate(statment);

                statment = "CREATE TABLE IF NOT EXISTS `experiments_table` ( "
                        + "  `exp_id` int(11) NOT NULL auto_increment, "
                        + "  `fraction_range` int(2) NOT NULL default '0', "
                        + "  `name` varchar(100) NOT NULL, "
                        + "  `fractions_number` int(11) NOT NULL default '0', "
                        + "  `ready` int(11) NOT NULL default '0', "
                        + "  `uploaded_by` varchar(100) NOT NULL, "
                        + "  `peptide_file` int(2) NOT NULL default '0', "
                        + "  `species` varchar(100) NOT NULL, "
                        + "  `sample_type` varchar(100) NOT NULL, "
                        + "  `sample_processing` varchar(100) NOT NULL, "
                        + "  `instrument_type` varchar(100) NOT NULL, "
                        + "  `frag_mode` varchar(100) NOT NULL, "
                        + "  `proteins_number` int(11) NOT NULL default '0', "
                        + "  `peptides_number` int(11) NOT NULL default '0', "
                        + "  `email` varchar(100) NOT NULL, "
                        + "  `pblication_link` varchar(300) NOT NULL default 'NOT AVAILABLE', "
                        + "  `description` varchar(1000) NOT NULL default 'NO DESCRIPTION AVAILABLE', "
                        + "  `exp_type` int(10) NOT NULL default '0', "
                        + "  `valid_prot` int(11) NOT NULL default '0', "
                        + "  PRIMARY KEY  (`exp_id`) "
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;  ";
                st.executeUpdate(statment);

                statment = "CREATE TABLE IF NOT EXISTS `experiment_fractions_table` ( "
                        + "  `exp_id` int(11) NOT NULL, "
                        + "  `fraction_id` int(11) NOT NULL auto_increment, "
                        + "  `min_range` double NOT NULL default '0', "
                        + "  `max_range` double NOT NULL default '0', "
                        + "  `index` int(11) NOT NULL default '0', "
                        + "  PRIMARY KEY  (`fraction_id`), "
                        + "  KEY `exp_id` (`exp_id`) "
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ; ";
                st.executeUpdate(statment);

                statment = "CREATE TABLE IF NOT EXISTS `experiment_peptides_proteins_table` ( "
                        + "  `exp_id` varchar(50) NOT NULL, "
                        + "  `peptide_id` int(50) NOT NULL, "
                        + "  `protein` varchar(1000) NOT NULL "
                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf8; ";
                st.executeUpdate(statment);

                statment = "CREATE TABLE IF NOT EXISTS `experiment_peptides_table` ( "
                        + "  `exp_id` int(11) NOT NULL default '0', "
                        + "  `pep_id` int(11) NOT NULL auto_increment, "
                        + "  PRIMARY KEY  (`pep_id`), "
                        + "  KEY `exp_id` (`exp_id`) "
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ; ";
                st.executeUpdate(statment);

                statment
                        = "CREATE TABLE IF NOT EXISTS `experiment_protein_table` ( "
                        + "  `exp_id` int(11) NOT NULL, "
                        + "  `prot_accession` varchar(30) NOT NULL, "
                        + "  `other_protein(s)` varchar(1000) default NULL, "
                        + "  `protein_inference_class` varchar(100) default NULL, "
                        + "  `sequence_coverage(%)` double default NULL, "
                        + "  `observable_coverage(%)` double default NULL, "
                        + "  `confident_ptm_sites` varchar(500) default NULL, "
                        + "  `number_confident` varchar(500) default NULL, "
                        + "  `other_ptm_sites` varchar(500) default NULL, "
                        + "  `number_other` varchar(500) default NULL, "
                        + "  `number_validated_peptides` int(11) default NULL, "
                        + "  `number_validated_spectra` int(11) default NULL, "
                        + "  `em_pai` double default NULL, "
                        + "  `nsaf` double default NULL, "
                        + "  `mw_(kDa)` double default NULL, "
                        + "  `score` double default NULL, "
                        + "  `confidence` double default NULL, "
                        + "  `starred` varchar(5) default NULL, "
                        + "  `peptide_fraction_spread_lower_range_kDa` varchar(10) default NULL, "
                        + "  `peptide_fraction_spread_upper_range_kDa` varchar(10) default NULL, "
                        + "  `spectrum_fraction_spread_lower_range_kDa` varchar(10) default NULL, "
                        + "  `spectrum_fraction_spread_upper_range_kDa` varchar(10) default NULL, "
                        + "  `non_enzymatic_peptides` varchar(5) NOT NULL, "
                        + "  `gene_name` varchar(50) NOT NULL default 'Not Available', "
                        + "  `chromosome_number` varchar(20) NOT NULL default '', "
                        + "  `prot_key` varchar(500) NOT NULL, "
                        + "  `valid` varchar(7) NOT NULL default 'false',  "
                        + "  `description` varchar(500) NOT NULL, "
                        + "  `prot_group_id` int(255) NOT NULL auto_increment, "
                        + "  PRIMARY KEY  (`prot_group_id`), "
                        + "  KEY `exp_id` (`exp_id`), "
                        + "  KEY `prot_key` (`prot_key`(333)) "
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ; ";
                st.executeUpdate(statment);

                statment
                        = "CREATE TABLE IF NOT EXISTS `fractions_table` ( "
                        + "  `fraction_id` int(11) NOT NULL, "
                        + "  `prot_accession` varchar(500) NOT NULL, "
                        + "  `number_peptides` int(11) NOT NULL default '0', "
                        + "  `peptide_fraction_spread_lower_range_kDa` varchar(10) default NULL, "
                        + "  `peptide_fraction_spread_upper_range_kDa` varchar(10) default NULL, "
                        + "  `spectrum_fraction_spread_lower_range_kDa` varchar(10) default NULL, "
                        + "  `spectrum_fraction_spread_upper_range_kDa` varchar(10) default NULL, "
                        + "  `number_spectra` int(11) NOT NULL default '0', "
                        + "  `average_ precursor_intensity` double default NULL, "
                        + "  `exp_id` int(255) NOT NULL default '0', "
                        + "  KEY `prot_accession` (`prot_accession`(333)), "
                        + "  KEY `fraction_id` (`fraction_id`)  "
                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf8; ";
                st.executeUpdate(statment);

                statment
                        = "CREATE TABLE IF NOT EXISTS `proteins_peptides_table` ( "
                        + "  `protein` varchar(70) default NULL, "
                        + "  `other_protein(s)` text, "
                        + "  `peptide_protein(s)` text, "
                        + "  `other_protein_description(s)` text, "
                        + "  `peptide_proteins_description(s)` text, "
                        + "  `aa_before` varchar(2000) default NULL, "
                        + "  `sequence` varchar(300) default NULL, "
                        + "  `aa_after` varchar(2000) default NULL, "
                        + "  `peptide_start` text, "
                        + "  `peptide_end` text, "
                        + "  `variable_modification` varchar(500) default NULL, "
                        + "  `location_confidence` varchar(500) default NULL, "
                        + "  `precursor_charge(s)` varchar(70) default NULL, "
                        + "  `number_of_validated_spectra` int(20) default NULL, "
                        + "  `score` double NOT NULL default '0', "
                        + "  `confidence` double NOT NULL default '0', "
                        + "  `peptide_id` int(50) NOT NULL default '0', "
                        + "  `fixed_modification` varchar(100) default NULL, "
                        + "  `protein_inference` varchar(500) default NULL, "
                        + "  `sequence_tagged` varchar(500) default NULL, "
                        + "  `enzymatic` varchar(5) default NULL, "
                        + "  `validated` double default NULL, "
                        + "  `starred` varchar(5) default NULL, "
                        + "  `glycopattern_position(s)` varchar(100) default NULL, "
                        + "  `deamidation_and_glycopattern` varchar(5) default NULL, "
                        + "  `exp_id` int(250) NOT NULL default '0',  "
                        + "  `likelyNotGlycosite` varchar(5) NOT NULL default 'FALSE', "
                        + "  KEY `peptide_id` (`peptide_id`) "
                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf8; ";
                st.executeUpdate(statment);

                statment = "CREATE TABLE IF NOT EXISTS `quantitative_peptides_table` ( "
                        + "  `index` int(255) NOT NULL auto_increment, "
                        + "  `prot_index` int(255) NOT NULL default '-1', "
                        + "  `peptide_sequance` varchar(600) NOT NULL default 'Not Available', "
                        + "  `peptide_modification` varchar(600) NOT NULL default 'Not Available', "
                        + "  `modification_comment` varchar(600) NOT NULL default 'Not Available', "
                        + "  `string_fc_value` varchar(200) NOT NULL default 'Not Provided', "
                        + "  `string_p_value` varchar(200) NOT NULL default 'Not Available', "
                        + "  `p_value` double NOT NULL default '-1000000000', "
                        + "  `roc_auc` double NOT NULL default '-1000000000', "
                        + "  `fc_value` double NOT NULL default '-1000000000', "
                        + "  `DsKey` int(255) NOT NULL default '-1', "
                        + "  `p_value_comments` varchar(500) NOT NULL default 'Not Available', "
                        + "  `proteinAccession` varchar(50) NOT NULL default 'Not Available', "
                        + "  KEY `index` (`index`) "
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ; ";
                st.executeUpdate(statment);

                statment
                        = "CREATE TABLE IF NOT EXISTS `quantitative_proteins_table` ( "
                        + "  `index` int(255) NOT NULL auto_increment, "
                        + "  `ds_ID` int(255) NOT NULL default '-1', "
                        + "  `uniprot_accession` varchar(150) NOT NULL default 'Not Available', "
                        + "  `uniprot_protein_name` varchar(700) NOT NULL default 'Not Available', "
                        + "  `publication_acc_number` varchar(150) NOT NULL default 'Not Available', "
                        + "  `publication_protein_name` varchar(700) NOT NULL default 'Not Available', "
                        + "  `quantified_peptides_number` int(255) NOT NULL default '-1', "
                        + "  `identified_peptides_number` int(255) NOT NULL default '-1', "
                        + "  `fold_change` varchar(20) NOT NULL default 'Not Available', "
                        + "  `sequance` text NOT NULL, "
                        + "  `fc_value` double NOT NULL default '-1000000000', "
                        + "  `roc_auc` double NOT NULL default '-1000000000', "
                        + "  `string_p_value` varchar(100) NOT NULL default 'Not Available', "
                        + "  `p_value` double NOT NULL default '-1000000000', "
                        + "  `p_value_comments` varchar(500) NOT NULL default 'Not Available', "
                        + "  KEY `index` (`index`) "
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ; ";
                st.executeUpdate(statment);

                statment = "CREATE TABLE IF NOT EXISTS `quant_dataset_table` ( "
                        + "  `additional_comments` varchar(700) NOT NULL default 'Not Available', "
                        + "  `pumed_id` varchar(30) NOT NULL, "
                        + "  `files_num` int(255) NOT NULL default '-1', "
                        + "  `identified _proteins_num` int(255) NOT NULL default '-1', "
                        + "  `quantified_protein_num` int(255) NOT NULL default '-1', "
                        + "  `disease_group` varchar(300) NOT NULL default 'Not Available', "
                        + "  `raw_data_url` varchar(500) NOT NULL default 'Raw Data Not Available', "
                        + "  `year` int(4) NOT NULL default '0', "
                        + "  `index` int(255) NOT NULL auto_increment, "
                        + "  `type_of_study` varchar(200) NOT NULL default 'Not Available', "
                        + "  `sample_type` varchar(200) NOT NULL default 'Not Available', "
                        + "  `sample_matching` varchar(300) NOT NULL default 'Not Available', "
                        + "  `technology` varchar(300) NOT NULL default 'Not Available', "
                        + "  `analytical_approach` varchar(300) NOT NULL default 'Not Available', "
                        + "  `enzyme` varchar(300) NOT NULL default 'Not Available', "
                        + "  `shotgun_targeted` varchar(200) NOT NULL default 'Not Available', "
                        + "  `quantification_basis` varchar(200) NOT NULL default 'Not Available', "
                        + "  `quant_basis_comment` varchar(500) NOT NULL default 'Not Available', "
                        + "  `patients_group_i_number` int(255) NOT NULL default '-1000000000', "
                        + "  `patients_group_ii_number` int(255) NOT NULL default '-1000000000', "
                        + "  `normalization_strategy` varchar(600) NOT NULL default 'Not Available', "
                        + "  `author` varchar(300) NOT NULL default 'John Smith', "
                        + "  `patient_group_i` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_gr_i_comment` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_sub_group_i` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_group_ii` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_sub_group_ii` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_gr_ii_comment` varchar(700) NOT NULL default 'Not Available', "
                        + "  PRIMARY KEY  (`index`) "
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ; ";
                st.executeUpdate(statment);

                statment = "CREATE TABLE IF NOT EXISTS `quant_prot_table` ( "
                        + "  `pumed_id` varchar(150) NOT NULL, "
                        + "  `uniprot_accession` varchar(150) NOT NULL, "
                        + "  `uniprot_protein_name` varchar(700) NOT NULL, "
                        + "  `publication_acc_number` varchar(150) default 'Not Available', "
                        + "  `publication_protein_name` varchar(700) default 'Not Available', "
                        + "  `raw_data_available` varchar(700) default 'Not Available', "
                        + "  `type_of_study` varchar(150) default 'Not Available', "
                        + "  `sample_type` varchar(150) default 'Not Available', "
                        + "  `patient_group_i` varchar(700) default 'Not Available', "
                        + "  `patient_sub_group_i` varchar(700) default 'Not Available', "
                        + "  `patient_gr_i_comment` varchar(700) default 'Not Available', "
                        + "  `patient_group_ii` varchar(700) default 'Not Available',  "
                        + "  `patient_sub_group_ii` varchar(700) default 'Not Available', "
                        + "  `patient_gr_ii_comment` varchar(700) default 'Not Available',   "
                        + "  `sample_matching` varchar(500) default 'Not Available', "
                        + "  `normalization_strategy` varchar(500) default 'Not Available', "
                        + "  `technology` varchar(500) default 'Not Available', "
                        + "  `analytical_approach` varchar(500) default 'Not Available', "
                        + "  `enzyme` varchar(500) default 'Not Available', "
                        + "  `shotgun_targeted` varchar(100) default 'Not Available', "
                        + "  `quantification_basis` varchar(500) default 'Not Available', "
                        + "  `quant_basis_comment` varchar(700) default 'Not Available', "
                        + "  `additional_comments` varchar(700) default 'Not Available', "
                        + "  `q_peptide_key` varchar(700) default 'Not Available', "
                        + "  `peptide_sequance` varchar(700) default 'Not Available', "
                        + "  `peptide_modification` varchar(700) default 'Not Available', "
                        + "  `modification_comment` varchar(700) default 'Not Available', "
                        + "  `string_fc_value` varchar(200) default 'Not Available',  "
                        + "  `string_p_value` varchar(200) default 'Not Available', "
                        + "  `quantified_proteins_number` int(255) default NULL, "
                        + "  `peptideId_number` int(255) default NULL, "
                        + "  `quantified_peptides_number` int(255) default NULL, "
                        + "  `patients_group_i_number` int(255) default NULL, "
                        + "  `patients_group_ii_number` int(255) default NULL, "
                        + "  `p_value` double default NULL, "
                        + "  `roc_auc` double default NULL, "
                        + "  `fc_value` double default NULL, "
                        + "  `peptide_prot` varchar(5) NOT NULL default 'False', "
                        + "  `index` int(255) NOT NULL auto_increment, "
                        + "  PRIMARY KEY  (`index`) "
                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ; ";
                st.executeUpdate(statment);

                statment = "CREATE TABLE IF NOT EXISTS `standard_plot_proteins` ( "
                        + "  `exp_id` int(11) NOT NULL, "
                        + "  `mw_(kDa)` double NOT NULL, "
                        + "  `name` varchar(30) NOT NULL, "
                        + "  `lower` int(11) NOT NULL, "
                        + "  `upper` int(11) NOT NULL, "
                        + "  `color` varchar(30) NOT NULL "
                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf8; ";
                st.executeUpdate(statment);

                statment = "CREATE TABLE IF NOT EXISTS `studies_table` ( "
                        + "  `pumed_id` varchar(30) NOT NULL, "
                        + "  `files_num` int(255) NOT NULL default '0', "
                        + "  `identified _proteins_num` int(255) NOT NULL default '0', "
                        + "  `quantified_protein_num` int(255) NOT NULL default '0', "
                        + "  `disease_group` varchar(300) NOT NULL default 'Not Available', "
                        + "  `raw_data_url` varchar(500) NOT NULL default 'Not Available', "
                        + "  `year` int(4) NOT NULL default '0', "
                        + "  `index` int(255) NOT NULL auto_increment, "
                        + "  `type_of_study` varchar(200) NOT NULL default 'Not Available', "
                        + "  `sample_type` varchar(200) NOT NULL default 'Not Available', "
                        + "  `sample_matching` varchar(300) NOT NULL default 'Not Available', "
                        + "  `technology` varchar(300) NOT NULL default 'Not Available', "
                        + "  `analytical_approach` varchar(300) NOT NULL default 'Not Available', "
                        + "  `enzyme` varchar(300) NOT NULL default 'Not Available', "
                        + "  `shotgun_targeted` varchar(200) NOT NULL default 'Not Available', "
                        + "  `quantification_basis` varchar(200) NOT NULL default 'Not Available', "
                        + "  `quant_basis_comment` varchar(500) NOT NULL default 'Not Available', "
                        + "  `quantified_proteins_number` int(255) NOT NULL default '-1000000000', "
                        + "  `patients_group_i_number` int(255) NOT NULL default '-1000000000', "
                        + "  `patients_group_ii_number` int(255) NOT NULL default '-1000000000', "
                        + "  `normalization_strategy` varchar(600) NOT NULL default 'Not Available', "
                        + "  `author` varchar(300) NOT NULL default 'John Smith', "
                        + "  `patient_group_i` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_gr_i_comment` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_sub_group_i` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_group_ii` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_sub_group_ii` varchar(700) NOT NULL default 'Not Available', "
                        + "  `patient_gr_ii_comment` varchar(700) NOT NULL default 'Not Available', "
                        + "  PRIMARY KEY  (`index`) "
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ; ";
                st.executeUpdate(statment);

                statment = "CREATE TABLE IF NOT EXISTS `users_table` ( "
                        + "  `id` int(20) NOT NULL auto_increment, "
                        + "  `password` varchar(100) NOT NULL, "
                        + "  `admin` varchar(5) NOT NULL default 'FALSE', "
                        + "  `user_name` varchar(20) NOT NULL, "
                        + "  `email` varchar(100) NOT NULL, "
                        + "  PRIMARY KEY  (`email`), "
                        + "  KEY `id` (`id`)"
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";

                st.executeUpdate(statment);

//                //CREATE TABLE  `users_table`
//                String users_table = "CREATE TABLE IF NOT EXISTS `users_table` (  `id` int(20) NOT NULL auto_increment,  `password` varchar(100) NOT NULL,  `admin` varchar(5) NOT NULL default 'FALSE',  `user_name` varchar(20) NOT NULL,  `email` varchar(100) NOT NULL,  PRIMARY KEY  (`email`),  KEY `id` (`id`)) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
//                st.executeUpdate(users_table);
//
//                //CREATE TABLE `datasets_table`
//                String datasets_table = "CREATE TABLE IF NOT EXISTS `experiments_table` (\n" + "  `exp_id` int(11) NOT NULL auto_increment,\n" + "  `name` varchar(100) NOT NULL,\n" + "  `fractions_number` int(11) NOT NULL default '0',\n" + "  `ready` int(11) NOT NULL default '0',\n" + "  `uploaded_by` varchar(100) NOT NULL,\n" + "  `peptide_file` int(2) NOT NULL default '0',\n"
//                        + "  `species` varchar(100) NOT NULL,\n" + "  `sample_type` varchar(100) NOT NULL,\n" + "  `sample_processing` varchar(100) NOT NULL,\n" + "  `instrument_type` varchar(100) NOT NULL,\n" + "  `frag_mode` varchar(100) NOT NULL,\n" + "  `proteins_number` int(11) NOT NULL default '0',\n" + "  `peptides_number` int(11) NOT NULL default '0',\n" + "  `email` varchar(100) NOT NULL,\n" + "  `pblication_link` varchar(300) NOT NULL default 'NOT AVAILABLE',\n"
//                        + "  `description` varchar(1000) NOT NULL default 'NO DESCRIPTION AVAILABLE',\n" + "  `exp_type` int(10) NOT NULL default '0',\n" + "  PRIMARY KEY  (`exp_id`)\n" + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=20 ;";
//                st.executeUpdate(datasets_table);
//
//                //CREATE TABLE dataset_protein_table
//                String dataset_protein_table = "CREATE TABLE IF NOT EXISTS `experiment_protein_table` (\n"
//                        + "  `exp_id` int(11) NOT NULL,\n"
//                        + "  `prot_accession` varchar(30) NOT NULL,\n"
//                        + "  `other_protein(s)` varchar(1000) default NULL,\n"
//                        + "  `protein_inference_class` varchar(100) default NULL,\n"
//                        + "  `sequence_coverage(%)` double default NULL,\n"
//                        + "  `observable_coverage(%)` double default NULL,\n"
//                        + "  `confident_ptm_sites` varchar(500) default NULL,\n"
//                        + "  `number_confident` varchar(500) default NULL,\n"
//                        + "  `other_ptm_sites` varchar(500) default NULL,\n"
//                        + "  `number_other` varchar(500) default NULL,\n"
//                        + "  `number_validated_peptides` int(11) default NULL,\n"
//                        + "  `number_validated_spectra` int(11) default NULL,\n"
//                        + "  `em_pai` double default NULL,\n"
//                        + "  `nsaf` double default NULL,\n"
//                        + "  `mw_(kDa)` double default NULL,\n"
//                        + "  `score` double default NULL,\n"
//                        + "  `confidence` double default NULL,\n"
//                        + "  `starred` varchar(5) default NULL,\n"
//                        + "  `peptide_fraction_spread_lower_range_kDa` varchar(10) default NULL,\n"
//                        + "  `peptide_fraction_spread_upper_range_kDa` varchar(10) default NULL,\n"
//                        + "  `spectrum_fraction_spread_lower_range_kDa` varchar(10) default NULL,\n"
//                        + "  `spectrum_fraction_spread_upper_range_kDa` varchar(10) default NULL,\n"
//                        + "  `non_enzymatic_peptides` varchar(5) NOT NULL,\n"
//                        + "  `gene_name` varchar(50) NOT NULL default 'Not Available',\n"
//                        + "  `chromosome_number` varchar(20) NOT NULL default '',\n"
//                        + "  `prot_key` varchar(500) NOT NULL,\n"
//                        + "  `valid` varchar(7) NOT NULL default 'false',\n"
//                        + "  `description` varchar(500) NOT NULL,\n"
//                        + "  `prot_group_id` int(255) NOT NULL auto_increment,\n"
//                        + "  PRIMARY KEY  (`prot_group_id`),\n"
//                        + "  KEY `exp_id` (`exp_id`),\n"
//                        + "  KEY `prot_key` (`prot_key`(333))\n"
//                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=0 ;";
//                st.executeUpdate(dataset_protein_table);
//
//                //  CREATE TABLE  `experiment_peptides_table`
//                String dataset_peptide_table = "CREATE TABLE IF NOT EXISTS `experiment_peptides_table` (  `exp_id` INT NOT NULL DEFAULT  '0',  `pep_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,FOREIGN KEY (`exp_id`) REFERENCES experiments_table (`exp_id`) ON DELETE CASCADE  ) ENGINE = MYISAM ;";
//                st.executeUpdate(dataset_peptide_table);
//
//                // CREATE TABLE  `proteins_peptides_table`
//                String proteins_peptides_table = "CREATE TABLE IF NOT EXISTS `proteins_peptides_table` (\n"
//                        + "  `protein` varchar(70) default NULL,\n"
//                        + "  `other_protein(s)` text,\n"
//                        + "  `peptide_protein(s)` text,\n"
//                        + "  `other_protein_description(s)` text,\n"
//                        + "  `peptide_proteins_description(s)` text,\n"
//                        + "  `aa_before` varchar(2000) default NULL,\n"
//                        + "  `sequence` varchar(300) default NULL,\n"
//                        + "  `aa_after` varchar(2000) default NULL,\n"
//                        + "  `peptide_start` text,\n"
//                        + "  `peptide_end` text,\n"
//                        + "  `variable_modification` varchar(500) default NULL,\n"
//                        + "  `location_confidence` varchar(500) default NULL,\n"
//                        + "  `precursor_charge(s)` varchar(70) default NULL,\n"
//                        + "  `number_of_validated_spectra` int(20) default NULL,\n"
//                        + "  `score` double NOT NULL default '0',\n"
//                        + "  `confidence` double NOT NULL default '0',\n"
//                        + "  `peptide_id` int(50) NOT NULL default '0',\n"
//                        + "  `fixed_modification` varchar(100) default NULL,\n"
//                        + "  `protein_inference` varchar(500) default NULL,\n"
//                        + "  `sequence_tagged` varchar(500) default NULL,\n"
//                        + "  `enzymatic` varchar(5) default NULL,\n"
//                        + "  `validated` double default NULL,\n"
//                        + "  `starred` varchar(5) default NULL,\n"
//                        + "  `glycopattern_position(s)` varchar(100) default NULL,\n"
//                        + "  `deamidation_and_glycopattern` varchar(5) default NULL,\n"
//                        + "  `exp_id` int(250) NOT NULL default '0',\n"
//                        + "  `likelyNotGlycosite` varchar(5) NOT NULL default 'FALSE',\n"
//                        + "  KEY `peptide_id` (`peptide_id`)\n"
//                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf1;";
//                st.executeUpdate(proteins_peptides_table);
//                System.out.println("at context start creating ds ");
//
//                //CREATE TABLE fractions_table
//                String fractions_table = "CREATE TABLE IF NOT EXISTS `fractions_table` (\n"
//                        + "  `fraction_id` int(11) NOT NULL,\n"
//                        + "  `prot_accession` varchar(500) NOT NULL,\n"
//                        + "  `number_peptides` int(11) NOT NULL default '0',\n"
//                        + "  `peptide_fraction_spread_lower_range_kDa` varchar(10) default NULL,\n"
//                        + "  `peptide_fraction_spread_upper_range_kDa` varchar(10) default NULL,\n"
//                        + "  `spectrum_fraction_spread_lower_range_kDa` varchar(10) default NULL,\n"
//                        + "  `spectrum_fraction_spread_upper_range_kDa` varchar(10) default NULL,\n"
//                        + "  `number_spectra` int(11) NOT NULL default '0',\n"
//                        + "  `average_ precursor_intensity` double default NULL,\n"
//                        + "  `exp_id` int(255) NOT NULL default '0',\n"
//                        + "  KEY `prot_accession` (`prot_accession`(333)),\n"
//                        + "  KEY `fraction_id` (`fraction_id`)\n"
//                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf1;";
//                st.executeUpdate(fractions_table);
//
//                //CREATE TABLE dataset_peptides_proteins_table
//                String dataset_peptides_proteins_table = "CREATE TABLE IF NOT EXISTS `experiment_peptides_proteins_table` (  `exp_id` varchar(50) NOT NULL,  `peptide_id` int(50) NOT NULL,  `protein` varchar(70) NOT NULL,  UNIQUE KEY `exp_id` (`exp_id`,`peptide_id`,`protein`),  KEY `peptide_id` (`peptide_id`),  KEY `protein` (`protein`)) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
//                st.executeUpdate(dataset_peptides_proteins_table);
//
//                //CREATE TABLEstandard_plot_proteins
//                String standard_plot_proteins = " CREATE TABLE IF NOT EXISTS `standard_plot_proteins` (`exp_id` int(11) NOT NULL,	  `mw_(kDa)` double NOT NULL,	  `name` varchar(30) NOT NULL,	  `lower` int(11) NOT NULL,  `upper` int(11) NOT NULL,  `color` varchar(30) NOT NULL  ) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
//                st.executeUpdate(standard_plot_proteins);
//
//                //  CREATE TABLE  `quant_prot_table`
//                String quant_prot_table = "CREATE TABLE IF NOT EXISTS `quant_prot_table` (\n"
//                        + "  `pumed_id` varchar(150) NOT NULL,\n"
//                        + "  `uniprot_accession` varchar(150) NOT NULL,\n"
//                        + "  `uniprot_protein_name` varchar(700) NOT NULL,\n"
//                        + "  `publication_acc_number` varchar(150) default 'Not Available',\n"
//                        + "  `publication_protein_name` varchar(700) default 'Not Available',\n"
//                        + "  `raw_data_available` varchar(700) default 'Not Available',\n"
//                        + "  `type_of_study` varchar(150) default 'Not Available',\n"
//                        + "  `sample_type` varchar(150) default 'Not Available',\n"
//                        + "  `patient_group_i` varchar(700) default 'Not Available',\n"
//                        + "  `patient_sub_group_i` varchar(700) default 'Not Available',\n"
//                        + "  `patient_gr_i_comment` varchar(700) default 'Not Available',\n"
//                        + "  `patient_group_ii` varchar(700) default 'Not Available',\n"
//                        + "  `patient_sub_group_ii` varchar(700) default 'Not Available',\n"
//                        + "  `patient_gr_ii_comment` varchar(700) default 'Not Available',\n"
//                        + "  `sample_matching` varchar(500) default 'Not Available',\n"
//                        + "  `normalization_strategy` varchar(500) default 'Not Available',\n"
//                        + "  `technology` varchar(500) default 'Not Available',\n"
//                        + "  `analytical_approach` varchar(500) default 'Not Available',\n"
//                        + "  `enzyme` varchar(500) default 'Not Available',\n"
//                        + "  `shotgun_targeted` varchar(100) default 'Not Available',\n"
//                        + "  `quantification_basis` varchar(500) default 'Not Available',\n"
//                        + "  `quant_basis_comment` varchar(700) default 'Not Available',\n"
//                        + "  `additional_comments` varchar(700) default 'Not Available',\n"
//                        + "  `q_peptide_key` varchar(700) default 'Not Available',\n"
//                        + "  `peptide_sequance` varchar(700) default 'Not Available',\n"
//                        + "  `peptide_modification` varchar(700) default 'Not Available',\n"
//                        + "  `modification_comment` varchar(700) default 'Not Available',\n"
//                        + "  `string_fc_value` varchar(200) default 'Not Available',\n"
//                        + "  `string_p_value` varchar(200) default 'Not Available',\n"
//                        //                        + "  `quantified_proteins_number` int(255) default NULL,\n"
//                        + "  `peptideId_number` int(255) default NULL,\n"
//                        + "  `quantified_peptides_number` int(255) default NULL,\n"
//                        + "  `patients_group_i_number` int(255) default NULL,\n"
//                        + "  `patients_group_ii_number` int(255) default NULL,\n"
//                        + "  `p_value` double default NULL,\n"
//                        + "  `roc_auc` double default NULL,\n"
//                        + "  `fc_value` double default NULL,\n"
//                        + "  `peptide_prot` varchar(5) NOT NULL default 'False',\n"
//                        + "  `index` int(255) NOT NULL auto_increment,\n"
//                        + "  PRIMARY KEY  (`index`)) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
//                st.executeUpdate(quant_prot_table);
//
//                //CREATE TABLE  `studies_table`
////                String studies_table = "CREATE TABLE IF NOT EXISTS `studies_table` (\n"
////                        + "  `pumed_id` varchar(30) NOT NULL,\n"
////                        + "  `files_num` int(255) NOT NULL default '0',\n"
////                        + "  `identified _proteins_num` int(255) NOT NULL default '0',\n"
////                        + "  `quantified_protein_num` int(255) NOT NULL default '0',\n"
////                        + "  `disease_group` varchar(300) NOT NULL default 'Not Available',\n"
////                        + "  `raw_data_url` varchar(500) NOT NULL default 'Not Available',\n"
////                        + "  `year` int(4) NOT NULL default '0',\n"
////                        + "  `index` int(255) NOT NULL auto_increment,\n"
////                        + "  `type_of_study` varchar(200) NOT NULL default 'Not Available',\n"
////                        + "  `sample_type` varchar(200) NOT NULL default 'Not Available',\n"
////                        + "  `sample_matching` varchar(300) NOT NULL default 'Not Available',\n"
////                        + "  `technology` varchar(300) NOT NULL default 'Not Available',\n"
////                        + "  `analytical_approach` varchar(300) NOT NULL default 'Not Available',\n"
////                        + "  `enzyme` varchar(300) NOT NULL default 'Not Available',\n"
////                        + "  `shotgun_targeted` varchar(200) NOT NULL default 'Not Available',\n"
////                        + "  `quantification_basis` varchar(200) NOT NULL default 'Not Available',\n"
////                        + "  `quant_basis_comment` varchar(500) NOT NULL default 'Not Available',\n"
////                        + "  `quantified_proteins_number` int(255) NOT NULL default '-1000000000',\n"
////                        + "  `patients_group_i_number` int(255) NOT NULL default '-1000000000',\n"
////                        + "  `patients_group_ii_number` int(255) NOT NULL default '-1000000000',\n"
////                        + "  `normalization_strategy` varchar(600) NOT NULL default 'Not Available',\n"
////                        + "  `author` varchar(300) NOT NULL default 'Adam Smith',\n"
////                        + "  PRIMARY KEY  (`index`)\n"
////                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
////
////                st.executeUpdate(studies_table);
////CREATE TABLE  Quant_Dataset_table
//                String quant_ds_table = "CREATE TABLE IF NOT EXISTS `quant_dataset_table` (\n"
//                        + "  `pumed_id` varchar(30) NOT NULL,\n"
//                        + "  `files_num` int(255) NOT NULL default '0',\n"
//                        + "  `identified _proteins_num` int(255) NOT NULL default '0',\n"
//                        + "  `quantified_protein_num` int(255) NOT NULL default '0',\n"
//                        + "  `disease_group` varchar(300) NOT NULL default 'Not Available',\n"
//                        + "  `raw_data_url` varchar(500) NOT NULL default 'Not Available',\n"
//                        + "  `year` int(4) NOT NULL default '0',\n"
//                        + "  `index` int(255) NOT NULL auto_increment,\n"
//                        + "  `type_of_study` varchar(200) NOT NULL default 'Not Available',\n"
//                        + "  `sample_type` varchar(200) NOT NULL default 'Not Available',\n"
//                        + "  `sample_matching` varchar(300) NOT NULL default 'Not Available',\n"
//                        + "  `technology` varchar(300) NOT NULL default 'Not Available',\n"
//                        + "  `analytical_approach` varchar(300) NOT NULL default 'Not Available',\n"
//                        + "  `enzyme` varchar(300) NOT NULL default 'Not Available',\n"
//                        + "  `shotgun_targeted` varchar(200) NOT NULL default 'Not Available',\n"
//                        + "  `quantification_basis` varchar(200) NOT NULL default 'Not Available',\n"
//                        + "  `quant_basis_comment` varchar(500) NOT NULL default 'Not Available',\n"
//                        //                        + "  `quantified_proteins_number` int(255) NOT NULL default '-1000000000',\n"
//                        + "  `patients_group_i_number` int(255) NOT NULL default '-1000000000',\n"
//                        + "  `patients_group_ii_number` int(255) NOT NULL default '-1000000000',\n"
//                        + "  `normalization_strategy` varchar(600) NOT NULL default 'Not Available',\n"
//                        + "  `author` varchar(300) NOT NULL default 'John Smith',\n"
//                        + "  `patient_group_i` varchar(700) NOT NULL default 'Not Available',\n"
//                        + "  `patient_gr_i_comment` varchar(700) NOT NULL default 'Not Available',\n"
//                        + "  `patient_sub_group_i` varchar(700) NOT NULL default 'Not Available',\n"
//                        + "  `patient_group_ii` varchar(700) NOT NULL default 'Not Available',\n"
//                        + "  `patient_sub_group_ii` varchar(700) NOT NULL default 'Not Available',\n"
//                        + "  `patient_gr_ii_comment` varchar(700) NOT NULL default 'Not Available',\n"
//                        + "  PRIMARY KEY  (`index`)\n"
//                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
//                System.out.println("at db start quant prot table");
//                st.executeUpdate(quant_ds_table);
//
//                String quantitative_proteins_table = "CREATE TABLE IF NOT EXISTS `quantitative_proteins_table` (\n"
//                        + "  `index` int(255) NOT NULL auto_increment,\n"
//                        + "  `ds_ID` int(255) NOT NULL default '-1',\n"
//                        + "  `uniprot_accession` varchar(150) NOT NULL default 'Not Available',\n"
//                        + "  `uniprot_protein_name` varchar(700) NOT NULL default 'Not Available',\n"
//                        + "  `publication_acc_number` varchar(150) NOT NULL default 'Not Available',\n"
//                        + "  `publication_protein_name` varchar(700) NOT NULL default 'Not Available',\n"
//                        + "  `quantified_peptides_number` int(255) NOT NULL default '-1',\n"
//                        + "  `identified_peptides_number` int(255) NOT NULL default '-1',\n"
//                        + "  KEY `index` (`index`)\n"
//                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
//                st.executeUpdate(quantitative_proteins_table);
//
//                String quantitative_peptides_table = "CREATE TABLE IF NOT EXISTS `quantitative_peptides_table` (\n"
//                        + "  `index` int(255) NOT NULL auto_increment,\n"
//                        + "  `prot_index` int(255) NOT NULL default '-1',\n"
//                        + "  `peptide_sequance` varchar(600) NOT NULL default 'Not Available',\n"
//                        + "  `peptide_modification` varchar(600) NOT NULL default 'Not Available',\n"
//                        + "  `modification_comment` varchar(600) NOT NULL default 'Not Available',\n"
//                        + "  `string_fc_value` varchar(200) NOT NULL default 'Not Available',\n"
//                        + "  `string_p_value` varchar(200) NOT NULL default 'Not Available',\n"
//                        + "  `p_value` double NOT NULL default '-1000000000',\n"
//                        + "  `roc_auc` double NOT NULL default '-1000000000',\n"
//                        + "  `fc_value` double NOT NULL default '-1000000000',\n"
//                        + "  KEY `index` (`index`)\n"
//                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
//                st.executeUpdate(quantitative_peptides_table);
//                st.executeUpdate(statment);
                conn.close();
                st.close();
                System.gc();
            } catch (SQLException s) {
                System.err.println(s.getLocalizedMessage());
                s.printStackTrace();
                conn.close();

                return false;
            }
            // 
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 904 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return false;
        } catch (IllegalAccessException e) {
            System.err.println("at error line 907 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return false;
        } catch (InstantiationException e) {
            System.err.println("at error line 910 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return false;
        } catch (SQLException e) {
            System.err.println("at error line 914 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    /**
     * get the available identification datasets
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
//        singleuseUpdateDb();
//        initPublications();
        return datasetList;

    }

    /**
     * get selected identification dataset
     *
     * @param datasetId
     * @return dataset
     */
    public synchronized IdentificationDatasetBean retriveIdentficationDataset(int datasetId) {
        IdentificationDatasetBean dataset = new IdentificationDatasetBean();
        dataset.setDatasetId(datasetId);
        dataset = this.getIdentificationDatasetDetails(dataset);
        System.gc();
        return dataset;
    }

    /**
     * get identification fractions Ids list for a dataset
     *
     * @param datasetId
     * @return list of fraction Id's list
     */
    private synchronized List<Integer> getIdentificationProteinFractionIdsList(int datasetId) {
        PreparedStatement selectDatasetFractionStat;
        String selectDatasetFraction = "SELECT `fraction_id` FROM `experiment_fractions_table` WHERE `exp_id`=?;";
        List<Integer> fractionList = new ArrayList<Integer>();
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectDatasetFractionStat = conn.prepareStatement(selectDatasetFraction);
            selectDatasetFractionStat.setInt(1, datasetId);
            ResultSet rs = selectDatasetFractionStat.executeQuery();
            while (rs.next()) {
                fractionList.add(rs.getInt("fraction_id"));
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
        return fractionList;
    }

    /**
     * get identification fractions Ids list for a dataset
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
     * get identification dataset details
     *
     * @param dataset
     * @return list of fraction Id's list
     */
    private synchronized IdentificationDatasetBean getIdentificationDatasetDetails(IdentificationDatasetBean dataset) {
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
     * get identification dataset fractions list
     *
     * @param datasetId
     * @param accession
     * @param otherAccession
     * @return fractions list for the selected dataset
     */
    public synchronized Map<Integer, IdentificationProteinBean> getIdentificationProteinsGelFractionsList(int datasetId, String accession, String otherAccession) {
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
     * get identification peptides list for specific identification dataset
     *
     * @param identificationDatasetId
     * @param valid
     * @return dataset peptide List
     */
    @SuppressWarnings("SleepWhileInLoop")
    public synchronized Map<Integer, IdentificationPeptideBean> getAllIdentificationDatasetPeptidesList(int identificationDatasetId, boolean valid) {
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
     * get all peptides number for specific dataset
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
     * get identification proteins map for especial dataset
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
                temPb.setDescription(rs.getString("description"));
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
     * remove identification dataset from the database
     *
     * @param datasetId
     * @return boolean successful process
     */
    public synchronized boolean removeIdentificationDataset(int datasetId) {

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
     * search for proteins by accession keywords
     *
     * @param accession array of query words
     * @param datasetId
     * @param validatedOnly only validated proteins results
     * @return dataset Proteins Searching List
     */
    public synchronized Map<Integer, IdentificationProteinBean> searchIdentificationProteinByAccession(String accession, int datasetId, boolean validatedOnly) {
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
     * search for identification proteins by accession keywords
     *
     * @param searchSet set of query words
     * @param validatedOnly only validated proteins results
     * @return dataset Proteins Searching List
     */
    public synchronized Map<Integer, IdentificationProteinBean> searchIdentificationProteinAllDatasetsByAccession(Set<String> searchSet, boolean validatedOnly) {
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
     * get identification peptides list for giving ids
     *
     * @param accession peptides IDs
     * @param otherAcc peptides IDs
     * @param datasetId peptides IDs
     * @return peptides list
     */
    public synchronized Map<Integer, IdentificationPeptideBean> getIdentificationPeptidesList(String accession, String otherAcc, int datasetId) {

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
     * get identification proteins fractions average list
     *
     * @param accession
     * @param datasetId
     * @return dataset peptide List
     */
    public synchronized Map<Integer, IdentificationFractionBean> getIdentificationProteinFractionList(String accession, int datasetId) {
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
     * search for identification proteins by protein description keywords
     *
     * @param protSearchKeyword array of query words
     * @param datasetId dataset Id
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public synchronized Map<Integer, IdentificationProteinBean> searchIdentificationProteinByName(String protSearchKeyword, int datasetId, boolean validatedOnly) {
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
//            selectProStat.setInt(index++, datasetId);
            for (String str : searchSet) {
                selectProStat.setString(index++, "%" + str + "%");
            }
//            selectProStat.setString(1, "%" + protSearchKeyword + "%");
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
     * search for identification proteins by protein description keywords
     *
     * @param protSearchKeyword array of query words
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public synchronized Map<Integer, IdentificationProteinBean> searchIdentificationProteinAllDatasetsByName(String protSearchKeyword, boolean validatedOnly) {
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
     * fill identification peptides information from the result set
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
     * fill identification proteins information from the result set
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
                temPb.setDescription(rs.getString("description"));
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
     * search for identification proteins by peptide sequence keywords
     *
     * @param peptideSequenceKeyword array of query words
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public synchronized Map<Integer, IdentificationProteinBean> SearchIdentificationProteinAllDatasetsByPeptideSequence(String peptideSequenceKeyword, boolean validatedOnly) {
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
     * search for identification proteins by peptide sequence keywords
     *
     * @param peptideSequenceKeyword array of query words
     * @param datasetId dataset Id
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public synchronized Map<Integer, IdentificationProteinBean> SearchIdentificationProteinByPeptideSequence(String peptideSequenceKeyword, int datasetId, boolean validatedOnly) {
//        PreparedStatement selectProStat = null;
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
     * store new user details
     *
     * @param username user username
     * @param userPassword
     * @param admin user is admin or not
     * @param email user email
     *
     * @return test successful process
     */
    public synchronized boolean storeNewUser(String username, String userPassword, boolean admin, String email) {
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
     * check user username is available
     *
     * @param email user email
     *
     * @return test successful process
     */
    public synchronized boolean validateUsername(String email) {
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
     * authenticate username and password for user
     *
     * @param email user email
     *
     * @return password
     */
    public synchronized String authenticate(String email) {
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
     * get user details
     *
     * @param email user email
     *
     * @return user details of user
     */
    public synchronized User getUser(String email) {
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
     * get users list
     *
     * @return list of users
     */
    public synchronized Map<Integer, String> getUsersList() {
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
     * remove username from the database
     *
     * @param username
     *
     * @return test boolean successful process
     */
    public synchronized boolean removeUser(String username) {
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
    public synchronized boolean updateUserPassword(String username, String newpassword) {

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
     * store standard plot bean in the database
     *
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
     * remove standard plot data in the database
     *
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
     * retrieve standard proteins data for fraction plot
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
     * update identification dataset information
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
     * get available quantification datasets initial information object that
     * contains the available datasets list and the active columns (to hide them
     * if they are empty)
     *
     * @return QuantDatasetInitialInformationObject
     */
    public QuantDatasetInitialInformationObject getQuantDatasetInitialInformationObject() {

        Set<QuantDatasetObject> quantDatasetList = new HashSet<QuantDatasetObject>();
        boolean[] activeHeaders = new boolean[27];

        try {
            PreparedStatement selectStudiesStat;
            String selectStudies = "SELECT * FROM  `combined_dataset_table` ";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectStudiesStat = conn.prepareStatement(selectStudies);
            ResultSet rs = selectStudiesStat.executeQuery();
            while (rs.next()) {
                QuantDatasetObject pb = new QuantDatasetObject();
                String author = rs.getString("author");
                if (!activeHeaders[0] && author != null && !author.equalsIgnoreCase("Not Available")) {
                    activeHeaders[0] = true;
                }
                pb.setAuthor(author);
                int year = rs.getInt("year");
                if (!activeHeaders[1] && year != 0) {
                    activeHeaders[1] = true;
                }
                pb.setYear(year);
                int identified_proteins_num = rs.getInt("identified _proteins_num");
                if (!activeHeaders[2] && identified_proteins_num != -1 && identified_proteins_num != 0) {
                    activeHeaders[2] = true;
                }
                pb.setIdentifiedProteinsNumber(identified_proteins_num);

                int quantified_protein_num = rs.getInt("quantified_protein_num");
                if (!activeHeaders[3] && quantified_protein_num != -1) {
                    activeHeaders[3] = true;
                }
                pb.setQuantifiedProteinsNumber(quantified_protein_num);

                String disease_group = rs.getString("disease_group");
                if (!activeHeaders[4] && disease_group != null && !disease_group.equalsIgnoreCase("Not Available")) {
                    activeHeaders[4] = true;
                }
                pb.setDiseaseGroups(disease_group);

                String raw_data_url = rs.getString("raw_data_url");
                if (!activeHeaders[5] && raw_data_url != null && !raw_data_url.equalsIgnoreCase("Not Available")) {
                    activeHeaders[5] = true;
                }
                pb.setRawDataUrl(raw_data_url);

                int files_num = rs.getInt("files_num");
                if (!activeHeaders[6] && files_num != -1) {
                    activeHeaders[6] = true;
                }
                pb.setFilesNumber(files_num);

                String type_of_study = rs.getString("type_of_study");
                if (!activeHeaders[7] && type_of_study != null && !type_of_study.equalsIgnoreCase("Not Available")) {
                    activeHeaders[7] = true;
                }
                pb.setTypeOfStudy(type_of_study);

                String sample_type = rs.getString("sample_type");
                if (!activeHeaders[8] && sample_type != null && !sample_type.equalsIgnoreCase("Not Available")) {
                    activeHeaders[8] = true;
                }
                pb.setSampleType(sample_type);

                String sample_matching = rs.getString("sample_matching");
                if (!activeHeaders[9] && sample_matching != null && !sample_matching.equalsIgnoreCase("Not Available")) {
                    activeHeaders[9] = true;
                }
                pb.setSampleMatching(sample_matching);

                String shotgun_targeted = rs.getString("shotgun_targeted");
                if (!activeHeaders[10] && shotgun_targeted != null && !shotgun_targeted.equalsIgnoreCase("Not Available")) {
                    activeHeaders[10] = true;
                }
                pb.setShotgunTargeted(shotgun_targeted);

                String technology = rs.getString("technology");
                if (!activeHeaders[11] && technology != null && !technology.equalsIgnoreCase("Not Available")) {
                    activeHeaders[11] = true;
                }
                pb.setTechnology(technology);

                String analytical_approach = rs.getString("analytical_approach");
                if (!activeHeaders[12] && analytical_approach != null && !analytical_approach.equalsIgnoreCase("Not Available")) {
                    activeHeaders[12] = true;
                }
                pb.setAnalyticalApproach(analytical_approach);

                String enzyme = rs.getString("enzyme");
                if (!activeHeaders[13] && enzyme != null && !enzyme.equalsIgnoreCase("Not Available")) {
                    activeHeaders[13] = true;
                }
                pb.setEnzyme(enzyme);

                String quantification_basis = rs.getString("quantification_basis");
                if (!activeHeaders[14] && quantification_basis != null && !quantification_basis.equalsIgnoreCase("Not Available")) {
                    activeHeaders[14] = true;
                }

                pb.setQuantificationBasis(quantification_basis);

                String quant_basis_comment = rs.getString("quant_basis_comment");
                if (!activeHeaders[15] && quant_basis_comment != null && !quant_basis_comment.equalsIgnoreCase("Not Available")) {
                    activeHeaders[15] = true;
                }
                pb.setQuantBasisComment(quant_basis_comment);

                int id = rs.getInt("index");
                pb.setUniqId(id - 1);

                String normalization_strategy = rs.getString("normalization_strategy");
                if (!activeHeaders[16] && normalization_strategy != null && !normalization_strategy.equalsIgnoreCase("Not Available")) {
                    activeHeaders[16] = true;
                }
                pb.setNormalizationStrategy(normalization_strategy);

                String pumed_id = rs.getString("pumed_id");
                if (!activeHeaders[17] && pumed_id != null && !pumed_id.equalsIgnoreCase("Not Available")) {
                    activeHeaders[17] = true;
                }
                pb.setPumedID(pumed_id);

                String patient_group_i = rs.getString("patient_group_i");
                if (!activeHeaders[18] && patient_group_i != null && !patient_group_i.equalsIgnoreCase("Not Available")) {
                    activeHeaders[18] = true;
                }
                pb.setPatientsGroup1(patient_group_i);

                int patients_group_i_number = rs.getInt("patients_group_i_number");
                if (!activeHeaders[19] && patients_group_i_number != -1) {
                    activeHeaders[19] = true;
                }
                pb.setPatientsGroup1Number(patients_group_i_number);

                String patient_gr_i_comment = rs.getString("patient_gr_i_comment");
                if (!activeHeaders[20] && patient_gr_i_comment != null && !patient_gr_i_comment.equalsIgnoreCase("Not Available")) {
                    activeHeaders[20] = true;
                }
                pb.setPatientsGroup1Comm(patient_gr_i_comment);

                String patient_sub_group_i = rs.getString("patient_sub_group_i");
                if (!activeHeaders[21] && patient_sub_group_i != null && !patient_sub_group_i.equalsIgnoreCase("Not Available")) {
                    activeHeaders[21] = true;
                }
                pb.setPatientsSubGroup1(patient_sub_group_i);

                String patient_group_ii = rs.getString("patient_group_ii");
                if (!activeHeaders[22] && patient_group_ii != null && !patient_group_ii.equalsIgnoreCase("Not Available")) {
                    activeHeaders[22] = true;
                }
                pb.setPatientsGroup2(patient_group_ii);

                int patients_group_ii_number = rs.getInt("patients_group_ii_number");
                if (!activeHeaders[23] && patients_group_ii_number != -1) {
                    activeHeaders[23] = true;
                }
                pb.setPatientsGroup2Number(patients_group_ii_number);

                String patient_gr_ii_comment = rs.getString("patient_gr_ii_comment");
                if (!activeHeaders[24] && patient_gr_ii_comment != null && !patient_gr_ii_comment.equalsIgnoreCase("Not Available")) {
                    activeHeaders[24] = true;
                }
                pb.setPatientsGroup2Comm(patient_gr_ii_comment);

                String patient_sub_group_ii = rs.getString("patient_sub_group_ii");
                if (!activeHeaders[25] && patient_sub_group_ii != null && !patient_sub_group_ii.equalsIgnoreCase("Not Available")) {
                    activeHeaders[25] = true;
                }
                pb.setPatientsSubGroup2(patient_sub_group_ii);

                String additional_comments = rs.getString("additional_comments");
                if (!activeHeaders[26] && additional_comments != null && !additional_comments.equalsIgnoreCase("Not Available")) {
                    activeHeaders[26] = true;
                }
                pb.setAdditionalcomments(additional_comments);

                quantDatasetList.add(pb);

            }
            rs.close();
            QuantDatasetInitialInformationObject datasetObject = new QuantDatasetInitialInformationObject();
            QuantDatasetObject[] dss = new QuantDatasetObject[quantDatasetList.size()];
            Map<Integer, QuantDatasetObject> updatedQuantDatasetObjectMap = new LinkedHashMap<Integer, QuantDatasetObject>();
            for (QuantDatasetObject ds : quantDatasetList) {
                updatedQuantDatasetObjectMap.put(ds.getUniqId(), ds);
            }

            datasetObject.setQuantDatasetsList(updatedQuantDatasetObjectMap);
            datasetObject.setActiveHeaders(activeHeaders);
            return datasetObject;

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
     * get active quantification pie charts filters (to hide them if they are
     * empty)
     *
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public boolean[] getActivePieChartQuantFilters() {
        String[] columnArr = new String[]{"`identified _proteins_num`", "`quantified_protein_num`", "`disease_group`", "`raw_data_url`", "`year`", "`type_of_study`", "`sample_type`", "`sample_matching`", "`technology`", "`analytical_approach`", "`enzyme`", "`shotgun_targeted`", "`quantification_basis`", "`quant_basis_comment`", "`patients_group_i_number`", "`patients_group_ii_number`", "`normalization_strategy`"};
        boolean[] activeFilters = new boolean[columnArr.length];

        try {

            PreparedStatement selectPumed_idStat;
            String selectPumed_id = "SELECT  `pumed_id` FROM  `combined_dataset_table` GROUP BY  `pumed_id` ORDER BY  `pumed_id` ";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectPumed_idStat = conn.prepareStatement(selectPumed_id);
            ResultSet rs = selectPumed_idStat.executeQuery();
            int pumed_id_index = 0;
            while (rs.next()) {
                pumed_id_index++;
            }
            rs.close();
            /// check the colums one by one 
            for (int index = 0; index < columnArr.length; index++) {
                String selectPumed_id1 = "SELECT  `pumed_id` FROM  `combined_dataset_table` GROUP BY  `pumed_id`, " + columnArr[index] + " ORDER BY  `pumed_id` ";
                if (conn == null || conn.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                }
                selectPumed_idStat = conn.prepareStatement(selectPumed_id1);
                rs = selectPumed_idStat.executeQuery();
                int pumed_id_com_index = 0;
                while (rs.next()) {
                    pumed_id_com_index++;
                }
                rs.close();
                if (pumed_id_index != pumed_id_com_index) {
                    activeFilters[index] = true;
                }
            }
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
        activeFilters[0] = false;
        activeFilters[1] = false;
        activeFilters[2] = false;
        activeFilters[3] = true;
        activeFilters[4] = true;
        activeFilters[7] = false;
        activeFilters[activeFilters.length - 2] = false;
        activeFilters[activeFilters.length - 3] = false;
        activeFilters[activeFilters.length - 4] = false;

        return activeFilters;

    }

    /**
     * get available quantification datasets initial information object within
     * quant searching proteins results that contains the available datasets
     * list and the active columns (to hide them if they are empty)
     *
     * @param searchQuantificationProtList
     * @return QuantDatasetInitialInformationObject
     */
    public QuantDatasetInitialInformationObject getQuantDatasetListObject(List<QuantProtein> searchQuantificationProtList) {

        Set<QuantDatasetObject> quantDatasetList = new HashSet<QuantDatasetObject>();
        boolean[] activeHeaders = new boolean[27];
        Set<Integer> QuantDatasetIds = new HashSet<Integer>();
        for (QuantProtein quantProt : searchQuantificationProtList) {
            QuantDatasetIds.add(quantProt.getDsKey());
        }
        StringBuilder sb = new StringBuilder();

        for (int index : QuantDatasetIds) {
            sb.append("  `index` = ").append(index);
            sb.append(" OR ");

        }
        String stat = sb.toString().substring(0, sb.length() - 4);
        try {
            PreparedStatement selectStudiesStat;

            String selectStudies = "SELECT * FROM  `combined_dataset_table` WHERE  " + stat;
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            selectStudiesStat = conn.prepareStatement(selectStudies);
            ResultSet rs = selectStudiesStat.executeQuery();
            while (rs.next()) {
                QuantDatasetObject quantDSObject = new QuantDatasetObject();
                String author = rs.getString("author");
                if (!activeHeaders[0] && author != null && !author.equalsIgnoreCase("Not Available")) {
                    activeHeaders[0] = true;
                }
                quantDSObject.setAuthor(author);
                int year = rs.getInt("year");
                if (!activeHeaders[1] && year != 0) {
                    activeHeaders[1] = true;
                }
                quantDSObject.setYear(year);
                int identified_proteins_num = rs.getInt("identified _proteins_num");
                if (!activeHeaders[2] && identified_proteins_num != -1 && identified_proteins_num != 0) {
                    activeHeaders[2] = true;
                }
                quantDSObject.setIdentifiedProteinsNumber(identified_proteins_num);

                int quantified_protein_num = rs.getInt("quantified_protein_num");
                if (!activeHeaders[3] && quantified_protein_num != -1) {
                    activeHeaders[3] = true;
                }
                quantDSObject.setQuantifiedProteinsNumber(quantified_protein_num);

                String disease_group = rs.getString("disease_group");
                if (!activeHeaders[4] && disease_group != null && !disease_group.equalsIgnoreCase("Not Available")) {
                    activeHeaders[4] = true;
                }
                quantDSObject.setDiseaseGroups(disease_group);

                String raw_data_url = rs.getString("raw_data_url");
                if (!activeHeaders[5] && raw_data_url != null && !raw_data_url.equalsIgnoreCase("Not Available")) {
                    activeHeaders[5] = true;
                }
                quantDSObject.setRawDataUrl(raw_data_url);

                int files_num = rs.getInt("files_num");
                if (!activeHeaders[6] && files_num != -1) {
                    activeHeaders[6] = true;
                }
                quantDSObject.setFilesNumber(files_num);

                String type_of_study = rs.getString("type_of_study");
                if (!activeHeaders[7] && type_of_study != null && !type_of_study.equalsIgnoreCase("Not Available")) {
                    activeHeaders[7] = true;
                }
                quantDSObject.setTypeOfStudy(type_of_study);

                String sample_type = rs.getString("sample_type");
                if (!activeHeaders[8] && sample_type != null && !sample_type.equalsIgnoreCase("Not Available")) {
                    activeHeaders[8] = true;
                }
                quantDSObject.setSampleType(sample_type);

                String sample_matching = rs.getString("sample_matching");
                if (!activeHeaders[9] && sample_matching != null && !sample_matching.equalsIgnoreCase("Not Available")) {
                    activeHeaders[9] = true;
                }
                quantDSObject.setSampleMatching(sample_matching);

                String shotgun_targeted = rs.getString("shotgun_targeted");
                if (!activeHeaders[10] && shotgun_targeted != null && !shotgun_targeted.equalsIgnoreCase("Not Available")) {
                    activeHeaders[10] = true;
                }
                quantDSObject.setShotgunTargeted(shotgun_targeted);

                String technology = rs.getString("technology");
                if (!activeHeaders[11] && technology != null && !technology.equalsIgnoreCase("Not Available")) {
                    activeHeaders[11] = true;
                }
                quantDSObject.setTechnology(technology);

                String analytical_approach = rs.getString("analytical_approach");
                if (!activeHeaders[12] && analytical_approach != null && !analytical_approach.equalsIgnoreCase("Not Available")) {
                    activeHeaders[12] = true;
                }
                quantDSObject.setAnalyticalApproach(analytical_approach);

                String enzyme = rs.getString("enzyme");
                if (!activeHeaders[13] && enzyme != null && !enzyme.equalsIgnoreCase("Not Available")) {
                    activeHeaders[13] = true;
                }
                quantDSObject.setEnzyme(enzyme);

                String quantification_basis = rs.getString("quantification_basis");
                if (!activeHeaders[14] && quantification_basis != null && !quantification_basis.equalsIgnoreCase("Not Available")) {
                    activeHeaders[14] = true;
                }

                quantDSObject.setQuantificationBasis(quantification_basis);

                String quant_basis_comment = rs.getString("quant_basis_comment");
                if (!activeHeaders[15] && quant_basis_comment != null && !quant_basis_comment.equalsIgnoreCase("Not Available")) {
                    activeHeaders[15] = true;
                }
                quantDSObject.setQuantBasisComment(quant_basis_comment);

                int id = rs.getInt("index");
                quantDSObject.setUniqId(id - 1);

                String normalization_strategy = rs.getString("normalization_strategy");
                if (!activeHeaders[16] && normalization_strategy != null && !normalization_strategy.equalsIgnoreCase("Not Available")) {
                    activeHeaders[16] = true;
                }
                quantDSObject.setNormalizationStrategy(normalization_strategy);

                String pumed_id = rs.getString("pumed_id");
                if (!activeHeaders[17] && pumed_id != null && !pumed_id.equalsIgnoreCase("Not Available")) {
                    activeHeaders[17] = true;
                }
                quantDSObject.setPumedID(pumed_id);

                String patient_group_i = rs.getString("patient_group_i");
                if (!activeHeaders[18] && patient_group_i != null && !patient_group_i.equalsIgnoreCase("Not Available")) {
                    activeHeaders[18] = true;
                }
                quantDSObject.setPatientsGroup1(patient_group_i);

                int patients_group_i_number = rs.getInt("patients_group_i_number");
                if (!activeHeaders[19] && patients_group_i_number != -1) {
                    activeHeaders[19] = true;
                }
                quantDSObject.setPatientsGroup1Number(patients_group_i_number);

                String patient_gr_i_comment = rs.getString("patient_gr_i_comment");
                if (!activeHeaders[20] && patient_gr_i_comment != null && !patient_gr_i_comment.equalsIgnoreCase("Not Available")) {
                    activeHeaders[20] = true;
                }
                quantDSObject.setPatientsGroup1Comm(patient_gr_i_comment);

                String patient_sub_group_i = rs.getString("patient_sub_group_i");
                if (!activeHeaders[21] && patient_sub_group_i != null && !patient_sub_group_i.equalsIgnoreCase("Not Available")) {
                    activeHeaders[21] = true;
                }
                quantDSObject.setPatientsSubGroup1(patient_sub_group_i);

                String patient_group_ii = rs.getString("patient_group_ii");
                if (!activeHeaders[22] && patient_group_ii != null && !patient_group_ii.equalsIgnoreCase("Not Available")) {
                    activeHeaders[22] = true;
                }
                quantDSObject.setPatientsGroup2(patient_group_ii);

                int patients_group_ii_number = rs.getInt("patients_group_ii_number");
                if (!activeHeaders[23] && patients_group_ii_number != -1) {
                    activeHeaders[23] = true;
                }
                quantDSObject.setPatientsGroup2Number(patients_group_ii_number);

                String patient_gr_ii_comment = rs.getString("patient_gr_ii_comment");
                if (!activeHeaders[24] && patient_gr_ii_comment != null && !patient_gr_ii_comment.equalsIgnoreCase("Not Available")) {
                    activeHeaders[24] = true;
                }
                quantDSObject.setPatientsGroup2Comm(patient_gr_ii_comment);

                String patient_sub_group_ii = rs.getString("patient_sub_group_ii");
                if (!activeHeaders[25] && patient_sub_group_ii != null && !patient_sub_group_ii.equalsIgnoreCase("Not Available")) {
                    activeHeaders[25] = true;
                }
                quantDSObject.setPatientsSubGroup2(patient_sub_group_ii);

                String additional_comments = rs.getString("additional_comments");
                if (!activeHeaders[26] && additional_comments != null && !additional_comments.equalsIgnoreCase("Not Available")) {
                    activeHeaders[26] = true;
                }
                quantDSObject.setAdditionalcomments(additional_comments);

                quantDatasetList.add(quantDSObject);

            }
            rs.close();
            QuantDatasetInitialInformationObject datasetObject = new QuantDatasetInitialInformationObject();
            Map<Integer, QuantDatasetObject> updatedQuantDatasetObjectMap = new LinkedHashMap<Integer, QuantDatasetObject>();

            for (QuantDatasetObject ds : quantDatasetList) {
                updatedQuantDatasetObjectMap.put(ds.getUniqId(), ds);
            }

            datasetObject.setQuantDatasetsList(updatedQuantDatasetObjectMap);
            datasetObject.setActiveHeaders(activeHeaders);
            return datasetObject;

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 3156 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error line 3159 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error line 3162 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error line 3165 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }
        System.gc();

        return null;

    }

    /**
     * get active quantification pie charts filters within quant searching
     * proteins results (to hide them if they are empty)
     *
     * @param searchQuantificationProtList
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public boolean[] getActivePieChartQuantFilters(List<QuantProtein> searchQuantificationProtList) {
        String[] columnArr = new String[]{"`identified _proteins_num`", "`quantified_protein_num`", "`disease_group`", "`raw_data_url`", "`year`", "`type_of_study`", "`sample_type`", "`sample_matching`", "`technology`", "`analytical_approach`", "`enzyme`", "`shotgun_targeted`", "`quantification_basis`", "`quant_basis_comment`", "`patients_group_i_number`", "`patients_group_ii_number`", "`normalization_strategy`"};
        boolean[] activeFilters = new boolean[columnArr.length];
        Set<Integer> QuantDatasetIds = new HashSet<Integer>();
        for (QuantProtein quantProt : searchQuantificationProtList) {
            QuantDatasetIds.add(quantProt.getDsKey());
        }
        StringBuilder sb = new StringBuilder();

        for (int index : QuantDatasetIds) {
            sb.append("  `index` = ").append(index);
            sb.append(" OR ");

        }
        String stat = sb.toString().substring(0, sb.length() - 4);

        try {

            PreparedStatement selectPumed_idStat;
            String selectPumed_id = "SELECT  `pumed_id` FROM  `combined_dataset_table` WHERE " + stat + "  GROUP BY  `pumed_id` ORDER BY  `pumed_id` ";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectPumed_idStat = conn.prepareStatement(selectPumed_id);
            ResultSet rs = selectPumed_idStat.executeQuery();
            int pumed_id_index = 0;
            while (rs.next()) {
                pumed_id_index++;
            }
            rs.close();

            /// check the colums one by one 
            for (int index = 0; index < columnArr.length; index++) {
                String selectPumed_id1 = "SELECT  `pumed_id` FROM  `combined_dataset_table`   WHERE " + stat + "   GROUP BY  `pumed_id`, " + columnArr[index] + " ORDER BY  `pumed_id` ";
                if (conn == null || conn.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                }
                selectPumed_idStat = conn.prepareStatement(selectPumed_id1);
                rs = selectPumed_idStat.executeQuery();
                int pumed_id_com_index = 0;
                while (rs.next()) {
                    pumed_id_com_index++;
                }
                rs.close();
                if (pumed_id_index != pumed_id_com_index) {
                    activeFilters[index] = true;

                }

            }
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 3235 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error line 3238 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error line 3241 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error line 3244 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }
        System.gc();
        activeFilters[0] = false;
        activeFilters[1] = false;
        activeFilters[2] = false;
        activeFilters[3] = true;
        activeFilters[4] = true;
        activeFilters[7] = false;
        activeFilters[activeFilters.length - 2] = false;
        activeFilters[activeFilters.length - 3] = false;
        activeFilters[activeFilters.length - 4] = false;

        return activeFilters;

    }

    /**
     * store quant proteins list to database
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
                insertQProtStat.setString(25, qprot.getPeptideSequance());
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
     * get quant proteins list for quant dataset
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
            String selectDsGroupNum = "SELECT `patients_group_i_number` , `patients_group_ii_number` FROM `combined_dataset_table` Where  `index`=?;";
            PreparedStatement selectselectDsGroupNumStat = conn.prepareStatement(selectDsGroupNum);
            selectselectDsGroupNumStat.setInt(1, quantDatasetId + 1);
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
            selectQuantProtStat.setInt(1, quantDatasetId + 1);
            ResultSet rs1 = selectQuantProtStat.executeQuery();
            while (rs1.next()) {
                QuantProtein quantProt = new QuantProtein();
                quantProt.setPatientsGroupIINumber(groupIINum);
                quantProt.setPatientsGroupINumber(groupINum);
                quantProt.setProtKey(rs1.getInt("index"));
                quantProt.setDsKey(quantDatasetId);
                quantProt.setSequance(rs1.getString("sequance"));
                quantProt.setUniprotAccession(rs1.getString("uniprot_accession"));
                quantProt.setUniprotProteinName(rs1.getString("uniprot_protein_name"));
                quantProt.setPublicationAccNumber(rs1.getString("publication_acc_number"));
                quantProt.setPublicationProteinName(rs1.getString("publication_protein_name"));
                quantProt.setQuantifiedPeptidesNumber(rs1.getInt("quantified_peptides_number"));
                quantProt.setIdentifiedProteinsNum(rs1.getInt("identified_peptides_number"));
                quantProt.setStringFCValue(rs1.getString("fold_change"));
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
     * get quant proteins list for a number of quant datasets
     *
     * @param quantDatasetIds
     * @return quant proteins list
     */
    public Set<QuantProtein> getQuantificationProteins(int[] quantDatasetIds) {
        Set<QuantProtein> quantProtList = new HashSet<QuantProtein>();
        Set<QuantProtein> tquantProtList = new HashSet<QuantProtein>();
        try {
            StringBuilder sb = new StringBuilder();

            for (int index : quantDatasetIds) {
                sb.append("  `index` = ").append(index + 1);
                sb.append(" OR ");

            }
            String stat = sb.toString().substring(0, sb.length() - 4);

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String selectDsGroupNum = "SELECT `patients_group_i_number` , `patients_group_ii_number`,`patient_group_i`,`patient_group_ii`,`patient_sub_group_i`,`patient_sub_group_ii`,`index` FROM `combined_dataset_table` WHERE  " + stat + " ;";
            PreparedStatement selectselectDsGroupNumStat = conn.prepareStatement(selectDsGroupNum);
            ResultSet rs = selectselectDsGroupNumStat.executeQuery();
            Map<Integer, Object[]> datasetIdDesGrs = new HashMap<Integer, Object[]>();
            while (rs.next()) {
                datasetIdDesGrs.put(rs.getInt("index"), new Object[]{rs.getInt("patients_group_i_number"), rs.getInt("patients_group_ii_number"), rs.getString("patient_group_i"), rs.getString("patient_group_ii"), rs.getString("patient_sub_group_i"), rs.getString("patient_sub_group_ii")});
            }
            rs.close();

            sb = new StringBuilder();
            for (int index : quantDatasetIds) {
                sb.append("  `ds_ID` = ").append(index + 1);
                sb.append(" OR ");

            }
            stat = sb.toString().substring(0, sb.length() - 4);

            String selectQuantProt = "SELECT * FROM `quantitative_proteins_table`  WHERE  " + stat + " ;";
            PreparedStatement selectQuantProtStat = conn.prepareStatement(selectQuantProt);
            ResultSet rs1 = selectQuantProtStat.executeQuery();
            while (rs1.next()) {
                QuantProtein quantProt = new QuantProtein();
                quantProt.setProtKey(rs1.getInt("index"));
                quantProt.setDsKey(rs1.getInt("ds_ID") - 1);
                quantProt.setSequance(rs1.getString("sequance"));
                quantProt.setUniprotAccession(rs1.getString("uniprot_accession"));
                quantProt.setUniprotProteinName(rs1.getString("uniprot_protein_name"));
                quantProt.setPublicationAccNumber(rs1.getString("publication_acc_number"));
                quantProt.setPublicationProteinName(rs1.getString("publication_protein_name"));
                quantProt.setQuantifiedPeptidesNumber(rs1.getInt("quantified_peptides_number"));
                quantProt.setIdentifiedProteinsNum(rs1.getInt("identified_peptides_number"));
                quantProt.setStringFCValue(rs1.getString("fold_change"));
                quantProtList.add(quantProt);
            }
            rs1.close();
            for (QuantProtein qp : quantProtList) {
                qp.setPatientsGroupIINumber((Integer) datasetIdDesGrs.get(qp.getDsKey() + 1)[1]);
                qp.setPatientsGroupINumber((Integer) datasetIdDesGrs.get(qp.getDsKey() + 1)[0]);
                qp.setPatientGroupI(datasetIdDesGrs.get(qp.getDsKey() + 1)[2].toString());
                qp.setPatientGroupII(datasetIdDesGrs.get(qp.getDsKey() + 1)[3].toString());
                qp.setPatientSubGroupI(datasetIdDesGrs.get(qp.getDsKey() + 1)[4].toString());
                qp.setPatientSubGroupII(datasetIdDesGrs.get(qp.getDsKey() + 1)[5].toString());
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
     * get quant peptides list for specific quant dataset
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
            selectQuantProtStat.setInt(1, quantDatasetId + 1);
            ResultSet rs1 = selectQuantProtStat.executeQuery();
            while (rs1.next()) {
                QuantPeptide quantPeptide = new QuantPeptide();
                quantPeptide.setDsKey(quantDatasetId);
                quantPeptide.setProtIndex(rs1.getInt("prot_index") - 1);
                quantPeptide.setUniqueId(rs1.getInt("index"));
                quantPeptide.setPeptideModification(rs1.getString("peptide_modification"));
                quantPeptide.setPeptideSequance(rs1.getString("peptide_sequance"));
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
     * get quant peptides list for specific quant dataset
     *
     * @param quantDatasetIds
     * @return quant peptides list
     */
    public Map<String, Set<QuantPeptide>> getQuantificationPeptides(int[] quantDatasetIds) {
        Set<QuantPeptide> quantPeptidetList = new HashSet<QuantPeptide>();
        try {
            StringBuilder sb = new StringBuilder();

            for (int index : quantDatasetIds) {
                sb.append("  `DsKey` = ").append(index + 1);
                sb.append(" OR ");

            }
            String stat = sb.toString().substring(0, sb.length() - 4);

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            String selectQuantPeptides = "SELECT * FROM `quantitative_peptides_table` WHERE " + stat;
            PreparedStatement selectQuantProtStat = conn.prepareStatement(selectQuantPeptides);
//            selectQuantProtStat.setInt(1, quantDatasetId + 1);
            ResultSet rs1 = selectQuantProtStat.executeQuery();
            while (rs1.next()) {
                QuantPeptide quantPeptide = new QuantPeptide();
                quantPeptide.setDsKey(rs1.getInt("DsKey") - 1);
                quantPeptide.setProtIndex(rs1.getInt("prot_index") - 1);
                quantPeptide.setUniqueId(rs1.getInt("index"));
                quantPeptide.setPeptideModification(rs1.getString("peptide_modification"));
                quantPeptide.setPeptideSequance(rs1.getString("peptide_sequance"));
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
     * fill quant proteins information from the result set
     *
     * @param resultSet results set to fill identification peptides data
     * @return quant proteins List
     */
    private List<QuantProtein> fillQuantProtData(ResultSet rs) {

        List<QuantProtein> quantProtResultList = new ArrayList<QuantProtein>();

        try {
            while (rs.next()) {

                QuantProtein qpb = new QuantProtein();
                qpb.setUniprotAccession(rs.getString("uniprot_accession"));
                qpb.setUniprotProteinName(rs.getString("uniprot_protein_name"));
                qpb.setPublicationAccNumber(rs.getString("publication_acc_number"));
                qpb.setPublicationProteinName(rs.getString("publication_protein_name"));
                qpb.setStringFCValue(rs.getString("fold_change"));
                qpb.setStringPValue(rs.getString("string_p_value"));
                qpb.setPeptideIdNumb(rs.getInt("identified_peptides_number"));
                qpb.setQuantifiedPeptidesNumber(rs.getInt("quantified_peptides_number"));
                qpb.setFcPatientGroupIonPatientGroupII(rs.getDouble("fc_value"));
                qpb.setRocAuc(rs.getDouble("roc_auc"));
                qpb.setpValue(rs.getDouble("p_value"));
                qpb.setProtKey(rs.getInt("index"));
                qpb.setDsKey(rs.getInt("ds_ID"));
                qpb.setPvalueComment(rs.getString("p_value_comments"));
                qpb.setSequance(rs.getString("sequance"));
                quantProtResultList.add(qpb);

            }

        } catch (Exception exp) {
            System.err.println("at error line 2119 " + this.getClass().getName() + "   " + exp.getLocalizedMessage());
        }
        return quantProtResultList;

    }

    /**
     * search for quant proteins by keywords
     *
     * @param query query object that has all query information
     * @return Quant Proteins Searching List
     */
    public synchronized List<QuantProtein> searchQuantificationProteins(Query query) {

        StringBuilder sb = new StringBuilder();
        QueryConstractorHandler qhandler = new QueryConstractorHandler();

        PreparedStatement selectProStat;
        String selectPro;
        //main filters  
        if (query.getSearchKeyWords() != null && !query.getSearchKeyWords().equalsIgnoreCase("")) {

            String[] queryWordsArr = query.getSearchKeyWords().split(" ");
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
                    sb.append("`uniprot_accession` = ? OR `publication_acc_number` = ?");
                    qhandler.addQueryParam("String", str);
                    qhandler.addQueryParam("String", str);
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
//           
            List<QuantProtein> quantProtResultList = fillQuantProtData(rs);
            System.gc();

            Set<Integer> datasetsIds = new HashSet<Integer>();
            for (QuantProtein quantProt : quantProtResultList) {

                datasetsIds.add(quantProt.getDsKey());
            }

            String selectDsGroupNum = "SELECT `patients_group_i_number` , `patients_group_ii_number` FROM `combined_dataset_table` Where  `index`=?;";
            PreparedStatement selectselectDsGroupNumStat = conn.prepareStatement(selectDsGroupNum);

            Map<Integer, int[]> dsDGrNumMap = new HashMap<Integer, int[]>();
            for (int i : datasetsIds) {
                selectselectDsGroupNumStat.setInt(1, i);
                ResultSet dsIdRs = selectselectDsGroupNumStat.executeQuery();

                while (dsIdRs.next()) {
                    int[] grNumArr = new int[]{dsIdRs.getInt("patients_group_i_number"), dsIdRs.getInt("patients_group_ii_number")};
                    dsDGrNumMap.put(i, grNumArr);
                }
                dsIdRs.close();

            }
            List<QuantProtein> updatedQuantProtResultList = new ArrayList<QuantProtein>();
            for (QuantProtein quantProt : quantProtResultList) {

                if (dsDGrNumMap.containsKey(quantProt.getDsKey())) {
                    int[] grNumArr = dsDGrNumMap.get(quantProt.getDsKey());
                    quantProt.setPatientsGroupINumber(grNumArr[0]);
                    quantProt.setPatientsGroupIINumber(grNumArr[1]);
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
            System.err.println("at error line 3665 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return null;
        }

    }
}
