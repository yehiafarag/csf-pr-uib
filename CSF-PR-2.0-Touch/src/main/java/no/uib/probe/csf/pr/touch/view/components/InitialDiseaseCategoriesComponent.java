package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.view.core.ResizableTextLabel;

/**
 * This class represents initial disease categories layout for quant data where
 * users can select the main disease category bubble OoO
 *
 * @author Yehia Farag
 */
public abstract class InitialDiseaseCategoriesComponent extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    /**
     * The thumb image container to be updated with disease category selection.
     */
    private final HorizontalLayout thumbImgLayout;
    /**
     * The maximum number of datasets available in the current system (used for
     * calculating the size of each bubble).
     */
    private int maxNumber;
    /**
     * Map of disease categories and its title.
     */
    private final Map<String, DiseaseCategoryObject> diseaseCategoryMap;
    /**
     * Main layout that contains the disease category bubbles and select data
     * label.
     */
    private final GridLayout frame;
    /**
     * The main component height.
     */
    private final int height;
    /**
     * The main component width.
     */
    private final int width;
    /**
     * List of disease category objects that has disease category information.
     */
    private final Collection<DiseaseCategoryObject> diseaseCategorySet;
    /**
     * Text label "select disease category".
     */
    private final ResizableTextLabel title;
    /**
     * Resource for default thumb button logo.
     */
    private final ThemeResource logoRes = new ThemeResource("img/logo.png");

    /**
     * *
     * Constructor to initialize the main attributes
     *
     * @param diseaseCategorySet list of disease categories for main disease
     * category panel
     * @param width body layout width in pixels
     * @param height body layout height in pixels
     */
    public InitialDiseaseCategoriesComponent(Collection<DiseaseCategoryObject> diseaseCategorySet, int width, int height) {

        this.diseaseCategorySet = diseaseCategorySet;
        this.height = height;
        this.width = width - 150;
        this.setWidth(this.width, Unit.PIXELS);
        this.setHeight(height, Unit.PIXELS);
        this.addStyleName("slowslide");
        this.diseaseCategoryMap = new HashMap<>();

        frame = new GridLayout(3, 3);
        frame.setSpacing(true);
        frame.setMargin(true);
        frame.setStyleName("margintop3per");
        this.addComponent(frame);
        this.setComponentAlignment(frame, Alignment.BOTTOM_CENTER);

        title = new ResizableTextLabel("<center Style='color:#4d749f;'>Disease Category</center>");
        title.setContentMode(ContentMode.HTML);
        title.addStyleName(ValoTheme.LABEL_H3);
        title.setWidth(250, Unit.PIXELS);
//        frame.addComponent(title, 1, 1);
//        frame.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
        thumbImgLayout = new HorizontalLayout();
        updateData(diseaseCategorySet);
        DiseaseCategoryObject multipleDiseaseCategory = new DiseaseCategoryObject();
        multipleDiseaseCategory.setDiseaseStyleName("alldiseasestyle");
        multipleDiseaseCategory.setDiseaseCategory("Multiple Diseases");
        diseaseCategoryMap.put("Multiple Diseases", multipleDiseaseCategory);

        Label clickcommentLabel = new Label("Click disease category to select data");
        clickcommentLabel.setStyleName(ValoTheme.LABEL_SMALL);
        clickcommentLabel.addStyleName(ValoTheme.LABEL_TINY);
        clickcommentLabel.addStyleName("italictext");
        clickcommentLabel.addStyleName("bubblelabels");
        clickcommentLabel.setWidth(260, Unit.PIXELS);

        this.addComponent(clickcommentLabel);
        this.setComponentAlignment(clickcommentLabel, Alignment.BOTTOM_RIGHT);

    }

    public void updateThumbLabel(int dsNumber, String diseaseCategory) {
        if (dsNumber == 0) {
            this.resetThumbBtn();
            return;
        }
        if (diseaseCategory.equalsIgnoreCase("No Diseases")) {
            return;
        }
        DiseaseCategoryObject selectedDiseaseCategory = this.diseaseCategoryMap.get(diseaseCategory);
        selectedDiseaseCategory.setDatasetNumber(dsNumber);

        thumbImgLayout.removeAllComponents();
        VerticalLayout min = initDiseaseLayout(selectedDiseaseCategory, 100, 100, maxNumber);
        min.setDescription("Disease Categories");
        thumbImgLayout.addComponent(min);
        thumbImgLayout.addStyleName("bigbtn");
        thumbImgLayout.addStyleName("blink");
    }

    /**
     * Initialize the disease category layout
     *
     * @param diseaseObject disease category object that has disease information
     * @param width The available width of the layout
     * @param height The available height of the layout
     * @param max the max number of available datasets
     */
    private VerticalLayout initDiseaseLayout(DiseaseCategoryObject diseaseObject, int width, int height, int max) {
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
            ResizableTextLabel diseaseTitle = new ResizableTextLabel("<center>" + diseaseObject.getDiseaseCategory().replace("Amyotrophic Lateral Sclerosis", "ALS")  + SpacerI + diseaseObject.getDatasetNumber() + "/" + max + SpacerII + "</center>");
            if (height >= 60 && height <= 80) {
                diseaseTitle.addStyleName("xsmallfont");
            } else {
                diseaseTitle.addStyleName("smallfont");
            }
            diseaseTitle.addStyleName("padding2");

            diseaseLayout.setDescription("#Datasets: " + diseaseObject.getDatasetNumber());
            diseaseLayout.addComponent(diseaseTitle);
            diseaseTitle.setContentMode(ContentMode.HTML);
            diseaseLayout.setComponentAlignment(diseaseTitle, Alignment.MIDDLE_CENTER);
            diseaseLayout.setStyleName(diseaseObject.getDiseaseStyleName());

            diseaseLayout.addStyleName("pointer");
            diseaseLayout.setData(diseaseObject);
        }

        return diseaseLayout;

    }

    /**
     * Rest the thumb to default logo and hide the dataset number.
     */
    public void resetThumbBtn() {
        thumbImgLayout.removeAllComponents();
        VerticalLayout min = initDiseaseLayout(null, 100, 100, maxNumber);
        min.setDescription("Disease Categories");
        thumbImgLayout.addComponent(min);
        thumbImgLayout.addStyleName("bigbtn");
        thumbImgLayout.addStyleName("blink");
    }

    /**
     * Reset disease category selection across the system.
     */
    public abstract void resetSelection();

    /**
     * Update data input for the component to generate the different bubbles for
     * disease category
     *
     * @param diseaseCategorySet Set of disease category objects
     */
    public final void updateData(Collection<DiseaseCategoryObject> diseaseCategorySet) {

        if (diseaseCategorySet == null) {
            return;
        }

        DiseaseCategoryObject sharedDc = null;
        for (DiseaseCategoryObject dc : diseaseCategorySet) {
            if (dc.getDiseaseCategory().contains("_")) {
                sharedDc = dc;
                break;
            }

        }
        frame.removeAllComponents();
//        frame.addComponent(title, 1, 1);
//        frame.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
        diseaseCategoryMap.clear();
        resetSelection();
        thumbImgLayout.removeAllComponents();

        if (diseaseCategorySet.size() == 1) {
            diseaseCategorySet.stream().map((dCategory) -> {
                VerticalLayout disease = initDiseaseBubbleLayout(dCategory, 20, 200);
                diseaseCategoryMap.put(dCategory.getDiseaseCategory(), dCategory);
                frame.addComponent(disease, 1, 1);
                return disease;
            }).forEachOrdered((disease) -> {
                frame.setComponentAlignment(disease, Alignment.MIDDLE_CENTER);

            });

            VerticalLayout min = initDiseaseLayout(null, 100, 100, maxNumber);
            min.setDescription("Disease Categories");
            thumbImgLayout.addComponent(min);

            thumbImgLayout.addStyleName("bigbtn");
            thumbImgLayout.addStyleName("blink");
            return;

        }

        DiseaseCategoryObject allDeseases = (DiseaseCategoryObject) diseaseCategorySet.toArray()[diseaseCategorySet.size() - 1];
        maxNumber = allDeseases.getDatasetNumber();
        diseaseCategoryMap.put("All Diseases", allDeseases);
        int rowcounter = 0;
        int colCounter = 1;
        for (DiseaseCategoryObject dCategory : diseaseCategorySet) {
            if (dCategory == allDeseases) {
                continue;
            }

            double scaledMax = scaleValues(dCategory.getDatasetNumber(), maxNumber, 1);
            int dia = (int) (scaledMax * 0.1 * height);

            VerticalLayout disease = initDiseaseBubbleLayout(dCategory, maxNumber, dia);
            if (dCategory.getDatasetNumber() == 1 && maxNumber == 1) {
                disease.setWidth(200, Unit.PIXELS);
                disease.setHeight(200, Unit.PIXELS);
            }

            diseaseCategoryMap.put(dCategory.getDiseaseCategory(), dCategory);
            frame.addComponent(disease, colCounter++, rowcounter);
            frame.setComponentAlignment(disease, Alignment.MIDDLE_CENTER);
            colCounter++;
            if (rowcounter == 0) {
                colCounter = 0;
                rowcounter = 1;
            }
            if (rowcounter == 1 && colCounter == 4) {
                colCounter = 1;
                rowcounter = 2;
            }

        }

        double scaledMax = scaleValues(allDeseases.getDatasetNumber(), maxNumber, 1);
        int dia = (int) (scaledMax * 0.1 * height);

        if (diseaseCategorySet.size() > 2) {
            VerticalLayout allDisease = initDiseaseBubbleLayout(allDeseases, maxNumber, dia);
            frame.addComponent(allDisease, 1, 1);
            frame.setComponentAlignment(allDisease, Alignment.MIDDLE_CENTER);
        }

        VerticalLayout min = initDiseaseLayout(null, 100, 100, maxNumber);
        min.setDescription("Disease Categories");
        thumbImgLayout.addComponent(min);

        thumbImgLayout.addStyleName("bigbtn");
        thumbImgLayout.addStyleName("blink");

    }

    /**
     * Update data input for the component to generate the different bubbles for
     * disease category (on searching or compare data mode)
     *
     * @param diseaseCategoriesIdMap Map of datasets id to disease categories.
     */
    public void updateData(Map<String, Set<Integer>> diseaseCategoriesIdMap) {

        HashSet<DiseaseCategoryObject> tempDiseaseCategorySet = new LinkedHashSet<>();
        int maxCounter = 0;
        DiseaseCategoryObject allDisCat = new DiseaseCategoryObject();
        if (diseaseCategoriesIdMap != null) {
            for (DiseaseCategoryObject dcat : diseaseCategorySet) {
                if (dcat.getDiseaseCategory().equalsIgnoreCase("All Diseases")) {
                    allDisCat.setDiseaseCategory(dcat.getDiseaseCategory());
                    allDisCat.setDiseaseStyleName(dcat.getDiseaseStyleName());
                    continue;
                }
                DiseaseCategoryObject updateDisCat = new DiseaseCategoryObject();
                if (diseaseCategoriesIdMap.containsKey(dcat.getDiseaseCategory())) {
                    updateDisCat.setDiseaseCategory(dcat.getDiseaseCategory());
                    updateDisCat.setDiseaseStyleName(dcat.getDiseaseStyleName());
                    updateDisCat.setDatasetNumber(diseaseCategoriesIdMap.get(dcat.getDiseaseCategory()).size());
                    maxCounter += updateDisCat.getDatasetNumber();
                    tempDiseaseCategorySet.add(updateDisCat);
                }
            }
            allDisCat.setDatasetNumber(maxCounter);
            tempDiseaseCategorySet.add(allDisCat);
            updateData(tempDiseaseCategorySet);
            thumbImgLayout.removeAllComponents();
            if (tempDiseaseCategorySet.size() > 2) {

                VerticalLayout min = initDiseaseLayout(allDisCat, 100, 100, maxNumber);
                min.setDescription("Disease Categories");
                thumbImgLayout.addComponent(min);

            } else {

                VerticalLayout min = initDiseaseLayout(tempDiseaseCategorySet.iterator().next(), 100, 100, maxNumber);
                min.setDescription("Disease Categories");
                thumbImgLayout.addComponent(min);

            }

        }

    }

    /**
     * Initialize disease category bubble
     *
     * @param diseaseObject Disease category object that has ass disease
     * category information
     * @param max maximum number of datasets
     * @param dia bubble diameter
     */
    private VerticalLayout initDiseaseBubbleLayout(DiseaseCategoryObject diseaseObject, int max, int dia) {
        VerticalLayout diseaseLayout = new VerticalLayout();
        diseaseLayout.setHeight((int) dia, Unit.PIXELS);
        diseaseLayout.setWidth((int) dia, Unit.PIXELS);

        String SpacerI;
        String SpacerII;

        SpacerI = "<br/>(";
        SpacerII = ")";
        diseaseLayout.addLayoutClickListener(this);

        ResizableTextLabel diseaseTitle = new ResizableTextLabel("<center>" + diseaseObject.getDiseaseCategory().replace("Amyotrophic Lateral Sclerosis", "ALS") + SpacerI + diseaseObject.getDatasetNumber() + "/" + max + SpacerII + "</center>");
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

    /**
     * On disease category bubble click(selection from disease category bubble)
     *
     * @param event user selection event
     */
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (((VerticalLayout) event.getComponent()).getData() == null) {
            return;
        }
        thumbImgLayout.removeAllComponents();
        DiseaseCategoryObject diseaseObject = (DiseaseCategoryObject) (((VerticalLayout) event.getComponent()).getData());
        VerticalLayout min = initDiseaseLayout(diseaseObject, 100, 100, maxNumber);
        min.setDescription("Disease Categories");
        thumbImgLayout.addComponent(min);

        onSelectDiseaseCategory(diseaseObject.getDiseaseCategory());

    }

    /**
     * Update thumb component color based on user selection
     *
     * @param diseaseCategory Disease category name (AD,MS,PD...etc)
     */
    public void updateSelection(String diseaseCategory) {

        thumbImgLayout.removeAllComponents();
        DiseaseCategoryObject diseaseObject = diseaseCategoryMap.get(diseaseCategory);

        VerticalLayout min = initDiseaseLayout(diseaseObject, 100, 100, maxNumber);
        min.setDescription("Disease Categories");
        thumbImgLayout.addComponent(min);

        onSelectDiseaseCategory(diseaseObject.getDiseaseCategory());

    }

    /**
     * On select disease category update the system
     *
     * @param diseaseCategoryName Disease category name (AD,MS,PD...etc)
     */
    public abstract void onSelectDiseaseCategory(String diseaseCategoryName);

    /**
     * Get thumb image container that is used for the disease category button on
     * left side
     *
     * @return thumbImgLayout Thumb image container layout
     */
    public HorizontalLayout getThumbImgLayout() {
        return thumbImgLayout;
    }

    /**
     * Converts the value from linear scale to log scale; The log scale numbers
     * are limited by the range of the type float; The linear scale numbers can
     * be any double value.
     *
     * @param linearValue the value to be converted to log scale
     * @param max The upper limit number for the input numbers
     * @param lowerLimit the lower limit for the input numbers
     * @return the value in log scale
     */
    private double scaleValues(double linearValue, int max, double lowerLimit) {
        double logMax = (Math.log(max) / Math.log(2));
        double logValue = (Math.log(linearValue + 1) / Math.log(2));
        logValue = (logValue * 2 / logMax) + lowerLimit;
        return logValue;
    }

}
