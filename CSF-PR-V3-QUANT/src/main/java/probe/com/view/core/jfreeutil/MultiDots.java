/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core.jfreeutil;

import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 */
public class MultiDots extends VerticalLayout{
    
    private int x;
    private int y;

    /**
     *
     * @param x
     * @param y
     */
    public MultiDots(int x, int y) {
        this.x = x;
        this.y = y;
        this.setStyleName("transparentcomp");
    }
    
    
    
}
