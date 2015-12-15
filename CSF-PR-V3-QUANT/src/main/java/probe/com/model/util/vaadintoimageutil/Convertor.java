/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.util.vaadintoimageutil;

import com.itextpdf.text.pdf.codec.Base64;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jfree.chart.encoders.ImageEncoder;
import org.jfree.chart.encoders.ImageEncoderFactory;
import org.jfree.chart.encoders.ImageFormat;

/**
 *
 * @author yfa041
 */
public class Convertor implements Serializable {

    private final String Vertical_Layout = VerticalLayout.class.getName();
    private final Map<String, Color> backgroundMap = new HashMap<String, Color>();

    public Convertor() {
        backgroundMap.put("blue", Color.decode("#bcd3de"));
        backgroundMap.put("white", Color.decode("#fff"));
        backgroundMap.put("black", Color.decode("#1e2022"));//
        backgroundMap.put("", Color.decode("#f5f5f5"));

    }

    private final Map<Integer, Set<Layout>> vaadinLevelMap = new TreeMap<Integer, Set<Layout>>();
    private final Map<Integer, Set<JPanel>> jpanelLevelMap = new TreeMap<Integer, Set<JPanel>>();

    public String converLayout(Layout vaadinComponent) {
        vaadinLevelMap.clear();
        jpanelLevelMap.clear();
//        HashSet<Layout> rootset = new HashSet<Layout>();
//        rootset.add(vaadinComponent);
//        String classType = vaadinComponent.getClass().getName();
//        vaadinLevelMap.put(0, rootset);
//        if (classType.equalsIgnoreCase(Vertical_Layout)) {
//            System.out.println("componentType " + vaadinComponent.getClass().getName() + "   -" + vaadinComponent.getStyleName() + "-");
//            HashSet<JPanel> jPaneltset = new HashSet<JPanel>();
//            JPanel comp = convertVerticalLayout((VerticalLayout) vaadinComponent);
//            jPaneltset.add(comp);
//            jpanelLevelMap.put(0, jPaneltset);
//        }
//        if (vaadinComponent.getComponentCount() > 0) {
        fillVaadinMap(0, vaadinComponent);
//        }
        for (int k : jpanelLevelMap.keySet()) {
            for (JPanel parent : jpanelLevelMap.get(k)) {
                if (jpanelLevelMap.get(k + 1) != null) {
                    for (JPanel child : jpanelLevelMap.get(k + 1)) {
                        insert(parent, child);
                    }
                }

            }

            System.out.println(k + "  " + jpanelLevelMap.get(k).size());
        }

        JPanel root = jpanelLevelMap.get(0).iterator().next();
        String imgUrl = toImage(root.getWidth(), root.getHeight(), root);

        return imgUrl;
    }

    private JPanel insert(JPanel parent, JPanel child) {

        parent.add(child);
        return parent;
    }

    private void fillVaadinMap(int postion, Layout parentComponent) {

        if (parentComponent.getComponentCount() > 0) {
            for (int i = 0; i < parentComponent.getComponentCount(); i++) {
                fillVaadinMap(postion + 1, (Layout) parentComponent.getComponentIterator().next());
            }
        }
        if (parentComponent.getClass().getName().equalsIgnoreCase(Vertical_Layout)) {
            JPanel comp = convertVerticalLayout((VerticalLayout) parentComponent);
            if (!vaadinLevelMap.containsKey(postion)) {
                HashSet<Layout> set = new HashSet<Layout>();
                vaadinLevelMap.put(postion, set);
                HashSet<JPanel> jPaneltset = new HashSet<JPanel>();
                jpanelLevelMap.put(postion, jPaneltset);
            }
            Set<Layout> set = vaadinLevelMap.get(postion);
            Set<JPanel> jPaneltset = jpanelLevelMap.get(postion);
            set.add(parentComponent);
            jPaneltset.add(comp);
            vaadinLevelMap.put(postion, set);
            jpanelLevelMap.put(postion, jPaneltset);
        }

    }

    private JPanel convertVerticalLayout(VerticalLayout vaadinVerticalLayout) {

        JPanel verticalComponent = new JPanel();
        verticalComponent.setSize((int) vaadinVerticalLayout.getWidth(), (int) vaadinVerticalLayout.getHeight());
        verticalComponent.setBackground(backgroundMap.get(vaadinVerticalLayout.getStyleName()));
        return verticalComponent;
    }

    private final ImageEncoder in = ImageEncoderFactory.newInstance(ImageFormat.PNG, new Float(0.084666f));

    public String toImage(int height, int width, JComponent component) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setPaint(Color.WHITE);
        component.paint(graphics);
//        super.paint(graphics);
        byte[] imageData = null;

        try {

            imageData = in.encode(image);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        String base64 = Base64.encodeBytes(imageData);
        base64 = "data:image/png;base64," + base64;
        return base64;

    }

//    private Color getBackgroundColor(String color){
//    
//    
//    
//    }
}



