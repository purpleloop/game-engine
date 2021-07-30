package io.github.purpleloop.gameengine.action.model.objects;

import io.github.purpleloop.commons.direction.Direction;
import io.github.purpleloop.gameengine.action.model.environment.AbstractEnvironment;
import io.github.purpleloop.gameengine.action.model.interfaces.ISessionEnvironment;

/** This class extends the CommonAgent class.
 * An agent is localized on a discrete grid and has a direction.
 * It also has a name and is attached to a session.
 * 
 * TODO : This class is relatively specific to some kind of games.
 * It uses discrete grids and may require improvements.
 */
public abstract class Agent extends CommonAgent {

    /** Location on the 2D discrete grid and orientation. */
    protected OrientedGridMobile location;

    /** Number of moves left to finish the action. */
    protected int movesToDoLeft = 0;

    /** Name of the agent. */
    protected String name;

    /** The environment where the agent evolves. */
    protected ISessionEnvironment environment;

    /** Creates an agent.
     * @param name name of the agent
     * @param x abscissa
     * @param y ordinate
     * @param environment environment where the agent evolves
     */
    public Agent(String name, int x, int y, ISessionEnvironment environment) {
        this.location = new OrientedGridMobile(x, y);
        this.name = name;
        this.environment = environment;
    }

    /**
     * @return the agent location in the grid
     */
    public OrientedGridMobile getLocation() {
        return location;
    }

    /**
     * @return direction in which the agent is turned
     */
    public Direction getOrientation() {
        return location.getOrientation();
    }

    /**
     * @return textual description of the agent
     * */
    public String toString() {
        return "Name : " + name + " " + location.toString() + " moves to do left " + movesToDoLeft;
    }


    /**
     * Try to perform an action in a given environment.
     * 
     * @param env the agent environment
     * @param attemptedAction the action to try
     */
    public void tryAction(AbstractEnvironment env, int attemptedAction) {

        // The agent must be idle to try a new action
        if (isIdle()) {
        	
        	// The agent can act. 
        	// If there is an attempted action and if that action is possible from this location. 

            if ((attemptedAction != AbstractEnvironment.ACTION_NONE)
                    && env.isActionPossibleFromLocation(getLocation(), attemptedAction)) {

                doMove(env.getOrientationForAction(attemptedAction));
            }
        }
    }

    /**
     * @return true if the agent is idle, false if it is busy
     */
    public boolean isIdle() {
        return movesToDoLeft == 0;
    }

    /**
     * Engages the agent in an action.
     * From the model point of view, the action is considered as initiated and requires a set of moves to be accomplished.
     * The location is updated in advance such that each new action request will take the new (discrete) location in account.
     * 
     * @param action performed action
     * */
    protected void doMove(Direction action) {
        location.doMove(action);
        movesToDoLeft = getMovesRequiredToChangeCell();
    }

    /** @return the number of moves to perform before changing of cell */
    public abstract int getMovesRequiredToChangeCell();

    /**
     * Continues the movement in progress.
     * */
    public void updateMove() {

        // If there is a movement in progress, we update the number of moves left to do
        if (movesToDoLeft > 0) {
            movesToDoLeft--;

        }
    }

    /**
     * Tests if an agent meets another one, which requires they share the same location.
     * 
     * @param other Other agent to test
     * @return true if the two agents are at the same location, false otherwise
     * */
    public boolean meets(Agent other) {
        return location.equals(other.getLocation());
    }

    /**
     * Gives the number of moves left to terminate an action.
     * Warning, the action effects of this movement are already applied (location change).
     * 
     * @return number of moves left
     */
    public int getMovesToDoLeft() {
        return movesToDoLeft;
    }

    /** Teleport the agent to a specified location.
     * @param loc the new location
     */
    public void teleportToLocation(GridMobile loc) {

        location.setTo(loc);
        movesToDoLeft = 0;
    }

    /**
     * Tests if the agent is in a given location.
     * 
     * @param loc the location to test
     * @return true if the agent is in this location, false otherwise
     */
    public boolean isIn(GridMobile loc) {
        return location.equals(loc);
    }
    
}
