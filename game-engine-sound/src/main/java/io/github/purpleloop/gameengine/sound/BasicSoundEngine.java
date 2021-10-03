package io.github.purpleloop.gameengine.sound;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.player.JOrbisPlayer;
import com.jcraft.player.JOrbisPlayerContext;
import com.jcraft.player.playlist.PlayListHolder;
import com.jcraft.player.playlist.SimplePlayList;

import io.github.purpleloop.commons.lang.ThreadObserver;
import io.github.purpleloop.gameengine.core.sound.interfaces.ISoundFileResolver;
import io.github.purpleloop.gameengine.core.sound.interfaces.SoundEngine;
import io.github.purpleloop.gameengine.core.util.EngineException;

/**
 * A basic sound engine.
 * 
 * The sound engine uses a set of threads to play sounds with a non blocking
 * behavior. As the number of thread is bounded, a request to play a sound while
 * every thread is busy leads to skip the sound in order to avoid overwhelming
 * the sound channels.
 */
public class BasicSoundEngine implements SoundEngine, ThreadObserver, JOrbisPlayerContext {

    /** Class logger. */
    private static Log log = LogFactory.getLog(BasicSoundEngine.class);

    /**
     * Number of threads that can be used to start simultaneously to play audio
     * data.
     */
    private static final int MAX_PLAYING_THREADS = 3;

    /** The WAV sound player. */
    private SoundPlayer wavPlayer;

    /** The provider of audio files. */
    private ISoundFileResolver config;

    /** The threads used to play sounds. */
    private List<SoundPlayThread> runningThreads;

    /** JOrbis player for background music. */
    private JOrbisPlayer jorbisPlayer;

    /** A playlist for background music. */
    private SimplePlayList playList;

    /**
     * Creates a sound engine with the given resolver.
     * 
     * @param soundConfig the sound file resolver
     */
    public BasicSoundEngine(ISoundFileResolver soundConfig) {

        this.wavPlayer = new SoundPlayer();
        this.config = soundConfig;
        this.runningThreads = new ArrayList<>(MAX_PLAYING_THREADS);
        this.playList = new SimplePlayList();
    }

    /** {@inheritDoc} */
    public void playSound(String soundName) {

        log.debug("Playing sound" + soundName);

        String soundFileName = config.getSoundFileName(soundName);

        if (runningThreads.size() > MAX_PLAYING_THREADS) {
            log.debug("Sound workers overhead ... skipping");
            return;
        }

        SoundPlayThread soundThread = new SoundPlayThread(wavPlayer, soundFileName);
        soundThread.addThreadObserver(this);
        soundThread.start();

        this.runningThreads.add(soundThread);
    }

    /** {@inheritDoc} */
    public void loopSound(String soundName) {
        log.info("Sound loop is not yet implemented");
    }

    @Override
    public void threadMessage(Thread sourceThread, String message) {
        // Nothing to do here
    }

    @Override
    public void threadDeath(Thread sourceThread) {
        if (this.runningThreads.contains(sourceThread)) {
            this.runningThreads.remove(sourceThread);
        }

    }

    @Override
    public void playBackgroundSound(File bgSoundFile) throws EngineException {

        if (this.jorbisPlayer != null && this.jorbisPlayer.hasActivePlayerThread()) {

            log.warn(
                    "Background music player was still active, we issue a stop and wait a bit before playing another sound.");

            stopBackgroundSound();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("Interrupted thread on delay after stopping playing background music", e);
                throw new EngineException(
                        "Interrupted thread of after stop playing background music", e);
            }

        }

        String absolutePath = bgSoundFile.getAbsolutePath();
        log.info("Play background file " + absolutePath);
        playList.add(absolutePath);
        this.jorbisPlayer = new JOrbisPlayer(this, playList);
        jorbisPlayer.playSound();
    }

    @Override
    public void handleEndOfPlay() {
        log.info("Background music play ended");
        jorbisPlayer = null;
        playList.clear();
    }

    @Override
    public void stopBackgroundSound() {
        if (this.jorbisPlayer != null && this.jorbisPlayer.hasActivePlayerThread()) {
            log.info("Stop background music player");
            jorbisPlayer.stopSound();
        }
    }

    @Override
    public PlayListHolder getPlayListHolder() {
        return this.playList;
    }

}
