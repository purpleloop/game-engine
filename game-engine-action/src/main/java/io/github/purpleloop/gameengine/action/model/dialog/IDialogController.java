package io.github.purpleloop.gameengine.action.model.dialog;

import io.github.purpleloop.gameengine.action.model.interfaces.IControllable;

/** An interface for dialog controllers. */
public interface IDialogController extends IControllable {

    /** Updates the dialog. */
    void update();

    /** @return the current prompt text to display */
    String getCurrentPrompt();

    /** @return the possible answers */
    String[] getPossibleAnswers();

    /** @return the index of the current choice */
    int getCurrentChoiceIndex();

}
