
package probe.com.view.core.chart4j;

import java.io.PrintWriter;



/**
 * Given the binary matrix of a simple symmetric Venn diagram, 
 * this class provides different methods of drawing the diagram.
 * 
 * The diagram is drawn either in regular radial form or it is drawn in cylindrical form. Imagine the Venn diagram drawn on
 * the surface of cylinder with the unit radius. Then split the cylinder along a seam and roll it out flat. 
 * Each point on the surface of cylinder then is represented by (theta, phi) where theta is the distance of a vertical line 
 * through the point to the left side of the cylinder and phi is the height of the point.
 * 
 * <p><blockquote><pre> 
 *                          |<----------------------------------- 2 * Pi ----------------------------------->|
 *                          |--------------------------------------------------------------------------------|
 *                          |                   Venn Point                                                   |
 *                          |<------- (theta) ------->o                                                      |
 *                          |                         ^                                                      |
 *                          |                         |                                                      |
 *                          |                         | (phi)                                                |
 *                          |                         |                                                      |
 *                          |                         ∨                                                      |
 *                          |--------------------------------------------------------------------------------|
 *                          |                                                                                |
 *                          |                                                                                |
 *                          |                                                                                |
 *                          |                          Cylindrical representation                            |
 *                          |                                                                                |
 *                          |                                                                                |
 *                          |                                                                                |
 *                          |--------------------------------------------------------------------------------|
 * </pre></blockquote>
 *                          
 * Given the binary matrix of a Venn diagram, the program first compute the raw coordinate of each Venn point
 * in Cylindrical representation in such a way that at each intersection point the two intersecting curves are perpendicular. 
 * For regular drawing it transforms the polar coordinates of the points on cylinder to Euclidean coordinates. In both modes
 * the coordinates are scaled up to have a diagram with a good resolution.
 *  
 * @author Khalegh
 * @since July 12, 2009
 */

public class VennDiagram {
	
	/** static counter for the number of diagrams generated so far. */
    private static int diagramNo = 0;
    
    /** 
     * Matrix of indices of Venn points (vertices). This is the same as the binary matrix of Venn diagram
     * except that each entry of value 1 is replaced by the index of the corresponding vertex (Venn point) in the row.
     */
    private int [][] vertexIndexMatrix;
    
    /** Matrix of the Venn points */
    private VennPoint [][] points;
    
    
    /** 
     * number of intersection points per sector. 
     * It is the same as the number of columns of the binary matrix of the Venn diagram. 
     * */
    private int numberOfPointsInSector;
    
    /** Number of curves */
    private int n;
        
    /** the length of horizontal unit of drawing */
    private double horizontalUnitLen;

    /** the length of vertical unit of drawing */
    private double verticalUnitLen;
    
    /**
     * The amount of shift of points in vertical direction. It is used for vertical alignment of the diagram.
     */
    private double verticalOffset;
    
    /**
     * The amount of shift of points in horizontal direction. It is used for horizontal alignment of the diagram.
     */
    private double horizontalOffset;
    
    /** Contains the path of the edges of the innermost region of the diagram. */
    private String innerFace;

    /**
     * Hex codes of the filling/stroke colors of the diagram.
     */
    private String[] colors;
    
    /**
     * Creates an instance of the VennDiagram 
     * @param binaryMatrix	Binary matrix representing the Venn diagram.
     * @param colors		Filling colors
     */
    public VennDiagram(int [][] binaryMatrix, String[] colors) {
    	this.colors = colors;
    	    	
    	//The number of intersection points is the same as the number of columns of the binary matrix
    	numberOfPointsInSector = binaryMatrix[0].length;
    	
    	//Get the number of curves.
    	n = binaryMatrix.length + 1;
        
    	
    	/* Computing the vertex number (index of Venn points) in each row.
    	 * To do so we use another matrix which is the same as binary matrix except that
    	 * each entry of value 1 is substitute with the index of the corresponding intersection point
    	 */
    	vertexIndexMatrix = new int[n+1][numberOfPointsInSector];
        for (int i = 0; i < n - 1; i++) {
            int count = 1;
            for (int j = 0; j < numberOfPointsInSector; j++) {
            	//Set the index number of the intersection point
                if (binaryMatrix[i][j] == 1) {
                	vertexIndexMatrix[i + 1][j] = count++;
                } else {
                	vertexIndexMatrix[i + 1][j] = 0;
                }
            }
        }
        
        /*
         * Allocating memory for the VennPoint matrix of the diagram.
         * The matrix consists of n-1 rows and the k-th row of the matrix contains (n choose k)/n points
         */
        points = new VennPoint[n - 1][];
        for (int i = 0; i < n - 1; i++) {
            int count = BinomialCoeff.biCoeff(n, i + 1) / n;
            points[i] = new VennPoint[count];
        }
                
        //Creating the Venn points
        getPointStrings();
        
        //Adjusting the point coordinates such that at each points the two crossing curves are perpendicular.
        adjustPoints();
        
        diagramNo++;
    }
    
    /**
     * Computes the distance of the current point at the given row and column from the first point on the left/right in
     * the same/above/below row. 
     * @param row			The row number of the point
     * @param column		The column number of the point
     * @param direction		Indicates the horizontal direction for searching the next point; -1 : right-to-left; +1 : left-to-right.
     * @param rowIndicator	Indicates the row for searching the next point;	-1 : above row; 0 : the same row; +1 : below row.
     * @return				The distance (in terms of the number of columns)
     */
    private int getDistance(int row, int column, int direction, int rowIndicator) {
    	/*
    	 * Depending on the input parameters the method searches for the first non-zero entry which indicates an intersection point
    	 * and simply counts the number of columns up to that entry.
    	 */
        int count = 1;
        column = (numberOfPointsInSector + column + direction) % numberOfPointsInSector;
        
        //While a non-zero item has not been found goto the previous/next column.
        while (vertexIndexMatrix[row + rowIndicator][column] == 0 && count <= numberOfPointsInSector) {
            count++;
            column = (numberOfPointsInSector + column + direction) % numberOfPointsInSector;
        }
        return count;
    }
    
