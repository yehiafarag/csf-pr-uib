package probe.com.model.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class FractionBean implements Serializable{
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
	
	public void setFractionId(int fractionId) {
		this.fractionId = fractionId;
	}
	public int getFractionId() {
		return fractionId;
	}	
	
	public List<String> getProteins() {
		return proteins;
	}
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
	public int getFractionIndex() {
		return fractionIndex;
	}
	public void setFractionIndex(int fractionIndex) {
		this.fractionIndex = fractionIndex;
	}

   

    public Map<String,IdentificationProteinBean> getProteinList() {
        return proteinList;
    }

    public void setProteinList(Map<String,IdentificationProteinBean> proteinList) {
        this.proteinList = proteinList;
    }

}
