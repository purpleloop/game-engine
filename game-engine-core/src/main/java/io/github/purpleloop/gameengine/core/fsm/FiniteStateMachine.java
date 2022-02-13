package io.github.purpleloop.gameengine.core.fsm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Models a deterministic finite state machine (FSM).
 * 
 * <p>
 * This kind of models allow to represent systems with distinct states.
 * Transitions between states occurs on incoming events or facts. This is
 * typically a model to use for an automated systems or a basic agents. This may
 * be not enough for complex agents that would require knowledge inference, or
 * probabilistic computations, for instance.
 * </p>
 */
public class FiniteStateMachine {

    /** Logger of the class. */
    private static final Log LOG = LogFactory.getLog(FiniteStateMachine.class);

    /** The FSM nodes bindings with states. */
    private Map<MachineState, FSMNode> nodeBindings;

    /** The initial FSM node. */
    private FSMNode initialNode;

    /** The current FSM node. */
    private FSMNode currentNode;

    /** A set of current facts that will be consumed while processing. */
    private Set<MachineFact> currentFacts;

    /** Constructor of the finite state machine. */
    public FiniteStateMachine() {

        LOG.debug("Creating a new finite state machine");
        this.nodeBindings = new HashMap<>();
        this.currentFacts = new HashSet<>();
    }

    /**
     * Creates a new state in the machine.
     * 
     * @param state the state to add
     * @return the node bound to this state
     */
    public FSMNode newState(MachineState state) {
        FSMNode fsmNode = new FSMNode(state);
        this.nodeBindings.put(state, fsmNode);
        return fsmNode;
    }

    /**
     * Creates a new transition from a state to another one, on a specific guard
     * condition.
     * 
     * @param initialStateName the name of the initial state for the transition
     * @param finalStateName the name of the final state for the transition
     * @param guardFact the fact that need to be present to trigger the
     *            transition
     */
    public void newTransition(MachineState initialStateName, MachineState finalStateName,
            MachineFact guardFact) {

        FSMNode sourceNode = nodeBindings.get(initialStateName);
        FSMNode targetNode = nodeBindings.get(finalStateName);
        sourceNode.addTransition(guardFact, targetNode);
    }

    /**
     * Sets the initial state and resets the state machine (at this state).
     * 
     * @param state the initial state
     */
    public void setInitial(MachineState state) {
        this.initialNode = getNode(state);
        reset();
    }

    /**
     * Resets the machine at it's initial state. This resets also the current
     * facts.
     */
    public void reset() {
        this.currentNode = initialNode;
        this.currentFacts.clear();
    }

    /**
     * Get a FSM node from the given state.
     * 
     * @param state the requested state
     * @return the requested fsmNode
     */
    private FSMNode getNode(MachineState requestedState) {
        return nodeBindings.get(requestedState);
    }

    /**
     * Tests whether the finite state machine is in a given state.
     * 
     * @param testedState the state to test
     * @return true if the FSM node matches the given state, false otherwise
     */
    public boolean isInState(MachineState testedState) {
        return currentNode.isBoundToState(testedState);
    }

    /**
     * Make the finite state machine evolve from the current state to a new one,
     * following a transition whose condition is met.
     */
    public void process() {

        LOG.debug("Current state is " + currentNode.getState());

        FSMNode newNode = null;
        for (Entry<MachineFact, FSMNode> transition : currentNode.getTransitions().entrySet()) {

            MachineFact testedFact = transition.getKey();
            if (currentFacts.contains(testedFact)) {

                newNode = transition.getValue();
                LOG.debug("transition '" + testedFact + "' applies, new state is "
                        + newNode.getState());

                currentFacts.remove(testedFact);

            }

        }

        if (newNode != null) {
            this.currentNode = newNode;
        }

    }

    /** @return The current FSM node. */
    public FSMNode getCurrentNode() {
        return currentNode;
    }

    /**
     * Add a fact to the current facts.
     * 
     * @param fact the fact to add
     */
    public void addFact(MachineFact fact) {
        currentFacts.add(fact);
    }

}
