import java.util.LinkedList;
import java.util.Collections;
import java.util.HashMap;

public class Board {
    private LinkedList<Location> locations;
    private LinkedList<SceneCard> cards;
    private Location castingOffice;
    private Location trailer;
    private HashMap<String, LinkedList<String>> connections;
    private Upgrades upgrades;

    public Board(LinkedList<Location> locations, HashMap<String, LinkedList<String>> connections, LinkedList<SceneCard> cards, Upgrades upgrades, Location trailer, Location castingOffice) {
        this.locations = locations;
        this.castingOffice = castingOffice;
        this.trailer = trailer;
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

    public boolean moveLegal(Player player, String newLocation) {
        LinkedList<String> adjLocations = getAdjLocations(player);

        return adjLocations.contains(newLocation);
    }

    public Location getCasting() {
        int[] test = {1,2,3};
        return new Location("", new LinkedList<Role>(), 1);
    }

    public Location getTrailer() {
        return this.trailer;
    }

    public Upgrades getUpgradeMap() {
        return this.upgrades;
    }

    public void setCards() {
        Collections.shuffle(this.cards);
        for (int i = 0; i < locations.size(); ++i) {
            SceneCard currCard = this.cards.pollFirst();
            this.locations.get(i).setCard(currCard);
        }
    }

    private LinkedList<String> getAdjLocations(Player player) {
        Location playerLoc = player.getLocation();

        return this.connections.get(playerLoc.getName());
    }
}
