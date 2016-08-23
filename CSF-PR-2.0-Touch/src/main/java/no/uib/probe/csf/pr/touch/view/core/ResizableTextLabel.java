/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

/**
 *
 * @author Yehia Farag
 */
public class ResizableTextLabel extends Label{

    public ResizableTextLabel(String content) {
        super(content);
         this.addStyleName("linehight150");
    }

    public ResizableTextLabel(Property contentSource) {
        super(contentSource);
         this.addStyleName("linehight150");
    }

    public ResizableTextLabel(String content, ContentMode contentMode) {
        super(content, contentMode);
         this.addStyleName("linehight150");
    }

    public ResizableTextLabel(Property contentSource, ContentMode contentMode) {
        super(contentSource, contentMode);
         this.addStyleName("linehight150");
    }

    public ResizableTextLabel() {
        super();
         this.addStyleName("linehight150");
    }

    
    
    
    @Override
    public void setHeight(String height) {
        super.setHeight(height); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(height, unit); 
        if(unit.equals(Unit.PIXELS)){     
             this.removeStyleName("xxsmallfont");
              this.removeStyleName("xsmallfont");
               this.removeStyleName("smallfont");
//                if (height >= 25) {
//                    this.addStyleName("smallfont");
//                } else if (height >= 15 && height < 25) {
//                     this.addStyleName("xsmallfont");
//                } else {
//                     this.addStyleName("xxsmallfont");
//                    
//                }
//           this.removeStyleName("linehight200");
       
            if (height < 15) {
            this.addStyleName("xxsmallfont");
//            this.addStyleName("linehight150");
        } else if (height >= 15 && height < 20) {
            this.addStyleName("xsmallfont");
//            this.addStyleName("linehight180");
        } else {
            this.addStyleName("smallfont");
//            this.addStyleName("linehight180");
        }
          
        
        }
    }
    
    
    
}
