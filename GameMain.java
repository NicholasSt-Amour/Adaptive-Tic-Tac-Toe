import java.util.Random;
import java.io.Console;

public class GameMain {

    public static final Console console = System.console();
    public static final String NEW_LINE = System.getProperty("line.separator");
    public static final Random generator = new Random();

    /**
     * @param args command line parameters
     */

    public static void main(String[] args) {
        Game game;
        try {
            int rows, columns, win;
            rows = Integer.parseInt(args[0]);
            columns = Integer.parseInt(args[1]);
            win = Integer.parseInt(args[2]);

            if (win > rows && win > columns) {
                System.out.println("The game is impossible to win! You can't get " + win +
                        " in a row in a " + rows + " x " + columns + " grid.");
                throw new IllegalArgumentException("Invalid win condition");
            }

            game = new Game(rows, columns, win);
            
        } catch (Exception e) {
            System.out.println("Invalid or no custom arguments given. A default game will be generated.");
            game = new Game();
        }

        int n = 0;
        while (true) {
            try {
                n = Integer.parseInt(GameMain.console.readLine("Indicate the number of players (1 or 2): "));
                if (n != 1 && n !=2) {
                    throw new IllegalArgumentException("invalid amount of players");
                }
                break;
            } catch (Exception e) {
                System.out.println(e + ". Please try again.");
            }
        }

        Player[] players = new Player[2];
        User player1 = new User();
        players[0] = player1;

        if (n==2) {
            User player2 = new User();
            players[1] = player2;

        } else {
            Computer computer = new Computer();
            players[1] = computer;
        }

        int turn = 0;
        players[turn % 2].startNewGame(BoxSymbol.X);
        players[(turn + 1) % 2].startNewGame(BoxSymbol.O);

        while (game.getGameState() == GameState.PLAYING) {
            players[turn % 2].play(game, n);
            turn++;
        }

        System.out.println(game);
        System.out.println("Result: " + game.getGameState());
    }
}

