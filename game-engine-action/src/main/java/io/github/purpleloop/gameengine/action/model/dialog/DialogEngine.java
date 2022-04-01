package io.github.purpleloop.gameengine.action.model.dialog;

import java.util.Map;

import io.github.purpleloop.gameengine.action.model.interfaces.IDialogEngine;
import io.github.purpleloop.gameengine.action.model.interfaces.IGameEngine;
import io.github.purpleloop.gameengine.core.util.EngineException;

/** A dialog engine. */
public class DialogEngine implements IDialogEngine {

    /** The dialog controller, handling a current dialog when required. */
    private DialogController dialogUI;

    /** A registry of dialogs. */
    private Map<String, Dialog> dialogs;

    /**
     * Constructor of the dialog engine.
     * 
     * @param gameEngine the game engine.
     * @throws EngineException in case of problems
     */
    public DialogEngine(IGameEngine gameEngine) throws EngineException {

        String dialogSetFileName = gameEngine.getConfig().getProperty("dialogSetFileName");
        this.dialogs = Dialog.loadFromXml(dialogSetFileName);
        this.dialogUI = new DialogController();
    }

    @Override
    public DialogController getDialogController() {
        return dialogUI;
    }

    @Override
    public void selectDialog(String dialogId) {
        Dialog dialog = dialogs.get(dialogId);
        dialogUI.setUpDialog(dialog);
    }

}
