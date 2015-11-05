package probe.com.model.beans.quant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * this class represent the publication study object that has all publication
 * information
 *
 * @author Yehia Farag
 */
public class QuantDatasetObject implements Serializable, Comparable {

        
    private int x;
    private int y;
    
    private int dsKey;

    public String getDiseaseCategory() {
        return  values[27].toString();
    }

    public void setDiseaseCategory(String diseaseCategory) {
        values[27] = diseaseCategory;
        valuesMap.put("diseaseCategory", diseaseCategory);
    }

    /**
     *
     * @return
     */
    public Object[] getValues() {
        return values;
    }
    private final Object[] values = new Object[28];
    private final Map<String, Object> valuesMap = new HashMap<String, Object>();
    private String uniqueValues;
    private final String[] headers = new String[]{"Author", "Year", "#Identified Proteins", "#Quantified Proteins", "Disease Groups", "Raw Data", "#Files", "Study Type", "Sample Type", "Sample Matching", "Shotgun/Targeted", "Technology", "Analytical Approach", "Enzyme", "Quantification Basis", "Quantification Basis Comment","Normalization Strategy", "PumedID","Patients Gr.I", "#Patients Gr.I", "Patients Gr.I Comments", "Patients Sub-Gr.I", "Patients Gr.II","#Patients Gr.II", "Patients Gr.II Comments", "Patients Sub-Gr.II", "Additional Comments","Disease Category"};

    /**
     *
     * @param index
     * @return
     */
    public String getFilterTitle(int index) {
        return headers[index];
    }

    /**
     *
     * @return
     */
    public String getAuthor() {
        return values[0].toString();
    }

    /**
     *
     * @param author
     */
    public void setAuthor(String author) {
        values[0] = author;
        valuesMap.put("author", author);
    }

    /**
     *
     * @return
     */
    public int getYear() {
        return (Integer) values[1];
    }

    /**
     *
     * @param year
     */
    public void setYear(int year) {
        values[1] = year;
        valuesMap.put("year", year);
    }

    /**
     *
     * @return
     */
    public int getIdentifiedProteinsNumber() {
        return (Integer) values[2];
    }

    /**
     *
     * @param identifiedProteinsNumber
     */
    public void setIdentifiedProteinsNumber(int identifiedProteinsNumber) {
        values[2] = identifiedProteinsNumber;
        valuesMap.put("identifiedProteinsNumber", identifiedProteinsNumber);
    }

    /**
     *
     * @return
     */
    public int getQuantifiedProteinsNumber() {
        return (Integer) values[3];
    }

    /**
     *
     * @param quantifiedProteinsNumber
     */
    public void setQuantifiedProteinsNumber(int quantifiedProteinsNumber) {
        values[3] = quantifiedProteinsNumber;
        valuesMap.put("quantifiedProteinsNumber", quantifiedProteinsNumber);
    }

    /**
     *
     * @return
     */
    public String getAnalyticalMethod() {
        return values[4].toString();
    }

    /**
     *
     * @param analyticalMethod
     */
    public void setAnalyticalMethod(String analyticalMethod) {
        values[4] = analyticalMethod;
        valuesMap.put("analytical_method", analyticalMethod);
    }

    /**
     *
     * @return
     */
    public String getRawDataUrl() {
        return values[5].toString();
    }

    /**
     *
     * @param rawDataUrl
     */
    public void setRawDataUrl(String rawDataUrl) {
        values[5] = rawDataUrl;
        valuesMap.put("rawDataUrl", rawDataUrl);
    }

    /**
     *
     * @return
     */
    public int getFilesNumber() {
        return (Integer) values[6];
    }

    /**
     *
     * @param filesNumber
     */
    public void setFilesNumber(int filesNumber) {
        values[6] = filesNumber;
        valuesMap.put("filesNumber", filesNumber);
    }

    /**
     *
     * @return
     */
    public String getTypeOfStudy() {
        return values[7].toString();
    }

