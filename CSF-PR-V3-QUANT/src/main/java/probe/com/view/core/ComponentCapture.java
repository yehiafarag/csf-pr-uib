/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.addons.screenshot.Screenshot;
import org.vaadin.addons.screenshot.ScreenshotImage;
import org.vaadin.addons.screenshot.ScreenshotListener;
import org.vaadin.addons.screenshot.ScreenshotMimeType;

/**
 *
 * @author yfa041
 */
public class ComponentCapture extends VerticalLayout {

    private final Screenshot component;
    private final Image img = new Image();

    public ComponentCapture() {
        component = Screenshot.newBuilder()
                .withAllowTaint(true)
                .withBackground("#f00")
                .withHeight(768)
                .withWidth(1024)
                .withLogging(true)
                .withMimeType(ScreenshotMimeType.PNG)
                .build();

        component.addScreenshotListener(new ScreenshotListener() {

            @Override
            public void screenshotComplete(ScreenshotImage image) {
                VaadinSession.getCurrent().lock();
                url = image.getDataURL();
//                img.setSource(new ExternalResource(url));
                UI.getCurrent().removeWindow(window);
                VaadinSession.getCurrent().unlock();
            }
        });
        this.addComponent(component);
        this.addComponent(img);

    }
    private Window window = new Window();
    private String url = null;

    public void captureComponent(Component comp) {
        url = null;

        window.setWidth("100%");
        window.setHeight("100%");
        VerticalLayout vlo = new VerticalLayout();
        vlo.setWidth("100%");
        vlo.setHeight("100%");
        vlo.setStyleName(Reindeer.LAYOUT_WHITE);
        window.setContent(vlo);
        vlo.addComponent(comp);
        UI.getCurrent().addWindow(window);
        component.takeScreenshot();

    }

    public String getUrlValue() {
//        if (url == null || VaadinSession.getCurrent().hasLock()) {
      

        return url;
    }

}
