/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yfa041
 */
public class ComparisonDetailsBean  {

    private final List<Integer> upRegulatedList;
    private final List<Integer> notRegulatedList;
    private final List<Integer> downRegulatedList;
    
    

    public ComparisonDetailsBean() {
        upRegulatedList = new ArrayList<Integer>();
        notRegulatedList = new ArrayList<Integer>();
        downRegulatedList = new ArrayList<Integer>();
    }

   

    public List<Integer> getRegulatedList(int regulationCategory ) {
        if(regulationCategory == 2)
            return upRegulatedList;
        if(regulationCategory == 1)
        return notRegulatedList;
        else
            return downRegulatedList;
    }

    

    public void addUpRegulated(int up) {
        this.upRegulatedList.add(up);
    }

    public void addNotRegulated(int notRegulated) {
        this.notRegulatedList.add(notRegulated);
    }

    public void addDownRegulated(int down) {
        this.downRegulatedList.add(down);
    }

}
