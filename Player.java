import java.util.LinkedList;

public class Player {
    private int dollars;
    private int credits;
    private int practiceChips;
    private PlayerDice dice;
    private String name;
    private String gender;
    private Location currLocation;
    private boolean workingRole;
    private Role currRole;

    public Player(String name, int startRank, int startCredits, String gender) {
        this.name = name;
        this.dollars = 0;
        this.credits = startCredits;
        this.practiceChips = 0;
        this.dice = new PlayerDice();
        this.gender = gender;
        this.workingRole = false;
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

    public void changeRole(Role role) {
        this.workingRole = true;
        this.currRole = role;
    }

    public void removeRole() {
        this.workingRole = false;
        this.currRole = null;
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

    public boolean incrementPracitce() {
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
        this.removeRole();
    }
}
