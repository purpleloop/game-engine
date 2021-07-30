package io.github.purpleloop.gameengine.action.gui;

/** Abstraction of the user graphical interface for the game engine. */
public interface GameEngineUI {

    /**
     * @return the panel where to display the game view
     */
    GamePanel getGamePanel();

    /** Refresh the display of the user interface. */
    void repaint();

}
