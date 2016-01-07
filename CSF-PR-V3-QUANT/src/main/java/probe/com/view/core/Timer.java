/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.nextfour.datetimelabel.DateTimeLabel;
import com.nextfour.datetimelabel.client.datetimelabel.DateTimeLabelState;
import com.vaadin.data.Property;
import com.vaadin.server.Resource;
import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.ui.Label;
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
