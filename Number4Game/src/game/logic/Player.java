package game.logic;

/**
 * @author KrazyXL
 * @version 1.0
 * @created 31-okt.-2020 12:04:12
 */

/*Player: Játékos adatainak osztálya*/
public class Player {

    final private String name;
    private int score;

    /**Konstruktor*/
    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    /**Játékos pontszámának lekérése*/
    public int getScore() {
        return score;
    }

    /**Játékos pontszámának növelése eggyel*/
    protected void score() {
        score++;
    }
}//end Player
