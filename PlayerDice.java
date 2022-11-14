import java.util.LinkedList;

public class PlayerDice {
    private int rank;
    public Dice dice;

    public PlayerDice() {
        this.rank = 1;
        this.dice = new Dice();
    }

    public int getRank() {
        return this.rank;
    }

    public void updateRank(int rank) {
        this.rank = rank;
    }

    public int rollDie() {
        LinkedList<Integer> roll = this.dice.rollNDice(1);
        return roll.get(0);
    }

    public LinkedList<Integer> rollPlayerDice(int numOfDice) {
        return this.dice.rollNDice(numOfDice);
    }
}
