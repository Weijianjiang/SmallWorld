package edu.southalabama.csc527.smallworld.textui.parser;

import java.util.HashSet;
import java.util.Set;

import edu.southalabama.csc527.smallworld.controller.WorldController;
import edu.southalabama.csc527.smallworld.model.Direction;
import edu.southalabama.csc527.smallworld.model.Fight;
import edu.southalabama.csc527.smallworld.model.Fighter;
import edu.southalabama.csc527.smallworld.model.Item;
import edu.southalabama.csc527.smallworld.model.Monster;
import static edu.southalabama.csc527.smallworld.textui.TextUtilities.*;

/**
 * Parses user commands and informs a specified {@link WorldController} instance
 * of its results. For example, the String "go north" causes the
 * {@link WorldController#travel(Direction)} method to be invoked.
 * 
 */
public final class UserCommandParser {

    /**
     * The non-null world controller associated with this parser.
     */
    private final WorldController f_wc;

    /**
     * The fight object used during fighting mode.
     */
    private Fight f_fight;
    
    /**
     * An observer of this parser, recipient of certain UI-only
     * commands and error messages.
     */
    private IParserObserver f_pwo;

    /**
     * Constructs a text command parser with the specified observer and
     * WorldController.  The controller is sent commands that affect the
     * World (or could), while the observer is sent error messages and
     * output from commands that only query the world, in particular
     * those expected to be unique to a text-based interface.
     * 
     * @param controller a non-null controller for this parser to invoke once it
     *            understands the user's game command.
     * @param observer a non-null observer for this parser to send messages and
     *            query command output to
     */
    public UserCommandParser(WorldController controller,
                             IParserObserver observer) {
        assert (controller != null && observer != null);

        f_wc = controller;
        f_pwo = observer;
        f_fight = null;
    }

