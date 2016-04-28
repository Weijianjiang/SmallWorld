package edu.southalabama.csc527.smallworld.model;

import java.util.HashMap;
import java.util.Map;

public class Item extends Thing {

	/**
	 * Constructs a new Item instance. Only to be invoked by the {@link World} class.
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
	 * @param takePointAward
	 *            the points awarded when the Player takes this Item     
	 * @param dropPointAward
	 *            the points awarded when the Player drops this Item        
	 */
	public Item(World world, String name, String article, String description, int takePointAward, int dropPointAward) {
		super(world,name,article,description);
		f_takeAnywhere.setPoints(takePointAward);
		f_dropAnywhere.setPoints(dropPointAward);
	}
	
	private final PointAward f_dropAnywhere = new PointAward(0);
	
	/**
	 * Gets the PointAward for this Item when it is dropped by the player anywhere
	 * 
	 * @return PointAward for drop 
	 */
	public PointAward getDropPointsAward() {
		return f_dropAnywhere;
	}


	private Map<Place,PointAward> f_drop = new HashMap<Place,PointAward>();		
	
	public void setLocationDropPoints(Place p, int value) {
		assert(p != null);
		f_drop.put(p, new PointAward(value));
	}	
	
	public PointAward getLocationDropPoints(Place p) {
		if (f_drop.containsKey(p)) {
			return f_drop.get(p);
		}
		else {
			return null;
		}
	}
	
	private final PointAward f_takeAnywhere = new PointAward(0);;
	
	public PointAward getTakePointsAward() {
		return f_takeAnywhere;
	}
	
}