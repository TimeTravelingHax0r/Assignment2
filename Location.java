import java.util.LinkedList;

public class Location {
    private SceneCard card;
    public LinkedList<SceneCard> offTheCard;

    public Location() {
        
    }

    public int getBudget() {
        return 0;
    }

    public LinkedList<Role> getCard() {
        return new LinkedList<Role>();
    }

    public LinkedList<Role> getOffCard() {
        return new LinkedList<Role>();
    }

    public void takeRole() {

    }

}
