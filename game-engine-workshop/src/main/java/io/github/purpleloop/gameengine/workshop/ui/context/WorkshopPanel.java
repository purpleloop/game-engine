package io.github.purpleloop.gameengine.workshop.ui.context;

import java.util.Optional;

import javax.swing.JPanel;

/** A base class for workshop panels. */
public class WorkshopPanel extends JPanel {

    /** Serial tag. */
    private static final long serialVersionUID = -8236099334479606234L;
    
    /** The workshop context. */
    private WorkshopContext workshopContext;

    /** Create a workshop panel. */
    public WorkshopPanel(WorkshopContext workshopContext) {
        this.workshopContext = workshopContext;
    }
    
    /** Stores an object in the context.
     * @param name the name used for storage
     * @param object the object to store
     */
    protected <T> void store(String name, T object) {
        workshopContext.store(name, object);
    }    

    /** Get an object by it's name if it exists in the context, optional */
    protected <T> Optional<T> retrieve(String string) {
        return workshopContext.retrieve(string);
    }
}
