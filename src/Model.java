import java.util.LinkedList;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * Adapted from:
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Bayley Tuch, Sabrina Green, and Nicolas Corona
 */

/**
 * This class is a model for 2048.  
 * 
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 */
public class Model {

    // 2D int array containing current tile arrangement
    private static int[][] board;
    
    // LinkedLists to record move history for undo functionality
    private LinkedList<int[][]> boardhistory;
    private LinkedList<Integer> scorehistory;
    
    // int containing current score
    private int score;
    
    // boolean indicating whether game has been won (whether board contains tile of value 2048)
    private boolean gameWon;
    
    // random number generator used to randomly select empty cells to populate
    private Random rand = new Random();

    /**
     * Constructor sets up game state.
     */
    public Model() {
        reset();
    }
    
    /**
     * reset (re-)sets the game state to start a new game, clearing score and history and 
     * resetting board to have all cells empty except for top left and bottom right, which 
     * contain tiles of value 2
     */
    public void reset() {
        board = new int[4][4];
        board[3][3] = 2;
        board[0][0] = 2;
        boardhistory = new LinkedList<int[][]>();
        scorehistory = new LinkedList<Integer>();
        score = 0;
        gameWon = false;
    }
    
    // undoes the most recent move
    public void undo() {
        if (boardhistory.size() > 0 && scorehistory.size() > 0) {
            board = boardhistory.removeFirst();
            score = scorehistory.removeFirst();
        }
    }
    
