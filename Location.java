import java.util.LinkedList;

public class Location {
    private String name;
    private SceneCard card;
    public LinkedList<Role> offCardRoles;
    private int totalShots;
    private boolean isSpecial; // used for trailer, casting office

    public Location(String name, LinkedList<Role> offCardRoles, int shots) {
        this.name = name;
        this.offCardRoles = offCardRoles;
        this.totalShots = shots;
        this.isSpecial = false;
    }

    public Location(String name) {
        this.name = name;
        this.isSpecial = true;
    }

    public void setCard(SceneCard card) {
        if (!isSpecial) {
            this.card = card;
        } else {
            System.out.println("Cannot put special card on this location.");
        }
    }

    public String getName() {
        return this.name;
    }

    public int getBudget() {
        if (!isSpecial) {
            return card.getBudget();
        } else {
            return -1;
        }
    }

    public SceneCard getCard() {
        if (!isSpecial) {
            return this.card;
        } else {
            return null;
        }
    }

    public LinkedList<Role> getOffCard() {
        if (!isSpecial) {
            return this.offCardRoles;
        } else {
            return null;
        }
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
