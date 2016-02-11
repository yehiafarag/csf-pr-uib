package probe.com;

import java.io.Serializable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import eu.dusse.vaadin.waypoints.InviewExtension;
import eu.dusse.vaadin.waypoints.InviewExtension.EnterEvent;
import eu.dusse.vaadin.waypoints.InviewExtension.EnterListener;
import eu.dusse.vaadin.waypoints.InviewExtensionImpl;
import probe.com.handlers.CSFPRHandler;
import probe.com.view.Body;
import probe.com.view.HeaderLayout;

/**
 * @author Yehia Farag
 *
 * The CSF-PR application class the class is the main container for csf-pr html
 * web page the class contains the header layout and main body
 */
public class CSFPRApplication extends VerticalLayout implements Serializable {

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
        //header part
        HeaderLayout header = new HeaderLayout();
        this.addComponent(header);
        CSFPR_Handler.setHeader(header);
        //body
        final Body body = new Body(CSFPR_Handler);
        this.addComponent(body);
        
        final VerticalLayout bottom = new VerticalLayout();
        bottom.setWidth("100%");
        bottom.setHeight("10px");
        bottom.setStyleName(Reindeer.LAYOUT_WHITE);
//        this.addComponent(bottom);

    }

}
