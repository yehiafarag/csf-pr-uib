package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;

/**
 * This class contains pool sampling filter which allows user to show/hide pool
 * samples datasets
 *
 * @author Yehia Farag
 */
public abstract class PoolSamplesFilter extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    /**
     * Constructor to initialize the layout.
     */
    public PoolSamplesFilter() {
        this.setDescription("Include pool samples");
        this.setStyleName("filterbtn");
        this.addStyleName("smallpaading");
        Image icon = new Image();
        icon.setSource(new ThemeResource("img/group.png"));
        this.addComponent(icon);
        icon.setWidth(100, Unit.PERCENTAGE);
        icon.setHeight(100, Unit.PERCENTAGE);
        this.addLayoutClickListener(PoolSamplesFilter.this);
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (this.getStyleName().contains("unapplied")) {
            this.removeStyleName("unapplied");
        } else {
            this.addStyleName("unapplied");
        }
        updateSystem(this.getStyleName().contains("unapplied"));
    }

    /**
     * Update the system (hide/show pool samples datasets) based on user selection
     *
     * @param hidePoolSamplesDatasets hide pool samples datasets
     */
    public abstract void updateSystem(boolean hidePoolSamplesDatasets);

}
