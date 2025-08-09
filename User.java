public class User extends Player {

    public int chooseBox(Game game) {
        String player = (game.nextBoxSymbol() == BoxSymbol.X) ? "X" : "O";
        try {
            int box = Integer.parseInt(GameMain.console.readLine(player + " to play: "));
            return box;
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        }
    }

    public boolean valideBox(Game game, int box) {
        if (box < 1 || box > game.getRows() * game.getColumns()) {
            System.out.println("The value should be a number between 1 and " + (game.getRows() * game.getColumns()) + ".");
            return false;
        }
        if (game.boxSymbolAt(box) != null) {
            System.out.println("Box " + box + " is already taken. Please choose another one.");
            return false;
        }
        return true;
    }

    public void play(Game game, int n) {
        if (n == 1 || n == 2) {
            if (game.getGameState() != GameState.PLAYING) return;

            System.out.println("");
            System.out.println(game);
            int box = -1;
            
            if (n == 2 || game.getRound() % 2 == 0) {
                box = chooseBox(game);
            }

            if (valideBox(game, box)) {
                game.play(box);
            }

        } else {
            throw new IllegalArgumentException("invalid amount of players.");
        }
    }
}