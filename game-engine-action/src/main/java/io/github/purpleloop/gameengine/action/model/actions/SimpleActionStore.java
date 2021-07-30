package io.github.purpleloop.gameengine.action.model.actions;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/** Simple implementation of action storage. */
public class SimpleActionStore implements IActionStore {

    /* It's a start.
     * 
     * We work with simple actions, relative to keystrokes.
     * We can have several actions simultaneously (up + left).
     * 
     * Future improvement ideas : action priority, modifiers
     */

    /** Class logger. */
    private static final Log LOG = LogFactory.getLog(SimpleActionStore.class);

    /** Internal storage. */
    private Set<String> currentActions;

    /** Constructs the action store. */
    public SimpleActionStore() {
        this.currentActions = new HashSet<String>();
    }

    @Override
    public void addAction(String action) {

        if (!currentActions.contains(action)) {
            LOG.debug("Action : " + action);
            currentActions.add(action);
        }
    }

    @Override
    public Set<String> getCurrentActions() {
        return currentActions;
    }

    @Override
    public void forgetAll() {
        currentActions.clear();
    }

    @Override
    public void forgetAction(String action) {
        if (currentActions.contains(action)) {
            currentActions.remove(action);
        }
    }
    
}
