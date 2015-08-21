/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.identificationlayoutcomponents;

import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import probe.com.model.beans.identification.IdentificationProteinBean;
import probe.com.model.beans.identification.StandardIdentificationFractionPlotProteinBean;
/*/*
 * @author Yehia Farag
 */

/**
 *
 * @author Yehia Farag
 */
public class IdentificationFractionPlotLayout extends VerticalLayout implements Serializable {

    private final Map<String, List<StandardIdentificationFractionPlotProteinBean>> standProtGroups;
    private final VerticalLayout leftSideLayout;
    private final VerticalLayout rightSideLayout;

    /**
     *
     * @return
     */
    public Map<String, List<StandardIdentificationFractionPlotProteinBean>> getStandProtGroups() {
        return this.standProtGroups;
    }

    /**
     *
     * @param protienFractionList
     * @param mw
     * @param standProtList
     */
    public IdentificationFractionPlotLayout(Map<Integer, IdentificationProteinBean> protienFractionList, double mw, List<StandardIdentificationFractionPlotProteinBean> standProtList) {
        leftSideLayout = new VerticalLayout();
        rightSideLayout = new VerticalLayout();
        rightSideLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        rightSideLayout.setSizeFull();
        rightSideLayout.setMargin(false);
        this.standProtGroups = initGroups(standProtList, mw);
        leftSideLayout.setWidth("100%");
        leftSideLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        setSpacing(true);
        this.setMargin(false);
        this.setWidth("100%");
        this.setStyleName(Reindeer.PANEL_LIGHT);
        leftSideLayout.addComponent(new IdentificationPlotsComponent("#Peptides", protienFractionList, standProtGroups, mw));
        leftSideLayout.addComponent(new IdentificationPlotsComponent("#Spectra", protienFractionList, standProtGroups, mw));
        leftSideLayout.addComponent(new IdentificationPlotsComponent("Avg. Precursor Intensity", protienFractionList, standProtGroups, mw));
        this.addComponent(leftSideLayout);
        this.addComponent(rightSideLayout);
    }

    private Map<String, List<StandardIdentificationFractionPlotProteinBean>> initGroups(List<StandardIdentificationFractionPlotProteinBean> standProtList, double mw) {
        Map<String, List<StandardIdentificationFractionPlotProteinBean>> colorMap = new HashMap<String, List<StandardIdentificationFractionPlotProteinBean>>();
        List<StandardIdentificationFractionPlotProteinBean> blueList = new ArrayList<StandardIdentificationFractionPlotProteinBean>();
        List<StandardIdentificationFractionPlotProteinBean> redList = new ArrayList<StandardIdentificationFractionPlotProteinBean>();
        List<StandardIdentificationFractionPlotProteinBean> lowerList = new ArrayList<StandardIdentificationFractionPlotProteinBean>();
        List<StandardIdentificationFractionPlotProteinBean> upperList = new ArrayList<StandardIdentificationFractionPlotProteinBean>();
        for (StandardIdentificationFractionPlotProteinBean spb : standProtList) {
            if (spb.getMW_kDa() > mw) {
                upperList.add(spb);
            } else {
                lowerList.add(spb);
            }
        }
        StandardIdentificationFractionPlotProteinBean closeLowe = new StandardIdentificationFractionPlotProteinBean();
        closeLowe.setMW_kDa(-10000);
        StandardIdentificationFractionPlotProteinBean closeUpper = new StandardIdentificationFractionPlotProteinBean();
        closeUpper.setMW_kDa((10000 * 1000));
        for (StandardIdentificationFractionPlotProteinBean spb : lowerList) {
            if (closeLowe.getMW_kDa() <= spb.getMW_kDa()) {
                closeLowe = spb;
            }
        }
        for (StandardIdentificationFractionPlotProteinBean spb : upperList) {
            if (closeUpper.getMW_kDa() >= spb.getMW_kDa()) {
                closeUpper = spb;
            }
        }
        for (StandardIdentificationFractionPlotProteinBean spb : standProtList) {
            if ((spb.getMW_kDa() == closeLowe.getMW_kDa() && spb.getName().equalsIgnoreCase(closeLowe.getName())) || (spb.getMW_kDa() == closeUpper.getMW_kDa() && spb.getName().equalsIgnoreCase(closeUpper.getName()))) {
                spb.setTheoretical(true);
                redList.add(spb);
            } else {
                spb.setTheoretical(false);
                blueList.add(spb);
            }
        }
        colorMap.put("#CDE1FF", blueList);
        colorMap.put("#79AFFF", redList);
        return colorMap;

    }

}
