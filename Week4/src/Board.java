import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {
    private int[][] board;
    private final int board_dim;
    private int hamming_dist;
    private int manhattan_dist;
    private int empty_pos;
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles){
        if (tiles == null){
            throw new IllegalArgumentException("inital board cannot be null.");
        }
        board = new int[tiles.length][];
        hamming_dist = 0;
        manhattan_dist = 0;

        // Make a copy of the board, and calculate the distance        
        int val = 0;
        int row, col;
        for(int i = 0; i < tiles.length; i++){
            board[i] = tiles[i].clone();
            for(int j = 0; j < tiles.length; j++){
                val = tiles[i][j];
                if(val > 0){
                    row = (val-1)/tiles.length;
                    col = (val-1)%tiles.length;
                    if (i != row || j != col){
                        hamming_dist++;
                        manhattan_dist += Math.abs(row-i)+Math.abs(col-j);
                    }
                }
                else{
                    empty_pos = i*tiles.length + j;
                }
            }

        }

        board_dim = tiles.length;
        

    }
                                           
    // string representation of this board
    public String toString(){
        String tile_str = "";
        tile_str += board_dim + "\n";
        for(int i = 0; i < board_dim; i++){
            for(int j = 0; j < board_dim; j++){
                tile_str += board[i][j] + " ";
            }
            tile_str += "\n";
        }
        return tile_str;
    }

    // board dimension n
    public int dimension(){
        return board_dim;
    }

    // number of tiles out of place
    public int hamming(){
        return hamming_dist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan(){
        return manhattan_dist;
    }

    // is this board the goal board?
    public boolean isGoal(){
        int last_val = board_dim * board_dim;
        for(int i=0; i<board_dim;i++){
            for(int j=0; j<board_dim;j++){
                if (board[i][j] != (1+j+i*board_dim)%last_val)
                    return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y){
        if (y != null && y.getClass().isInstance(this) ){
            if (y.toString().equals(toString()))
                return true;
        }
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors(){
        return new neighbourIterable();
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin(){
        int[][] new_board = new int[board_dim][board_dim];

        for(int i = 0; i < board_dim; i++)
            new_board[i] = board[i].clone();
        if (board_dim>1){
            int empty_row = empty_pos/board_dim;
            int empty_col= empty_pos%board_dim;
            
            int target_row = 0;
            if (empty_row == 0 && empty_col<2)
                target_row = 1;

            new_board[target_row][0] += new_board[target_row][1];
            new_board[target_row][1] = new_board[target_row][0] - new_board[target_row][1];
            new_board[target_row][0] -= new_board[target_row][1];
        }

        return new Board(new_board);
    }

    private class neighbourIterable implements Iterable<Board>{
        private Board[] neighbours;
        private int n_neighbours;

        public neighbourIterable(){
            int empty_row = empty_pos/board_dim;
            int empty_col= empty_pos%board_dim;
            
            neighbours = new Board[4];
            n_neighbours=0;
            int[][] new_board = new int[board_dim][board_dim];

            for(int i = 0; i < board_dim; i++)
                new_board[i] = board[i].clone();                
            if (empty_row > 0){
                new_board[empty_row][empty_col] = new_board[empty_row-1][empty_col]; 
                new_board[empty_row-1][empty_col] = 0;
                neighbours[n_neighbours++] = new Board(new_board);
                new_board[empty_row-1][empty_col] = new_board[empty_row][empty_col]; 
                new_board[empty_row][empty_col] = 0;
            }
            if (empty_row < board_dim-1){
                new_board[empty_row][empty_col] = new_board[empty_row+1][empty_col]; 
                new_board[empty_row+1][empty_col] = 0;
                neighbours[n_neighbours++] = new Board(new_board);
                new_board[empty_row+1][empty_col] = new_board[empty_row][empty_col]; 
                new_board[empty_row][empty_col] = 0;
            }
            
            if (empty_col > 0){
                new_board[empty_row][empty_col] = new_board[empty_row][empty_col-1]; 
                new_board[empty_row][empty_col-1] = 0;
                neighbours[n_neighbours++] = new Board(new_board);
                new_board[empty_row][empty_col-1] = new_board[empty_row][empty_col]; 
                new_board[empty_row][empty_col] = 0;

            }
            if (empty_col < board_dim-1){
                new_board[empty_row][empty_col] = new_board[empty_row][empty_col+1]; 
                new_board[empty_row][empty_col+1] = 0;
                neighbours[n_neighbours++] = new Board(new_board); 
                new_board[empty_row][empty_col+1] = new_board[empty_row][empty_col]; 
                new_board[empty_row][empty_col] = 0;               
            }
            
        }

        public Iterator<Board> iterator(){
            return new neighbourIterator();
        }

        private class neighbourIterator implements Iterator<Board>{
            int current = 0;

            public boolean hasNext(){
                return current < n_neighbours;
            }

            public void remove(){
                throw new UnsupportedOperationException("Remove operation is not supported.");
            }

            public Board next(){
                if (!hasNext()){
                    throw new NoSuchElementException("No next item.");
                }
                return neighbours[current++];
            }
        }
    }


    // unit testing (not graded)
    public static void main(String[] args){
        int size = 3;
        int[][] tiles = new int[size][size];
        for (int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                tiles[i][j] = i*size+j+1;
            }
        }
        tiles[size-1][size-1] = tiles[1][1];
        tiles[1][1] = 0;

        Board board = new Board(tiles);
        Board board2 = new Board(tiles);

        System.out.println(board.toString());
        System.out.println(board.manhattan());
        System.out.println(board.hamming());
        System.out.println(board.isGoal());
        System.out.println(board.equals(null));
        System.out.println(board.equals(tiles));
        System.out.println(board.equals(board2));

        board2 = board.twin();
        System.out.println(board2.toString());
        System.out.println(board.equals(board2));
        System.out.println(board.toString());

        Iterable<Board> board_neighbours = board.neighbors();
        Iterator<Board> iter = board_neighbours.iterator();

        while(iter.hasNext()){
            System.out.println(iter.next().toString());
        }

        
    }

}