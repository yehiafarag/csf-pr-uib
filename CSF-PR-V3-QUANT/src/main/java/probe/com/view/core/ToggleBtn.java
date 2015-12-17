/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 * @author yfa041
 */
public class ToggleBtn extends HorizontalLayout {

    private final HorizontalLayout toggleSwichButton;

    public ToggleBtn(String strBtn1Label, String strBtn2Label, String btn1Comment, String btn2Comment) {

        final Label btn1CommentLabel = new Label(btn1Comment);
        btn1CommentLabel.setStyleName(Reindeer.LABEL_SMALL);
        final Label btn2CommentLabel = new Label(btn2Comment);
        btn2CommentLabel.setStyleName(Reindeer.LABEL_SMALL);
        btn2CommentLabel.setVisible(false);

        this.setSpacing(true);
        Label btn1Label = new Label(strBtn1Label);
        btn1Label.setStyleName(Reindeer.LABEL_SMALL);
        Label btn2Label = new Label(strBtn2Label);
        btn2Label.setStyleName(Reindeer.LABEL_SMALL);
        toggleSwichButton = new HorizontalLayout();
        toggleSwichButton.setStyleName("toggleleft");
        toggleSwichButton.setWidth("50px");
        toggleSwichButton.setHeight("15px");
        toggleSwichButton.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (toggleSwichButton.getStyleName().equalsIgnoreCase("toggleleft")) {
                    toggleSwichButton.setStyleName("toggleright");
                    btn1CommentLabel.setVisible(false);
                    btn2CommentLabel.setVisible(true);
                } else {
                    toggleSwichButton.setStyleName("toggleleft");
                    btn1CommentLabel.setVisible(true);
                    btn2CommentLabel.setVisible(false);
                }
            }
        });
        this.addComponent(btn1Label);
        this.addComponent(toggleSwichButton);
        this.addComponent(btn2Label);

        this.addComponent(btn1CommentLabel);//commentLabel
        this.setComponentAlignment(btn1CommentLabel, Alignment.TOP_LEFT);//commentLabel
        this.addComponent(btn2CommentLabel);//commentLabel
        this.setComponentAlignment(btn2CommentLabel, Alignment.TOP_LEFT);//commentLabel
    }

    @Override
    public void addLayoutClickListener(LayoutEvents.LayoutClickListener listener) {
        toggleSwichButton.addLayoutClickListener(listener);
    }

}
