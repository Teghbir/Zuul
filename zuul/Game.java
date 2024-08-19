import java.util.*;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes 
 * @version 2006.03.30
 * 
 * @author Lynn Marshall
 * @version A1 Solution
 * 
 * @author Teghbir Chadha (101258511)
 * @version A2 Solution
 * 
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Room previousRoom;
    private Stack<Room> previousRoomStack;
    
    private Item currentItem;
    
    //to check whether candy has been eaten
    private boolean eatenCandy;
    //to check how many items have been picked up after eating candy
    private int itemsPicked;
        
    /**
     * Create the game and initialise its internal map, as well
     * as the previous room (none) and previous room stack (empty).
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        previousRoom = null;
        previousRoomStack = new Stack<Room>();
        eatenCandy = false;
        itemsPicked = 0;
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theatre, pub, lab, office;
        TransporterRoom transporterRoom;
        Item chair1, chair2, chair3, chair4, bar, computer1, computer2, computer3, tree1, tree2, candy1, candy2;
        Beamer beamer1, beamer2;
        // creating items
        chair1 = new Item("a wooden chair",5, "chair");
        chair2 = new Item("a wooden chair",5, "chair");
        chair3 = new Item("a wooden chair",5, "chair");
        chair4 = new Item("a wooden chair",5, "chair");
        bar = new Item("a long bar with stools",95.67, "stool");
        computer1 = new Item("a PC",10, "computer");
        computer2 = new Item("a Mac",5, "computer");
        computer3 = new Item("a PC",10, "computer");
        tree1 = new Item("a fir tree",500.5, "tree");
        tree2 = new Item("a fir tree",500.5, "tree");
        candy1 = new Item("a candy",1, "candy");
        candy2 = new Item("a candy",1, "candy");
        beamer1 = new Beamer("a device beamer",10, "beamer");
        beamer2 = new Beamer("a device beamer",10, "beamer");
       
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        //create transporter room
        transporterRoom = new TransporterRoom("in a mysterious transporter room");
        
        // put items in the rooms
        outside.addItem(tree1);
        outside.addItem(tree2);
        theatre.addItem(chair1);
        pub.addItem(bar);
        lab.addItem(chair2);
        lab.addItem(computer1);
        lab.addItem(chair3);
        lab.addItem(computer2);
        office.addItem(chair4);
        office.addItem(computer3);
        office.addItem(candy1);
        lab.addItem(candy2);
        office.addItem(beamer1);
        outside.addItem(beamer2);
        
        // initialise room exits
        outside.setExit("east", theatre); 
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theatre.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);
        
        //set up ways into the transporter room
        outside.setExit("north", transporterRoom); 
        lab.setExit("west", transporterRoom); 
        
        //no rooms connected to transporter room
        transporterRoom.setExit("anywhere", null); 

        currentRoom = outside;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
        
        if (currentItem != null) {
            System.out.println("You are carrying " + currentItem.getName() + ".");
        }else {
            System.out.println("You are not carrying anything.");
        }
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed
     * @return true If the command ends the game, false otherwise
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")) {
            look(command);
        }
        else if (commandWord.equals("eat")) {
            eat(command);
        }
        else if (commandWord.equals("back")) {
            back(command);
        }
        else if (commandWord.equals("stackBack")) {
            stackBack(command);
        }
        //new commands for the game
        else if (commandWord.equals("take")) {
            take(command);
        } 
        else if (commandWord.equals("drop")) {
            drop(command);
        }
        else if (commandWord.equals("charge")) {
            charge(command);
        } else if (commandWord.equals("fire")) {
            fire(command);
        }
        
        // else command not recognised.
        return wantToQuit;
    }

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.getCommands());
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * If we go to a new room, update previous room and previous room stack.
     * 
     * @param command The command to be processed
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            previousRoom = currentRoom; // store the previous room
            previousRoomStack.push(currentRoom); // and add to previous room stack
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     * @return true, if this command quits the game, false otherwise
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /** 
     * "Look" was entered. Check the rest of the command to see
     * whether we really want to look.
     * 
     * @param command The command to be processed
     */
    private void look(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Look what?");
        }
        else {
            // output the long description of this room
            System.out.println(currentRoom.getLongDescription());
        }
    }
    
    /** 
     * "Eat" was entered. Check the rest of the command to see
     * whether we really want to eat.
     * 
     * @param command The command to be processed
     */
    private void eat(Command command) 
    {
        //checks to see if the eat command has more than one word
        if(command.hasSecondWord()) {
            System.out.println("Eat what?");
        }
        else {
            //checks that user has picked up an item and it has to be candy
            if (currentItem != null && currentItem.getName().equals("candy")) {
                System.out.println("You have eaten the candy and are no longer hungry.");
                itemsPicked = 0;
                eatenCandy = true;
                currentItem = null;
            } else{
                System.out.println("You have no candy.");
            }
        }
    }
    
    /** 
     * "Back" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     */
    private void back(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Back what?");
        }
        else {
            // go back to the previous room, if possible
            if (previousRoom==null) {
                System.out.println("No room to go back to.");
            } else {
                // go back and swap previous and current rooms,
                // and put current room on previous room stack
                Room temp = currentRoom;
                currentRoom = previousRoom;
                previousRoom = temp;
                previousRoomStack.push(temp);
                // and print description
                System.out.println(currentRoom.getLongDescription());
            }
        }
    }
    
    /** 
     * "StackBack" was entered. Check the rest of the command to see
     * whether we really want to stackBack.
     * 
     * @param command The command to be processed
     */
    private void stackBack(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("StackBack what?");
        }
        else {
            // step back one room in our stack of rooms history, if possible
            if (previousRoomStack.isEmpty()) {
                System.out.println("No room to go stack back to.");
            } else {
                // current room becomes previous room, and
                // current room is taken from the top of the stack
                previousRoom = currentRoom;
                currentRoom = previousRoomStack.pop();
                // and print description
                System.out.println(currentRoom.getLongDescription());
            }
        }
    }
    
    /** 
     * "take" was entered. Check the rest of the command to see
     * whether we really want to take.
     * 
     * @param command The command to be processed
     */
    private void take(Command command) {
        //Check to make sure that the command has a second word
        if (!command.hasSecondWord()) {
            System.out.println("Take what?");
            return;
        }
        //Item to be taken
        String itemName = command.getSecondWord();
        Item itemToTake = currentRoom.getItem(itemName);
        //Checks if the item user is trying to take can be taken and whether it is candy
        if (itemToTake != null && itemToTake.getName().equals("candy")) {
            currentItem = itemToTake;
            currentRoom.removeItem(itemName);
            System.out.println("You picked up " + itemName + ".");
        //Checks whether or not the player has eaten candy and can pick up other items now
        } else if (eatenCandy == true && itemsPicked < 5){
            if (currentItem != null) {
                System.out.println("You are already holding something.");
            } else if (itemToTake == null) {
                System.out.println("That item is not in the room.");
            } else {
                currentItem = itemToTake;
                itemsPicked++;
                currentRoom.removeItem(itemName);
                System.out.println("You picked up " + itemName + ".");
            }
        } else{
            //If user tries to pick up something without eating candy or tries to
            //pick up more than five items after eating candy
            System.out.println("You must find and eat a candy first.");
        }
    }
    
    
    /** 
     * "drop" was entered. Checks to see if the user is holding anything.
     * 
     * @param command The command to be processed
     */
    private void drop(Command command) {
        if (currentItem == null) {
            System.out.println("You have nothing to drop");
        }else {
            //drops the item if the user is holding something
            currentRoom.addItem(currentItem);
            System.out.println("You have dropped " + currentItem.getName() + ".");
            currentItem = null;
        }
    }
    
    /** 
     * "charge" was entered. Calls the charge method if the user has a beamer
     * and that the user only entered the one word command charge
     * 
     * @param command The command to be processed
     */
    private void charge(Command command) {
        //checks to see if the user isn't holding something or holding something that isnt a beamer
        if (currentItem == null || !currentItem.getName().equals("beamer")) {
            System.out.println("You are not holding a beamer");
        }
        else if (currentItem.getName().equals("beamer")){
            if (command.hasSecondWord()) {
                System.out.println("Charge what?");
            } else {
                ((Beamer) currentItem).charge(currentRoom);
            }
        }
    }
    
    /** 
     * "fire" was entered. Calls the fire methodif the user has a beamer
     * and that the user only entered the one word command fire
     * 
     * @param command The command to be processed
     */
    private void fire(Command command) {
        //checks to see if the user isn't holding something or holding something that isnt a beamer
        if (currentItem == null || !currentItem.getName().equals("beamer")) {
            System.out.println("You are not holding a beamer");
        }
        else if(currentItem.getName().equals("beamer")) {
            if (command.hasSecondWord()) {
                System.out.println("Fire what?");
            } else {
                if (((Beamer) currentItem).isCharged()) {
                    currentRoom = ((Beamer) currentItem).fire();
                    System.out.println("You have been transported to where the beamer was charged." + currentRoom.getLongDescription());
                } else{
                    ((Beamer) currentItem).fire();
                }
            }
        }
    }
}
