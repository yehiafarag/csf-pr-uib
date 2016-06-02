/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 *
 * this class represents Arrow up layout used in protein trend line chart and
 * spark line in quant protein table
 */
public class TrendSymbol extends VerticalLayout {

    public TrendSymbol(int trend) {
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
            case 3:  this.setStyleName("arrow-downless100");
                
                break;
            case 4:this.setStyleName("arrow-down100");
                
                break;
            case 5:
                this.setStyleName("graydiamond");
                break;
           

        }
    }

}
