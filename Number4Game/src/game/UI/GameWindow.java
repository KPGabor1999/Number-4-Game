package game.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import game.logic.*;

/**
 * @author KrazyXL
 * @version 1.0
 * @created 31-okt.-2020 12:04:34
 */

/**GameWindow: Játékablak osztálya*/
public class GameWindow extends JFrame {

    /**Játékablak konstruktora*/
    public GameWindow() {
        /**Elemi ablak beállítások*/
        setTitle("Number 4 Game");
        setSize(400, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(topX(), topY());
        setResizable(false);
        URL url = getClass().getResource("icon.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));

        /**Menüsor és elemeinek összerakása*/
        JMenuBar gameMenu = new JMenuBar();
        setJMenuBar(gameMenu);
        JMenu startGame = new JMenu("Start Game");
        gameMenu.add(startGame);
        JMenuItem small = new JMenuItem("3x3");
        startGame.add(small);
        JMenuItem medium = new JMenuItem("5x5");
        startGame.add(medium);
        JMenuItem large = new JMenuItem("7x7");
        startGame.add(large);
        JMenu others = new JMenu("Other");
        JMenuItem aboutMe = new JMenuItem("Credits");
        others.add(aboutMe);
        gameMenu.add(others);

        /**Frontend és Backend összekötése eseménykezelőkkel,*/
        small.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame(3);
            }
        });
        medium.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame(5);
            }
        });
        large.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame(7);
            }
        });
        aboutMe.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                developerMessage();
            }
        });
    }
    
    /** Új játék kezdése megadott méretű táblával
     * @param size A játéktábla mérete.
     */
    private void startNewGame(int size) {
        /**ContentPane előkészítése*/
        Container cp = getContentPane();
        cp.removeAll();

        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));

        /**Logikai tábla előkészítése*/
        GameGrid logicalGrid = new GameGrid();
        logicalGrid.loadGame(size);

        /**Pontjelző panel beállítása*/
        JPanel scoreBoard = new JPanel();
        JLabel[] scoreBoardReferences = new JLabel[2];
        JLabel redPlayerName = new JLabel("RED: ");
        redPlayerName.setForeground(Color.red);
        scoreBoard.add(redPlayerName);
        JLabel redPlayerScore = new JLabel(Integer.toString(logicalGrid.getRedPlayer().getScore()));
        redPlayerScore.setForeground(Color.red);
        scoreBoardReferences[0] = redPlayerScore;
        scoreBoard.add(redPlayerScore);
        JLabel bluePlayerName = new JLabel("BLUE: ");
        bluePlayerName.setForeground(Color.blue);
        scoreBoard.add(bluePlayerName);
        JLabel bluePlayerScore = new JLabel(Integer.toString(logicalGrid.getBluePlayer().getScore()));
        bluePlayerScore.setForeground(Color.blue);
        scoreBoardReferences[1] = bluePlayerScore;
        scoreBoard.add(bluePlayerScore);
        scoreBoard.setPreferredSize(new Dimension(200, 25));
        cp.add(scoreBoard);

        /**Kommentátorablak előkészítése*/
        JPanel commentatorWindow = new JPanel();
        JLabel currentPlayer = new JLabel(logicalGrid.getCurrentPlayer() + " IS UP.");
        currentPlayer.setForeground(Color.red);
        commentatorWindow.add(currentPlayer);
        commentatorWindow.setPreferredSize(new Dimension(200, 25));
        cp.add(commentatorWindow);

        /**Játéktábla és gombok előkészítése (vizuális és referenciatábla szinkronizálása)*/
        JButton[][] referenceGrid = new JButton[size][size];
        JPanel visualGrid = new JPanel();
        visualGrid.setPreferredSize(new Dimension(400, 400));
        visualGrid.setLayout(new GridLayout(size, size));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                addTile(scoreBoardReferences, currentPlayer, logicalGrid, referenceGrid, visualGrid, i, j);
            }
        }
        cp.add(visualGrid);
        
        /**ContentPane frissítése*/
        revalidate();
        repaint();
        System.out.println(size + "x" + size + "-as tábla legenerálva.");
    }
    
    private void developerMessage(){
        JDialog messageWindow = new JDialog(this, "Credits");
        JPanel messagePanel = new JPanel();
        JLabel developerMessage = new JLabel();
        developerMessage.setText("Developed by: Gabriel Paul Korom (former ELTE student)");
        messagePanel.add(developerMessage);
        messageWindow.add(messagePanel);
        messageWindow.setSize(400, 50);
        int xLocation = (Toolkit.getDefaultToolkit().getScreenSize().width - messageWindow.getWidth()) / 2;
        int yLocation = (Toolkit.getDefaultToolkit().getScreenSize().height - messageWindow.getHeight()) / 2;
        messageWindow.setLocation(xLocation, yLocation);
        messageWindow.setVisible(true);
    }
    
    private int topX() {
        return (Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2;
    }

    private int topY() {
        return (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2;
    }

    /**Új gomb hozzáadása a táblához (eseménykezelésel)
     * @param scoreBoardReferences A pontszámokat jelző címkék referenciái.
     * @param currentPlayer        A soronkövetkező játékos.
     * @param logicalGrid          A logikai tábla (a mezőkön lévő számértékeket tárolja).
     * @param referenceGrid        A nyomógombok referenciái (referenciatábla).
     * @param visualGrid           A nyomógombok GridLAyout-ba rendezbe (vizuális tábla).
     * @param i                    Az éppen generálandó sor száma.
     * @param j                    Az éppen generálandó oszlop száma.
     */
    private void addTile(JLabel[] scoreBoardReferences, JLabel currentPlayer, GameGrid logicalGrid, JButton[][] referenceGrid, JPanel visualGrid, int i, int j) {
        JButton newButton = new JButton();
        newButton.setText(Integer.toString(logicalGrid.getValue(i, j)));

        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Érzékeli");
                logicalGrid.increaseArea(i, j);
                updateScoreBoard(scoreBoardReferences, logicalGrid);
                updateCommentatorWindow(currentPlayer, logicalGrid);
                updateGrid(logicalGrid, referenceGrid);
                checkGameStatus(logicalGrid, currentPlayer, referenceGrid);
                logicalGrid.nextTurn();
            }
        });
        referenceGrid[i][j] = newButton;
        visualGrid.add(newButton);
        System.out.println("Új gomb hozzáadva.");
    }

    /**Pontjelző panel frissítése
     * @param scoreBoardReferences A pontszámokat jelző címkék referenciái.
     * @param logicalGrid          A logikai tábla (a mezőkön lévő számértékeket tárolja).
     */
    private void updateScoreBoard(JLabel[] scoreBoardReferences, GameGrid logicalGrid) {
        scoreBoardReferences[0].setText(Integer.toString(logicalGrid.getRedPlayer().getScore()));
        scoreBoardReferences[1].setText(Integer.toString(logicalGrid.getBluePlayer().getScore()));
    }

    /**Kommentátorablak frissítése
     * @param currentPlayer        A soronkövetkező játékos.
     * @param logicalGrid          A logikai tábla (a mezőkön lévő számértékeket tárolja).
     */
    private void updateCommentatorWindow(JLabel currentPlayer, GameGrid logicalGrid) {
        if (logicalGrid.getCurrentPlayer() == PlayerColor.RED) {
            currentPlayer.setText("BLUE IS UP.");
            currentPlayer.setForeground(Color.blue);
        } else {
            currentPlayer.setText("RED IS UP.");
            currentPlayer.setForeground(Color.red);
        }
    }

    /**Játéktábla frissítése
     * @param logicalGrid          A logikai tábla (a mezőkön lévő számértékeket tárolja).
     * @param referenceGrid        A nyomógombok referenciái (referenciatábla).
     */
    private void updateGrid(GameGrid logicalGrid, JButton[][] referenceGrid) {
        for (int row = 0; row < referenceGrid.length; row++) {
            for (int column = 0; column < referenceGrid[0].length; column++) {
                referenceGrid[row][column].setText(Integer.toString(logicalGrid.getValue(row, column)));
                if (logicalGrid.getValue(row, column) == 4 && referenceGrid[row][column].getBackground() == new JButton().getBackground()) {
                    if (logicalGrid.getCurrentPlayer() == PlayerColor.RED) {
                        referenceGrid[row][column].setBackground(Color.red);
                        referenceGrid[row][column].setEnabled(false);
                    } else {
                        referenceGrid[row][column].setBackground(Color.blue);
                        referenceGrid[row][column].setEnabled(false);
                    }
                }
                System.out.print(logicalGrid.getValue(row, column));
            }
            System.out.println();
        }
        revalidate();
        repaint();
    }

    /**Játékállás ellenőrzése (vége van-e már a játéknak?)
     * @param logicalGrid          A logikai tábla (a mezőkön lévő számértékeket tárolja).
     * @param currentPlayer        A soronkövetkező játékos.
     * @param referenceGrid        A nyomógombok referenciái (referenciatábla).
     */
    private void checkGameStatus(GameGrid logicalGrid, JLabel currentPlayer, JButton[][] referenceGrid) {
        if (logicalGrid.getRedPlayer().getScore() + logicalGrid.getBluePlayer().getScore() == Math.pow(logicalGrid.getGrid().length, 2)) {
            if (logicalGrid.getRedPlayer().getScore() > logicalGrid.getBluePlayer().getScore()) {
                currentPlayer.setText("RED WON!");
                currentPlayer.setForeground(Color.black);
                //announceWinner(PlayerColor.RED);
                //startNewGame(referenceGrid.length);
                System.out.println("Játék vége: A piros játékos győzött!");
            } else {
                currentPlayer.setText("BLUE WON!");
                currentPlayer.setForeground(Color.black);
                //announceWinner(PlayerColor.BLUE; 
                //startNewGame(referenceGrid.length);
                System.out.println("Játék vége: A kék játékos győzött!");
            }
            
            for (int i = 0; i < referenceGrid.length; i++) {
                for (int j = 0; j < referenceGrid[0].length; j++) {
                    referenceGrid[i][j].setEnabled(false);
                }
            }
        }
    }
    
    /**Győztes játékos bejelenetése
     * @param color A győztes játékos színe.
     */
    private void announceWinner(PlayerColor color) {
        JDialog endGameAnnouncement = new JDialog(this, "Játék vége");
        JPanel messagePanel = new JPanel();
        JLabel endGameMessage = new JLabel();
        endGameMessage.setText(color + " WON!");
        messagePanel.add(endGameMessage);
        endGameAnnouncement.add(messagePanel);
        endGameAnnouncement.setSize(100, 70);
        endGameAnnouncement.setLocation(topX() + getWidth()/3, topY() + getHeight()/2);
        endGameAnnouncement.setVisible(true);
    }

    /**Main függvény
     * @param args
     */
    public static void main(String[] args) {
        GameWindow window = new GameWindow();
        window.setVisible(true);
        window.setVisible(true);
    }
}//end GameWindow
