package io.github.purpleloop.gameengine.sound;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.gameengine.core.sound.interfaces.MutableSoundEngine;

/**
 * Dummy class that can be used to mock a sound engine.
 * We only log what should be done (useful late at night).
 */
public class DummySoundEngine implements MutableSoundEngine {

    /** Class logger. */
    private static final Log LOG = LogFactory.getLog(DummySoundEngine.class);

    /** {@inheritDoc} */
    public void playSound(String soundName) {
        LOG.debug("Dummy playing sound " + soundName);
    }

    /** {@inheritDoc} */
    public void loopSound(String soundName) {
        LOG.debug("Dummy looping sound " + soundName);

    }

    /** {@inheritDoc} */
    public void enableSounds(boolean mode) {
        LOG.debug("Dummy setting sounds to " + mode);

    }

    /** {@inheritDoc} */
    public boolean canPlaySounds() {
        return false;
    }

    /** {@inheritDoc} */
    public void switchSounds() {
    }

}
