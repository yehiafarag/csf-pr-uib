/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.bin;

import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 */
public class SparkLineGen {
    
    private double min;
    private double max;
    
    /**
     *
     * @param min
     * @param max
     */
    public SparkLineGen(double min,double max){
    this.max=max;
    this.min=min;
    
    
    }

    /**
     *
     * @param value
     * @return
     */
    public VerticalLayout getSparkLine(double value){
    VerticalLayout sparkLine = new VerticalLayout();
    double width = value*100/max;
    sparkLine.setHeight("20px");
    sparkLine.setStyleName("bluesparkline");    
    sparkLine.setWidth(((int)width)+"px");
    
    
    return sparkLine;
    
    
    
    }
    
}
