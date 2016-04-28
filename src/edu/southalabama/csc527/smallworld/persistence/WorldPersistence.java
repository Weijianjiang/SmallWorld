package edu.southalabama.csc527.smallworld.persistence;

import java.io.*;
import java.net.URL;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import edu.southalabama.csc527.smallworld.model.*;

/**
 * The persistence capability for the game. This class defines static methods to
 * load and store the game state as an XML file. It uses the JDOM library to
 * manipulate XML trees.
 * <p>
 * Persistence is separate architecture layer from the game model. Specifically,
 * this class depends upon the model but the model <i>never</i> depends upon
 * this package.
 * 
 */
public class WorldPersistence {

	/**
	 * The version of the game as defined by the XML save file format.
	 */
	public static final String SAVEFILE_VERSION = "1.1";

	/**
	 * The full location, on the Java classpath, of the default world file.
	 */
	public static final String DEFAULT_WORLD = "/edu/southalabama/csc527/smallworld/persistence/DefaultWorld.xml";

	/**
	 * Loads the game state from the specified filename on the Java classpath of
	 * the running program and creates a usable World instance.
	 * 
	 * @param classpathLocation
	 *            the non-null string representing the full location, on the
	 *            Java classpath, of the desired world file.
	 * @return a game world.
	 */
	public static World loadWorld(String classpathLocation) {
		URL defaultURL = Object.class.getResource(classpathLocation);
		if (defaultURL == null) {
			throw new IllegalStateException(
					"Unable to find world file:  cannot locate \""
							+ classpathLocation + "\" in classpath");
		}
		try {
			InputStream in = defaultURL.openStream();
			return loadWorld(in);
		} catch (IOException e) {
			throw new IllegalStateException("Unable to open world file", e);
		}
	}

