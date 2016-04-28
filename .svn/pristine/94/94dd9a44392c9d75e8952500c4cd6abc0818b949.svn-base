package edu.southalabama.csc527.smallworld;

import junit.framework.TestCase;
import edu.southalabama.csc527.smallworld.controller.WorldController;
import edu.southalabama.csc527.smallworld.model.Direction;
import edu.southalabama.csc527.smallworld.model.Place;
import edu.southalabama.csc527.smallworld.model.World;
import edu.southalabama.csc527.smallworld.textui.parser.IParserObserver;
import edu.southalabama.csc527.smallworld.textui.parser.UserCommandParser;
import edu.southalabama.csc527.smallworld.model.Player;

public class TestMovementShortcutFeatureKey extends TestCase implements
		IParserObserver {

	public void testDirection() {
		Player p = f_w.getPlayer();
		// Travel north
		f_parser.parse("go N");
		assertTrue(p.getLocation().equals(f_w.getPlace("N")));
		// Travel south
		f_parser.parse("go S");
		assertTrue(p.getLocation().equals(f_w.getPlace("C")));
		// Travel south
		f_parser.parse("go South");
		assertTrue(p.getLocation().equals(f_w.getPlace("S")));
		// Travel north
		f_parser.parse("go North");
		assertTrue(p.getLocation().equals(f_w.getPlace("C")));
		// Travel east
		f_parser.parse("go E");
		assertTrue(p.getLocation().equals(f_w.getPlace("E")));
		// Travel west
		f_parser.parse("go W");
		assertTrue(p.getLocation().equals(f_w.getPlace("C")));
		// Travel west
		f_parser.parse("go West");
		assertTrue(p.getLocation().equals(f_w.getPlace("W")));
		// Travel east
		f_parser.parse("go East");
		assertTrue(p.getLocation().equals(f_w.getPlace("C")));
		// Travel north
		f_parser.parse("go NoRtH");
		assertTrue(p.getLocation().equals(f_w.getPlace("N")));
		// Travel south
		f_parser.parse("go SoUtH");
		assertTrue(p.getLocation().equals(f_w.getPlace("C")));
		// Travel east
		f_parser.parse("go EAst");
		assertTrue(p.getLocation().equals(f_w.getPlace("E")));
		// Travel west
		f_parser.parse("go WeSt");
		assertTrue(p.getLocation().equals(f_w.getPlace("C")));
		// Travel nowhere (bad spelling)
		f_parser.parse("go Wset");
		assertTrue(p.getLocation().equals(f_w.getPlace("C")));
		assertFalse(p.getLocation().equals(f_w.getPlace("W")));
		// Travel nowhere (bad spelling)
		f_parser.parse("go Esat");
		assertTrue(p.getLocation().equals(f_w.getPlace("C")));
		assertFalse(p.getLocation().equals(f_w.getPlace("E")));
		// Travel nowhere (bad spelling)
		f_parser.parse("go Nroth");
		assertTrue(p.getLocation().equals(f_w.getPlace("C")));
		assertFalse(p.getLocation().equals(f_w.getPlace("N")));	
		// Travel nowhere (bad spelling)
		f_parser.parse("go Suoth");
		assertTrue(p.getLocation().equals(f_w.getPlace("C")));
		assertFalse(p.getLocation().equals(f_w.getPlace("W")));
		// Travel nowhere (random characters)
		f_parser.parse("go re34");
		assertTrue(p.getLocation().equals(f_w.getPlace("C")));
		assertFalse(p.getLocation().equals(f_w.getPlace("W")));
	}

	public void testAbbreviation() {
		Player p = f_w.getPlayer();
		f_parser.parse("N");
		assertTrue(p.getLocation().equals(f_w.getPlace("N")));
		f_parser.parse("S");
		assertTrue(p.getLocation().equals(f_w.getPlace("C")));
		f_parser.parse("s");
		assertTrue(p.getLocation().equals(f_w.getPlace("S")));
		f_parser.parse("n");
		assertTrue(p.getLocation().equals(f_w.getPlace("C")));
		f_parser.parse("E");
		assertTrue(p.getLocation().equals(f_w.getPlace("E")));
		f_parser.parse("W");
		assertTrue(p.getLocation().equals(f_w.getPlace("C")));
		f_parser.parse("w");
		assertTrue(p.getLocation().equals(f_w.getPlace("W")));
		f_parser.parse("e");
		assertTrue(p.getLocation().equals(f_w.getPlace("C")));
		// Travel nowhere (random letter)
		f_parser.parse("r");
		assertTrue(p.getLocation().equals(f_w.getPlace("C")));
		assertFalse(p.getLocation().equals(f_w.getPlace("W")));
		// Travel nowhere (random number)
		f_parser.parse("3");
		assertTrue(p.getLocation().equals(f_w.getPlace("C")));
		assertFalse(p.getLocation().equals(f_w.getPlace("W")));
	}

	World f_w;

	WorldController f_wc;

	UserCommandParser f_parser;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		f_w = new World();
		f_wc = new WorldController();
		f_wc.setWorld(f_w);
		f_parser = new UserCommandParser(f_wc, this);

		// create a simple world
		Place c = f_w.createPlace("C", "the", "C");
		Place n = f_w.createPlace("N", "the", "N");
		Place s = f_w.createPlace("S", "the", "S");
		Place e = f_w.createPlace("E", "the", "E");
		Place w = f_w.createPlace("W", "the", "W");

		c.setTravelDestination(Direction.NORTH, n);
		n.setTravelDestination(Direction.SOUTH, c);

		c.setTravelDestination(Direction.SOUTH, s);
		s.setTravelDestination(Direction.NORTH, c);

		c.setTravelDestination(Direction.EAST, e);
		e.setTravelDestination(Direction.WEST, c);

		c.setTravelDestination(Direction.WEST, w);
		w.setTravelDestination(Direction.EAST, c);

		f_w.getPlayer().setLocation(c);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		f_parser = null;
		f_wc = null;
		f_w = null;
	}

	public void look(World world) {
		// do nothing

	}

	public void display(String msg) {
		// do nothing
	}
}
