import java.util.LinkedList;

public class Location {
    private String name;
    private SceneCard card;
    public LinkedList<Role> offCardRoles;
    private int totalShots;

    public Location(String name, LinkedList<Role> offCardRoles, int shots) {
        this.name = name;
        this.offCardRoles = offCardRoles;
        this.totalShots = shots;
    }

    public void setCard(SceneCard cart) {
        this.card = card;
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
        return this.name;
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

        if (this.name.equals(otherLoc.getName())) {
            return true;
        } else {
            return false;
        }
    }

}
