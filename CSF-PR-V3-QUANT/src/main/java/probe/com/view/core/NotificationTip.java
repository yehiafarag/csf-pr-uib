/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

/**
 *
 * @author yfa041
 */
public class NotificationTip extends Notification {

    private boolean visible;
    private final String defaultStyleName;
    private final NotificationTip notification;

    public NotificationTip(String caption, String styleName) {
        super(caption);
        this.defaultStyleName = styleName;
        
        this.setHtmlContentAllowed(true);
        this.setPosition(Position.BOTTOM_LEFT);
        this.notification=NotificationTip.this;

    }

    public void showNotification() {
        if (visible) {
            hideNotficiation();
          
        } else {
            this.setDelayMsec(15000);
            visible = true;
            this.setStyleName(defaultStyleName);
            notification.show(Page.getCurrent());
            
        }
    }

    public void hideNotficiation() {
      notification.setDelayMsec(DELAY_NONE);
      notification.setStyleName("hideNotification");
       visible = true;
    }

}
