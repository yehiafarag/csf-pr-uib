/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantcompare;

import com.vaadin.server.Page;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.text.AttributedString;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author yfa041
 */
public class PieChart extends VerticalLayout {

    private final Page.Styles styles = Page.getCurrent().getStyles();
private final String[]labels;
private Double[] values;
private final Map<String, Color> defaultKeyColorMap = new HashMap<String, Color>();
 private final Map<String, String> valuesMap = new HashMap<String, String>();
  private final DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
    private DecimalFormat df = null;
     
    public PieChart(String title,double csfValue,double userValue) {
        
       
        this.setWidth(200 + "px");
        this.setHeight(200 + "px");
        defaultKeyColorMap.put("Found",new Color(110, 177, 206));
        defaultKeyColorMap.put("Not Found", new Color(219, 169, 1));
         otherSymbols.setGroupingSeparator('.');
         
        labels = new String[]{"Found","Not Found"};
        
        double uservalue = ((userValue/csfValue)*100.0);
          df = new DecimalFormat("#.##", otherSymbols);
        valuesMap.put("Found", df.format(uservalue)+"%");
        values = new Double[]{userValue,100.0-uservalue};
        valuesMap.put("Not Found", df.format(100.0-uservalue)+"%");
        
        String defaultImgURL = initPieChart(200, 200,title);
        String teststyle = "pieChartCssfilter_" + title;
        styles.add("." + teststyle + " :focus { outline: none !important;}");
        styles.add("." + teststyle + " {  background-image: url(" + defaultImgURL + " );background-position:center; background-repeat: no-repeat; cursor: pointer; }");
        this.setStyleName(teststyle);

    }
     private String initPieChart(int width, int height, String title) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (int x = 0; x < labels.length; x++) {
            dataset.setValue(labels[x], values[x]);

        }
        PiePlot plot = new PiePlot(dataset);
        plot.setNoDataMessage("No data available");
        plot.setCircular(true);

        plot.setLabelGap(0);

        plot.setLabelFont(new Font("Verdana", Font.BOLD, 10));
        plot.setLabelGenerator(new PieSectionLabelGenerator() {

            @Override
            public String generateSectionLabel(PieDataset pd, Comparable cmprbl) {
                return valuesMap.get(cmprbl.toString());
            }

            @Override
            public AttributedString generateAttributedSectionLabel(PieDataset pd, Comparable cmprbl) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        plot.setSimpleLabels(true);

        plot.setLabelBackgroundPaint(null);
        plot.setLabelShadowPaint(null);
        plot.setLabelPaint(Color.WHITE);
        plot.setLabelOutlinePaint(null);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setInteriorGap(0);
        plot.setShadowPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setBaseSectionOutlinePaint(Color.WHITE);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(1.2f));
        plot.setInteriorGap(0.05);
        for (String label : labels) {
            plot.setSectionPaint(label, defaultKeyColorMap.get(label));
        }

        JFreeChart chart = new JFreeChart(plot);
//        chart.setTitle(new TextTitle(title, new Font("Verdana", Font.BOLD, 13)));
        chart.setBorderPaint(null);
        chart.setBackgroundPaint(null);
        chart.getLegend().setFrame(BlockBorder.NONE);
        chart.getLegend().setItemFont(new Font("Verdana", Font.PLAIN, 10));
        String imgUrl = saveToFile(chart, width, height);

        return imgUrl;

    }
       private final ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();
      private String saveToFile(final JFreeChart chart, final double width, final double height) {
        byte imageData[];
        try {

            imageData = ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height, chartRenderingInfo));
            String base64 = Base64.encodeBase64String(imageData);
            base64 = "data:image/png;base64," + base64;
            return base64;
        } catch (IOException e) {
            System.err.println("at error " + e.getMessage());
        }
        return "";
    }

}
