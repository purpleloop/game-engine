package io.github.purpleloop.gameengine.action.model.dialog;

/** Observer of a dialog. */
public interface DialogObserver {

    /** Dialog ended event. */
    int DIALOG_ENDED_EVENT = 0;

    /**
     * Notifies a change in the dialog.
     * 
     * @param dialogEvent the dialog event
     */
    void dialogChanged(int dialogEvent);

}
