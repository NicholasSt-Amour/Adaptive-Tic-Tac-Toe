import java.util.ArrayList;

/**
 * Represents a Connect 4 game.
 * 
 * The board is stored as a list of columns, each column being a list of 
 * BoxSymbol (X, O, or null for empty).
 * 
 * Note: In this implementation, the first index is the column number, 
 * and the second is the row number, with index 0 at the top left.
 */
public class C4 extends Game {
    private ArrayList<ArrayList<BoxSymbol>> board;

    /**
     * Constructor: Sets up a standard Connect 4 game (6 rows × 7 columns).
     */
    public C4() {
        this.rows = 6;
        this.columns = 7;
        this.gameState = GameState.PLAYING;
        this.round = 0;
        this.numberWin = 4;
        this.board = new ArrayList<ArrayList<BoxSymbol>>(this.columns);

        // Initialize each column with rows filled with "null" to help toString.
        for (int i = 0; i < this.columns; i++) {
            this.board.add(new ArrayList<BoxSymbol>(this.rows));
            for (int j = 0; j < this.rows; j++) {
                this.board.get(i).add(null);
            }
        }
    }

    /**
     * Finds the next available row index in the given column where a piece can be placed.
     * Returns the index from the top (0 = top row).
     * 
     * @param col the column index
     * @return row index where the piece will fall
     */
    public int getColHeight(int col) {
        int row = 0;
        // Count how many empty slots remain in this column
        for (BoxSymbol symbol : board.get(col)) {
            if (symbol == null) row++;
        }
        return row - 1; // Return the actual position index for the piece
    }

    /**
     * Plays a move in the specified column for the current player.
     * Updates the game state afterward.
     * 
     * @param col the column index to play in
     */
    public void play(int col) {
        int row = getColHeight(col);
        board.get(col).set(row, this.nextBoxSymbol()); // Drop piece into the column
        update(row * columns + col); // Convert (row, col) to single index and check for win/draw
        
    }

    /**
     * Creates a deep copy of the current game state for simulation purposes.
     */
    public C4 clone() {
        C4 copy = new C4();
        copy.round = this.round;
        copy.gameState = this.gameState;
        copy.board.clear();
        for (ArrayList<BoxSymbol> row : this.board) {
            ArrayList<BoxSymbol> newRow = new ArrayList<>(row);
            copy.board.add(newRow);
        }
        return copy;
    }

     /**
     * Returns the symbol (X, O, or null) at the specified linear index.
     * 
     * @param i position in the flattened board (0-based)
     * @throws IllegalArgumentException if the index is outside the board
     */
    public BoxSymbol boxSymbolAt(int i) {
        if (i < 0 || i >= rows * columns) {
            throw new IllegalArgumentException("Illegal position: " + i);
        }
        int row = i / columns;
        int col = i % columns;
        return board.get(col).get(row);
    }

    /**
     * Checks if a given column is already full (no moves possible there).
     */
    public boolean columnIsFull(int col) {
        return boxSymbolAt(col) != null;
    }

    /**
     * Returns a list of all available (non-full) column indices.
     */
    public ArrayList<Integer> getAvailableMoves() {
        ArrayList<Integer> availableMoves = new ArrayList<>();
        for (int i = 0; i < columns; i++) {
            if (!columnIsFull(i)) {
                availableMoves.add(i);
            }
        }
        return availableMoves;
    }

    /**
     * Resets the game state to start a new game:
     * - Resets the round counter to 0.
     * - Sets the game state back to PLAYING.
     * - Clears the current board data.
     * - Initializes the board to prepare for a fresh game.
     */
    public void reset() {
        round = 0;
        gameState = GameState.PLAYING;
        board.clear();

        // Initialize each column with rows filled with "null" to help toString.
        for (int i = 0; i < this.columns; i++) {
            this.board.add(new ArrayList<BoxSymbol>(this.rows));
            for (int j = 0; j < this.rows; j++) {
                this.board.get(i).add(null);
            }
        }
    }
}