public class User extends Player {

    public void play(Game game, int n) {
        if (game.getRound() == game.getRows() * game.getColumns()) {
            System.out.println("Game is finished already!");
        }

        if (game.getNumberWin() > game.getRows() && game.getNumberWin() > game.getColumns()) {
            System.out.println("The game is impossible to win! You can't get " + game.getNumberWin() +
                    " in a row in a " + game.getRows() + " x " + game.getColumns() + " grid.");
            System.exit(0);
        }

        int box = -1;
        if (n == 1) {
            while (true) {
                if (game.getGameState() != GameState.PLAYING) return;

                System.out.println("");
                System.out.println(game);
                
                if ((game.getRound() + 1) % 2 == 0) {
                    String player = (game.nextBoxSymbol() == BoxSymbol.X) ? "X" : "O";
                    box = Integer.parseInt(GameMain.console.readLine(player + " to play:"));
                }

                if (box < 1 || box > game.getRows() * game.getColumns()) {
                    System.out.println("The value should be between 1 and " + (game.getRows() * game.getColumns()) + ".");
                    return;
                }
                if (game.boxSymbolAt(box) != null) {
                    System.out.println("Box " + box + " is already taken. Please choose another one.");
                    return;
                } else {
                        game.play(box);
                        return;
                }
            }
        }

        else if (n == 2) {
            while (true) {
                if (game.getGameState() != GameState.PLAYING) return;

                System.out.println("");
                System.out.println(game);

                String player = (game.nextBoxSymbol() == BoxSymbol.X) ? "X" : "O";
                box = Integer.parseInt(GameMain.console.readLine(player + " to play:"));

                if (box < 1 || box > game.getRows() * game.getColumns()) {
                    System.out.println("The value should be between 1 and " + (game.getRows() * game.getColumns()) + ".");
                    return;
                }
                if (game.boxSymbolAt(box) != null) {
                    System.out.println("Box " + box + " is already taken. Please choose another one.");
                    return;
                } else {
                        game.play(box);
                        return;
                }
            }
        } else {
            System.out.println("Number of Players must be between 1 and 2. You're input is "+n);
            System.exit(0);
        }
    }
}