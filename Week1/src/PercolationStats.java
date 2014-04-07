/**
 * This is a class which creates a data type to perform a series of computational experiments for Percolation
 * @version 02/19/2014
 * @author
 */
public class PercolationStats 
{
    private double [] fractions;
    private int numOfExperiments;
    
    /**
     * Perform T independent computational experiments on an N-by-N grid
     * @param N Size of the grid
     * @param T Number of experiments
     */
   public PercolationStats(int N, int T)
   {
       if (N < 1)
       {
           throw new IllegalArgumentException("N must >= 1");
       }
       else if (T < 1)
       {
           throw new IllegalArgumentException("T must >= 1");
       }
       else
       {
           numOfExperiments = T;
           fractions = new double[T];
           for (int i = 0; i < T; i++)
           {
               double numOfOpenSites = 0.0;
               Percolation percolationSystem = new Percolation(N);
               while (!percolationSystem.percolates())
               {
                   int row = StdRandom.uniform(1, N + 1);
                   int col = StdRandom.uniform(1, N + 1);
                   if (!percolationSystem.isOpen(row, col))
                   {
                       percolationSystem.open(row, col);
                       numOfOpenSites++;
                   }
               }
               fractions[i] = numOfOpenSites / (N * N);
           }
       }
   }
   
   /**
    * sample mean of percolation threshold
    * @return The average fraction of all the experiments
    */
   public double mean()
   {
       return StdStats.mean(fractions);
   }
   
   /**
    * Sample standard deviation of percolation threshold
    * @return The standard deviation of the percolation threshold
    */
   public double stddev()
   {
       if (fractions.length == 1)
       {
           return Double.NaN;
       }
       else
       {
           return StdStats.stddev(fractions);
       }
   }
   
   /**
    * Calculate the lower bound of the 95% confidence interval
    * @return lower bound of the 95% confidence interval
    */
   public double confidenceLo()
   {
       double avgFractions = this.mean();
       double deviation = this.stddev();
       return avgFractions - (1.96 * deviation / Math.sqrt(numOfExperiments));
   }
   
   /**
    * Calculate the upper bound of the 95% confidence interval
    * @return upper bound of the 95% confidence interval
    */
   public double confidenceHi()
   {
       double avgFractions = this.mean();
       double deviation = this.stddev();
       return avgFractions + (1.96 * deviation / Math.sqrt(numOfExperiments));
   }
   
   /**
    * The test client
    * @param args
    */
   public static void main(String[] args)
   {
       int N = Integer.parseInt(args[0]);
       int T = Integer.parseInt(args[1]);
       PercolationStats experiment = new PercolationStats(N, T);
       System.out.println("mean                    =" + Double.toString(experiment.mean()));
       System.out.println("stddev                  =" + Double.toString(experiment.stddev()));
       System.out.println("95% confidence interval =" + Double.toString(experiment.confidenceLo()) 
                        + ", " + Double.toString(experiment.confidenceHi()));
   }
}