    /**
     * Parses the user's command and sending appropriate messages to the
     * {@link WorldController}. Once the users command is understood (i.e.,
     * parsed) a specific method is invoked on the controller. If the parser
     * couldn't understand the user's command an error message is sent.
     * 
     * @param command the users's command to the game.
     */
    public void parse(String command) {
        String[] words = command.trim().toUpperCase().split("\\s+");

        /*
         * The below flag is used to indicate if we were able to understand the
         * command and callback our observer (f_observer) to execute a command
         * in the game.
         */
        boolean commandExecuted = false;
        
        Direction d = Direction.getInstance(words[0]);
        if (d != null) { 
        	/*
             * "N"
             * "S
             * "E"
             * "W"
             * "North"
             * "South"
             * "East"
             * "West"
             */
        	
        	f_wc.travel(d);
        	commandExecuted = true;
        	
        }
        else if (words[0].equals("GO") || words[0].equals("MOVE")) {
            /*
             * "GO <direction>" command
             */
        	if(f_wc.getWorld().getPlayer().getFighting()){
        		f_pwo.display("Not now! You're in combat!");
        		commandExecuted = true;
        	}
        	else{
        		Direction direction = null;
                if (words.length == 1) {
                    f_pwo.display(
                            words[0] + " where?  You must specify a direction!"
                            + " Please type \"help\" if you need more help.");
                } else {
                    direction = Direction.getInstance(words[1]);
                    if (direction == null) {
                        f_pwo.display(
                            words[1] + " is not a direction I recognize. "
                            + "Please type \"help\" if you need more help.");
                    } else {
                        f_wc.travel(direction);
                    }
                }
                commandExecuted = true;
            }     
        }    
        else if (words[0].equals("HELP")) {
            /*
             * "HELP" command
             */
            f_pwo.display(getHelpMessage());
            commandExecuted = true;

        } 
        else if (words[0].equals("LOAD")) {
            /*
             * "LOAD <file>" command
             */
            String fileName = command.substring(words[0].length()).trim();
            if (fileName.length() == 0) {
                f_pwo.display(
                        "You must specify a file name to load a saved game.  "
                        + "Please type \"help\" if you need more help.");
            } else {
                f_wc.loadWorld(fileName);
            }
            commandExecuted = true;

        } 
        else if (words[0].equals("LOOK") || words[0].equals("")) {
            /*
             * "LOOK" command
             * This is executed locally and the result sent through
             * the Controller's World back to its observers.
             */
            f_pwo.look(f_wc.getWorld());
            commandExecuted = true;

        } 
        else if (words[0].equals("QUIT") || words[0].equals("EXIT")) {
            /*
             * "QUIT" or "EXIT" command
             */
            f_wc.quit();
            commandExecuted = true;

        } 
        else if (words[0].equals("SAVE")) {
            /*
             * "SAVE <file>" command
             */
            String fileName = command.substring(words[0].length()).trim();
            if (fileName.length() == 0) {
                f_pwo.display(
                        "You must specify a file name to save your game.  "
                        + "Please type \"help\" if you need more help.");
            } else {
                f_wc.saveWorld(fileName);
            }
            commandExecuted = true;
        }
        else if (words[0].equals("INVENTORY") || words[0].equals("INV") || words[0].equals("I")) {
            /*
             * "INVENTORY" command
             * This is executed by the Controller and messages 
             * sent to its observers
             */
        	if(f_wc.getWorld().getPlayer().getFighting()){
        		f_pwo.display("Not now! You're in combat!");
        		commandExecuted = true;
        	}
        	else{
        		
                f_wc.inventory();
                commandExecuted = true;
        	}

        }
        else if (words[0].equals("TAKE")) {
            /*
             * "TAKE" command
             * This is executed by the Controller
             */
            if(f_wc.getWorld().getPlayer().getLocation().getMonster() != null && f_wc.getWorld().getPlayer().getLocation().getMonster().stillAlive()){
            	f_pwo.display("The monster won't let you!");
            	commandExecuted = true;
            }
            else{
            	if (words.length == 1) {
                    f_pwo.display(
                            words[0] + " what?  You must specify something to take!"
                            + " Please type \"help\" if you need more help.");
                } else {
                	if (words[1].equals("ALL")) {
                		f_wc.takeAll();
                	}
                	else {
                		String itemString = words[1];
                		for (int i = 2; i < words.length; i++) {
                			itemString = itemString + " " + words[i];
                		}
                		Item item = f_wc.getWorld().getItem(itemString);
                		if (item == null) {
                           f_pwo.display("There is no item named \"" +  itemString + "\" in this world."
                            + " Please type \"help\" if you need more help.");            			
                		}
                		else {
                			f_wc.take(item);
                		}
                	}
                }
                commandExecuted = true;
            }
        }
        else if (words[0].equals("DROP")) {
            /*
             * "DROP" command
             * This is executed locally and the result sent through
             * the Controller's World back to its observers.
             */
        	if(f_wc.getWorld().getPlayer().getLocation().getMonster() != null && f_wc.getWorld().getPlayer().getLocation().getMonster().stillAlive()){
            	f_pwo.display("The monster won't let you!");
            	commandExecuted = true;
            }
        	else{
        		if (words.length == 1) {
                    f_pwo.display(
                            words[0] + " what?  You must specify something to drop!"
                            + " Please type \"help\" if you need more help.");
                } else {
                	if (words[1].equals("ALL")) {
                		f_wc.dropAll();
                	}
                	else {
             		String itemString = words[1];
             		for (int i = 2; i < words.length; i++) {
             			itemString = itemString + " " + words[i];
             		}
                		Item item = f_wc.getWorld().getItem(itemString);
                		if (item == null) {
                           f_pwo.display("There is no item named \"" +  words[1] + "\" in this world."
                            + " Please type \"help\" if you need more help.");            			
                		}
                		else {
                			f_wc.drop(item);
                		}
                	}
                }
                commandExecuted = true;

        	}
        	
        }
        else if (words[0].equals("SCORE")) {
            /*
             * "SCORE" command
             *  List the score of the player
             */
            f_wc.score();
            commandExecuted = true;

        }
        else if (words[0].equals("HEAL")){
        	/*
        	 * "HEAL" command
        	 *  Heals the player's HP if MP is available.
        	 */
        	f_wc.heal();
        	commandExecuted = true;
        }
        
        else if (words[0].equals("FIGHT")){
        	
        	/*
        	 * "FIGHT" command
        	 *  Activates fighting mode.
        	 */
        	Monster mon = f_wc.getWorld().getPlayer().getLocation().getMonster();
        	if(mon == null){
        		f_pwo.display("What are you fighting? There's nothing here.");
        		commandExecuted = true;
        	}
        	else{
        		f_fight = f_wc.fight(f_wc.getWorld().getPlayer(), mon);
        		if(f_fight == null){
        			f_pwo.display("It's dead Jim.");
        			commandExecuted = true;
        		}
        		else{
        			f_pwo.display("You draw your weapon and square off against " + mon.getArticle()
        					+ " " + mon.getName() + "!\nA fight begins!!");
            		commandExecuted = true;
        		}
        		
        	}
        }
        
        else if (words[0].equals("ATTACK") && f_wc.getWorld().getPlayer().getFighting() && f_fight != null){
        	
        	/*
        	 * "ATTACK" command
        	 *  Causes the player to attack.
        	 */
        	f_wc.attack(f_fight);
        	commandExecuted = true;
        }
        
        else if (words[0].equals("HEAVY") && f_wc.getWorld().getPlayer().getFighting() && f_fight != null){
        	
        	/*
        	 * "HEAVY HIT" command
        	 *  Causes the player to execute a higher risk attack.
        	 */
        	f_wc.heavyHit(f_fight);
        	commandExecuted = true;
        }
        
        else if (words[0].equals("RUN") && f_wc.getWorld().getPlayer().getFighting() && f_fight != null){
        	
        	/*
        	 * "RUN" command
        	 *  Causes the player to retreat in the direction the monster is not guarding.
        	 */
        	Direction runAway = f_wc.run(f_fight);
        	f_pwo.display("You escaped the monster!");
        	f_wc.travel(runAway);
        	commandExecuted = true;
        }
        
        else if(words[0].equals("CHECK")){
        	/*
        	 * "CHECK" command
        	 * Displays player's current HP and stats.
        	 */
        	f_pwo.display("---Player's Current Condition---\n\nPlayer's HP: " + f_wc.getWorld().getPlayer().getStats().getHP()
        			+ " \\ " + f_wc.getWorld().getPlayer().getStats().getMaxHP() + "\nPlayer's MP: "
        			+ f_wc.getWorld().getPlayer().getStats().getMP() + " \\ " 
        			+ f_wc.getWorld().getPlayer().getStats().getMaxMP());
        	commandExecuted = true;
        }
        
        else if(words[0].equals("PASCAL")){
        	if(f_wc.getWorld().getPlayer().getInventory().contains(f_wc.getWorld().getThingByName("Pascal's Collar")) 
        		&& f_wc.getWorld().getPlayer().getLocation() == f_wc.getWorld().getPlaceByName("Small Shrine")){
        		f_pwo.display("> PASCAL THE DOG IS HERE.");
        	}
        	else{
        		f_pwo.display("???");
        	}
        	commandExecuted = true;
        }

        if (!commandExecuted) {
            /*
             * We couldn't understand the command the user entered. Hence we
             * need report this problem.
             */
            f_pwo.display("Sorry, I don't understand what the command \""
                            + command
                            + "\" means. Please type \"help\" if you need some help.");
        }
    }

