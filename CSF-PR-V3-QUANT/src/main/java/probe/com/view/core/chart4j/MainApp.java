package probe.com.view.core.chart4j;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

/**
 * This is the main class for drawing simple symmetric Venn diagrams.
 * <p>
 * A pie slice of a simple symmetric monotone Venn diagram of n curves can be represented by a binary matrix of n-1 rows
 * and (2^n - 2)/n columns such that each column contains exactly one 1 and no two 1's are adjacent in a row. The 1's in
 * the matrix represent the points at which the curves intersect. Similarly, it can be represented by a sequence
 * of length (2^n -2)/n of integers in range [0, n-2] which is called the <b>crossing sequence</b>. The i-th element 
 * indicates the row number of the matrix which contains a 1 at column i. 
 * <p>
 * For example the famous three circle Venn diagram can be represented by the following binary matrix and crossing sequence.
 * 
 * <p><blockcode><pre>
 *   A pie-slice of symmetric 3-Venn diagram : 
 *                            ____   ___________
 *                                \ /
 *                                 *
 *                            ____/ \____   ____
 *                                       \ /
 *                                        *
 *                            ___________/ \____
 *                            
 *    Binary Matrix :              1      0          
 *                            
 *                                 0       1
 *                                 
 *                              ---------------
 *                                 
 *    Crossing Sequence :          0   ,   1  
 * </pre></blockcode>   
 * <p>
 * See <a> href="http://arxiv.org/abs/1207.6452"</a> for more information about simple symmetric Venn diagrams.   
 * <p>
 * Given the number of curves n and an input file containing the crossing sequence of a collection of simple symmetric 
 * Venn diagrams of n curves, this class generates the plot of Venn diagrams in SVG format.
 * 
 * @author khalegh Mamakani
 *
 */

public class MainApp {
  
	/**
	 * The number of curves of the Venn diagrams.
	 */
	private int n;
	
	/**
	 * The name of the input file.
	 */
	private String inputFileName;
	
	/**
	 * The name of file containing the number and the hex code of colors used for coloring the diagrams.
	 */
	private String colorsFileName;
	
	/**
	 * Array containing the color codes.
	 */
	private String[] colors;
	
	/**
	 * This constructor creates an instance of main application of drawing simple symmetric Venn diagrams.
	 * 
	 * @param n            The number of curves of Venn diagrams.
	 * @param fileName     The name of input file that contains the crossing sequence of Venn diagrams.
	 */
	public MainApp(int n, String fileName, String colorsFile) {
		this.n = n;
		this.inputFileName = "\\\\eir.uib.no\\home6\\yfa041\\Settings\\Desktop\\VennPlot-master\\VennPlot-master\\InputData\\CrossingSequences\\VennSeven.txt";
                this.colorsFileName = "\\\\eir.uib.no\\home6\\yfa041\\Settings\\Desktop\\VennPlot-master\\VennPlot-master\\InputData\\ColorCodes\\VennSeven.txt";
	}

	/**
	 * This method creates the binary matrix of Venn diagram using the input crossing sequence x.
	 * 
	 * @param x    The crossing sequence of the Venn diagram.
	 * @return     The binary matrix of the Venn diagram.
	 */
    private int[][] createMatrix(int[] x) {
    	
    	//Allocating memory for the matrix.
        int [][] mx = new int[n-1][x.length];
        
        //For every i-th entry in the crossing sequence :
        //      set the entry at row x[i] and column i of the binary matrix to 1.
        for (int i = 0; i < x.length; i++){
        	int r = x[i];
        	mx[r][i] = 1;
        }
        return mx;   	
    }
    
    /**
     * This method reads the hex codes of the colors.
     * 
     * @return Error code. 0 : If there are no errors in reading the color codes. 1 : otherwise.
     */
    private int readColors() {
    	
    	Scanner colorFile = null;
    	try {
    		//Open the colors files.
    		colorFile = new Scanner(new BufferedReader(new FileReader(colorsFileName)));
    		
    		try {
    			//Read the number of colors.
    			int numberOfColors = colorFile.nextInt();
    			
    			//Allocate memory for the array of colors.
    			colors = new String[numberOfColors];
    			
    			//Read the color codes.
    			for (int i = 0; i < numberOfColors; i++) {
    				colors[i] = colorFile.next();
    			}
    		}
    		catch (Exception e) {
    			System.err.println("Error in reading color codes.");
    			return 1;
    		}
    		
    	}
    	catch (java.io.IOException e) {
    		System.err.println("Cannot open the colors file.");
    		return 1;
    	}
    	
    	return 0;
    }
    
    /**
     * This method reads the crossing sequence of Venn diagrams from the input file and draws them.
     */
    public void createDiagrams(int fillingMode, int drawingMode) {
    	
        //Computing the number of intersection points (the length of crossing sequence).
        int len = ((1 << n) - 2) / n;
        
        Scanner in = null;
        try {
        	
        	//Opening input file.
            in = new Scanner(new BufferedReader(new FileReader(inputFileName)));
            try {
		        int [] exchanges = new int[len];
		        
		        //Reading input data
		        while (in.hasNext()) {
		        	//For each Venn diagram,
		        	//1) Read the crossing sequence of diagram from the input file.
		        	for(int i = 0; i < len; i++) {
		        		exchanges[i] = in.nextInt();
		        	}
		        	
		        	//2) Create the binary matrix of diagram using the crossing sequence.
		        	int [][] mx = createMatrix(exchanges);
		        	
		        	//3) Create an instance of VennDiagram and call the appropriate drawing method.
		            VennDiagram diagram = new VennDiagram(mx, colors);
		            
		            if (fillingMode == 1) {
		            	//Create filled SVG diagram in cylindrical or radial mode 
			            diagram.drawFilledDiagram(drawingMode);
		            }
		            else {
		            	//Drawing only the curves of diagram without filling the regions.
		            	if (drawingMode == 0) {
		            		diagram.drawCurves();
		            	}
		            	else {
		            		diagram.drawCylindrical();
		            	}
		            }
		            
		            //diagram.createArcDiagram();
		        }
            }
            catch (Exception e) {
            	//Either there are not enough data items in the input crossing sequence
            	//Or the next item in the input is not an integer.
            	System.err.println("Error : invalid or insufficient data.");
            }
            finally {
            	in.close();
            }
        }
        catch (java.io.IOException e) {
        	//File does not exist or it cannot be opened.
        	System.err.println("Error : Cannot open the input file. No such file exists");
        }
    }
    
    
   
	
}
