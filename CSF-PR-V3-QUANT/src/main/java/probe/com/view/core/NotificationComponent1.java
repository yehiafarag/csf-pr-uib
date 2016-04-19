/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author yfa041
 */
public class NotificationComponent1 extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final Window popupWindow;
    private final VerticalLayout popupBody;

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (event.isDoubleClick()) {
            popupWindow.setVisible(false);
        } else {
            Notifi();
        }
//       
    }
    
    public void hideNotification(){
    popupWindow.setVisible(false); 
    }

    public void Notifi() {
        popupWindow.markAsDirtyRecursive();
        if(popupWindow.getStyleName().contains("slowinvisible")){
          popupWindow.removeStyleName("slowinvisible");
        }else{
        popupWindow.addStyleName("slowinvisible");   
        }
      
        popupWindow.setVisible(true);   
                 
        

    }

    public String getUniqueID() {
        return uniqueID;
    }

    private final String uniqueID;
    public NotificationComponent1(int x, int y, String text,String uniqueID) {
        this.uniqueID=uniqueID;
        popupBody = new VerticalLayout();
        popupBody.setWidthUndefined();;//setWidth((200) + "px");
        popupBody.setHeightUndefined();;//setHeight((200) + "px");
        popupBody.setStyleName("notificationbody");
        popupBody.addLayoutClickListener(NotificationComponent1.this);

        popupWindow = new Window() {

            @Override
            public void close() {
                popupWindow.setVisible(false);
            }

        };

        popupWindow.setStyleName("notificationwindow");
        popupWindow.setCaption(null);
        popupWindow.setContent(popupBody);
        popupWindow.setWindowMode(WindowMode.NORMAL);
        popupWindow.setWidthUndefined();//.setWidth((200) + "px");
        popupWindow.setHeightUndefined();//setHeight((200) + "px");
        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(false);
        popupWindow.setModal(false);
        popupWindow.setDraggable(false);
        popupWindow.setModal(false);

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.setPositionX(x);
        popupWindow.setPositionY(y);

        popupWindow.setCaptionAsHtml(true);
        popupWindow.setClosable(false);

        popupBody.setMargin(true);
        popupBody.setSpacing(true);
        
        Label content = new Label("<center>"+text+"</center>");
        content.setStyleName("notificationtext");
        content.setContentMode(ContentMode.HTML);
        popupBody.addComponent(content);
        
        VerticalLayout footer = new VerticalLayout();
        footer.setStyleName("bubbletalkfooter");
        footer.setWidth("30px");
        footer.setHeight("20px");
         popupBody.addComponent(footer);
        
    }

}
