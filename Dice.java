import java.util.LinkedList;
import java.util.Random;

public class Dice {

    private Random rand;

    public Dice() {
        this.rand = new Random();
    }

    public LinkedList<Integer> rollNDice(int n) {
        LinkedList<Integer> diceRoll = new LinkedList<Integer>();
        int currRoll;

        for (int i = 0; i < n; ++i) {
            currRoll = rand.nextInt(6) + 1;
            diceRoll.add(currRoll);
        }

        return diceRoll;
    }
}