	/**
	 * Loads the game state from the specified {@link File} and creates a usable
	 * World instance. This is a convenience method that simply opens and
	 * {@link java.io.InputStream} on the specified {@link File} and calls
	 * {@link #loadWorld(InputStream)}.
	 * 
	 * @param file
	 *            the non-null file to read the game state from.
	 * @return a game world.
	 */
	public static World loadWorld(File file) {
		try {
			return loadWorld(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Unable to find world file", e);
		}
	}

	/**
	 * Loads the game state from the specified {@link java.io.InputStream} and
	 * creates a {@link World} usable World instance.
	 * 
	 * @param in
	 *            the non-null stream to read the game state from.
	 * @return a game world.
	 */
	public static World loadWorld(InputStream in) {
		assert (in != null);

		World world = new World();
		SAXBuilder parser = new SAXBuilder();
		try {
			Document saveXML = parser.build(in);
			Element root = saveXML.getRootElement();

			loadPlaceXML(root, world);

			loadPlayerXML(root, world);
			
			loadMinionXML(root, world);
			
			loadBossXML(root, world);
			
			loadItemXML(root,world);
			
			loadStatXML(root, world);

		} catch (IOException e) {
			throw new IllegalStateException(
					"A system error occurred while reading world file:", e);
		} catch (JDOMException e) {
			throw new IllegalStateException("Errors found in world file:", e);
		}
		return world;
	}

	/**
	 * Saves the state of the specified {@link World} into the specified
	 * {@link File} in XML format. It is suggested calls to this surround the
	 * call with a try-catch block if recovery from a save problem is desired.
	 * 
	 * @param world
	 *            the game state to save.
	 * @param file
	 *            the file to save the game state to.
	 * @throws IllegalStateException
	 *             if something goes wrong.
	 */
	public static void saveWorld(World world, File file) {
		Element worldElement = new Element(SMALLWORLD_TAG);

		worldElement.setAttribute(VERSION_TAG, SAVEFILE_VERSION);

		/*
		 * Create XML for Places
		 */
		for (Place l : world.getPlaces()) {
			/*
			 * We don't save the nowhere place to the save file. This place
			 * always exists in every world so its inclusion in the save file
			 * XML will cause an attempt on loading a save file into a model to
			 * try and create it again (resulting in an exception).
			 */
			if (l != world.getNowherePlace()) {
				worldElement.addContent(createPlaceXML(l));
			}
		}
		
		/*
		 * Create XML for the Items
		 */
		for (Item i : world.getItems()) {
			worldElement.addContent(createItemXML(i, world));
		}
		
		/*
		 * Create XML for the Stats
		 */
		for (Stats s : world.getStats()) {
			worldElement.addContent(createStatXML(s));
		}
		
		/*
		 * Create XML for the Minions
		 */
		for (Minion m : world.getMinions()) {
			worldElement.addContent(createMinionXML(m));
		}
		
		/*
		 * Create XML for the Bosses
		 */
		for (Boss b : world.getBosses()) {
			worldElement.addContent(createBossXML(b));
		}

		/*
		 * Create XML for the Player
		 */
		worldElement.addContent(createPlayerXML(world.getPlayer()));

		Document gameStateInformation = new Document(worldElement);

		try {
			OutputStream save = new BufferedOutputStream(new FileOutputStream(file));
			// XML outputter with two-space indentation and newlines after
			// elements
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			// actually output the XML tree to the save file
			outputter.output(gameStateInformation, save);
			save.close();
		} catch (IOException e) {
			// something went wrong
			throw new IllegalStateException("Unable to write world file", e);
		}
	}

	/**
	 * Creates an XML tree for a game place.
	 * 
	 * @param place
	 *            the game place.
	 * @return the constructed XML tree.
	 */
	private static Element createPlaceXML(Place place) {
		Element placeElement = new Element(PLACE_TAG);
		placeElement.setAttribute(NAME_TAG, place.getName());
		placeElement.setAttribute(ARTICLE_TAG, place.getArticle());
		if (place.arrivalWinsGame()) {
			placeElement.setAttribute(ARRIVAL_WINS_GAME_TAG, "Y");
		}
		Element description = new Element(DESCRIPTION_TAG);
		placeElement.addContent(description);
		description.setText(place.getDescription());

		for (Direction possibleDirection : Direction.values()) {
			if (place.isTravelAllowedToward(possibleDirection)) {
				Element neighbor = new Element(TRAVEL_TAG);
				placeElement.addContent(neighbor);
				neighbor.setAttribute(DIRECTION_TAG, possibleDirection
						.getAbbreviation());
				neighbor.setText(place.getTravelDestinationToward(
						possibleDirection).getName());
			}
		}

		return placeElement;
	}
	
	
	/**
	 * Creates an XML tree for a game item.
	 * 
	 * @param item
	 *            the game item.
	 * @return the constructed XML tree.
	 */
	private static Element createItemXML(Item item, World world) {
		Element itemElement = new Element(ITEM_TAG);
		itemElement.setAttribute(NAME_TAG, item.getName());
		itemElement.setAttribute(ARTICLE_TAG, item.getArticle());
		itemElement.setAttribute(LOCATION_TAG, item.getLocation().getName());
		itemElement.setAttribute(TAKEPOINTS_TAG, item.getTakePointsAward().toString());
		itemElement.setAttribute(DROPPOINTS_TAG, item.getDropPointsAward().toString());
		
		/*
		 * <item name="Coin" article="a" location="Hall" takePoints="20" dropPoints="1">
		 *    <place takePoints="0" dropPoints="100">Hall</place>
		 *    <place takePoints="0" dropPoints="200">Road</place> 
		 *    <place takePoints="0" dropPoints="-50" neededToEnter="Y" blockedMsg="Need money">Building</place> 
		 * </item> 
		 * <item name="Skunk" article="a" location="Hall" takePoints="-5" dropPoints="-10" /> 
		 * <item name="Lamp" article="the" location="Road" takePoints="10" dropPoints="10"> 
		 *    <place neededToEnter="Y" blockedMsg="Too dark">Hall</place> 
		 *    <place neededToEnter="Y" blockedMsg="Too dark">Building</place> 
		 * </item>
		 */
		  

		for (Place p: world.getPlaces()) {
			String placeName = p.getName();
			
			boolean hasNeededToEnter = false;
			boolean hasDropSpecificPoints = false;
			
			PointAward locationSpecificDropPointsForItem = item.getLocationDropPoints(p);
			
			if (locationSpecificDropPointsForItem != null) {
				hasDropSpecificPoints = true;
			}
			
			if (p.getItemsNeededForEntry().contains(item)) {
				hasNeededToEnter = true;
			}
			
			if (hasDropSpecificPoints || hasNeededToEnter) {
				Element placeElement = new Element(PLACE_TAG);
				itemElement.addContent(placeElement);
				placeElement.setText(placeName);
				if (hasDropSpecificPoints) {
					placeElement.setAttribute(TAKEPOINTS_TAG, "0");
					placeElement.setAttribute(DROPPOINTS_TAG, locationSpecificDropPointsForItem.toString());
				}
				if (hasNeededToEnter) {
					placeElement.setAttribute(NEEDEDTOENTER_TAG, "Y");
					placeElement.setAttribute(BLOCKEDMSG_TAG, p.getBlockedMessageForItem(item));
				}
			}
		}

		return itemElement;
	}

	/**
	 * Creates an XML tree for the player.
	 * 
	 * @param player
	 *            the player.
	 * @return the constructed XML tree.
	 */
	private static Element createPlayerXML(Player player) {
		Element playerElement = new Element(PLAYER_TAG);
		playerElement.setAttribute(LOCATION_TAG, ""
				+ player.getLocation().getName());
		playerElement.setAttribute(SCORE_TAG, ""
				+ player.getScore());
		return playerElement;
	}

	/**
	 * Creates an XML tree for the stats objects.
	 * 
	 * @param stats the incoming stats object.
	 * @return the completed XML tree
	 */
	private static Element createStatXML(Stats stats){
		Element statElement = new Element(STAT_TAG);
		statElement.setAttribute(HP_TAG, stats.getHP() + "");
		statElement.setAttribute(MP_TAG, stats.getMP() + "");
		statElement.setAttribute(INT_TAG, stats.getInt() + "");
		statElement.setAttribute(DEF_TAG, stats.getDef() + "");
		statElement.setAttribute(STR_TAG, stats.getStr() + "");
		statElement.setAttribute(HIT_TAG, stats.getHit() + "");
		statElement.setAttribute(NAME_TAG, stats.getName() + "");
		statElement.setAttribute(MAX_HP_TAG, stats.getMaxHP() + "");
		statElement.setAttribute(MAX_MP_TAG, stats.getMaxMP() + "");
		return statElement;
	}
	
	/**
	 * Creates an XML tree for the minions.
	 * 
	 * @param stats the incoming minion.
	 * @return the completed XML tree
	 */
	private static Element createMinionXML(Minion minion){
		Element minionElement = new Element(MINION_TAG);
		minionElement.setAttribute(NAME_TAG, minion.getName());
		minionElement.setAttribute(ARTICLE_TAG, minion.getArticle());
		minionElement.setAttribute(LOCATION_TAG, minion.getLocation().getName());
		minionElement.setAttribute(DEFEAT_TAG, minion.getBeatMonPointsAward().getPoints() + "");
		Element description = new Element(DESCRIPTION_TAG);
		minionElement.addContent(description);
		description.setText(minion.getDescription());
		return minionElement;
	}
	
	private static Element createBossXML(Boss boss){
		Element bossElement = new Element(BOSS_TAG);
		bossElement.setAttribute(NAME_TAG, boss.getName());
		bossElement.setAttribute(ARTICLE_TAG, boss.getArticle());
		bossElement.setAttribute(LOCATION_TAG, boss.getLocation().getName());
		bossElement.setAttribute(DEFEAT_TAG, boss.getBeatMonPointsAward().getPoints() + "");
		Element description = new Element(DESCRIPTION_TAG);
		bossElement.addContent(description);
		description.setText(boss.getDescription());
		return bossElement;
	}
	
	
	/**
	 * Loads all places found within the root XML element into the world under
	 * construction.
	 * 
	 * @param root
	 *            the root of the save file's XML tree.
	 * @param world
	 *            the game world under construction.
	 */
	@SuppressWarnings("unchecked")
	private static void loadPlaceXML(Element root, World world) {
		List<Element> placeList = root.getChildren(PLACE_TAG);
		/*
		 * First Pass: We need to be careful on creating the map of places
		 * because the interconnections require the places to exist in the world
		 * (a chicken and the egg type problem). Hence, we do this in two steps.
		 * The first step is to load in the descriptive information about all
		 * the places and create them all within the world under construction.
		 */
		for (Element placeElement : placeList) {
			String name = placeElement.getAttributeValue(NAME_TAG);
			String article = placeElement.getAttributeValue(ARTICLE_TAG);
			String description = placeElement.getChild(DESCRIPTION_TAG)
					.getText();
			if (name == null || article == null || description == null)
				throw new IllegalStateException();

			Place p = world.createPlace(name, article, description);
			
			/*
			 * Check to see if the arrival wins game tag is present
			 */
			String arrivalWinsGame = placeElement.getAttributeValue(ARRIVAL_WINS_GAME_TAG);
			if (arrivalWinsGame == null) {
				p.setArrivalWinsGame(false);
			}	
			else {
				if (arrivalWinsGame.equalsIgnoreCase("Y")) {
					p.setArrivalWinsGame(true);
				}
				else {
					p.setArrivalWinsGame(false);
				}
			}
						
		}
		/*
		 * Second Pass: Next, we need to connect the places into a map as
		 * specified by the "travel" elements in the XML.
		 */
		for (Element placeElement : placeList) {
			Place l = world.getPlace(placeElement.getAttributeValue(NAME_TAG));
			if (l == null)
				throw new IllegalStateException(
						"Unable to find a place named \""
								+ placeElement.getAttributeValue(NAME_TAG)
								+ "\" during the second pass through the file..."
								+ "did the file change while we were reading it?");

			List<Element> travelList = placeElement.getChildren(TRAVEL_TAG);
			for (Element t : travelList) {
				Direction d = Direction.getInstance(t
						.getAttributeValue(DIRECTION_TAG));
				if (d == null)
					throw new IllegalStateException("\""
							+ t.getAttributeValue(DIRECTION_TAG)
							+ "\" is not a valid direction for travel from "
							+ "the place named \"" + l.getName() + "\"");

				Place destPlace = world.getPlace(t.getText());
				if (destPlace == null)
					throw new IllegalStateException(
							"Unable to find a place named \"" + t.getText()
									+ "\" as the destination when traveling "
									+ d + " from the place named \""
									+ l.getName() + "\"");

				l.setTravelDestination(d, destPlace);
			}
		}
	}

	/**
	 * Loads information about the player found within the root XML element into
	 * the world under construction.
	 * 
	 * @param root
	 *            the root of the save file's XML tree.
	 * @param world
	 *            the game world under construction.
	 */
	private static void loadPlayerXML(Element root, World world) {
		Element playerElement = root.getChild(PLAYER_TAG);
		if (playerElement == null)
			throw new IllegalStateException();

		String locationName = playerElement.getAttributeValue(LOCATION_TAG);
		if (locationName != null) {
			Place location = world.getPlace(locationName);
			if (location != null) {
				world.getPlayer().setLocation(location);
			} else {
				System.err.println("Unable to find a place named \""
						+ locationName + "\" as the player's location");
			}
		}
		
		String scoreStr = playerElement.getAttributeValue(SCORE_TAG);
		if (scoreStr != null) {
			
			int score = 0;
			try {
				score = Integer.parseInt(scoreStr);
			} catch (NumberFormatException e) {
				System.err.println("Unable to parse value for player score: " + scoreStr);
				System.err.println("    Assigning value to 0");
			}
			
			world.getPlayer().setScore(score);
		}
		else {
			world.getPlayer().setScore(0);
		}
	}
	
	
	/**
	 * Loads all items found within the root XML element into the world under
	 * construction.
	 * 
	 * @param root
	 *            the root of the save file's XML tree.
	 * @param world
	 *            the game world under construction.
	 */
	@SuppressWarnings("unchecked")
	private static void loadItemXML(Element root, World world) {
		List<Element> itemList = root.getChildren(ITEM_TAG);
		/*
		 * First Pass: We need to be careful on creating the map of places
		 * because the interconnections require the places to exist in the world
		 * (a chicken and the egg type problem). Hence, we do this in two steps.
		 * The first step is to load in the descriptive information about all
		 * the places and create them all within the world under construction.
		 */
		for (Element itemElement : itemList) {
			String itemname = itemElement.getAttributeValue(NAME_TAG);
			String article = itemElement.getAttributeValue(ARTICLE_TAG);
			String location = itemElement.getAttributeValue(LOCATION_TAG);
			String takePointsStr = itemElement.getAttributeValue(TAKEPOINTS_TAG);
			String dropPointsStr = itemElement.getAttributeValue(DROPPOINTS_TAG);
			
			if (itemname == null || article == null || location == null || 
					takePointsStr == null || dropPointsStr == null)
				throw new IllegalStateException();
			
			
			int dropPoints = 0;
			int takePoints = 0;
			
			try {
				takePoints = Integer.parseInt(takePointsStr);
			} catch (NumberFormatException e) {
				System.err.println("Unable to parse value for take points for item \"" + itemname + "\"");
				System.err.println("    Assigning value to 0");
			}
			
			try {
				dropPoints = Integer.parseInt(dropPointsStr);
			} catch (NumberFormatException e) {
				System.err.println("Unable to parse value for drop points for item \"" + itemname + "\"");
				System.err.println("    Assigning value to 0");
			}

			String description = article + " " + itemname;
			
			Item i = world.createItem(itemname, article, location, description, takePoints, dropPoints);
			
			List<Element> neededToEnterList = itemElement.getChildren(PLACE_TAG);
			for (Element placeElement : neededToEnterList) {
				String nameOfPlace = placeElement.getText();
				String neededToEnter = placeElement.getAttributeValue(NEEDEDTOENTER_TAG);
				String blockedMessage = placeElement.getAttributeValue(BLOCKEDMSG_TAG);
				String takePointsSpecificStr = placeElement.getAttributeValue(TAKEPOINTS_TAG);
				String dropPointsSpecificStr = placeElement.getAttributeValue(DROPPOINTS_TAG);
				
				if (nameOfPlace == null) {
					throw new IllegalStateException();
				}
				
				Place p = world.getPlaceByName(nameOfPlace);
				
				if (p == null) {
					System.err.println("Unable to find a place named \""
							+ nameOfPlace + "\" that requires item \""
							+ itemname + "\" as needed to enter");				
				}
				else {
					if (neededToEnter != null) {
						if (neededToEnter.equalsIgnoreCase("Y")) {
							if (blockedMessage == null) {
								System.err.println("The blocked message for location \""
										+ nameOfPlace + "\" that requires item \""
										+ itemname + "\" is missing");								
							}
							else {
								p.setItemNeededForEntry(i, blockedMessage);	
							}
						}
					}
					if (dropPointsSpecificStr != null) {	
						int dropPointsSpecific = 0;
						try {
							dropPointsSpecific = Integer.parseInt(dropPointsSpecificStr);
						} catch (NumberFormatException e) {
							System.err.println("Unable to parse value for drop points specific for item \"" 
									+ itemname + "\" at location \"" 
									+ nameOfPlace);
							System.err.println("    Assigning value to 0");
						}					
						i.setLocationDropPoints(p, dropPointsSpecific);
					}
					if (takePointsSpecificStr != null) {
						// Left unimplemented
					}
				}
			}			
		}
	}

	/**
	 * Loads all stats objects found within the root XML element into the world under
	 * construction and associates them with the appropriate Thing.
	 * 
	 * @param root
	 *            the root of the save file's XML tree.
	 * @param world
	 *            the game world under construction.
	 */
	@SuppressWarnings("unchecked")
	private static void loadStatXML(Element root, World world) {
		// Get basic attributes from the XML.
		List<Element> statList = root.getChildren(STAT_TAG);
		for (Element statElement : statList) {
			int hp = -1;
			int mp = -1;
			int stren = -1;
			int intel = -1;
			int defen = -1;
			int hit = -1;
			String name = "";
			int maxHP = -1;
			int maxMP = -1;
			name = statElement.getAttributeValue(NAME_TAG);
			String hpString = statElement.getAttributeValue(HP_TAG);
			if(hpString != null){
				hp = Integer.parseInt(hpString);
			}
			String mpString = statElement.getAttributeValue(MP_TAG);
			if(mpString != null){
				mp = Integer.parseInt(mpString);
			}
			String strString = statElement.getAttributeValue(STR_TAG);
			if(strString != null){
				stren = Integer.parseInt(strString);
			}
			String intString = statElement.getAttributeValue(INT_TAG);
			if(intString != null){
				intel = Integer.parseInt(intString);
			}
			String defString = statElement.getAttributeValue(DEF_TAG);
			if(defString != null){
				defen = Integer.parseInt(defString);
			}
			String hitString = statElement.getAttributeValue(HIT_TAG);
			if(hitString != null){
				hit = Integer.parseInt(hitString);
			}
			String maxHPString = statElement.getAttributeValue(MAX_HP_TAG);
			if(maxHPString != null){
				maxHP = Integer.parseInt(maxHPString);
			}
			String maxMPString = statElement.getAttributeValue(MAX_MP_TAG);
			if(maxMPString != null){
				maxMP = Integer.parseInt(maxMPString);
			}
			if (name == null)
				throw new IllegalStateException();
			Stats thisStats = world.createStats(hp, mp, stren, defen, intel, hit, name, maxHP, maxMP);
			
			Thing target = world.getThingByName(name);
			if(target == null){
				System.out.println(name + " was not found in this world.");
			}
			else{
				if(target instanceof Fighter){
					((Fighter) target).setStats(thisStats);
				}
			}

		}	
	}

	/**
	 * Loads all minions found in the root XML into the world under construction.
	 * @param root
	 * 			the root of the save file's XML tree.
	 * @param world
	 * 			the game world under construction.
	 */		
	private static void loadMinionXML(Element root, World world){
		@SuppressWarnings("unchecked")
		List<Element> minionList = root.getChildren(MINION_TAG);
		for (Element minionElement : minionList) {
			String name = minionElement.getAttributeValue(NAME_TAG);
			String article = minionElement.getAttributeValue(ARTICLE_TAG);
			String description = minionElement.getChild(DESCRIPTION_TAG)
					.getText();
			String location = minionElement.getAttributeValue(LOCATION_TAG);
			String pointString = minionElement.getAttributeValue(DEFEAT_TAG);
			int points = Integer.parseInt(pointString);
			if (name == null || article == null || description == null || location == null)
				throw new IllegalStateException();
			world.createMinion(name, article, location, description, points);
		}
		
	}
	
	/**
	 * Loads all bosses found in the root XML into the world under construction.
	 * @param root
	 * 			the root of the save file's XML tree.
	 * @param world
	 * 			the game world under construction.
	 */		
	private static void loadBossXML(Element root, World world){
		@SuppressWarnings("unchecked")
		List<Element> bossList = root.getChildren(BOSS_TAG);
		for (Element bossElement : bossList) {
			String name = bossElement.getAttributeValue(NAME_TAG);
			String article = bossElement.getAttributeValue(ARTICLE_TAG);
			String description = bossElement.getChild(DESCRIPTION_TAG)
					.getText();
			String location = bossElement.getAttributeValue(LOCATION_TAG);
			String pointString = bossElement.getAttributeValue(DEFEAT_TAG);
			int points = Integer.parseInt(pointString);
			if (name == null || article == null || description == null || location == null)
				throw new IllegalStateException();
			world.createBoss(name, article, location, description, points);
		}
	}
	
	
	private static final String ARTICLE_TAG = "article";

	private static final String DESCRIPTION_TAG = "description";

	private static final String DIRECTION_TAG = "direction";

	private static final String PLACE_TAG = "place";

	private static final String LOCATION_TAG = "location";

	private static final String NAME_TAG = "name";

	private static final String PLAYER_TAG = "player";
	
	private static final String ITEM_TAG = "item";

	private static final String SMALLWORLD_TAG = "smallworld";

	private static final String TRAVEL_TAG = "travel";

	private static final String VERSION_TAG = "version";
	
	private static final String TAKEPOINTS_TAG = "takePoints";
	
	private static final String DROPPOINTS_TAG = "dropPoints";
	
	private static final String NEEDEDTOENTER_TAG = "neededToEnter";
	
	private static final String BLOCKEDMSG_TAG = "blockedMsg";
	
	private static final String ARRIVAL_WINS_GAME_TAG = "arrivalWinsGame";
	
	private static final String SCORE_TAG = "score";
	
	private static final String HP_TAG = "HP";
	
	private static final String MP_TAG = "MP";
	
	private static final String STR_TAG = "STR";
	
	private static final String INT_TAG = "INT";
	
	private static final String DEF_TAG = "DEF";
	
	private static final String HIT_TAG = "HIT";
	
	private static final String STAT_TAG = "stats";
	
	private static final String MINION_TAG = "minion";
	
	private static final String DEFEAT_TAG = "defeatPts";
	
	private static final String BOSS_TAG = "boss";
	
	private static final String MAX_HP_TAG = "maxHP";
	
	private static final String MAX_MP_TAG = "maxMP";
}
