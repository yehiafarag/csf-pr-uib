package probe.com.view.core.chart4j;
/**
 * This class computes the binomial coefficient n choose k for 0 <= k <= n <= 100.
 * It is the same as the number of all distinct subsets of the set {1,2,...,n} with k elements.
 * It is also the number of all binary strings of length n containing exactly k bits of 0.
 * 
 * @author Khalegh
 * @since July 12, 2009
 */
public class BinomialCoeff {
  
	/**
	 * The maximum size of the binomial coefficients table.
	 */
    private static final int MAX_SIZE = 100;
    
    /**
     * Table for precomputing the binomial coefficients.
     */
    private static int [][] binomial;
    
    /*
     * Precomputing the binomial coefficients using dynamic programming based on the recurrence :
     *                  n choose k = (n-1) choose k  + (n-1) choose (k-1).
     */
    static {
    	//Allocating memory.
    	binomial = new int[MAX_SIZE + 1][MAX_SIZE + 1];
        
        //Computing boundary conditions (diagonal entries and the leftmost columns.
        for (int i = 0; i <= MAX_SIZE; i++) {
        	binomial[i][i] = 1;
        	binomial[i][0] = 1;
        }
        
        //Given binomial[i-1][j] and binomial[i-1][j-1] it is easy to compute binomial[i][j].
        for (int i = 1; i <= MAX_SIZE; i++) {
            for (int j = 1; j < i; j++) {
            	binomial[i][j] = binomial[i-1][j] + binomial[i-1][j-1];
            }
        }
    }
    
    /**
     * Returns the binomial coefficient n choose k.
     * @param n   The total number of elements. 
     * @param k   the number of elements to be chosen.
     * @return n choose k; the number of ways to choose k elements out of the set {1,2,...,n} 
     */
    
    public static int biCoeff(int n, int k) {
        return binomial[n][k];
    }
}
