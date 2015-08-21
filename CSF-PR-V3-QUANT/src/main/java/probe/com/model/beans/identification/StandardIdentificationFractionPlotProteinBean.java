package probe.com.model.beans.identification;

import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public class StandardIdentificationFractionPlotProteinBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private double MW_kDa;
    private int lowerFraction;
    private int upperFraction;
    private String fractionIndicator;
    private String name;
    private String color;
    private boolean theoretical;

    /**
     *
     * @return
     */
    public int getLowerFraction() {
        return lowerFraction;
    }

    /**
     *
     * @param lowerFraction
     */
    public void setLowerFraction(int lowerFraction) {
        this.lowerFraction = lowerFraction;
    }

    /**
     *
     * @return
     */
    public double getMW_kDa() {
        return MW_kDa;
    }

    /**
     *
     * @param mW_kDa
     */
    public void setMW_kDa(double mW_kDa) {
        MW_kDa = mW_kDa;
    }

    /**
     *
     * @return
     */
    public int getUpperFraction() {
        return upperFraction;
    }

    /**
     *
     * @param upperFraction
     */
    public void setUpperFraction(int upperFraction) {
        this.upperFraction = upperFraction;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getColor() {
        return color;
    }

    /**
     *
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return this.name + " " + this.color;
    }

    /**
     *
     * @return
     */
    public boolean isTheoretical() {
        return theoretical;
    }

    /**
     *
     * @param theoretical
     */
    public void setTheoretical(boolean theoretical) {
        this.theoretical = theoretical;
    }

    /**
     *
     * @return
     */
    public String getFractionIndicator() {
        return fractionIndicator;
    }

    /**
     *
     * @param fractionIndicator
     */
    public void setFractionIndicator(String fractionIndicator) {
        this.fractionIndicator = fractionIndicator;
    }
}
