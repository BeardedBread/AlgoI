import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
//import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private int trials;
    private double results_mean;
    private double results_stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials){
        if (n<=0 || trials<=0){
            throw new IllegalArgumentException("n and trials should be non-zero: (" + n + " " + trials + ")");
        }

        this.trials = trials;
        double[] trial_results = new double[trials];
        Percolation grid;
        for (int i=0;i<trials; i++){
            grid = new Percolation(n);
            int row, col;
            while(!grid.percolates()){
                row = StdRandom.uniform(1, n+1);
                col = StdRandom.uniform(1, n+1);
                grid.open(row, col);
            }
            trial_results[i] = (double)grid.numberOfOpenSites()/(n*n);
        }
        results_mean = StdStats.mean(trial_results);
        results_stddev = StdStats.stddev(trial_results);
    }

    // sample mean of percolation threshold
    public double mean(){
        return results_mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return results_stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        double error = 1.96 * results_stddev / Math.sqrt(trials);
        return results_mean - error;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        double error = 1.96 * results_stddev / Math.sqrt(trials);
        return results_mean + error;
    }

   // test client (see below)
    public static void main(String[] args){
        if (!(args.length == 2)){
            throw new IllegalArgumentException("Only 2 arguments are required");
        }
        int n, trials;
        try{
            n = Integer.parseInt(args[0]);
            trials = Integer.parseInt(args[1]);
        }catch(NumberFormatException e){
            System.out.println("Please give integers as inputs.");
            throw(e);
        }        

        //Stopwatch clock = new Stopwatch();
        PercolationStats percol_stats = new PercolationStats(n, trials);
        //System.out.printf("%-45s = %.20f\n", "Time elapsed", clock.elapsedTime());        

        System.out.printf("%-45s = %.20f\n", "mean", percol_stats.mean());
        System.out.printf("%-45s = %.20f\n", "stddev", percol_stats.stddev());
        System.out.printf("%-45s = [%.20f, %.20f]\n", "95%% confidence interval", percol_stats.confidenceLo(), percol_stats.confidenceHi());
    }

}