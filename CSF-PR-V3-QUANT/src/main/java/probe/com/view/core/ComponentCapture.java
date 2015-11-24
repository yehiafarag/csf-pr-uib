/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.vaadin.addons.screenshot.Screenshot;
import org.vaadin.addons.screenshot.ScreenshotImage;
import org.vaadin.addons.screenshot.ScreenshotListener;
import org.vaadin.addons.screenshot.ScreenshotMimeType;
import probe.com.view.core.exporter.ImageToExport;

/**
 *
 * @author yfa041
 */
public class ComponentCapture extends VerticalLayout {

    private final Screenshot component;
    public ComponentCapture() {
        component = Screenshot.newBuilder()
                .withAllowTaint(true)
                .withBackground("#f00")
                .withHeight(Page.getCurrent().getBrowserWindowHeight())
                .withWidth(Page.getCurrent().getBrowserWindowWidth())
                .withLogging(true)
                .withMimeType(ScreenshotMimeType.PNG)
                .build();

        component.addScreenshotListener(new ScreenshotListener() {

            @Override
            public void screenshotComplete(ScreenshotImage image) {
//                VaadinSession.getCurrent().lock();
                imageToExport.setImgByteArr(image.getImageData());
                UI.getCurrent().removeWindow(window);
//                VaadinSession.getCurrent().unlock();
            }
        });
        this.addComponent(component);
     
    }
    private Window window = new Window();
    private ImageToExport imageToExport = new ImageToExport();

    public void captureComponent(Component comp, int width, int height) {
        Window mainWindow = UI.getCurrent().getWindows().iterator().next();
        window.setWidth("100%");
        window.setHeight("100%");
//        int yposition = Page.getCurrent().getBrowserWindowHeight();
//        int xposition = Page.getCurrent().getBrowserWindowWidth();
//        window.setPositionX(xposition);
//        window.setPositionY(yposition);
        window.setStyleName("capturedwindow");
        window.setClosable(false);
        window.setResizable(false);
        
        VerticalLayout vlo = new VerticalLayout();
        vlo.setWidth(width + "px");
        vlo.setHeight(height + "px");
        imageToExport.setHeight(height);
        imageToExport.setWidth(width);
        vlo.setStyleName(Reindeer.LAYOUT_WHITE);
       
        
        window.setContent(vlo);
   
//        vlo.addComponent(btn);
        vlo.addComponent(comp);
        UI.getCurrent().addWindow(window);
        mainWindow.focus(); 
        component.takeScreenshot();

    }

    public ImageToExport getImageToExport() {
        return imageToExport;
    }

}
