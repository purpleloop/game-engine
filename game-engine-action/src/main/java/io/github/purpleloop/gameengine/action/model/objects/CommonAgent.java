package io.github.purpleloop.gameengine.action.model.objects;

import io.github.purpleloop.gameengine.action.model.interfaces.IAgent;

/** A common agent in a base class game agents. */
public abstract class CommonAgent extends GameObject implements IAgent {

    @Override
    public void behave() {
    }    
    
    @Override
    public void reward(double i) {
    }

    @Override
    public double getCummulativeReward() {
        return 0;
    }

}