    /**
     *
     * @param typeOfStudy
     */
    public void setTypeOfStudy(String typeOfStudy) {
        values[7] = typeOfStudy;
        valuesMap.put("typeOfStudy", typeOfStudy);
    }

    /**
     *
     * @return
     */
    public String getSampleType() {
        return values[8].toString();
    }

    /**
     *
     * @param sampleType
     */
    public void setSampleType(String sampleType) {
        values[8] = sampleType;
        valuesMap.put("sampleType", sampleType);
    }

    /**
     *
     * @return
     */
    public String getSampleMatching() {
        return values[9].toString();
    }

    /**
     *
     * @param sampleMatching
     */
    public void setSampleMatching(String sampleMatching) {
        values[9] = sampleMatching;
        valuesMap.put("sampleMatching", sampleMatching);
    }

    /**
     *
     * @return
     */
    public String getShotgunTargeted() {
        return values[10].toString();
    }

    /**
     *
     * @param shotgunTargeted
     */
    public void setShotgunTargeted(String shotgunTargeted) {
        values[10] = shotgunTargeted;
        valuesMap.put("shotgunTargeted", shotgunTargeted);
    }

    /**
     *
     * @return
     */
    public String getTechnology() {
        return values[11].toString();
    }

    /**
     *
     * @param technology
     */
    public void setTechnology(String technology) {
        values[11] = technology;
        valuesMap.put("technology", technology);
    }

    /**
     *
     * @return
     */
    public String getAnalyticalApproach() {
        return values[12].toString();
    }

    /**
     *
     * @param analyticalApproach
     */
    public void setAnalyticalApproach(String analyticalApproach) {
        values[12] = analyticalApproach;
        valuesMap.put("analyticalApproach", analyticalApproach);
    }

    /**
     *
     * @return
     */
    public String getEnzyme() {
        return values[13].toString();
    }

    /**
     *
     * @param enzyme
     */
    public void setEnzyme(String enzyme) {
        values[13] = enzyme;
        valuesMap.put("enzyme", enzyme);
    }

    /**
     *
     * @return
     */
    public String getQuantificationBasis() {
        return values[14].toString();
    }

    /**
     *
     * @param quantificationBasis
     */
    public void setQuantificationBasis(String quantificationBasis) {
        values[14] = quantificationBasis;
        valuesMap.put("quantificationBasis", quantificationBasis);
    }

    /**
     *
     * @return
     */
    public String getQuantBasisComment() {
        return values[15].toString();
    }

    /**
     *
     * @param quantBasisComment
     */
    public void setQuantBasisComment(String quantBasisComment) {
        values[15] = quantBasisComment;
        valuesMap.put("quantBasisComment", quantBasisComment);
    }

    /**
     *
     * @return
     */
    public String getNormalizationStrategy() {
        return values[16].toString();
    }

    /**
     *
     * @param normalizationStrategy
     */
    public void setNormalizationStrategy(String normalizationStrategy) {

        values[16] = normalizationStrategy;
        valuesMap.put("normalizationStrategy", normalizationStrategy);
    }

    /**
     *
     * @return
     */
    public String getPumedID() {
        return values[17].toString();
    }

    /**
     *
     * @param pumedID
     */
    public void setPumedID(String pumedID) {
        values[17] = pumedID;
        valuesMap.put("pumedID", pumedID);
    }
      
    /**
     *
     * @return
     */
    public String getPatientsGroup1() {
        return (String) values[18];
    }

    /**
     *
     * @param patientsGroup1
     */
    public void setPatientsGroup1(String patientsGroup1) {
        values[18] = patientsGroup1;
        valuesMap.put("patientsGroup1", patientsGroup1);
    }

    /**
     *
     * @return
     */
    public int getPatientsGroup1Number() {
        return (Integer) values[19];
    }

