import java.util.LinkedList;

public class Location {
    private String name;
    private SceneCard card;
    public LinkedList<Role> offCardRoles;
    int[] shots;

    public Location(String name, SceneCard card, LinkedList<Role> offCardRoles, int[] shots) {
        this.name = name;
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

    public String getName() {
        return this.sceneName;
    }

    public void takeRole() {
        // implement later
    }

    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o) {
            return true;
        }
        // null check
        if (o == null) {
            return false;
        }
        // type check and cast
        if (getClass() != o.getClass()) {
            return false;
        }

        Location otherLoc = (Location) o;

        if (this.sceneName.equals(otherLoc.getName())) {
            return true;
        } else {
            return false;
        }
    }

}
