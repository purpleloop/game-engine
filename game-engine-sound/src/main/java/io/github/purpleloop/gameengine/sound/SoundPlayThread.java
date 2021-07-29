package io.github.purpleloop.gameengine.sound;

import io.github.purpleloop.commons.lang.ThreadObserver;

/** A thread used to play sounds. */
public class SoundPlayThread extends Thread {

    /** The name of the file to play. */
    private String soundFileName;

    /** The sound player. */
    private SoundPlayer soundPlayer;

    /** The thread observer. */
    private ThreadObserver threadObserver;

    /**
     * @param soundPlayer The sound player
     * @param soundFileName The name of the file to play.
     */
    public SoundPlayThread(SoundPlayer soundPlayer, String soundFileName) {
        super("SoundPlayThread");
        this.soundPlayer = soundPlayer;
        this.soundFileName = soundFileName;
    }

    @Override
    public void run() {
        super.run();
        this.soundPlayer.play(soundFileName);
        threadObserver.threadDeath(this);
    }

    /** @param threadObserver the thread observer to add */
    public void addThreadObserver(ThreadObserver threadObserver) {
        this.threadObserver = threadObserver;
    }

}
