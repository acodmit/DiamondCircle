package card;

abstract public class Card {

    private static int COUNT = 0;

    protected final int id;

    public Card(){

        this.id = COUNT++;

    }

}
