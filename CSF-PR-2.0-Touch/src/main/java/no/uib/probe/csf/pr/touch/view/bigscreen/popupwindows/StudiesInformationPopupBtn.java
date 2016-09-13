
package no.uib.probe.csf.pr.touch.view.bigscreen.popupwindows;

import com.vaadin.server.ThemeResource;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;

/**
 *
 * @author Yehia Farag
 */
public abstract class StudiesInformationPopupBtn extends ImageContainerBtn {

    private final StudiesInformationWindow studiesInformationWindow;

    public StudiesInformationPopupBtn(boolean smallScreen) {
        this.updateIcon(new ThemeResource("img/file-text-o-1.png"));
        this.setWidth(40,Unit.PIXELS);
        this.setHeight(40,Unit.PIXELS);
        this.setEnabled(true);
      
        this.setDescription("Show dataset information");
        this.studiesInformationWindow = new StudiesInformationWindow(null,smallScreen){

            @Override
            public List<Object[]> updatePublications(Set<String> pumedId) {
                return updatePublicationsData(pumedId);
            }
        
        
        };
    }

    public void updateData(Collection<QuantDatasetObject> quantDatasetSet) {
        studiesInformationWindow.updateData(quantDatasetSet);
    }
    public void view(){
        studiesInformationWindow.view();
    }

    @Override
    public void onClick() {
//       view();
    }
     public abstract List<Object[]> updatePublicationsData(Set<String> pumedId);
    

}
