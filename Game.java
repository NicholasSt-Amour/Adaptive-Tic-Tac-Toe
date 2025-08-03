public class Game {

    /**
     * Le plateau du jeu, stocké sous forme de tableau
     */
    private BoxSymbol[] board;


    /**
     * round enregistre le nombre de tours qui ont été
     * joué jusqu'à présent. Commence à 0.
     */
    private int round;

    /**
     * gameState enregistre l'état actuel du jeu.
     */
    private GameState gameState;


    /**
     * rows est le nombre de lignes dans la grille
     */
    private final int rows;

    /**
     * columns est le nombre de colonnes dans la grille
     */
    private final int columns;


    /**
     * numberWin est le nombre de cellules du même type
     * qu'il faut aligner pour gagner la partie
     */
    private final int numberWin;


    /**
     * constructeur par défaut, pour un jeu de 3x3, qui doit
     * aligner 3 cellules
     */
    public Game() {
        this(3, 3, 3);
    }


    /**
     * constructeur permettant de préciser le nombre de lignes
     * et le nombre de colonnes pour le jeu, ainsi que
     * le nombre de cellules qu'il faut aligner pour gagner.
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


    /**
     * getter pour la variable rows
     *
     * @return the value of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * getter pour la variable columns
     *
     * @return the value of columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * getter pour la variable round
     *
     * @return the value of roud
     */
    public int getRound() {
        return round;
    }


    /**
     * getter pour la variable gameState
     *
     * @return the value of gameState
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * getter pour la variable numberWin
     *
     * @return the value of numberWin
     */
    public int getNumberWin() {
        return numberWin;
    }

    /**
     * renvoie le prochain BoxSymbol prevu,
     * Cette méthode ne modifie pas l'état du jeu.
     *
     * @return the value of the enum BoxSymbol corresponding
     * to the next expected symbol.
     */
    public BoxSymbol nextBoxSymbol() {
        if (round % 2 == 0) return BoxSymbol.X;
        return BoxSymbol.O;
    }


    /**
     * renvoie la valeur de la case a l'index i.
     * Si l'index n'est pas valide, un message d'erreur est
     * imprimé. Le comportement est alors indéterminé
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
     * Cette méthode est appelée par le prochain joueur à jouer
     * à la case à l'index i.
     * Si l'index n'est pas valide, un message d'erreur est
     * imprimé. Le comportement est alors indéterminé
     * Si la case choisie n'est pas vide, un message d'erreur s'affiche.
     * Le comportement est alors indéterminé
     * Si le coup est valide, le plateau (board) est également mis à jour
     * ainsi que l'état du jeu. Doit appeler la méthode update.
     *
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