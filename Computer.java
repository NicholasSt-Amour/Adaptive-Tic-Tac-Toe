import java.util.*;

public class Computer extends Player {

    public void play(Game game, int n) {
        if (game.getGameState() != GameState.PLAYING) return;

        if (game.getRound() % 2 == 1) {
            if (game.getRows()*game.getColumns() < 15) {
                game.play(findBestMove(game, 0));
            } else {
                while (true) {
                    int box = GameMain.generator.nextInt(game.getRows() * game.getColumns()) + 1;
                    if (game.boxSymbolAt(box) == null) {
                        game.play(box);
                        break;
                    }
                }
            }
        }
    }

    public int findBestMove(Game game, int depth) {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = 0;
            for (int i = 1; i <= game.getRows()*game.getColumns(); i++) {
                if (game.boxSymbolAt(i) == null) {
                    game.play(i);
                    int score = minimax(game, depth + 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    game.undo(i);
                    if (bestScore < score) {
                        bestScore = score;
                        bestMove = i;
                    }
                }
            }
            return bestMove;
    }

    public int minimax(Game game, int depth, boolean isMaximizing, int alpha, int beta) {
        if (game.getGameState() == GameState.DRAW) {
            return 0;
        } else if (game.getGameState() == GameState.XWIN) { // The player won
            return depth - 10;
        } else if (game.getGameState() == GameState.OWIN) { // The AI won
            return 10 - depth;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 1; i <= game.getRows()*game.getColumns(); i++) {
                if (game.boxSymbolAt(i) == null) {
                    game.play(i);
                    int score = minimax(game, depth + 1, false, alpha, beta);
                    game.undo(i);
                    bestScore = Math.max(bestScore, score);
                    
                    alpha = Math.max(alpha, score);
                    if (beta <= alpha) break;
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 1; i <= game.getRows()*game.getColumns(); i++) {
                if (game.boxSymbolAt(i) == null) {
                    game.play(i);
                    int score = minimax(game, depth + 1, true, alpha, beta);
                    game.undo(i);
                    bestScore = Math.min(bestScore, score);

                    beta = Math.min(beta, score);
                    if (beta <= alpha) break;
                }
            }
            return bestScore;
        }
    }

    public int MCTS(Game game) {
        long startTime = System.currentTimeMillis();
        List<Integer> legalMoves = game.getAvailableMoves();
        Map<Integer, Integer> wins = new HashMap<>();
        Map<Integer, Integer> plays = new HashMap<>();

        for (int move : legalMoves) {
            wins.put(move, 0);
            plays.put(move, 0);
        }

        while (System.currentTimeMillis() - startTime < 1000) {
            int move = legalMoves.get(GameMain.generator.nextInt(legalMoves.size()));

            Game sim = game.clone();
            sim.play(move);

            int result = sim.randomPlayout();

            plays.put(move, plays.get(move) + 1);
            if (result == 1) {
                wins.put(move, wins.get(move) + 1);
            }
        }
        
        // Choose move with hightes win ratio
        double bestScore = -1;
        int bestMove = legalMoves.get(0);
        for (int move : legalMoves) {
            double score = (double) wins.get(move) / plays.get(move);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }
}