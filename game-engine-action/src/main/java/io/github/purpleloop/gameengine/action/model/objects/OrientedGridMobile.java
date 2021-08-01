package io.github.purpleloop.gameengine.action.model.objects;

import io.github.purpleloop.commons.direction.Direction;
import io.github.purpleloop.commons.direction.Direction4;

/** Location of a mobile object in a 2D grid with a 4 way coordination orientation . */
public class OrientedGridMobile extends GridMobile {

    /** The direction the mobile is headed to in a 4 way coordination. */
    protected Direction ori = Direction4.NORTH;

    /** Creates an oriented object.
     * @param x abscissa
     * @param y ordinate
     */
    public OrientedGridMobile(int x, int y) {
        super(x, y);
    }

    /**
     * 
     * @return the direction the mobile is headed to
     */
    public Direction getOrientation() {
        return ori;
    }

    /**
     * @return a textual representation of the mobile object
     */
    public String toString() {
        return super.toString() + " heading " + ori;
    }

    /**
     * Do an oriented move.
     * 
     * @param direction heading direction 
     */
    public void doMove(Direction direction) {
        ori = direction;
        xloc += (int) direction.getXStep();
        yloc += (int) direction.getYStep();

    }

    /** Orients the mobile in a given direction.
     * @param dir direction
     */
    public void setOrientation(Direction dir) {
        ori = dir;
    }

}
