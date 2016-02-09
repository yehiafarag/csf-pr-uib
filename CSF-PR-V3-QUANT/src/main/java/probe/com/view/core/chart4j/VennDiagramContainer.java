/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core.chart4j;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JPanel;
import probe.com.model.util.SwingToImageGenerator;

/**
 *
 * @author yfa041
 */
public class VennDiagramContainer extends VerticalLayout {

    private SwingToImageGenerator chartImgGenerator = new SwingToImageGenerator();

    public VennDiagramContainer() {
        ArrayList<String> grI = new ArrayList<String>();
        grI.add("A");
        grI.add("B");
        grI.add("C");
        grI.add("D");
        ArrayList<String> grII = new ArrayList<String>();
        grII.add("E");
        grII.add("F");
        grII.add("G");
        grII.add("A");
        grII.add("B");
        ArrayList<String> grIII = new ArrayList<String>();
        grI.add("I");
        grI.add("H");
        grI.add("C");
        grI.add("D");
        ArrayList<String> grIIII = new ArrayList<String>();
        grII.add("J");
        grII.add("K");
        grII.add("L");
        grII.add("A");
        grII.add("B");

        VennCode vennDiagramPanel = new VennCode();

        VennDiagramPanel vdp = new VennDiagramPanel(grI, grII, grIII, grIIII, "A", "B", "C", "D", Color.yellow, Color.yellow, Color.yellow, Color.yellow);

        JPanel xyPlotPanel = new JPanel();
        xyPlotPanel.add(vennDiagramPanel);
        xyPlotPanel.setSize(500, 500);
        vennDiagramPanel.setSize(500, 500);

        Image img = new Image("", new ExternalResource(chartImgGenerator.generateHeatMap(vennDiagramPanel)));
        this.addComponent(img);

        MainApp main = new MainApp(7, DESIGN_ATTR_PLAIN_TEXT, DESIGN_ATTR_PLAIN_TEXT);
        main.createDiagrams(1, 0);
        
    }

}
