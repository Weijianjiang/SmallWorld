package edu.southalabama.csc527.smallworld;

import static java.lang.System.out;

import java.io.File;

import junit.framework.TestCase;
import edu.southalabama.csc527.smallworld.controller.WorldController;
import edu.southalabama.csc527.smallworld.model.*;
import edu.southalabama.csc527.smallworld.persistence.WorldPersistence;
import edu.southalabama.csc527.smallworld.textui.ParserWorldObserver;
import edu.southalabama.csc527.smallworld.textui.parser.UserCommandParser;

public class TestItemFeature extends TestCase {

	public static final String TEST_WORLD = "/edu/southalabama/csc527/smallworld/TestItemsWorld.xml";

	private World f_w;

	private WorldController f_wc;

	private Place f_hall, f_road, f_building;

	private Item f_coin, f_lamp, f_skunk, f_goldenidol;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		f_w = WorldPersistence.loadWorld(TEST_WORLD);
		assertNotNull(f_w);
		f_wc = new WorldController();
		f_wc.setWorld(f_w);
		f_hall = f_w.getPlace("hall");
		assertNotNull(f_hall);
		f_road = f_w.getPlace("road");
		assertNotNull(f_road);
		f_building = f_w.getPlace("building");
		assertNotNull(f_building);

		f_coin = f_w.getItem("coin");
		assertNotNull(f_coin);
		f_lamp = f_w.getItem("lamp");
		assertNotNull(f_lamp);
		f_skunk = f_w.getItem("skunk");
		assertNotNull(f_skunk);
		f_goldenidol = f_w.getItem("golden idol");
		assertNotNull(f_goldenidol);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		f_w = null;
		f_hall = f_road = f_building = null;
		f_coin = f_lamp = f_skunk = f_goldenidol = null;
	}

	public void testItemPositions() {
		assertSame(f_coin.getLocation(), f_hall);
		assertSame(f_lamp.getLocation(), f_road);
		assertSame(f_skunk.getLocation(), f_hall);
		assertSame(f_goldenidol.getLocation(), f_road);
	}

	public void testItemsRequiredForEntry() {
		Player p = f_w.getPlayer();

		// ROAD (player loc) <-> BUILDING <-> HALL

		// blocked: need Lamp and Coin
		f_wc.travel(Direction.EAST);
		assertSame(p.getLocation(), f_road);

		// blocked: need Coin
		f_coin.setLocation(p);
		f_wc.travel(Direction.EAST);
		assertSame(p.getLocation(), f_road);

		// movement should work
		f_lamp.setLocation(p);
		f_wc.travel(Direction.EAST);
		assertSame(p.getLocation(), f_building);
	}

	public void testCollocatedTakeAndDrop() {
		Player p = f_w.getPlayer();

		assertTrue(p.getInventory().isEmpty());
		f_wc.take(f_lamp);
		assertSame(p, f_lamp.getLocation());
		assertEquals(p.getInventory().size(), 1);
		f_wc.drop(f_lamp);
		assertSame(p.getLocation(), f_lamp.getLocation());
		assertTrue(p.getInventory().isEmpty());
		f_w.getPlayer().setLocation(f_hall);
		f_wc.take(f_skunk);
		assertSame(p, f_skunk.getLocation());
		assertEquals(p.getInventory().size(), 1);
		f_wc.take(f_coin);
		assertSame(p, f_skunk.getLocation());
		assertSame(p, f_coin.getLocation());
		assertEquals(p.getInventory().size(), 2);
		f_wc.drop(f_coin);
		assertSame(p, f_skunk.getLocation());
		assertSame(p.getLocation(), f_coin.getLocation());
		assertEquals(p.getInventory().size(), 1);
		f_wc.drop(f_skunk);
		assertSame(p.getLocation(), f_skunk.getLocation());
		assertSame(p.getLocation(), f_coin.getLocation());
		assertTrue(p.getInventory().isEmpty());
	}

	public void testNoncollocatedTake() {
		Player p = f_w.getPlayer();

		assertTrue(p.getInventory().isEmpty());
		// should fail player is on the Road, Coin is in the Hall
		f_wc.take(f_coin);
		assertSame(f_hall, f_coin.getLocation());
		assertTrue(p.getInventory().isEmpty());
	}

	public void testTakeAnywherePointAward() {
		Player p = f_w.getPlayer();

		assertEquals(p.getScore(), 0);
		f_wc.take(f_lamp);
		assertSame(p, f_lamp.getLocation());
		assertEquals(p.getScore(), 10);
		f_w.getPlayer().setLocation(f_hall);
		f_wc.take(f_skunk);
		assertSame(p, f_skunk.getLocation());
		assertEquals(p.getScore(), 5);
		f_wc.take(f_coin);
		assertSame(p, f_coin.getLocation());
		assertEquals(p.getScore(), 25);
	}
	
	public void testTakePointsSaveLoad() {
		Player p = f_w.getPlayer();

		assertEquals(p.getScore(), 0);
		f_wc.take(f_lamp);
		assertSame(p, f_lamp.getLocation());
		assertEquals(p.getScore(), 10);
		f_w.getPlayer().setLocation(f_hall);
		f_wc.take(f_skunk);
		assertSame(p, f_skunk.getLocation());
		assertEquals(p.getScore(), 5);
		f_wc.take(f_coin);
		assertSame(p, f_coin.getLocation());
		assertEquals(p.getScore(), 25);
		
		try {
			WorldPersistence.saveWorld(f_wc.getWorld(), new File(TestConstants.POINTSSAVEFILE));
		} catch (Exception e) {
			fail();
		}
		World w1 = WorldPersistence.loadWorld(new File(TestConstants.POINTSSAVEFILE));
		if (w1 == null)
			fail();
		assertEquals(w1.getPlayer().getScore(),25);
		
		
	}

	public void testDropAnywherePointAward() {
		Player p = f_w.getPlayer();

		assertEquals(p.getScore(), 0);
		f_wc.take(f_lamp);
		assertSame(p, f_lamp.getLocation());
		assertEquals(p.getScore(), 10);
		f_w.getPlayer().setLocation(f_hall);
		f_wc.take(f_skunk);
		assertSame(p, f_skunk.getLocation());
		assertEquals(p.getScore(), 5);
		f_w.getPlayer().setLocation(f_road);
		f_wc.drop(f_lamp);
		assertSame(p.getLocation(), f_lamp.getLocation());
		assertEquals(p.getScore(), 15);
		f_wc.drop(f_skunk);
		assertSame(p.getLocation(), f_skunk.getLocation());
		assertEquals(p.getScore(), 5);
	}

	public void testDoubleTakeDropAnywherePointAward() {
		Player p = f_w.getPlayer();

		assertEquals(p.getScore(), 0);
		f_wc.take(f_lamp);
		assertSame(p, f_lamp.getLocation());
		assertEquals(p.getScore(), 10);
		f_w.getPlayer().setLocation(f_hall);
		f_wc.take(f_skunk);
		assertSame(p, f_skunk.getLocation());
		assertEquals(p.getScore(), 5);
		f_w.getPlayer().setLocation(f_road);
		f_wc.drop(f_lamp);
		assertSame(p.getLocation(), f_lamp.getLocation());
		assertEquals(p.getScore(), 15);
		f_wc.drop(f_skunk);
		assertSame(p.getLocation(), f_skunk.getLocation());
		assertEquals(p.getScore(), 5);

		// second take-drop on the same items should not change score
		f_wc.take(f_lamp);
		assertSame(p, f_lamp.getLocation());
		assertEquals(p.getScore(), 5);
		f_wc.take(f_skunk);
		assertSame(p, f_skunk.getLocation());
		assertEquals(p.getScore(), 5);
		f_wc.drop(f_lamp);
		assertSame(p.getLocation(), f_lamp.getLocation());
		assertEquals(p.getScore(), 5);
		f_wc.drop(f_skunk);
		assertSame(p.getLocation(), f_skunk.getLocation());
		assertEquals(p.getScore(), 5);
	}

	public void testLocationSpecificDropPointAward_1() {
		Player p = f_w.getPlayer();

		// normal anywhere and location specific award
		p.setLocation(f_hall);
		f_wc.take(f_coin);
		assertEquals(p.getScore(), 20);
		f_wc.drop(f_coin);
		assertEquals(p.getScore(), 100 + 1 + 20);
	}

	public void testLocationSpecificDropPointAward_2() {
		Player p = f_w.getPlayer();

		// a second anywhere and location specific award
		p.setLocation(f_hall);
		f_wc.take(f_coin);
		assertEquals(p.getScore(), 20);
		p.setLocation(f_road);
		f_wc.drop(f_coin);
		assertEquals(p.getScore(), 200 + 1 + 20);
	}

	public void testLocationSpecificDropPointAward_3() {
		Player p = f_w.getPlayer();

		// negative location specific award
		p.setLocation(f_hall);
		f_wc.take(f_coin);
		assertEquals(p.getScore(), 20);
		p.setLocation(f_building);
		f_wc.drop(f_coin);
		// Three possible answers
		if (f_coin.getLocation() == f_building) {
			/*
			 * Zero or One depending upon the order the anywhere and location
			 * specific points were awarded -- in the case that the score never
			 * goes below 0. If they allow the score to go below zero the result
			 * will be -29.
			 */
			assertTrue("score should be either 0 or 1 (if score never below 0)"
					+ " or -29 (negative scores allowed) and it is "
					+ p.getScore(), p.getScore() == 0 || p.getScore() == 1
					|| p.getScore() == -29);
		} else {
			/*
			 * The item couldn't be dropped because it is needed to enter this
			 * location (one possible solution for the stuck player problem).
			 */
			assertTrue(p.getInventory().contains(f_coin));
		}
	}

	public void testLocationSpecificDropPointAward_4() {
		Player p = f_w.getPlayer();

		// double-drop test
		p.setLocation(f_hall);
		f_wc.take(f_coin);
		assertEquals(p.getScore(), 20);
		f_wc.drop(f_coin);
		assertEquals(p.getScore(), 100 + 1 + 20);
		f_wc.take(f_coin);
		assertEquals(p.getScore(), 100 + 1 + 20);
		f_wc.drop(f_coin);
		assertEquals(p.getScore(), 100 + 1 + 20);
	}

	public void testSaveLoad() {
		World w = new World();
		Item lamp = w.createItem("lamp", "a", "Very Remote Place", "A lamp", 0, 0);
		lamp.setLocation(w.getPlayer());
		try {
			WorldPersistence.saveWorld(w, new File(TestConstants.SAVEFILE));
		} catch (Exception e) {
			fail();
		}
		World w1 = WorldPersistence.loadWorld(new File(TestConstants.SAVEFILE));
		if (w1 == null)
			fail();
		assertSame(w1.getItem("lamp").getLocation(), w1.getPlayer());
	}
	
	
	public void testParser() {
		TextualParserWorldObserver po = new TextualParserWorldObserver();
		UserCommandParser parser = new UserCommandParser(f_wc, po);
		String command = "look";
		parser.parse(command);
		assertTrue(f_wc.getWorld().getPlayer().getLocation() == f_road);
		assertTrue(f_goldenidol.getLocation() == f_road);
		command = "take golden idol";
		parser.parse(command);
		assertTrue(f_goldenidol.getLocation() == f_wc.getWorld().getPlayer());
		command = "look";
		parser.parse(command);
		
	}
		
	private class TextualParserWorldObserver extends ParserWorldObserver {
		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.southalabama.csc527.smallworld.textui.ParserWorldObserver#gameOver()
		 */
		@Override
		public void gameOver() {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.southalabama.csc527.smallworld.textui.ParserWorldObserver#show(java.lang.String)
		 */
		@Override
		public void show(String msg) {
			out.println(msg);
		}
	}

}
