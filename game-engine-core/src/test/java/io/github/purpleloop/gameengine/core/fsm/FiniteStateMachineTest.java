package io.github.purpleloop.gameengine.core.fsm;

import static org.junit.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

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
public class FiniteStateMachineTest {

    /** Logger of the class. */
    private static final Log LOG = LogFactory.getLog(FiniteStateMachineTest.class);

    enum LightState implements MachineState {

        /** FSM test state - The cord is unplugged. */
        UNPLUGGED("Power cord is unplugged"),

        /** FSM test state - The light is on. */
        ON("Light is on"),

        /** FSM test state - the light is off. */
        OFF("Light is off");

        private String description;

        LightState(String description) {
            this.description = description;
        }
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

    /** Tests a valid transition - Plug the power cord. */
    @Test
    public void testTransitionAppliesPlugCord() {

        FiniteStateMachine lightFsm = createFsm(LightState.UNPLUGGED);

        lightFsm.addFact(LightFact.PLUG);
        lightFsm.process();
        assertTrue(lightFsm.isInState(LightState.OFF));
    }

    /** Tests a valid transition - Unplug the power cord. */
    @Test
    public void testTransitionAppliesUnPlugCord() {

        FiniteStateMachine lightFsm = createFsm(LightState.OFF);
        lightFsm.addFact(LightFact.UNPLUG);
        lightFsm.process();
        assertTrue(lightFsm.isInState(LightState.UNPLUGGED));
    }

    /** Tests a valid transition - Turn on the light. */
    @Test
    public void testTransitionAppliesSwitchOn() {

        FiniteStateMachine lightFsm = createFsm(LightState.OFF);

        lightFsm.addFact(LightFact.SWITCH_ON);
        lightFsm.process();
        assertTrue(lightFsm.isInState(LightState.ON));
    }

    /** Tests a valid transition - Turn off the light. */
    @Test
    public void testTransitionAppliesSwitchOff() {

        FiniteStateMachine lightFsm = createFsm(LightState.ON);

        lightFsm.addFact(LightFact.SWITCH_OFF);
        lightFsm.process();
        assertTrue(lightFsm.isInState(LightState.OFF));
    }

    /** Tests a invalid transition - Can's turn off the light (twice). */
    @Test
    public void testTransitionDoesNotApplySwitchOff() {

        FiniteStateMachine lightFsm = createFsm(LightState.OFF);

        lightFsm.addFact(LightFact.SWITCH_OFF);
        lightFsm.process();
        assertTrue(lightFsm.isInState(LightState.OFF));
    }    
    
    /** Tests a complete cycle. */
    @Test
    public void testCompleteCycle() {

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
