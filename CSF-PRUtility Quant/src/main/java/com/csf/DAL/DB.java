package com.csf.DAL;

import com.pepshaker.util.beans.ExperimentBean;
import com.pepshaker.util.beans.FractionBean;
import com.pepshaker.util.beans.IdentificationProteinBean;
import com.pepshaker.util.beans.PeptideBean;
import com.pepshaker.util.beans.ProteinBean;
import com.quantcsf.beans.QuantDatasetObject;
import com.quantcsf.beans.QuantPeptide;
import com.quantcsf.beans.QuantProtein;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Yehia Farag
 */
public class DB implements Serializable {

    private Connection conn_ii = null;
    private Connection conn = null;
    private Connection conn_i = null;
    private final String url;
    private final String dbName;
    private final String driver;
    private final String userName;
    private final String password;

    public DB(String url, String dbName, String driver, String userName, String password) {
        this.url = url;
        this.dbName = dbName;
        this.driver = driver;
        this.userName = userName;
        this.password = password;
    }

    public boolean createTables() throws SQLException//create CSF the database tables if not exist
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
                System.out.println("database created with name " + dbName);

                //temp
                String sqoDataBase = "SHOW DATABASES ;";
                ResultSet rs1 = statement.executeQuery(sqoDataBase);
                Set<String> datasetnames = new HashSet<String>();
                while (rs1.next()) {
                    String db = rs1.getString("Database");
                    datasetnames.add(db);
                    System.err.println("db is " + db);

                }
//                 Statement statement2 = conn_i.createStatement();
//                
//                for(String db:datasetnames)
//                {
//                    if(!db.equals(dbName) && !db.equalsIgnoreCase("csf_db_v2") && !db.equalsIgnoreCase("all_no_MCP"))
//                        statement2.executeUpdate("DROP DATABASE "+ db+" ;");
//                
//                }
//                conn_i.close();
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

                String statment = "CREATE TABLE IF NOT EXISTS `publication_table` (\n"
                        + "  `pubmed_id` varchar(15) NOT NULL default 'Not Available',\n"
                        + "  `author` varchar(1000) NOT NULL default 'Not Available',\n"
                        + "  `year` varchar(1000) NOT NULL default 'Not Available',\n"
                        + "  `title` varchar(1000) NOT NULL default 'Not Available',\n"
                        + "  `active` varchar(5) NOT NULL default 'false',\n"
                        + "  `total_prot_num` int(255) NOT NULL default '0',\n"
                        + "  `uniq_prot_num` int(255) NOT NULL default '0',\n"
                        + "  `total_pept_num` int(255) NOT NULL default '0',\n"
                        + "  `uniq_pept_num` int(255) NOT NULL default '0',\n"
                        + "  PRIMARY KEY  (`pubmed_id`)\n"
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8;";
                st.executeUpdate(statment);
//

                String dropStat = "TRUNCATE TABLE  `publication_table`";
                st = conn.createStatement();
                st.executeUpdate(dropStat);
                st.close();

                String inserStat = "INSERT INTO publication_table VALUES ('25098164', 'Liguori, Maria, et al.', '2014', 'Proteomic profiling in multiple sclerosis clinical courses reveals potential biomarkers of neurodegeneration.', 'false', 0, 0, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('20093204', 'Ottervald, Jan, et al.', '2010', 'Multiple sclerosis: Identification and clinical evaluation of novel CSF biomarkers.', 'false', 0, 0, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('20237129', 'Comabella, Manuel, et al.', '2010', 'Cerebrospinal fluid chitinase 3-like 1 levels are associated with conversion to multiple sclerosis.', 'true', 23, 0, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('20600910', 'Harris, Violaine K., et al.', '2010', 'Bri2-23 is a potential cerebrospinal fluid biomarker in multiple sclerosis.', 'false', 0, 0, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('22473675', 'Dhaunchak, Ajit Singh, et al.', '2012', 'Implication of perturbed axoglial apparatus in early pediatric multiple sclerosis.', 'false', 0, 0, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('22846148', 'Jia, Yan, et al.', '2012', 'Development of protein biomarkers in cerebrospinal fluid for secondary progressive multiple sclerosis using selected reaction monitoring mass spectrometry (SRM-MS).', 'true', 26, 7, 26, 15);\n"
                        + "INSERT INTO publication_table VALUES ('23059536', 'Kroksveen, Ann C., et al.', '2013', 'Discovery and initial verification of differentially abundant proteins between multiple sclerosis patients and controls using iTRAQ and SID-SRM.', 'true', 816, 248, 23, 2);\n"
                        + "INSERT INTO publication_table VALUES ('23278663', 'Kroksveen, Ann C., et al.', '2012', 'Cerebrospinal fluid proteome comparison between multiple sclerosis patients and controls.', 'true', 77, 2, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('25406498', 'Cantó, Ester, et al.', '2014', 'Validation of semaphorin 7A and ala-beta-his-dipeptidase as biomarkers associated with the conversion from clinically isolated syndrome to multiple sclerosis.', 'true', 6, 0, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('25698171', 'Hinsinger, G., et al.', '2015', 'Chitinase 3-like proteins as diagnostic and prognostic biomarkers of multiple sclerosis.', 'true', 526, 97, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('23339689', 'Stoop, Marcel P., et al', '2013', 'Effects of natalizumab treatment on the cerebrospinal fluid proteome of multiple sclerosis patients.', 'true', 310, 17, 2971, 2708);\n"
                        + "INSERT INTO publication_table VALUES ('26152395', 'Kroksveen, Ann C., et al.', '2015', 'Quantitative proteomics suggests decrease in the secretogranin-1 cerebrospinal fluid levels during the disease course of multiple sclerosis.', 'true', 771, 340, 4, 1);\n"
                        + "INSERT INTO publication_table VALUES ('24902845', 'Wildsmith, Kristin R., et al.', '2014', 'Identification of longitudinally dynamic biomarkers in Alzheimer''s disease cerebrospinal fluid by targeted proteomics.', 'true', 28, 1, 31, 5);\n"
                        + "INSERT INTO publication_table VALUES ('25318543', 'Barucker, Christian, et al.', '2015', 'Alzheimer amyloid peptide a beta 42 regulates gene expression of transcription and growth factors.', 'true', 107, 18, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('24039694', 'Schutzer, Steven E., et al.', '2013', 'Gray matter is targeted in first-attack multiple sclerosis.', 'true', 19, 0, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('25556233', 'Shi, Min, et al.', '2015', 'Cerebrospinal fluid peptides as potential Parkinson disease biomarkers: a staged pipeline for discovery and validation.', 'true', 16, 0, 17, 7);\n"
                        + "INSERT INTO publication_table VALUES ('26552840', 'Borràs, Eva, et al.', '2015', 'Protein-Based Classifier to Predict Conversion from Clinically Isolated Syndrome to Multiple Sclerosis.', 'true', 22, 3, 32, 4);\n"
                        + "INSERT INTO publication_table VALUES ('25676562', 'Spellman, Daniel S., et al.', '2015', 'Development and evaluation of a multiplexed mass spectrometry based assay for measuring candidate peptide biomarkers in Alzheimer''s Disease Neuroimaging Initiative (ADNI) CSF', 'true', 35, 2, 53, 31);\n"
                        + "INSERT INTO publication_table VALUES ('22327139', 'Lehnert, Stefan, et al.', '2012', 'iTRAQ and multiple reaction monitoring as proteomic tools for biomarker search in cerebrospinal fluid of patients with Parkinson''s disease dementia.', 'true', 14, 4, 14, 13);\n"
                        + "INSERT INTO publication_table VALUES ('26401960', 'Collins, Mahlon A., et al.', '2015', 'Label-Free LC-MS/MS Proteomic Analysis of Cerebrospinal Fluid Identifies Protein/Pathway Alterations and Candidate Biomarkers for Amyotrophic Lateral Sclerosis.', 'false', 0, 0, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('24295388', 'Varghese, Anu Mary, et al.', '2013', 'Chitotriosidase - a putative biomarker for sporadic amyotrophic lateral sclerosis.', 'false', 0, 0, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('26627638', 'Heywood, Wendy E., et al.', '2015', 'Identification of novel CSF biomarkers for neurodegeneration and their validation by a high-throughput multiplexed targeted proteomic assay.', 'true', 25, 3, 31, 13);\n"
                        + "INSERT INTO publication_table VALUES ('26841090', 'Opsahl, Jill A., et al.', '2016', 'Label free analysis of human cerebrospinal fluid addressing various normalization strategies and revealing protein groups affected by multiple sclerosis.', 'true', 904, 276, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('27845782', 'Paterson, R. W., et al.', '2016', 'A targeted proteomic multiplex CSF assay identifies increased malate dehydrogenase and other neurodegenerative biomarkers in individuals with Alzheimer\\'s disease pathology.', 'true', 0, 0, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('27728768', 'Kroksveen, Ann Cathrine, et al.', '2017', 'In-Depth Cerebrospinal Fluid Quantitative Proteome and Deglycoproteome Analysis: Presenting a Comprehensive Picture of Pathways and Processes Affected by Multiple Sclerosis.', 'true', 0, 0, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('29708756', 'Begcevic, Ilijana, et al.', '2018', 'Targeted Mass Spectrometry-Based Assays for Relative Quantification of 30 Brain-Related Proteins and Their Clinical Applications.', 'true', 0, 0, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('28941200', 'Stoop, Marcel P., et al.', '2017', 'Decreased Neuro-Axonal Proteins in CSF at First Attack of Suspected Multiple Sclerosis.', 'true', 0, 0, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('29028155', 'Brinkmalm, Gunnar, et al.', '2018', 'A parallel reaction monitoring mass spectrometric method for analysis of potential CSF biomarkers for Alzheimers disease.', 'true', 0, 0, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('29873030', 'Timirci-Kahraman, O., et al.', '2018', 'Identification of candidate biomarkers in converting and non-converting clinically isolated syndrome by proteomics analysis of cerebrospinal fluid.', 'true', 0, 0, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('29684683', 'Begcevic, Ilijana, et al.', '2018', 'Brain-related proteins as potential CSF biomarkers of Alzheimers disease: A targeted mass spectrometry approach.', 'true', 0, 0, 0, 0);\n"
                        + "INSERT INTO publication_table VALUES ('27347402', 'Pavelek, Zbyšek, et al.','2016','Proteomic analysis of cerebrospinal fluid for relapsing-remitting multiple sclerosis and clinically isolated syndrome.','true', 0, 0, 0, 0);";
                st = conn.createStatement();
                for (String str : inserStat.split("\n")) {
                    st.executeUpdate(str);
                }
                st = conn.createStatement();
                statment = "CREATE TABLE IF NOT EXISTS `defin_disease_groups` (\n"
                        + "  `min` varchar(100) NOT NULL default ' ',\n"
                        + "  `full` varchar(500) NOT NULL default ' ',\n"
                        + "  PRIMARY KEY  (`min`)\n"
                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(statment);
//                st = conn.createStatement();
//                String insertStat = "INSERT INTO `defin_disease_groups` (`min`, `full`) VALUES \n"
//                        + "(' MS', ' Multiple sclerosis'),\n"
//                        + "(' CIS', ' Clinically isolated syndrome'),\n"
//                        + "(' OND', ' Other neurological disorders'),\n"
//                        + "(' ONID', ' Other inflammatory neurological disorders'),\n"
//                        + "(' RRMS', ' Relapsing remitting multiple sclerosis'),\n"
//                        + "(' SPMS', ' Secondary progressive multiple sclerosis'),\n"
//                        + "(' PMS', 'progressive multiple sclerosis'),\n"
//                        + "(' CIS-MS', ' Clinically isolated syndrome, with conversion to multiple sclerosis'),\n"
//                        + "('CIS-CIS', 'Clinically isolated syndrome, without conversion to multiple sclerosis'),\n"
//                        + "(' CIS-MS/CIS', ' Clinically isolated syndrome, with and without conversion to multiple sclerosis'),\n"
//                        + "(' AD', 'Alzheimer''s disease'),\n"
//                        + "(' MCI', ' Mild cognitive impairment'),\n"
//                        + "(' RRMS a/ Natalizumab', ' Relapsing remitting multiple sclerosis after natalizumab'),\n"
//                        + "(' SPMS a/Lamotrigine', ' Secondary progressive multiple sclerosis after lamotrigine'),\n"
//                        + "(' OIND + OND ', ' Other inflammatory neurological disorders + Other neurological disorders');";
//                st.executeUpdate(insertStat);
                st = conn.createStatement();
                statment = "CREATE TABLE IF NOT EXISTS `quant_dataset_table` (\n"
                        + "  `study_key` varchar(100) NOT NULL default 'Not Available',\n"
                        + "  `pumed_id` varchar(30) NOT NULL default 'Not Available',\n"
                        + "  `files_num` int(255) NOT NULL default '-1',\n"
                        + "  `identified_proteins_number` int(255) NOT NULL default '-1',\n"
                        + "  `quantified_proteins_number` int(255) NOT NULL default '-1',\n"
                        + "  `raw_data_available` varchar(500) NOT NULL default 'Raw Data Not Available',\n"
                        + "  `year` int(4) NOT NULL default '0',\n"
                        + "  `index` int(255) NOT NULL default '0',\n"
                        + "  `type_of_study` varchar(200) NOT NULL default 'Not Available',\n"
                        + "  `sample_type` varchar(200) NOT NULL default 'Not Available',\n"
                        + "  `sample_matching` varchar(300) NOT NULL default 'Not Available',\n"
                        + "  `technology` varchar(300) NOT NULL default 'Not Available',\n"
                        + "  `analytical_approach` varchar(300) NOT NULL default 'Not Available',\n"
                        + "  `enzyme` varchar(300) NOT NULL default 'Not Available',\n"
                        + "  `shotgun_targeted` varchar(200) NOT NULL default 'Not Available',\n"
                        + "  `quantification_basis` varchar(200) NOT NULL default 'Not Available',\n"
                        + "  `quant_basis_comment` varchar(500) NOT NULL default 'Not Available',\n"
                        + "  `patients_group_i_number` int(255) NOT NULL default '-1000000000',\n"
                        + "  `patients_group_ii_number` int(255) NOT NULL default '-1000000000',\n"
                        + "  `normalization_strategy` varchar(600) NOT NULL default 'Not Available',\n"
                        + "  `author` varchar(300) NOT NULL default 'John Smith',\n"
                        + "  `patient_group_i` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `patient_gr_i_comment` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `patient_sub_group_i` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `patient_group_ii` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `patient_sub_group_ii` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `patient_gr_ii_comment` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `analytical_method` varchar(500) NOT NULL default 'Not Available',\n"
                        + "  `disease_category` varchar(200) NOT NULL default 'Not Available',\n"
                        + "  `total_prot_num` int(255) NOT NULL default '0',\n"
                        + "  `uniq_prot_num` int(255) NOT NULL default '0',\n"
                        + "  `total_pept_num` int(255) NOT NULL default '0',\n"
                        + "  `uniq_pept_num` int(255) NOT NULL default '0',`pooledsamples` varchar(5) NOT NULL default 'False',\n"
                        + "  PRIMARY KEY  (`index`)\n"
                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf8;";
                st.executeUpdate(statment);

