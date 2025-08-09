import java.util.ArrayList;

public class Game {

    private BoxSymbol[] board;
    private int round;
    private GameState gameState;
    private final int rows;
    private final int columns;
    private final int numberWin;

    public Game() {
        this(3, 3, 3);
    }

    /**
     * constructeur permettant de préciser le nombre de lignes et le nombre de colonnes pour le jeu,
     * ainsi que le nombre de cellules qu'il faut aligner pour gagner.
     *
     * @param rows      the number of rows in the game
     * @param columns   the number of columns in the game
     * @param numberWin the number of cells that must be aligned to win.
     */

    public Game(int rows, int columns, int numberWin) {
        this.rows = rows;
        this.columns = columns;
        this.numberWin = numberWin;
        this.board = new BoxSymbol[rows * columns];
        this.round = 0;
        this.gameState = GameState.PLAYING;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getRound() {
        return round;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getNumberWin() {
        return numberWin;
    }

    /**
     * @return the value of the enum BoxSymbol corresponding to the next expected symbol.
     */
    public BoxSymbol nextBoxSymbol() {
        if (round % 2 == 0) return BoxSymbol.X;
        return BoxSymbol.O;
    }


    /**
     * renvoie la valeur de la case a l'index i. Si l'index n'est pas valide, 
     * un message d'erreur est imprimé. Le comportement est alors indéterminé
     *
     * @param i the index of the Box in the array board
     * @return the value at index i in the variable board.
     */
    public BoxSymbol boxSymbolAt(int i) {
        if (i < 1 || i > rows * columns) {
            System.out.println("Illegal position: " + i);
        }
        return board[i - 1];
    }

    /**
     * @param i the index of the box in the array board that has been
     *          selected by the next player
     */
    public void play(int i) {
        if (gameState != GameState.PLAYING) return;

        if (i < 1 || i > rows * columns) {
            System.out.println("Illegal position: " + i);
            return;
        }
        if (board[i - 1] != null) {
            System.out.println("Box " + i + " is already taken");
            return;
        }

        board[i - 1] = this.nextBoxSymbol();
        round++;
        update(i - 1);
    }

    public void undo(int i) {
        // We assume that if a game is won, no other plays can be done.
        // Therefore, the last move either didn't change the game state or it was a winning move. Undoing the move therefore changes to a playing state.
        gameState = GameState.PLAYING;
        board[i - 1] = null;
        round--;
    }

    /**
     * Une méthode d'assistance qui met à jour la variable gameState
     * correctement après que la case à l'index i vient d'etre défini.
     * La méthode suppose qu'avant de paramétrer la case
     * à l'index i, la variable gameState a été correctement définie.
     * cela suppose aussi qu'elle n'est appelée que si le jeu n'a pas encore été
     * terminé lorsque la case à l'index i a été jouée
     * (le jeu en cours). Il suffit donc de
     * Vérifiez si jouer à l'index i a terminé la partie.
     *
     * @param index the index of the box in the array board that has just
     *              been set
     */
    private void update(int index) {
    int row = index / columns;
    int col = index % columns;
    BoxSymbol currentSymbol = board[index];
    int numAllignedBox = 1;

    // Check the row
    // Check the right side
    for (int j = 1; col + j < columns; j++) {
        if (board[index + j] == currentSymbol) {
            numAllignedBox++;
        } else {
            break;
        }
    }

    // Check the left side
    for (int j = 1; col - j >= 0; j++) {
        if (board[index - j] == currentSymbol) {
            numAllignedBox++;
        } else {
            break;
        }
    }
    // Check win statement
    if (numAllignedBox >= numberWin) {
        gameState = currentSymbol == BoxSymbol.X ? GameState.XWIN : GameState.OWIN;
        return;
    }
    // Check the column
    numAllignedBox = 1; // Reset count
    // Check down
    for (int j = 1; row + j < rows; j++) {
        if (board[(row + j) * columns + col] == currentSymbol) {
            numAllignedBox++;
        } else {
            break;
        }
    }
    // Check up
    for (int j = 1; row - j >= 0; j++) {
        if (board[(row - j) * columns + col] == currentSymbol) {
            numAllignedBox++;
        } else {
            break;
        }
    }
    // Check win statement
    if (numAllignedBox >= numberWin) {
        gameState = currentSymbol == BoxSymbol.X ? GameState.XWIN : GameState.OWIN;
        return;
    }

    // Check main diagonal
    numAllignedBox = 1; // Reset count
    // Check down-right
    for (int j = 1; row + j < rows && col + j < columns; j++) {
        if (board[(row + j) * columns + col + j] == currentSymbol) {
            numAllignedBox++;
        } else {
            break;
        }
    }
    // Check up-left
    for (int j = 1; row - j >= 0 && col - j >= 0; j++) {
        if (board[(row - j) * columns + col - j] == currentSymbol) {
            numAllignedBox++;
        } else {
            break;
        }
    }

    // Check win statement
    if (numAllignedBox >= numberWin) {
        gameState = currentSymbol == BoxSymbol.X ? GameState.XWIN : GameState.OWIN;
        return;
    }

    // Check anti-diagonal
    numAllignedBox = 1; // Reset count
    // Check down-left
    for (int j = 1; row + j < rows && col - j >= 0; j++) {
        if (board[(row + j) * columns + col - j] == currentSymbol) {
            numAllignedBox++;
        } else {
            break;
        }
    }
    // Check up-right
    for (int j = 1; row - j >= 0 && col + j < columns; j++) {
        if (board[(row - j) * columns + col + j] == currentSymbol) {
            numAllignedBox++;
        } else {
            break;
        }
    }

    // Check win statement
    if (numAllignedBox >= numberWin) {
        gameState = currentSymbol == BoxSymbol.X ? GameState.XWIN : GameState.OWIN;
        return;
    }

    // Check for draw
    if (round >= rows * columns) {
        gameState = GameState.DRAW;
    }
}

    public ArrayList<Integer> getAvailableMoves() {
        ArrayList<Integer> availableMoves = new ArrayList<>();
        for (int i = 1; i<=getRows()*getColumns(); i++) {
            if (boxSymbolAt(i) == null) {
                availableMoves.add(i);
            }
        }
        return availableMoves;
    }

    public Game clone() {
        Game copy = new Game(this.rows, this.columns, this.numberWin);
        copy.round = this.round;
        copy.gameState = this.gameState;
        copy.board = this.board.clone();
        return copy;
    }

    public int randomPlayout() {
        while (getGameState() == GameState.PLAYING) {
            ArrayList<Integer> availableMoves = this.getAvailableMoves();
            int move = availableMoves.get(GameMain.generator.nextInt(availableMoves.size()));
            play(move);
        }

        if (getGameState() == GameState.OWIN) return 10;
        if (getGameState() == GameState.XWIN) return -10;
        return 0;
    }

    /**
     * Renvoie une représentation sous forme de chaîne du jeu correspondant
     * à l'exemple fourni dans la description du devoir
     *
     * @return String representation of the game
     */

    public String toString() {
        String res = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                BoxSymbol symbol = boxSymbolAt(i * columns + j + 1);
                if (j < columns - 1) {
                    res += (symbol == null ? " " : symbol) + " | ";
                } else {
                    res += (symbol == null ? " " : symbol);
                }
            }
            res += GameMain.NEW_LINE;

            if (i < rows - 1) {
                // Assure la bonne taille pour la ligne
                for (int j = 0; j < columns - 1; j++) {
                    res += "----";
                }
                res += "-" + GameMain.NEW_LINE;
            }
        }
        return res;
    }
}