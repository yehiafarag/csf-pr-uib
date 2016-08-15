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

    public BigBtn(String header, String text, String imgUrl, boolean smallScreen) {
        this.setWidthUndefined();
        this.setMargin(false);
        int lineHeight=40;
        if (!smallScreen) {
            this.setSpacing(true);
            lineHeight=70;
        }
        this.addLayoutClickListener(BigBtn.this);
        this.setStyleName("bigbtn");

        bigBtnIcon = new Image();
        bigBtnIcon.setSource(new ThemeResource(imgUrl));

        bigBtnIcon.addStyleName("blink");
        this.addComponent(bigBtnIcon);
        String labelText = "<b>" + header + "</b><br/><font size='1'>" + text + "</font>";

        Label btnLabel = new Label(labelText);
        btnLabel.setContentMode(ContentMode.HTML);
        this.addComponent(btnLabel);
        btnLabel.setWidth(240, Unit.PIXELS);
        btnLabel.setHeight(lineHeight,Unit.PIXELS);

        thumbContainer = new ImageContainerBtn() {

            @Override
            public void onClick() {
                BigBtn.this.onClick();
            }

        };

        thumbContainer.updateIcon(new ThemeResource(imgUrl));

        thumbContainer.setEnabled(true);
        thumbContainer.setReadOnly(false);

        if (smallScreen) {
            bigBtnIcon.setWidth(40, Unit.PIXELS);
            bigBtnIcon.setHeight(40, Unit.PIXELS);
            thumbContainer.setWidth(25, Unit.PIXELS);
            thumbContainer.setHeight(25, Unit.PIXELS);
            thumbContainer.addStyleName("nopaddingimg");
        } else {
            bigBtnIcon.setWidth(70, Unit.PIXELS);
            bigBtnIcon.setHeight(70, Unit.PIXELS);
            thumbContainer.setWidth(40, Unit.PIXELS);
            thumbContainer.setHeight(40, Unit.PIXELS);
        }

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
