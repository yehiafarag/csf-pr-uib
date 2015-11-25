package probe.com;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Image;
import java.io.Serializable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.awt.image.BufferedImage;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.util.vaadintoimageutil.Convertor;
import probe.com.model.util.vaadintoimageutil.peptideslayout.PeptidesSequenceContainer;
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
        
//        Convertor convertor = new Convertor();
//        VerticalLayout vlout = new VerticalLayout();
//        vlout.setHeight("300px");
//        vlout.setWidth("300px");
//        vlout.setStyleName(Reindeer.LAYOUT_BLACK);
//        
//        
//        
//        
//         VerticalLayout vlout2 = new VerticalLayout();
//        vlout2.setHeight("100px");
//        vlout2.setWidth("100px");
//        vlout2.setStyleName(Reindeer.LAYOUT_BLUE);
//        vlout.addComponent(vlout2);
//        
//        VerticalLayout vlout3 = new VerticalLayout();
//        vlout3.setHeight("50px");
//        vlout3.setWidth("50px");
//        vlout3.setStyleName(Reindeer.LAYOUT_BLACK);
//        vlout2.addComponent(vlout3);
//        
//        
//        
//        
////        
//        Image img = new Image();
//          BufferedImage image = new BufferedImage(595, 500, BufferedImage.TYPE_INT_ARGB);
//        PeptidesSequenceContainer pep = new PeptidesSequenceContainer("test title ",null,image.createGraphics(),null);
////        this.addComponent(vlout);
//        img.setSource(new ExternalResource(pep.toImage(image)));
////        
////        VerticalLayout spacer = new VerticalLayout();
////        spacer.setWidth("100%");
////        spacer.setHeight("5px");
////       this.addComponent(spacer);
//         this.addComponent(img);
         
           

    }
        
}
