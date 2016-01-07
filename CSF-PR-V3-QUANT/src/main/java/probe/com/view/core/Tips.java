/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 */
public class Tips extends VerticalLayout {

    private final Notification notification;
    private final Styles style = Page.getCurrent().getStyles();
    private final String initialCssStr = ".tippopup{background: url(VAADIN/themes/dario-theme/img/tipbubble.png) no-repeat !important;  background-size:100% 100%  !important;   height:50px; color:black;    padding-left:10px !important;    padding-top:10px !important;  opacity: 1;"
            + "font-size:13px !important;   padding-right: 20px !important;}.tippopup b{padding: 20px !important;font-size:18px !important;font-weight:bold !important;}";

    protected Tips(String tipsInfo, String tipName, int x, int y) {
        notification = new Notification("<font style='width: 900px;'><b>Tip!</b></font>");
        notification.setDescription("<font>" + tipsInfo + "</font>");
        String dinamicStyle = initialCssStr.replace("tippopup", tipName) + "." + tipName + "{ margin-left:" + x + "px !important;  margin-bottom:" + y + "px !important;}";
        style.add(dinamicStyle);

        notification.setStyleName(tipName);
        notification.setHtmlContentAllowed(true);
        notification.setPosition(Position.BOTTOM_LEFT);

    }

    protected void showTip() {
        if (this.isConnectorEnabled()) {
            notification.setDelayMsec(0);
            notification.show(Page.getCurrent());
            notification.setDelayMsec(20000);
            UI.getCurrent().scrollIntoView(this);
        }
    }

}
