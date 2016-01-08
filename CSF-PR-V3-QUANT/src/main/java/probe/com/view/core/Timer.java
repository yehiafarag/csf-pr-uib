/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.nextfour.datetimelabel.DateTimeLabel;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author yfa041
 */
public class Timer extends VerticalLayout {

    public Timer() {
        DateTimeLabel dateTimeLabel = new DateTimeLabel(){
            
        };
        try {
            dateTimeLabel.setRefreshIntervalMs(1000);
        } catch (NumberFormatException e) {
            dateTimeLabel.setRefreshIntervalMs(1000);
        }
        
         this.addComponent(dateTimeLabel);
    }
    
    
    

}
