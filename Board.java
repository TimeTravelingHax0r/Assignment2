import java.util.LinkedList;
import java.util.Collections;
import java.util.HashMap;

public class Board {
    private LinkedList<Location> locations;
    private LinkedList<SceneCard> cards;
    private HashMap<String, LinkedList<String>> connections;
    private Upgrades upgrades;

    public Board(LinkedList<Location> locations, HashMap<String, LinkedList<String>> connections,
            LinkedList<SceneCard> cards, Upgrades upgrades) {
        this.locations = locations;
        this.connections = connections;
        this.upgrades = upgrades;
        this.cards = cards;
    }

    public Location getLocation(String locName) {
        for (Location location : this.locations) {
            if (location.getName().equals(locName)) {
                return location;
            }
        }

        return null;
    }

    public Upgrades getUpgradeMap() {
        return this.upgrades;
    }

    private LinkedList<String> getAdjLocations(Player player) {
        Location playerLoc = player.getLocation();

        return this.connections.get(playerLoc.getName());
    }

    public void setCards() {
        Collections.shuffle(this.cards);
        for (int i = 0; i < locations.size(); ++i) {

            Location currLoc = this.locations.get(i);

            if (currLoc.getName().equals("office") || currLoc.getName().equals("trailer")) {
                continue;
            }

            SceneCard currCard = this.cards.pollFirst();
            currLoc.setCard(currCard);
            this.cards.remove(currCard);
        }
    }

    public boolean moveLegal(Player player, String newLocation) {
        LinkedList<String> adjLocations = getAdjLocations(player);

        return adjLocations.contains(newLocation);
    }

    public void newDay() {
        for (Location location : this.locations) {
            location.resetScene();
        }
    }
}
