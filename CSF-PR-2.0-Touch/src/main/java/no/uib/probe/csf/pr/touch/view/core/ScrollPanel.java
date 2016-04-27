
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 */
public class ScrollPanel extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private boolean show = true;
    private final Layout mainLayout, miniLayout;
    private final VerticalLayout navigationBtn;

    /**
     * Slide touch friendly panel
     *
     * @param mainLayout main panel components
     * @param miniLayout mini layout on close panel
     * @param orientation the scroll direction 0 scroll to up any other scroll bottom
     * slide to right
     */
    public ScrollPanel(Layout mainLayout, Layout miniLayout, int orientation) {
        this.mainLayout = mainLayout;
        navigationBtn = new VerticalLayout();
        navigationBtn.setHeight("30px");
        navigationBtn.setWidth("30px");
        navigationBtn.addStyleName("thumbBtn");
        mainLayout.addStyleName("border");
        

        this.miniLayout = miniLayout;
        miniLayout.addStyleName("slowscroll");
        miniLayout.addStyleName("hidescrolllayout");
        mainLayout.addStyleName("slowscroll");
        if (orientation == 0) {
           

            navigationBtn.setStyleName("upscrollebtn");
            
            this.addComponent(mainLayout); 
            this.addComponent(miniLayout);
            this.addComponent(navigationBtn);

        } else {
            navigationBtn.setStyleName("downscrollbtn");
            this.addComponent(miniLayout);
            this.addComponent(navigationBtn);            
            this.addComponent(mainLayout);
            
           
        }
        this.setComponentAlignment(navigationBtn, Alignment.MIDDLE_CENTER);
        this.setWidthUndefined();
        this.setHeightUndefined();
        this.setStyleName("scrollpanel");
        this.navigationBtn.addLayoutClickListener(ScrollPanel.this);
        this.setWidthUndefined();

    }

    /**
     * show full panel (Main layout)
     *
     * @param show boolean
     */
    public final void setShowPanel(boolean show) {
        if (show) {
            this.navigationBtn.removeStyleName("transformslidebtn");
            this.mainLayout.removeStyleName("hidescrolllayout");
            this.miniLayout.addStyleName("hidescrolllayout");
            this.setWidthUndefined();
            navigationBtn.detach();
             this.addComponent(navigationBtn);
              this.setComponentAlignment(navigationBtn, Alignment.MIDDLE_CENTER);
        } else {
            this.navigationBtn.addStyleName("transformslidebtn");
            this.mainLayout.addStyleName("hidescrolllayout");
            this.miniLayout.removeStyleName("hidescrolllayout");
              navigationBtn.detach();
            miniLayout.addComponent(navigationBtn);
        }
        this.show = show;

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        setShowPanel(!show);
    }
     /**
     * show full panel (Main layout)
     *
     * @param show boolean
     */
    public void setShowNavigationBtn(boolean  show){
        this.navigationBtn.setVisible(show);
    }

}

