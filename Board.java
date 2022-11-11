import java.util.LinkedList;
import java.util.HashMap;

public class Board {
    private LinkedList<Location> locations;
    private Location castingOffice;
    private Location trailer;
    private HashMap<Location, LinkedList<String>> connections;

    public Board(LinkedList<Location> locations, Location castingOffice, Location trailer, HashMap<Location, LinkedList<String>> connections) {
        this.locations = locations;
        this.castingOffice = castingOffice;
        this.trailer = trailer;
        this.connections = connections;
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
        return new Location("", new SceneCard("", 5, new LinkedList<Role>()), new LinkedList<Role>(), test);
    }

    public Location getTrailer() {
        return this.trailer;
    }
}
