package io.github.purpleloop.gameengine.sound;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.gameengine.core.sound.interfaces.MutableSoundEngine;
import io.github.purpleloop.gameengine.core.sound.interfaces.SoundEngine;
import io.github.purpleloop.gameengine.core.util.EngineException;

/** Class adapter for the sound engine that allows to mute/unmute sounds. */
public class MutableSoundEngineAdapter implements MutableSoundEngine {

	/** Muted sound indication. */
    private static final String SOUND_IS_MUTED_INFO = "Sound is muted - playing silently ";

    /** Class logger. */
    private static Log log = LogFactory.getLog(MutableSoundEngineAdapter.class);

    /** Can the engine play sounds ? */
    private boolean canPlaySounds = true;

    /** The delegate sound engine. */
    private SoundEngine delegate;

    /**
     * Creates a mutable sound engine from another one.
     * 
     * @param simpleEngine the delegate sound engine
     */
    public MutableSoundEngineAdapter(SoundEngine simpleEngine) {
        this.delegate = simpleEngine;
    }

    @Override
    public void playSound(String soundName) {

        if (!canPlaySounds) {
            log.debug(SOUND_IS_MUTED_INFO + soundName);
            return;
        }

        delegate.playSound(soundName);
    }

    @Override
    public void loopSound(String soundName) {
        if (!canPlaySounds) {
            log.debug(SOUND_IS_MUTED_INFO + soundName);
            return;
        }

        delegate.loopSound(soundName);
    }


    /** {@inheritDoc} */
    public void switchSounds() {
        canPlaySounds = !canPlaySounds;
        log.debug("Sounds : " + canPlaySounds);
    }

    /** {@inheritDoc} */
    public void enableSounds(boolean mode) {
        canPlaySounds = mode;
    }

    /** {@inheritDoc} */
    public boolean canPlaySounds() {
        return canPlaySounds;
    }

    @Override
    public void playBackgroundSound(File bgSoundFile) throws EngineException {
        if (canPlaySounds) {
            delegate.playBackgroundSound(bgSoundFile);
        }
    }

    @Override
    public void stopBackgroundSound() {
        if (canPlaySounds) {
            delegate.stopBackgroundSound();
        }
    }

}
