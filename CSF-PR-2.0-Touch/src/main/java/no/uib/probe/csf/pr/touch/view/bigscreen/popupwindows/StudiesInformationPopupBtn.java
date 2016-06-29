
package no.uib.probe.csf.pr.touch.view.bigscreen.popupwindows;

import com.vaadin.server.ThemeResource;
import java.util.Collection;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;

/**
 *
 * @author Yehia Farag
 */
public class StudiesInformationPopupBtn extends ImageContainerBtn {

    private final StudiesInformationWindow studiesInformationWindow;

    public StudiesInformationPopupBtn() {
        this.updateIcon(new ThemeResource("img/file-text-o-1.png"));
        this.setWidth(45,Unit.PIXELS);
        this.setHeight(45,Unit.PIXELS);
        this.setEnabled(true);
      
        this.setDescription("Show dataset information");
        this.studiesInformationWindow = new StudiesInformationWindow(null);
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
    

}
