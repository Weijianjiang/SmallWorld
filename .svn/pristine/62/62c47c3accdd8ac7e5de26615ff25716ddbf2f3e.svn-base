package edu.southalabama.csc527.smallworld.model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Boss extends Monster{

	public Boss(World world, String name, String article, String description,
			 int beatMonPointsAward) {
		super(world, name, article, description, beatMonPointsAward);
		
	}
	
	/**
	 * Creates a special set of random behaviors for bosses.
	 * @param play the monster's opponent
	 */
	public void takeAction(Player play){
		
		if(this.getStats().getHP() < 75 && this.getStats().getMP() > 5){
		
			int healCommand = (int)(Math.random() * 3);
			if(healCommand > 1){
				int healing = (this.getStats().getInt() * ((int)(Math.random()+ 1)* 10));
				this.getStats().setHP(this.getStats().getHP() + healing);
				this.getStats().setMP(this.getStats().getMP() - 5);
				if(this.getStats().getHP() > this.getStats().getMaxHP()){
					this.getStats().setHP(this.getStats().getMaxHP());
					this.getWorld().addToMessage(this.getName() + " is fully rejuvenated!!");
				}
				else{
					this.getWorld().addToMessage(this.getName() + " healed " + healing + " points of HP!!");
				}
			}
		}
		else{
			int command = (int)(Math.random() * 10);
			if(command == 0){
				play.getWorld().addToMessage(this.getName() + " is reeling. Is this your chance?!");
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
					this.getWorld().addToMessage(this.getName() + " attacks! You take "
							+ damage + " points of damage!\nPlayer HP: " + play.getStats().getHP() +
							"\n" + this.getName() + " HP:  " + this.getStats().getHP());
				}
				else{
					this.getWorld().addToMessage("You barely dodge " + this.getName()
							+ "'s attack!");
				}
			}
			else if(command >= 5 && command < 9){
				Random r = new Random();
				float chance = r.nextFloat();
				if(chance <= ((float)(this.getStats().getHit()-15)/100)){
					System.out.println(chance);
					int damage = (this.getStats().getStr() * (int)((Math.random()+1)*7));
					int newHP = play.getStats().getHP();
					newHP -= damage;
					play.getStats().setHP(newHP);
					this.getWorld().addToMessage(this.getName() + " hits you hard!! You take " + damage
							+ " points of damage!!\nPlayer HP: " + play.getStats().getHP() +
							"\n" + this.getName() + " HP:  " + this.getStats().getHP());
				}
				else{
					this.getWorld().addToMessage(this.getName()
							+ " tried a special attack, but failed.\nPlayer HP: " + play.getStats().getHP() +
								"\n" + this.getName() + " HP:  " + this.getStats().getHP());
				}
			}
			else{
				int damage = (this.getStats().getInt() * (int)((Math.random()+1)*10));
				this.getStats().setMP(this.getStats().getMP() - 5);
				int newHP = play.getStats().getHP();
				newHP -= damage;
				play.getStats().setHP(newHP);
				this.getWorld().addToMessage("\"Vengeful swarm, tear my enemy to shreds!! Plague of Locusts!!\""
						+ "\nAs Lord Abaddon yells his command, from every corner of the room fly countless black"
						+ " insects!! The swarm tears into you!! You take " + damage + " damage!!\nPlayer HP: " + play.getStats().getHP() +
						"\n" + this.getName() + " HP:  " + this.getStats().getHP());
				
			}
		}
	}
	
	/**
	 * Returns items in the Boss's inventory
	 * 
	 * @return set of Items that are located in the Boss
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
		Set<Item> spoils = this.getInventory();
		for(Item i : spoils){
			i.setLocation(this.getWorld().getPlayer());
			this.getWorld().addToMessage("You received a " + i.getName() + ".");
		}
		return this.getBeatMonPointsAward();
	}
}