                st = conn.createStatement();
                statment = "CREATE TABLE IF NOT EXISTS  `quant_full_table` (\n"
                        + "  `author` varchar(500) NOT NULL,\n"
                        + "  `year` int(255) NOT NULL,\n"
                        + "  `pumed_id` varchar(150) NOT NULL default 'Not Available',\n"
                        + "  `study_key` varchar(100) NOT NULL default 'Not Available',\n"
                        + "  `uniprot_accession` varchar(150) NOT NULL default 'Not Available',\n"
                        + "  `uniprot_protein_name` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `publication_acc_number` varchar(150) NOT NULL default 'Not Available',\n"
                        + "  `publication_protein_name` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `raw_data_available` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `type_of_study` varchar(150) NOT NULL default 'Not Available',\n"
                        + "  `sample_type` varchar(150) NOT NULL default 'Not Available',\n"
                        + "  `patient_group_i` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `patient_sub_group_i` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `patient_gr_i_comment` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `patient_group_ii` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `patient_sub_group_ii` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `patient_gr_ii_comment` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `sample_matching` varchar(500) NOT NULL default 'Not Available',\n"
                        + "  `normalization_strategy` varchar(500) NOT NULL default 'Not Available',\n"
                        + "  `technology` varchar(500) NOT NULL default 'Not Available',\n"
                        + "  `analytical_method` varchar(500) NOT NULL default 'Not Available',\n"
                        + "  `analytical_approach` varchar(500) NOT NULL default 'Not Available',\n"
                        + "  `enzyme` varchar(500) NOT NULL default 'Not Available',\n"
                        + "  `shotgun_targeted` varchar(100) NOT NULL default 'Not Available',\n"
                        + "  `quantification_basis` varchar(500) NOT NULL default 'Not Available',\n"
                        + "  `quant_basis_comment` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `additional_comments` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `q_peptide_key` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `peptide_sequance` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `sequence_annotated` varchar(500) NOT NULL default 'Not Available',\n"
                        + "  `peptide_modification` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `modification_comment` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `string_fc_value` varchar(200) NOT NULL default 'Not Available',\n"
                        + "  `string_p_value` varchar(200) NOT NULL default 'Not Available',\n"
                        + "  `pvalue_comments` varchar(500) NOT NULL default 'Not Available',\n"
                        + "  `quantified_proteins_number` int(255) NOT NULL default '-1',\n"
                        + "  `identified_proteins_number` int(255) NOT NULL default '-1',\n"
                        + "  `peptideId_number` int(255) NOT NULL default '-1',\n"
                        + "  `quantified_peptides_number` int(255) NOT NULL default '-1',\n"
                        + "  `patients_group_i_number` int(255) NOT NULL default '-1',\n"
                        + "  `patients_group_ii_number` int(255) NOT NULL default '-1',\n"
                        + "  `p_value` double NOT NULL default '-1000000000',\n"
                        + "  `pvalue_significance_threshold` varchar(500) NOT NULL default '-1000000000',\n"
                        + "  `roc_auc` double NOT NULL default  '-1000000000',\n"
                        + "  `fc_value` double NOT NULL default '-1000000000',\n"
                        + "  `log_2_FC` double NOT NULL default '-1000000000',\n"
                        + "  `peptide_prot` varchar(5) NOT NULL default 'False',\n"
                        + "  `index` int(255) NOT NULL default '0',\n"
                        + "  `peptide_charge` int(255) NOT NULL default '-1',\n"
                        + "  `disease_category` varchar(200) NOT NULL default 'Not Available', `pooledsamples` varchar(5) NOT NULL default 'False',\n"
                        + "  PRIMARY KEY  (`index`)\n"
                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf8;";

                st.executeUpdate(statment);

                st = conn.createStatement();
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

                st = conn.createStatement();
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

                st = conn.createStatement();
                statment = "CREATE TABLE IF NOT EXISTS `experiment_peptides_proteins_table` ( "
                        + "  `exp_id` varchar(50) NOT NULL, "
                        + "  `peptide_id` int(50) NOT NULL, "
                        + "  `protein` varchar(1000) NOT NULL "
                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf8; ";
                st.executeUpdate(statment);
                st = conn.createStatement();
                statment = "CREATE TABLE IF NOT EXISTS `experiment_peptides_table` ( "
                        + "  `exp_id` int(11) NOT NULL default '0', "
                        + "  `pep_id` int(11) NOT NULL auto_increment, "
                        + "  PRIMARY KEY  (`pep_id`), "
                        + "  KEY `exp_id` (`exp_id`) "
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ; ";
                st.executeUpdate(statment);

                st = conn.createStatement();
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
                        + "  `description` varchar(1000) NOT NULL, "
                        + "  `prot_group_id` int(255) NOT NULL auto_increment, "
                        + "  PRIMARY KEY  (`prot_group_id`), "
                        + "  KEY `exp_id` (`exp_id`), "
                        + "  KEY `prot_key` (`prot_key`(333)) "
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ; ";
                st.executeUpdate(statment);
                st = conn.createStatement();
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
                st = conn.createStatement();
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

                st = conn.createStatement();
                statment = "CREATE TABLE IF NOT EXISTS `quantitative_peptides_table` (\n"
                        + "  `index` int(255) NOT NULL default '0',\n"
                        + "  `prot_index` int(255) NOT NULL default '-1',\n"
                        + "  `peptide_sequance` varchar(600) NOT NULL default 'Not Available',\n"
                        + "  `peptide_modification` varchar(600) NOT NULL default 'Not Available',\n"
                        + "  `modification_comment` varchar(600) NOT NULL default 'Not Available',\n"
                        + "  `string_fc_value` varchar(200) NOT NULL default 'Not Provided',\n"
                        + "  `string_p_value` varchar(200) NOT NULL default 'Not Available',\n"
                        + "  `p_value` double NOT NULL default '-1000000000',\n"
                        + "  `roc_auc` double NOT NULL default '-1000000000',\n"
                        + "  `fc_value` double NOT NULL default '-1000000000',\n"
                        + "  `DsKey` int(255) NOT NULL default '-1',\n"
                        + "  `p_value_comments` varchar(500) NOT NULL default 'Not Available',\n"
                        + "  `proteinAccession` varchar(50) NOT NULL default 'Not Available',\n"
                        + "  `additional_comments` varchar(100) NOT NULL default 'Not Available',\n"
                        + "  `quant_bases_comments` varchar(1000) NOT NULL default 'Not Available',\n"
                        + "  `log_2_FC` double NOT NULL default '-1000000000',\n"
                        + "  `pvalue_significance_threshold` varchar(500) NOT NULL default 'Not Available',\n"
                        + "  `sequence_annotated` varchar(600) NOT NULL default 'Not Available',\n"
                        + "  `peptide_charge` int(255) NOT NULL default '-1',\n"
                        + "  PRIMARY KEY  (`index`)\n"
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8; ";
                st.executeUpdate(statment);

                st = conn.createStatement();

                statment = "CREATE TABLE IF NOT EXISTS `quantitative_proteins_table` (\n"
                        + "  `index` int(255) NOT NULL default '0',\n"
                        + "  `ds_ID` int(255) NOT NULL default '-1',\n"
                        + "  `uniprot_accession` varchar(150) NOT NULL default 'Not Available',\n"
                        + "  `uniprot_protein_name` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `publication_acc_number` varchar(150) NOT NULL default 'Not Available',\n"
                        + "  `publication_protein_name` varchar(700) NOT NULL default 'Not Available',\n"
                        + "  `quantified_peptides_number` int(255) NOT NULL default '-1',\n"
                        + "  `identified_peptides_number` int(255) NOT NULL default '-1',\n"
                        + "  `fold_change` varchar(20) NOT NULL default 'Not Available',\n"
                        + "  `sequance` text,\n"
                        + "  `fc_value` double NOT NULL default '-1000000000',\n"
                        + "  `roc_auc` double NOT NULL default '-1000000000',\n"
                        + "  `string_p_value` varchar(100) NOT NULL default 'Not Available',\n"
                        + "  `p_value` double NOT NULL default '-1000000000',\n"
                        + "  `p_value_comments` varchar(500) NOT NULL default 'Not Available',\n"
                        + "  `additional_comments` varchar(1000) NOT NULL default 'Not Available',\n"
                        + "  `quant_bases_comments` varchar(1000) NOT NULL default 'Not Available',\n"
                        + "  `log_2_FC` double NOT NULL default '-1000000000',\n"
                        + "  `pvalue_significance_threshold` varchar(500) NOT NULL default 'Not Available',\n"
                        + "  PRIMARY KEY  (`index`)\n"
                        + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8; ";
                st.executeUpdate(statment);
                st = conn.createStatement();
                statment = "CREATE TABLE IF NOT EXISTS `standard_plot_proteins` ( "
                        + "  `exp_id` int(11) NOT NULL, "
                        + "  `mw_(kDa)` double NOT NULL, "
                        + "  `name` varchar(30) NOT NULL, "
                        + "  `lower` int(11) NOT NULL, "
                        + "  `upper` int(11) NOT NULL, "
                        + "  `color` varchar(30) NOT NULL "
                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf8; ";
                st.executeUpdate(statment);
                st = conn.createStatement();
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
//                String datasets_table = "CREATE TABLE IF NOT EXISTS `experiments_table` ( " + "  `exp_id` int(11) NOT NULL auto_increment,\n" + "  `name` varchar(100) NOT NULL,\n" + "  `fractions_number` int(11) NOT NULL default '0',\n" + "  `ready` int(11) NOT NULL default '0',\n" + "  `uploaded_by` varchar(100) NOT NULL,\n" + "  `peptide_file` int(2) NOT NULL default '0',\n"
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
////                        + "  `identified_proteins_number` int(255) NOT NULL default '0',\n"
////                        + "  `quantified_proteins_number` int(255) NOT NULL default '0',\n"
////                        + "  `disease_group` varchar(300) NOT NULL default 'Not Available',\n"
////                        + "  `raw_data_available` varchar(500) NOT NULL default 'Not Available',\n"
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
//                        + "  `identified_proteins_number` int(255) NOT NULL default '0',\n"
//                        + "  `quantified_proteins_number` int(255) NOT NULL default '0',\n"
//                        + "  `disease_group` varchar(300) NOT NULL default 'Not Available',\n"
//                        + "  `raw_data_available` varchar(500) NOT NULL default 'Not Available',\n"
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
                s.printStackTrace();
                System.err.println(s.getLocalizedMessage());
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

