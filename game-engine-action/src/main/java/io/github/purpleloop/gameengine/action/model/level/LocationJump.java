package io.github.purpleloop.gameengine.action.model.level;

import io.github.purpleloop.gameengine.action.model.environment.AbstractCellObjectEnvironment;
import io.github.purpleloop.gameengine.action.model.objects.GameObject;
import io.github.purpleloop.gameengine.core.util.Location;

/**
 * Models a location jump, in the level (like a teleporter, secret passage, cave
 * shortcut...).
 */
public class LocationJump extends LocatedLevelLink {

    /** Destination location. */
    private Location destinationLocation;

    /**
     * Creates a location jump that, when reaching the given coordinates, leads
     * to another location in the same level.
     * 
     * @param sourceLocation the source location
     * @param destinationLocation the destination location
     */
    public LocationJump(Location sourceLocation, Location destinationLocation) {
        super(sourceLocation);
        this.destinationLocation = destinationLocation;
    }

    /** @return The destination location. */
    public Location getDestinationLocation() {
        return destinationLocation;
    }

    @Override
    public void applyChanges(AbstractCellObjectEnvironment environment, GameObject object) {
        environment.locationJump(destinationLocation, object);
    }

}
