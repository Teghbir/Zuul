/**
 * Child class of Item for adding a beamer to the game which can be charged and fired. 
 *
 * @author Teghbir Chadha
 * @version A2 Solution 
 */
public class Beamer extends Item {
    private Room chargedRoom;
    private boolean isCharged;

    /**
     * Constructor for objects of class Beamer.
     * 
     * @param description The description of the item
     * @param weight The weight of the item
     * @param name The name of the item
     */
    public Beamer(String description, double weight, String name) {
        super(description, weight, name);
        this.chargedRoom = null;
        this.isCharged = false;
    }

    /**
     * Returns the name of the item.
     * 
     * @return  The name of the item
     */
    public String getName() {
        return super.getName();
    }

    /**
     * Charges the device beamer in the current room if it isn't already charged
     * 
     * @param  current room of type Room
     */
    public void charge(Room currentRoom) {
        if (!isCharged) {
            chargedRoom = currentRoom;
            isCharged = true;
            System.out.println("Beamer has been charged in the current room.");
        } else {
            System.out.println("Beamer is already charged.");
        }
    }

    /**
     * Checks and returns the value of the variable isCharged.
     * 
     * @return the boolean status of the variable isCharged
     */
    public boolean isCharged() {
        return isCharged;
    }

    /**
     * Fires the device if user has a charged beamer
     * 
     * @return the room where the beamer was charged
     */
    public Room fire() {
        if (isCharged) {
            isCharged = false;
            return chargedRoom;
        } else {
            System.out.println("Beamer has not been charged yet.");
            return null;
        }
    }
}
