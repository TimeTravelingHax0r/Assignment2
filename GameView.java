import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class GameView {

    private BoardLayersListener bll; 
    private Board board;

    public GameView() {

    }

    public String getCmd() {
        return bll.getCurrCmd();
    }

    public void setWarning(String warning) {
        this.bll.toggleWarning(warning);
    }

    public void initWindow(Board board, GameController gc) {
        this.board = board;

        this.bll = new BoardLayersListener(gc, board);
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

    public void updatePlayerLoc() {

        HashMap<String,Integer> locationOffSets = new HashMap<>();
        int x, y, h;
        int roleX, roleY;
        int trailerY = 0;
        int officeY = 0;

        LinkedList<Player> players = this.bll.getPlayers();
        for (Player player : players) {
            Location currLoc = player.getLocation();
            String currLocName = currLoc.getName();
            JLabel currLabel = player.getLabel();
            x = currLoc.getX();
            y = currLoc.getY(); 
            h = currLoc.getH(); 

            if (!locationOffSets.containsKey(currLocName)) {
                locationOffSets.put(currLocName, 0);
            } else {
                locationOffSets.put(currLocName, locationOffSets.get(currLocName)+50);
            }

            if (currLocName.equals("trailer")) {
                if (locationOffSets.get("trailer") > 194) {
                    locationOffSets.put(currLocName, 0);
                    trailerY += 50;
                }
                currLabel.setLocation(x+locationOffSets.get(currLocName),y+trailerY);
            } else if (currLocName.equals("office")) {
                if (locationOffSets.get("office") > 209) {
                    locationOffSets.put(currLocName, 0);
                    officeY += 50;
                }
                currLabel.setLocation(x+locationOffSets.get(currLocName),y+officeY);
            } else {
                if (player.workingRole()) {
                    Role currRole = player.currRole();
                    roleX = currRole.getX();
                    roleY = currRole.getY();
                    if (currRole.onCard()) {
                        currLabel.setLocation(x+roleX, y+roleY);
                    } else {
                        currLabel.setLocation(roleX, roleY);
                    }
                } else {
                    currLabel.setLocation(x+locationOffSets.get(currLocName),y+h-40);
                }      
            }
            
            this.bll.setLabelFront(currLabel);
        }
    }

    public void updateLocationState() {
        this.bll.updateLocationState(this.board.getCurrLocs());
    }

    public void updatePlayerInfo() {
        this.bll.updatePlayerInfo();
    }
}
