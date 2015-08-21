/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import probe.com.model.beans.identification.IdentificationDatasetBean;

/**
 *
 * @author Yehia Farag
 */
public class GeneralUtil {

    /**
     *
     * @param expName
     * @param expList
     * @return
     */
    public int getExpId(String expName, Map<Integer, IdentificationDatasetBean> expList) {
        for (IdentificationDatasetBean exp : expList.values()) {
            if (exp.getName().equalsIgnoreCase(expName)) {
                return exp.getDatasetId();
            }
        }
        return 0;
    }

    /**
     *
     * @param expList
     * @param userEmail
     * @return
     */
    public List<String> getStrExpList(Map<Integer, IdentificationDatasetBean> expList, String userEmail) {
        List<String> strExpList = new ArrayList<String>();
        for (IdentificationDatasetBean exp : expList.values()) {
            if (userEmail.equalsIgnoreCase("csf-pr@googlegroups.com") || exp.getEmail().equalsIgnoreCase(userEmail)) {
                String str = exp.getDatasetId() + "	" + exp.getName() + "	( " + exp.getUploadedByName() + " )";
                strExpList.add(str);
            }
        }
        return strExpList;
    }


}
