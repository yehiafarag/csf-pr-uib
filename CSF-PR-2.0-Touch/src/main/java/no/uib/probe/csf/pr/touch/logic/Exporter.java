/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author Yehia Farag
 */
public class Exporter implements Serializable{
    
    
    public void exportHeatMapImage(){
    
    }
    
    
    
    public byte[] exportImage(BufferedImage image){
    return null;
    
    }
    

    /**
     * this function to be use for csv peptides exporting with large datasets
     *
     * @param allPeptides peptides to be exported
     * @param datasetName
     * @param dataType validated/all
     * @param path for the file csf-pr file system
     */
    public byte[] expotProteinAccessionListToCSV(Set<String> accessionList) {
        File csvText = new File("CSF-PR Protein Accession List.csv");// "CSF-PR - " + datasetName + " - All - " + dataType + "- Peptides" + ".csv");
        PrintWriter out1 = null;
        try {
            if (csvText.exists()) {
                System.err.println("delet file " + csvText.delete());
            }
            csvText.createNewFile();
            FileWriter outFile = new FileWriter(csvText, true);
            out1 = new PrintWriter(outFile);
            String title = "CSF-PR Protein Accession List";
            out1.append(title);
            out1.append('\n');
            String header = "Accession";
            out1.append(header);
            out1.append('\n');
            for (String acc : accessionList) {
                out1.append(acc);
                out1.append('\n');

            }

            out1.flush();
            out1.close();

            byte[] crunchifyByteStream = new byte[(int) csvText.length()];

            try (FileInputStream crunchifyInputStream = new FileInputStream(csvText)) {
                crunchifyInputStream.read(crunchifyByteStream);
            }
            csvText.delete();

//            byte fileData[] = IOUtils.toByteArray(new FileInputStream(csvText));
            return crunchifyByteStream;
        } catch (Exception exp) {
            System.err.println(exp.getLocalizedMessage());
            if (out1 != null) {
                out1.flush();
                out1.close();
            }
        }
        return null;

    }

}
