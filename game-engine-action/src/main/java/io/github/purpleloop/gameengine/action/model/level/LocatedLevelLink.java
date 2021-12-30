package io.github.purpleloop.gameengine.action.model.level;

import io.github.purpleloop.gameengine.action.model.environment.AbstractCellObjectEnvironment;
import io.github.purpleloop.gameengine.action.model.objects.GameObject;
import io.github.purpleloop.gameengine.core.util.Location;

/**
 * An abstract link, internal or external to the current level, that is taken
 * when reaching a given location.
 */
public abstract class LocatedLevelLink implements LevelLink {

    /** Start location. */
    protected Location startLocation;

    /**
     * Creates a link that is triggered when reaching the given coordinates.
     * 
     * @param startLocation the start location
     */
    protected LocatedLevelLink(Location startLocation) {
        this.startLocation = startLocation;
    }

    /**
     * Check if the provided coordinates matches this link.
     * 
     * @param x abscissa
     * @param y ordinate
     * @return true if the start location matches the given coordinates, false
     *         otherwise
     */
    public boolean matches(int x, int y) {
        return startLocation.equals(x, y);
    }

    /**
     * Applies the link effects, when the object reaches the link location in
     * the given environment.
     * 
     * @param environment the context environment
     * @param object the object on which to apply the effects of the link
     */
    public abstract void applyChanges(AbstractCellObjectEnvironment environment,
            GameObject object);

}
