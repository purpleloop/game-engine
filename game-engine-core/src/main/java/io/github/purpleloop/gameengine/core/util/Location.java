package io.github.purpleloop.gameengine.core.util;

import java.util.HashMap;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Objects of this class model a position in a 2 dimensional coordinate system
 * (x, y).
 *
 * Similarly to java.lang.Integer, instances of this class are shared and
 * immutable.
 */
public final class Location {

    /** Start index for alphabet in character table. */
    private static final int CHARACTER_ALPHA_UPPER_START_INDEX = 64;

    /** Known locations. */
    private static HashMap<Integer, Location> locations;

    static {
        locations = new HashMap<Integer, Location>();
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

    /** Tests if the location equals to a given coordinate set.
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

        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(x);
        hcb.append(y);
        return hcb.hashCode();
    }

    /**
     * @param x abscissa
     * @param y ordinate
     * @return location (x, y)
     */
    public static Location getLocation(int x, int y) {

        int hc = hashCode(x, y);

        Location loc = locations.get(hc);
        if (loc == null) {
            loc = new Location(x, y);
            locations.put(hc, loc);
        }
        return loc;
    }

    /** @return String describing the location under the form : alpha,number. */
    public String toAlphanumString() {
        return ((char) (x + CHARACTER_ALPHA_UPPER_START_INDEX)) + "," + y;
    }

}
