public class C4main {
    // Entry point of the Connect 4 game application
    public static void main(String[] args) {
        C4 game = new C4();

        // Launch the game using the Main class method,
        // passing the created game instance and the number of players
        Main.launchGame(game, Main.getNumberOfPlayers());        
    }
}