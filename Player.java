public abstract class Player {

    protected BoxSymbol mySymbol;

    public abstract void play(Game game, int n);

    public void startNewGame(BoxSymbol mySymbol){
        this.mySymbol = mySymbol;
    }

}