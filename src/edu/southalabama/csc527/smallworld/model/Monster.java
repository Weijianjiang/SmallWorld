package edu.southalabama.csc527.smallworld.model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class Monster extends Fighter {

	/**
	 * Constructs a new Monster instance. Only to be invoked by the {@link World} class.
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
	 * @param guarding
	 *            the set of directions guarded by monster.   
	 * @param beatMonPointsAward
	 *            the points awarded when the Player beat the monster.        
	 */
	public Monster(World world, String name, String article, String description, 
			       int beatMonPointsAward) {
		super(world, name, article, description);
		setLocation(world.getNowherePlace());
		f_stats = null;
		f_beatMonPt.setPoints(beatMonPointsAward);	
	}
	
	/**
	 *  the points awarded when the Player beat the Monster. 
	 */
	private final PointAward f_beatMonPt = new PointAward(0);
	
	/**
	 *  Gets the points awarded when the Player beat the Monster. 
	 */
	public PointAward getBeatMonPointsAward() {
		return f_beatMonPt;
	}
	
	/**
	 * Returns the non-null location of this Monster.
	 * 
	 * @return the location of this Monster
	 */
	public Place getLocation() {
		return (Place) super.getLocation();
	}
	
	/**
	 * Sets the location of the parameter location to be located 
	 * in this instance of Monster
	 * 
	 * @param t
	 * 		where this Thing should now be located at
	 */
	public void setLocation(Thing location) {
		assert(location instanceof Place);
		super.setLocation(location);
	}

	/**
	 * The monster state, initialized to 0, which stores the value of the states
	 * accumulated by the monster during the fight.
	 */
	private Stats f_stats = new Stats(0, 0, 0, 0, 0, 0, "", 0, 0);
	
	/**
	 * Returns the state of this Monster.
	 * 
	 * @return the state of this Monster.
	 */
	public Stats getMonStats() {
		return f_stats;
	}
	
	/**
	 * The directions guarded by Monster.
	 */
	Set<Direction> f_guarding = new HashSet<Direction>();
	
	/**
	 * Gets the directions guarded by Monster.
	 * 
	 * @return the directions guarded by Monster.
	 */
	public Set<Direction> getGuarding(){
		return new HashSet<Direction>(f_guarding);
	}
	
	public void setGuarding(Direction d){
		f_guarding.add(d);
	}
	
	/**
	 * Creates a random behavior for the monsters.
	 * @param play the monster's opponent
	 */
	public void takeAction(Player play){
		
		int command = (int)(Math.random() * 6);
		if(command == 0){
			play.getWorld().addToMessage(this.getName() + " is staring off into space...");
		}
		else if(command != 0 && command < 5){
			Random r = new Random();
			float chance = r.nextFloat();
			if(chance <= ((float)(this.getStats().getHit())/100)){
				System.out.println(chance);
				int damage = (this.getStats().getStr() * (int)((Math.random()+1)*5));
				int newHP = play.getStats().getHP();
				newHP -= damage;
				play.getStats().setHP(newHP);
				this.getWorld().addToMessage("The " + this.getName() + " attacks! You take "
						+ damage + " points of damage!\nPlayer HP: " + play.getStats().getHP() +
						"\n" + this.getName() + " HP:  " + this.getStats().getHP());
			}
			else{
				this.getWorld().addToMessage("You barely dodge the " + this.getName()
						+ "'s attack!");
			}
		}
		else{
			Random r = new Random();
			float chance = r.nextFloat();
			if(chance <= ((float)(this.getStats().getHit()-15)/100)){
				System.out.println(chance);
				int damage = (this.getStats().getStr() * (int)((Math.random()+1)*7));
				int newHP = play.getStats().getHP();
				newHP -= damage;
				play.getStats().setHP(newHP);
				this.getWorld().addToMessage("The " + this.getName() + " hits you hard!! You take " + damage
						+ " points of damage!!\nPlayer HP: " + play.getStats().getHP() +
						"\n" + this.getName() + " HP:  " + this.getStats().getHP());
			}
			else{
				this.getWorld().addToMessage("The " + this.getName()
						+ " tried a special attack, but failed.\nPlayer HP: " + play.getStats().getHP() +
							"\n" + this.getName() + " HP:  " + this.getStats().getHP());
			}
		}
		if(play.getStats().getHP() <= 0)
		{
			play.getWorld().addToMessage("You were killed by the " + this.getName() 
					+ "\nSo you died!" + "\nGame over!");
			play.getWorld().setGameOver();
			play.getWorld().turnOver();
		}
	}
	
	/**
	 * If the monster dies, this will normalize all of its values and return
	 * the point value for beating it.
	 * @return the points gained for beating the monster.
	 */
	public PointAward defeat(){
		boolean dead = this.stillAlive();
		if(dead){
			System.out.println("Necromancy?!");
		}
		while(!f_guarding.isEmpty()){
			if(f_guarding.contains(Direction.NORTH)){
				f_guarding.remove(Direction.NORTH);
			}
			else if(f_guarding.contains(Direction.SOUTH)){
				f_guarding.remove(Direction.SOUTH);
			}
			else if(f_guarding.contains(Direction.WEST)){
				f_guarding.remove(Direction.WEST);
			}
			else{
				f_guarding.remove(Direction.EAST);
			}
		}
		return this.getBeatMonPointsAward();
	}
	
}
