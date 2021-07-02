package game.logic;

/**
 * @author KrazyXL
 * @version 1.0
 * @created 31-okt.-2020 13:30:32
 */

/**GameGrid: Játékadatok osztálya*/
public class GameGrid {

    private int[][] grid;
    private Player redPlayer;
    private Player bluePlayer;
    private PlayerColor currentPlayer;

    /**Konstruktor*/
    public GameGrid() {
        redPlayer = new Player("Piros");
        bluePlayer = new Player("Kék");
        currentPlayer = PlayerColor.RED;
    }

    /**Getter metódusok*/
    public int[][] getGrid() {
        return grid;
    }

    public Player getRedPlayer() {
        return redPlayer;
    }

    public Player getBluePlayer() {
        return bluePlayer;
    }

    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    public int getValue(int i, int j) {
        return grid[i][j];
    }
    
    /**Játék betöltése (backend oldalon)
     * @param size A játéktábla mérete (n*n alapon).
     */
    public void loadGame(int size) {
        grid = new int[size][size];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = 0;
                System.out.print(grid[i][j] + " ");
            }
            System.out.println("");
        }
    }
    
    /**Mezők növelése
     * @param i A kattintott mező sora.
     * @param j A kattintott mező oszlopa.
     */
    public void increaseArea(int i, int j) {    //a játékosok itt játsszák meg a köreiket   //inkább egy külön makeAMove metódus?
        if (grid[i][j] < 4) {                     //az eredetileg kattintott mező
            grid[i][j]++;
            if (grid[i][j] == 4) {
                if (currentPlayer == PlayerColor.RED) {
                    redPlayer.score();
                } else {
                    bluePlayer.score();
                }
            }
        } else {
            return;
        }
        if (i - 1 >= 0 && grid[i - 1][j] < 4) {     //felső szomszéd
            grid[i - 1][j]++;
            if (grid[i - 1][j] == 4) {
                if (currentPlayer == PlayerColor.RED) {
                    redPlayer.score();
                } else {
                    bluePlayer.score();
                }
            }
        }
        if (j + 1 < grid[0].length && grid[i][j + 1] < 4) {   //jobb szomszéd
            grid[i][j + 1]++;
            if (grid[i][j + 1] == 4) {
                if (currentPlayer == PlayerColor.RED) {
                    redPlayer.score();
                } else {
                    bluePlayer.score();
                }
            }
        }
        if (i + 1 < grid.length && grid[i + 1][j] < 4) {    //felső szomszéd
            grid[i + 1][j]++;
            if (grid[i + 1][j] == 4) {
                if (currentPlayer == PlayerColor.RED) {
                    redPlayer.score();
                } else {
                    bluePlayer.score();
                }
            }
        }
        if (j - 1 >= 0 && grid[i][j - 1] < 4) {               //bal szomszéd
            grid[i][j - 1]++;
            if (grid[i][j - 1] == 4) {
                if (currentPlayer == PlayerColor.RED) {
                    redPlayer.score();
                } else {
                    bluePlayer.score();
                }
            }
        }
    }

    /**Új körre váltás*/
    public void nextTurn() {                  //Új körben átállítjuk a soronkövetkező játékost
        if (currentPlayer == PlayerColor.RED) {
            currentPlayer =PlayerColor.BLUE;
        } else {
            currentPlayer = PlayerColor.RED;
        }
    }
}//end GameGrid
