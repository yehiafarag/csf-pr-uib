package probe.com.model.util;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.*;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Yehia Farag
 */
public class RangeReader implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * read identification fraction range files
     *
     * @param fileIS
     * @return
     */
    public String[] readRangeFile(FileInputStream fileIS) {
        @SuppressWarnings("UseOfObsoleteCollectionType")
        Vector<Vector<XSSFCell>> dataHolder;
        dataHolder = ReadFile(fileIS);
        String[] strArr = read(dataHolder);
        return strArr;
    }

    /**
     * read identification fraction range files excel file type
     *
     * @param myInput
     * @return
     */
    @SuppressWarnings("UseOfObsoleteCollectionType")
    private Vector<Vector<XSSFCell>> ReadFile(FileInputStream myInput) {
        Vector<Vector<XSSFCell>> cellVectorHolder = new Vector<Vector<XSSFCell>>();
        try {
            XSSFWorkbook planilha = new XSSFWorkbook(myInput);

            XSSFSheet aba = planilha.getSheetAt(0);
            Iterator<?> rowIter = aba.rowIterator();

            while (rowIter.hasNext()) {
                XSSFRow myRow = (XSSFRow) rowIter.next();
                Iterator<?> cellIter = myRow.cellIterator();
                Vector<XSSFCell> cellStoreVector = new Vector<XSSFCell>();
                while (cellIter.hasNext()) {

                    XSSFCell myCell = (XSSFCell) cellIter.next();
                    cellStoreVector.addElement(myCell);
                }
                cellVectorHolder.addElement(cellStoreVector);
            }
        } catch (Exception e) {
            System.err.println("at error " + this.getClass().getName() + " line 63  " + e.getLocalizedMessage());
        }
        return cellVectorHolder;
    }

    /**
     * read identification fraction range files
     *
     * @param myInput
     * @return
     */
    @SuppressWarnings("UseOfObsoleteCollectionType")
    private String[] read(Vector<Vector<XSSFCell>> dataHolder) {
        String[] strArr = new String[dataHolder.size()];
        for (int i = 0; i < dataHolder.size(); i++) {
            Vector<?> cellStoreVector = (Vector<?>) dataHolder.elementAt(i);
            String line = "";
            for (int j = 0; j < cellStoreVector.size(); j++) {
                XSSFCell myCell = (XSSFCell) cellStoreVector.elementAt(j);
                String stringCellValue = myCell.toString();
                line = line + stringCellValue + "\t";
            }
            strArr[i] = line;

        }
        return strArr;
    }
}
