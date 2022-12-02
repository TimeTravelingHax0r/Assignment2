import java.util.LinkedList;

import javax.swing.JLabel;

public class Player {
    private int dollars;
    private int credits;
    private int practiceChips;
    private PlayerDice dice;
    private String name;
    private String gender;
    private Location currLocation;
    private boolean workingRole;
    private boolean justWrapped;
    private Role currRole;
    private String warningMsg;
    private LinkedList<String> sceneWrapMsgs;
    private String diceImgName;
    JLabel diceLabel;

    public Player(String name, int startRank, int startCredits, String gender, String diceImgName) {
        this.name = name;
        this.dollars = 0;
        this.credits = startCredits;
        this.practiceChips = 0;
        this.dice = new PlayerDice();
        this.gender = gender;
        this.workingRole = false;
        this.justWrapped = false;
        this.diceImgName = diceImgName;
        dice.updateRank(startRank);
    }

    public int rollDie() {
        return this.dice.rollDie();
    }

    public LinkedList<Integer> rollDice(int numOfDice) {
        return dice.rollPlayerDice(numOfDice);
    }

    public String getName() {
        return this.name;
    }

    public int getDollars() {
        return this.dollars;
    }

    public int getCredits() {
        return this.credits;
    }

    public Location getLocation() {
        return this.currLocation;
    }

    public int getRank() {
        return dice.getRank();
    }

    public String getGender() {
        return this.gender;
    }

    public int getChips() {
        return this.practiceChips;
    }

    public Role getRole() {
        return this.currRole;
    }

    public boolean justWrapped() {
        if (justWrapped) {
            this.justWrapped = false;
            return true;
        } else {
            return false;
        }
    }

    public String getImgName() {
        return this.diceImgName;
    }

    public String getWarning() {
        return this.warningMsg;
    }

    public LinkedList<String> getMsgs() {
        return this.sceneWrapMsgs;
    }

    public JLabel getLabel() {
        return this.diceLabel;
    }

    public void setLabel(JLabel label) {
        this.diceLabel = label;
    }

    public void setWrapped() {
        this.justWrapped = true;
    }

    public void setWarning(String warning) {
        this.warningMsg = warning;
    }

    public void setWrapMsgs(LinkedList<String> msgs) {
        this.sceneWrapMsgs = msgs;
    }

    public void changeRole(Role role) {
        this.workingRole = true;
        this.currRole = role;
    }

    public void removeRole() {
        this.workingRole = false;
        this.currRole = null;
        this.clearPractice();
    }

    public Role currRole() {
        return this.currRole;
    }

    public boolean workingRole() {
        return this.workingRole;
    }

    public boolean updateCredits(int amount) {
        if ((amount) < 0 && ((-amount) > this.credits)) {
            System.out.println("Insufficient funds: credit");
            return false;
        } else {
            this.credits += amount;
            return true;
        }
    }

    public boolean updateDollars(int amount) {
        if ((amount) < 0 && ((-amount) > this.dollars)) {
            System.out.println("Insufficient funds: dollar");
            return false;
        } else {
            this.dollars += amount;
            return true;
        }
    }

    public void updateRank(int desiredRank) {
        this.dice.updateRank(desiredRank);
    }

    public boolean incrementPractice() {
        if (this.practiceChips == 5) {
            System.out.println("Maximum chips reached");
            return false;
        } else {
            this.practiceChips++;
            return true;
        }
    }

    public void clearPractice() {
        this.practiceChips = 0;
    }

    public void movePlayer(Location newLocation) {
        this.currLocation = newLocation;
        this.currLocation.setDiscovery();
        this.removeRole();
    }
}
