import java.util.*;
/**
 * This class is a subclass of Room and used to create rooms of type TransporterRoom
 * in a room in the game of Zuul.
 * 
 * @author Teghbir Chadha
 * @version A2 Solution  
 */
public class TransporterRoom extends Room
{
    private Random random;
    /**
     * Constructor for the TransporterRoom. Calls the superclass constructor
     * to set the room's description.
     *
     * @param description The room's description.
     */
    public TransporterRoom(String description) {
        super(description);
        random = new Random();
    }
    
    /**
     * Returns a random room, independent of the direction parameter.
     *
     * @param direction Ignored in this subclass.
     * @return A randomly selected room.
     */
    @Override
    public Room getExit(String direction) {
        return findRandomRoom();
    }
    
    /**
     * Chooses a random room from the list of all rooms.
     *
     * @return The room we end up in upon leaving this one.
     */
    private Room findRandomRoom() {
        //Stores all the rooms returned in the variable 
        ArrayList<Room> allRooms = Room.getAllRooms();
        int randomIndex = random.nextInt(allRooms.size());
        return allRooms.get(randomIndex);
    }
}
