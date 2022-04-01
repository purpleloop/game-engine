package io.github.purpleloop.gameengine.action.model.interfaces;

import io.github.purpleloop.gameengine.action.model.dialog.DialogController;

/** Models a dialog engine. */
public interface IDialogEngine {

    /** @return the dialog controller */
    DialogController getDialogController();

    /**
     * Select the dialog to prepare.
     * 
     * @param dialogId the dialog id
     */
    void selectDialog(String dialogId);

}
