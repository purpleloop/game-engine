package io.github.purpleloop.gameengine.core.fsm;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests on finite states machines (FSM).
 * 
 * <p>
 * We use a simple finite state machine model of an electric light device with a
 * power cord and a switch. We consider three states : unplugged, off, on. For
 * sake of simplicity, plug / unplug can occur only when device is off and
 * switch-on / switch-off can only occurs when plugged.
 * </p>
 * 
 */
class FiniteStateMachineTest {

    enum LightState implements MachineState {

        /** FSM test state - The cord is unplugged. */
        UNPLUGGED,

        /** FSM test state - The light is on. */
        ON,

        /** FSM test state - the light is off. */
        OFF;
    }

    enum LightFact implements MachineFact {

        /** Plug cord into outlet. */
        PLUG,

        /** Plug cord into outlet . */
        UNPLUG,

        /** Switch on the light. */
        SWITCH_ON,

        /** Switch off the light. */
        SWITCH_OFF;

    }

    /** Test on new transition - Unknown initial state. */
    @Test
    void testTransitionFromUnknownStateThrowsIllegalArgumentException() {

        FiniteStateMachine lightFsm = new FiniteStateMachine();

        assertThrows(IllegalArgumentException.class, () -> {
            lightFsm.newTransition(LightState.OFF, LightState.ON, LightFact.SWITCH_ON);
        });

    }

    /** Test on new transition - Unknown final state. */
    @Test
    void testTransitionToUnknownStateThrowsIllegalArgumentException() {

        FiniteStateMachine lightFsm = new FiniteStateMachine();
        lightFsm.newState(LightState.OFF);
        
        assertThrows(IllegalArgumentException.class, () -> {
            lightFsm.newTransition(LightState.OFF, LightState.ON, LightFact.SWITCH_ON);
        });

    }
    
    /** Tests a valid transition - Plug the power cord. */
    @Test
    void testTransitionAppliesPlugCord() {

        FiniteStateMachine lightFsm = createFsm(LightState.UNPLUGGED);

        lightFsm.addFact(LightFact.PLUG);
        lightFsm.process();
        assertTrue(lightFsm.isInState(LightState.OFF));
    }

    /** Tests a valid transition - Unplug the power cord. */
    @Test
    void testTransitionAppliesUnPlugCord() {

        FiniteStateMachine lightFsm = createFsm(LightState.OFF);
        lightFsm.addFact(LightFact.UNPLUG);
        lightFsm.process();
        assertTrue(lightFsm.isInState(LightState.UNPLUGGED));
    }

    /** Tests a valid transition - Turn on the light. */
    @Test
    void testTransitionAppliesSwitchOn() {

        FiniteStateMachine lightFsm = createFsm(LightState.OFF);

        lightFsm.addFact(LightFact.SWITCH_ON);
        lightFsm.process();
        assertTrue(lightFsm.isInState(LightState.ON));
    }

    /** Tests a valid transition - Turn off the light. */
    @Test
    void testTransitionAppliesSwitchOff() {

        FiniteStateMachine lightFsm = createFsm(LightState.ON);

        lightFsm.addFact(LightFact.SWITCH_OFF);
        lightFsm.process();
        assertTrue(lightFsm.isInState(LightState.OFF));
    }

    /** Tests a invalid transition - Can's turn off the light (twice). */
    @Test
    void testTransitionDoesNotApplySwitchOff() {

        FiniteStateMachine lightFsm = createFsm(LightState.OFF);

        lightFsm.addFact(LightFact.SWITCH_OFF);
        lightFsm.process();
        assertTrue(lightFsm.isInState(LightState.OFF));
    }

    /** Tests a complete cycle. */
    @Test
    void testCompleteCycle() {

        FiniteStateMachine lightFsm = createFsm(LightState.UNPLUGGED);

        lightFsm.addFact(LightFact.PLUG);
        lightFsm.process();

        lightFsm.addFact(LightFact.SWITCH_ON);
        lightFsm.process();

        lightFsm.addFact(LightFact.SWITCH_OFF);
        lightFsm.process();
        lightFsm.addFact(LightFact.UNPLUG);
        lightFsm.process();

        assertTrue(lightFsm.isInState(LightState.UNPLUGGED));
    }

    /**
     * Creates a finite state machine to model the light system state.
     * 
     * @param initialState the initial state
     * @return the finite state machine
     */
    private FiniteStateMachine createFsm(LightState initialState) {
        FiniteStateMachine lightFsm = new FiniteStateMachine();
        lightFsm.newState(LightState.UNPLUGGED);
        lightFsm.newState(LightState.OFF);
        lightFsm.newState(LightState.ON);
        lightFsm.newTransition(LightState.UNPLUGGED, LightState.OFF, LightFact.PLUG);
        lightFsm.newTransition(LightState.OFF, LightState.UNPLUGGED, LightFact.UNPLUG);
        lightFsm.newTransition(LightState.OFF, LightState.ON, LightFact.SWITCH_ON);
        lightFsm.newTransition(LightState.ON, LightState.OFF, LightFact.SWITCH_OFF);
        lightFsm.setInitial(initialState);
        return lightFsm;
    }

}
