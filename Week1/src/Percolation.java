/*----------------------------------------------------------------
 * Author: 
 * Written: 02/18/2014
 * Last Updated: 02/18/2014
 * 
 * This class is a data type that models the percolation system
 *----------------------------------------------------------------*/
public class Percolation 
{
    private int gridSize;
    private boolean[][] grid;
    private WeightedQuickUnionUF unionFind;
    private WeightedQuickUnionUF unionFindPreventBackWash;
    
   /**
    * create N-by-N grid, with all sites blocked 
    * @param N Dimension of the grid
    */
   public Percolation(int N)              
   {
       gridSize = N;
       grid = new boolean[gridSize][gridSize];
       //Create a WeightQuickUnionUF instance
       unionFind = new WeightedQuickUnionUF(gridSize*gridSize+2); 
       //Create another WeightQuickUnionUF instance to use in isFull() method to
       //prevent backwash
       unionFindPreventBackWash = new WeightedQuickUnionUF(gridSize*gridSize+2);
       
   } 
    
   /**
    * Open site (row i, column j) if it is not already
    * @param i The row index from 1 to N
    * @param j The column index from 1 to N
    */
   public void open(int i, int j)
   {
       this.isIdxValid(i, j);
       if (!isOpen(i, j)) 
       {
           grid[i-1][j-1] = true;
           if (i == 1) 
           {
               unionFind.union(this.xyTo1D(i, j), 0);
               unionFindPreventBackWash.union(this.xyTo1D(i, j), 0);
           } 
           
           if (i == gridSize) 
           {
               unionFind.union(this.xyTo1D(i, j), gridSize * gridSize + 1);
           }
           
           this.union(i, j, i-1, j);
           this.union(i, j, i+1, j);
           this.union(i, j, i, j-1);
           this.union(i, j, i, j+1);
        }
   }
   
   /**
    * Check if site (row i, column j) is open
    * @param i The row index from 1 to N 
    * @param j The column index from 1 to N
    * @return True if the site is open, false if not
    */
   public boolean isOpen(int i, int j)
   {
       this.isIdxValid(i, j);
       return grid[i-1][j-1];
   }
   
   /**
    * Check if the site (row i, column j) is full
    * @param i The row index from 1 to N
    * @param j The column index from 1 to N
    * @return True if the site is full, false if not
    */
   public boolean isFull(int i, int j)
   {
       this.isIdxValid(i, j);
       return unionFindPreventBackWash.connected(xyTo1D(i, j), 0);
   }
   
   /**
    * Check if the grid is percolate
    * @return True if the grid percolates, false if not
    */
   public boolean percolates() 
   {
       return unionFind.connected(0, gridSize*gridSize + 1);
   }
   
   /**
    * Convert 2-dimensional (row, column) pair to a 1-dimensional Union-Find 
    * object index
    * @param i The row index from 1 to N
    * @param j The column index from 1 to N
    * @return The index for the 1-dimensional WeightedQuickUnionUF data structure
    */
   private int xyTo1D(int i, int j)
   {
       return gridSize*(i-1)+j;
   }
   
   /**
    * Validate if the indices are out of bounds
    * @param i The row index from 1 to N
    * @param j The column index from 1 to N
    */
   private void isIdxValid(int i, int j)
   {
       if (i < 1 || i > gridSize)
       {
           throw new IndexOutOfBoundsException("row index i out of bounds");
       }
       else if (j < 1 || j > gridSize)
       {
           throw new IndexOutOfBoundsException("column index j out of bounds");
       }
   }
   
   /**
    * Connect two neighboring sites if they are all open
    * @param row1 The row index of the first site
    * @param col1 The column index of the first site
    * @param row2 The row index of the second site
    * @param col2 The column index of the second site
    */
   private void union(int row1, int col1, int row2, int col2)
   {
       if (row2 > 0 && row2 <= gridSize && col2 > 0 && col2 <= gridSize && isOpen(row2, col2))
       {
           unionFind.union(xyTo1D(row1, col1), xyTo1D(row2, col2));
           unionFindPreventBackWash.union(xyTo1D(row1, col1), xyTo1D(row2, col2));
       }
   }
}