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

    public void setPlayerNum(int numPlayers) {
        this.model.setPlayerNum(numPlayers);
    }

    // ask user to initialize player names and gender (for printing data)
    public void initPlayers(int numPlayers, int startRank, int startCredits) {
        view.initPlayers(numPlayers, startRank, startCredits, this);
    }

    public Player startGame(LinkedList<Player> players) {
        model.setupPlayers(players);
        return model.getActivePlayer();
    }

    private void gameLoop(Board board, int maxDays) {
        Scanner input = new Scanner(System.in);
        String cmd;
        this.day = 0;

        for (int currDay = 0; currDay < maxDays; ++currDay) {

            while (true) {
                System.out.print("> ");
                cmd = input.nextLine();
                model.processCmd(cmd);

                if (this.scenesUsed == 9) {
                    this.scenesUsed = 0;
                    break;
                }
            }

            System.out.println("Day " + (currDay + 1) + " is over! That's a wrap.");
            board.newDay();
            board.setCards();
        }

        model.activateWin();
    }
    
}
