public class Player {
    private int dollars;
    private int credits;
    private int practiceChips;
    private PlayerDice dice;
    private String name;
    private Location currLocation;

    public Player(String name, int startRank, int startCredits) {
        this.name = name;
        this.dollars = 0;
        this.credits = startCredits;
        this.practiceChips = 0;
        this.dice = new PlayerDice();
        dice.updateRank(startRank);
    }

    public int rollDice(int numOfDice) {
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

    public boolean updateCredit(int amount) {
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

    public boolean updateRank(int desiredRank) {
        if (desiredRank > this.dice.getRank()) {
            this.dice.updateRank(desiredRank);
            return false;
        } else {
            System.out.println("Cannot lower rank");
            return true;
        }
    }

    public void movePlayer(Location newLocation) {
        this.currLocation = newLocation;
    }
}
