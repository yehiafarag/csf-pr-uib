package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the big buttons layout
 *
 */
public abstract class BigBtn extends HorizontalLayout implements LayoutEvents.LayoutClickListener, MouseEvents.ClickListener {
    
    private final Image bigBtnIcon;
//    private final Image thumbBtnIcon;
    private final ImageContainerBtn thumbContainer;
    
    public ImageContainerBtn getThumbBtn() {
        return thumbContainer;
    }
    
    public BigBtn(String header, String text, String imgUrl) {
        this.setWidthUndefined();
        this.setMargin(false);
        this.setSpacing(true);
        this.addLayoutClickListener(BigBtn.this);
        this.setStyleName("bigbtn");
        
        bigBtnIcon = new Image();
        bigBtnIcon.setSource(new ThemeResource(imgUrl));
        bigBtnIcon.setWidth(70, Unit.PIXELS);
        bigBtnIcon.setHeight(70, Unit.PIXELS);
        bigBtnIcon.addStyleName("blink");
        this.addComponent(bigBtnIcon);
        String labelText = "<b>" + header + "</b><br/><font size='1'>" + text + "</font>";
        
        Label btnLabel = new Label(labelText);
        btnLabel.setContentMode(ContentMode.HTML);
        this.addComponent(btnLabel);
        btnLabel.setWidth("220px");
        
        thumbContainer = new ImageContainerBtn() {
            
            @Override
            public void onClick() {
                BigBtn.this.onClick();
            }
            
        };
//        thumbContainer.setStyleName("bigbtn");
//        thumbBtnIcon = new Image();
//        thumbBtnIcon.setWidth(70, Unit.PIXELS);
//        thumbBtnIcon.setHeight(70, Unit.PIXELS);
//        thumbBtnIcon.setDescription(labelText);

        thumbContainer.updateIcon(new ThemeResource(imgUrl));
        thumbContainer.setWidth(50, Unit.PIXELS);
        thumbContainer.setHeight(50, Unit.PIXELS);
        thumbContainer.setEnabled(true);
        thumbContainer.setReadOnly(false);
//        thumbContainer.addComponent(thumbBtnIcon);
//        thumbContainer.addLayoutClickListener(BigBtn.this);

    }
    
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        onClick();
    }
    
    @Override
    public void click(MouseEvents.ClickEvent event) {
        onClick();
    }
    
    public abstract void onClick();
    
}
