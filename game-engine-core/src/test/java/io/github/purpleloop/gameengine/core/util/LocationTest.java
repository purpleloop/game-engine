package io.github.purpleloop.gameengine.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** Tests on locations. */
public class LocationTest {

    /** Tests (0, 0) location. */
    @Test
    public void testGetLocationZeroZero() {
        Location loc = Location.getLocation(0, 0);
        assertEquals(0, loc.getX());
        assertEquals(0, loc.getY());
        assertEquals("(0, 0)", loc.toString());
        assertTrue(loc.equals(0, 0));
        assertEquals(0, loc.hashCode());
    }

    /** Tests a location on (Ox). */
    @Test
    public void testGetLocationValueZero() {
        Location loc = Location.getLocation(31, 0);
        assertEquals(31, loc.getX());
        assertEquals(0, loc.getY());
        assertEquals("(31, 0)", loc.toString());
        assertTrue(loc.equals(31, 0));
        assertEquals(31, loc.hashCode());
    }

    /** Tests a location on (Oy). */
    @Test
    public void testGetLocationZeroValue() {
        Location loc = Location.getLocation(0, 27);
        assertEquals(0, loc.getX());
        assertEquals(27, loc.getY());
        assertEquals("(0, 27)", loc.toString());
        assertTrue(loc.equals(0, 27));
        assertEquals(270000, loc.hashCode());
    }

    /** Tests an ordinary location. */
    @Test
    public void testGetLocationTwoValues() {
        Location loc = Location.getLocation(123, 751);
        assertEquals(123, loc.getX());
        assertEquals(751, loc.getY());
        assertEquals("(123, 751)", loc.toString());

        assertTrue(loc.equals(123, 751));
        assertFalse(loc.equals(87, 751));
        assertFalse(loc.equals(123, 4670));
        assertFalse(loc.equals(2456, 9873));

        assertNotEquals(loc, new Object());

        assertEquals(Location.getLocation(123, 751), loc);
        assertNotEquals(Location.getLocation(87, 751), loc);
        assertNotEquals(Location.getLocation(123, 4670), loc);
        assertNotEquals(Location.getLocation(2456, 9873), loc);

        assertEquals(7510123, loc.hashCode());
    }

    /** Tests (-5, 0) location. */
    @Test(expected = IllegalArgumentException.class)
    public void testGetLocationOutOfBoundsXnegative() {
        Location.getLocation(-5, 0);
    }

    /** Tests (0, -900) location. */
    @Test(expected = IllegalArgumentException.class)
    public void testGetLocationOutOfBoundsYnegative() {
        Location.getLocation(0, -900);
    }

    /** Tests (25125, 0) location. */
    @Test(expected = IllegalArgumentException.class)
    public void testGetLocationOutOfBoundsXOut() {
        Location.getLocation(25125, 0);
    }

    /** Tests (0, 478451) location. */
    @Test(expected = IllegalArgumentException.class)
    public void testGetLocationOutOfBoundsYOut() {
        Location.getLocation(0, 478451);
    }

    /** Tests (1, 1) location for alpha. */
    @Test
    public void testAlphaA1() {
        assertEquals("A,1", Location.getLocation(1, 1).toAlphanumString());
    }

    /** Tests (26, 26) location for alpha. */
    @Test
    public void testAlphaZ26() {
        assertEquals("Z,26", Location.getLocation(26, 26).toAlphanumString());
    }

}
