/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.vaadin.visjs.networkDiagram.Color;
import org.vaadin.visjs.networkDiagram.Edge;
import org.vaadin.visjs.networkDiagram.NetworkDiagram;
import org.vaadin.visjs.networkDiagram.Node;
import org.vaadin.visjs.networkDiagram.options.Options;
import org.vaadin.visjs.networkDiagram.options.physics.Physics;

/**
 *
 * @author yfa041
 */
public class  BasicUsageView extends HorizontalLayout {
    NetworkDiagram networkDiagram;
    Options options;
    public BasicUsageView() {
        System.out.println("enter is working");
        setWidth("1000px");
        setHeight("1000px");
        this.setStyleName(Reindeer.LAYOUT_BLUE);
        options = new Options();
        
        networkDiagram = new NetworkDiagram(options);
        networkDiagram.setSizeFull();
        addComponent(networkDiagram);

        //crete nodes
        Node node1 = new Node(1,"");
        Node node2 = new Node(2,"Node 2");
        Node node3 = new Node(3,"Node 3");
        Node node4 = new Node(4,"Node 4");
        Node node5 = new Node(5,"Node 5");
        Node node6 = new Node(6,"Node 6");
        
        node1.setRadius(0);
        node1.setAllowedToMoveX(false);
        node1.setAllowedToMoveY(false);
        node1.setShape(Node.Shape.dot);
       
        
        //create edges
        Edge edge1 = new Edge(node1.getId(),node2.getId());
        Edge edge2 = new Edge(node1.getId(),node3.getId());
        Edge edge3 = new Edge(node2.getId(),node5.getId());
        Edge edge4 = new Edge(node2.getId(),node4.getId());
        
        edge1.setAllowedToMove(false);

        networkDiagram.addNode(node1);
       
       
        networkDiagram.addNode(node2,node3,node4,node5,node6);
        networkDiagram.addEdge(edge1,edge2,edge3,edge4);
        networkDiagram.setVisible(true);
     
    }
}