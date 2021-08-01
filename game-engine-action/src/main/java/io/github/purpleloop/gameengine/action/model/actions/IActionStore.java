package io.github.purpleloop.gameengine.action.model.actions;

import java.util.Set;

/**
 * A store for action to be performed.
 * Actions are coming from a controller.
 */
public interface IActionStore {

    /**
     * Adds an action to perform.
     * 
     * @param action action to perform
     */
    void addAction(String action);

    /**
     * Gives the current actions.
     * 
     * @return currently stored action names
     */
    Set<String> getCurrentActions();

    /**
     * Forget / consume the action (prevents a double execution).
     * 
     * @param action to forget
     */
    void forgetAction(String action);

    /**
     * Forget / consume all stored actions (prevents all executions).
     */
    void forgetAll();

}