    /**
     *
     * @param patientsGroup1Number
     */
    public void setPatientsGroup1Number(int patientsGroup1Number) {
        values[19] = patientsGroup1Number;
        valuesMap.put("patientsGroup1Number", patientsGroup1Number);
    }

    /**
     *
     * @return
     */
    public String getPatientsGroup1Comm() {
        return (String) values[20];
    }

    /**
     *
     * @param patientsGroup1Comm
     */
    public void setPatientsGroup1Comm(String patientsGroup1Comm) {
        values[20] = patientsGroup1Comm;
        valuesMap.put("patientsGroup1Comm", patientsGroup1Comm);
    }

    /**
     *
     * @return
     */
    public String getPatientsSubGroup1() {
        return (String) values[21];
    }

    /**
     *
     * @param patientsSubGroup1
     */
    public void setPatientsSubGroup1(String patientsSubGroup1) {
        values[21] = patientsSubGroup1;
        valuesMap.put("patientsSubGroup1", patientsSubGroup1);
    }

    /**
     *
     * @return
     */
    public String getPatientsGroup2() {
        return (String) values[22];
    }

    /**
     *
     * @param patientsGroup2
     */
    public void setPatientsGroup2(String patientsGroup2) {
        values[22] = patientsGroup2;
        valuesMap.put("patientsGroup2", patientsGroup2);
    }

    /**
     *
     * @return
     */
    public int getPatientsGroup2Number() {
        return (Integer) values[23];
    }

    /**
     *
     * @param patientsGroup2Number
     */
    public void setPatientsGroup2Number(int patientsGroup2Number) {
        values[23] = patientsGroup2Number;
        valuesMap.put("patientsGroup2Number", patientsGroup2Number);
    }

    /**
     *
     * @return
     */
    public String getPatientsGroup2Comm() {
        return (String) values[24];
    }

    /**
     *
     * @param patientsGroup2Comm
     */
    public void setPatientsGroup2Comm(String patientsGroup2Comm) {
        values[24] = patientsGroup2Comm;
        valuesMap.put("patientsGroup2Comm", patientsGroup2Comm);
    }

    /**
     *
     * @return
     */
    public String getPatientsSubGroup2() {
        return (String) values[25];
    }

    /**
     *
     * @param patientsSubGroup2
     */
    public void setPatientsSubGroup2(String patientsSubGroup2) {
        values[25] = patientsSubGroup2;
        valuesMap.put("patientsSubGroup2", patientsSubGroup2);
    }

    /**
     *
     * @param additionalComments
     */
    public void setAdditionalcomments(String additionalComments) {
        values[26] = additionalComments;
        valuesMap.put("additionalComments", additionalComments);
    }

    /**
     *
     * @return
     */
    public String getAdditionalcomments() {
        return (String) values[26];
    }

    @Override
    public int compareTo(Object t) {
        if ((Integer) ((QuantDatasetObject) t).values[1] > (Integer) this.values[1]) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     *
     * @param propertyName
     * @return
     */
    public Object getProperty(String propertyName) {
        return valuesMap.get(propertyName);
    }

//    

    /**
     *
     * @param propertyIndex
     * @return
     */
        public Object getProperty(int propertyIndex) {
        if (propertyIndex < values.length) {
            return values[propertyIndex];
        }

        return null;

    }

    /**
     *
     * @param propertyIndex
     * @param value
     */
    public void setProperty(int propertyIndex,Object value) {
        if (propertyIndex >= values.length) {
            return ;
        }
         
        values[propertyIndex] = value;

    }

    /**
     *
     * @return
     */
    public String getUniqueValues() {
        return uniqueValues;
    }

    /**
     *
     * @param uniqueValues
     */
    public void setUniqueValues(String uniqueValues) {
        this.uniqueValues = uniqueValues;
    }

    /**
     *
     * @return
     */
    public int getDsKey() {
        return dsKey;
    }

    /**
     *
     * @param dsKey
     */
    public void setDsKey(int dsKey) {
        this.dsKey = dsKey;
    }

    /**
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

}
