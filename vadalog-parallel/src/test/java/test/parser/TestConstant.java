package test.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.co.prometheux.prometheuxreasoner.model.Constant;

public class TestConstant {

	
	@Test
	public void detailedTestConstants() {
		Constant<String> sc1 = new Constant<String>("\"Hello\"");
		Constant<String> sc2 = new Constant<String>("\"Hello2\"");
		Constant<String> sc3 = new Constant<String>("\"Hello\"");
		Constant<String> empty = new Constant<String>("\"\"");

		Constant<Integer> ic1 = new Constant<Integer>(1);
		Constant<Integer> ic2 = new Constant<Integer>(2);
		Constant<Integer> ic3 = new Constant<Integer>(1);

		assertTrue(sc1.toString().equals("\"Hello\""));
		assertTrue(sc1.strip().equals("Hello"));
		assertTrue(empty.toString().equals("\"\""));
		assertTrue(empty.strip().equals(""));
		assertTrue(ic1.strip().equals(1));
		
		assertTrue(ic1.equals(ic1));
		assertTrue(sc1.equals(sc1));
		assertTrue(ic1.toString().equals("1"));
		assertTrue(ic1.strip().equals(1));
		
		assertTrue(sc1.equals(sc3));
		assertFalse(sc1.equals(sc2));
		assertTrue(ic1.equals(ic3));
		assertFalse(ic1.equals(ic2));
		assertFalse(ic1.equals(sc1));
		assertFalse(sc1.equals(ic1));
		
	}
}