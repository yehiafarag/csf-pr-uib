/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 * @author yfa041
 */
public class Tips extends VerticalLayout{
    private final PopupView popupTips ; 
    private final VerticalLayout popupBody;
    

    public Tips(String tipsInfo) {
        popupBody = new VerticalLayout();
        popupBody.setStyleName("tipbody");
        popupTips = new PopupView("Tip of the day!",popupBody);
        popupTips.setStyleName("tippopup");
        this.addComponent(popupTips);
        popupTips.setHideOnMouseOut(false);
        
        Label header = new Label("<font>Tip!</font>");
        header.setContentMode(ContentMode.HTML);
        
        Label content = new Label(tipsInfo);
        content.setStyleName(Reindeer.LABEL_SMALL);
        popupBody.addComponent(header);
        popupBody.addComponent(content);
        
    }
    
    public void showTip(){
        popupTips.setPopupVisible(true);
    
    }
    
}
