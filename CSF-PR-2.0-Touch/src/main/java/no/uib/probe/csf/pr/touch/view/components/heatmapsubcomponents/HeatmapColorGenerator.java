package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

/**
 * This class is responsible for generating Color Gradient as HTML hashed color
 * code for the heat map
 *
 * @author Yehia Farag
 */
public class HeatmapColorGenerator {

    private final double max;

    /**
     * Constructor to initialize the main attributes
     *
     * @param max the maximum value
     */
    public HeatmapColorGenerator(double max) {

        this.max = max;
    }

    /**
     * Get the color for the input value
     *
     * @param value double value to be converted to color
     * @return HTML hashed color for the input value
     */
    public String getColor(double value) {
        if (value == 0) {
            return "RGB(" + 255 + "," + 255 + "," + 255 + ")";
        }
        double n = (value) / max;
        int R = 49;
        int G = (int) (46.0 + (146.0 - (146.0 * n)));
        int B = 229;
        return "RGB(" + R + "," + G + "," + B + ")";
    }
}
