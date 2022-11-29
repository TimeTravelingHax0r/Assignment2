import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class GameView {

    private BoardLayersListener bll; 

    public GameView() {

    }

    public void initWindow(Board board, GameController gc) {
        this.bll = new BoardLayersListener(gc);
        this.bll.setVisible(true);

        this.bll.toggleHowManyOpts();
    }

    public void initPlayers(int numPlayers, int startRank, int startCredits, GameController gc) {
        this.bll.setNumRankCredits(numPlayers, startRank, startCredits);
        this.bll.togglePlayerNameOpts();
    }

    public void getPlayers() {
        this.bll.getPlayers();
    }
}
