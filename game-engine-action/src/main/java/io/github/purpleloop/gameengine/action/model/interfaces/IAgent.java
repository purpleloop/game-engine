package io.github.purpleloop.gameengine.action.model.interfaces;

/**
 * Represents an agent (an active entity of the environment) in the game engine.
 */
public interface IAgent extends IEnvironmentObjet {

    /**
     * Behavior of the agent.
     * 
     * Note that this is distinct from the natural evolution of the agent as an
     * object of the environment.
     */
    void behave();

    /**
     * Returns a reward for the agent.
     * 
     * This is an explicit value function and can serve as a basis for learning
     * or comparison purpose. The idea behind is to reward agents according to
     * their behavior or reversely to adjust behavior to optimize the reward.
     * 
     * @param value reward value
     */
    void reward(double value);

    /** @return returns the cumulative reward for the agent */
    double getCummulativeReward();

}