    // creates a copy of a 2D array
    public static int[][] clone(int[][] org) {
        int rows = org.length;
        int cols = org[0].length;
        int[][] copy = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                copy[r][c] = org[r][c];
            }
        }
        return copy;
    }
    
    // saves game state by writing out score and tile arrangement to the SavedGameOf2048.txt file
    public void exportGame() {
        try {
            FileWriter writer = new FileWriter("SavedGameOf2048.txt", false);
            writer.write(String.valueOf(score));
            writer.write("\r\n");
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    writer.write(String.valueOf(board[r][c]));
                    writer.write("\r\n");
                }
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // reads in saved game state from SavedGameOf2048.txt and modifying score and board accordingly
    public void importGame() {
        try {
            Scanner scan = new Scanner(new File("SavedGameOf2048.txt"));
            if (scan.hasNext()) {
                score = scan.nextInt();
            }
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if (scan.hasNext()) {
                        board[r][c] = scan.nextInt();
                    }
                }
            }
            scan.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // setter functions for testing and for importing
    public void setBoard(int[][] testboard) {
        board = clone(testboard);
    }
    
    public void setScore(int s) {
        score = s;
    }
    
    // getter functions
    public int[][] getBoard() {
        return clone(board);
    }

    public int getScore() {
        return score;
    }
    
    public String getStatus() {
        String res;
        if (this.gameWon) {
            res = "Game Won! --- ";
        } else {
            res = "Game in Play --- ";
        }
        res = res + "Score: " + this.getScore();
        return res;
    }
    
    // checks for existence of tile of value 2048
    private void check2048() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] == 2048) {
                    gameWon = true;
                }
            }
        }
    }
    
    // adds new tile of value 2 to a randomly chosen empty cell 
    // called in slideTiles, which is called after every key press)
    private void addNewTile() {
        int length = 0;
        LinkedList<Integer> emptyRows = new LinkedList<Integer>();
        LinkedList<Integer> emptyColumns = new LinkedList<Integer>();
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (board[r][c] == 0) {
                    length = length + 1;
                    emptyRows.add(r);
                    emptyColumns.add(c);
                }
            }
        }
        if (length > 0) {
            int i = rand.nextInt(length);
            int rn = emptyRows.get(i);
            int cn = emptyColumns.get(i);
            board[rn][cn] = 2;
        }
    }
    
    // slides all of the tiles in the direction indicated by the key
    // pressed by the user; this makes calls to the shift functions
    // defined below for each row/column in the board 3 times per 
    // iteration (to account for the changes in each adjacent pair of
    // tiles) and 3 iterations per row/column (to have each tile slide
    // as far as possible as opposed to only interacting with the tile 
    // adjacent to it and shifting just once)
    //
    // note that order in which pairs are shifted ensures that tile
    // interactions closer to side indicated by direction of movement
    // take precedence; e.g. if board has a row {2, 2, 2, 4} and tiles
    // are slid left, then resulting row will be {4, 2, 4, ?} (last 
    // entry might be 2 due to random selection but 0 if not selected)
    //
    // also adds old board and score to history if changes were made
    // and adds new tiles of value 2 if board is not grid-locked
    public void slideTiles(String direction) {
        int[][] oldboard = clone(board);
        int oldscore = score;
        // System.out.println("oldboard");
        // System.out.println(Arrays.deepToString(oldboard));
        
        if (direction.equals("up")) {
            for (int col = 0; col < 4; col++) {
                for (int i = 0; i < 3; i++) {
                    this.shiftUp(col, 0, 1);
                    this.shiftUp(col, 1, 2);
                    this.shiftUp(col, 2, 3);
                }
            }
        } else if (direction.equals("down")) {
            for (int col = 0; col < 4; col++) {
                for (int i = 0; i < 3; i++) {
                    this.shiftDown(col, 2, 3);
                    this.shiftDown(col, 1, 2);
                    this.shiftDown(col, 0, 1);
                }
            }
        } else if (direction.equals("left")) {
            for (int row = 0; row < 4; row++) {
                for (int i = 0; i < 3; i++) {
                    this.shiftLeft(row, 0, 1);
                    this.shiftLeft(row, 1, 2);
                    this.shiftLeft(row, 2, 3);
                }
            }
        } else if (direction.equals("right")) {
            for (int row = 0; row < 4; row++) {
                for (int i = 0; i < 3; i++) {
                    this.shiftRight(row, 2, 3);
                    this.shiftRight(row, 1, 2);
                    this.shiftRight(row, 0, 1);
                }
            }
        }
        this.check2048();
        int neq = 0;
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (oldboard[r][c] != board[r][c]) {
                    neq++;
                }
            }
        }
        if (neq != 0) {
            boardhistory.addFirst(oldboard);
            scorehistory.addFirst(oldscore);
        }
        this.addNewTile();
    }
    
    // updates the two cells indicated by the row and column parameters
    // when the up key is pressed; if the upper cell has a value of 0,
    // then the tiles swap places, and if the two tiles have the same 
    // value, then the tiles merge and the upper cell contains the new 
    // tile, while the other cell now contains a 0; otherwise, nothing
    // happens
    private void shiftUp(int col, int urow, int drow) {
        if (board[urow][col] == 0) {
            board[urow][col] = board[drow][col];
            board[drow][col] = 0;
        } else if (board[urow][col] == board[drow][col]) {
            int nval = board[urow][col] + board[drow][col];
            if (nval == 2048) {
                gameWon = true;
            }
            score = score + nval;
            board[urow][col] = nval;
            board[drow][col] = 0;
        }
    }
    
    // updates the two cells indicated by the row and column parameters
    // when the down key is pressed; if the lower cell has a value of 0,
    // then the tiles swap places, and if the two tiles have the same 
    // value, then the tiles merge and the lower cell contains the new 
    // tile, while the other cell now contains a 0; otherwise, nothing
    // happens
    private void shiftDown(int col, int urow, int drow) {
        if (board[drow][col] == 0) {
            board[drow][col] = board[urow][col];
            board[urow][col] = 0;
        } else if (board[drow][col] == board[urow][col]) {
            int nval = board[drow][col] + board[urow][col];
            if (nval == 2048) {
                gameWon = true;
            }
            score = score + nval;
            board[drow][col] = nval;
            board[urow][col] = 0;
        }
    }
    
    // updates the two cells indicated by the row and column parameters
    // when the left key is pressed; if the left cell has a value of 0,
    // then the tiles swap places, and if the two tiles have the same 
    // value, then the tiles merge and the left cell contains the new 
    // tile, while the other cell now contains a 0; otherwise, nothing
    // happens
    private void shiftLeft(int row, int lcol, int rcol) {
        if (board[row][lcol] == 0) {
            board[row][lcol] = board[row][rcol];
            board[row][rcol] = 0;
        } else if (board[row][lcol] == board[row][rcol]) {
            int nval = board[row][lcol] + board[row][rcol];
            if (nval == 2048) {
                gameWon = true;
            }
            score = score + nval;
            board[row][lcol] = nval;
            board[row][rcol] = 0;
        }
    }
    
    // updates the two cells indicated by the row and column parameters
    // when the right key is pressed; if the right cell has a value of 0,
    // then the tiles swap places, and if the two tiles have the same 
    // value, then the tiles merge and the right cell contains the new 
    // tile, while the other cell now contains a 0; otherwise, nothing
    // happens
    private void shiftRight(int row, int lcol, int rcol) {
        if (board[row][rcol] == 0) {
            board[row][rcol] = board[row][lcol];
            board[row][lcol] = 0;
        } else if (board[row][rcol] == board[row][lcol]) {
            int nval = board[row][rcol] + board[row][lcol];
            if (nval == 2048) {
                gameWon = true;
            }
            score = score + nval;
            board[row][rcol] = nval;
            board[row][lcol] = 0;
        }
    }

    // getter function for tile value
    public int getTile(int r, int c) {
        return board[r][c];
    }
    
}
