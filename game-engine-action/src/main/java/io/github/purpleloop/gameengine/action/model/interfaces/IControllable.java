package io.github.purpleloop.gameengine.action.model.interfaces;

import io.github.purpleloop.gameengine.action.model.actions.IActionStore;

/** A controllable element of the game. */
public interface IControllable {

    /** @return the action store able to receive actions from the controller */
    IActionStore getActionStore();

    /** Removes all pending actions */
    void drainActions();

}
