import java.util.LinkedList;

public class Location {
    private SceneCard card;
    public LinkedList<Role> offCardRoles;
    int[] shots;

    public Location(SceneCard card, LinkedList<Role> offCardRoles, int[] shots) {
        this.card = card;
        this.offCardRoles = offCardRoles;
        this.shots = shots;
    }

    public int getBudget() {
        return card.getBudget();
    }

    public SceneCard getCard() {
        return this.card;
    }

    public LinkedList<Role> getOffCard() {
        return this.offCardRoles;
    }

    public void takeRole() {
        // implement later
    }

}
