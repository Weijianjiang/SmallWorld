package edu.southalabama.csc527.smallworld.model;

/**
 * A container for point values awarded to the player
 */
public class PointAward {
	
	/**
	 * Constructor for PointAward 
	 * 
	 * @param value
	 * 			the number of points in this award
	 */
	public PointAward(int value) {
		f_points = value;
	}

	/**
	 * Amount of points in this award
	 */
	private int f_points;
	
	/**
	 * Gets the number points in this point award
	 * 
	 * @return number of points
	 */
	public int getPoints() {
		return f_points;
	}
	
	/**
	 * Set the number points in this point award
	 * 
	 * @param value
	 * 			the number of points in this award
	 */
	public void setPoints(int value) {
		f_points = value;
	}
	
	public String toString() {
		return "" + f_points;
	}

}
