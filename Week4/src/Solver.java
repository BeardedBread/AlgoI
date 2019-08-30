//import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    // This class is a linked list for storing checked boards
    /*private class CheckedBoard{
        public Board board;
        public CheckedBoard next;
    }*/

    // This class is used to store the state    
    private class BoardState implements Comparable<BoardState>{
        public Board current_board;
        public BoardState prev_state;
        public int moves;
        private int priority;

        public BoardState(Board board, BoardState prev_state, int moves){
            current_board = board;
            this.prev_state = prev_state;
            this.moves = moves;
            //priority = board.hamming() + moves;
            priority = board.manhattan() + moves;
        }

        public int compareTo(BoardState that) {
            return this.priority - that.priority;            
        }

    }

    private boolean solvable;
    private BoardState final_state;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial){
        if (initial == null){
            throw new IllegalArgumentException("inital board cannot be null.");
        }

        // Initialise the initial BoardState
        BoardState initialBoardState = new BoardState(initial, null, 0);

        // Initialise minPQ and put the initial BoardState
        MinPQ<BoardState> priority_queue = new MinPQ<BoardState>();
        priority_queue.insert(initialBoardState);

        // Get also the twin version and initial its own minPQ
        MinPQ<BoardState> priority_queue_twin = new MinPQ<BoardState>();
        priority_queue_twin.insert(new BoardState(initial.twin(), null, 0));

        //Initiate checkedboard linked list
        /*CheckedBoard head = new CheckedBoard();
        head.board = initialBoardState.current_board;
        head.next = null;

        //Do the same for the twin
        CheckedBoard head_twin = new CheckedBoard();
        head_twin.board = initial.twin();
        head_twin.next = null;*/

        BoardState state = initialBoardState;
        Iterable<Board> state_neighbours;
        Iterator<Board> iter;
        //CheckedBoard new_board;

        // While both PQ is not empty
        while(!(priority_queue.isEmpty() && priority_queue_twin.isEmpty())){
            // delmin
            if (!priority_queue.isEmpty()){
                state = priority_queue.delMin();

                //check isGoal
                if (!state.current_board.isGoal()){
                    //If not, get the neighbours.
                    state_neighbours = state.current_board.neighbors();
                    iter = state_neighbours.iterator();
                    
                    //For each neighbour
                    Board next_board;
                    while(iter.hasNext()){
                        next_board = iter.next();
                        boolean checked = false;
                        BoardState current_state = state;
                        for(int i=0;i<1;i++){
                            current_state = current_state.prev_state;
                            if (current_state == null){
                                break;
                            }
                            else{
                                if(next_board.equals(current_state.current_board)){
                                    checked = true;
                                    break;
                                }
                            }                   

                        }
                        /*CheckedBoard current_checkedBoard = head;
                        // Check if the board has been checked before
                        while(current_checkedBoard != null){
                            if(next_board.equals(current_checkedBoard.board)){
                                checked = true;
                                break;
                            }
                            current_checkedBoard = current_checkedBoard.next;                      
                        }*/
                        //If not create a new BoardState
                        if (!checked){
                            BoardState new_state = new BoardState(next_board, state, state.moves+1);
                            //Push to PQ
                            priority_queue.insert(new_state);
                            // Put current board in checkedBoard list
                            /*new_board = new CheckedBoard();
                            new_board.board = next_board;
                            new_board.next = head;
                            head = new_board;*/
                        }
                    }
                }else{
                    //Otherwise, break
                    break;
                }

            }
            
            if (!priority_queue_twin.isEmpty()){
                // REPEAT FOR THE TWIN
                state = priority_queue_twin.delMin();

                //check isGoal
                if (!state.current_board.isGoal()){
                    //If not, get the neighbours.
                    state_neighbours = state.current_board.neighbors();
                    iter = state_neighbours.iterator();
                    //For each neighbour, check if in checkedState
                    Board next_board;
                    while(iter.hasNext()){
                        next_board = iter.next();
                        boolean checked = false;
                        BoardState current_state = state;
                        for(int i=0;i<1;i++){
                            current_state = current_state.prev_state;
                            if (current_state == null){
                                break;
                            }
                            else{
                                if(next_board.equals(current_state.current_board)){
                                    checked = true;
                                    break;
                                }
                            }    
                        }
                        /*CheckedBoard current_checkedBoard = head_twin;
                        while(current_checkedBoard != null){
                            if(next_board.equals(current_checkedBoard.board)){
                                checked = true;
                                break;
                            } 
                            current_checkedBoard = current_checkedBoard.next;                     
                        }*/
                        //If not create a new BoardState, set prev to this
                        if (!checked){
                            BoardState new_state = new BoardState(next_board, state, state.moves+1);
                            //Push to PQ
                            priority_queue_twin.insert(new_state);
                            // Put current board in checkedBoard
                            /*new_board = new CheckedBoard();
                            new_board.board = state.current_board;
                            new_board.next = head_twin;
                            head_twin = new_board;*/
                        }
                    }
                }else{
                    //Otherwise, break
                    break;
                }
            }


        }
            
        // The puzzle must be solvable for either one of them
        // Check if the starting board is original or twin
        // If original, solvable; otherwise no.
        BoardState current_state = state;
        while(current_state.prev_state != null){
            current_state = current_state.prev_state;
        }
        solvable = current_state.current_board.equals(initialBoardState.current_board);
        if (solvable){
            final_state = state;
        }else{
            final_state = initialBoardState;
        }
        

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable(){
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves(){
        if (solvable)
            return final_state.moves;
        return -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution(){
        if (solvable)
            return new solutionIterable();
        return null;        
    }

    private class solutionIterable implements Iterable<Board>{
        Board[] solution_boards;

        public solutionIterable(){
            //generate the iterable by tracing back from
            //the final BoardState using prev_board recursively
            //Number fo recursions determined by moves;
            solution_boards = new Board[final_state.moves+1];
            BoardState current_state = final_state;
            for(int i=final_state.moves;i>=0;i--){
                solution_boards[i] = current_state.current_board;
                current_state = current_state.prev_state;
            }

        }

        public Iterator<Board> iterator(){
            return new solutionIterator();
        }

        private class solutionIterator implements Iterator<Board>{
            int current = 0;

            public boolean hasNext(){
                return current < solution_boards.length;
            }

            public void remove(){
                throw new UnsupportedOperationException("Remove operation is not supported.");
            }

            public Board next(){
                if (!hasNext()){
                    throw new NoSuchElementException("No next item.");
                }
                return solution_boards[current++];
            }
        }
    }    
    
    // test client (see below) 
    public static void main(String[] args){// create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        StdOut.println(initial.toString());
    
        // solve the puzzle
        Solver solver = new Solver(initial);
    
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}