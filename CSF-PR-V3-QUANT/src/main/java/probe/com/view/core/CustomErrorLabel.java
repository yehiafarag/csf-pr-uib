/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public class CustomErrorLabel extends VerticalLayout implements Serializable {
    
    private final Label errorLabel = new Label();
    
    public CustomErrorLabel() {
        errorLabel.setContentMode(ContentMode.HTML);
        this.addComponent(errorLabel);
    }

    /**
     *
     * @param protName
     */
    public void updateErrot(String protName) {
      
        if (protName != null && !protName.trim().equalsIgnoreCase("")) {
            errorLabel.setValue("<font Style='color:red; '>No results found for ( " + protName + " ) </font>");
            errorLabel.setVisible(true);
            
        } else {
            errorLabel.setValue("");
            errorLabel.setVisible(false);
        }
        
        
    }
}
