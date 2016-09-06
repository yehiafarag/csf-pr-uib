/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.ui.VerticalLayout;
import java.util.HashMap;

/**
 *
 * @author Yehia Farag
 *
 * this class represents Arrow up layout used in protein trend line chart and
 * spark line in quant protein table
 */
public class TrendSymbol extends VerticalLayout implements Comparable<TrendSymbol> {

    private final HashMap<String, Object> paramMap;
    private  int trend;

    public TrendSymbol(int trend) {
        paramMap = new HashMap<>();
        this.trend=trend;
        this.addStyleName("slowtransition");
        switch (trend) {
            case 0:
                this.setStyleName("arrow-up100");
                break;
            case 1:
                this.setStyleName("arrow-upless100");
                break;
            case 2:
                this.setStyleName("diamond");
                break;
            case 3:
                this.setStyleName("arrow-downless100");

                break;
            case 4:
                this.setStyleName("arrow-down100");

                break;
            case 5:
                this.setStyleName("darkgraydiamond");
                break;
            case 6:
                this.setStyleName("graydiamond");
                break;

        }
    }

    public void setTrend(int trend) {
        this.trend = trend;
    }

    public void addParam(String name, Object value) {
        paramMap.put(name, value);

    }

    public Object getParam(String paramName) {
        return paramMap.get(paramName);
    }

    @Override
    public int compareTo(TrendSymbol o) {
        if (this.trend > o.trend) {
            return 1;
        } else {
            return -1;
        }
    }

}
