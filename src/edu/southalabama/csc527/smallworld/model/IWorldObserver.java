package edu.southalabama.csc527.smallworld.model;

/**
 * A class can implement this interface when it wants to be informed of changes
 * in {@link World} objects.
 * 
 */
public interface IWorldObserver {

	/**
	 * This method is called whenever the observed {@link World} object is
	 * changed. A reference to the changed world is passed as a parameter
	 * 
	 * @param world
	 *            the changed world object.
	 */
	void update(World world);
}
