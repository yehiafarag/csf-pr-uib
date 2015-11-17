/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core.jfreeutil;

import com.vaadin.ui.VerticalLayout;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.quant.QuantPeptide;

/**
 *
 * @author Yehia Farag
 */
public class StackedBarPeptideComponent extends VerticalLayout implements Comparable<StackedBarPeptideComponent> {

    private final Map<String, Object> param = new HashMap<String, Object>();
    private String defaultStyleShowAllMode;
    private boolean significant;
    private final String peptideKey;
//    private final NetworkDiagram ptmDiagram;
//    private final Options ptmOptions;

    public String getPeptideKey() {
        return peptideKey;
    }

    /**
     *
     * @return
     */
    public int getLevel() {
        return level;
    }

    /**
     *
     * @param level
     */
    public void setLevel(int level) {
        this.level = level;
    }
    private int level = 0;

    private boolean overlapped;

    /**
     *
     * @return
     */
    public boolean isMerged() {
        return merged;
    }

    /**
     *
     * @param merged
     */
    public void setMerged(boolean merged) {
        this.merged = merged;
    }
    private boolean merged;
    private final Set<QuantPeptide> quantpeptideSet = new LinkedHashSet<QuantPeptide>();

    /**
     *
     * @return
     */
    public boolean isOverlapped() {
        return overlapped;
    }

    /**
     *
     * @param overlapped
     */
    public void setOverlapped(boolean overlapped) {
        this.overlapped = overlapped;
    }

    /**
     *
     * @return
     */
    public String getDefaultStyleShowAllMode() {
        return defaultStyleShowAllMode;
    }

    /**
     *
     * @param defaultStyleShowAllMode
     */
    public void setDefaultStyleShowAllMode(String defaultStyleShowAllMode) {
        this.defaultStyleShowAllMode = defaultStyleShowAllMode.replace("selected", "").trim();
        this.setStyleName(this.defaultStyleShowAllMode);
    }

    /**
     *
     * @return
     */
    public int getX0() {
        return x0;
    }

    /**
     *
     * @return
     */
    public int getWidthArea() {
        return widthArea;
    }

    private final int x0;
    private final Integer widthArea;
    private final VerticalLayout ptmLayout = new VerticalLayout();
    private boolean ptmAvailable = false;

    /**
     *
     * @param x0
     * @param widthArea
     * @param peptideKey
     * @param peptideModification
     */
    public StackedBarPeptideComponent(int x0, int widthArea, String peptideKey, String peptideModification) {
        this.setHeight("15px");
        this.setWidth((widthArea + 2) + "px");
        this.x0 = x0;
        this.widthArea = widthArea;
        this.peptideKey = peptideKey;
//        ptmOptions = new Options();
//        ptmDiagram = new NetworkDiagram(ptmOptions);
        if (peptideModification != null && !peptideModification.trim().equalsIgnoreCase("")) {
//            Node n = initNode(peptideKey);
//            Node n2 = new Node(peptideKey + "-l", "-G-");
//            n2.setRadius(1);
//            n2.setShape(Node.Shape.circle);
//            Edge edge1 = new Edge(n.getId(), n2.getId());
//            edge1.setAllowedToMove(false);
//            ptmDiagram.addNode(n);
//            ptmDiagram.addNode(n2);
//            ptmDiagram.addEdge(edge1);
//            ptmDiagram.setWidth("50px");
//            ptmDiagram.setHeight("500px");
            ptmAvailable = true;
//            
            ptmLayout.setStyleName("ptmcycle");
            ptmLayout.setWidth("10px");
            ptmLayout.setHeight("10px");
            ptmLayout.setDescription(peptideModification);
            ptmLayout.setVisible(false);
//            PopupView labelpopup = new PopupView(null, i);
//            labelpopup.setPopupVisible(true);
//            labelpopup.setPrimaryStyleName("ptmcycle");
//            labelpopup.setHideOnMouseOut(false);

//                            exportAllProteinPeptidesPopup.setDescription("Export CSF-PR Peptides for ( " + accession + " ) for All Available Datasets");
//            this.addComponent(i);
//            labelpopup.setVisible(true);
        }
    }

    public boolean isPtmAvailable() {
        return ptmAvailable;
    }

    public VerticalLayout getPtmLayout() {
        return ptmLayout;
    }

//    public NetworkDiagram getPtmDiagram() {
//        return ptmDiagram;
//    }
//
//    private Node initNode(String id) {
//        Node node = new Node(id, "");
//
//        node.setRadius(0);
//        node.setAllowedToMoveX(false);
//        node.setAllowedToMoveY(false);
//        node.setShape(Node.Shape.dot);
//        return node;
//
//    }
    /**
     *
     * @param key
     * @param value
     */
    public void setParam(String key, Object value) {
        param.put(key, value);
    }

    /**
     *
     * @param key
     * @return
     */
    public Object getParam(String key) {
        return param.get(key);
    }

    /**
     *
     * @param select
     */
    public void heighlight(Boolean select) {
        if (select == null) {
            this.setStyleName(defaultStyleShowAllMode);
        } else if (select) {
            this.setStyleName("selected" + defaultStyleShowAllMode);
        } else {
            this.setStyleName("unselected" + defaultStyleShowAllMode);
        }
    }

    /**
     *
     * @param qp
     */
    public void addQuantPeptide(QuantPeptide qp) {
        this.quantpeptideSet.add(qp);
    }

    /**
     *
     * @return
     */
    public Set<QuantPeptide> getQuantpeptideSet() {
        return quantpeptideSet;
    }

    @Override
    public int compareTo(StackedBarPeptideComponent o) {
        return widthArea.compareTo(o.widthArea);
    }

    /**
     *
     * @return
     */
    public boolean isSignificant() {
        return significant;
    }

    /**
     *
     * @param significant
     */
    public void setSignificant(boolean significant) {
        this.significant = significant;
    }

}
