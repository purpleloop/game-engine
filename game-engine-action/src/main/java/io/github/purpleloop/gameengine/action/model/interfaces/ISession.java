package io.github.purpleloop.gameengine.action.model.interfaces;

import java.util.List;

import io.github.purpleloop.gameengine.action.model.dialog.DialogObserver;
import io.github.purpleloop.gameengine.action.model.events.IGameEvent;
import io.github.purpleloop.gameengine.core.util.EngineException;

/** Models a session of the game. */
public interface ISession extends IEnvironmentObserver, DialogObserver {

    /**
     * Updates the session by one unit of time (frame).
     * 
     * @throws EngineException in case of problems
     */
    void update() throws EngineException;

    /** @return true if the session is ended, false otherwise. */
    boolean isEnded();

    /** @return the session is currently in intermission mode */
    boolean isIntermission();

    /**
     * @return the current environment of the session
     */
    ISessionEnvironment getCurrentEnvironment();

    /**
     * @return the game engine where the session takes place
     */
    IGameEngine getGameEngine();

    /**
     * Takes a game event into account in the environment.
     * 
     * @param event the source event
     */
    void environmentChanged(IGameEvent event);

    /** Finish and cleanup the session. */
    void cleanup();

    /** @return list of all players in this session */
    List<IPlayer> getPlayers();

    /** @return Current level id. */
    String getCurrentLevelId();

    /** @return Target level id. */
    String getTargetLevelId();

}
