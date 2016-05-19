package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Collection;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;

/**
 *
 * @author Yehia Farag
 *
 * this class represents initial layout for quant data
 */
public abstract class QuantInitialLayout extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final HorizontalLayout miniLayout;
    private final int maxNumber;

    /**
     *
     * @param diseaseCategorySet list of disease categories for main disease
     * category panel
     * @param width body layout width in pixels
     * @param height body layout height in pixels
     */
    public QuantInitialLayout(Collection<DiseaseCategoryObject> diseaseCategorySet, int width, int height) {
        this.setWidth(height, Unit.PIXELS);
        this.setHeight(height, Unit.PIXELS);

        GridLayout frame = new GridLayout(3, 3);
        frame.setWidthUndefined();//100, Unit.PERCENTAGE);
        frame.setHeightUndefined();//100,Unit.PERCENTAGE);
        frame.setSpacing(true);
//        frame.setStyleName("border");
        this.addComponent(frame);
        this.setComponentAlignment(frame, Alignment.TOP_CENTER);

        Label title = new Label("<center Style='color:#4d749f;'>Disease Category</center>");
        title.setContentMode(ContentMode.HTML);
        title.addStyleName(ValoTheme.LABEL_H2);
        title.addStyleName(ValoTheme.LABEL_BOLD);
        frame.addComponent(title, 1, 1);
        frame.setComponentAlignment(title, Alignment.MIDDLE_CENTER);

        DiseaseCategoryObject allDeseases = (DiseaseCategoryObject) diseaseCategorySet.toArray()[diseaseCategorySet.size() - 1];
        maxNumber = allDeseases.getDatasetNumber();
        double scaledMax = scaleValues(maxNumber, maxNumber, 0.5, 0.05);
        VerticalLayout allDisease = initDiseaseBubbleLayout(allDeseases, maxNumber,scaledMax);
        frame.addComponent(allDisease, 1, 0);
        frame.setComponentAlignment(allDisease, Alignment.MIDDLE_CENTER);

//        HorizontalLayout firstLevelContainer = new HorizontalLayout();
//        firstLevelContainer.setWidth(100, Unit.PERCENTAGE);
//        frame.addComponent(firstLevelContainer);
        int rowcounter = 1;
        int colCounter = 0;
        for (DiseaseCategoryObject dCategory : diseaseCategorySet) {
            if (dCategory == allDeseases) {
                continue;
            }
             VerticalLayout disease = initDiseaseBubbleLayout(dCategory, maxNumber,scaledMax);
            frame.addComponent(disease, colCounter++, rowcounter);
            frame.setComponentAlignment(disease, Alignment.MIDDLE_CENTER);
            colCounter++;
            if(colCounter>2)
            {
                colCounter=1;
                rowcounter++;
            }

        }

        miniLayout = new HorizontalLayout();
        miniLayout.addComponent(initDiseaseLayout(diseaseCategorySet.iterator().next(), 100, 100,maxNumber));
        miniLayout.addStyleName("bigbtn");
        miniLayout.addStyleName("blink");

    }

    /**
     * initialize the disease category layout
     *
     * @param diseaseObject disease category object that has disease information
     */
    private VerticalLayout initDiseaseLayout(DiseaseCategoryObject diseaseObject, int height, int width, int max) {
        VerticalLayout diseaseLayout = new VerticalLayout();
        diseaseLayout.setWidth(width, Unit.PIXELS);
        diseaseLayout.setHeight(height, Unit.PIXELS);
        String SpacerI;
        String SpacerII;
        SpacerI = "<br/>(";
        SpacerII = ")";
        Label diseaseTitle = new Label("<center>" + diseaseObject.getDiseaseCategory() + SpacerI + diseaseObject.getDatasetNumber() +"/"+max+ SpacerII + "</center>");
        diseaseTitle.setDescription("#Datasets " + diseaseObject.getDatasetNumber());
        diseaseLayout.addComponent(diseaseTitle);
        diseaseTitle.setContentMode(ContentMode.HTML);
        diseaseLayout.setComponentAlignment(diseaseTitle, Alignment.MIDDLE_CENTER);
        diseaseLayout.setStyleName(diseaseObject.getDiseaseStyleName());

        diseaseLayout.addStyleName("pointer");
        diseaseLayout.setData(diseaseObject);

        return diseaseLayout;

    }

    private VerticalLayout initDiseaseBubbleLayout(DiseaseCategoryObject diseaseObject, int max,double scaledMax) {
        VerticalLayout diseaseLayout = new VerticalLayout();
        double width = (scaleValues(diseaseObject.getDatasetNumber(), max, 0.5, 0.05));
        width = width*90/scaledMax;
        width = width*3;
        diseaseLayout.setWidth((int) width, Unit.PIXELS);
        diseaseLayout.setHeight((int)width, Unit.PIXELS);
        String SpacerI;
        String SpacerII;

        SpacerI = "<br/>(";
        SpacerII = ")";
        diseaseLayout.addLayoutClickListener(this);

        Label diseaseTitle = new Label("<center>" + diseaseObject.getDiseaseCategory() + SpacerI + diseaseObject.getDatasetNumber()+"/"+max + SpacerII + "</center>");
        diseaseTitle.setDescription("#Datasets " + diseaseObject.getDatasetNumber());
        diseaseLayout.addComponent(diseaseTitle);
        diseaseTitle.setContentMode(ContentMode.HTML);
        diseaseLayout.setComponentAlignment(diseaseTitle, Alignment.MIDDLE_CENTER);
        diseaseLayout.setStyleName(diseaseObject.getDiseaseStyleName());

        diseaseLayout.addStyleName("pointer");
        diseaseLayout.addStyleName("bubble");
        diseaseLayout.setData(diseaseObject);

        return diseaseLayout;

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (((VerticalLayout) event.getComponent()).getData() == null) {
            return;
        }
        miniLayout.removeAllComponents();
        DiseaseCategoryObject diseaseObject = (DiseaseCategoryObject) (((VerticalLayout) event.getComponent()).getData());
        miniLayout.addComponent(initDiseaseLayout(diseaseObject, 100, 100,maxNumber));
        onClick(diseaseObject.getDiseaseCategory());

    }

    public abstract void onClick(String diseaseCategoryName);

    public HorizontalLayout getMiniLayout() {
        return miniLayout;
    }

    /**
     * Converts the value from linear scale to log scale. The log scale numbers
     * are limited by the range of the type float. The linear scale numbers can
     * be any double value.
     *
     * @param linearValue the value to be converted to log scale
     * @param max
     * @param upperLimit
     * @param lowerLimit
     * @return the value in log scale
     * @throws IllegalArgumentException if value out of range
     */
    public final double scaleValues(double linearValue, int max, double upperLimit, double lowerLimit) {
        double logMax = (Math.log(max) / Math.log(2));
        double logValue = (Math.log(linearValue + 1) / Math.log(2));
        logValue = (logValue * 2 / logMax) + lowerLimit;
        return logValue;
    }

}
