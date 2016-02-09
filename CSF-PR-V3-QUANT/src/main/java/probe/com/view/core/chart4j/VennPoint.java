package probe.com.view.core.chart4j;
/**
 * This class represents an intersection point of a simple Venn diagram.
 * <p> 
 * An intersection point of a simple Venn diagram is a point in the plane at which exactly two curves intersect. 
 * A simple monotone Venn diagram of n curves is represented by a matrix of Venn points. This matrix consists of n-1 rows,
 * where row i contains n choose i Venn points. 
 *  
 * @author Khalegh Mamakani
 * @since July 11, 2009
 */
public class VennPoint {
    
    /** x coordinate of the Venn point */
    double x;
    
    /** y coordinate of the Venn point */
    double y;
    
    /** 
     * Location of adjacent points in the Venn point matrix. 
     * <p>
     * Each Venn point is adjacent to four Venn points. The adjacent points could be in the same row as the current point 
     * or one row above or below the row containing the current Venn point.
     * 
     * <p>
     * Locations of adjacent points in the matrix of Venn points are specified by the 2*4 nextPoints array.
     * The index of each adjacent point in the array is as follow :
     * <p><blockcode><pre>
     *                Top Left : 0 -----       -----  1 : Top Right
     *                                   \   /
     *                                    \ /
     *                                Venn o Point
     *                                    / \
     *                                   /   \
     *             Bottom Left : 2 -----       -----  3 : Bottom Right
     * </pre></blockcode>
     * <p>
     * The first row of array indicates the row at which the adjacent points are located relative to the row of the current 
     * Venn point (-1 : one row above, 0 : the same row, +1 : one row below) 
     * The second row indicates the column number of the matrix for each adjacent Venn point.
     * 
     */
    int [][] nextPoints;
    
    /** 
     * Distance of the Venn point from its adjacent points in terms of the number of columns.
     * 
     */
    double [] nextLen;
    
    /** 
     * Creates a new instance of VennPoint with the given coordinates. 
     * 
     * @param x     x coordinate of the Venn point
     * @param y    y coordinate of the Venn point
     */
    public VennPoint(double x, double y) {
        this.x = x;
        this.y = y;
        
        //Allocating memory for location and distance array
        nextPoints = new int[2][4];
        nextLen = new double[4];
    }
    
}
