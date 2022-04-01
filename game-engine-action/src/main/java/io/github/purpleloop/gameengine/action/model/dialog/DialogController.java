package io.github.purpleloop.gameengine.action.model.dialog;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.purpleloop.gameengine.action.model.actions.IActionStore;
import io.github.purpleloop.gameengine.action.model.actions.SimpleActionStore;
import io.github.purpleloop.gameengine.action.model.interfaces.IController;

/** The dialog UI manages user interactions during a dialog. */
public class DialogController implements IDialogController {

    /** A specific dialog node for starting a dialog. */
    private static final String START_ID = "start";

    /** A specific outcome for ending the dialog. */
    private static final String OUTCOME_END = "end";

    /** User action for validating the answer. */
    private static final String DIALOG_RIGHT = "right";

    /** User action for going up to the previous answer. */
    private static final String DIALOG_UP = "up";

    /** User action for going down to the next answer. */
    private static final String DIALOG_DOWN = "down";

    /** The current dialog. */
    private Dialog currentDialog;

    /** The current dialog node. */
    private DialogNode currentDialogNode;

    /** Current user choice index. */
    private int currentChoice = 0;

    /** Actions performed on the dialog. */
    private IActionStore actionStore;

    /** The observers of the dialog. */
    private Set<DialogObserver> observers;

    /** Creates a dialog controller. */
    public DialogController() {
        this.actionStore = new SimpleActionStore();
        this.observers = new HashSet<>();
    }

    /**
     * @param observer the observer to add
     */
    public void addObserver(DialogObserver observer) {
        observers.add(observer);
    }

    /** @param dialogObserver the observer to remove */
    public void removeObserver(DialogObserver dialogObserver) {
        observers.remove(dialogObserver);
    }

    /** @param controller a controller on the dialog UI */
    public void setController(IController controller) {
        controller.registerControlListener(this);
    }

    /** @param controller a controller on the dialog UI */
    public void removeController(IController controller) {
        controller.unRegisterControlListener(this);
    }

    @Override
    public IActionStore getActionStore() {
        return actionStore;
    }

    @Override
    public void drainActions() {
        this.actionStore.forgetAll();
    }

    @Override
    public int getCurrentChoiceIndex() {
        return currentChoice;
    }

    @Override
    public String getCurrentPrompt() {
        return currentDialogNode.getText();
    }

    @Override
    public String[] getPossibleAnswers() {
        List<DialogChoice> choices = currentDialogNode.getChoices();
        String[] answers = new String[choices.size()];
        int i = 0;
        for (DialogChoice choice : choices) {
            answers[i++] = choice.getText();
        }

        return answers;
    }

    @Override
    public void update() {

        Set<String> actionSet = actionStore.getCurrentActions();

        if (actionSet.contains(DIALOG_UP) && currentChoice > 0) {
            currentChoice--;
        }

        List<DialogChoice> choices = currentDialogNode.getChoices();

        if (actionSet.contains(DIALOG_DOWN) && currentChoice < (choices.size() - 1)) {
            currentChoice++;
        }

        if (actionSet.contains(DIALOG_RIGHT)) {

            DialogChoice choice = currentDialogNode.getChoices().get(currentChoice);

            String outcome = choice.getOutcome();
            if (outcome.equals(OUTCOME_END)) {
                fireDialogChanged(DialogObserver.DIALOG_ENDED_EVENT);
                currentDialog = null;
            } else {
                currentDialogNode = currentDialog.getNode(outcome);
            }

        }

        actionSet.clear();
    }

    /**
     * Fires a dialog event to the observers.
     * 
     * @param dialogEvent the dialog event to fire
     */
    private void fireDialogChanged(int dialogEvent) {
        for (DialogObserver obs : observers) {
            obs.dialogChanged(dialogEvent);
        }
    }

    /**
     * Prepare the controller for the given dialog.
     * 
     * @param dialog the dialog to use
     */
    public void setUpDialog(Dialog dialog) {
        currentDialog = dialog;

        if (currentDialog != null) {
            currentDialogNode = currentDialog.getNode(START_ID);
        }
    }

    /** @return true if there is a dialog in progress, false otherwise */
    public boolean hasDialogInProgress() {
        return currentDialog != null;
    }

}
