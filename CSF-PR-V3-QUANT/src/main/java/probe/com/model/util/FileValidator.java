package probe.com.model.util;

import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public class FileValidator implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -14218494555136445L;

    /**
     * find out the type of the updated file
     * @param strArr
     * @param MIMEType
     * @return
     */
    public int validateFile(String[] strArr, String MIMEType)//check if the file type and  file format
    {

        if (strArr == null || (!MIMEType.equalsIgnoreCase("text/plain") && (!MIMEType.equalsIgnoreCase("application/octet-stream") && (!MIMEType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) && strArr.length < 3)))//the file type must be 'txt' and the rows greater than 8 (in fraction protein initial rows is 8) 
        {
            return -1;//file is not either text or xlsx format or headers less than minimum number
        } else if (MIMEType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") && strArr.length == 3 && strArr[0].equalsIgnoreCase("Index") && strArr[1].contains("Start (kDa)") && strArr[2].contains("Stop (kDa)")) {
            return -5; //fraction range
        } else if ((MIMEType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || MIMEType.equalsIgnoreCase("application/octet-stream")) && strArr.length == 5 && strArr[1].trim().equalsIgnoreCase("Name") && strArr[2].trim().contains("Lower")) {
            return -100; //standard Plot Gel
        } else if (strArr.length == 19 && (strArr[0].equalsIgnoreCase("Accession") && strArr[13].equalsIgnoreCase("emPAI") && strArr[18].equalsIgnoreCase("Starred"))) {
            return 0; //the file is protein file
        }
        else if (strArr.length >= 18 && strArr[0].equalsIgnoreCase("Accession") && strArr[3].equalsIgnoreCase("Description") && strArr[(strArr.length - 1)].equalsIgnoreCase("Starred")) {
            int fractionNumber = (strArr.length + 1 - 15) / 3; //return the number of the fractions 

            return fractionNumber; //for fraction   file			
        }
        if (strArr.length == 26&& strArr[0].trim().equalsIgnoreCase("Protein") && strArr[8].trim().equalsIgnoreCase("Sequence") && strArr[11].trim().equalsIgnoreCase("Enzymatic")) {
            return -7; //glycofile
        }
        return -1;
    }
}
