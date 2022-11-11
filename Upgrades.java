import java.util.HashMap;

public class Upgrades {
    private HashMap<Integer, Integer> dollarCosts;
    private HashMap<Integer, Integer> creditCosts;

    public Upgrades(HashMap<Integer, Integer> dollarsCosts, HashMap<Integer, Integer> creditCosts) {
        this.dollarCosts = dollarsCosts;
        this.creditCosts = creditCosts;
    }

    public HashMap<Integer, Integer> getDollarCosts() {
        return this.dollarCosts;
    }

    public HashMap<Integer, Integer> getCreditCosts() {
        return this.creditCosts;
    }
}
