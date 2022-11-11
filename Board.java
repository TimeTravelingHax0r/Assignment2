import java.util.LinkedList;
import java.util.HashMap;

public class Board {
    private LinkedList<Location> locations;
    private Location castingOffice;
    private Location trailer;
    private HashMap<String, LinkedList<String>> connections;
    private Upgrades upgrades;

    public Board(LinkedList<Location> locations, HashMap<String, LinkedList<String>> connections, Upgrades upgrades, Location trailer, Location castingOffice) {
        this.locations = locations;
        this.castingOffice = castingOffice;
        this.trailer = trailer;
        this.connections = connections;
        this.upgrades = upgrades;
    }

    public LinkedList<String> getAdjLocations(Player player) {
        return new LinkedList<String>();
    }

    public void setBoard(LinkedList<Location> locations, HashMap<Location, Location> connections) {

    }

    public boolean moveLegal(Location currLocation, Location newLocation) {
        return true;
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
}
