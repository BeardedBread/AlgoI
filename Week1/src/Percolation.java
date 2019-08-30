import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import edu.princeton.cs.algs4.StdRandom;

public class Percolation {
    private boolean[][] grid;
    private int size;
    private WeightedQuickUnionUF grid_uf;
    private WeightedQuickUnionUF grid_uf_2; //This is to check for isFull to prevent backwash
    private int n_open_sites;
    private int end_site;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){
        if (n<=0){
            throw new IllegalArgumentException("Invalid size: " + n);
        }
        
        grid = new boolean[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                grid[i][j] = false;
            }
        }
        size = n;
        grid_uf = new WeightedQuickUnionUF(n*n+2);  // Need extra 2 virtual sites
        grid_uf_2 = new WeightedQuickUnionUF(n*n+1);  // Need extra 1 virtual sites
        end_site = n*n+1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        if (row<=0 || row>size || col<=0 || col>size){
            throw new IllegalArgumentException("Invalid row and col values: (" + row + ", " + col + ")");
        }
        
        if (!isOpen(row, col)){
            grid[row-1][col-1] = true;
            n_open_sites++;
            int index = col + (row - 1) * size;
        
            if (row == 1){
                grid_uf.union(0, index);
                grid_uf_2.union(0, index);
            }
            if (row == size){
                grid_uf.union(end_site, index);
            }

            // Check up
            if(row>1 && isOpen(row-1, col)){
                grid_uf.union(index - size, index);
                grid_uf_2.union(index - size, index);
            }
            // Check down
            if(row<size && isOpen(row+1, col)){
                grid_uf.union(index + size, index);
                grid_uf_2.union(index + size, index);
            }
            // Check left
            if(col>1 && isOpen(row, col-1)){
                grid_uf.union(index-1, index);
                grid_uf_2.union(index-1, index);
            }
            // Check right
            if(col<size && isOpen(row, col+1)){
                grid_uf.union(index+1, index);
                grid_uf_2.union(index+1, index);
            }
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        if (row<=0 || row>size || col<=0 || col>size){
            throw new IllegalArgumentException("Invalid row and col values: (" + row + ", " + col + ")");
        }
        return grid[row-1][col-1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        if (row<=0 || row>size || col<=0 || col>size){
            throw new IllegalArgumentException("Invalid row and col values: (" + row + ", " + col + ")");
        }
        return grid_uf_2.connected(0, col + (row - 1) * size);
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return n_open_sites;
    }

    // does the system percolate?
    public boolean percolates(){
        return grid_uf.connected(0, end_site);
    }

    // test client (optional)
    public static void main(String[] args){
        int n = 8;
        Percolation grid = new Percolation(n);
        int row, col;
        while(!grid.percolates()){
            row = StdRandom.uniform(1, n+1);
            col = StdRandom.uniform(1, n+1);
            System.out.print(row);
            System.out.println(" " + col);
            grid.open(row, col);
        }
        System.out.println(grid.numberOfOpenSites());
        System.out.println((double)grid.numberOfOpenSites()/(n*n));
        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= n; j++){
                if (grid.isFull(i, j))
                    System.out.printf("%1s", 2);
                else
                    System.out.printf("%1s", grid.isOpen(i, j)?1:0);
            }
            System.out.println("");
        }
    }
}