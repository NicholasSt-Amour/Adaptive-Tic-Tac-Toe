public class Computer extends Player {

    public void play(Game game, int n) {
        if (game.getRound() == game.getRows() * game.getColumns()) {
            System.out.println("Game is finished already!");
        }

        if (game.getGameState() != GameState.PLAYING) return;

        if (game.getRound() % 2 == 0) {
            int box = GameMain.generator.nextInt(game.getRows() * game.getColumns()) + 1;
            if (game.boxSymbolAt(box) == null) {
                game.play(box);
            } else {play(game, 1);}
        }
    }
}