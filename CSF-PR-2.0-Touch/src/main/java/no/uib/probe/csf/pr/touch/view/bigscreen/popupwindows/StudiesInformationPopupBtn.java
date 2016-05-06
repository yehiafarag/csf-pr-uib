
package no.uib.probe.csf.pr.touch.view.bigscreen.popupwindows;

import com.vaadin.ui.Button;
import java.util.Collection;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;

/**
 *
 * @author Yehia Farag
 */
public class StudiesInformationPopupBtn extends Button {

    private final StudiesInformationWindow studiesInformationWindow;

    public StudiesInformationPopupBtn() {
        this.setPrimaryStyleName("studyinfobtn");
        this.setDescription("Show dataset information");
        this.studiesInformationWindow = new StudiesInformationWindow();
    }

    public void updateData(Collection<QuantDatasetObject> quantDatasetSet) {
        studiesInformationWindow.updateData(quantDatasetSet);
    }
    public void view(){
        studiesInformationWindow.view();
    }

}