    /**
     * Constructs a message containing some (hopefully useful) help
     * to the user about what the game commands do.
     */
    private String getHelpMessage() {
        StringBuilder helpMessage = new StringBuilder();
        helpMessage
                .append("You are controlling a player "
                        + "interacting with a small world created within the computer.  "
                        + "The commands you type control what the player does within "
                        + "the game.  Some actions add points to your score.  You win the "
                        + "game when you obtain enough points through your actions.");
        helpMessage.append(LINESEP2);
        helpMessage
                .append("The following is a description of the commands the "
                        + "game understands:");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"go <north | south | east | west>\" moves the "
                + "player from his or her current location in the specified "
                + "direction to a new location. The word \"move\" may be used "
                + "to mean \"go\" within this command. "
                + "An example is \"go north\" or \"go n\"");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"<north | south | east | west>\" are shortcut "
                + "commands for movement, including their abbreviations: "
                + " <n | s | e | w> ");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"take <item>\": if the item is in the same place "
                + "will place the item in the player's inventory");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"take all\": takes all items at the "
                        + "current location and places them in the player's inventory ");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"drop <item>\": if the item is in the player's inventory "
                + "will drop the item in the player's current location");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"drop all\": drops all items in the "
                + "player's inventory");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"inventory\" \"inv\" \"i\": lists the player's inventory");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"load <filename>\" loads the specified save file "
                + "into the game.  Discards the existing game state."
                + "  Example \"load C:\\save1.xml\"");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"look\" examines the player's current location.");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"check\" returns player's current condition.");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"fight\" enters fighting mode. While in fighting mode"
        		+ " certain functions are disabled.");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"attack\" causes the player to execute a standard attack."
        		+ " Only available during fighting mode.");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"heavy hit\" causes the player to execute a special attack."
        		+ " Only available during fighting mode.");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"run\" causes the player to retreat from combat in the direction they entered."
        		+ " Only available during fighting mode.");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"score\" displays the player's current score.");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"heal\" heals the player's health if MP is sufficient. The healing spell costs five magic points.");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"quit\" or \"exit\" terminates the game.");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"pascal\" ???");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"save <filename>\" saves the current game state "
                + "to the specified filename. Example \"save C:\\save1.xml\"");
        return helpMessage.toString();
    }
}