    public int setupExperiment(ExperimentBean exp) {
        int id = 0;
        String insertExp = "INSERT INTO  `" + dbName + "`.`experiments_table` (`name`,`ready` ,`uploaded_by`,`species`,`sample_type`,`sample_processing`,`instrument_type`,`frag_mode`,`proteins_number` ,`email` ,`pblication_link`,`description`,`fraction_range`,`peptide_file`,`fractions_number`,`peptides_number`,`exp_type`,`valid_prot`)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ;";
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName, userName, password);
            PreparedStatement insertExpStat = conn.prepareStatement(insertExp, Statement.RETURN_GENERATED_KEYS);
            insertExpStat.setString(1, exp.getName().toUpperCase());
            insertExpStat.setInt(2, 2);
            insertExpStat.setString(3, exp.getUploadedByName().toUpperCase());
            insertExpStat.setString(4, exp.getSpecies());
            insertExpStat.setString(5, exp.getSampleType());
            insertExpStat.setString(6, exp.getSampleProcessing());
            insertExpStat.setString(7, exp.getInstrumentType());
            insertExpStat.setString(8, exp.getFragMode());
            insertExpStat.setInt(9, exp.getProteinsNumber());
            insertExpStat.setString(10, exp.getEmail().toUpperCase());
            if (exp.getPublicationLink() != null) {
                insertExpStat.setString(11, exp.getPublicationLink());
            } else {
                insertExpStat.setString(11, "NOT AVAILABLE");
            }
            insertExpStat.setString(12, exp.getDescription());
            insertExpStat.setInt(13, 1);
            insertExpStat.setInt(14, exp.getPeptidesInclude());
            insertExpStat.setInt(15, exp.getFractionsNumber());
            insertExpStat.setInt(16, exp.getPeptidesNumber());
            insertExpStat.setInt(17, exp.getExpType());
            insertExpStat.setInt(18, exp.getNumberValidProt());
            insertExpStat.executeUpdate();
            ResultSet rs = insertExpStat.getGeneratedKeys();
            while (rs.next()) {
                id = rs.getInt(1);
            }
            insertExpStat.clearParameters();
            insertExpStat.close();
            rs.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return id;
    }

    public int insertProteinExper(int expId, ProteinBean pb, String key) {

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String insertProtExp = "INSERT INTO  `" + dbName + "`.`experiment_protein_table` (`exp_id` ,`prot_accession` ,`other_protein(s)` ,`protein_inference_class` ,`sequence_coverage(%)` ,`observable_coverage(%)` ,`confident_ptm_sites` ,`number_confident` ,`other_ptm_sites` ,`number_other` ,`number_validated_peptides` ,`number_validated_spectra` ,`em_pai` ,`nsaf` ,`mw_(kDa)` ,`score` ,`confidence` ,`starred`,`non_enzymatic_peptides`,`gene_name`,`chromosome_number`,`prot_key`,`valid`,`description`)VALUES (?,?,?,?,?,  ?, ?, ?, ?,  ?,  ?, ?, ?, ?, ?,  ?, ?,?,?,?,?,?,?,?);";
            PreparedStatement insertProtStat = conn.prepareStatement(insertProtExp, Statement.RETURN_GENERATED_KEYS);
            insertProtStat.setInt(1, expId);
            insertProtStat.setString(2, pb.getAccession().toUpperCase());
            insertProtStat.setString(3, pb.getOtherProteins().toUpperCase());
            insertProtStat.setString(4, pb.getProteinInferenceClass().toUpperCase());
            insertProtStat.setDouble(5, pb.getSequenceCoverage());
            insertProtStat.setDouble(6, pb.getObservableCoverage());
            insertProtStat.setString(7, pb.getConfidentPtmSites().toUpperCase());// `confidence` ,`starred`
            insertProtStat.setString(8, pb.getNumberConfident());
            insertProtStat.setString(9, pb.getOtherPtmSites().toUpperCase());
            insertProtStat.setString(10, pb.getNumberOfOther().toUpperCase());
            insertProtStat.setInt(11, pb.getNumberValidatedPeptides());
            insertProtStat.setInt(12, pb.getNumberValidatedSpectra());
            insertProtStat.setDouble(13, pb.getEmPai());
            insertProtStat.setDouble(14, pb.getNsaf());
            insertProtStat.setDouble(15, pb.getMw_kDa());
            insertProtStat.setDouble(16, pb.getScore());
            insertProtStat.setDouble(17, pb.getConfidence());
            insertProtStat.setString(18, String.valueOf(pb.isStarred()));
            insertProtStat.setString(19, (String.valueOf(pb.isNonEnzymaticPeptides()).toUpperCase()));

            if (pb.getGeneName() == null) {
                insertProtStat.setString(20, "");
            } else {
                insertProtStat.setString(20, pb.getGeneName().toUpperCase());
            }
            if (pb.getChromosomeNumber() == null) {
                insertProtStat.setString(21, "");
            } else {
                insertProtStat.setString(21, pb.getChromosomeNumber().toUpperCase());
            }
            insertProtStat.setString(22, (String.valueOf(key).toUpperCase()));
            insertProtStat.setString(23, (pb.isValidated() + "").toUpperCase());
            insertProtStat.setString(24, pb.getDescription().toUpperCase());

            int test = insertProtStat.executeUpdate();
            insertProtStat.close();
            return test;
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            return -1;
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getLocalizedMessage());
            return -1;
        } catch (InstantiationException ex) {
            System.out.println(ex.getLocalizedMessage());
            return -1;
        } catch (IllegalAccessException ex) {
            System.out.println(ex.getLocalizedMessage());
            return -1;
        }
    }

    @SuppressWarnings("SleepWhileInLoop")
    public boolean updatePeptideFile(ExperimentBean exp) {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            int counter = 0;
            for (PeptideBean pepb : exp.getPeptideList().values()) {
                if (pepb.getDecoy() == 1) {
                    continue;
                }
                insertPeptide(-1, pepb, exp.getExpId(), conn);
                counter++;
                if (counter == 500) {
                    conn.close();
                    System.gc();
                    Thread.sleep(100);
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                    counter = 0;

                }
            }

        } catch (ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        } catch (IllegalAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        } catch (InstantiationException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    @SuppressWarnings("null")
    public int insertPeptide(int pepId, PeptideBean pepb, int expId, Connection conn) {
        String insertPeptide = "INSERT INTO  `" + dbName + "`.`proteins_peptides_table` (`protein` ,`other_protein(s)` ,`peptide_protein(s)` ,`other_protein_description(s)` ,`peptide_proteins_description(s)` ,`aa_before` ,`sequence` ,"
                + "`aa_after` ,`peptide_start` ,`peptide_end` ,`variable_modification` ,`location_confidence` ,`precursor_charge(s)` ,`number_of_validated_spectra` ,`score` ,`confidence` ,`peptide_id`,`fixed_modification`,`protein_inference`,`sequence_tagged`,`enzymatic`,`validated`,`starred`,`glycopattern_position(s)`,`deamidation_and_glycopattern`,`likelyNotGlycosite`,`exp_id` )VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,? , ? , ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        if (pepId == -1)//generate peptide id
        {
            String insertPeptideExp = "INSERT INTO  `" + dbName + "`.`experiment_peptides_table` (`exp_id`) VALUES (?) ;";
            try {
                if (conn == null || conn.isClosed()) {
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url + dbName, userName, password);
                }
                PreparedStatement insertPeptExpStat = conn.prepareStatement(insertPeptideExp, Statement.RETURN_GENERATED_KEYS);
                insertPeptExpStat.setInt(1, expId);
                insertPeptExpStat.executeUpdate();
                ResultSet rs = insertPeptExpStat.getGeneratedKeys();
                while (rs.next()) {
                    pepId = rs.getInt(1);
                }
                rs.close();
                insertPeptExpStat.clearParameters();
                insertPeptExpStat.close();

            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        int test = -1;
        PreparedStatement insertPeptideStat = null;
        String pepKey = ",";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            insertPeptideStat = conn.prepareStatement(insertPeptide, Statement.RETURN_GENERATED_KEYS);
            insertPeptideStat.setString(1, pepb.getProtein().toUpperCase());
            insertPeptideStat.setString(2, pepb.getOtherProteins().toUpperCase());
            insertPeptideStat.setString(3, pepb.getPeptideProteins().toUpperCase());
            insertPeptideStat.setString(4, pepb.getOtherProteinDescriptions().toUpperCase());
            insertPeptideStat.setString(5, pepb.getPeptideProteinsDescriptions().toUpperCase());
            insertPeptideStat.setString(6, pepb.getAaBefore().toUpperCase());
            insertPeptideStat.setString(7, pepb.getSequence().toUpperCase());
            insertPeptideStat.setString(8, pepb.getAaAfter().toUpperCase());
            insertPeptideStat.setString(9, pepb.getPeptideStart().toUpperCase());
            insertPeptideStat.setString(10, pepb.getPeptideEnd().toUpperCase());
            insertPeptideStat.setString(11, pepb.getVariableModification().toUpperCase());
            insertPeptideStat.setString(12, pepb.getLocationConfidence().toUpperCase());
            insertPeptideStat.setString(13, pepb.getPrecursorCharges().toUpperCase());
            insertPeptideStat.setInt(14, pepb.getNumberOfValidatedSpectra());
            insertPeptideStat.setDouble(15, pepb.getScore());
            insertPeptideStat.setDouble(16, pepb.getConfidence());
            insertPeptideStat.setInt(17, pepId);
            insertPeptideStat.setString(18, pepb.getFixedModification().toUpperCase());
            insertPeptideStat.setString(19, pepb.getProteinInference());
            insertPeptideStat.setString(20, pepb.getSequenceTagged());
            insertPeptideStat.setString(21, String.valueOf(pepb.isEnzymatic()).toUpperCase());
            insertPeptideStat.setDouble(22, pepb.getValidated());
            insertPeptideStat.setString(23, String.valueOf(pepb.isStarred()).toUpperCase());
            if (pepb.getGlycopatternPositions() != null) {
                insertPeptideStat.setString(24, pepb.getGlycopatternPositions());
            } else {
                insertPeptideStat.setString(24, null);
            }
            if (pepb.isDeamidationAndGlycopattern() != null && pepb.isDeamidationAndGlycopattern()) {
                insertPeptideStat.setString(25, String.valueOf(pepb.isDeamidationAndGlycopattern()).toUpperCase());
            } else {
                insertPeptideStat.setString(25, "FALSE");
            }
            if (pepb.isLikelyNotGlycopeptide() != null && pepb.isLikelyNotGlycopeptide()) {
                insertPeptideStat.setString(26, String.valueOf(pepb.isLikelyNotGlycopeptide()).toUpperCase());
            } else {
                insertPeptideStat.setString(26, "FALSE");
            }
            insertPeptideStat.setInt(27, expId);

            test = insertPeptideStat.executeUpdate();

            insertPeptideStat.clearParameters();
            insertPeptideStat.close();

            if (!pepb.getProtein().equalsIgnoreCase("shared peptide")) {
                pepKey = pepKey + pepb.getProtein().toUpperCase();
            }
            if (pepb.getOtherProteins() != null && !pepb.getOtherProteins().equals("") && !pepKey.endsWith(",")) {
                pepKey = pepKey + "," + pepb.getOtherProteins().toUpperCase();
            } else if (pepb.getOtherProteins() != null && !pepb.getOtherProteins().equals("") && pepKey.endsWith(",")) {
                pepKey = pepKey + pepb.getOtherProteins().toUpperCase();
            }
            if (pepb.getPeptideProteins() != null && !pepb.getPeptideProteins().equals("") && !pepKey.endsWith(",")) {
                pepKey = pepKey + "," + pepb.getPeptideProteins().toUpperCase();
            } else if (pepb.getPeptideProteins() != null && !pepb.getPeptideProteins().equals("") && pepKey.endsWith(",")) {
                pepKey = pepKey + pepb.getPeptideProteins().toUpperCase();
            }
            if (!pepKey.endsWith(",")) {
                pepKey = pepKey + ",";
            }

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            try {
                insertPeptideStat.close();
            } catch (SQLException e1) {
                System.out.println(e1.getLocalizedMessage());
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        insertExpProtPept(expId, pepId, pepKey, conn);

        return test;
    }

    public int insertExpProtPept(int expId, int pepId, String pepKey, Connection conn) {
        int test = -1;

        try {

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            String insertExpProtPeptQ = "INSERT INTO  `" + dbName + "`.`experiment_peptides_proteins_table` (`exp_id` ,`peptide_id`,`protein`)VALUES (?,?,?);";
            PreparedStatement insertExpProtPeptQStat = conn.prepareStatement(insertExpProtPeptQ);
            insertExpProtPeptQStat.setInt(1, expId);
            insertExpProtPeptQStat.setInt(2, pepId);
            insertExpProtPeptQStat.setString(3, pepKey.toUpperCase());
            test = insertExpProtPeptQStat.executeUpdate();
            insertExpProtPeptQStat.close();

//            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return test;
    }

    public boolean insertFractions(ExperimentBean exp) {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String insertFractExp = "INSERT INTO  `" + dbName + "`.`experiment_fractions_table` (`exp_id`,`min_range` ,`max_range`,`index`) VALUES (?,?,?,?) ;";
            PreparedStatement insertFractExpStat;//conn.prepareStatement(insertFractExp, Statement.RETURN_GENERATED_KEYS);
            int fractId = 0;

            for (FractionBean fb : exp.getFractionsList().values()) {
                insertFractExpStat = conn.prepareStatement(insertFractExp, Statement.RETURN_GENERATED_KEYS);
                insertFractExpStat.setInt(1, exp.getExpId());
                insertFractExpStat.setDouble(2, 0);
                insertFractExpStat.setDouble(3, 0);
                insertFractExpStat.setInt(4, fb.getFractionIndex());
                insertFractExpStat.executeUpdate();
                ResultSet rs = insertFractExpStat.getGeneratedKeys();
                while (rs.next()) {
                    fractId = rs.getInt(1);
                }
                rs.close();
                for (ProteinBean pb : fb.getProteinList().values()) {
                    this.insertProteinFract(fractId, pb);
                }
            }
        } catch (SQLException exc) {
            System.out.println(exc.getLocalizedMessage());
            return false;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    private int insertProteinFract(int fractId, ProteinBean fpb) {
        int test;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String insertProtFract = "INSERT INTO  `" + dbName + "`.`fractions_table` (`fraction_id` ,`prot_accession` ,`number_peptides` ,`number_spectra` ,`average_ precursor_intensity`)VALUES (?, ?,  ?,  ?,  ?);";
            PreparedStatement insertProtFracStat = conn.prepareStatement(insertProtFract, Statement.RETURN_GENERATED_KEYS);
            insertProtFracStat.setInt(1, fractId);
            insertProtFracStat.setString(2, fpb.getAccession().toUpperCase() + "," + fpb.getOtherProteins().toUpperCase());
            insertProtFracStat.setInt(3, fpb.getNumberOfPeptidePerFraction());
            insertProtFracStat.setInt(4, fpb.getNumberOfSpectraPerFraction());
            insertProtFracStat.setDouble(5, fpb.getAveragePrecursorIntensityPerFraction());
            test = insertProtFracStat.executeUpdate();
            insertProtFracStat.close();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
            return -1;
        } catch (IllegalAccessException e) {
            System.out.println(e.getLocalizedMessage());
            return -1;
        } catch (InstantiationException e) {
            System.out.println(e.getLocalizedMessage());
            return -1;
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            return -1;
        }
        return test;

    }

    public boolean checkName(String name) throws SQLException {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String selectName = "SELECT `name` FROM `experiments_table` where `name`=?;";
            PreparedStatement selectNameStat = conn.prepareStatement(selectName);
            selectNameStat.setString(1, name.toUpperCase());
            ResultSet rs = selectNameStat.executeQuery();
            while (rs.next()) {
                return false;
            }

            selectNameStat.close();
            rs.close();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
            throw new SQLException();
        } catch (IllegalAccessException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return true;
    }

    public boolean exportDataBase(String mysqldumpUrl, String sqlFileUrl) {

        try {
//            String executeCmd = "C:\\\\AppServ\\\\MySQL\\\\bin\\\\mysqldump.exe  -u " + userName + " -p" + password + " " + dbName + " -r  C:\\CSF_Files\\backup-quant.sql";//C:\\AppServ\\MySQL\\bin\\mysqldump.exe           ///usr/bin/mysqldump

            String executeCmd = mysqldumpUrl + "  -u " + userName + " -p" + password + " " + dbName + " -r  " + sqlFileUrl;//C:\\AppServ\\MySQL\\bin\\mysqldump.exe           ///usr/bin/mysqldump

            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();
            if (processComplete == 0) {

                System.out.println("Backup taken successfully ");
                return true;

            } else {

                System.out.println("Could not take mysql backup");
                return false;

            }

        } catch (IOException e) {
        } catch (InterruptedException e) {
        }
        return false;

    }

    private void createUpdatedTables() {
        String statment = "-- phpMyAdmin SQL Dump\n"
                + "-- version 2.10.3\n"
                + "-- http://www.phpmyadmin.net\n"
                + "-- \n"
                + "-- Host: localhost\n"
                + "-- Generation Time: Aug 18, 2015 at 04:04 PM\n"
                + "-- Server version: 5.0.51\n"
                + "-- PHP Version: 5.2.6\n"
                + "\n"
                + "SET SQL_MODE=\"NO_AUTO_VALUE_ON_ZERO\";\n"
                + "\n"
                + "-- \n"
                //                + "-- Database: `csf_db_v2_1`\n"
                + "-- \n"
                + "\n"
                + "-- --------------------------------------------------------\n"
                + "\n"
                + "-- \n"
                + "-- Table structure for table `quant_dataset_table`\n"
                + "-- \n"
                + "-- Creation: Jun 25, 2015 at 01:07 PM\n"
                + "-- Last update: Jun 25, 2015 at 01:07 PM\n"
                + "-- \n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS `quant_dataset_table` (\n"
                + "  `additional_comments` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `pumed_id` varchar(30) NOT NULL default 'Not Available',\n"
                + "  `files_num` int(255) NOT NULL default '-1',\n"
                + "  `identified_proteins_number` int(255) NOT NULL default '-1',\n"
                + "  `quantified_proteins_number` int(255) NOT NULL default '-1',\n"
                + "  `raw_data_available` varchar(500) NOT NULL default 'Raw Data Not Available',\n"
                + "  `year` int(4) NOT NULL default '0',\n"
                + "  `index` int(255) NOT NULL auto_increment,\n"
                + "  `type_of_study` varchar(200) NOT NULL default 'Not Available',\n"
                + "  `sample_type` varchar(200) NOT NULL default 'Not Available',\n"
                + "  `sample_matching` varchar(300) NOT NULL default 'Not Available',\n"
                + "  `technology` varchar(300) NOT NULL default 'Not Available',\n"
                + "  `analytical_approach` varchar(300) NOT NULL default 'Not Available',\n"
                + "  `enzyme` varchar(300) NOT NULL default 'Not Available',\n"
                + "  `shotgun_targeted` varchar(200) NOT NULL default 'Not Available',\n"
                + "  `quantification_basis` varchar(200) NOT NULL default 'Not Available',\n"
                + "  `quant_basis_comment` varchar(500) NOT NULL default 'Not Available',\n"
                + "  `patients_group_i_number` int(255) NOT NULL default '-1000000000',\n"
                + "  `patients_group_ii_number` int(255) NOT NULL default '-1000000000',\n"
                + "  `normalization_strategy` varchar(600) NOT NULL default 'Not Available',\n"
                + "  `author` varchar(300) NOT NULL default 'John Smith',\n"
                + "  `patient_group_i` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_gr_i_comment` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_sub_group_i` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_group_ii` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_sub_group_ii` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_gr_ii_comment` varchar(700) NOT NULL default 'Not Available',\n"
                + "  KEY `index` (`index`)\n"
                + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;\n"
                + "\n"
                + "-- --------------------------------------------------------\n"
                + "\n"
                + "-- \n"
                + "-- Table structure for table `quant_full_table`\n"
                + "-- \n"
                + "-- Creation: Jun 25, 2015 at 01:07 PM\n"
                + "-- Last update: Jun 25, 2015 at 01:07 PM\n"
                + "-- \n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS `quant_full_table` (\n"
                + "  `author` varchar(500) NOT NULL,\n"
                + "  `year` int(255) NOT NULL,\n"
                + "  `pumed_id` varchar(150) NOT NULL default 'Not Available',\n"
                + "  `uniprot_accession` varchar(150) NOT NULL default 'Not Available',\n"
                + "  `uniprot_protein_name` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `publication_acc_number` varchar(150) NOT NULL default 'Not Available',\n"
                + "  `publication_protein_name` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `raw_data_available` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `type_of_study` varchar(150) NOT NULL default 'Not Available',\n"
                + "  `sample_type` varchar(150) NOT NULL default 'Not Available',\n"
                + "  `patient_group_i` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_sub_group_i` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_gr_i_comment` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_group_ii` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_sub_group_ii` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_gr_ii_comment` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `sample_matching` varchar(500) NOT NULL default 'Not Available',\n"
                + "  `normalization_strategy` varchar(500) NOT NULL default 'Not Available',\n"
                + "  `technology` varchar(500) NOT NULL default 'Not Available',\n"
                + "  `analytical_approach` varchar(500) NOT NULL default 'Not Available',\n"
                + "  `enzyme` varchar(500) NOT NULL default 'Not Available',\n"
                + "  `shotgun_targeted` varchar(100) NOT NULL default 'Not Available',\n"
                + "  `quantification_basis` varchar(500) NOT NULL default 'Not Available',\n"
                + "  `quant_basis_comment` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `additional_comments` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `q_peptide_key` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `peptide_sequance` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `peptide_modification` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `modification_comment` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `string_fc_value` varchar(200) NOT NULL default 'Not Available',\n"
                + "  `string_p_value` varchar(200) NOT NULL default 'Not Available',\n"
                + "  `quantified_proteins_number` int(255) NOT NULL default '-1',\n"
                + "  `identified_proteins_number` int(255) NOT NULL default '-1',\n"
                + "  `peptideId_number` int(255) NOT NULL default '-1',\n"
                + "  `quantified_peptides_number` int(255) NOT NULL default '-1',\n"
                + "  `patients_group_i_number` int(255) NOT NULL default '-1',\n"
                + "  `patients_group_ii_number` int(255) NOT NULL default '-1',\n"
                + "  `p_value` double NOT NULL default '-1000000000',\n"
                + "  `roc_auc` double NOT NULL default '-1000000000',\n"
                + "  `fc_value` double NOT NULL default '-1000000000',\n"
                + "  `peptide_prot` varchar(5) NOT NULL default 'False',\n"
                + "  `index` int(255) NOT NULL auto_increment,\n"
                + "  PRIMARY KEY  (`index`)\n"
                + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;\n"
                + "\n"
                + "-- --------------------------------------------------------\n"
                + "\n"
                + "-- \n"
                + "-- Table structure for table `experiments_table`\n"
                + "-- \n"
                + "-- Creation: Jun 25, 2015 at 01:08 PM\n"
                + "-- Last update: Jun 25, 2015 at 01:08 PM\n"
                + "-- \n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS `experiments_table` (\n"
                + "  `exp_id` int(11) NOT NULL auto_increment,\n"
                + "  `fraction_range` int(2) NOT NULL default '0',\n"
                + "  `name` varchar(100) NOT NULL,\n"
                + "  `fractions_number` int(11) NOT NULL default '0',\n"
                + "  `ready` int(11) NOT NULL default '0',\n"
                + "  `uploaded_by` varchar(100) NOT NULL,\n"
                + "  `peptide_file` int(2) NOT NULL default '0',\n"
                + "  `species` varchar(100) NOT NULL,\n"
                + "  `sample_type` varchar(100) NOT NULL,\n"
                + "  `sample_processing` varchar(100) NOT NULL,\n"
                + "  `instrument_type` varchar(100) NOT NULL,\n"
                + "  `frag_mode` varchar(100) NOT NULL,\n"
                + "  `proteins_number` int(11) NOT NULL default '0',\n"
                + "  `peptides_number` int(11) NOT NULL default '0',\n"
                + "  `email` varchar(100) NOT NULL,\n"
                + "  `pblication_link` varchar(300) NOT NULL default 'NOT AVAILABLE',\n"
                + "  `description` varchar(1000) NOT NULL default 'NO DESCRIPTION AVAILABLE',\n"
                + "  `exp_type` int(10) NOT NULL default '0',\n"
                + "  `valid_prot` int(11) NOT NULL default '0',\n"
                + "  PRIMARY KEY  (`exp_id`)\n"
                + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;\n"
                + "\n"
                + "-- --------------------------------------------------------\n"
                + "\n"
                + "-- \n"
                + "-- Table structure for table `experiment_fractions_table`\n"
                + "-- \n"
                + "-- Creation: Jun 25, 2015 at 01:07 PM\n"
                + "-- Last update: Jun 25, 2015 at 01:07 PM\n"
                + "-- Last check: Jun 25, 2015 at 01:07 PM\n"
                + "-- \n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS `experiment_fractions_table` (\n"
                + "  `exp_id` int(11) NOT NULL,\n"
                + "  `fraction_id` int(11) NOT NULL auto_increment,\n"
                + "  `min_range` double NOT NULL default '0',\n"
                + "  `max_range` double NOT NULL default '0',\n"
                + "  `index` int(11) NOT NULL default '0',\n"
                + "  PRIMARY KEY  (`fraction_id`),\n"
                + "  KEY `exp_id` (`exp_id`)\n"
                + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;\n"
                + "\n"
                + "-- --------------------------------------------------------\n"
                + "\n"
                + "-- \n"
                + "-- Table structure for table `experiment_peptides_proteins_table`\n"
                + "-- \n"
                + "-- Creation: Jun 25, 2015 at 01:07 PM\n"
                + "-- Last update: Jun 25, 2015 at 01:07 PM\n"
                + "-- \n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS `experiment_peptides_proteins_table` (\n"
                + "  `exp_id` varchar(50) NOT NULL,\n"
                + "  `peptide_id` int(50) NOT NULL,\n"
                + "  `protein` varchar(1000) NOT NULL\n"
                + ") ENGINE=MyISAM DEFAULT CHARSET=utf8;\n"
                + "\n"
                + "-- --------------------------------------------------------\n"
                + "\n"
                + "-- \n"
                + "-- Table structure for table `experiment_peptides_table`\n"
                + "-- \n"
                + "-- Creation: Jun 25, 2015 at 01:07 PM\n"
                + "-- Last update: Jun 25, 2015 at 01:08 PM\n"
                + "-- Last check: Jun 25, 2015 at 01:08 PM\n"
                + "-- \n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS `experiment_peptides_table` (\n"
                + "  `exp_id` int(11) NOT NULL default '0',\n"
                + "  `pep_id` int(11) NOT NULL auto_increment,\n"
                + "  PRIMARY KEY  (`pep_id`),\n"
                + "  KEY `exp_id` (`exp_id`)\n"
                + ") ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;\n"
                + "\n"
                + "-- --------------------------------------------------------\n"
                + "\n"
                + "-- \n"
                + "-- Table structure for table `experiment_protein_table`\n"
                + "-- \n"
                + "-- Creation: Jun 25, 2015 at 01:08 PM\n"
                + "-- Last update: Jun 25, 2015 at 01:08 PM\n"
                + "-- Last check: Jun 25, 2015 at 01:08 PM\n"
                + "-- \n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS `experiment_protein_table` (\n"
                + "  `exp_id` int(11) NOT NULL,\n"
                + "  `prot_accession` varchar(30) NOT NULL,\n"
                + "  `other_protein(s)` varchar(1000) default NULL,\n"
                + "  `protein_inference_class` varchar(100) default NULL,\n"
                + "  `sequence_coverage(%)` double default NULL,\n"
                + "  `observable_coverage(%)` double default NULL,\n"
                + "  `confident_ptm_sites` varchar(500) default NULL,\n"
                + "  `number_confident` varchar(500) default NULL,\n"
                + "  `other_ptm_sites` varchar(500) default NULL,\n"
                + "  `number_other` varchar(500) default NULL,\n"
                + "  `number_validated_peptides` int(11) default NULL,\n"
                + "  `number_validated_spectra` int(11) default NULL,\n"
                + "  `em_pai` double default NULL,\n"
                + "  `nsaf` double default NULL,\n"
                + "  `mw_(kDa)` double default NULL,\n"
                + "  `score` double default NULL,\n"
                + "  `confidence` double default NULL,\n"
                + "  `starred` varchar(5) default NULL,\n"
                + "  `peptide_fraction_spread_lower_range_kDa` varchar(10) default NULL,\n"
                + "  `peptide_fraction_spread_upper_range_kDa` varchar(10) default NULL,\n"
                + "  `spectrum_fraction_spread_lower_range_kDa` varchar(10) default NULL,\n"
                + "  `spectrum_fraction_spread_upper_range_kDa` varchar(10) default NULL,\n"
                + "  `non_enzymatic_peptides` varchar(5) NOT NULL,\n"
                + "  `gene_name` varchar(50) NOT NULL default 'Not Available',\n"
                + "  `chromosome_number` varchar(20) NOT NULL default '',\n"
                + "  `prot_key` varchar(500) NOT NULL,\n"
                + "  `valid` varchar(7) NOT NULL default 'false',\n"
                + "  `description` varchar(500) NOT NULL,\n"
                + "  `prot_group_id` int(255) NOT NULL auto_increment,\n"
                + "  PRIMARY KEY  (`prot_group_id`),\n"
                + "  KEY `exp_id` (`exp_id`),\n"
                + "  KEY `prot_key` (`prot_key`(333))\n"
                + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;\n"
                + "\n"
                + "-- --------------------------------------------------------\n"
                + "\n"
                + "-- \n"
                + "-- Table structure for table `fractions_table`\n"
                + "-- \n"
                + "-- Creation: Jun 25, 2015 at 01:08 PM\n"
                + "-- Last update: Jun 25, 2015 at 01:08 PM\n"
                + "-- Last check: Jun 25, 2015 at 01:08 PM\n"
                + "-- \n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS `fractions_table` (\n"
                + "  `fraction_id` int(11) NOT NULL,\n"
                + "  `prot_accession` varchar(500) NOT NULL,\n"
                + "  `number_peptides` int(11) NOT NULL default '0',\n"
                + "  `peptide_fraction_spread_lower_range_kDa` varchar(10) default NULL,\n"
                + "  `peptide_fraction_spread_upper_range_kDa` varchar(10) default NULL,\n"
                + "  `spectrum_fraction_spread_lower_range_kDa` varchar(10) default NULL,\n"
                + "  `spectrum_fraction_spread_upper_range_kDa` varchar(10) default NULL,\n"
                + "  `number_spectra` int(11) NOT NULL default '0',\n"
                + "  `average_ precursor_intensity` double default NULL,\n"
                + "  `exp_id` int(255) NOT NULL default '0',\n"
                + "  KEY `prot_accession` (`prot_accession`(333)),\n"
                + "  KEY `fraction_id` (`fraction_id`)\n"
                + ") ENGINE=MyISAM DEFAULT CHARSET=utf8;\n"
                + "\n"
                + "-- --------------------------------------------------------\n"
                + "\n"
                + "-- \n"
                + "-- Table structure for table `proteins_peptides_table`\n"
                + "-- \n"
                + "-- Creation: Jun 25, 2015 at 01:08 PM\n"
                + "-- Last update: Jun 25, 2015 at 01:09 PM\n"
                + "-- Last check: Jun 25, 2015 at 01:09 PM\n"
                + "-- \n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS `proteins_peptides_table` (\n"
                + "  `protein` varchar(70) default NULL,\n"
                + "  `other_protein(s)` text,\n"
                + "  `peptide_protein(s)` text,\n"
                + "  `other_protein_description(s)` text,\n"
                + "  `peptide_proteins_description(s)` text,\n"
                + "  `aa_before` varchar(2000) default NULL,\n"
                + "  `sequence` varchar(300) default NULL,\n"
                + "  `aa_after` varchar(2000) default NULL,\n"
                + "  `peptide_start` text,\n"
                + "  `peptide_end` text,\n"
                + "  `variable_modification` varchar(500) default NULL,\n"
                + "  `location_confidence` varchar(500) default NULL,\n"
                + "  `precursor_charge(s)` varchar(70) default NULL,\n"
                + "  `number_of_validated_spectra` int(20) default NULL,\n"
                + "  `score` double NOT NULL default '0',\n"
                + "  `confidence` double NOT NULL default '0',\n"
                + "  `peptide_id` int(50) NOT NULL default '0',\n"
                + "  `fixed_modification` varchar(100) default NULL,\n"
                + "  `protein_inference` varchar(500) default NULL,\n"
                + "  `sequence_tagged` varchar(500) default NULL,\n"
                + "  `enzymatic` varchar(5) default NULL,\n"
                + "  `validated` double default NULL,\n"
                + "  `starred` varchar(5) default NULL,\n"
                + "  `glycopattern_position(s)` varchar(100) default NULL,\n"
                + "  `deamidation_and_glycopattern` varchar(5) default NULL,\n"
                + "  `exp_id` int(250) NOT NULL default '0',\n"
                + "  `likelyNotGlycosite` varchar(5) NOT NULL default 'FALSE',\n"
                + "  KEY `peptide_id` (`peptide_id`)\n"
                + ") ENGINE=MyISAM DEFAULT CHARSET=utf8;\n"
                + "\n"
                + "-- --------------------------------------------------------\n"
                + "\n"
                + "-- \n"
                + "-- Table structure for table `quantitative_peptides_table`\n"
                + "-- \n"
                + "-- Creation: Jul 09, 2015 at 05:20 PM\n"
                + "-- Last update: Jul 10, 2015 at 12:53 PM\n"
                + "-- \n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS `quantitative_peptides_table` (\n"
                + "  `index` int(255) NOT NULL auto_increment,\n"
                + "  `prot_index` int(255) NOT NULL default '-1',\n"
                + "  `peptide_sequance` varchar(600) NOT NULL default 'Not Available',\n"
                + "  `peptide_modification` varchar(600) NOT NULL default 'Not Available',\n"
                + "  `modification_comment` varchar(600) NOT NULL default 'Not Available',\n"
                + "  `string_fc_value` varchar(200) NOT NULL default 'Not Provided',\n"
                + "  `string_p_value` varchar(200) NOT NULL default 'Not Available',\n"
                + "  `p_value` double NOT NULL default '-1000000000',\n"
                + "  `roc_auc` double NOT NULL default '-1000000000',\n"
                + "  `fc_value` double NOT NULL default '-1000000000',\n"
                + "  `DsKey` int(255) NOT NULL default '-1',\n"
                + "  `p_value_comments` varchar(500) NOT NULL default 'Not Available',\n"
                + "  `quant_bases_comments` varchar(1000) NOT NULL default 'Not Available',\n"
                + "  `proteinAccession` varchar(50) NOT NULL default 'Not Available',\n"
                + "  KEY `index` (`index`)\n"
                + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;\n"
                + "\n"
                + "-- --------------------------------------------------------\n"
                + "\n"
                + "-- \n"
                + "-- Table structure for table `quantitative_proteins_table`\n"
                + "-- \n"
                + "-- Creation: Jul 01, 2015 at 01:46 PM\n"
                + "-- Last update: Jul 01, 2015 at 02:06 PM\n"
                + "-- \n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS `quantitative_proteins_table` (\n"
                + "  `index` int(255) NOT NULL auto_increment,\n"
                + "  `ds_ID` int(255) NOT NULL default '-1',\n"
                + "  `uniprot_accession` varchar(150) NOT NULL default 'Not Available',\n"
                + "  `uniprot_protein_name` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `publication_acc_number` varchar(150) NOT NULL default 'Not Available',\n"
                + "  `publication_protein_name` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `quantified_peptides_number` int(255) NOT NULL default '-1',\n"
                + "  `identified_peptides_number` int(255) NOT NULL default '-1',\n"
                + "  `fold_change` varchar(20) NOT NULL default 'Not Available',\n"
                + "  `sequance` text NOT NULL,\n"
                + "  `fc_value` double NOT NULL default '-1000000000',\n"
                + "  `roc_auc` double NOT NULL default '-1000000000',\n"
                + "  `string_p_value` varchar(100) NOT NULL default 'Not Available',\n"
                + "  `p_value` double NOT NULL default '-1000000000',\n"
                + "  `p_value_comments` varchar(500) NOT NULL default 'Not Available',\n"
                + "  KEY `index` (`index`)\n"
                + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;\n"
                + "\n"
                + "-- --------------------------------------------------------\n"
                + "\n"
                + "-- \n"
                + "-- Table structure for table `quant_dataset_table`\n"
                + "-- \n"
                + "-- Creation: Jun 25, 2015 at 01:09 PM\n"
                + "-- Last update: Jun 25, 2015 at 01:09 PM\n"
                + "-- \n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS `quant_dataset_table` (\n"
                + "  `additional_comments` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `pumed_id` varchar(30) NOT NULL,\n"
                + "  `files_num` int(255) NOT NULL default '-1',\n"
                + "  `identified_proteins_number` int(255) NOT NULL default '-1',\n"
                + "  `quantified_proteins_number` int(255) NOT NULL default '-1',\n"
                + "  `raw_data_available` varchar(500) NOT NULL default 'Raw Data Not Available',\n"
                + "  `year` int(4) NOT NULL default '0',\n"
                + "  `index` int(255) NOT NULL auto_increment,\n"
                + "  `type_of_study` varchar(200) NOT NULL default 'Not Available',\n"
                + "  `sample_type` varchar(200) NOT NULL default 'Not Available',\n"
                + "  `sample_matching` varchar(300) NOT NULL default 'Not Available',\n"
                + "  `technology` varchar(300) NOT NULL default 'Not Available',\n"
                + "  `analytical_approach` varchar(300) NOT NULL default 'Not Available',\n"
                + "  `enzyme` varchar(300) NOT NULL default 'Not Available',\n"
                + "  `shotgun_targeted` varchar(200) NOT NULL default 'Not Available',\n"
                + "  `quantification_basis` varchar(200) NOT NULL default 'Not Available',\n"
                + "  `quant_basis_comment` varchar(500) NOT NULL default 'Not Available',\n"
                + "  `patients_group_i_number` int(255) NOT NULL default '-1000000000',\n"
                + "  `patients_group_ii_number` int(255) NOT NULL default '-1000000000',\n"
                + "  `normalization_strategy` varchar(600) NOT NULL default 'Not Available',\n"
                + "  `author` varchar(300) NOT NULL default 'John Smith',\n"
                + "  `patient_group_i` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_gr_i_comment` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_sub_group_i` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_group_ii` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_sub_group_ii` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_gr_ii_comment` varchar(700) NOT NULL default 'Not Available',\n"
                + "  PRIMARY KEY  (`index`)\n"
                + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;\n"
                + "\n"
                + "-- --------------------------------------------------------\n"
                + "\n"
                + "-- \n"
                + "-- Table structure for table `quant_prot_table`\n"
                + "-- \n"
                + "-- Creation: Aug 18, 2015 at 03:14 PM\n"
                + "-- Last update: Aug 18, 2015 at 03:14 PM\n"
                + "-- \n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS `quant_prot_table` (\n"
                + "  `pumed_id` varchar(150) NOT NULL,\n"
                + "  `uniprot_accession` varchar(150) NOT NULL,\n"
                + "  `uniprot_protein_name` varchar(700) NOT NULL,\n"
                + "  `publication_acc_number` varchar(150) default 'Not Available',\n"
                + "  `publication_protein_name` varchar(700) default 'Not Available',\n"
                + "  `raw_data_available` varchar(700) default 'Not Available',\n"
                + "  `type_of_study` varchar(150) default 'Not Available',\n"
                + "  `sample_type` varchar(150) default 'Not Available',\n"
                + "  `patient_group_i` varchar(700) default 'Not Available',\n"
                + "  `patient_sub_group_i` varchar(700) default 'Not Available',\n"
                + "  `patient_gr_i_comment` varchar(700) default 'Not Available',\n"
                + "  `patient_group_ii` varchar(700) default 'Not Available',\n"
                + "  `patient_sub_group_ii` varchar(700) default 'Not Available',\n"
                + "  `patient_gr_ii_comment` varchar(700) default 'Not Available',\n"
                + "  `sample_matching` varchar(500) default 'Not Available',\n"
                + "  `normalization_strategy` varchar(500) default 'Not Available',\n"
                + "  `technology` varchar(500) default 'Not Available',\n"
                + "  `analytical_approach` varchar(500) default 'Not Available',\n"
                + "  `enzyme` varchar(500) default 'Not Available',\n"
                + "  `shotgun_targeted` varchar(100) default 'Not Available',\n"
                + "  `quantification_basis` varchar(500) default 'Not Available',\n"
                + "  `quant_basis_comment` varchar(700) default 'Not Available',\n"
                + "  `additional_comments` varchar(700) default 'Not Available',\n"
                + "  `q_peptide_key` varchar(700) default 'Not Available',\n"
                + "  `peptide_sequance` varchar(700) default 'Not Available',\n"
                + "  `peptide_modification` varchar(700) default 'Not Available',\n"
                + "  `modification_comment` varchar(700) default 'Not Available',\n"
                + "  `string_fc_value` varchar(200) default 'Not Available',\n"
                + "  `string_p_value` varchar(200) default 'Not Available',\n"
                + "  `quantified_proteins_number` int(255) default NULL,\n"
                + "  `peptideId_number` int(255) default NULL,\n"
                + "  `quantified_peptides_number` int(255) default NULL,\n"
                + "  `patients_group_i_number` int(255) default NULL,\n"
                + "  `patients_group_ii_number` int(255) default NULL,\n"
                + "  `p_value` double default NULL,\n"
                + "  `roc_auc` double default NULL,\n"
                + "  `fc_value` double default NULL,\n"
                + "  `peptide_prot` varchar(5) NOT NULL default 'False',\n"
                + "  `index` int(255) NOT NULL auto_increment,\n"
                + "  PRIMARY KEY  (`index`)\n"
                + ") ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;\n"
                + "\n"
                + "-- --------------------------------------------------------\n"
                + "\n"
                + "-- \n"
                + "-- Table structure for table `standard_plot_proteins`\n"
                + "-- \n"
                + "-- Creation: Jun 25, 2015 at 01:09 PM\n"
                + "-- Last update: Jun 25, 2015 at 01:09 PM\n"
                + "-- \n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS `standard_plot_proteins` (\n"
                + "  `exp_id` int(11) NOT NULL,\n"
                + "  `mw_(kDa)` double NOT NULL,\n"
                + "  `name` varchar(30) NOT NULL,\n"
                + "  `lower` int(11) NOT NULL,\n"
                + "  `upper` int(11) NOT NULL,\n"
                + "  `color` varchar(30) NOT NULL\n"
                + ") ENGINE=MyISAM DEFAULT CHARSET=utf8;\n"
                + "\n"
                + "-- --------------------------------------------------------\n"
                + "\n"
                + "-- \n"
                + "-- Table structure for table `studies_table`\n"
                + "-- \n"
                + "-- Creation: Jun 25, 2015 at 01:09 PM\n"
                + "-- Last update: Jun 25, 2015 at 01:09 PM\n"
                + "-- \n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS `studies_table` (\n"
                + "  `pumed_id` varchar(30) NOT NULL,\n"
                + "  `files_num` int(255) NOT NULL default '0',\n"
                + "  `identified_proteins_number` int(255) NOT NULL default '0',\n"
                + "  `quantified_proteins_number` int(255) NOT NULL default '0',\n"
                + "  `raw_data_available` varchar(500) NOT NULL default 'Not Available',\n"
                + "  `year` int(4) NOT NULL default '0',\n"
                + "  `index` int(255) NOT NULL auto_increment,\n"
                + "  `type_of_study` varchar(200) NOT NULL default 'Not Available',\n"
                + "  `sample_type` varchar(200) NOT NULL default 'Not Available',\n"
                + "  `sample_matching` varchar(300) NOT NULL default 'Not Available',\n"
                + "  `technology` varchar(300) NOT NULL default 'Not Available',\n"
                + "  `analytical_approach` varchar(300) NOT NULL default 'Not Available',\n"
                + "  `enzyme` varchar(300) NOT NULL default 'Not Available',\n"
                + "  `shotgun_targeted` varchar(200) NOT NULL default 'Not Available',\n"
                + "  `quantification_basis` varchar(200) NOT NULL default 'Not Available',\n"
                + "  `quant_basis_comment` varchar(500) NOT NULL default 'Not Available',\n"
                + "  `quantified_proteins_number` int(255) NOT NULL default '-1000000000',\n"
                + "  `patients_group_i_number` int(255) NOT NULL default '-1000000000',\n"
                + "  `patients_group_ii_number` int(255) NOT NULL default '-1000000000',\n"
                + "  `normalization_strategy` varchar(600) NOT NULL default 'Not Available',\n"
                + "  `author` varchar(300) NOT NULL default 'John Smith',\n"
                + "  `patient_group_i` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_gr_i_comment` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_sub_group_i` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_group_ii` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_sub_group_ii` varchar(700) NOT NULL default 'Not Available',\n"
                + "  `patient_gr_ii_comment` varchar(700) NOT NULL default 'Not Available',\n"
                + "  PRIMARY KEY  (`index`)\n"
                + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;\n"
                + "\n"
                + "-- --------------------------------------------------------\n"
                + "\n"
                + "-- \n"
                + "-- Table structure for table `users_table`\n"
                + "-- \n"
                + "-- Creation: Jun 25, 2015 at 01:09 PM\n"
                + "-- Last update: Jun 25, 2015 at 01:09 PM\n"
                + "-- \n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS `users_table` (\n"
                + "  `id` int(20) NOT NULL auto_increment,\n"
                + "  `password` varchar(100) NOT NULL,\n"
                + "  `admin` varchar(5) NOT NULL default 'FALSE',\n"
                + "  `user_name` varchar(20) NOT NULL,\n"
                + "  `email` varchar(100) NOT NULL,\n"
                + "  PRIMARY KEY  (`email`),\n"
                + "  KEY `id` (`id`)\n"
                + ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            Statement st = conn_ii.createStatement();
            //CREATE TABLE  `users_table`
            st.executeUpdate(statment);

        } catch (ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());

        }

    }

    @SuppressWarnings("CallToPrintStackTrace")
    public boolean restoreDB(String sqlFileUrl, String mysqldumpUrl) {
        //create if not exist

        try {
            createTables();
            if (conn == null || conn.isClosed()) {
                Class.forName(driver);
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            String[] executeCmd = new String[]{mysqldumpUrl, "--user=" + userName, "--password=" + password, dbName, "-e", "source " + sqlFileUrl};//  C:\\AppServ\\MySQL\\bin\\mysql
            System.out.println(" sqlMysqlPath  " + mysqldumpUrl + "  sqlFileUrl  " + sqlFileUrl);
            Process runtimeProcess;
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                System.out.println("Backup restored successfully");
                return true;
            } else {
                System.out.println("Could not restore the backup");
            }

        } catch (SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        return false;
    }

    //handel quant data
    @SuppressWarnings("CallToPrintStackTrace")
    public boolean storeCombinedQuantProtTable(List<QuantProtein> qProtList) {

        System.out.println("start store data");
        boolean success = true;
        String insertQProt = "INSERT INTO  `quant_full_table` (`author` ,`year` ,`pumed_id`,`study_key` ,`quantified_proteins_number`,`uniprot_accession` ,`uniprot_protein_name` ,`publication_acc_number` ,`publication_protein_name`,`peptide_prot` ,`raw_data_available` ,`peptideId_number`,`quantified_peptides_number`,`peptide_charge`"
                + ",`peptide_sequance`,`sequence_annotated`,`peptide_modification`,`modification_comment`,`type_of_study` ,`sample_type`,"
                + "`patients_group_i_number`,`patient_group_i` ,`patient_sub_group_i` ,`patient_gr_i_comment`,`patients_group_ii_number`,"
                + "`patient_group_ii` ,`patient_sub_group_ii` ,`patient_gr_ii_comment`,"
                + "`sample_matching` ,`normalization_strategy`"
                + ",`string_fc_value`,`fc_value`,`log_2_FC`,`string_p_value`,`p_value`,`pvalue_significance_threshold`,`pvalue_comments`,"
                + "`roc_auc`,`technology`,`analytical_method`,`analytical_approach`,"
                + "`shotgun_targeted`,`enzyme`,`quantification_basis`,`quant_basis_comment`,`additional_comments`,"
                + "`q_peptide_key`,`identified_proteins_number`,`index`,`disease_category`,`pooledsamples`)VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

        PreparedStatement insertQProtStat;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            try {
                Statement st = conn.createStatement();
                String dropStat = "DROP TABLE IF EXISTS `quant_dataset_table`;";
                st.executeUpdate(dropStat);
                st = conn.createStatement();
                dropStat = "DROP TABLE IF EXISTS `quant_full_table`;";
                st.executeUpdate(dropStat);
                st = conn.createStatement();
                dropStat = "DROP TABLE IF EXISTS `quantitative_proteins_table`;";
                st.executeUpdate(dropStat);
                st = conn.createStatement();
                dropStat = "DROP TABLE IF EXISTS `quantitative_peptides_table`;";
                st.executeUpdate(dropStat);
                createTables();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            int counter = 0;
            for (QuantProtein qprot : qProtList) {
                insertQProtStat = conn.prepareStatement(insertQProt, Statement.RETURN_GENERATED_KEYS);
                insertQProtStat.setString(1, qprot.getAuthor());
                insertQProtStat.setInt(2, qprot.getYear());
                insertQProtStat.setString(3, qprot.getPumedID());
                insertQProtStat.setString(4, qprot.getStudyKey());
                Integer num = qprot.getQuantifiedProteinsNumber();
                insertQProtStat.setInt(5, num);
                insertQProtStat.setString(6, qprot.getUniprotAccession());
                insertQProtStat.setString(7, qprot.getUniprotProteinName());
                insertQProtStat.setString(8, qprot.getPublicationAccNumber());
                insertQProtStat.setString(9, qprot.getPublicationProteinName());
                insertQProtStat.setString(10, String.valueOf(qprot.isPeptideObject()));
                if (qprot.getDiseaseCategory() == null || qprot.getDiseaseCategory().trim().equalsIgnoreCase("")) {
                    System.out.println(" --- qprot.getDiseaseCategory() --------------" + qprot.getDiseaseCategory());
                }

                insertQProtStat.setString(11, qprot.getRawDataAvailable());
                num = qprot.getPeptideIdNumb();
                insertQProtStat.setInt(12, num);
                num = qprot.getQuantifiedPeptidesNumber();
                insertQProtStat.setInt(13, num);
                num = qprot.getPeptideCharge();
                insertQProtStat.setInt(14, num);

                insertQProtStat.setString(15, qprot.getPeptideSequance());
                insertQProtStat.setString(16, qprot.getPeptideSequenceAnnotated());
                insertQProtStat.setString(17, qprot.getPeptideModification());
                insertQProtStat.setString(18, qprot.getModificationComment());
                insertQProtStat.setString(19, qprot.getTypeOfStudy());
                insertQProtStat.setString(20, qprot.getSampleType());

                num = qprot.getPatientsGroupINumber();
                insertQProtStat.setInt(21, num);
                insertQProtStat.setString(22, qprot.getPatientGroupI());
                insertQProtStat.setString(23, qprot.getPatientSubGroupI());
                insertQProtStat.setString(24, qprot.getPatientGrIComment());
                num = qprot.getPatientsGroupIINumber();
                insertQProtStat.setInt(25, num);
                insertQProtStat.setString(26, qprot.getPatientGroupII());
                insertQProtStat.setString(27, qprot.getPatientSubGroupII());
                insertQProtStat.setString(28, qprot.getPatientGrIIComment());

                insertQProtStat.setString(29, qprot.getSampleMatching());
                insertQProtStat.setString(30, qprot.getNormalizationStrategy());

                insertQProtStat.setString(31, qprot.getStringFCValue());
                Double dnum = qprot.getFcPatientGroupIonPatientGroupII();
                insertQProtStat.setDouble(32, dnum);
                dnum = qprot.getLogFC();
                insertQProtStat.setDouble(33, dnum);

                insertQProtStat.setString(34, qprot.getStringPValue());
                dnum = qprot.getpValue();
                insertQProtStat.setDouble(35, dnum);
                insertQProtStat.setString(36, qprot.getSignificanceThreshold());
                insertQProtStat.setString(37, qprot.getPvalueComment());

                dnum = qprot.getRocAuc();
                insertQProtStat.setDouble(38, dnum);
                insertQProtStat.setString(39, qprot.getTechnology());
                insertQProtStat.setString(40, qprot.getAnalyticalMethod());
                insertQProtStat.setString(41, qprot.getAnalyticalApproach());

                insertQProtStat.setString(42, qprot.getShotgunOrTargetedQquant());
                insertQProtStat.setString(43, qprot.getEnzyme());
                insertQProtStat.setString(44, qprot.getQuantificationBasis());
                insertQProtStat.setString(45, qprot.getQuantBasisComment());
                insertQProtStat.setString(46, qprot.getAdditionalComments());

                insertQProtStat.setString(47, qprot.getQuantPeptideKey());
                insertQProtStat.setInt(48, qprot.getIdentifiedProteinsNum());
                insertQProtStat.setInt(49, counter++);
                insertQProtStat.setString(50, qprot.getDiseaseCategory());
                insertQProtStat.setString(51, qprot.isPooledSample() + "");

                insertQProtStat.executeUpdate();
                insertQProtStat.clearParameters();
                insertQProtStat.close();

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
            try {
                createTables();
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            }
            DB.this.storeCombinedQuantProtTable(qProtList);

            success = false;
        } catch (ClassNotFoundException exp) {
            System.err.println(exp);
            exp.printStackTrace();
            success = false;
        } catch (InstantiationException exp) {
            System.err.println(exp);
            exp.printStackTrace();
            success = false;
        } catch (IllegalAccessException exp) {
            System.err.println(exp);
            exp.printStackTrace();
            success = false;
        }

        return success;

    }

    //quant data store 
    @SuppressWarnings({"null", "CallToPrintStackTrace"})
    public boolean updateProtSequances(Map<String, String> protSeqMap) {
        String updateProtSeqStat = "UPDATE  `quantitative_proteins_table` SET  `sequance` =  ? WHERE  `quantitative_proteins_table`.`uniprot_accession` =?";
        PreparedStatement updateProtSeqStatment = null;
        boolean test = false;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            for (String key : protSeqMap.keySet()) {
                updateProtSeqStatment = conn.prepareStatement(updateProtSeqStat);
                updateProtSeqStatment.setString(1, protSeqMap.get(key));
                updateProtSeqStatment.setString(2, key);
                int i = updateProtSeqStatment.executeUpdate();
                if (i > 0) {
                    test = true;
                }
            }

            updateProtSeqStatment.close();

        } catch (SQLException exp) {
            exp.printStackTrace();
        } catch (ClassNotFoundException exp) {
            exp.printStackTrace();
        } catch (InstantiationException exp) {
            exp.printStackTrace();
        } catch (IllegalAccessException exp) {
            exp.printStackTrace();
        }

        return test;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void storeQuantDatasets() {

        String selectPro = "SELECT DISTINCT  `author` ,`disease_category` ,`year` ,`pumed_id` , `study_key`, `quantified_proteins_number` , `identified_proteins_number` ,`raw_data_available` ,  `type_of_study` ,  `sample_type` ,  `sample_matching` ,  `normalization_strategy` ,  `technology` ,  `analytical_approach`  ,  `analytical_method`,  `enzyme` ,  `shotgun_targeted` ,  `quantification_basis` ,   `patients_group_i_number` ,  `patients_group_ii_number` ,  `patient_group_i` ,  `patient_gr_i_comment` ,  `patient_sub_group_i` ,  `patient_group_ii` ,  `patient_sub_group_ii` , `patient_gr_ii_comment`, `pooledsamples` \n"
                + "FROM  `quant_full_table` WHERE `peptide_prot` = 'false';";

        PreparedStatement selectProStat;
//        List<QuantProtein> quantProtResultList = null;
        Set<QuantDatasetObject> pubmidIds = new HashSet<QuantDatasetObject>();
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProStat = conn.prepareStatement(selectPro);
            ResultSet rs = selectProStat.executeQuery();

            int x = 0;
            while (rs.next()) {
                QuantDatasetObject pb = new QuantDatasetObject();
                pb.setStudyKey(rs.getString("study_key"));
                pb.setAuthor(rs.getString("author"));
                pb.setYear(rs.getInt("year"));
                pb.setPumedID(rs.getString("pumed_id"));
                pb.setQuantifiedProteinsNumber(rs.getInt("quantified_proteins_number"));
                pb.setTypeOfStudy(rs.getString("type_of_study"));
                pb.setSampleType(rs.getString("sample_type"));
                pb.setSampleMatching(rs.getString("sample_matching"));
                pb.setTechnology(rs.getString("technology"));
                pb.setAnalyticalApproach(rs.getString("analytical_approach"));
                pb.setAnalyticalMethod(rs.getString("analytical_method"));
                pb.setEnzyme(rs.getString("enzyme"));
                pb.setShotgunTargeted(rs.getString("shotgun_targeted"));
                pb.setQuantificationBasis(rs.getString("quantification_basis"));
                pb.setPatientsGroup1Number(rs.getInt("patients_group_i_number"));
                pb.setPatientsGroup2Number(rs.getInt("patients_group_ii_number"));
                pb.setIdentifiedProteinsNumber(rs.getInt("identified_proteins_number"));

                pb.setRawDataUrl(rs.getString("raw_data_available"));
                pb.setNormalizationStrategy(rs.getString("normalization_strategy"));
                pb.setPatientsGroup1(rs.getString("patient_group_i"));
                pb.setPatientsGroup1Comm(rs.getString("patient_gr_i_comment"));
                pb.setPatientsSubGroup1(rs.getString("patient_sub_group_i"));
                pb.setPatientsGroup2(rs.getString("patient_group_ii"));
                pb.setPatientsGroup2Comm(rs.getString("patient_gr_ii_comment"));
                pb.setPatientsSubGroup2(rs.getString("patient_sub_group_ii"));
                pb.setDiseaseCategory(rs.getString("disease_category"));
                pb.setPooledSample(Boolean.valueOf(rs.getString("pooledsamples")));
                pubmidIds.add(pb);
                x++;
            }

            String insertQProt = "INSERT INTO  `" + dbName + "`.`quant_dataset_table` (`pumed_id` ,\n"
                    + "`author` ,\n"
                    + "`identified_proteins_number` ,\n"
                    + "`quantified_proteins_number` ,\n"
                    + "`raw_data_available` ,\n"
                    + "`year` ,\n"
                    + "`type_of_study` ,\n"
                    + "`sample_type` ,\n"
                    + "`sample_matching` ,\n"
                    + "`technology` ,\n"
                    + "`analytical_approach` ,\n"
                    + "`enzyme` ,\n"
                    + "`shotgun_targeted` ,\n"
                    + "`quantification_basis` ,\n"
                    + "`patients_group_i_number` ,\n"
                    + "`patients_group_ii_number` ,  `normalization_strategy`"
                    + ",`patient_group_i`,`patient_gr_i_comment`,`patient_sub_group_i`,`patient_group_ii`,`patient_gr_ii_comment`,`patient_sub_group_ii`,`study_key`,`analytical_method`,`index`,`disease_category`,`pooledsamples`)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

            PreparedStatement insertPbublicationStat;

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            int counter = 0;
            for (QuantDatasetObject pb : pubmidIds) {
                insertPbublicationStat = conn.prepareStatement(insertQProt, Statement.RETURN_GENERATED_KEYS);
                insertPbublicationStat.setString(1, pb.getPumedID());
                insertPbublicationStat.setString(2, pb.getAuthor());
                insertPbublicationStat.setInt(3, pb.getIdentifiedProteinsNumber());
                insertPbublicationStat.setInt(4, pb.getQuantifiedProteinsNumber());

                insertPbublicationStat.setString(5, pb.getRawDataUrl());
                insertPbublicationStat.setInt(6, pb.getYear());

                insertPbublicationStat.setString(7, pb.getTypeOfStudy());
                insertPbublicationStat.setString(8, pb.getSampleType());
                insertPbublicationStat.setString(9, pb.getSampleMatching());
                insertPbublicationStat.setString(10, pb.getTechnology());
                insertPbublicationStat.setString(11, pb.getAnalyticalApproach());
                insertPbublicationStat.setString(12, pb.getEnzyme());

                insertPbublicationStat.setString(13, pb.getShotgunTargeted());

                insertPbublicationStat.setString(14, pb.getQuantificationBasis());

                insertPbublicationStat.setInt(15, pb.getPatientsGroup1Number());
                insertPbublicationStat.setInt(16, pb.getPatientsGroup2Number());
                insertPbublicationStat.setString(17, pb.getNormalizationStrategy());

                insertPbublicationStat.setString(18, pb.getPatientsGroup1());

                insertPbublicationStat.setString(19, pb.getPatientsGroup1Comm());

                insertPbublicationStat.setString(20, pb.getPatientsSubGroup1());

                insertPbublicationStat.setString(21, pb.getPatientsGroup2());

                insertPbublicationStat.setString(22, pb.getPatientsGroup2Comm());

                insertPbublicationStat.setString(23, pb.getPatientsSubGroup2());
                insertPbublicationStat.setString(24, pb.getStudyKey());
                insertPbublicationStat.setString(25, pb.getAnalyticalMethod());
                insertPbublicationStat.setInt(26, counter++);
                insertPbublicationStat.setString(27, pb.getDiseaseCategory());
                insertPbublicationStat.setString(28, pb.isPooledSample() + "");
                insertPbublicationStat.executeUpdate();
                insertPbublicationStat.clearParameters();
                insertPbublicationStat.close();

            }

            System.out.println("done with storing ds ");

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());
        }

    }

    public Object[] storeQuantitiveProteins(List<QuantProtein> qProtList) {
        Map<String, Integer> updatedProtList = new HashMap<String, Integer>();
        Map<Integer, Integer> protDsMap = new HashMap<Integer, Integer>();
        String sqlStat = "INSERT INTO "
                + " `quantitative_proteins_table` ( "
                + "`ds_ID` , "
                + "`uniprot_accession` , "
                + "`uniprot_protein_name` , "
                + "`publication_acc_number` , "
                + "`publication_protein_name` , "
                + "`quantified_peptides_number` , "
                + "`identified_peptides_number`,`fold_change`,`additional_comments`,`sequance`,`fc_value`,`roc_auc`,`string_p_value`, `p_value`, `p_value_comments`, `log_2_FC`, `pvalue_significance_threshold`, `index`, `quant_bases_comments`"
                + ")"
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

        try {
            PreparedStatement insertQuantProtStat;
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            int counter = 0;
            for (QuantProtein quantProt : qProtList) {
                if (quantProt.isPeptideObject()) {
                    continue;
                }
                insertQuantProtStat = conn.prepareStatement(sqlStat, Statement.RETURN_GENERATED_KEYS);
//                insertQuantProtStat.setInt(1, quantProt.getProtKey());
                insertQuantProtStat.setInt(1, quantProt.getDsKey());
                if (quantProt.getUniprotAccession().trim().equalsIgnoreCase("")) {
                    System.out.println("------------>> " + quantProt.getPublicationAccNumber());
                }
                insertQuantProtStat.setString(2, quantProt.getUniprotAccession());
                insertQuantProtStat.setString(3, quantProt.getUniprotProteinName());

                insertQuantProtStat.setString(4, quantProt.getPublicationAccNumber());
                insertQuantProtStat.setString(5, quantProt.getPublicationProteinName());
                insertQuantProtStat.setInt(6, quantProt.getQuantifiedPeptidesNumber());
                insertQuantProtStat.setInt(7, quantProt.getPeptideIdNumb());
                insertQuantProtStat.setString(8, quantProt.getStringFCValue());
                insertQuantProtStat.setString(9, quantProt.getAdditionalComments());
                insertQuantProtStat.setString(10, quantProt.getSequance());

                insertQuantProtStat.setDouble(11, quantProt.getFcPatientGroupIonPatientGroupII());
                insertQuantProtStat.setDouble(12, quantProt.getRocAuc());
                insertQuantProtStat.setString(13, quantProt.getStringPValue());
                insertQuantProtStat.setDouble(14, quantProt.getpValue());
                insertQuantProtStat.setString(15, quantProt.getPvalueComment());
                insertQuantProtStat.setDouble(16, quantProt.getLogFC());
                insertQuantProtStat.setString(17, quantProt.getSignificanceThreshold());
                insertQuantProtStat.setInt(18, counter);
                insertQuantProtStat.setString(19, quantProt.getQuantBasisComment());

                insertQuantProtStat.executeUpdate();
                ResultSet rs = insertQuantProtStat.getGeneratedKeys();
                quantProt.setProtKey(counter++);

//                while (rs.next()) {
//                    int index = rs.getInt(1);
//                    System.out.println("generated key is "+index);
//                    quantProt.setProtKey(index);
//
//                }
                rs.close();
                updatedProtList.put(quantProt.getQuantPeptideKey(), quantProt.getProtKey());
                protDsMap.put(quantProt.getProtKey(), quantProt.getDsKey());
            }

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e);

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e);

        } catch (InstantiationException e) {
            System.err.println("at error" + e);

        } catch (SQLException e) {
            System.err.println("at error" + e);

        }
        System.out.println("done storing prote");
        System.gc();
        Object[] maps = new Object[2];
        maps[0] = updatedProtList;
        maps[1] = protDsMap;
        return maps;

    }

    public void storeQuantitivePeptides(List<QuantProtein> qPeptidestList) {
        String sqlStat = "INSERT INTO  `quantitative_peptides_table` ( "
                + "`DsKey` , "
                + "`prot_index` , "
                + "`peptide_sequance` , "
                + "`peptide_modification` , "
                + "`modification_comment` , "
                + "`string_fc_value` , "
                + "`string_p_value` , "
                + "`p_value` , "
                + "`roc_auc` , "
                + "`fc_value`,`p_value_comments`,`proteinAccession` ,`additional_comments`,`log_2_FC`, `pvalue_significance_threshold`,`sequence_annotated`,`peptide_charge`,`index`,`quant_bases_comments`"
                + ")\n"
                + "VALUES ( "
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

        try {
            PreparedStatement insertQuantPeptidtStat;
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            int counter = 0;
            for (QuantProtein quantPept : qPeptidestList) {
                insertQuantPeptidtStat = conn.prepareStatement(sqlStat, Statement.RETURN_GENERATED_KEYS);
                insertQuantPeptidtStat.setInt(2, quantPept.getProtKey());
                insertQuantPeptidtStat.setInt(1, quantPept.getDsKey());
                insertQuantPeptidtStat.setString(3, quantPept.getPeptideSequance());
                insertQuantPeptidtStat.setString(4, quantPept.getPeptideModification());

                insertQuantPeptidtStat.setString(5, quantPept.getModificationComment());
                insertQuantPeptidtStat.setString(6, quantPept.getStringFCValue());
                insertQuantPeptidtStat.setString(7, quantPept.getStringPValue());
                insertQuantPeptidtStat.setDouble(8, quantPept.getpValue());
                insertQuantPeptidtStat.setDouble(9, quantPept.getRocAuc());
                insertQuantPeptidtStat.setDouble(10, quantPept.getFcPatientGroupIonPatientGroupII());
                insertQuantPeptidtStat.setString(11, quantPept.getPvalueComment());

                insertQuantPeptidtStat.setString(12, quantPept.getUniprotAccession());
                insertQuantPeptidtStat.setString(13, quantPept.getAdditionalComments());
                insertQuantPeptidtStat.setDouble(14, quantPept.getLogFC());
                insertQuantPeptidtStat.setString(15, quantPept.getSignificanceThreshold());
                insertQuantPeptidtStat.setString(16, quantPept.getPeptideSequenceAnnotated());
                insertQuantPeptidtStat.setInt(17, quantPept.getPeptideCharge());
                insertQuantPeptidtStat.setInt(18, counter++);
                insertQuantPeptidtStat.setString(19, quantPept.getQuantBasisComment());
                insertQuantPeptidtStat.executeUpdate();
                ResultSet rs = insertQuantPeptidtStat.getGeneratedKeys();
                rs.close();
            }

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        System.out.println("done storing pep");
        System.gc();

    }

    private String updateStringFormat(String str) {
        str = str.toLowerCase();
        str = str.replaceFirst(str.substring(0, 1), str.substring(0, 1).toUpperCase());
        return str;

    }

    public int getCurrentProtIndex() {
        String sqlStat = "SELECT `index`  "
                + "FROM  `quantitative_proteins_table`  "
                + "ORDER BY  `quantitative_proteins_table`.`index` DESC  "
                + "LIMIT 0 , 1";

        try {
            PreparedStatement selectCurrentQuantProtIndexStat;
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectCurrentQuantProtIndexStat = conn.prepareStatement(sqlStat);
            ResultSet rs = selectCurrentQuantProtIndexStat.executeQuery();
            int index = 0;
            while (rs.next()) {
                index = rs.getInt("index");
                index++;

            }
            rs.close();

            return index;

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        System.gc();

        return 0;

    }

    public Set<QuantDatasetObject> getQuantDatasetListObject() {

        Set<QuantDatasetObject> quantDatasetList = new HashSet<QuantDatasetObject>();
        boolean[] activeHeaders = new boolean[26];

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
                int identified_proteins_number = rs.getInt("identified_proteins_number");
                if (!activeHeaders[2] && identified_proteins_number != -1 && identified_proteins_number != 0) {
                    activeHeaders[2] = true;
                }
                pb.setIdentifiedProteinsNumber(identified_proteins_number);

                int quantified_proteins_number = rs.getInt("quantified_proteins_number");
                if (!activeHeaders[3] && quantified_proteins_number != -1) {
                    activeHeaders[3] = true;
                }
                pb.setQuantifiedProteinsNumber(quantified_proteins_number);

//                String disease_group = rs.getString("disease_group");
//                if (!activeHeaders[4] && disease_group != null && !disease_group.equalsIgnoreCase("Not Available")) {
//                    activeHeaders[4] = true;
//                }
//                pb.setAnalyticalMethod(disease_group);
                String raw_data_available = rs.getString("raw_data_available");
                if (!activeHeaders[5] && raw_data_available != null && !raw_data_available.equalsIgnoreCase("Not Available")) {
                    activeHeaders[5] = true;
                }
                pb.setRawDataUrl(raw_data_available);

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
                pb.setUniqId(id);

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

                pb.setDiseaseCategory(rs.getString("disease_category"));

//                String additional_comments = rs.getString("additional_comments");
//                if (!activeHeaders[26] && additional_comments != null && !additional_comments.equalsIgnoreCase("Not Available")) {
//                    activeHeaders[26] = true;
//                }
//                pb.setAdditionalcomments(additional_comments);
                quantDatasetList.add(pb);

            }
            rs.close();

            return quantDatasetList;

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        System.gc();

        return null;

    }

    public void correctProtInfo() {
        String sqlStat = "SELECT *FROM `quantitative_peptides_table` where `peptide_sequance` ='Not Available' ;";
        String protStat = "UPDATE `quantitative_proteins_table` SET `fold_change` = ?   , `fc_value` = ? ,`roc_auc` = ? , `string_p_value` = ?,`p_value` = ?,`p_value_comments` = ?  WHERE `quantitative_proteins_table`.`index` = ? ;";

        try {
            PreparedStatement selectCurrentQuantProtIndexStat;
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectCurrentQuantProtIndexStat = conn.prepareStatement(sqlStat);
            ResultSet rs = selectCurrentQuantProtIndexStat.executeQuery();
            int index = 0;
            Set<QuantPeptide> peptideMap = new HashSet<QuantPeptide>();
            while (rs.next()) {
                QuantPeptide qpep = new QuantPeptide();
                qpep.setProtKey(rs.getInt("prot_index"));
                qpep.setStrPvalue(rs.getString("string_p_value"));
                qpep.setFc(rs.getString("string_fc_value"));
                qpep.setPvalue(rs.getDouble("p_value"));
                qpep.setRoc(rs.getDouble("roc_auc"));
                qpep.setFcPatientGroupIonPatientGroupII(rs.getDouble("fc_value"));
                qpep.setPvalueComment(rs.getString("p_value_comments"));
                peptideMap.add(qpep);
            }
            rs.close();
            PreparedStatement updateProtTableStat;
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            for (QuantPeptide qPep : peptideMap) {
                if (qPep.getFc().equalsIgnoreCase("Not Provided") && qPep.getFcPatientGroupIonPatientGroupII() == -1000000000) {
                    continue;
                }
                updateProtTableStat = conn.prepareStatement(protStat);
                updateProtTableStat.setString(1, qPep.getFc());
                updateProtTableStat.setDouble(2, qPep.getFcPatientGroupIonPatientGroupII());
                updateProtTableStat.setDouble(3, qPep.getRoc());
                updateProtTableStat.setString(4, qPep.getStrPvalue());
                updateProtTableStat.setDouble(5, qPep.getPvalue());
                updateProtTableStat.setString(6, qPep.getPvalueComment());
                updateProtTableStat.setInt(7, qPep.getProtKey());
                updateProtTableStat.executeUpdate();

            }

            conn.close();

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }

        System.gc();

    }

    public void getIdentificationProteinsList() {
        List<IdentificationProteinBean> proteinDatasetList = new ArrayList<IdentificationProteinBean>();
        try {
            PreparedStatement selectProtDatasetStat;
            String selectProtDataset = "SELECT `prot_accession`,`other_protein(s)` FROM `experiment_protein_table`;";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProtDatasetStat = conn.prepareStatement(selectProtDataset);
            ResultSet rs = selectProtDatasetStat.executeQuery();
            while (rs.next()) {
                IdentificationProteinBean temPb = new IdentificationProteinBean();
                temPb.setAccession(rs.getString("prot_accession"));
                temPb.setOtherProteins(rs.getString("other_protein(s)"));
                proteinDatasetList.add(temPb);

            }
            rs.close();
            Set<String> protAccessionSet = new HashSet<String>();
            for (IdentificationProteinBean pb : proteinDatasetList) {
                protAccessionSet.add(pb.getAccession());
                if (pb.getOtherProteins() == null || pb.getOtherProteins().trim().equalsIgnoreCase("")) {
                    continue;
                }
                if (!pb.getOtherProteins().contains(",")) {
                    protAccessionSet.add(pb.getOtherProteins());
                } else {
                    String[] accArr = pb.getOtherProteins().split(",");
                    protAccessionSet.addAll(Arrays.asList(accArr));
                }

            }

            File csvText = new File("D:\\iddatanames", "CSF-PR-id-accession-list.csv");// "CSF-PR - " + datasetName + " - All - " + dataType + "- Peptides" + ".csv");
            PrintWriter out1 = null;
            try {
                if (csvText.exists()) {
                    csvText.delete();
                }
                csvText.createNewFile();
                FileWriter outFile = new FileWriter(csvText, true);
                out1 = new PrintWriter(outFile);
                for (String idProt : protAccessionSet) {
                    out1.append(idProt);
                    out1.append('\n');

                }
                out1.flush();
                out1.close();
            } catch (Exception exp) {
                System.err.println(exp.getLocalizedMessage());
                if (out1 != null) {
                    out1.flush();
                    out1.close();
                }
            }

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
    }

    @SuppressWarnings("null")
    public void updateIdentificationProteinsList(String fileUrl) {
        List<IdentificationProteinBean> proteinDatasetList = new ArrayList<IdentificationProteinBean>();
        try {
            PreparedStatement selectProtDatasetStat;
            String selectProtDataset = "SELECT `prot_accession`,`other_protein(s)`,`description`,`prot_group_id` FROM `experiment_protein_table`;";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProtDatasetStat = conn.prepareStatement(selectProtDataset);
            ResultSet rs = selectProtDatasetStat.executeQuery();
            while (rs.next()) {
                IdentificationProteinBean temPb = new IdentificationProteinBean();
                temPb.setAccession(rs.getString("prot_accession"));
                temPb.setOtherProteins(rs.getString("other_protein(s)"));
                temPb.setDescription(rs.getString("description"));
                temPb.setProtGroupId(rs.getInt("prot_group_id"));
                proteinDatasetList.add(temPb);

            }
            rs.close();
            System.out.println("done with db");
            //load the data
            Map<String, String> updatedDescriptionSet = new HashMap<String, String>();

            try {
                FileReader sequanceReader = new FileReader(fileUrl);
                BufferedReader sequanceBufRdr = new BufferedReader(sequanceReader);
//            sequanceBufRdr.readLine();

//             Map<String, String> proteinAccNameMap = new HashMap<String, String>();
                String seqline;

                int row = 1;
                while ((seqline = sequanceBufRdr.readLine()) != null && !seqline.trim().equalsIgnoreCase("") && row < 1000000000) {
                    if (seqline.startsWith(">sp|")) {
                        String[] seqArr = seqline.replace("|", "----").split("----");
                        String acc = (seqArr[1]);
                        String secSide = seqArr[2].split("OS=")[0].trim();
                        String protName = secSide.replace(secSide.split(" ")[0], "") + " (" + secSide.split(" ")[0] + ")";
                        updatedDescriptionSet.put(acc, protName);

                    }
//             
                }

                sequanceBufRdr.close();
            } catch (IOException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            }
            int counter = 0;
            Set<String> tobeUpdatedSet = new HashSet<String>();

            System.out.println("done with reader");
            List<IdentificationProteinBean> updateProteinDatasetList = new ArrayList<IdentificationProteinBean>();
            int size = 0;
            for (IdentificationProteinBean temPb : proteinDatasetList) {
                String desc = "";
                if (updatedDescriptionSet.containsKey(temPb.getAccession())) {
                    desc = desc + updatedDescriptionSet.get(temPb.getAccession());

                } else {
                    counter++;
                    tobeUpdatedSet.add(temPb.getAccession());
                }

//                if (temPb.getOtherProteins() == null || temPb.getOtherProteins().equalsIgnoreCase("")) {
//
//                } else if (!temPb.getOtherProteins().contains(",")) {
//
//                    desc = desc + "," + updatedDescriptionSet.get(temPb.getOtherProteins());
//                } else {
//                    String[] accArr = temPb.getOtherProteins().split(",");
//                    for (String str : accArr) {
//                        desc = desc + "," + updatedDescriptionSet.get(str);
//                    }
//
//                }
//                if (desc.length() ==3763) {
//                    System.out.println("-----------------------------------------------------------------");
//                    System.out.println("dsid : "+temPb.getProtGroupId()+"  "+ temPb.getDescription());
//                    System.out.println("desc: "+ desc);
//                    System.out.println("-----------------------------------------------------------------");
//                }
                if (!desc.equalsIgnoreCase("")) {
                    temPb.setDescription(desc);
                }
                updateProteinDatasetList.add(temPb);

            }
            System.out.println("un updated " + counter);
            System.out.println("tobe updated " + tobeUpdatedSet.size());
            //update the database
            String updateProtDescStat = "UPDATE  `experiment_protein_table` SET  `description` =  ? WHERE  `experiment_protein_table`.`prot_group_id` =?";
            PreparedStatement updateProtDescStatment = null;

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            for (IdentificationProteinBean pb : updateProteinDatasetList) {
                updateProtDescStatment = conn.prepareStatement(updateProtDescStat);
                updateProtDescStatment.setString(1, pb.getDescription());
                updateProtDescStatment.setInt(2, pb.getProtGroupId());
                updateProtDescStatment.executeUpdate();
            }

            updateProtDescStatment.close();
            System.gc();

        } catch (SQLException e) {
            System.err.println("at error line 1340 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 1340 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error line 1340 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error line 1340 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        }
        System.gc();
    }

    public boolean updateDiseaseGroupsFullName(Map<String, String> diseaseGroupsNamingMap) {

        StringBuilder valuesBuilder = new StringBuilder();
        for (String key : diseaseGroupsNamingMap.keySet()) {
            valuesBuilder.append("(").append("?").append(",").append("?").append("),");

        }
        String valueStr = valuesBuilder.toString().substring(0, valuesBuilder.length() - 1) + ";";

        String insertStat = "INSERT INTO `defin_disease_groups` (`min`, `full`) VALUES " + valueStr;
        try {
            PreparedStatement insertFullDiseaseNameStat;
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String dropStat = "TRUNCATE TABLE  `defin_disease_groups`";
            Statement st = conn.createStatement();
            st.executeUpdate(dropStat);
            st.close();

            insertFullDiseaseNameStat = conn.prepareStatement(insertStat, Statement.RETURN_GENERATED_KEYS);
            int counter = 1;
            for (String key : diseaseGroupsNamingMap.keySet()) {
                insertFullDiseaseNameStat.setString(counter++, key);
                insertFullDiseaseNameStat.setString(counter++, diseaseGroupsNamingMap.get(key));

            }
            insertFullDiseaseNameStat.executeUpdate();

        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        System.out.println("done storing pep");
        System.gc();

        return true;
    }

    public boolean insertPublication(String pubmedid, String author, String year, String title) {

        String insertStat = "INSERT INTO  `publication_table` (`pubmed_id` ,`author` ,`year` ,`title`)VALUES (?,?,?,?);";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String dropStat = "DELETE FROM `publication_table` WHERE   `pubmed_id` =?";
            PreparedStatement st = conn.prepareStatement(dropStat);
            st.setString(1, pubmedid);
            st.executeUpdate();
            st.close();

            st = conn.prepareStatement(insertStat);
            st.setString(1, pubmedid);
            st.setString(2, author);
            st.setString(3, year);
            st.setString(4, title);
            int i = st.executeUpdate();
            st.close();
            if (i > 0) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        System.gc();
        return false;

    }

    public void updateActivePublications(Map<String, Object[]> publicationUpdatingMap) {
        System.out.println("in update publications ");

        String initStatment = "UPDATE  `publication_table` SET  `active` = false";
        String statment = "UPDATE  `publication_table` SET  `active` = ?,  `total_prot_num` = ? , `uniq_prot_num` = ? , `total_pept_num` = ? , `uniq_pept_num` = ? WHERE  `publication_table`.`pubmed_id` = ? ;";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            PreparedStatement initSat = conn.prepareStatement(initStatment);
            initSat.executeUpdate();

            for (String pubmedId : publicationUpdatingMap.keySet()) {
                Object[] publicationData = publicationUpdatingMap.get(pubmedId);
                PreparedStatement st = conn.prepareStatement(statment);
                st.setString(1, publicationData[0].toString().toLowerCase());
                st.setInt(2, (Integer) publicationData[1]);
                st.setInt(3, (Integer) publicationData[2]);
                st.setInt(4, (Integer) publicationData[3]);
                st.setInt(5, (Integer) publicationData[4]);
                st.setString(6, pubmedId);

                st.executeUpdate();
                st.close();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        System.gc();

    }

    public void updateQuantStudies(Map<Integer, Object[]> styudyUpdatingMap) {

        String statment = "UPDATE  `quant_dataset_table` SET    `total_prot_num` = ? , `uniq_prot_num` = ? , `total_pept_num` = ? , `uniq_pept_num` = ? WHERE  `quant_dataset_table`.`index` = ? ;";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            for (int studyId : styudyUpdatingMap.keySet()) {
                Object[] publicationData = styudyUpdatingMap.get(studyId);
                PreparedStatement st = conn.prepareStatement(statment);
                st.setInt(1, (Integer) publicationData[1]);
                st.setInt(2, (Integer) publicationData[2]);
                st.setInt(3, (Integer) publicationData[3]);
                st.setInt(4, (Integer) publicationData[4]);
                st.setInt(5, studyId);

                st.executeUpdate();
                st.close();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error" + e.getLocalizedMessage());

        }
        System.gc();

    }

    public List<Object[]> getPublications() {

        List<Object[]> publicationsData = new ArrayList<Object[]>();
        String selectStat = "SELECT * FROM  `publication_table` ";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            PreparedStatement st = conn.prepareStatement(selectStat);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                publicationsData.add(new Object[]{rs.getString("pubmed_id"), rs.getString("author"), rs.getString("year"), rs.getString("title")});
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
        return publicationsData;

    }

}
