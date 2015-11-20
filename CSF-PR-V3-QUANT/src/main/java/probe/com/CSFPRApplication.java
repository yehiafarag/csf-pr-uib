package probe.com;

import java.io.Serializable;
import com.vaadin.ui.VerticalLayout;
import probe.com.handlers.CSFPRHandler;
import probe.com.view.Body;
import probe.com.view.Header;

/**
 * @author Yehia Farag The CSF-PR application class the class is the main
 * container for csf-pr html web page the class contains the header layout and
 * main body
 */
public class CSFPRApplication extends VerticalLayout implements Serializable {

    private static final long serialVersionUID = 1490961570483515444L;
    private final CSFPRHandler handler;

    public CSFPRApplication(CSFPRHandler handler) {
        this.handler = handler;
        buildMainLayout();

    }

    /**
     * initialize the header and main body layout
     *
     *
     */
    private void buildMainLayout() {
        //header part
        Header header = new Header();
        this.addComponent(header);

        //body (tables)
        final Body body = new Body(handler);
        this.addComponent(body);

    }
        
}
