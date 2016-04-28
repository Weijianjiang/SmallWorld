package edu.southalabama.csc527.smallworld.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a thing, which is associated with other things, in a {@link World} instance.
 * A World must have 2 or more things: One Place and One Player
 * 
 */
public abstract class Thing {
	
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
	public Thing(World world, String name, String article, String description) {
		assert (world != null);
		assert (name != null);
		assert (article != null);
		assert (description != null);
		f_world = world;
		f_name = name;
		f_article = article;
		f_description = description;
	}
	
	/**
	 * The immutable reference to the {@link World} instance this is contained
	 * within.
	 */
	private final World f_world;

	/**
	 * Gets the world this exists in.
	 * 
	 * @return a reference to the {@link World} instance containing this.
	 */
	public final World getWorld() {
		return f_world;
	}
	
	/**
	 * The name of this thing, e.g., "Grande Hall". This identifier uniquely
	 * identifies this {@link Thing} instance within the game.
	 */
	private final String f_name;

	/**
	 * Gets the name of this.
	 * 
	 * @return the name of this.
	 */
	public final String getName() {
		return f_name;
	}

	/**
	 * The appropriate indefinite article with which to prefix the name so as to
	 * form a proper short description, e.g., "the" or "a".
	 */
	private final String f_article;

	/**
	 * Gets the article of this. This refers to the appropriate indefinite
	 * article with which to prefix the name so as to form a proper short
	 * description, e.g., "the" or "a".
	 * 
	 * @return the article of this.
	 */
	public final String getArticle() {
		return f_article;
	}
	
	/**
	 * The possibly null location of this Thing
	 */
	private Thing f_location;
	
	/**
	 * Returns the non-null location of this Thing.
	 * 
	 * @return the location of this Thing
	 */
	public Thing getLocation() {
		return f_location;
	}

	/**
	 * This method sets the location of the parameter t to be located 
	 * in this instance of Thing
	 * 
	 * @param t
	 * 		where this Thing should now be located at
	 */
	public void setLocation(Thing t) {
		/*
		 * Where this Thing instance WAS located, remove it from that Item's "thingsHere" collection
		 * It will be no longer part of that Things inventory, or set of Things
		 * 
		 */
		if (f_location != null) {
			f_location.f_thingsHere.remove(this);
		}
			
		/*
		 * Now, add this Thing to the "thingsHere" collection of the new location
		 */
		if (t != null) {
			t.f_thingsHere.add(this);
		}
		
		/*
		 * Finally, set the Location of this Thing to the new location
		 */
		f_location = t;
	}
	
	
	/**
	 * The set of non-null Things that are located at this location
	 */
	private Set<Thing> f_thingsHere = new HashSet<Thing>();
	
	
	public Set<Thing> getThingsHere() {
		return new HashSet<Thing>(f_thingsHere);
	}
	
	/**
	 * A long description of this thing.
	 */
	private final String f_description;

	/**
	 * Gets the long description of this.
	 * 
	 * @return the long description for this.
	 */
	public final String getDescription() {
		return f_description;
	}
	
	/**
	 * Gets the short description, e.g., "the Grande Hall", for this Thing.
	 * 
	 * @return the short description for this.
	 */
	public final String getShortDescription() {
		return f_article + " " + f_name;
	}



}
