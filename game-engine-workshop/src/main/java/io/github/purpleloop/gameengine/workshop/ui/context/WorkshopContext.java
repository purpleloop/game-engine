package io.github.purpleloop.gameengine.workshop.ui.context;

import java.util.Optional;

/** A context for the workshop. */
public interface WorkshopContext {

    /** Stores an object in the context.
     * @param name the name used for storage
     * @param object the object to store
     */
    <T> void store(String name, T object);

    /** Get an object by it's name if it exists in the context, optional */
    <T> Optional<T> get(String name);

}
