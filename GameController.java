import java.util.LinkedList;
import java.util.Scanner;

public class GameController {

    private GameManager model;
    private GameView view;
    int day, scenesUsed;

    public GameController(GameManager model, GameView view) {
        this.model = model;
        this.view = view;
    }

    public int initPlayerNum() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the number of players: ");
        return Integer.parseInt(scan.nextLine());
    }

    // ask user to initialize player names and gender (for printing data)
    public LinkedList<Player> initPlayers(int numPlayers, int startRank, int startCredits) {
        Scanner scan = new Scanner(System.in);
        LinkedList<Player> players = new LinkedList<>();

        for (int i = 0; i < numPlayers; ++i) {
            System.out.print("Please enter the name of player " + (i + 1) + ": ");
            String currPlayer = scan.next();
            System.out.println("Please enter the gender of [" + currPlayer + "] [M/F/GN]: ");
            String genderChoice = scan.next();

            // ask until user input is valid
            while ((!genderChoice.equals("M") && !genderChoice.equals("F")) && !genderChoice.equals("GN")) {
                System.out.println("Invalid selection, please try again.");
                System.out.println("Please enter the gender of [" + currPlayer + "] [M/F/GN]: ");
                genderChoice = scan.next();
            }

            String playerGender;
            switch (genderChoice) {
                case "M":
                    playerGender = "He";
                    break;
                case "F":
                    playerGender = "She";
                    break;
                case "GN":
                default:
                    playerGender = "They";
                    break;
            }

            Player tempPlayer = new Player(currPlayer, startRank, startCredits, playerGender);
            players.add(tempPlayer);
        }

        return players;
    }

    public void initWindow() {
        view.initWindow();
    }

    public void gameLoop(Board board, int maxDays) {
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
