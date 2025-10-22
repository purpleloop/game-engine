package io.github.purpleloop.gameengine.action.model.environment;

import io.github.purpleloop.gameengine.action.model.interfaces.ISession;
import io.github.purpleloop.gameengine.action.model.level.IGameLevel;
import io.github.purpleloop.gameengine.core.util.EngineException;

/**
 * An abstract environment that models a simple scene with linear locations.
 * 
 * This class serves as basis for games where environment is scene where agents
 * are placed and move along a single line. The scene is bound at the left and
 * at the right.
 * 
 * This is typically the base class for a legacy game fighting arena.
 */
public abstract class AbstractLinearSceneEnvironment extends AbstractObjectEnvironment {

    /** Width of the scene, expressed in base units. */
    protected int width;

    /**
     * Constructor of the scene environment.
     * 
     * @param session the game session
     * @param level the game level
     */
    protected AbstractLinearSceneEnvironment(ISession session, IGameLevel level)
            throws EngineException {
        super(session, level);

        initFromGameLevel();
    }

    /**
     * Initializes the scene.
     * 
     * @param sceneWidth the scene width
     */
    protected void initScene(int sceneWidth) {
        this.width = sceneWidth;
    }

    /**
     * Tests if an object within bounds of the scene environment.
     * 
     * @param x abscissa in base units
     * @param objectWidth object width
     * @return true if the location is in bounds, false otherwise
     */
    public boolean isObjectInBounds(int x, int objectWidth) {
        return (x >= 0) && (x + objectWidth < width);
    }

}
