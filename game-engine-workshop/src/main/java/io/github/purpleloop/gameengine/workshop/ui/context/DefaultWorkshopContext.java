package io.github.purpleloop.gameengine.workshop.ui.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** A simple workshop context. */
public class DefaultWorkshopContext implements WorkshopContext {

    /** A simple storage map. */
    private Map<String, Object> storage = new HashMap<>();

    @Override
    public <T> void store(String name, T object) {
        storage.put(name, object);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> retrieve(String name) {
        return Optional.ofNullable((T) storage.get(name));
    }

}
