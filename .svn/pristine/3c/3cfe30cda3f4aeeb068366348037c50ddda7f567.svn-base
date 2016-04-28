package edu.southalabama.csc527.smallworld.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Fighter extends Thing {
	
	/**
	 * A boolean that determines if the Fighter is in combat.
	 */
	private boolean f_isFighting;
	
	/**
	 * A boolean that determines if the Fighter is alive.
	 */
	private boolean f_isAlive;
	
	/**
	 * This represents the stats of the particular Fighter.
	 */
	private Stats f_stats;
	
	/**
	 * Constructs a new instance.
	 * 
	 * @param world
	 * 		the world the Fighter inhabits
	 * 
	 * @param name
	 * 		the name of the Fighter
	 * 
	 * @param article
	 * 		the article associated with the Fighter
	 * 
	 * @param description
	 * 		a description of the Fighter
	 * 
	 * @param stats
	 * 		a stats object indicating the Fighter's abilities
	 */
	
	public Fighter(World world, String name, String article, String description) {
		super(world, name, article, description);
		f_isFighting = false;
		f_isAlive = true;
	}
	
	/**
	 * Causes the Fighter to enter fighting state.
	 */
	public void beginsFighting(){
		f_isFighting = true;
	}
	
	/**
	 * Sets the stats for the fighter.
	 * 
	 * @param stats
	 * 		the incoming stats object associated with this Fighter
	 */
	public void setStats(Stats stats){
		f_stats = stats;
	}
	
	/**
	 * Causes the Fighter to exit fighting state.
	 */
	public void endsFighting(){
		f_isFighting = false;
	}
	
	/**
	 * Gets the stats of the Fighter in question.
	 * 
	 * @return the stats object of the fighter
	 */
	public Stats getStats(){
		return f_stats;
	}

	/**
	 * Indicates if the fighter is alive or dead.
	 * 
	 * @return returns if character is alive
	 */
	public boolean stillAlive(){
		if(this.getStats().getHP() <= 0){
			f_isAlive = false;
		}
		return f_isAlive;
	}
	
	/**
	 * Returns the whether the Fighter is currently in combat.
	 * @return is fighting
	 */
	public boolean getFighting(){
		return f_isFighting;
	}
	
}