    /**
     * Creates the Venn point matrix of the diagram. For each intersection point this matrix contains the list and location of its adjacent
     * intersection points.
     */
    private void getPointStrings() {
        
        //The middle row is the reference row (It has a height of 0).
        double middleRow = (n - 2) / 2.0;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < numberOfPointsInSector; j++) {
            	//for each intersection point :
                if (vertexIndexMatrix[i][j] != 0) {
                    int row = i - 1;
                    int column = vertexIndexMatrix[i][j] - 1;
                    
                    //Set the raw x,y coordinates of the point.
                    double x = j;
                    double y = (middleRow - (i - 1));
                    
                    //Create a Venn point with these coordinates.
                    points[row][column] = new VennPoint(x, y);
                    
                    /*
                     * Find the adjacent points and compute the distance for each adjacent point. 
                     */
                    
                    for (int k = 0; k < 4; k++) {
                    	/*
                    	 * Find the four adjacent points, their location in the Venn point matrix and their distance from the current point.
                    	 * Each one of the two up-left and up-right adjacent points are either in the same row as the current point or 
                    	 * they are in one row above it. So their row indicator is either 0 or it is -1. Similarly, for the tow bottom
                    	 * adjacent points the row indicator is either 0 or +1. 
                    	 * Also the horizontal direction for the top-left and bottom left is -1 since they are on the left of the current point.
                    	 * For the top-right and bottom-right points it is +1.
                    	 */
                        int rowIndicator = 2 * (k / 2) - 1; 
                        int direction = 2 * (k % 2) - 1;
                        
                        //Find the distance of the next point not in the same row as the current point.
                        int nextRowDistance = getDistance(i, j, direction, rowIndicator);
                        //Find the distance of the next point in the same row as the current point.
                        int sameRowDistance = getDistance(i, j, direction, 0);
                        //Find the closest point and its row indicator
                        int distance = (nextRowDistance < sameRowDistance) ? nextRowDistance : sameRowDistance;
                        
                        //Find and set the row indicator of the adjacent point. 
                        rowIndicator = (nextRowDistance < sameRowDistance) ? rowIndicator : 0;
                        points[row][column].nextPoints[0][k] = rowIndicator;
                        
                        //Find and set the index of adjacent point in the matrix.
                        int adjacentIndex = (numberOfPointsInSector + j + distance * direction) % numberOfPointsInSector;
                        points[row][column].nextPoints[1][k] = vertexIndexMatrix[i + rowIndicator][adjacentIndex] - 1;
                        //Set the distance of the adjacent point.
                        points[row][column].nextLen[k] = distance;
                    }
                }
            }
        }
    }
    
    /**
     * Adjusts all points of the Venn diagram such that at every intersection point the two crossing curves are perpendicular.
     */
    private void adjustPoints() {
    	
    	//Find the middle row.
        int middleRow = (n - 1) / 2;
 
        /*
         * This algorithm doesn't work perfectly for all cases. It needs to be improved or redesigned.
         */
        //Do the adjusting for 10 rounds.
        for (int adjustingRound = 0; adjustingRound < 10; adjustingRound++) {
        	
        	//Adjust the points on the top half from inside towards outside.
	        for (int i = middleRow - 1; i >= 0; i--) {
	            for (int j = 0; j < points[i].length; j++) {
	            	adjustPoint(i, j, 1);
	            }
	        }
	
        	//Adjust the points on the bottom half from inside towards outside.
	        for (int i = middleRow - 1; i >= 0; i--) {
	            for (int j = 0; j < points[i].length; j++) {
	            	adjustPoint(n - 2 - i, j, -1);
	            }
	        }
	                
        	//Adjust the points on the top half from outside towards inside.
	        for (int i = 0; i <= middleRow; i++) {
	            for (int j = 0; j < points[i].length; j++) {
	            	adjustPoint(i, j, 1);
	            }
	        }
	
        	//Adjust the points on the bottom half from outside towards inside.
	        for (int i = 0; i <= middleRow; i++) {
	            for (int j = 0; j < points[i].length; j++) {
	                adjustPoint(n - 2 - i, j, -1);
	            }
	        }
        }
                
    }
    
    /**
     * Adjusts the location of the given point such that the two curve crossing at this point are perpendicular.
     * @param row				The row number of this point in the Venn point matrix.
     * @param column			The column number of this point in the Venn point matrix.
     * @param rowIndicator		Indicates the above row (-1) or below row (+1) as the reference row for adjusting 
     */
    private void adjustPoint(int row, int column, int rowIndicator) {
    	
    	//Find the index of the first left and right adjacent points.
        int leftIndex = 1 + rowIndicator;
        int rightIndex = 2 + rowIndicator;
        
        VennPoint point = points[row][column];
        double leftPointX, leftPointY, rightPointX, rightPointY;
        //If at least one the left or right adjacent points are not in the same row as the current point
        if (point.nextPoints[0][leftIndex] == rowIndicator || point.nextPoints[0][rightIndex] == rowIndicator) {
        	
        	//Compute the index of the other left and right adjacent points in the list of adjacent points.
            int otherLeftIndex = 1 - rowIndicator;
            int otherRightIndex = 2 - rowIndicator;
            
            /*------------------------------------ When the rowIndicator is +1 ------------------------------------*
             *                                                                                                     *
             *                        Other Left Point              Other Right Point                              *
             *                                o                             o                                      *
             *                                 \________           ________/                                       *
             *                                           \       /                                                 *
             *                                             \   /                                                   *
             *                                               o   (Venn Point)                                      *
             *                                             /   \                                                   *
             *                                           /       \                                                 *
             *                                  ________           ________                                        *
             *                                /                             \                                      *
             *                               o                               o                                     *
             *                          Left Point                      Right Point                                *
             *                                                                                                     *
             *-----------------------------------------------------------------------------------------------------*/
            
            //Get all four adjacent points.
            VennPoint leftPoint = points[row + point.nextPoints[0][leftIndex]][point.nextPoints[1][leftIndex]];
            VennPoint rightPoint = points[row + point.nextPoints[0][rightIndex]][point.nextPoints[1][rightIndex]];
            VennPoint otherLeftPoint = points[row + point.nextPoints[0][otherLeftIndex]][point.nextPoints[1][otherLeftIndex]];
            VennPoint otherRightPoint = points[row + point.nextPoints[0][otherRightIndex]][point.nextPoints[1][otherRightIndex]];
            
            /* 
             * If the left adjacent point is in the same row as the current point then 
             * find the head point and use it for adjusting the current point.
             * Otherwise use the left point itself. The head point is the turning point (peak/valley point) 
             * of the arc connecting the two points in the same row.
             */
            if (point.nextPoints[0][leftIndex] == 0) {
                leftPointX = getBalancedX(point.x - point.nextLen[leftIndex], leftPoint.y, point.x, point.y, -rowIndicator);
                leftPointY = getBalancedY(point.x - point.nextLen[leftIndex], leftPoint.y, point.x, point.y, -rowIndicator);
            } else {
                leftPointX = point.x - point.nextLen[leftIndex];
                leftPointY = leftPoint.y;
            }

            /* 
             * If the right adjacent point is in the same row as the current point then 
             * find the head point and use it for adjusting the current point.
             * Otherwise use the left point itself.
             */
            if (point.nextPoints[0][rightIndex] == 0) {
                rightPointX = getBalancedX(point.x, point.y, point.x + point.nextLen[rightIndex], rightPoint.y, -rowIndicator);
                rightPointY = getBalancedY(point.x, point.y, point.x + point.nextLen[rightIndex], rightPoint.y, -rowIndicator);
            } else {
                rightPointX = point.x + point.nextLen[rightIndex];
                rightPointY = rightPoint.y;
            }
            
            //Compute the required x coordinate for adjustment. 
            double x = getBalancedX(leftPointX, leftPointY, rightPointX, rightPointY, rowIndicator);
            
            //Compute the difference in  x coordinate needed for adjustment
            double xDifference = x - point.x;
            
            //Do not allow negative distances for any of the adjacent points.
            if (point.nextLen[rightIndex] - xDifference < 0)
            	return;
            if (point.nextLen[leftIndex] + xDifference  < 0)
            	return; 
            if (point.nextLen[otherRightIndex] - xDifference < 0)
            	return;
            if (point.nextLen[otherLeftIndex] + xDifference < 0)
            	return;
 
            //Adjust the height of the current point.
            point.y = getBalancedY(leftPointX, leftPointY, rightPointX, rightPointY, rowIndicator);

            //Update the distance fields of the current point.
            point.nextLen[rightIndex] -= xDifference;
            point.nextLen[leftIndex] += xDifference;
            
            //Assign the same distance value to the distance fields of the left and right adjacent points.
            //Because if p is in distance d of q then q is also in distance d of p.
            if (point.nextPoints[0][rightIndex] == 0) {
                rightPoint.nextLen[leftIndex] -= xDifference;
            } else {
                rightPoint.nextLen[otherLeftIndex] -= xDifference;
            }
            
            if (point.nextPoints[0][leftIndex] == 0) {
                leftPoint.nextLen[rightIndex] += xDifference;
            } else {
                leftPoint.nextLen[otherRightIndex] += xDifference;
            }
            
            /*If this is not the single outermost/innermost point in the sector of Venn diagram, 
             * then update distance from the other left and right points.
             */
            if (otherRightPoint != point) {           	
            	//update the distance of this point to the other right point.
                point.nextLen[otherRightIndex] -= xDifference;                
                //assign the same distance value to the distance field of the other right point
                if (point.nextPoints[0][otherRightIndex] == 0) {
                    otherRightPoint.nextLen[otherLeftIndex] -= xDifference;
                } else {
                    otherRightPoint.nextLen[leftIndex] -= xDifference;
                }
            }
            
            //Update distance to the other left point.
            if (otherLeftPoint != point) {
                point.nextLen[otherLeftIndex] += xDifference;
                if (point.nextPoints[0][otherLeftIndex] == 0) {
                    otherLeftPoint.nextLen[otherRightIndex] += xDifference;
                } else {
                    otherLeftPoint.nextLen[rightIndex] += xDifference;
                }
            }
            
            //adjust the coordinate of the current point
            if (x < 0) {
                x += numberOfPointsInSector;
            }
            if (x >= numberOfPointsInSector) {
                x -= numberOfPointsInSector;
            }
            
            point.x = x;
        }
    }
    
    /**
     * Given the coordinates of two points, it computes the x coordinate of the point where the two lines through these 
     * points intersect perpendicularly.
     * 
     * @param leftX		x coordinate of the left point
     * @param leftY		y coordinate of the left point.
     * @param rightX	x coordinate of the right point.
     * @param rightY	y coordinate of the right point.
     * @param slope		Slope of the line that goes through the left point. It is either -1 or +1.
     * @return			the x coordinate of the intersection of the two lines through the left and right points.
     */
    private double getBalancedX(double leftX, double leftY, double rightX, double rightY, int slope){
    	return (leftX - slope * leftY + rightX + slope * rightY) / 2.0;
    }
    
    /**
     * Given the coordinates of two points, it computes the y coordinate of the point where the two lines through these 
     * points intersect perpendicularly.
     * 
     * @param leftX		x coordinate of the left point
     * @param leftY		y coordinate of the left point.
     * @param rightX	x coordinate of the right point.
     * @param rightY	y coordinate of the right point.
     * @param slope		Slope of the line that goes through the left point. It is either -1 or +1.
     * @return			the y coordinate of the intersection of the two lines through the left and right points.
     */
    private double getBalancedY(double leftX, double leftY, double rightX, double rightY, int slope) {
        return (leftY - slope * leftX + rightY + slope * rightX) / 2.0;
    }
    
    /**
     * Computes the Euclidean x coordinate of a point using it cylindrical coordinates
     * @param theta		The longitude of the point in cylindrical coordinates.
     * @param phi		The latitude (height) of the point in cylindrical coordinates
     * @return			The x coordinate of the point in Euclidean coordinate system
     */
    private double polarX(double theta, double phi){
        return (1 << (n / 2)) * Math.cos(theta)/Math.tan(Math.PI/4 - phi/2.0);
    }
    
    /**
     * Computes the Euclidean y coordinate of a point using it cylindrical coordinates
     * @param theta		The longitude of the point in cylindrical coordinates.
     * @param phi		The latitude (height) of the point in cylindrical coordinates
     * @return			The x coordinate of the point in Euclidean coordinate system
     */
    private double polarY(double theta, double phi){
        return (1 << (n / 2)) * Math.sin(theta)/Math.tan(Math.PI/4 - phi/2.0);
    }
        
    /**
     * Returns the absolute value of a real number.
     * @param x		The real number.
     * @return		Returns the absolute value of x.
     */   
    private double abs(double x) {
        return (x >= 0) ? x : -x;
    }
    
    /**
     * Returns the string of the physical coordinates of the Venn point depending on the mode of drawing.
     * 
     * @param sector	The sector of the diagram containing the Venn Point. 
     * @param mode		The mode of drawing; 0 : regular (radial)  1 : Cylindrical
     * @param x			The raw x coordinate
     * @param y			The raw y coordinate
     * @return			The string of the physical coordinates.
     */
    private String getPointString(int sector, int mode, double x, double y) {
        double pointX = transformX(x, y, sector, mode);
        double pointY = transformY(x, y, sector, mode);
        
        return String.format("%f,%f ", pointX, pointY);
    }
    
    /**
     * Computes the actual x-coordinate of the given point given with respect to the drawing mode.
     * @param x			The initial x-coordinate of the point in the Venn point matrix.
     * @param y			The initial y-coordinate of the point in the Venn point matrix.
     * @param sector	The sector of the diagram containing the point.
     * @param mode		The drawing mode. 0 : regular, 1 : cylindrical.
     * @return
     */
    private double transformX(double x, double y, int sector, int mode) {
    	double transformedX;
       	double ratio = 5 * n;
    	//Compute the cylindrical coordinates of the point
        double theta = x * horizontalUnitLen + sector * 2 * Math.PI / n;
        double phi = y * verticalUnitLen; 
        
        if (mode == 0) {
        //Compute and scale up the Euclidean coordinates of the point for regular drawing
        	transformedX =  horizontalOffset + ratio * polarX(theta, phi);
        }
        else {
        	transformedX = horizontalOffset + n * n * n * theta;
        }
        
        return transformedX;
    }
    
    /**
     * Computes the actual y-coordinate of the given point given with respect to the drawing mode.
     * @param x			The initial x-coordinate of the point in the Venn point matrix.
     * @param y			The initial y-coordinate of the point in the Venn point matrix.
     * @param sector	The sector of the diagram containing the point.
     * @param mode		The drawing mode. 0 : regular, 1 : cylindrical.
     * @return
     */
    private double transformY(double x, double y, int sector, int mode) {
    	double transformedY;
       	double ratio = 5 * n;
    	//Compute the cylindrical coordinates of the point
        double theta = x * horizontalUnitLen + sector * 2 * Math.PI / n;
        double phi = y * verticalUnitLen; 
        
        if (mode == 0) {
        //Compute and scale up the Euclidean coordinates of the point for regular drawing
        	transformedY =  verticalOffset + ratio * polarY(theta, phi);
        }
        else {
        	transformedY = verticalOffset + phi * n * n * n;
        }
        
        return transformedY;
    }
    
    /**
     * Gives the description of an elliptical curve from the point (x1,y1) to point (x2, y2).
     * @param sector		The sector of Venn diagram containing the points.
     * @param mode			Mode of drawing. 0 : regular, 1: cylindrical.
     * @param x1			The x-coordinate of the first point.
     * @param y1			The y-coordinate of the first point.
     * @param x2			The x-coordinate of the second point.
     * @param y2			The y coordinate of the second point.
     * @param dir			The direction of drawing. 1 : clockwise, -1 : counter clockwise.
     * @param row			The row of the Venn-Point matrix containing the points.
     * @param leveled		Indicates if the two points have the same height. 0 : same height, 1 : different height.			
     * @return				Description of the elliptical curve.
     */
    
    private String drawEllipticalCurve(int sector, int mode, double x1, double y1, double x2, double y2, int dir, int row, int leveled) {
    	
    	/*
    	* Not finalized, Need a more general algorithm.
    	*/
    	double [] rx_coef3 = {0.45, 1.5};
    	double [] rx_coef5 = {0.51, 0.65, 1.0, 1.5};
    	double [] rx_coef7 = {0.58, 0.72, 0.78, 0.84, 0.9, 1.2}; 
    	double [] rx_coef11 = {0.65, 0.72, 0.73, 0.74, 0.74, 0.75, 0.78, 0.8, 0.85, 0.95}; 
    	double [] rx_coef13 = {0.65, 0.72, 0.73, 0.75, 0.76, 0.76, 0.76, 0.77, 0.78, 0.8, 0.85, 0.95}; 
    	
    	//Get the physical coordinates.
		double px1 = transformX(x1, y1, sector, mode);
		double py1 = transformY(x1, y1, sector, mode);
		double px2 = transformX(x2, y2, sector, mode);
		double py2 = transformY(x2, y2, sector, mode);
		
		//Compute the distance between the two points.
    	double distance = Math.sqrt((px1 - px2) * (px1 - px2) + (py1 - py2) * (py1 - py2));
	
		//Drawing elliptical curve in regular mode.
    	if (mode == 0) {
    		int sflag = (dir == 1) ? 1 : 0;
    		double alpha = x1 * horizontalUnitLen + sector * 2 * Math.PI / n;
    		double beta = x2 * horizontalUnitLen + sector * 2 * Math.PI / n;
    		double angle  = (Math.PI  + alpha + beta) / 2.0;
    		angle = 180 * angle / Math.PI;
    	
    		double rx = 1.0;
    		switch (n) {
    		case 3 :
    			rx = rx_coef3[row];
    			break;
    		case 5 : 
    			rx = rx_coef5[row];
    			break;
    		case 7 :
    			rx = rx_coef7[row];
    			break;
    		case 11 :
    			rx = rx_coef11[row];
    			break;
    		case 13 :
    			rx = rx_coef13[row];
    		}
    	
    		return String.format("A%.2f,%.2f %.2f 0,%d %.2f,%.2f",  rx * distance , distance, angle, sflag, px2, py2);
    	}
		//Drawing elliptical curve in cylindrical mode.
    	else {
        	double rx = (leveled == 1) ? 0.82 : 0.76; 
        	
        	int sflag = (dir == 1) ? 0 : 1;
        	return String.format("A%.2f,%.2f %d 0,%d %.2f,%.2f",  rx * distance , distance, 0 , sflag, px2, py2);
    	}
    }
    
    /**
     * Connects the point (originX, originY) to point (destinationX, destinationY) with a sequence of line segments.
     * 
     * @param originX			The x-coordinate of the origin point.
     * @param originY			The y-coordinate of the origin point.
     * @param destinationX		The x-coordinate of the destination point.
     * @param destinationY		The y-coordinate of the destination point.
     * @param sector			The sector of the Venn diagram containing the two points.
     * @param mode				The mode of drawing. 0 : regular, 1: Cylindrical.
     * @return					The string for the sequnce of line segments connecting the two points.
     */
    private String drawLine(double originX, double originY, double destinationX, double destinationY, int sector, int mode) {
    	int numberOfLineSegmenys = 10;
    	String lineString = "";
		double lenX = (destinationX - originX) / numberOfLineSegmenys;
		double lenY = (destinationY - originY) / numberOfLineSegmenys;
		for (int i = 1; i <= numberOfLineSegmenys; i++) {    			
    		lineString = lineString + "L" + getPointString(sector, mode, originX + i * lenX, originY + i * lenY);
		}

    	return lineString;
    }
    
    /**
     * Draws the edge with the given edge index and incident to the vertex at the given row and column.
     * @param vennFile					Output file stream of the Venn diagram
     * @param sector					The current sector of the diagram
     * @param mode						The mode of drawing
     * @param row						The row number of the vertex
     * @param column					The column number of the vertex
     * @param edgeIndex					The index of edge in the list of incident edges.
     * @param originX					The x coordinate of the origin vertex
     * @param numberOfBoundingVertices	The number of bounding vertices counted so far.
     * @return							The x coordinate of the destination vertex.
     */
    private double drawEdge(PrintWriter vennFile, int sector, int mode, int row, int column, int edgeIndex, double originX, int numberOfBoundingVertices) {
    	
    	int[] slopes = {-1, 1, 1, -1};
    	
    	String pointStr = "";
    	
    	/* Finding the drawing direction of the edge -1 : right to left; 1 : left to right.
    	 * If the index of edge is 0 or 2 then the edge is drawn from right to left and if
    	 * the index is 1 or 3 then it is drawn from left to right.
    	 */
    	int edgeDirection = 2 * (edgeIndex % 2) - 1;
    	
    	//Get the origin point of the edge.
    	VennPoint origin = points[row][column];
		int dir = origin.nextPoints[0][edgeIndex];
		
		//Get the destination point of the edge.
    	VennPoint destination = points[row+dir][origin.nextPoints[1][edgeIndex]];
    	
    	//Compute the coordinates of the next point
    	double destinationX = originX + edgeDirection * origin.nextLen[edgeIndex];
    	double destinationY = destination.y;
    	
    	//If this is the first (leftmost) vertex of the region
    	if (numberOfBoundingVertices == 0) {
    		//Set the origin vertex as the start of the path
    		pointStr = pointStr + "M" + getPointString(sector, mode, originX, origin.y);
    	}
    	
    	if (numberOfBoundingVertices == points[n-3].length + 1 && row == n - 2 && sector == 0) {
    		innerFace = innerFace + "M" + getPointString(sector, mode, originX, origin.y);    		
    	}
    	
    	//If the two end points of the edge are not in the same row
    	if (origin.nextPoints[0][edgeIndex] != 0) {
    		//Connect the origin point to destination with a sequence of line segments.
    		pointStr = pointStr + drawLine(originX, origin.y, destinationX, destinationY, sector, mode);
    	}
    	else {
    		/*
    		 * In this case we need to draw an arc connecting the two end points if they both have
    		 * the same y coordinate.
    		 * Otherwise we need to find a middle point that has the same y coordinate as the origin/destination point
    		 * and draw a straight line from the destination/origin to the middle point and then draw an arc between the middle
    		 * point and the origin/destination point. 
    		 * 
    		 * In the following example the Middle point and the Destination point have the same y coordinate :
    		 * 
    		 *                                           Head Point
    		 *          ------------------------------------ o -----------------------------------
    		 *           ∧                                  / \                                 ∧
    		 *           |                                 /   \                                |
    		 *           |                                /     \                               |
    		 *           |                               /       \                              |
    		 *           |                              /         \                             |
    		 *           |                             /           \                  (destinationDistance)
    		 *           |                            /             \                           |
    		 *           |                           /               \                          |
    		 *    (originDistance)                  ___________________                         |
    		 *           |                         /        arc        \                        |
    		 *           |                        /                     \ /                     ∨
    		 *           |    --- Middle Point   o ---                   o   Destination Point ---
    		 *           |                      /                       / \
    		 *           |                     /     
    		 *           |                    /       
    		 *           |                   / 
    		 *           ∨                \ /
    		 *          --- Origin Point   o --------
    		 *                            / \
    		 *                       
    		 */                  
    		
        	//Find the slope of edge.
        	int slope =  slopes[edgeIndex];
        	
        	/*
        	 * Find the y coordinate of the head point. This is the point where the two lines through
        	 * the origin and destination intersect.
        	 */
            double headPointY = getBalancedY(originX, origin.y, destinationX, destinationY, slope);

            //Find the (vertical) distance from the head point to the origin and destination points
            double originDistance = abs(headPointY - origin.y);
            double destinationDistance = abs(headPointY - destinationY);
            double distance = (originDistance <= destinationDistance) ? originDistance : destinationDistance;
            
            //Define and initialize the coordinate of the end points of the arc
            double arcStartX = originX; 
            double arcStartY = origin.y;
            double arcEndX = destinationX; 
            double arcEndY = destinationY;
            
        	//If the origin and middle point don't have the same height
            if (originDistance - distance != 0) {
            	/* Get the coordinate of the middle point (the start point of the arc) and 
            	 * draw a sequence of line segments from the origin point to the middle point.
            	 */
                arcStartX = originX + (originDistance - distance) * edgeDirection;
                arcStartY = origin.y + edgeDirection * slope * (originDistance - distance);
                pointStr = pointStr + drawLine(originX, origin.y, arcStartX, arcStartY, sector, mode);
                //pointStr = pointStr + "L" + getPointString(sector, mode, arcStartX, arcStartY);
            }
            //If the middle point and the destination don't have the same height
            else if (destinationDistance - distance != 0) {
            	//Calculate the coordinate of the middle point (the end point of the arc)
                arcEndX = destinationX - (destinationDistance - distance) * edgeDirection;
                arcEndY = destinationY + edgeDirection * slope * (destinationDistance - distance);
            }
            
            /*
             * Drawing the arc : The arc is drawn as an elliptical arc from the start point of the arc
             * to the end point of the arc using two control points (see SVG curves). 
             */
                        
            //Draw an elliptical arc from the current point (the start point of the arc) to the end point of the arc
            if (destinationDistance == originDistance)
            	pointStr = pointStr + drawEllipticalCurve(sector, mode, arcStartX, arcStartY, arcEndX, arcEndY, slope, row, 0);
            else
            	pointStr = pointStr + drawEllipticalCurve(sector, mode, arcStartX, arcStartY, arcEndX, arcEndY, slope, row, 1);
            	
            /*
             * The last edge of each region of the last ring must be also be added to another path to draw the innermost region.  
             */
            if (row == n - 2 && numberOfBoundingVertices == points[n-3].length + 1) {
            	if (sector == 0) {
            		innerFace = innerFace + drawEllipticalCurve(sector, mode, arcStartX, arcStartY, arcEndX, arcEndY, slope, row, 0);
            	}
            	else {            		
            		innerFace = innerFace + drawEllipticalCurve(n - sector, mode, arcStartX, arcStartY, arcEndX, arcEndY, slope, row, 0);
           	}
            }
            
            //If the middle point and the destination don't have the same height
            if (destinationDistance - distance != 0) {
            	//Draw a sequence of line segments from the middle point to the destination point.
            	pointStr = pointStr + drawLine(arcEndX, arcEndY, destinationX, destinationY, sector, mode);
                //pointStr = pointStr + "L" + getPointString(sector, mode, destinationX, destinationY);
            }
    	}
    	
    	//Write the edge code as part of the path to the Venn diagram file.
    	vennFile.print(pointStr);
    	
    	//Return the x coordinate of the destination for drawing the next edge
    	return destinationX;
    }
    
    /**
     * Draws a region of the symmetric Venn diagram started from a Venn point located at row i and column j.
     * It is drawn by as a path of edges bounding the region. Starting from the given point, it simply follows the edges
     * around the region.
     * 
     * @param vennFile  Output file stream of the Venn diagram
     * @param sector	Sector number of the diagram
     * @param mode		The mode of drawing, 0 : regular, 1 : Cylindrical.
     * @param row		Row number of the leftmost vertex of the region
     * @param column	Column Number of the leftmost vertex of the region
     * @param startX	X coordinate of the leftmost vertex of the region
     * @return			The x coordinate of the leftmost vertex of next region
     */
        
    private double drawRegion(PrintWriter vennFile, int sector, int mode, int row, int column, double startX) {
    	
    	/*
    	 * The most important operation in this method is how to find the next edge and link edges together to create the path.
    	 * Each vertex (intersection point) has exactly four incident edges, top left, top right, bottom left and bottom right
    	 * with the given indices 0, 1, 2 and 3 respectively.
    	 * 
    	 *                                      0 : __   __ : 1
    	 *                                            \ /
    	 *                                             o
    	 *                                      2 : __/ \__ : 3
    	 *                                      
    	 * To each edge is also assigned a value (-1, 0, +1), which we call it direction, through which we can find 
    	 * the other end point of the edge. The top left and top right edges can only have a direction of -1 or 0, because they
    	 * are either connected to another vertex in the above row or to a vertex in the same row of the current vertex.
    	 * Similarly, the bottom edges can only have a direction of 0 or +1. 
    	 * 
    	 * Based on the index and direction of an edge we can find the next vertex (Venn point) and the next edge that bounds 
    	 * the region. Given the index of an incident edge of a vertex that bounds a region, we can easily find the index of 
    	 * the following incident edge that bounds the same region. because they come in counter clockwise order. 
    	 * The only thing that remains is that given the index of an incident edge of the current point, how to find the index
    	 * the same edge in the list of incident edges of the opposite point. Putting these two together, the following table
    	 * shows how to compute the index of the next bounding edge, giving the direction and index of the current edge.
    	 * 
    	 *                        Direction   |   index of current edge  |  index of next edge 
    	 *                    ----------------|--------------------------|------------------------
    	 *                           -1       |             0            |            1
    	 *                            0       |             0            |            0
    	 *                           -1       |             1            |            3
    	 *                            0       |             1            |            2
    	 *                            0       |             2            |            1
    	 *                           +1       |             2            |            0
    	 *                            0       |             3            |            3
    	 *                           +1       |             3            |            2
    	 *                    --------------------------------------------------------------------
    	 *                    
    	 * Below is an example of a region with the direction and index of its bounding edges.
    	 *                    
    	 *                                      (0,3)      (0,3)     (+1,3)
    	 *                                        o          o          o
    	 *                                       / \________/ \________/ \
    	 *                                      /                         \
    	 *                                     /                           \
    	 *                            (-1,1)  o                             o  (+1,2)
    	 *                                     \                           /
    	 *                                      \   ___________________   /
    	 *                                       \ /                   \ /
    	 *                                        o                     o
    	 *                                     (-1,0)                 (0,0)
    	 *                                     
    	 * Therefore, given the absolute value of direction and the index of the current edge, we can use a 2*4 array, which
    	 * is named edgeMap, to get the index of the next bounding edge.
    	 * 
    	 *  We always start from the top right edge of the leftmost vertex of a region to create a closed path of its bounding
    	 *  edges.
    	 *    
    	 */
    	
    	//The map for finding the next bounding edge using the direction and index of current edge.
    	int[][] edgeMap = {{0,2,1,3},{1,3,0,2}};      
    	
    	//Get the leftmost point of the region
    	VennPoint startPoint = points[row][column];
    	
    	//x coordinate of the bounding vertices
    	double x = startX;
    	
    	//The current bounding vertex
    	VennPoint currentPoint = startPoint;
    	
    	//The index of the current bounding edge 
    	int edgeIndex = 1;
    	
    	//The total number of bounding vertices
    	int numberOfBoundingVertices = 0;          
    	
    	int leftmostPointRow = row;
    	
    	//Starting a path
    	vennFile.print("<path d=\"");
    	
    	//Drawing the region by creating a path of its bounding edges simply by following the vertices bounding the region.
    	do {
    		
    		//Getting direction of the current bounding edge. 
    		int dir = currentPoint.nextPoints[0][edgeIndex];
    		
    		//Drawing the next bounding edge.
    		x = drawEdge(vennFile, sector, mode, row ,column , edgeIndex, x, numberOfBoundingVertices);
    		
    		//Increment the number of bounding vertices
    		numberOfBoundingVertices++;
    		
    		//Find the next vertex bounding the region.
    		row = row + dir;
    		column = currentPoint.nextPoints[1][edgeIndex];
    		currentPoint = points[row][column];
    		
    		//Compute index of the next bounding edge.
    		edgeIndex = edgeMap[dir * dir][edgeIndex];
    		
    		/*If this is the rightmost vertex of the region then save its x coordinate as the starting x coordinate of
    		 * the next region in the ring. The rightmost vertex of a region is the leftmost of its following region with
    		 * the same weight. The rightmost vertex is the one which is in the same row as the leftmost vertex and its 
    		 * incident of index 2 bounds the regions. 
    		 */
    		if (row == leftmostPointRow && edgeIndex == 2) {
    			startX = x;
    		}
    	
    		//while we haven't reached to the starting point, add the next bounding edge. 
    	} while (currentPoint != startPoint || edgeIndex != 1);

    	//Close the path.
    	vennFile.println("\" stroke=\"none\" stroke-width=\"1\" fill=\"" + colors[row] + "\"/>");
    	
    	//Return the starting x coordinate for the next region.
    	return startX;
    }
    
    /**
     * Draws a curve of the Venn diagram with the given number.
     * @param vennFile		The output stream of the Venn diagram
     * @param curveNo		The number of the curve to be drawn
     * @param mode			The mode of drawing    0 : regular	1 : cylindrical 
     * @param strokeColor
     */
    private void drawCurve(PrintWriter vennFile, int curveNo, int mode, String strokeColor) {
    	
        int row = 0;				//Row number of the Venn point
        int column = 0;				//Column number of the Venn point
        
        /*
         * Direction of the current curve segment;
         * -1 the curve segment goes from the current row to one row above it.
         * +1 the curve segment goes from the current row to one row below it.
         */
        int dir = -1;				
        double originX = points[0][0].x;	//x coordinate of the origin (current) Venn point
        double originY = points[0][0].y;	//y coordinate of the origin Venn point
        double destinationX;				//x coordinate of the destination (next) Venn point
        double destinationY;				//y coordinate of the destination Venn point
        
        //Start the curve SVG path.
        String curveStr = "<path d=\"";
        
        //Add the starting point (the outermost point) to the path.
        curveStr = curveStr + "M" + getPointString(curveNo, mode, originX, originY);
        do {
            //If the next point is not in the same row
            if (points[row][column].nextPoints[0][2 - dir] != 0) {
            	//Get the x coordinate of the next point.
            	destinationX = originX + points[row][column].nextLen[2 - dir];
                
                //Get the row and column of the next point.
                column = points[row][column].nextPoints[1][2 - dir];
                row = row - dir;
                
                //Get the y coordinate of the next point.
                destinationY = points[row][column].y;
                
                //Draw  the curve segment as a sequence of lines from the current point to the next point.
        		//curveStr = curveStr + "L" + getPointString(curveNo, mode, destinationX, destinationY);
        		curveStr = curveStr + drawLine(originX, originY, destinationX, destinationY, curveNo, mode);
            } else {
            	/*Otherwise, we need to draw an arc from the current point to the next point
            	 * if they both have the same height or we need to find a middle point that has 
            	 * the same height as one of the current or next points connecting it to the middle point
            	 * with an arc and connect the other to the middle point with an straight line.
            	 * See drawEdge method for details.
            	 */
            	
            	//Get the x and y coordinate of the next point.
                destinationX = originX + points[row][column].nextLen[2 - dir];
                destinationY = points[row][points[row][column].nextPoints[1][2 - dir]].y;
                
               	/*
            	 * Find the y coordinate of the point where the two lines through
            	 * the origin and destination intersect.
            	 */                
                double headPointY = getBalancedY(originX, originY, destinationX, destinationY, dir);

                //Find the vertical distance of the origin and destination to the head point. 
                double originDistance = abs(headPointY - originY);
                double destinationDistance = abs(headPointY - destinationY);
                //Find the closest distance.
                double distance = (originDistance <= destinationDistance) ? originDistance : destinationDistance;
                
                //Define and initialize the coordinate of the end points of the arc
                double arcStartX = originX;
                double arcStartY = originY;
                double arcEndX = destinationX;
                double arcEndY = destinationY;
                
                //If the origin and the middle point don't have the same height
                if (originDistance - distance != 0) {
                	//Find the middle point as the start point of the arc.
                	arcStartX = originX + originDistance - distance;
                	arcStartY = originY + dir * (originDistance - distance);
                	//Draw a sequence of line segments from the origin to the middle point.
                    curveStr = curveStr + drawLine(originX, originY, arcStartX, arcStartY, curveNo, mode);
                }
                //If destination and the middle point don't have the same height.
                else if (destinationDistance - distance != 0) {
                	//Find the middle point as the end point of the arc.
                    arcEndX = destinationX - (destinationDistance - distance);
                    arcEndY = destinationY + dir * (destinationDistance - distance);
                }
                
                //Draw an elliptical arc from the current point (the start point of the arc) to the end point of the arc
                if (destinationDistance == originDistance)
                	curveStr = curveStr + drawEllipticalCurve(curveNo, 0, arcStartX, arcStartY, arcEndX, arcEndY, dir, row, 0);
                else
                	curveStr = curveStr + drawEllipticalCurve(curveNo, 0, arcStartX, arcStartY, arcEndX, arcEndY, dir, row, 1);
                
                //Change the direction od curve segments because of the arc.
                dir = -dir;
                if (destinationDistance - distance != 0) {
                	/* Draw a sequence of line segments from the arc end point to the destination point if the middle point
                	 * and the destination point don't have the same height.
                	 */
                    curveStr = curveStr + drawLine(arcEndX, arcEndY, destinationX, destinationY, curveNo, mode);
                }
                
                //Get the column of the next point.
                column = points[row][column].nextPoints[1][2 + dir];
            }
            
            //Move to the next point.
            originX = destinationX;
            originY = destinationY;
            
        //Draw the next curve segment while we haven't reach to the starting point.
        } while(row != 0 || column != 0 || dir == 1);
        
        //Write the curve path to the file of the Venn diagram.
        curveStr = curveStr + "\" stroke=\"" + strokeColor + "\" stroke-width=\"2.0\" fill=\"none\"/>";
        vennFile.println(curveStr);
    }
    
    /**
     * Creates a plot of Venn diagram filling regions such that all regions with the same weight, 
     * are inside the interior of the same number of curves, have the same color.
     * 
     * @param mode   The type of drawing, 0 : regular, 1 : Cylindrical. 
     */   
    public void drawFilledDiagram(int mode) {
    	
    	int[] startIndex = new int[n-1];
    	startIndex[0] = 1;
    	for (int i = 1; i < n/2; i++) {
    		startIndex[i] = BinomialCoeff.biCoeff(n, i+1)/n - startIndex[i-1];
    		startIndex[n-2-i] = startIndex[i-1];
    	}
    	startIndex[0] = 0; 
    	
    	//Getting the total length of the sector (pie-slice).
        double sectorLen = points[0][0].nextLen[1];
        
        //verticalUnitLen = 0.75 / (sectorLen);                       //Vertical unit length
        verticalUnitLen = 2 * Math.PI /(n * sectorLen);                       //Vertical unit length
        horizontalUnitLen = 2 * Math.PI /(n * sectorLen);			//Horizontal unit length
        

        PrintWriter vennFile = null;
        String fileName;
        if (mode == 0) {
        	fileName = String.format("VD_%2d_Filled_%02d.svg", n, diagramNo);
        }
        else {
        	fileName = String.format("VD_%2d_CylindricalFilled_%02d.svg", n, diagramNo);
        }
        
        //Create the output file.
        try {
            vennFile = new PrintWriter(fileName);
        } catch (java.io.IOException e) {
            System.err.println("Cannot create the output file of the diagram.");
            System.exit(1);
        }
        
        try {
	        //Writing SVG header of the diagram
	        vennFile.println("<?xml version=\"1.0\" standalone=\"no\"?>");
	        vennFile.println("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"");
	        vennFile.println("\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">");
	        if (mode == 0) {
		        //Defining dimension of the diagram. 
	        	int width = 1000;
	        	int height = 1000;
	        	
	        	switch (n) {
	        	case 3 :
	        		width = height = 250;
	        		break;
	        	case 5 :
	        		width = height = 650;
	        		break;
	        	case 7 :
	        		width = height = 1200;
	        		break;
	        	case 11 :
	        		width = height = 5200;
	        		break;
	        	case 13 :
	        		width = height = 11500;
	        	}
	    
		        horizontalOffset = width / 2.0;
		        verticalOffset = height / 2.0;
	        	//vennFile.println("<svg width=\"6950\" height=\"6950\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">");
	        	vennFile.println("<svg width=\"" + width + "\" height=\"" + height + "\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">");
	        }
	        else {
		        //Defining the unit lengths
		        horizontalUnitLen = verticalUnitLen = 2 * Math.PI /(n * sectorLen);			//Horizontal unit length
		        
		        //Defining dimension of the diagram. 
		       int width = (int) ((2 * points[0][0].x + n * sectorLen) * horizontalUnitLen * n * n * n);
		       int height = (int) ((10 + sectorLen + (points[0][0].y - points[n-2][0].y)) * verticalUnitLen * n * n * n);
		        vennFile.println("<svg width=\"" + width + "\" height=\"" + height + "\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">");
		        
		        //Computing the vertical and horizontal offsets. These values are added to the x and y coordinate of each point
		        //for alignment purposes.
		        verticalOffset = (10 + sectorLen + (points[0][0].y - points[n-2][0].y)) * verticalUnitLen * n * n * n / 2.0;
		        horizontalOffset = 0;
	        }
	        
	        innerFace = "<path d=\"";
	        
	        double nextX = 0.0;
	        
		    for (int i = 0; i < n-1; i++) {
		    	
		    	//Find the x coordinate of the starting region in ring (i + 1)
	        	double startX = points[i][startIndex[i]].x;
	        	
	        	//Draw all regions of the ring
	        	for (int j = 0; j < points[i].length; j++) {
	        		
	        		//Find the index of next region in the ring
	        		int k = (startIndex[i] + j) % points[i].length;
	        		
	        		//Create n copies of the region, one per each sector.
	            	for (int sector = 0; sector < n; sector++) {
	            	    nextX = drawRegion(vennFile, sector, mode, i, k, startX);
	        		}
	            	startX = nextX;
	        	}
	        }
	        
		    //Creating the innermost region of the diagram.
	        if (mode == 0) {
	         
	        	 innerFace = innerFace + "\" stroke=\"none\" stroke-width=\"1\" fill=\""+ colors[n-1] + "\"/>";
	        
	        	 vennFile.println(innerFace);
	        }
	        
	        //End of SVG file. Adding the closing tag.
	        vennFile.println("</svg>");
	        
        }
        catch (Exception e) {
        	System.err.println("Error in writting to the output file of digram No. " + diagramNo);
        }
        finally {
        	vennFile.close();
        }
    	
    }
    
    /**
     * Creates a plot of the Venn diagram without filling the regions (only the curves).
     */
    
    public void drawCurves() {
    	
        PrintWriter vennFile = null;
        String fileName = String.format("VD_%2d_%02d.svg", n, diagramNo);
        //String fileName = "Newroz.svg";
        
        //Create the output file.
        try {
            vennFile = new PrintWriter(fileName);
        } catch (java.io.IOException e) {
            System.err.println("Cannot create the output file of the diagram.");
            System.exit(1);
        }
        
        try {
	        //Writing SVG header of the diagram
	        vennFile.println("<?xml version=\"1.0\" standalone=\"no\"?>");
	        vennFile.println("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"");
	        vennFile.println("\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">");
	        
	    	//Getting the total length of the sector (pie-slice).
	        double sectorLen = points[0][0].nextLen[1];
	        
	        verticalUnitLen = 2 * Math.PI / (n * sectorLen);                       //Vertical unit length
	        horizontalUnitLen = 2 * Math.PI / (n * sectorLen);			//Horizontal unit length
	        
        	int width = 1000;
        	int height = 1000;
        	
        	switch (n) {
        	case 3 :
        		width = height = 250;
        		break;
        	case 5 :
        		width = height = 650;
        		break;
        	case 7 :
        		width = height = 1200;
        		break;
        	case 11 :
        		width = height = 5200;
        		break;
        	case 13 :
        		width = height = 11500;
        	}
    
	        horizontalOffset = width / 2;
	        verticalOffset = height / 2;
	        
	        //Defining dimension of the diagram. 
	        vennFile.println("<svg width=\"" + width + "\" height=\"" + height + "\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">");
	        
	        //Drawing the curves
	        for (int curveNo = 0; curveNo < n; curveNo++) {
	        	drawCurve(vennFile, curveNo, 0, colors[curveNo]);
	        }
    	
	        //End of SVG file. Adding the closing tag.
	        vennFile.println("</svg>");
	        
        }
        catch (Exception e) {
        	System.err.println("Error in writting to the output file of digram No. " + diagramNo);
        }
        finally {
        	vennFile.close();
        }
    }
    
    /**
     * Draws a cylindrical plot of the rotationally symmetric Venn diagram. 
     */
    
    public void drawCylindrical() {
    	
    	/*
    	 * Each curve of diagram is divided into n segments, one per sector. 
    	 * Because of the rotational symmetry the i-th segment of the first curve is simply the fist segment 
    	 * of the i-th curve shifted by 2i*pi / n. Therefore, following the first curve of the diagram, we creates all
    	 * n segments of this curves, shifting the i-th segment by 2i*pi/n radians to the left. In this way, we can 
    	 * construct the first sector of the diagram. The other n-1 sectors are simply copies of the first one. Each
    	 * curve segment is composed of a path of line segments.
    	 */
        
        //Create the output file.
        PrintWriter vennFile = null;
        String fileName = String.format("VD_%2d_Cylindrical_%02d.svg", n, diagramNo);
        
        try {
            vennFile = new PrintWriter(fileName);
        } catch (java.io.IOException e) {
            System.err.println("Cannot create the output file of the diagram.");
            System.exit(1);
        }
        
        try {
	        //Writing SVG header of the diagram
	        vennFile.println("<?xml version=\"1.0\" standalone=\"no\"?>");
	        vennFile.println("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"");
	        vennFile.println("\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">");
	        
	        //Computing the length of a sector of the diagram.
	        double sectorLen = points[0][0].nextLen[1];

	        //Defining the unit lengths
	        horizontalUnitLen = verticalUnitLen = 2 * Math.PI /(n * sectorLen);			//Horizontal unit length
	        
	        //Defining dimension of the diagram. 
	        int width = (int) ((2 * points[0][0].x + n * sectorLen) * horizontalUnitLen * n * n * n);
	        int height = (int) ((10 + sectorLen + (points[0][0].y - points[n-2][0].y)) * verticalUnitLen * n * n * n);
	        vennFile.println("<svg width=\"" + width + "\" height=\"" + height + "\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">");
	        
	        //Computing the vertical and horizontal offsets. These values are added to the x and y coordinate of each point
	        //for alignment purposes.
	        verticalOffset = (10 + sectorLen + (points[0][0].y - points[n-2][0].y)) * verticalUnitLen * n * n * n / 2.0;
	        horizontalOffset = 0;
	        
	        int curveNo = 0;
	        for (int sectorNo = 0; sectorNo < n; sectorNo++) {
	        	//Computing the x-coordinate of the left boundary of the sector
			    double startX = points[0][0].x + sectorNo * sectorLen;
			    
			    //Varibales for the coordinate of turning points
		        double headPointX;
		        double headPointY;
		        
		        //For the coordinate of the right end point of the curve segment. This is where a curve touches the right boundary
		        // of the sector.
		        double rightEndX, rightEndY;
		        
		        //For the coordinates of destination of a line segment.
		        double destinationX;
		        double destinationY;
		        
		        //For the coordinates of the origin of a line segment.
		        double originX = startX;
		        double originY = points[0][0].y;
		        
		        int row = 0;
		        int column = 0;
		        
		        int dir = 1;     //Indicates the slope of line segments.
		        
		        //Starting a new curve segment.
		        String curveStr = "<path d=\"M" + getPointString(0, 1, originX, originY);
		        
		        do {
		        	//If the next point is not in the same row
		            if (points[row][column].nextPoints[0][2 - dir] != 0) {
		            	//Get the x coordinate of the next point.
		            	destinationX = originX + points[row][column].nextLen[2 - dir];
		            	//Get the row and column number of the next point.
		                column = points[row][column].nextPoints[1][2 - dir];
		                row = row - dir;
		                //Get the y-coordinate of the next point.
		                destinationY = points[row][column].y;
		                
		                //If the next point passes the right boundary of the sector, find the point at which the curve
		                //intersects the boundary.
		                if (destinationX - startX > sectorLen) {
		                    rightEndX = startX + sectorLen;
		                    rightEndY = destinationY - dir * (destinationX - rightEndX);
		                    
		                    //Add a line segment from the current point to the boundary point.
		                    curveStr = curveStr + "L" + getPointString(0, 1, rightEndX, rightEndY);
		                    //Write the path to the file
		                    curveStr = curveStr + "\" stroke=\"" + colors[curveNo] + "\" stroke-width=\"1.0\" fill=\"none\"/>";
		                    vennFile.println(curveStr);
		                    
		                    
		                    /*
		                     * Start a new curve segment
		                     */
		                    curveNo = (curveNo + 1) % n;
		                    destinationX -= sectorLen;
		                    curveStr = "<path d=\"M" + getPointString(0, 1, startX, rightEndY);
		                }
		            } 
		            else {
		            	/*
		            	 * Otherwise compute the coordinates of the head point.
		            	 */
		                destinationX = originX + points[row][column].nextLen[2 - dir];
		                destinationY = points[row][points[row][column].nextPoints[1][2 - dir]].y;
		                headPointX = getBalancedX(originX, originY, destinationX, destinationY, dir);
		                headPointY = getBalancedY(originX, originY, destinationX, destinationY, dir);
		                
		                //If the head point passes the right boundary, find the intersection point.
		                if (headPointX - startX > sectorLen) {
		                    rightEndX = startX + sectorLen;
		                    rightEndY = headPointY - dir * (headPointX - rightEndX);
		                    
		                    //Add a new line segment from the current point to the boundary point.
		                    curveStr = curveStr + "L" + getPointString(0, 1, rightEndX, rightEndY);
		                    curveStr = curveStr + "\" stroke=\"" + colors[curveNo] + "\" stroke-width=\"1.0\" fill=\"none\"/>";
		                    vennFile.println(curveStr);
		                    
		                    //Start a new curve segment.
		                    curveNo = (curveNo + 1) % n;
		                    
		                    headPointX -= sectorLen;
		                    destinationX -= sectorLen;
		                    curveStr = "<path d=\"M" + getPointString(0, 1, startX, rightEndY); 
		                }
		                
		                //Add the line segment from the current/boundary point to the head point. 
		                curveStr = curveStr + "L" + getPointString(0, 1, headPointX, headPointY);
		                
		                //If the next point passes the right boundary of the sector
		                if (destinationX - startX > sectorLen){
		                	//Compute the coordinates of the boundary point
		                    rightEndX = startX + sectorLen;
		                    rightEndY = destinationY + dir * (destinationX - rightEndX);
		                    
		                    //Add a line segment from the current point to the boundary point.
		                    curveStr = curveStr + "L" + getPointString(0, 1, rightEndX, rightEndY);
		                    curveStr = curveStr + "\" stroke=\"" + colors[curveNo] + "\" stroke-width=\"1.0\" fill=\"none\"/>";
		                    vennFile.println(curveStr);
		                    
		                    //Start a new curve segment.
		                    curveNo = (curveNo + 1) % n;	                    
		                    destinationX -= sectorLen;
		                    curveStr = "<path d=\"M" + getPointString(0, 1, startX, rightEndY);
		                }
		                
		                //Change the slope.
		                dir = -dir;
		                
		                column = points[row][column].nextPoints[1][2 + dir];
		            }
		            
		            //Add a line segment from the current/head/boundary point to the next point.
		            curveStr = curveStr + "L" + getPointString(0, 1, destinationX, destinationY);
		            
		            originX = destinationX;
		            originY = destinationY;
		            
		            //While we haven't reached to the starting point.
		        } while(row != 0 || column != 0 || dir == -1);
	    	
	            curveStr = curveStr + "\" stroke=\"" + colors[curveNo] + "\" stroke-width=\"1.0\" fill=\"none\"/>";
	            vennFile.println(curveStr);
	        }
	        //End of SVG file. Adding the closing tag.
	        vennFile.println("</svg>");
        
        }
        catch (Exception e) {
        	System.err.println("Error in writting to the output file of digram No. " + diagramNo);
        }
        finally {
        	vennFile.close();
        }
    }
    
}
