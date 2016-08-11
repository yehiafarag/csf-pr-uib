/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.dd.DropHandler;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.declarative.DesignContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jsoup.nodes.Element;

/**
 *
 * @author Yehia Farag
 * 
 * this table support scrolling listener
 */
public class ScrollingTable extends Table{
    
    List<ScrollingTableScrollListener> listeners = new ArrayList<>();

    private void fireSrollEvent() {
        for (ScrollingTableScrollListener listener : listeners) {
            listener.doTableScroll();
        }
    }

    public void addScrollListener(ScrollingTableScrollListener listener) {
        listeners.add(listener);
    }

    @Override
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);
//        fireSrollEvent();
    }

    @Override
    protected Element writeItem(Element tbody, Object itemId, DesignContext context) {
        
        return super.writeItem(tbody, itemId, context); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void writeItems(Element design, DesignContext context) {
        super.writeItems(design, context); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Object readItem(Element tr, Set<String> selected, DesignContext context) {
        return super.readItem(tr, selected, context); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void readBody(Element design, DesignContext context) {
        super.readBody(design, context); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void readItems(Element design, DesignContext context) {
        super.readItems(design, context); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DropHandler getDropHandler() {
        return super.getDropHandler(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TableDragMode getDragMode() {
        return super.getDragMode(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object firstItemId() {
        return super.firstItemId(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void resetPageBuffer() {
        super.resetPageBuffer(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean shouldHideAddedRows() {
        return super.shouldHideAddedRows(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean isPartialRowUpdate() {
        return super.isPartialRowUpdate(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean isRowCacheInvalidated() {
        return super.isRowCacheInvalidated(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void unregisterComponent(Component component) {System.out.println("at scroll 2 is detected ");
        super.unregisterComponent(component); //To change body of generated methods, choose Tools | Templates.
    }

  
    @Override
    public int getCurrentPageFirstItemIndex() {
        return super.getCurrentPageFirstItemIndex(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected int indexOfId(Object itemId) {
        return super.indexOfId(itemId); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void setCurrentPageFirstItemIndex(int newIndex) {
     super.setCurrentPageFirstItemIndex(newIndex);
    }

    @Override
    protected void paintRowAttributes(PaintTarget target, Object itemId) throws PaintException {
        super.paintRowAttributes(target, itemId); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void paintRowIcon(PaintTarget target, Object[][] cells, int indexInRowbuffer) throws PaintException {
        super.paintRowIcon(target, cells, indexInRowbuffer); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void paintRowHeader(PaintTarget target, Object[][] cells, int indexInRowbuffer) throws PaintException {
        super.paintRowHeader(target, cells, indexInRowbuffer); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean rowHeadersAreEnabled() {
        return super.rowHeadersAreEnabled(); //To change body of generated methods, choose Tools | Templates.
    }

   

    @Override
    public void requestRepaint() {
        System.out.println("at scroll 5 is detected ");
        super.requestRepaint(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    
    
    
    
    
    
    
}
