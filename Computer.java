import java.util.*;

public class Computer extends Player {
    // Time in ms given for the AI to think
    private static int AIdiff = 100;

    // Boolean to enable/disable minimax algorithm
    private static boolean difficultAI = true;
    
    // Getters
    public static int getAIdiff() {return AIdiff;}

    public static boolean getDifficultAI() {return difficultAI;}

    // Setter
    public static void setAIdiff(int newAIdiff) {
        if (newAIdiff < 1) {
            throw new IllegalArgumentException("AI thinking time can't be smaller than 1 ms.");
        } else if (newAIdiff > 10000) {
            throw new IllegalArgumentException("AI thinking time can't be bigger than 10000 ms (10 s)");
        }
        System.out.println("AI thinking time has been changed from " + AIdiff + " to " + newAIdiff);
        AIdiff = newAIdiff;
    }

    public static void setDifficultAI(boolean bool) {difficultAI = bool;}

    /**
     * Main method for the computer player to decide and make a move.
     * Chooses between Minimax and Monte Carlo methods depending on game size.
     * 
     * @param game The current game instance (TTT or C4)
     */
    public void play(Game game) {
        // For small boards, use Minimax for perfect play
        if (game.getRows()*game.getColumns() <= 16 && game instanceof TTT && difficultAI) {
            int move = findBestMoveMinimax(game, 0);
            System.out.println("\n" + mySymbol + " to play: " + (move + 1));
            game.play(move);
        } else {
            // For larger boards (like Connect 4), use Monte Carlo simulations  
            int move = MonteCarlo(game);
            System.out.println("\n" + mySymbol + " to play: " + (move + 1));
            game.play(move);
        }
    }

    /**
     * Find the best move for the computer player using the Minimax algorithm.
     * 
     * @param game The current game state
     * @param depth The current depth of recursion (starts at 0)
     * @return The index of the box with the best move found
     */
    private int findBestMoveMinimax(Game game, int depth) {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = 0;
            // Iterate through all possible moves
            for (int i = 0; i < game.getRows()*game.getColumns(); i++) {
                // Check if the spot is available
                if (game.boxSymbolAt(i) == null) {
                    // Simulate the move on a cloned game
                    Game sim = game.clone();
                    sim.play(i);
                    // Calculate the score using minimax recursively
                    int score = minimax(sim, depth + 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    if (bestScore < score) {
                        bestScore = score;
                        bestMove = i;
                    }
                }
            }
            return bestMove;
    }

    /**
     * Minimax algorithm with alpha-beta pruning to evaluate game states.
     * 
     * @param game The current game state
     * @param depth Current recursion depth
     * @param isMaximizing True if the current player is maximizing the score
     * @param alpha Alpha value for pruning
     * @param beta Beta value for pruning
     * @return Score for the game state
     */
    private int minimax(Game game, int depth, boolean isMaximizing, int alpha, int beta) {
        // Base cases for terminal states
        if (game.getGameState() == GameState.DRAW) {
            return 0;
        } else if (game.getGameState() == GameState.X_WIN) {
            return depth - 10; // X (player) wins: negative score
        } else if (game.getGameState() == GameState.O_WIN) {
            return 10 - depth; // O (AI) wins: positive score
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < game.getRows()*game.getColumns(); i++) {
                if (game.boxSymbolAt(i) == null) {
                    Game sim = game.clone();
                    sim.play(i);
                    int score = minimax(sim, depth + 1, false, alpha, beta);
                    bestScore = Math.max(bestScore, score);
                    
                    alpha = Math.max(alpha, score);
                    if (beta <= alpha) break;
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < game.getRows()*game.getColumns(); i++) {
                if (game.boxSymbolAt(i) == null) {
                    Game sim = game.clone();
                    sim.play(i);
                    int score = minimax(sim, depth + 1, true, alpha, beta);
                    bestScore = Math.min(bestScore, score);

                    beta = Math.min(beta, score);
                    if (beta <= alpha) break;
                }
            }
            return bestScore;
        }
    }

    /**
     * Flat Monte Carlo simulation to pick the best move based on playout results.
     * Runs simulations for about 1 second and picks the move with highest win ratio.
     * 
     * @param game The current game state
     * @return The index of the best move found
     */
    private int MonteCarlo(Game game) {
        long startTime = System.currentTimeMillis();
        List<Integer> legalMoves = game.getAvailableMoves();

        // Maps to store the number of wins and plays for each move
        Map<Integer, Integer> wins = new HashMap<>();
        Map<Integer, Integer> plays = new HashMap<>();

        // Initialize counts to zero
        for (int move : legalMoves) {
            wins.put(move, 0);
            plays.put(move, 0);
        }

        // Run simulations until 0.1 second passes
        while (System.currentTimeMillis() - startTime < AIdiff) {
            // Pick a random legal move
            int move = legalMoves.get(Main.generator.nextInt(legalMoves.size()));

            // Clone the game and play the move
            Game sim = game.clone();
            sim.play(move);

            // Play random playout to the end of the game
            int result = sim.randomPlayout();

            // Update statistics for the move
            plays.put(move, plays.get(move) + 1);
            wins.put(move, wins.get(move) + result);
        }

        // Select the move with the highest average win-to-loss score
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