import java.util.ArrayList;

/**
 * Tic-Tac-Toe game implementation extending the abstract Game class.
 */
public class TTT extends Game {
    // The board is represented as a one-dimensional array of BoxSymbols (X, O, or null)
    private BoxSymbol[] board;

    /**
     * Default constructor creating a standard 3x3 Tic-Tac-Toe game
     * requiring 3 symbols in a row to win.
     */
    public TTT() {
        this(3, 3, 3);
    }

    /**
     * Constructor allowing custom board size and winning condition.
     * Initializes the game board and sets initial game state.
     * 
     * @param rows Number of rows on the board
     * @param columns Number of columns on the board
     * @param numberWin Number of consecutive symbols needed to win
     */
    public TTT(int rows, int columns, int numberWin) {
        this.rows = rows;
        this.columns = columns;
        this.numberWin = numberWin;
        this.board = new BoxSymbol[rows * columns];
        this.round = 0;
        this.gameState = GameState.PLAYING;
    }

    // Getter for the number of symbols required to win.
    public int getNumberWin() {
        return numberWin;
    }

    /**
     * Returns the symbol at a given index on the board.
     * Throws IllegalArgumentException if index is out of bounds.
     * 
     * @param i linear index on the board
     * @return BoxSymbol at position i or null if empty
     */
    public BoxSymbol boxSymbolAt(int i) {
        if (i < 0 || i >= rows * columns) {
            throw new IllegalArgumentException("Illegal position: " + i);
        }
        return board[i];
    }

    /**
     * Returns a list of available (empty) positions where moves can still be played.
     * 
     * @return an ArrayList of integer indexes representing empty board positions
     */
    public ArrayList<Integer> getAvailableMoves() {
        ArrayList<Integer> availableMoves = new ArrayList<>();
        for (int i = 0; i < getRows()*getColumns(); i++) {
            if (boxSymbolAt(i) == null) {
                availableMoves.add(i);
            }
        }
        return availableMoves;
    }

     /**
     * Creates and returns a deep copy of the current game state.
     * Useful for simulations and AI algorithms.
     * 
     * @return a cloned TTT instance with the same state as this one
     */
    public TTT clone() {
        TTT copy = new TTT(this.rows, this.columns, this.numberWin);
        copy.round = this.round;
        copy.gameState = this.gameState;
        copy.board = this.board.clone();
        return copy;
    }    

    /**
     * Plays a move at the given index by setting the next symbol on the board.
     * Then calls update() to check and update the game state accordingly.
     * 
     * @param i the index where the current player wants to play
     */
    public void play(int i) {
        board[i] = this.nextBoxSymbol();
        update(i);
    }

    @Override
    public void reset() {
        super.reset();
        board = new BoxSymbol[rows * columns];
    }
}