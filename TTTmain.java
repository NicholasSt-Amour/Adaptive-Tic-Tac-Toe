public class TTTmain {
    /**
     * The entry point for the Tic-Tac-Toe game.
     * It attempts to read custom board size and win condition from command-line arguments.
     * If invalid or no arguments are provided, it defaults to a standard 3x3 game with 3 in a row to win.
     * Then it launches the game with user-chosen number of players.
     * 
     * @param args command-line arguments expected as: rows columns numberToWin
     */
    public static void main(String[] args) {
        TTT game;
        try {
            // Parse command line arguments for rows, columns, and winning line length
            int rows, columns, win;
            rows = Integer.parseInt(args[0]);
            columns = Integer.parseInt(args[1]);
            win = Integer.parseInt(args[2]);

            // Validate that the win condition is possible on the given board size
            if (win > rows && win > columns) {
                System.out.println("The game is impossible to win! You can't get " + win +
                        " in a row in a " + rows + "x" + columns + " grid.");
                throw new IllegalArgumentException("Invalid win condition");
            }

            // Create a new game instance with custom parameters
            game = new TTT(rows, columns, win);
        } catch (Exception e) {
            // If any error occurs (missing args, wrong format, invalid conditions),
            // fallback to the default 3x3 Tic-Tac-Toe game
            System.out.println("Invalid or no custom arguments given. A default game will be generated.");
            game = new TTT();

        }
        
        // Ask user how many players and launch the game
        Main.launchGame(game, Main.getNumberOfPlayers());        
    }
}