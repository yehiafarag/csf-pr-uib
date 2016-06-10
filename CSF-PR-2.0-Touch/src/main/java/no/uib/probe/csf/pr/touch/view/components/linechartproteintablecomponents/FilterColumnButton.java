/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.linechartproteintablecomponents;

import com.vaadin.server.ThemeResource;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;

/**
 *
 * @author Yehia Farag
 *
 * this class is responsible for filtering protein table based on columns
 */
public abstract class FilterColumnButton extends ImageContainerBtn {

    private final ThemeResource sortFilterRes;
    private final ThemeResource filterSortRes;
    private boolean activeFilter = false;

    public FilterColumnButton() {
        this.setHeight(45, Unit.PIXELS);
        this.setWidth(45, Unit.PIXELS);
        this.sortFilterRes = new ThemeResource("img/filter_sort.png");
        this.filterSortRes = new ThemeResource("img/sort_filter.png");
        this.updateIcon(sortFilterRes);
        this.setEnabled(true);
        this.setReadOnly(false);
//        this.addStyleName("smallimg");
        this.setDescription("Sort or filter comparisons");

    }

    @Override
    public void onClick() {
        if (activeFilter) {
            activeFilter = false;
            this.updateIcon(sortFilterRes);

        } else {
            activeFilter = true; 
            this.updateIcon(filterSortRes);
        }
        onClickFilter(activeFilter);
       
    }

    public abstract void onClickFilter(boolean isFilter);

}
