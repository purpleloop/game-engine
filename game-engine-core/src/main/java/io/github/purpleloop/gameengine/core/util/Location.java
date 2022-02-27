package io.github.purpleloop.gameengine.core.util;

import java.util.HashMap;

/**
 * Objects of this class model a position in a 2 dimensional coordinate system
 * (x, y), bounded to (0-9999, 0-9999).
 *
 * Similarly to java.lang.Integer, instances of this class are shared and
 * immutable.
 */
public final class Location {

    /** Maximum value for coordinates. */
    public static final int MAX_VALUE = 9999;

    /**
     * A sufficient hash factor to prevent collisions on (0-9999, 0-9999)
     * locations.
     */
    private static final int HASH_FACTOR = MAX_VALUE + 1;

    /** Start index for alphabet in character table. */
    private static final int CHARACTER_ALPHA_UPPER_START_INDEX = 64;

    /** Known locations. */
    private static HashMap<Integer, Location> locations;

    static {
        locations = new HashMap<>();
    }

    /** Abscissa. */
    private final int x;

    /** Ordinate. */
    private final int y;

    /**
     * Creates the location (x, y).
     * 
     * @param x abscissa
     * @param y ordinate
     */
    private Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Tests if coordinates are valid for a location.
     * 
     * @param x abscissa
     * @param y ordinate
     * @return true if locations are in bounds (0-9999, 0-9999), false
     *         otherwise.
     */
    private static boolean isValidLocation(int x, int y) {
        return x >= 0 && x <= MAX_VALUE && y >= 0 && y <= MAX_VALUE;
    }

    /** @return abscissa of the position */
    public int getX() {
        return x;
    }

    /** @return ordinate of the position */
    public int getY() {
        return y;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return hashCode(x, y);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Location)) {
            return false;
        }

        Location otherLoc = (Location) obj;
        return (x == otherLoc.x) && (y == otherLoc.y);
    }

    /**
     * Tests if the location equals to a given coordinate set.
     * 
     * @param xt the abscissa to test
     * @param yt the ordinate to test
     * @return true if locations equals to (xt, yt), false otherwise
     */
    public boolean equals(int xt, int yt) {
        return (x == xt) && (y == yt);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    /**
     * Hashcode for a position given by it's coordinates.
     * 
     * @param x abscissa
     * @param y ordinate
     * @return hashcode of the location
     */
    public static int hashCode(int x, int y) {
        return HASH_FACTOR * y + x;
    }

    /**
     * @param x abscissa
     * @param y ordinate
     * @return location (x, y)
     */
    public static Location getLocation(int x, int y) {
        
        if (!isValidLocation(x, y)) {
            throw new IllegalArgumentException("Locations are bounded to (0-9999,0-9999).");
        }
        
        return locations.computeIfAbsent(hashCode(x, y), Location::createFor);
    }

    /**
     * Creates a canonical location for the given hashcode.
     * 
     * @param hashCode the hashcode
     * @return the canonical location derived from the hashcode
     */
    private static Location createFor(int hashCode) {
        return new Location(hashCode % HASH_FACTOR, hashCode / HASH_FACTOR);
    }

    /** @return String describing the location under the form : alpha,number. */
    public String toAlphanumString() {
        return ((char) (x + CHARACTER_ALPHA_UPPER_START_INDEX)) + "," + y;
    }

}
