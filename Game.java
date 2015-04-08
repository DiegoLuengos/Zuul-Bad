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
 * @author  Michael KÃ¶lling and David J. Barnes
 * @version 2011.07.31
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room exterior, entrada, cocina, salon, patio, despensa, sotano;
      
        // create the rooms
        exterior = new Room("el exterior.");
        entrada = new Room("la entrada.");
        cocina = new Room("la cocina.");
        salon = new Room("el salon.");
        patio = new Room("el patio.");
        despensa = new Room("la despensa.");
        sotano = new Room("el sotano, la habitación que más miedo da.");
        
        // initialise room exits
        exterior.setExits(null, entrada, null, null);
        entrada.setExits(cocina, null, salon, exterior);
        salon.setExits(entrada, sotano, null, null);
        sotano.setExits(null, null, null, salon);
        cocina.setExits(null, patio, entrada, despensa);
        patio.setExits(null, null, null, cocina);
        despensa.setExits(null, cocina, null, null);

        currentRoom = exterior;  // start game outside
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
        System.out.println("Gracias por jugar.  Adiós :D");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Bienvenido a la casa del terror.");
        System.out.println("Preparate para pasar mucho miedo buscando \na un fantasma que está escondido en la casa.");
        System.out.println("Escribe 'ayuda' si necesitas ayuda.");
        System.out.println();
        System.out.println("Estás en " + currentRoom.getDescription());
        System.out.print("Salidas: ");
        if(currentRoom.northExit != null) {
            System.out.print("norte ");
        }
        if(currentRoom.eastExit != null) {
            System.out.print("este ");
        }
        if(currentRoom.southExit != null) {
            System.out.print("sur ");
        }
        if(currentRoom.westExit != null) {
            System.out.print("oeste ");
        }
        System.out.println();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("No se a qué te refieres.");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("ayuda")) {
            printHelp();
        }
        else if (commandWord.equals("ir")) {
            goRoom(command);
        }
        else if (commandWord.equals("salir")) {
            wantToQuit = quit(command);
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("Intenta no perderte o te quedarás atrapado para siempre.");
        System.out.println("El fantasma se encuentra en una de las habitaciones.");
        System.out.println("Es fácil suponer en qué habitaciíon está.");
        System.out.println("Tus comandos son:");
        System.out.println("   ir salir y ayuda");
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("¿Ir a dónde?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = null;
        if(direction.equals("norte")) {
            nextRoom = currentRoom.northExit;
        }
        if(direction.equals("este")) {
            nextRoom = currentRoom.eastExit;
        }
        if(direction.equals("sur")) {
            nextRoom = currentRoom.southExit;
        }
        if(direction.equals("oeste")) {
            nextRoom = currentRoom.westExit;
        }

        if (nextRoom == null) {
            System.out.println("¡Ahí no hay ninguna puerta!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println("Estás en " + currentRoom.getDescription());
            System.out.print("Salidas: ");
            if(currentRoom.northExit != null) {
                System.out.print("norte ");
            }
            if(currentRoom.eastExit != null) {
                System.out.print("este ");
            }
            if(currentRoom.southExit != null) {
                System.out.print("sur ");
            }
            if(currentRoom.westExit != null) {
                System.out.print("oeste ");
            }
            System.out.println();
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
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
}
