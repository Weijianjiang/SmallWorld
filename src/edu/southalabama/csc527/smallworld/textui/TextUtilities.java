package edu.southalabama.csc527.smallworld.textui;

import edu.southalabama.csc527.smallworld.model.Direction;
import edu.southalabama.csc527.smallworld.model.Item;
import edu.southalabama.csc527.smallworld.model.Monster;
import edu.southalabama.csc527.smallworld.model.Place;

/**
 * A convenience class of handy constants and methods for working with text.
 * 
 */
public class TextUtilities {

	/**
	 * Disable default constructor
	 */
	private TextUtilities() {
	}

	/**
	 * The operating system-specific line separation character.
	 */
	public static final String LINESEP = System.getProperty("line.separator");

	/**
	 * Convenience constant for adding two line separators at once.
	 */
	public static final String LINESEP2 = LINESEP + LINESEP;

	/**
	 * Gets several lines of directions about the possible travel destinations
	 * from a place. These are in the form "To the north you see the Hall."
	 * 
	 * @param place
	 *            the place of origin
	 * @return directions and travel destinations from this place.
	 */
	public static String getDirectionsFrom(Place place) {
		StringBuilder result = new StringBuilder();
		for (Direction possibleDirection : Direction.values()) {
			if (place.isTravelAllowedToward(possibleDirection)) {
				result.append("To the "
						+ possibleDirection.toString().toLowerCase()
						+ " you see "
						+ place.getTravelDestinationToward(possibleDirection)
								.getShortDescription() + ". ");
			}
		}
		return result.toString().trim();
	}
	
	/**
	 * Gets several lines of text regarding the items at a location. 
	 * These are in the form "On the floor you see ..."
	 * 
	 * @param place
	 *            the place of location
	 * @return item names
	 */
	public static String getItems(Place place) {
		StringBuilder result = new StringBuilder();
		if (place.getInventory().isEmpty()) {
			result.append("You do not see anything on the floor");
		}
		else {
			result.append("On the floor you notice the following items:" + LINESEP);
			for (Item i: place.getInventory()) {
				result.append("   " + i.getName() + LINESEP);
			}
		}
		return result.toString().trim();
	}
	
	/**
	 * Gets several lines of text regarding the items at a location. 
	 * These are in the form "On the floor you see ..."
	 * 
	 * @param place
	 *            the place of location
	 * @return item names
	 */
	public static String getMonster(Place place) {
		StringBuilder result = new StringBuilder();
		if (place.getMonster() == null) {
			result.append("The area seems clear.");
		}
		else {
			result.append("There's something here!!" + LINESEP);
			Monster m = place.getMonster();
			if(!m.stillAlive()){
				result.append("Oh wait, it's dead. Nevermind.");
			}
			else{
				result.append(m.getDescription() + "\nIt's " + m.getArticle() + " " + m.getName() + "!!\n"
						+ "\nWhat will you do?!");
			}
		}
		return result.toString().trim();
	}

	/**
	 * Gets a full description of the specified place, including possible
	 * destinations for travel from there.
	 * 
	 * @param place
	 *            the place to describe
	 */
	public static String getFullLocationDescription(Place place) {
		StringBuilder msg = new StringBuilder(place.getShortDescription());
		// Capitalize the first letter
		msg.replace(0, 1, msg.substring(0, 1).toUpperCase());
		msg.append(". ");
		msg.append(place.getDescription());
		msg.append(LINESEP2);
		msg.append(TextUtilities.getDirectionsFrom(place));
		msg.append(LINESEP2);
		msg.append(TextUtilities.getItems(place));
		msg.append(LINESEP2);
		msg.append(TextUtilities.getMonster(place));
		return msg.toString();
	}
}
