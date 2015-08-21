package probe.com.model.beans.identification;

import probe.com.model.beans.identification.IdentificationProteinBean;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Yehia Farag
 */
public class IdentificationFractionBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int fractionId;	
	private Map<String,IdentificationProteinBean> proteinList ; //the key is protein Accession  will be used for insertion only
	private List<String>proteins;
//	private double minRange;
//	private double maxRange;
	private int fractionIndex;
	
    /**
     *
     * @param fractionId
     */
    public void setFractionId(int fractionId) {
		this.fractionId = fractionId;
	}

    /**
     *
     * @return
     */
    public int getFractionId() {
		return fractionId;
	}	
	
    /**
     *
     * @return
     */
    public List<String> getProteins() {
		return proteins;
	}

    /**
     *
     * @param proteins
     */
    public void setProteins(List<String> proteins) {
		this.proteins = proteins;
	}
//	public double getMinRange() {
//		return minRange;
//	}
//	public void setMinRange(double minRange) {
//		this.minRange = minRange;
//	}
//	public double getMaxRange() {
//		return maxRange;
//	}
//	public void setMaxRange(double maxRange) {
//		this.maxRange = maxRange;
//	}

    /**
     *
     * @return
     */
    	public int getFractionIndex() {
		return fractionIndex;
	}

    /**
     *
     * @param fractionIndex
     */
    public void setFractionIndex(int fractionIndex) {
		this.fractionIndex = fractionIndex;
	}

    /**
     *
     * @return
     */
    public Map<String,IdentificationProteinBean> getProteinList() {
        return proteinList;
    }

    /**
     *
     * @param proteinList
     */
    public void setProteinList(Map<String,IdentificationProteinBean> proteinList) {
        this.proteinList = proteinList;
    }

}
