package io.github.purpleloop.gameengine.action.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** A thread used to refresh the view. */
public class RefreshThread extends Thread {

    /** Refresh rate in frame per second. */
    public static final int FPS = 25;

    /** Seconds per milliseconds. */
    public static final int SECONDS_PER_MS = 1000;

    /** View refresh in millis. */
    protected static final long VIEW_REFRESH_DELAY = SECONDS_PER_MS / FPS;

    /** Class logger. */
    private static final Log LOG = LogFactory.getLog(RefreshThread.class);

    /** The game panel to refresh. */
    private GamePanel gamePanel;

    /** Is the thread running ? */
    private boolean running;

    /** Creates a refresh thread for a given panel.
     * @param gamePanel the game panel to refresh
     */
    public RefreshThread(GamePanel gamePanel) {
        super("RefreshGameViewThread");
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        
        running = true;

        while (running) {

            try {
                Thread.sleep(VIEW_REFRESH_DELAY);
                gamePanel.repaint();
            } catch (InterruptedException e) {
                LOG.debug("Refresh thread sleep has been interrupted, so exiting the refresh loop", e);
                running = false;
            }
        }
        
        LOG.info("Refresh thread terminated");
    }

    /** Terminates the refresh thread. */
    public void terminate() {
        LOG.info("Refresh thread is required to terminate");
        running = false;        
    }

}
