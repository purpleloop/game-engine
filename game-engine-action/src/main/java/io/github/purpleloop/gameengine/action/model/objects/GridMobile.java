package io.github.purpleloop.gameengine.action.model.objects;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import io.github.purpleloop.commons.math.GeomUtils;
import io.github.purpleloop.gameengine.core.util.Location;

/**
 * Class used to position an object in a 2D Grid.
 * 
 * Warning : Objects of this class are not immutable.
 * 
 * See also {@link Location}
 * 
 */
public class GridMobile {

    /** Abscissa of the object in the grid. */
    protected int xloc;

    /** Ordinate of the object in the grid. */
    protected int yloc;

    /** Creates a new mobile localized at the given coordinates in the grid.
     * @param x abscissa
     * @param y ordinate 
     */
    public GridMobile(int x, int y) {
        xloc = x;
        yloc = y;
    }

    /**
     * @return abscissa of the object in the grid
     */
    public int getXloc() {
        return xloc;
    }

    /**
     * @return ordinate of the object in the grid
     */
    public int getYloc() {
        return yloc;
    }

    /**
     * Tests if the object is in the grid cell (x,y).
     * 
     * @param x abscissa of the tested cell
     * @param y ordinate of the tested cell
     * @return true if the mobile is in the cell, false otherwise
     */
    public boolean isIn(int x, int y) {
        return (xloc == x) && (yloc == y);
    }

    /**
     * Tests if the object is a rectangle of grid cellq (x1,y1)-(x2,y2).
     * 
     * @param x1 abscissa of the left top corner cell
     * @param y1 ordinate of the left top corner cell
     * @param x2 abscissa of the right bottom corner cell
     * @param y2 ordinate of the right bottom corner cell
     * @return true if the mobile is in the rectangle of cells, false otherwise
     * */
    public boolean isInZone(int x1, int y1, int x2, int y2) {
        return (xloc >= x1) && (yloc >= y1) && (xloc <= x2) && (yloc <= y2);
    }

    /**
     * Computes the distance between the mobile and the given cell (x,y).
     * 
     * @param x abscissa of the tested cell
     * @param y ordinate of the tested cell
     * @return distance between the mobile and the given cell
     * */
    public double distanceTo(int x, int y) {
        return GeomUtils.distance(xloc, yloc, x, y);
    }

    /**
     * Computes the distance between the mobile and another one.
     * 
     * @param other another mobile
     * @return distance between the two mobile objects.
     * */
    public double distanceTo(GridMobile other) {
        return GeomUtils.distance(xloc, yloc, other.getXloc(), other.getYloc());
    }

    /**
     * Describes the mobile object.
     * 
     * @return textual description (essentially the location)
     */
    public String toString() {
        return "(" + xloc + "," + yloc + ")";
    }

    /** Modifies the coordinates of the mobile object.
     * @param x abscissa
     * @param y ordinate
     */
    public void setTo(int x, int y) {
        xloc = x;
        yloc = y;
    }
    
    /** Modifies the coordinates of the mobile object to put it as the same place of another one.
     * @param other other mobile object coordinate
     */
    public void setTo(GridMobile other) {
        xloc = other.xloc;
        yloc = other.yloc;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(xloc);
        builder.append(yloc);
        return builder.toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof GridMobile) {
            GridMobile otherLoc = (GridMobile) other;
            return (xloc == otherLoc.xloc) && (yloc == otherLoc.yloc);

        } else {
            return false;
        }
    }
}
