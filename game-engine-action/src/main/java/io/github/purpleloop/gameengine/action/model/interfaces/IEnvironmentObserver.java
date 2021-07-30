package io.github.purpleloop.gameengine.action.model.interfaces;

import io.github.purpleloop.gameengine.action.model.events.IGameEvent;

/** Observer of environment events. */
public interface IEnvironmentObserver {

    /**
     * Reacts to a change in the observed environment.
     * 
     * @param gameEvent the event that is source of the change
     */
    void environmentChanged(IGameEvent gameEvent);

}
