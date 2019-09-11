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
public class CustomErrorLabel extends VerticalLayout implements Serializable{
    public void updateErrot(String protName)
    {
        this.removeAllComponents();
        Label  errorLabel = new Label("<h3 Style='color:gray;max-height: 20px;line-height: 20px;margin-top: -16px;position: absolute;'>No results found:</h3><h4 style='word-spacing: 5px; color: gray;'> " + protName + " </h4>");
        errorLabel.setContentMode(ContentMode.HTML);
        this.addComponent(errorLabel);
    
    }
}
