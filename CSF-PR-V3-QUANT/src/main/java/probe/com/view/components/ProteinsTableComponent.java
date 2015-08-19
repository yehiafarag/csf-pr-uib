package probe.com.view.components;

import com.vaadin.data.Item;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

import com.vaadin.ui.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import probe.com.model.beans.IdentificationProteinBean;
import probe.com.view.core.CustomEmbedded;
import probe.com.view.core.CustomExternalLink;
import probe.com.view.core.CustomPI;

/**
 * @author Yehia Farag main proteins table component
 */
public class ProteinsTableComponent extends Table implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private DecimalFormat df = null;
    private Map<String, Integer> tableSearchMap = new HashMap<String, Integer>();
    private Map<String, Integer> tableSearchMapIndex = new HashMap<String, Integer>();
    private Map<Integer, Integer> tableSearchMapIndexToId = new HashMap<Integer, Integer>();

    private int firstIndex;

    /**
     * @param proteinsList
     * @param fractionNumber
     */
    public ProteinsTableComponent(Map<String, IdentificationProteinBean> proteinsList, int fractionNumber) {

        Map<String, Integer> rankMap = initRank(proteinsList);
        this.setSelectable(true);
        this.setColumnReorderingAllowed(true);
        this.setColumnCollapsingAllowed(true);
        this.setImmediate(true); // react at once when something is selected
        this.setWidth("100%");
        this.setHeight("160px");

        this.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        String Protein_Inference = "Protein Inference";
        this.addContainerProperty(Protein_Inference, CustomPI.class, null, "PI", null, Table.Align.CENTER);

        this.addContainerProperty("Accession", CustomExternalLink.class, null);

        this.addContainerProperty("Other Protein(s)", String.class, null);
        this.setColumnCollapsed("Other Protein(s)", true);

        this.addContainerProperty("Description", String.class, null);
        this.addContainerProperty("Chr", String.class, null, "CHROMOSOME", null, Table.Align.RIGHT);

        this.addContainerProperty("Gene Name", String.class, null);

        this.addContainerProperty("Sequence Coverage(%)", Double.class, null, "Coverage(%)", null, Table.Align.RIGHT);
        this.addContainerProperty("Non Enzymatic Peptides", CustomEmbedded.class, null, "Non Enzymatic Peptides", null, Table.Align.CENTER);
        Object validatedPeptide  = "# Validated Peptides";
        this.addContainerProperty(validatedPeptide, Integer.class, null, "#Peptides", null, Table.Align.RIGHT);
        this.addContainerProperty("# Validated Spectra", Integer.class, null, "#Spectra", null, Table.Align.RIGHT);
        this.addContainerProperty("NSAF", Double.class, null, "NSAF", null, Table.Align.RIGHT);
        this.addContainerProperty("RANK", Integer.class, null, "RANK (NSAF)", null, Table.Align.RIGHT);
        this.addContainerProperty("MW", Double.class, null, "MW", null, Table.Align.RIGHT);
        Object Confidence = "Confidence";
        this.addContainerProperty(Confidence, Double.class, null, Confidence.toString(), null, Table.Align.RIGHT);
        if (fractionNumber > 0) {
            this.addContainerProperty("SpectrumFractionSpread lower range_kDa", Double.class, null, "Spectrum Lower Range", null, Table.Align.RIGHT);
            this.addContainerProperty("SpectrumFractionSpread upper range kDa", Double.class, null, "Spectrum Upper Range", null, Table.Align.RIGHT);
            this.addContainerProperty("PeptideFractionSpread lower range kDa", Double.class, null, "Peptide Lower Range", null, Table.Align.RIGHT);
            this.addContainerProperty("PeptideFractionSpread upper range kDa", Double.class, null, "Peptide Upper Range", null, Table.Align.RIGHT);
            this.setColumnCollapsed("SpectrumFractionSpread lower range_kDa", true);
            this.setColumnCollapsed("SpectrumFractionSpread upper range kDa", true);
            this.setColumnCollapsed("PeptideFractionSpread lower range kDa", true);
            this.setColumnCollapsed("PeptideFractionSpread upper range kDa", true);

        }
        this.addContainerProperty("Validated", CustomEmbedded.class, null, "Validated", null, Table.Align.CENTER);
        int index = 1;
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.##", otherSymbols);
        CustomExternalLink link = null;
        CustomEmbedded nonEnz = null;
        CustomEmbedded validated = null;
        Resource res = null;
        Double d1 = null;
        Double d2 = null;
        Double d3 = null;
        Double d4 = null;
        CustomPI pi = null;
        Resource res2 = null;
        Resource res3 = null;
        for (IdentificationProteinBean pb : proteinsList.values()) {

            link = new CustomExternalLink(pb.getAccession(), "http://www.uniprot.org/uniprot/" + pb.getAccession());
            link.setDescription("UniProt link for " + pb.getAccession());

            if (pb.isNonEnzymaticPeptides()) {
                res = new ThemeResource("img/true.jpg");
            } else {
                res = new ThemeResource("img/false.jpg");
            }

            if (pb.isValidated()) {
                res3 = new ThemeResource("img/true.jpg");
            } else {
                res3 = new ThemeResource("img/false.jpg");
            }

            nonEnz = new CustomEmbedded(pb.isNonEnzymaticPeptides(), res);
            nonEnz.setWidth("16px");
            nonEnz.setHeight("16px");
            nonEnz.setDescription("" + pb.isNonEnzymaticPeptides());

            validated = new CustomEmbedded(pb.isValidated(), res3);
            validated.setWidth("16px");
            validated.setHeight("16px");
            validated.setDescription("" + pb.isValidated());

            if (pb.getProteinInferenceClass().trim().equalsIgnoreCase("SINGLE PROTEIN")) {
                res2 = new ThemeResource("img/green.jpg");
            } else if (pb.getProteinInferenceClass().trim().equalsIgnoreCase("UNRELATED PROTEINS")) {
                res2 = new ThemeResource("img/red.jpg");
            } else if (pb.getProteinInferenceClass().equalsIgnoreCase("ISOFORMS")) {
                res2 = new ThemeResource("img/yellow.jpg");
            } else if (pb.getProteinInferenceClass().trim().equalsIgnoreCase("UNRELATED ISOFORMS") || pb.getProteinInferenceClass().equalsIgnoreCase("ISOFORMS AND UNRELATED PROTEIN(S)")) {
                res2 = new ThemeResource("img/orange.jpg");
            } else if (pb.getProteinInferenceClass().trim().equalsIgnoreCase("Related Proteins")) {
                res2 = new ThemeResource("img/yellow.jpg");
            } else if (pb.getProteinInferenceClass().equalsIgnoreCase("RELATED AND UNRELATED PROTEINS")) {
                res2 = new ThemeResource("img/red.jpg");
            }

            pi = new CustomPI(pb.getProteinInferenceClass(), res2);
            pi.setDescription(pb.getProteinInferenceClass());
            try {
                d1 = Double.valueOf(pb.getSpectrumFractionSpread_lower_range_kDa());
            } catch (Exception nfx) {
                d1 = null;
            }
            try {
                d2 = Double.valueOf(pb.getSpectrumFractionSpread_upper_range_kDa());
            } catch (Exception nfx) {
                d2 = null;
            }

            try {
                d3 = Double.valueOf(pb.getPeptideFractionSpread_lower_range_kDa());
            } catch (Exception nfx) {
            }
            try {
                d4 = Double.valueOf(pb.getPeptideFractionSpread_upper_range_kDa());
            } catch (Exception nfx) {
            }

            int rank = rankMap.get(pb.getAccession() + "," + pb.getOtherProteins());
            if (fractionNumber <= 0) {
                this.addItem(new Object[]{index, pi, link, pb.getOtherProteins(), pb.getDescription(), pb.getChromosomeNumber(), pb.getGeneName(), Double.valueOf(df.format(pb.getSequenceCoverage())), nonEnz, pb.getNumberValidatedPeptides(), pb.getNumberValidatedSpectra(), Double.valueOf(df.format(pb.getNsaf())), rank, Double.valueOf(df.format(pb.getMw_kDa())), Double.valueOf(df.format(pb.getConfidence())), validated}, index);
            } else {
                this.addItem(new Object[]{index, pi, link, pb.getOtherProteins(), pb.getDescription(), pb.getChromosomeNumber(), pb.getGeneName(), Double.valueOf(df.format(pb.getSequenceCoverage())), nonEnz, pb.getNumberValidatedPeptides(), pb.getNumberValidatedSpectra(), Double.valueOf(df.format(pb.getNsaf())), rank, Double.valueOf(df.format(pb.getMw_kDa())), Double.valueOf(df.format(pb.getConfidence())), d1, d2, d3, d4, validated}, index);
            }

            index++;

        }
        try{
        this.sort(new Object[]{Confidence, validatedPeptide}, new boolean[]{false, false});
        }catch(Exception exp){exp.printStackTrace();}
        
//        this.commit();

//        this.setSortAscending(false);

        setColumnWidth("Index", 33);
        setColumnWidth("Protein_Inference", 33);
        setColumnWidth("Accession", 60);
        setColumnWidth("Chr", 36);
        setColumnWidth("Gene Name", 70);
        setColumnWidth("Sequence Coverage(%)", 75);
        setColumnWidth("Non Enzymatic Peptides", 80);
        setColumnWidth("# Validated Peptides", 58);
        setColumnWidth("# Validated Spectra", 55);
        setColumnWidth("NSAF", 35);
        setColumnWidth("RANK", 35);

        setColumnWidth(Confidence, 65);
        setColumnWidth("Validated", 55);

        for (Object propertyId : this.getSortableContainerPropertyIds()) {
            if (propertyId.toString().equals("Description")) {
                setColumnExpandRatio(propertyId, 1.0f);
            }

        }

        TreeMap<Integer, String> sortMap = new TreeMap<Integer, String>();
        int indexing = 1;
        for (Object id : this.getItemIds()) {
            Item item = this.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            if (indexing == 1) {
                firstIndex = (Integer) id;
            }
            Object accObject = item.getItemProperty("Accession").getValue();
            Object otherAccObject = item.getItemProperty("Other Protein(s)").getValue();
            Object descObject = item.getItemProperty("Description").getValue();
            sortMap.put(indexing, accObject.toString().toUpperCase().trim() + "," + otherAccObject.toString().toUpperCase().trim() + "," + descObject.toString().toUpperCase().trim() + "," + (Integer) id);
            indexing++;
        }
       

        tableSearchMap.clear();
        tableSearchMapIndex.clear();
        for (int indexValue = 1; indexValue <= sortMap.size(); indexValue++) {
            String str = sortMap.get(indexValue); 
            int itemId = Integer.valueOf(str.split(",")[str.split(",").length - 1]);
            tableSearchMap.put(str, itemId);
            tableSearchMapIndex.put(str, indexValue);
        }

    }

    /**
     * search proteins map for searching proteins by accession or description
     * (name)
     *
     * @return tableSearchMap
     */
    public Map<String, Integer> getTableSearchMap() {
        return tableSearchMap;
    }

    public Map<String, Integer> getTableSearchMapIndex() {
        return tableSearchMapIndex;
    }

    /**
     * initialize the rank column for the selected dataset
     *
     * @param proteinsList proteins list
     * @return rankMap
     */
    private Map<String, Integer> initRank(Map<String, IdentificationProteinBean> proteinsList) {
        List<IdentificationProteinBean> protList = new ArrayList<IdentificationProteinBean>();
        Map<String, Integer> rankMap = new TreeMap<String, Integer>();
        protList.addAll(proteinsList.values());
        Collections.sort(protList);
        double currentNsaf = -1;
        int rank = 0;
        for (int index = (protList.size() - 1); index >= 0; index--) {
            IdentificationProteinBean pb = protList.get(index);
            if (currentNsaf != pb.getNsaf()) {
                rank++;
            }

            rankMap.put(pb.getAccession() + "," + pb.getOtherProteins(), rank);
            currentNsaf = pb.getNsaf();

        }
        return rankMap;

    }
    
    @Override
    public void select(Object itemId){
        super.select(itemId);
        
    }
//    
    @Override
    public void setCurrentPageFirstItemIndex(int itemId)
    {
        super.setCurrentPageFirstItemId(itemId);
    }
    
//    @Override
//    public void sort(){
//    super.sort();
//            System.err.println("its  auto sorting ");
//    }
    
    

    public int getFirstIndex() {
        return firstIndex;
    }
}
