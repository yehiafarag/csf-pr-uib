package no.uib.probe.csf.pr.touch.logic;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import no.uib.probe.csf.pr.touch.database.DataBaseLayer;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetInitialInformationObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.logic.beans.QuantPeptide;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;
import no.uib.probe.csf.pr.touch.view.smallscreen.OverviewInfoBean;

/**
 *
 * @author Yehia Farag
 *
 * this class contains the logic layer (main computing code) this class interact
 * with both the data handler and the data access layer
 */
public class CoreLogic implements Serializable {

    private final DataBaseLayer database;

    public CoreLogic(String url, String dbName, String driver, String userName, String password, String filesURL) {
        database = new DataBaseLayer(url, dbName, driver, userName, password);

    }

    /**
     * this method responsible for getting the resource overview information
     *
     * @return OverviewInfoBean resource information bean
     */
    public OverviewInfoBean getResourceOverviewInformation() {
        return database.getResourceOverviewInformation();

    }

    /**
     * this method responsible for getting initial publication information
     *
     * @return list of publications available in the the resource
     */
    public List<Object[]> getPublicationList() {

        return database.getPublicationList();

    }

    /**
     * this method responsible for getting initial datasets information
     *
     * @return set of datasets information available in the the resource
     */
    public Set<QuantDatasetObject> getQuantDatasetList() {
        return database.getQuantDatasetList();

    }

    /**
     * Get the current available disease category list
     *
     * @return set of disease category objects that has all disease category
     * information and styling information
     */
    public Map<String, DiseaseCategoryObject> getDiseaseCategorySet() {
        Map<String, DiseaseCategoryObject> availableDiseaseCategory = database.getDiseaseCategorySet();
        return availableDiseaseCategory;
    }

    /**
     * get available quantification datasets initial information object that
     * contains the available datasets list and the active columns (to hide them
     * if they are empty)
     *
     * @return QuantDatasetInitialInformationObject
     */
    public Map<String, QuantDatasetInitialInformationObject> getQuantDatasetInitialInformationObject() {
        Map<String, QuantDatasetInitialInformationObject> quantStudyInitInfoMap = database.getQuantDatasetInitialInformationObject();

        boolean[] activeHeaders = new boolean[27];
        Set<String> diseaseCategories = new LinkedHashSet<>();
        QuantDatasetInitialInformationObject allDatasetObject = new QuantDatasetInitialInformationObject();
        Map<Integer, QuantDatasetObject> updatedQuantDatasetObjectMap = new LinkedHashMap<>();

        quantStudyInitInfoMap.keySet().stream().map((disease_category) -> quantStudyInitInfoMap.get(disease_category)).map((datasetObject) -> {
            boolean[] dataactiveHeader = datasetObject.getActiveHeaders();
            int counter = 0;
            for (boolean active : dataactiveHeader) {
                if (!activeHeaders[counter] && active) {
                    activeHeaders[counter] = true;
                }
                counter++;
            }
            updatedQuantDatasetObjectMap.putAll(datasetObject.getQuantDatasetsList());
            return datasetObject;
        }).forEach((datasetObject) -> {
            diseaseCategories.addAll(datasetObject.getDiseaseCategories());
        });
        allDatasetObject.setQuantDatasetsList(updatedQuantDatasetObjectMap);
        allDatasetObject.setActiveHeaders(activeHeaders);
        allDatasetObject.setDiseaseCategories(diseaseCategories);
        quantStudyInitInfoMap.put("All Diseases", allDatasetObject);

        return quantStudyInitInfoMap;

    }

    /**
     * get active quantification pie charts filters (to hide them if they are
     * empty)
     *
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public Map<String, boolean[]> getActivePieChartQuantFilters() {
        return database.getActivePieChartQuantFilters();

    }

    /**
     * get active quantification pie charts filters within quant searching
     * proteins results (to hide them if they are empty)
     *
     * @param searchQuantificationProtList
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public Map<String, boolean[]> getActivePieChartQuantFilters(List<QuantProtein> searchQuantificationProtList) {
        return database.getActivePieChartQuantFilters(searchQuantificationProtList);

    }

    /**
     * Get set of disease groups names for special disease category
     *
     * @param diseaseCat
     * @return map of the short and long diseases names
     */
    public Set<String> getDiseaseGroupNameMap(String diseaseCat) {
        return database.getDiseaseGroupNameMap(diseaseCat);

    }

    /**
     * Get map for disease groups full name
     *
     * @return map of the short and long diseases groups names
     */
    public Map<String, String> getDiseaseGroupsFullNameMap() {
        return database.getDiseaseGroupsFullNameMap();
    }

