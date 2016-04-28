/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view;

import java.util.LinkedHashMap;
import java.util.Map;
import no.uib.probe.csf.pr.touch.view.core.Resizable;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the view controller the controller responsible for the
 * select the requested layout and view it and component resize
 */
public class LayoutViewManager {

    private final Map<String, Resizable> layoutMap = new LinkedHashMap<String, Resizable>();
    private Resizable currentView;

    public LayoutViewManager() {
    }

    public void registerComponent(Resizable component) {
        layoutMap.put(component.getViewId(), component);

    }

    public void viewLayout(String viewId) {
        if (currentView != null && !currentView.getViewId().equalsIgnoreCase(viewId) && layoutMap.containsKey(viewId)) {
            currentView.hide();

        }
        if (layoutMap.containsKey(viewId)) {
            currentView = layoutMap.get(viewId);
            currentView.view();
        }

    }
}
