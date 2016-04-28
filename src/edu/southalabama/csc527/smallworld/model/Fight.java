package edu.southalabama.csc527.smallworld.model;

import java.util.Random;

public class Fight {
	
	/**
	 * Constructs a new association instance between {@link Player} and {@link Monster}. 
	 * 
	 * @param p
	 *            the {@link Player} instance this exists within.
	 * @param m
	 *            the {@link Monster} instance this exists within.   
	 */
	public Fight(Player p, Monster m) {
		f_player = p;
		f_monster = m;
	}
	
	/**
	 * the {@link Player} object who is invoked in Fight.
	 */
	private Player f_player;
	
	/**
	 * the {@link Monster} object that is invoked in Fight.
	 */
	private Monster f_monster;
	
	/**
	 * the important transition during the fighting.
	 * 
	 * @return <code>true</code> if the monster and player are both alive , 
	 * 		   <code>false</code> otherwise.
	 */
	public boolean fightTransition() {
		if(f_player.getFighting()){
			f_player.endsFighting();
			f_monster.endsFighting();
			System.out.println("here.");
			return true;
		}
		else{
			if((f_player.stillAlive() == true) && (f_monster.stillAlive() == true))
			{
				f_player.beginsFighting();
				f_monster.beginsFighting();
				return true;
			}
			else if((f_player.stillAlive() == true) && (f_monster.stillAlive() == false)){
				f_player.endsFighting();
				f_monster.endsFighting();
				return false;
			}
			else if((f_player.stillAlive() == false) && (f_monster.stillAlive() == true)){
				f_player.getWorld().setGameOver();
				return false;
			}
			else{
				return false;
			}
		}
	}
	
	/**
	 * the attack action between {@link Monster} and {@link Player}.
	 */
	public void attack(){
		Random r = new Random();
		float chance = r.nextFloat();
		if(chance <= ((float)(f_player.getStats().getHit())/100)){
			int damage = (f_player.getStats().getStr() * ((int)(Math.random()+1)*5));
			int newHP = f_monster.getStats().getHP();
			newHP -= damage;
			f_monster.getStats().setHP(newHP);
			f_player.getWorld().addToMessage("You strike the monster! It takes " + damage
					+ " points of damage!\nPlayer HP: " + f_player.getStats().getHP() +
					"\n" + f_monster.getName() + " HP:  " + f_monster.getStats().getHP());
		}
		else{
			f_player.getWorld().addToMessage("You missed!");
		}
		
		if(!(f_monster.stillAlive())){
			PointAward exp = f_monster.defeat();
			this.fightTransition();
			f_player.addToScore(exp.getPoints());
			f_player.getWorld().addToMessage("You defeated the " + f_monster.getName() 
					+ "!\nYou received " + exp.getPoints() + " experience points.");
		} else if(!(f_player.stillAlive())) {
			f_player.getWorld().addToMessage("You were killed by the " + f_monster.getName() 
					+ "\nSo you died!" + "\nGame over!");
			f_player.getWorld().setGameOver();
			f_player.getWorld().turnOver();
			return;
		} else{
			f_monster.takeAction(f_player);
		}
	}
	
	/**
	 * the special attack action between {@link Monster} and {@link Player}.
	 */
	public void heavyHit(){
		Random r = new Random();
		float chance = r.nextFloat();
		if(chance <= ((float)(f_player.getStats().getHit()-15)/100)){
			System.out.println(chance);
			int damage = (f_player.getStats().getStr() * ((int)(Math.random()+1)*10));
			int newHP = f_monster.getStats().getHP();
			newHP -= damage;
			f_monster.getStats().setHP(newHP);
			f_player.getWorld().addToMessage("You land a critical hit on the monster!! It takes " + damage
					+ " points of damage!\nPlayer HP: " + f_player.getStats().getHP() +
					"\n" + f_monster.getName() + " HP:  " + f_monster.getStats().getHP());
		}
		else{
			f_player.getWorld().addToMessage("You missed!");
		}
		
		if(!(f_monster.stillAlive())){
			PointAward exp = f_monster.defeat();
			this.fightTransition();
			f_player.addToScore(exp.getPoints());
			f_player.getWorld().addToMessage("You defeated the " + f_monster.getName() 
					+ "!\nYou received " + exp.getPoints() + " experience points.");
		} else if(!(f_player.stillAlive())) {
			f_player.getWorld().addToMessage("You were killed by the " + f_monster.getName() 
					+ "\nSo you died!" + "\nGame over!");
			f_player.getWorld().setGameOver();	
		} else{
			f_monster.takeAction(f_player);
		}
	}
}
