import java.util.ArrayList;

public abstract class Game {
    protected int rows;
    protected int columns;
    protected int round;
    protected GameState gameState; // Current state of the game (e.g. PLAYING, XWIN, OWIN, DRAW)
    protected int numberWin; // Number of consecutive symbols needed to win

     // Getters (accessors)
    public int getRows() {return rows;}

    public int getColumns() {return columns;}


    public int getRound() {return round;}

    public GameState getGameState() {return gameState;}

    /**
     * Determines the next player symbol based on the current round.
     * Even rounds -> X plays, Odd rounds -> O plays.
     * @return BoxSymbol.X or BoxSymbol.O representing the next player
     */
    public BoxSymbol nextBoxSymbol() {
        if (round % 2 == 0) return BoxSymbol.X;
        return BoxSymbol.O;
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
    }

    /**
     * Returns a string representation of the board, useful for printing to console.
     * Shows the board with symbols and grid lines.
     * 
     * @return formatted board string
     */
    public String toString() {
        String res = "";
        for (int i = 0; i < rows; i++) {
            // Do one row at a time
            for (int j = 0; j < columns; j++) {
                BoxSymbol symbol = boxSymbolAt(i * columns + j);
                if (j < columns - 1) {
                    res += (symbol == null ? " " : symbol) + " | ";
                } else {
                    res += (symbol == null ? " " : symbol);
                }
            }
            res += "\n";

            // Row seperator
            if (i < rows - 1) {
                for (int j = 0; j < columns - 1; j++) {
                    res += "----";
                }
                res += "-" + "\n";
            }
        }
        return res;
    }

    /**
     * Helper method to update the game state after a move is played at index i.
     * This method assumes the game was not already finished before this move.
     * It checks if the move ended the game by forming a line of numberWin symbols
     * horizontally, vertically, or diagonally.
     * If no win is found and the board is full, sets the game state to DRAW.
     *
     * @param i the linear index of the box that was just played
     */
    protected void update(int i) {
        int row = i / columns;
        int col = i % columns;
        BoxSymbol currentSymbol = this.nextBoxSymbol(); // Symbol of the player who just played
        int numAllignedBox = 1; // Count of aligned symbols, starting at 1 for the current box
        round++; // Increment round counter after the move and after extracting current Symbol

        // Check the row
        // Check the right side
        for (int j = 1; j < columns - col; j++) {
            if (boxSymbolAt(i+j) != currentSymbol) {
                break;
            }
            numAllignedBox++;
        }

        // Check the left side
        for (int j = 1; j < col + 1; j++) {
            if (boxSymbolAt(i-j) != currentSymbol) {
                break;
            }
            numAllignedBox++;
        }

        // Check win statement
        if (numAllignedBox >= numberWin) {
            gameState = currentSymbol == BoxSymbol.X ? GameState.X_WIN : GameState.O_WIN;
            return;
        }
        
        numAllignedBox = 1; // Reset count
        // Check the column
        // Check down
        for (int j = 1; j < rows - row; j++) {
            if (boxSymbolAt(i+j*columns) != currentSymbol) {
                break;
            }
            numAllignedBox++;
        }

        // Check up
        for (int j = 1; j < row + 1; j++) {
            if (boxSymbolAt(i-j*columns) != currentSymbol) {
                break;
            }
            numAllignedBox++;
        }

        // Check win statement
        if (numAllignedBox >= numberWin) {
            gameState = currentSymbol == BoxSymbol.X ? GameState.X_WIN : GameState.O_WIN;
            return;
        }        

        numAllignedBox = 1; // Reset count
        // Check main diagonal
        // Check up-left
        for (int j = 1; j < Math.min(row + 1, col + 1); j++) {
            if (boxSymbolAt(i-j*(columns+1)) != currentSymbol) {
                break;
            }
            numAllignedBox++;
        }

        // Check down-right
        for (int j = 1; j < Math.min(rows - row, columns - col); j++) {
            if (boxSymbolAt(i+j*(columns+1)) != currentSymbol) {
                break;
            }
            numAllignedBox++;
        }

        // Check win statement
        if (numAllignedBox >= numberWin) {
            gameState = currentSymbol == BoxSymbol.X ? GameState.X_WIN : GameState.O_WIN;
            return;
        } 

        numAllignedBox = 1; // Reset count
        // Check anti-diagonal
        // Check up-right
        for (int j = 1; j < Math.min(row + 1, columns - col); j++) {
            if (boxSymbolAt(i-j*(columns-1)) != currentSymbol) {
                break;
            }
            numAllignedBox++;
        }

        // Check down-left
        for (int j = 1; j < Math.min(rows - row, col + 1); j++) {
            if (boxSymbolAt(i+j*(columns-1)) != currentSymbol) {
                break;
            }
            numAllignedBox++;
        }

        // Check win statement
        if (numAllignedBox >= numberWin) {
            gameState = currentSymbol == BoxSymbol.X ? GameState.X_WIN : GameState.O_WIN;
            return;
        } 

        // Check for draw
        if (round >= columns * rows) {
            gameState = GameState.DRAW;
        }
    }

    // Abstract method to get the list of available moves for the current game state
    public abstract ArrayList<Integer> getAvailableMoves();

    /**
     * Simulates a random playout from the current state until the game ends.
     * This is used for the Monte Carlo algorithm.
     * 
     * @return 1 if O wins, -1 if X wins, 0 for draw
     */
    public int randomPlayout() {
        while (gameState == GameState.PLAYING) {
            ArrayList<Integer> availableMoves = getAvailableMoves();
            int move = availableMoves.get(Main.generator.nextInt(availableMoves.size()));
            play(move);
        }
        if (gameState == GameState.O_WIN) return 1;
        if (gameState == GameState.X_WIN) return -1;
        return 0;
    }


    /**
     * Abstract method to get the symbol at a specific position.
     * 
     * @param i linear index of the box on the board
     * @return BoxSymbol at index i or null if empty
     */
    public abstract BoxSymbol boxSymbolAt(int i);

    // Abstract method to play a move at index i
    public abstract void play(int i);

    // Abstract method to clone the current game state (deep copy)
    public abstract Game clone();
}
