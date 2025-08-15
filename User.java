public class User extends Player {
    
    /**
     * Prompts the user to choose a box to play.
     * Reads input from the console and converts it to a zero-based index.
     * Returns -1 if the input is invalid.
     * 
     * @return the index of the chosen box (0-based), or -1 if input is invalid.
     */
    public int chooseBox() {
        try {
            // Prompt the user with their symbol and read input as a string,
            // convert it to integer and adjust to zero-based indexing
            int box = Integer.parseInt(Main.console.readLine(mySymbol + " to play: ")) - 1;
            return box;
        } catch (Exception e) {
            // Print exception message if parsing fails and return -1 to indicate invalid input
            System.out.println(e);
            return -1;
        }
    }

    /**
     * Validates the chosen box index for the current game.
     * Checks if the index is within the board limits and the box is free.
     * Separate validation logic depending on whether the game is TicTacToe (TTT) or Connect4 (C4).
     * 
     * @param game the current game instance.
     * @param box the zero-based index or column chosen by the user.
     * @return true if the box choice is valid and playable, false otherwise.
     */
    public boolean valideBox(Game game, int box) {
        if (game instanceof TTT) {
            // For Tic Tac Toe, check index is within range of total cells
            if (box < 0 || box >= game.getRows() * game.getColumns()) {
                System.out.println("The value should be a number between 1 and " + (game.getRows() * game.getColumns()) + ".");
                return false;
            }
            // Check if the cell is already taken
            if (game.boxSymbolAt(box) != null) {
                System.out.println("Box " + (box + 1) + " is already taken. Please choose another one.");
                return false;
            }
            return true;
        } else if (game instanceof C4) {
            // For Connect4, box refers to column index
            C4 c4game = (C4) game;

            // Check if column index is valid
            if (box < 0 || box >= c4game.getColumns()) {
            System.out.println("The value should be a number between 1 and " + (c4game.getColumns()) + ".");
            return false;
        }
        // Check if the column is already full
        if (c4game.columnIsFull(box)) {
            System.out.println("Column " + (box + 1) + " is full. Please choose another one.");
            return false;
        }
        return true;
        }

        // If the game type is neither TTT nor C4, return false (invalid)
        return false;
    }

    /**
     * Executes a player's turn:
     * - Prints the current state of the game.
     * - Prompts the player to choose a valid box repeatedly until a valid move is made.
     * - Plays the move on the game.
     * 
     * @param game the current game instance
     */
    public void play(Game game) {
        System.out.println("\n" + game);

        // Keep asking for input until a valid box is chosen
        while(true) {
            int box = chooseBox();
            if (valideBox(game, box)) {
                // Play the move if the box is valid and exit the loop
                game.play(box);
                break;
            }
        }
    }
}