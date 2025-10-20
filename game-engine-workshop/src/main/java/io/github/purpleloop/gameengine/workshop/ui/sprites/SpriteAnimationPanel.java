package io.github.purpleloop.gameengine.workshop.ui.sprites;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.swing.sprites.exception.SpriteRenderingException;
import io.github.purpleloop.commons.swing.sprites.model.IndexedSpriteSet;
import io.github.purpleloop.commons.swing.sprites.model.SpriteModel;

/** A panel used to show sprite animations. */
public class SpriteAnimationPanel extends JPanel {

    /** The animation progression rate. */
    private static final double ANIMATION_PROGRESS_RATE = 0.1;

    /** Default animation delay. */
    private static final int DEFAULT_ANIMATION_DELAY = 50;

    /** Serial tag. */
    private static final long serialVersionUID = -8339354047667292952L;

    /** Class logger. */
    private static final Log LOG = LogFactory.getLog(SpriteAnimationPanel.class);

    /** Panel width. */
    private int panelWidth;

    /** Panel height. */
    private int panelHeight;

    /** Animation timer. */
    private Timer animationTimer;

    /** Progression in the animation cycle. */
    private double animationProgression;

    /** Current animation index. */
    private IndexedSpriteSet currentAnimationIndex = null;

    /** The underlying sprite model. */
    private SpriteModel spriteModel;

    /** Timer task used for animation. */
    private TimerTask currentAnimationTask;

    /** Animation delay. */
    private long animationDelay = DEFAULT_ANIMATION_DELAY;

    /** Number of frames in the animation. */
    private int frameCount = 1;

    /**
     * Constructor of the animation panel.
     * 
     * @param panelWidth width of the panel
     * @param panelHeight height of the panel
     */
    public SpriteAnimationPanel(int panelWidth, int panelHeight) {

        super();

        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.animationProgression = 0.0;

        // Creates an animation timer
        animationTimer = new Timer();

        setPreferredSize(new Dimension(panelWidth, panelHeight));
    }

    @Override
    public void paint(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, panelWidth, panelHeight);

        drawAnimatedSprite(g, 10, 10);

    }

    /**
     * @param graphics the graphic context
     * @param x ordinate for drawing the sprite
     * @param y abscissa for drawing the sprite
     */
    private void drawAnimatedSprite(Graphics graphics, int x, int y) {
        if (currentAnimationIndex != null) {

            LOG.debug("Animation index is " + currentAnimationIndex);

            try {

                int indexInSequence = Math.min((int) Math.floor(animationProgression * frameCount),
                        frameCount - 1);

                spriteModel.putSpriteForTime(graphics, this, currentAnimationIndex, indexInSequence,
                        x, y);
            } catch (SpriteRenderingException e) {
                LOG.error(e.getMessage());
            }
        }
    }

    /**
     * Set the animation index.
     * 
     * @param newAnimationIndex the index to animate
     */
    public void setIndexToAnimate(IndexedSpriteSet newAnimationIndex) {
        this.currentAnimationIndex = newAnimationIndex;
    }

    /** @param spriteModel the sprite model */
    public void setSpriteModel(SpriteModel spriteModel) {
        this.spriteModel = spriteModel;
    }

    /** Plays the animation. */
    public void play() {

        LOG.debug("Playing animation");
        if (currentAnimationTask != null) {
            LOG.info("Ignore play command, an animation is already playing");

            JOptionPane.showMessageDialog(this, "An animation is already playing");

            return;
        }

        if (currentAnimationIndex == null) {
            JOptionPane.showMessageDialog(this, "Please select an image as starting index first");
            return;
        }

        if (frameCount <= 2) {
            JOptionPane.showMessageDialog(this, "The number of frame is " + frameCount
                    + ", try to increase it to get an animation working");
        }

        // The animation task
        currentAnimationTask = new TimerTask() {

            @Override
            public void run() {

                animationProgression = animationProgression + ANIMATION_PROGRESS_RATE;
                if (animationProgression >= 1.0) {
                    animationProgression = 0.0;
                }

                repaint();
            }
        };
        animationTimer.scheduleAtFixedRate(currentAnimationTask, 0, animationDelay);

    }

    /** Stop the animation task. */
    public void stop() {
        if (currentAnimationTask != null) {
            LOG.debug("Stopping animation");
            currentAnimationTask.cancel();
            currentAnimationTask = null;
            animationTimer.purge();
        }
    }

    /** @param value the new animation delay */
    public void setAnimationDelay(int value) {
        this.animationDelay = value;
    }

    /**
     * Set the number of frame for the animation.
     * 
     * @param value the number of frames
     */
    public void setFrameCount(int value) {
        this.frameCount = value;
    }

}
