/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

/**
 *
 * @author Yehia Farag
 */
public interface Resizable {
    public void resizeComponent(int width,int height);
    
    public String getViewId();
    
    public void view();
    
    public void hide();
}
