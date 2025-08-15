/**
 * Abstract class representing a player in the game.
 * It defines the basic structure that all types of players (human, AI) must follow.
 */
public abstract class Player {
   
    // The symbol assigned to this player (e.g., X or O)
    protected BoxSymbol mySymbol;

     /**
     * Initializes the player for a new game by assigning their symbol.
     * This method should be called at the start of each game.
     *
     * @param mySymbol the symbol (BoxSymbol) assigned to this player
     */
    public void startNewGame(BoxSymbol mySymbol){
        this.mySymbol = mySymbol;
    }
    
    /**
     * Abstract method representing the player's move.
     * Each subclass must implement this method to define how the player makes a move.
     *
     * @param game the current game instance where the player should make a move
     */
    public abstract void play(Game game);
}