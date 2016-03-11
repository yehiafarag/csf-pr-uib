package probe.com;

import com.vaadin.ui.Panel;
import java.io.Serializable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import probe.com.handlers.CSFPRHandler;
import probe.com.view.Body;
import probe.com.view.HeaderLayout;

/**
 * @author Yehia Farag
 *
 * The CSF-PR application class the class is the main container for csf-pr html
 * web page the class contains the header layout and main body
 */
public class CSFPRApplication extends Panel implements Serializable {

    private static final long serialVersionUID = 1490961570483515444L;
    private final CSFPRHandler CSFPR_Handler;

    /**
     * Initialize the main view layout
     *
     * @param CSFPR_Handler 
     */
    public CSFPRApplication(CSFPRHandler CSFPR_Handler) {

        this.CSFPR_Handler = CSFPR_Handler;
        buildMainLayout();

    }

    /**
     * initialize the header and main body container layout
     *
     *
     */
    private void buildMainLayout() {
        this.setWidth("100%");
        VerticalLayout content = new VerticalLayout();
        this.setContent(content);
        //header part
        HeaderLayout header = new HeaderLayout();
        content.addComponent(header);
        CSFPR_Handler.setHeader(header);
        //body
        final Body body = new Body(CSFPR_Handler);
        content.addComponent(body);
//        body.addStyleName("zoom");
        
        final VerticalLayout bottom = new VerticalLayout();
        bottom.setWidth("100%");
        bottom.setHeight("10px");
        bottom.setStyleName(Reindeer.LAYOUT_WHITE);
//        this.addComponent(bottom);

    }

}
