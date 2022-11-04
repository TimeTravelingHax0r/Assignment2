import java.util.LinkedList;
import java.util.HashMap;

public class Board {
    private LinkedList<Location> locations;
    private Location castingOffice;
    private Location trailer;
    private HashMap<Location, Location> connections;

    public Board() {

    }

    public LinkedList<Location> getLocations() {
        return  new LinkedList<Location>();
    }

    public void setBoard(LinkedList<Location> locations, HashMap<Location, Location> connections) {

    }

    public boolean moveLegal(Location currLocation, Location newLocation) {
        return true;
    }

    public Location getCasting() {
        return new Location();
    }

    public Location getTrailer() {
        return new Location();
    }
}
