package io.github.purpleloop.gameengine.core.fsm;

import java.util.HashMap;
import java.util.Map;

/** Models the a finite state machine internal node, bound to a state. */
public class FSMNode {

    /** The associated of the state. */
    private MachineState state;

    /**
     * Deterministic transitions from this node to other ones, depending on
     * facts.
     */
    private Map<MachineFact, FSMNode> transitions;

    /**
     * Create an internal FSM node, associated to a state.
     * 
     * @param state The state
     */
    public FSMNode(MachineState state) {
        this.state = state;
        this.transitions = new HashMap<>();
    }

    /**
     * Add a transition that fires on a given fact and leads to an target node.
     * 
     * @param fact the guard of the transition
     * @param targetNode the target node
     */
    public void addTransition(MachineFact fact, FSMNode targetNode) {
        transitions.put(fact, targetNode);
    }

    /** @return the associated state */
    public MachineState getState() {
        return state;
    }

    /**
     * @param testedState the state to test
     * @return true if this node is bound to the given state
     */
    public boolean isBoundToState(MachineState testedState) {
        return state.equals(testedState);
    }

    /** @return the transitions from this this node */
    public Map<MachineFact, FSMNode> getTransitions() {
        return transitions;
    }

    @Override
    public String toString() {
        return "<" + state.name() + ">";
    }

}
