package probe.com;

import java.io.Serializable;
import com.vaadin.ui.VerticalLayout;
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
    private final CSFPRHandler csfprHandler;

    /**
     * Initialize the main view layout
     *
     * @param csfprHandler
     */
    public CSFPRApplication(CSFPRHandler csfprHandler) {
        this.csfprHandler = csfprHandler;
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

        //body
        final Body body = new Body(csfprHandler);
        this.addComponent(body);
    }

}
