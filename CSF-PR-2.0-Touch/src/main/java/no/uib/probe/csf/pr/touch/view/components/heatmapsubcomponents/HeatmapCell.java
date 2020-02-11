package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.util.Map;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;

/**
 * This class represents heat map squared cell
 *
 * @author yehia Farag
 */
public abstract class HeatmapCell extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    /**
     * The main cell value.
     */
    private final double value;
    /**
     * The main cell value as string.
     */
    private String strValue;
    /**
     * The cell CSS cursor type.
     */
    private String pointer;
    /**
     * The main cell caption label.
     */
    private Label valueLabel;
    /**
     * The cell is selected.
     */
    private boolean selected = false;
    /**
     * Is the cell is belong for combined-renamed disease sub group.
     */
    private boolean combinedHeader = false;
    /**
     * The comparison that this cell represents.
     */
    private final QuantDiseaseGroupsComparison comparison;
    /**
     * The main disease category that that cell belong to.
     */
    private final String diseaseCategory;

    /**
     * Get cell value
     *
     * @return value Double value of the cell
     */
    public double getValue() {
        return value;
    }

    /**
     * Get the comparison that this cell represents
     *
     * @return comparison The included quant comparison
     */
    public QuantDiseaseGroupsComparison getComparison() {
        return comparison;
    }

    /**
     * Check if the cell is belong for combined-renamed disease sub group
     *
     * @return combinedHeader The cell for combined/updated disease-subgroup
     * name.
     */
    public boolean isCombinedHeader() {
        return combinedHeader;
    }

    /**
     * Constructor to initialize the main attributes
     *
     * @param value the cell value
     * @param cellColor the HTML hashed color code
     * @param diseaseCategoryColor the HTML hashed disease category color code
     * (used in quant comparison object)
     * @param datasetMap Map of dataset index and datasets objects included in
     * this cell
     * @param rowLabelIndex the row index for this cell
     * @param colLabelIndex the column index for this cell
     * @param updatedComparisonTitile the new disease sub group title
     * @param fullCompTitle the full name for the disease sub group
     * @param oreginalComparisonTitle the publication title for the comparison
     * @param heatmapCellWidth the width of the cell (used as width and height)
     * @param publicationsNumber the number of publication for this disease
     * sub-group comparisons
     *
     * @param diseaseCategory the disease category for the comparison
     * @param diseaseCategoryStyle the CSS style for the disease category
     *
     *
     */
    public HeatmapCell(boolean collapsedHeader , double value, final String cellColor, String diseaseCategoryColor, Map<Integer, QuantDataset> datasetMap, final int rowLabelIndex, final int colLabelIndex, int heatmapCellWidth, int publicationsNumber, String updatedComparisonTitile, String fullCompTitle, String oreginalComparisonTitle, String diseaseCategory, String diseaseCategoryStyle) {

        this.value = value;

        this.diseaseCategory = diseaseCategory;
        this.comparison = new QuantDiseaseGroupsComparison();
        comparison.setDiseaseCategory(diseaseCategory);
        comparison.setComparisonFullName(fullCompTitle);
        comparison.setComparisonHeader(updatedComparisonTitile);
        comparison.setOreginalComparisonHeader(oreginalComparisonTitle);
        comparison.setDiseaseCategoryColor(diseaseCategoryColor);
        comparison.setDatasetMap(datasetMap);
        comparison.setDiseaseCategoryStyle(diseaseCategoryStyle);
        strValue = "";
        pointer = "default";
        if (cellColor.equalsIgnoreCase("#EFF2FB") && value != 0 && !collapsedHeader ) {
            strValue = ((int) value) + "*";
            this.updateLabel(strValue);
            String[] gr = fullCompTitle.replace("__" + diseaseCategory, "").split(" / ");
            this.setDescription("Same type comparison <br/>"
                    + "Numerator: " + gr[0] + "<br/>"
                    + "Denominator: " + gr[1]
                    + "<br/>Disease: " + diseaseCategory + "<br/>#Datasets: " + strValue + "<br/>#Publications: " + publicationsNumber);
            comparison.setComparisonHeader(" / ");
            comparison.setOreginalComparisonHeader(" / ");
            combinedHeader = true;

            this.valueLabel = new Label();
            this.valueLabel.setWidth(100, Unit.PERCENTAGE);
            this.valueLabel.setHeight(100, Unit.PERCENTAGE);
            this.valueLabel.setContentMode(ContentMode.HTML);
            this.addComponent(valueLabel);
            this.setComponentAlignment(valueLabel, Alignment.TOP_CENTER);
            this.valueLabel.setStyleName("hmbodycell");
            this.valueLabel.setValue("<div style='background:" + cellColor + "; width:" + (100) + "% !important; height:" + (100) + "% !important; color: #474747; font-weight: 600; font-size: 10px;'>" + strValue + "</div>");

        } else if (cellColor.equalsIgnoreCase("#EFF2FB") && value != 0 && collapsedHeader) {
//            cellColor="#e3e304";
            strValue = ((int) value) + "";//+ "*";
            this.updateLabel(strValue);
            String[] gr = fullCompTitle.replace("__" + diseaseCategory, "").split(" / ");
            this.setDescription("Collapsed Disease Category (click to expand)<br/>"
                    + "Numerator: " + gr[0] + "<br/>"
                    + "Denominator: " + gr[1]
                    + "<br/>Disease: " + diseaseCategory + "<br/>#Datasets: " + strValue + "<br/>#Publications: " + publicationsNumber);
            comparison.setComparisonHeader(" / ");
            comparison.setOreginalComparisonHeader(" / ");
            combinedHeader = true;

            this.valueLabel = new Label();
            this.valueLabel.setWidth(100, Unit.PERCENTAGE);
            this.valueLabel.setHeight(100, Unit.PERCENTAGE);
            this.valueLabel.setContentMode(ContentMode.HTML);
            this.addComponent(valueLabel);
            this.setComponentAlignment(valueLabel, Alignment.TOP_CENTER);
            this.valueLabel.setStyleName("hmbodycell");
            this.valueLabel.setValue("<div style='background:" + "#e3e304" + "; width:" + (100) + "% !important; height:" + (100) + "% !important; color: #474747; font-weight: 600; font-size: 12px;'>" + strValue + "</div>");

        } else if (value != 0) {

            this.valueLabel = new Label();
            this.valueLabel.setWidth(100, Unit.PERCENTAGE);
            this.valueLabel.setHeight(100, Unit.PERCENTAGE);
            this.valueLabel.setContentMode(ContentMode.HTML);
            this.addComponent(valueLabel);
            this.setComponentAlignment(valueLabel, Alignment.TOP_CENTER);
            this.valueLabel.setStyleName("hmbodycell");
            this.valueLabel.setValue("<div style='background:" + cellColor + "; width:" + (100) + "% !important; height:" + (100) + "% !important;'>" + (int) value + "</div>");

            strValue = ((int) value) + "";
            pointer = "pointer";

            this.updateLabel(strValue);

        } else if (cellColor.equalsIgnoreCase("#EFF2FB")) {
            strValue = " ";

            this.valueLabel = new Label();
            this.valueLabel.setWidth(100, Unit.PERCENTAGE);
            this.valueLabel.setHeight(100, Unit.PERCENTAGE);
            this.valueLabel.setContentMode(ContentMode.HTML);
            this.addComponent(valueLabel);
            this.setComponentAlignment(valueLabel, Alignment.TOP_CENTER);
            this.valueLabel.setStyleName("hmbodycell");
            this.valueLabel.setValue("<div style='background:" + cellColor + "; width:" + (100) + "% !important; height:" + (100) + "% !important;'>" + strValue + "</div>");

        } else {
            this.updateLabel(strValue);
        }

        if (value > 0 && !cellColor.equalsIgnoreCase("#EFF2FB")) {
            String[] gr = comparison.getComparisonFullName().replace("__" + diseaseCategory, "").split(" / ");
            HeatmapCell.this.setDescription("Numerator: " + gr[0] + "<br/>Denominator: " + gr[1]
                    + "<br/>Disease: " + diseaseCategory + " <br/>#Datasets: " + strValue + "<br/>#Publications: " + publicationsNumber);
        }
        if (!strValue.contains("*")) {
            HeatmapCell.this.addStyleName(pointer);
            HeatmapCell.this.addLayoutClickListener(HeatmapCell.this);
        }
    }

    /**
     * On click the cell select/un-select
     *
     * @param event user selection event
     */
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (selected) {
            this.unselect();
            unSelectData(this);

        } else {
            this.select();
            selectData(this);
        }
    }

    /**
     * Un-select the cell.
     */
    public void unselect() {
        selected = false;
        this.removeStyleName("hmselectedcell");
        this.addStyleName("hmunselectedcell");

    }

    /**
     * Select the cell.
     */
    public void select() {
        selected = true;
        this.removeStyleName("hmunselectedcell");
        this.addStyleName("hmselectedcell");
    }

    /**
     * Rest cell style to initial state.
     */
    public void resetCell() {
        selected = false;
        this.removeStyleName("hmunselectedcell");
        this.removeStyleName("hmselectedcell");

    }

    /**
     * Update cell label value.
     *
     * @param strValue String value for the cell.
     */
    public final void updateLabel(String strValue) {
        this.strValue = strValue;
    }

    /**
     *
     * Update the system (select) using the cell object (HeatmapCell.this)
     *
     * @param cell Heat-map cell
     */
    public abstract void selectData(HeatmapCell cell);

    /**
     * Update the system (un-select) using the cell object (HeatmapCell.this)
     *
     * @param cell Heat-map cell
     */
    public abstract void unSelectData(HeatmapCell cell);

    /**
     * Get the main disease category that that cell belong to
     *
     * @return diseaseCategory Disease category name
     */
    public String getDiseaseCategory() {
        return diseaseCategory;
    }

    /**
     * Set the height and width and update cell value label height as well as
     * the font size of the labels
     *
     * @param height Cell height
     * @param unit Height unit
     */
    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(height, unit);
        if (valueLabel == null) {
            return;
        }
        valueLabel.removeStyleName("linehight200");
        valueLabel.removeStyleName("linehight180");

        if (height < 15) {
            valueLabel.addStyleName("xxsmallfont");
            valueLabel.addStyleName("linehight180");
        } else if (height >= 15 && height < 20) {
            valueLabel.addStyleName("xsmallfont");
            valueLabel.addStyleName("linehight180");
        } else {
            valueLabel.addStyleName("smallfont");
            valueLabel.addStyleName("linehight200");
        }
        valueLabel.setHeight(height - 2, unit);

    }

}
