/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.server.Resource;
import com.vaadin.shared.Position;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.Notification;
import com.vaadin.ui.SelectiveRenderer;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 */
public class Tips extends VerticalLayout {

    private final NotificationTip notification;
    private final Styles style = Page.getCurrent().getStyles();
    private final String initialCssStr = ".tippopup{background: url(VAADIN/themes/dario-theme/img/tipbubble.png) no-repeat !important;  background-size:100% 100%  !important;   height:50px; color:black;    padding-left:10px !important;    padding-top:10px !important;  opacity: 1;"
            + "font-size:13px !important;   padding-right: 20px !important;}.tippopup b{padding: 20px !important;font-size:18px !important;font-weight:bold !important;}";

    private final String showStyleName;

    protected Tips(String tipsInfo, String tipName, int x, int y) {
        showStyleName = tipName;
        notification = new NotificationTip("<font style='width: 900px;'><b>Tip!</b></font>", tipName);
        notification.setDescription("<font>" + tipsInfo + "</font>");
        String dinamicStyle = initialCssStr.replace("tippopup", tipName) + "." + tipName + "{ margin-left:" + x + "px !important;  margin-bottom:" + y + "px !important;}";
        style.add(dinamicStyle);
    
//        notification.setPosition(Position.BOTTOM_LEFT);

    }
    private boolean force = true;

    protected void showTip() {
//        HasComponents hasComponents = super.getParent();
//        if ((hasComponents instanceof SelectiveRenderer)
//                && !((SelectiveRenderer) hasComponents).isRendered(this)) {
//            notification.showNotification();
//        } else {
//            notification.hideNotficiation();
//
//        }

        if (this.isConnectorEnabled() || force) {
            force = false;
            notification.showNotification();
            if (this.isConnectorEnabled()) {
                UI.getCurrent().scrollIntoView(this);
            }
        }
    }

//    @Override
//    public HasComponents getParent() {
//        HasComponents hasComponents = super.getParent();
//        if ((hasComponents instanceof SelectiveRenderer)
//                && !((SelectiveRenderer) hasComponents).isRendered(this)) {
//            
//        } else {
//            if (notification != null) {
//                notification.hideNotficiation();
//            }
//
//        }
//        return hasComponents;
//    }

}
