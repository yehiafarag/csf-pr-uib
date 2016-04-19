/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author yfa041
 */
public class NotificationComponent extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final VerticalLayout popupBody;
    private final PopupView popup;

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (event.isDoubleClick()) {
            popup.setPopupVisible(false);
        } else {
            Notifi();
        }
//       
    }

    public void hideNotification() {
        popup.setPopupVisible(false);
    }

    public void Notifi() {
        popup.setPopupVisible(true);
        if (popupBody.getStyleName().contains("slowinvisible")) {
//            popupBody.removeStyleName("fastvisible");
            popupBody.removeStyleName("slowinvisible");
            
//            popupBody.addStyleName("fastvisible");
        } else {
            popupBody.addStyleName("slowinvisible");
        }
//        if (popup.isPopupVisible() && !popupBody.getStyleName().contains("slowinvisible")) {
//            popupBody.addStyleName("slowinvisible");
//        }
//        Timer t = new Timer();
       

    }

    public String getUniqueID() {
        return uniqueID;
    }

    private final String uniqueID;

    public NotificationComponent(String text, String uniqueID) {

        this.uniqueID = uniqueID;
        popupBody = new VerticalLayout();
        popupBody.setWidthUndefined();//setWidth((200) + "px");
        popupBody.setHeightUndefined();//setHeight((200) + "px");
        popupBody.setStyleName("notificationbody");
        popupBody.addLayoutClickListener(NotificationComponent.this);
        popup = new PopupView(null, popupBody) {

            @Override
            public void setPopupVisible(boolean visible) {
                super.setPopupVisible(visible); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean isPopupVisible() {
                return super.isPopupVisible(); //To change body of generated methods, choose Tools | Templates.
            }

        };
        this.addComponent(popup);

//        popupWindow = new Window() {
//
//            @Override
//            public void close() {
//                popupWindow.setVisible(false);
//            }
//
//        };
//
//        popupWindow.setStyleName("notificationwindow");
//        popupWindow.setCaption(null);
//        popupWindow.setContent(popupBody);
//        popupWindow.setWindowMode(WindowMode.NORMAL);
//        popupWindow.setWidthUndefined();//.setWidth((200) + "px");
//        popupWindow.setHeightUndefined();//setHeight((200) + "px");
//        popupWindow.setVisible(false);
//        popupWindow.setResizable(false);
//        popupWindow.setClosable(false);
//        popupWindow.setModal(false);
//        popupWindow.setDraggable(false);
//        popupWindow.setModal(false);
//
//        UI.getCurrent().addWindow(popupWindow);
//        popupWindow.setPositionX(x);
//        popupWindow.setPositionY(y);
//
//        popupWindow.setCaptionAsHtml(true);
//        popupWindow.setClosable(false);
        popupBody.setMargin(true);
        popupBody.setSpacing(true);

        Label content = new Label("<center>" + text + "</center>");
        content.setStyleName("notificationtext");
        content.setContentMode(ContentMode.HTML);
        popupBody.addComponent(content);

        VerticalLayout footer = new VerticalLayout();
        footer.setStyleName("bubbletalkfooter");
        footer.setWidth("30px");
        footer.setHeight("20px");
        popupBody.addComponent(footer);
        popup.setHideOnMouseOut(false);
        popup.setStyleName("popupnotification");
        popupBody.addStyleName("slowinvisible");

    }

}
