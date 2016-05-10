package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
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

    /**
     *
     * @param diseaseCategorySet list of disease categories for main disease
     * category panel
     * @param width body layout width in pixels
     * @param height body layout height in pixels
     */
    public QuantInitialLayout(Collection<DiseaseCategoryObject> diseaseCategorySet, int width, int height) {
        this.setWidth(width, Unit.PIXELS);
        this.setHeight(height, Unit.PIXELS);

        VerticalLayout frame = new VerticalLayout();
        frame.setWidthUndefined();
        frame.setHeightUndefined();
        frame.setSpacing(true);
        frame.setStyleName("border");
        this.addComponent(frame);
        this.setComponentAlignment(frame, Alignment.MIDDLE_CENTER);

        Label title = new Label("<center>Disease Category</center>");
        title.setContentMode(ContentMode.HTML);
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_BOLD);
        frame.addComponent(title);

        diseaseCategorySet.stream().forEach((diseaseObject) -> {
            frame.addComponent(this.initDiseaseLayout(diseaseObject, 50, 200));
        });
        miniLayout = new HorizontalLayout();
        miniLayout.addComponent(initDiseaseLayout(diseaseCategorySet.iterator().next(), 100, 100));
        miniLayout.addStyleName("bigbtn");
        miniLayout.addStyleName("blink");

    }

    /**
     * initialize the disease category layout
     *
     * @param diseaseObject disease category object that has disease information
     */
    private VerticalLayout initDiseaseLayout(DiseaseCategoryObject diseaseObject, int height, int width) {
        VerticalLayout diseaseLayout = new VerticalLayout();
        diseaseLayout.setWidth(width, Unit.PIXELS);
        diseaseLayout.setHeight(height, Unit.PIXELS);
        String SpacerI = " - ";
         String SpacerII = "";
        if (width == height) {
            SpacerI = "<br/>(";
            SpacerII=")";
        }else{
        diseaseLayout.addLayoutClickListener(this);
        
        }
        Label diseaseTitle = new Label("<center>" + diseaseObject.getDiseaseCategory() + SpacerI + diseaseObject.getDatasetNumber() +SpacerII+ "</center>");
        diseaseTitle.setDescription("#Datasets " + diseaseObject.getDatasetNumber());
        diseaseLayout.addComponent(diseaseTitle);
        diseaseTitle.setContentMode(ContentMode.HTML);
        diseaseLayout.setComponentAlignment(diseaseTitle, Alignment.MIDDLE_CENTER);
        diseaseLayout.setStyleName(diseaseObject.getDiseaseStyleName());
        
        diseaseLayout.addStyleName("pointer");        
        diseaseLayout.setData(diseaseObject);

        return diseaseLayout;

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        miniLayout.removeAllComponents();
        DiseaseCategoryObject diseaseObject = (DiseaseCategoryObject) (((VerticalLayout) event.getComponent()).getData());
        miniLayout.addComponent(initDiseaseLayout(diseaseObject, 100, 100));
        onClick(diseaseObject.getDiseaseCategory());

    }

    public abstract void onClick(String diseaseCategoryName);

    public HorizontalLayout getMiniLayout() {
        return miniLayout;
    }

}
