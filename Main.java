import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    // Console object to read input from the user via command line
    public static final Console console = System.console();

    // Random number generator used throughout the program
    public static final Random generator = new Random();

    // Statistics for number of wins
    private static int player1Win = 0;
    private static int player2Win = 0;

    /**
     * Prompts the user to input the number of players for the game (1 or 2).
     * Keeps asking until a valid integer 1 or 2 is entered.
     *
     * @return the number of players (1 or 2)
     */
    public static int getNumberOfPlayers() {
    int n = 0;
        while (true) {
            try {
                // Read input line from the console and parse it to an integer
                n = Integer.parseInt(console.readLine("Indicate the number of players (1 or 2): "));
                // Validate input: only accept 1 or 2
                if (n != 1 && n !=2) {
                    throw new IllegalArgumentException("invalid amount of players. Please try again.");
                }
                break; // valid input; exit loop
            } catch (Exception e) {
                // Inform the user of the error and prompt again
                System.out.println(e);
            }
        }
        return n;
    }

    /**
     * Starts the specified game with the given number of players.
     * Creates and initializes players (User or Computer), and runs the game loop until completion.
     *
     * @param game the game instance to be played
     * @param n number of players (1 or 2)
     */
    public static void launchGame(Game game, int n) {
        // List to hold two players
        List<Player> players = new ArrayList<>(2);

        // Always add the first player as a User
        User player1 = new User();
        players.add(player1);

        // Add second player:  Computer if 1 player, else User (2 players)
        if (n == 1) {
            Computer computer = new Computer();
            players.add(computer);
        } else {
            User player2 = new User();
            players.add(player2);
        }

        players.get(0).startNewGame(BoxSymbol.X);
        players.get(1).startNewGame(BoxSymbol.O);

        int turn = 0;

            // Main game loop: runs until the game state changes from PLAYING
        while (game.getGameState() == GameState.PLAYING) {
            players.get(turn % 2).play(game);
            turn++;
        }

        // Print final board state and game result
        endGame(game, n);
    }

    /**
     * Handles the end of a game session:
     * - Prints the final game board and result.
     * - Updates and displays win statistics.
     * - Asks the user what to do next: replay, change number of players, or exit.
     *
     * @param game the finished game instance
     * @param n number of players (1 or 2)
     */
    private static void endGame(Game game, int n) {
        GameState gameState = game.getGameState();

        // Display the final board and result
        System.out.println("\n" + game);
        System.out.println("Result: " + gameState);

        // Update win counters
        if (gameState == GameState.X_WIN) player1Win++;
        if (gameState == GameState.O_WIN) player2Win++;

        // Display current stats
        System.out.println("\n==============================\nCurrent game statistics:\n" + "Player 1 wins: " + player1Win + "\nPlayer 2 wins: " + player2Win + "\n==============================");

        // Ask user for next action
        boolean stop = false;
        while (!stop) {
            try {
                int select = Integer.parseInt(console.readLine("\n'1' to play again.\n'2' to change number of players.\n'3' to change AI difficulty.\n'0' to exit, change game or board size.\nSelect: "));
                switch (select) {
                    case 0:
                        System.out.println("");
                        stop = true;
                        break;
                    case 1:
                        game.reset();
                        launchGame(game, n);
                        stop = true;
                        break;
                    case 2:
                        player1Win = 0;
                        player2Win = 0;
                        game.reset();
                        launchGame(game, (n + 1) % 2);
                        stop = true;
                        break;
                    case 3:
                        try {
                            if (game instanceof TTT && game.getRows()*game.getColumns() <= 16) {
                                while (true) {
                                    int difficultAI = Integer.parseInt(console.readLine("\nType :\n'1' to set to hard AI\n'0' to set to easy AI\nSelect: "));
                                    if (difficultAI == 1 || difficultAI == 0) {
                                        Computer.setDifficultAI(difficultAI == 1 ? true : false);
                                        break;
                                    }
                                    System.out.println(difficultAI + " is an invalid entry.");
                                }
                            } else {
                                int AIdiff = Integer.parseInt(console.readLine("\nAI difficulty is set by thinking time. Default: 100 ms (0.1 s). \nEnter time in ms (recommended: 1 - 1000): "));
                                Computer.setAIdiff(AIdiff);
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException(select + " is an invalid selection. Choose 1, 2 or 3.");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
