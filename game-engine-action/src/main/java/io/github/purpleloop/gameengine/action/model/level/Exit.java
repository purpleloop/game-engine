package io.github.purpleloop.gameengine.action.model.level;

import io.github.purpleloop.gameengine.action.model.environment.AbstractCellObjectEnvironment;
import io.github.purpleloop.gameengine.action.model.objects.GameObject;
import io.github.purpleloop.gameengine.core.util.Location;

/** Models a located exit of a level. */
public class Exit extends LocatedLevelLink {

    /** The level to which the exit gives access. */
    private String targetLevelId;

    /**
     * Creates an exit from the level that, when reaching the given coordinates,
     * leads to the target level.
     * 
     * @param sourceLocation the location of the exit
     * @param targetLevelId target level id
     */
    public Exit(Location sourceLocation, String targetLevelId) {
        super(sourceLocation);
        this.targetLevelId = targetLevelId;
    }

    @Override
    public void applyChanges(AbstractCellObjectEnvironment environment,
            GameObject object) {
        environment.reachExit(targetLevelId, object);
    }

}
