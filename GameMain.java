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
        User me = new User();
        Player[] players = new Player[2];
        int n = Integer.parseInt(args[0]);
        if (n==2) {
            User you = new User();
            players[0] = you;
        } else if (n==1) {
            Computer ordi = new Computer();
            players[0] = ordi;
        } else {
            System.out.println("Number of Players must be between 1 and 2. You're input is "+n);
            System.exit(0);
        }
        players[1] = me;

        Game game;
        int lines, columns, win;
        lines = Integer.parseInt(args[1]);
        columns = Integer.parseInt(args[2]);
        win = Integer.parseInt(args[3]);

        game = new Game(lines, columns, win);

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

