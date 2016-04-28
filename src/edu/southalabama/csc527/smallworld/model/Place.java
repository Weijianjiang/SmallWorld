package edu.southalabama.csc527.smallworld.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a place, such as a room or corridor, that a character in the game
 * can travel through. Each instance is also contained, or exists within, a
 * single {@link World} instance.
 * 
 */
public class Place extends Thing {

	/**
	 * Constructs a new instance. Only to be invoked by the {@link World} class.
	 * 
	 * @param world
	 *            the {@link World} instance this exists within.
	 * @param name
	 *            a non-null unique name for the instance. The uniqueness of the
	 *            name can't be dependent upon case, e.g., "Hall" is considered
	 *            the same as "hall".
	 * @param article
	 *            the appropriate non-null indefinite article with which to
	 *            prefix the name so as to form a proper short description,
	 *            e.g., "the" or "a".
	 * @param description
	 *            a long, possibly mult-line, non-null description of this
	 *            thing.
	 */
	Place(World world, String name, String article, String description) {
		super(world,name,article,description);
		setLocation(null);
	}
	
	/**
	 * A field that indicates whether arrival of the Player at this Place wins the game
	 */
	private boolean f_arrivalWinsGame;

	/**
	 * Gets whether or not arrival at this Place wins the game
	 * 
	 * @return true or false, depending on whether this Place is a winning location
	 */
	public final boolean arrivalWinsGame() {
		return f_arrivalWinsGame;
	}
	
	/**
	 * Sets whether or not arrival at this Place wins the game
	 * 
	 * @param value 
	 * 		true if this Place is a winning location, false otherwise 
	 */
	public final void setArrivalWinsGame(boolean value) {
		f_arrivalWinsGame = value;
	}

	/**
	 * A mapping of directions of travel to the neighboring places. The map is
	 * ragged in the sense that if a direction is not legal to travel in a
	 * <code>null</code> will be returned. For example,
	 * <code>null == f_directionOfTravelToplace.get(Direction.NORTH)</code>
	 * will be <code>true</code> if no place exists to the north of this one.
	 */
	private final Map<Direction, Place> f_directionOfTravelToPlace = new HashMap<Direction, Place>();

	private final Map<Monster, Direction> f_DirectionGuardingByMon = new HashMap<Monster, Direction>();
	
	/**
	 * Returns <code>true</code> if travel is allowed in the specified
	 * direction from this place, <code>false</code> otherwise.
	 * 
	 * @param d
	 *            the non-null direction of travel.
	 * @return <code>true</code> if travel is allowed in the specified
	 *         direction from this place, <code>false</code> otherwise.
	 */
	public boolean isTravelAllowedToward(Direction d) {
		assert (d != null);
		Monster mon = this.getMonster();
		if(mon == null){
			return f_directionOfTravelToPlace.containsKey(d);
		}
		Set<Direction> directionsGuarded = mon.getGuarding();
		if(!directionsGuarded.contains(d)) {
			return f_directionOfTravelToPlace.containsKey(d);
		}
		else {
			return false;
		}
	}

	/**
	 * Gets the destination of travel in the specified direction.
	 * 
	 * @param d
	 *            the non-null direction of travel.
	 * @return the destination of travel in the specified direction, or
	 *         <code>null</code> if travel is not allowed in that direction.
	 * @see #isTravelAllowedToward(Direction)
	 */
	public Place getTravelDestinationToward(Direction d) {
		assert (d != null);
		return f_directionOfTravelToPlace.get(d);
	}

	/**
	 * Sets the destination of travel in the specified direction for this place.
	 * 
	 * @param d
	 *            the non-null direction of travel.
	 * @param l
	 *            the non-null destination of travel in the specified direction.
	 */
	public void setTravelDestination(Direction d, Place l) {
		assert (d != null);
		assert (l != null);
		f_directionOfTravelToPlace.put(d, l);
	}
	
	/**
	 * Returns items located in this Place
	 * 
	 * @return set of Items that are located in this Place
	 */
	public Set<Item> getInventory() {
		Set<Item> items = new HashSet<Item>();
		for (Thing t: getThingsHere()) {
			if (t instanceof Item) {
				items.add((Item) t);
			}
		}
		return items;
	}
	
	/**
	 * Returns monsters located in this Place
	 * 
	 * @return set of Monsters that are located in this Place
	 */
	public Monster getMonster() {
		for (Thing t: getThingsHere()) {
			if (t instanceof Monster) {
				return (Monster)t;
			}
		}
		return null;
	}
	
	/**
	 * A mapping of Items to blocked message strings that represents the
	 * items required for entry into this Place. The key set of the map
	 * represents the set of Items that are required for entry, and each 
	 * Item has an associated blocked message.
	 * 
	 */
	private final Map<Item, String> f_neededToEnter = new HashMap<Item,String>();
	
	/**
	 * Gets the destination of travel in the specified direction.
	 * 
	 * @param i
	 *         the {@link Item} object which has a requirements for entry 
	 *         to this Place object
	 * @return String for the blocked message that is displayed to the player
	 *         when they are not carrying the Item specified or 
	 *         <code>null</code> if the Item is not required for entry
	 * @see #getItemsNeededForEntry()
	 */
	public String getBlockedMessageForItem(Item i) {
		if (f_neededToEnter.containsKey(i)) {
			return f_neededToEnter.get(i);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Sets the associates the specified block message with the specified items 
	 * needed to enter the specify place. 
	 * 
	 * @param i
	 *         the {@link Item} object which has a requirements for entry 
	 *         to this Place object
	 * @param blockedMessage
	 *         the string message which map to the specify item needed to entry.
	 */
	public void setItemNeededForEntry(Item i, String blockedMessage) {
		assert(i != null);
		assert(blockedMessage != null);
		f_neededToEnter.put(i, blockedMessage);
	}

	/**
	 * Returns items required to enter this Place: the Items must be in the Player
	 * inventory set of items
	 * 
	 * @return set of Items that must be in the player inventory to all travel to this Place
	 */
	public Set<Item> getItemsNeededForEntry() {
		return new HashSet<Item>(f_neededToEnter.keySet());
	}
	
	
	/**
	 * Returns the non-null location of this Place, which is itself.
	 * 
	 * @return the location of this Place
	 */
	public Place getLocation() {
		return this;
	}
	
	/**
	 *  Sets the non-null location of this Place, which is itself.
	 *  
	 *  @param t
	 *  	   the {@link Thing} object which is instance of this place.
	 */
	public void setLocation(Thing t) {
		super.setLocation(this);
	}
	
	
}
