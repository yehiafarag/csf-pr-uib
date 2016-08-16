package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.view.core.ResizableTextLabel;

/**
 *
 * @author Yehia Farag
 *
 * this class represents initial layout for quant data
 */
public abstract class QuantInitialLayout extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final HorizontalLayout miniLayout;
    private final int maxNumber;
    private Map<String, DiseaseCategoryObject> diseaseMap;
    private final boolean smallScreen;

    /**
     *
     * @param diseaseCategorySet list of disease categories for main disease
     * category panel
     * @param width body layout width in pixels
     * @param height body layout height in pixels
     */
    public QuantInitialLayout(Collection<DiseaseCategoryObject> diseaseCategorySet, int width, int height, boolean smallScreen) {

        this.setWidth(width, Unit.PIXELS);
        this.setHeight(height, Unit.PIXELS);
        this.addStyleName("slowslide");
        this.diseaseMap = new HashMap<>();

        GridLayout frame = new GridLayout(3, 3);
        frame.setSpacing(true);
        frame.setMargin(true);
//        frame.setStyleName("border");
        this.addComponent(frame);
        this.setComponentAlignment(frame, Alignment.TOP_CENTER);

        ResizableTextLabel title = new ResizableTextLabel("<center Style='color:#4d749f;'>Disease Category</center>");
        title.setContentMode(ContentMode.HTML);
        title.addStyleName(ValoTheme.LABEL_H3);
        title.setWidth(250, Unit.PIXELS);
//        title.addStyleName(ValoTheme.LABEL_BOLD);
        frame.addComponent(title, 1, 1);
        frame.setComponentAlignment(title, Alignment.MIDDLE_CENTER);

        DiseaseCategoryObject allDeseases = (DiseaseCategoryObject) diseaseCategorySet.toArray()[diseaseCategorySet.size() - 1];
        maxNumber = allDeseases.getDatasetNumber();
        diseaseMap.put("All Diseases", allDeseases);

//        System.out.println("at h " + height + "  w " + width + "  scaledMax " + scaledMax);
        int rowcounter = 0;
        int colCounter = 1;
        for (DiseaseCategoryObject dCategory : diseaseCategorySet) {
            if (dCategory == allDeseases) {
                continue;
            }

            double scaledMax = scaleValues(dCategory.getDatasetNumber(), maxNumber, 0.01, 1);
            int dia = (int) (scaledMax * 0.1 * height);

            VerticalLayout disease = initDiseaseBubbleLayout(dCategory, maxNumber, dia);
            diseaseMap.put(dCategory.getDiseaseCategory(), dCategory);
            frame.addComponent(disease, colCounter++, rowcounter);
            frame.setComponentAlignment(disease, Alignment.MIDDLE_CENTER);
            colCounter++;
            if (rowcounter == 0) {
                colCounter = 0;
                rowcounter = 1;
            }

        }

        double scaledMax = scaleValues(allDeseases.getDatasetNumber(), maxNumber, 0.01, 1);
        int dia = (int) (scaledMax * 0.1 * height);
        VerticalLayout allDisease = initDiseaseBubbleLayout(allDeseases, maxNumber, dia);
        frame.addComponent(allDisease, 1, 2);
        frame.setComponentAlignment(allDisease, Alignment.MIDDLE_CENTER);

        miniLayout = new HorizontalLayout();
        if (smallScreen) {
            miniLayout.addComponent(initDiseaseLayout(null, 60, 60, maxNumber));
        } else {
            miniLayout.addComponent(initDiseaseLayout(null, 100, 100, maxNumber));
        }
        miniLayout.addStyleName("bigbtn");
        miniLayout.addStyleName("blink");
        this.smallScreen = smallScreen;

    }
    private final ThemeResource logoRes = new ThemeResource("img/logo.png");

    /**
     * initialize the disease category layout
     *
     * @param diseaseObject disease category object that has disease information
     */
    private VerticalLayout initDiseaseLayout(DiseaseCategoryObject diseaseObject, int height, int width, int max) {
        VerticalLayout diseaseLayout = new VerticalLayout();
        diseaseLayout.setWidth(width, Unit.PIXELS);
        diseaseLayout.setHeight(height, Unit.PIXELS);

        if (diseaseObject == null) {
            Image img = new Image();
            img.setWidth(100, Unit.PERCENTAGE);
            img.setHeight(100, Unit.PERCENTAGE);
            img.setSource(logoRes);
            diseaseLayout.addComponent(img);
            diseaseLayout.setEnabled(false);
        } else {

            String SpacerI;
            String SpacerII;
            SpacerI = "<br/>(";
            SpacerII = ")";
            ResizableTextLabel diseaseTitle = new ResizableTextLabel("<center>" + diseaseObject.getDiseaseCategory() + SpacerI + diseaseObject.getDatasetNumber() + "/" + max + SpacerII + "</center>");
            if (height >= 60 && height <= 80) {
                diseaseTitle.addStyleName("xsmallfont");
            } else {
                diseaseTitle.addStyleName("smallfont");
            }
             diseaseTitle.addStyleName("padding2");
            diseaseTitle.setDescription("#Datasets: " + diseaseObject.getDatasetNumber());

            diseaseLayout.addComponent(diseaseTitle);
            diseaseTitle.setContentMode(ContentMode.HTML);
            diseaseLayout.setComponentAlignment(diseaseTitle, Alignment.MIDDLE_CENTER);
            diseaseLayout.setStyleName(diseaseObject.getDiseaseStyleName());

            diseaseLayout.addStyleName("pointer");
            diseaseLayout.setData(diseaseObject);
        }

        return diseaseLayout;

    }

    private VerticalLayout initDiseaseBubbleLayout(DiseaseCategoryObject diseaseObject, int max, int dia) {
        VerticalLayout diseaseLayout = new VerticalLayout();
        diseaseLayout.setHeight((int) dia, Unit.PIXELS);
        diseaseLayout.setWidth((int) dia, Unit.PIXELS);

        String SpacerI;
        String SpacerII;

        SpacerI = "<br/>(";
        SpacerII = ")";
        diseaseLayout.addLayoutClickListener(this);

        ResizableTextLabel diseaseTitle = new ResizableTextLabel("<center>" + diseaseObject.getDiseaseCategory() + SpacerI + diseaseObject.getDatasetNumber() + "/" + max + SpacerII + "</center>");
        diseaseTitle.setDescription("#Datasets: " + diseaseObject.getDatasetNumber());
        diseaseLayout.addComponent(diseaseTitle);
        diseaseTitle.setContentMode(ContentMode.HTML);
          if (dia >= 60 && dia <= 80) {
                diseaseTitle.addStyleName("xsmallfont");
            } else {
                diseaseTitle.addStyleName("smallfont");
            }
          diseaseTitle.addStyleName("padding2");
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
//        this.addStyleName("hidelayout");
        miniLayout.removeAllComponents();
        DiseaseCategoryObject diseaseObject = (DiseaseCategoryObject) (((VerticalLayout) event.getComponent()).getData());
        if (smallScreen) {
            miniLayout.addComponent(initDiseaseLayout(diseaseObject, 60, 60, maxNumber));
        } else {
            miniLayout.addComponent(initDiseaseLayout(diseaseObject, 100, 100, maxNumber));
        }

        onClick(diseaseObject.getDiseaseCategory());

    }

    public void updateSelection(String diseaseCategory) {

        miniLayout.removeAllComponents();
        DiseaseCategoryObject diseaseObject = diseaseMap.get(diseaseCategory);
        if (smallScreen) {
            miniLayout.addComponent(initDiseaseLayout(diseaseObject, 60, 60, maxNumber));
        } else {
            miniLayout.addComponent(initDiseaseLayout(diseaseObject, 100, 100, maxNumber));
        }
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
