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

    public int rollPlayerDice(int numOfDice) {
        LinkedList<Integer> diceRoll = this.dice.rollNDice(numOfDice);
        int sum = 0;

        for (int roll : diceRoll) {
            sum += roll;
        }

        return sum;
    }
}