    /**
     * this method is responsible for update quant comparison proteins map for
     * each comparison
     *
     *
     * @param selectedQuantComparisonsList selected comparisons
     * @return updated quant comparisons list
     */
    public Set<QuantDiseaseGroupsComparison> updateComparisonQuantProteins(Set<QuantDiseaseGroupsComparison> selectedQuantComparisonsList) {

        Set<QuantDiseaseGroupsComparison> updatedSelectedComparisonList = new LinkedHashSet<>();
        Set<QuantProtein> fullComparisonProtMap = new HashSet<>();
        Map<Integer, QuantDiseaseGroupsComparison> dsIndexToComparisonsMap = new HashMap<>();
        Set<Integer> dsIdsList = new HashSet<>();

//        for (QuantDiseaseGroupsComparison comparison : selectedQuantComparisonsList) {
//            if (comparison.getQuantComparisonProteinMap() == null || comparison.getQuantComparisonProteinMap().isEmpty()) {
//                for (int datasetIndex : comparison.getDatasetMap().keySet()) {
//                    dsIdsList.add(datasetIndex);
//                    dsIndexToComparisonsMap.put(datasetIndex, comparison);
//                }
//
//            }else{
//                updatedSelectedComparisonList.add(comparison);
//                
//            }
//
//        }
        selectedQuantComparisonsList.stream().filter((comparison) -> (comparison.getQuantComparisonProteinMap() == null || comparison.getQuantComparisonProteinMap().isEmpty())).forEach((comparison) -> {
            comparison.getDatasetMap().keySet().stream().map((datasetIndex) -> {
                dsIdsList.add(datasetIndex);
                return datasetIndex;
            }).forEach((datasetIndex) -> {
                dsIndexToComparisonsMap.put(datasetIndex, comparison);
            });
        });
        if (dsIdsList.isEmpty()) {
            return selectedQuantComparisonsList;
        }
        fullComparisonProtMap.addAll(database.getQuantificationProteins(dsIdsList.toArray()));
        Map<String, Set<QuantPeptide>> fullComparisonPeptideMap = (database.getQuantificationPeptides(dsIdsList.toArray()));

        Map<String, Set<QuantProtein>> fullComparisonToProtMap = new HashMap<>();
        fullComparisonProtMap.stream().forEach((qprot) -> {
            String datasetIndex = dsIndexToComparisonsMap.get(qprot.getDsKey()).getComparisonHeader();
            if (!fullComparisonToProtMap.containsKey(datasetIndex)) {
                Set<QuantProtein> comparisonProtMap = new HashSet<>();
                fullComparisonToProtMap.put(datasetIndex, comparisonProtMap);

            }
            Set<QuantProtein> comparisonProtMap = fullComparisonToProtMap.get(datasetIndex);
            comparisonProtMap.add(qprot);
            fullComparisonToProtMap.put(datasetIndex, comparisonProtMap);
        });

        for (QuantDiseaseGroupsComparison comparison : selectedQuantComparisonsList) {
            Set<QuantProtein> comparisonProtMap = fullComparisonToProtMap.get(comparison.getComparisonHeader());
            if (comparisonProtMap == null) {
                continue;
            }

            Map<String, QuantComparisonProtein> comparProtList = new HashMap<>();

            String compGrI = comparison.getOreginalComparisonHeader().split(" / ")[0];

            String diseaseCategory = comparison.getDiseaseCategory();

            for (QuantProtein quant : comparisonProtMap) {
                boolean inverted = false;
                String dsPGrI = comparison.getDatasetMap().get(quant.getDsKey()).getUpdatedDiseaseGroupI();
                String pGrI;
                pGrI = "";
                String pGrII;
                pGrII = "";
                if (compGrI.equalsIgnoreCase(dsPGrI)) {
                    pGrI = comparison.getDatasetMap().get(quant.getDsKey()).getPatientsSubGroup1();
                    pGrII = comparison.getDatasetMap().get(quant.getDsKey()).getPatientsSubGroup2();
                } else {
                    pGrI = comparison.getDatasetMap().get(quant.getDsKey()).getPatientsSubGroup2();
                    pGrII = comparison.getDatasetMap().get(quant.getDsKey()).getPatientsSubGroup1();

                }

                String protAcc = quant.getUniprotAccession();
//                System.out.println("at ------------------------------------------------------ >> header "+comparison.getDatasetMap().get(quant.getDsKey()).getPatientsSubGroup1()+"  oreg "+comparison.getOreginalComparisonHeader());
                if (protAcc.equalsIgnoreCase("") || protAcc.equalsIgnoreCase("Not Available") || protAcc.equalsIgnoreCase("Entry Deleted") || protAcc.equalsIgnoreCase("Entry Demerged") || protAcc.equalsIgnoreCase("NOT RETRIEVED") || protAcc.equalsIgnoreCase("DELETED") || protAcc.trim().equalsIgnoreCase("UNREVIEWED")) {
                    protAcc = quant.getPublicationAccNumber();

                }

                if (!comparProtList.containsKey(protAcc)) {
                    QuantComparisonProtein comProt = new QuantComparisonProtein(comparison.getDatasetMap().size(), comparison, quant.getProtKey());
                    comProt.setQuantPeptidesList(new HashSet<>());
                    comparProtList.put(protAcc, comProt);
                }

                QuantComparisonProtein comProt = comparProtList.get(protAcc);

                boolean significantPValue = true;
                if (quant.getStringPValue().equalsIgnoreCase("Not Significant") || quant.getStringPValue().equalsIgnoreCase("Not Available")) {
                    significantPValue = false;

                }

//                System.out.println("at PGI -" + pGrI + "-   -" + quant.getPatientSubGroupI() + "-");
//                System.out.println("at PGII -" + pGrII + "-   -" + quant.getPatientSubGroupII() + "-");
//                System.out.println("at not enverted "+((pGrI.equalsIgnoreCase(quant.getPatientGroupI()) || pGrI.equalsIgnoreCase(quant.getPatientSubGroupI())) && (pGrII.equalsIgnoreCase(quant.getPatientGroupII()) || pGrII.equalsIgnoreCase(quant.getPatientSubGroupII()))));
                if ((pGrI.equalsIgnoreCase(quant.getPatientGroupI()) || pGrI.equalsIgnoreCase(quant.getPatientSubGroupI())) && (pGrII.equalsIgnoreCase(quant.getPatientGroupII()) || pGrII.equalsIgnoreCase(quant.getPatientSubGroupII()))) {
                    if (quant.getStringFCValue().equalsIgnoreCase("Decreased") || quant.getStringFCValue().equalsIgnoreCase("Decrease")) {
                        comProt.addDown((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey(), significantPValue);
                    } else if (quant.getStringFCValue().equalsIgnoreCase("Increased") || quant.getStringFCValue().equalsIgnoreCase("Increase")) {
                        comProt.addUp((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey(), significantPValue);
                    } else if (quant.getStringFCValue().equalsIgnoreCase("Not Provided")) {
                        comProt.addNoValueProvided((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
                    } else if (quant.getStringFCValue().equalsIgnoreCase("No change")) {
                        comProt.addStable((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
                    }

                } else {
                    inverted = true;

                    if (quant.getStringFCValue().equalsIgnoreCase("Decreased") || quant.getStringFCValue().equalsIgnoreCase("Decrease")) {
                        comProt.addUp((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey(), significantPValue);
                    } else if (quant.getStringFCValue().equalsIgnoreCase("Increased") || quant.getStringFCValue().equalsIgnoreCase("Increase")) {
                        comProt.addDown((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey(), significantPValue);
                    } else if (quant.getStringFCValue().equalsIgnoreCase("Not Provided")) {
                        comProt.addNoValueProvided((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
                    } else if (quant.getStringFCValue().equalsIgnoreCase("No change")) {
                        comProt.addStable((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
                    }
                }
                String uniprotAcc = quant.getUniprotAccession();
                String protName;
                String accession;
                String url;
//
                if (uniprotAcc.trim().equalsIgnoreCase("") || uniprotAcc.equalsIgnoreCase("Not Available") || uniprotAcc.equalsIgnoreCase("Entry Deleted") || uniprotAcc.equalsIgnoreCase("Entry Demerged") || uniprotAcc.equalsIgnoreCase("NOT RETRIEVED") || uniprotAcc.equalsIgnoreCase("DELETED") || uniprotAcc.trim().equalsIgnoreCase("UNREVIEWED")) {
                    protName = quant.getPublicationProteinName();
                    accession = quant.getPublicationAccNumber();
                    url = null;

                } else {
                    protName = quant.getUniprotProteinName();
                    accession = quant.getUniprotAccession();
                    url = "http://www.uniprot.org/uniprot/" + protAcc.toUpperCase();
                }
                if (protName.trim().equalsIgnoreCase("")) {
                    protName = quant.getPublicationProteinName();
                }

                comProt.setProtName(protName);
                comProt.setProteinAccssionNumber(accession);
                comProt.setUrl(url);
                comProt.setSequence(quant.getSequence());

                Set<QuantPeptide> quantPeptidesList = comProt.getQuantPeptidesList();
                for (String key : fullComparisonPeptideMap.keySet()) {

                    if (key.equalsIgnoreCase("__" + (quant.getProtKey()) + "__" + quant.getDsKey() + "__")) {
                        if (inverted) {
                            Set<QuantPeptide> updatedQuantPeptidesList = new HashSet<>();
                            fullComparisonPeptideMap.get(key).stream().map((quantPeptide) -> {
                                if (quantPeptide.getString_fc_value().equalsIgnoreCase("Increased") || quantPeptide.getString_fc_value().equalsIgnoreCase("Increase")) {

                                    quantPeptide.setString_fc_value("Decreased");

                                } else if (quantPeptide.getString_fc_value().equalsIgnoreCase("Decreased") || quantPeptide.getString_fc_value().equalsIgnoreCase("Decrease")) {
                                    quantPeptide.setString_fc_value("Increased");

                                }
                                return quantPeptide;
                            }).map((quantPeptide) -> {
                                if (quantPeptide.getFc_value() != -1000000000.0) {
                                    quantPeptide.setFc_value(quantPeptide.getFc_value() * -1.0);//((1.0 / quantPeptide.getFc_value()) * -1);
                                }
                                return quantPeptide;
                            }).forEach((quantPeptide) -> {
                                updatedQuantPeptidesList.add(quantPeptide);
                            });
                            quantPeptidesList.addAll(updatedQuantPeptidesList);

                        } else {

                            quantPeptidesList.addAll(fullComparisonPeptideMap.get(key));

                        }

                    }
                }
                Map<String, QuantProtein> dsQuantProteinsMap = comProt.getDsQuantProteinsMap();
                if (!dsQuantProteinsMap.containsKey("_-_" + quant.getDsKey() + "_-_" + comProt.getProteinAccssionNumber() + "_-_")) {
                    if (inverted) {
                        if (quant.getStringFCValue().equalsIgnoreCase("Increased") || quant.getStringFCValue().equalsIgnoreCase("Increase")) {

                            quant.setStringFCValue("Decreased");

                        } else if (quant.getStringFCValue().equalsIgnoreCase("Decreased") || quant.getStringFCValue().equalsIgnoreCase("Decrease")) {
                            quant.setStringFCValue("Increased");

                        }
                        if (quant.getFcPatientGroupIonPatientGroupII() != -1000000000.0) {
                            quant.setFcPatientGroupIonPatientGroupII(quant.getFcPatientGroupIonPatientGroupII() * -1.0);
                        }
                        String pgI = quant.getPatientGroupII();
                        String pSubGI = quant.getPatientSubGroupII();
                        String pGrIComm = quant.getPatientGrIIComment();
                        int pGrINum = quant.getPatientsGroupIINumber();

                        quant.setPatientGroupII(quant.getPatientGroupI());
                        quant.setPatientGrIIComment(quant.getPatientGrIComment());
                        quant.setPatientSubGroupII(quant.getPatientSubGroupI());
                        quant.setPatientsGroupIINumber(quant.getPatientsGroupINumber());

                        quant.setPatientGroupI(pgI);
                        quant.setPatientGrIComment(pSubGI);
                        quant.setPatientSubGroupI(pGrIComm);
                        quant.setPatientsGroupINumber(pGrINum);

                    }
                    dsQuantProteinsMap.put("_-_" + quant.getDsKey() + "_-_" + comProt.getProteinAccssionNumber() + "_-_", quant);

                } else {

                    /////for iso testing remove as soon as possible 
                    System.out.println("at major error in data dublicated keys " + ("_-_" + quant.getDsKey() + "_-_" + comProt.getProteinAccssionNumber() + "_-_"));
                    continue;
                }

                comProt.setDsQuantProteinsMap(dsQuantProteinsMap);
                comProt.setQuantPeptidesList(quantPeptidesList);
                comparProtList.put(protAcc, comProt);

            }

//            //init pep for quantProt
//            //sort the protiens map
            Map<String, QuantComparisonProtein> sortedcomparProtList = new TreeMap<>(Collections.reverseOrder());
            Map<Integer,Set<QuantComparisonProtein>>proteinsByTrendMap= new HashMap<>();
            proteinsByTrendMap.put(0, new HashSet<>());
            proteinsByTrendMap.put(1, new HashSet<>());
            proteinsByTrendMap.put(2, new HashSet<>());
            proteinsByTrendMap.put(2, new HashSet<>());
            proteinsByTrendMap.put(3, new HashSet<>());
            proteinsByTrendMap.put(4, new HashSet<>());
            proteinsByTrendMap.put(5, new HashSet<>());
            comparProtList.keySet().stream().forEach((Key) -> {
                QuantComparisonProtein temp = comparProtList.get(Key);
                sortedcomparProtList.put((temp.getSignificantUp() + "_" + Key), temp);
                temp.finalizeQuantData();
                Set<QuantComparisonProtein>set = proteinsByTrendMap.get(temp.getSignificantTrindCategory());
               
                set.add(temp);
                proteinsByTrendMap.put(temp.getSignificantTrindCategory(), set);
            });
            comparison.setQuantComparisonProteinMap(sortedcomparProtList);
            comparison.setProteinsByTrendMap(proteinsByTrendMap); 
            updatedSelectedComparisonList.add(comparison);

        }

        return updatedSelectedComparisonList;

    }

}
