import java.util.LinkedList;
import java.util.Scanner;

public class GameController {

    private GameManager model;
    private GameView view;
    private int day, scenesUsed;

    public GameController(GameManager model, GameView view) {
        this.model = model;
        this.view = view;
    }

    public void initWindow() {
        view.initWindow(this.model.getBoard(), this);
    }

    public Player getActivePlayer() {
        return model.getActivePlayer();
    }

    public boolean moveUsed() {
        return model.moveUsed();
    }

    public void setPlayerNum(int numPlayers) {
        this.model.setPlayerNum(numPlayers);
    }

    public void setWarning(String warning) {
        this.view.setWarning(warning);
    }

    // ask user to initialize player names and gender (for printing data)
    public void initPlayers(int numPlayers, int startRank, int startCredits) {
        view.initPlayers(numPlayers, startRank, startCredits, this);
    }

    public void startGame(LinkedList<Player> players) {
        model.setupPlayers(players);
    }

    public void updateGameState(String cmd) {
        model.processCmd(cmd);
        view.updatePlayerInfo();
        view.updateLocationState();
        view.updatePlayerLoc();
        this.scenesUsed = model.getScenesUsed();
        if (this.scenesUsed >= 9) {
            model.resetScenesUsed();
            this.scenesUsed = model.getScenesUsed();

            model.newDay();
        }
    }
}
