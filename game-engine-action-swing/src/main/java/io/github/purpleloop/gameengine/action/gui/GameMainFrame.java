package io.github.purpleloop.gameengine.action.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.swing.GraphicDeviceManager;
import io.github.purpleloop.gameengine.action.model.interfaces.IController;
import io.github.purpleloop.gameengine.action.model.interfaces.IGameEngine;
import io.github.purpleloop.gameengine.core.config.LocalDataFileProvider;

/** This class implements a main Swing JFrame as a game interface. */
public class GameMainFrame extends JFrame implements KeyListener {

    /** Serial tag. */
    private static final long serialVersionUID = 1L;

    /** Class logger. */
    private static final Log LOG = LogFactory.getLog(GameMainFrame.class);

    /** The graphic device manager used to adjust the screen resolution. */
    private GraphicDeviceManager graphicDeviceManager;

    /** The main panel principal. */
    private MainPanel mainPanel;

    /** The delegated key listener. */
    private KeyListener delegatedKeyListener;

    /** The delegated mouse listener. */
    private MouseListener delegatedMouseListener;

    /**
     * The constructor of the game main frame.
     * 
     * @param configFileName the configuration file name
     * @param fullScreen is full screen mode requested ?
     */
    public GameMainFrame(String configFileName, boolean fullScreen) {
        super("Game Engine");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Asks the user for the choice of a screen mode
        graphicDeviceManager = GraphicDeviceManager.getInstance();
        graphicDeviceManager.configureScreenMode();

		// Setup a handler for restoring the display if necessary
		// when the game window is closed.
        WindowListener windowListener = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

                mainPanel.stopRefresh();
                graphicDeviceManager.restoreDisplay();

                LOG.debug("Normal termination of the game engine application, exiting the JVM.");
                System.exit(0);
            }
        };
        addWindowListener(windowListener);

        mainPanel = new MainPanel(new LocalDataFileProvider(), configFileName);
        setContentPane(mainPanel);

        IGameEngine gameEngine = mainPanel.getGameEngine(); 
        IController gameController = gameEngine.getController();

        if (gameController instanceof KeyListener) {
            delegatedKeyListener = (KeyListener) gameController;
        }

		// Prepares the key listener
		// This is important to do this here, especially when switching in full screen
        addKeyListener(this);

        if (gameController instanceof MouseListener) {
            delegatedMouseListener = (MouseListener) gameController;
            addMouseListener(delegatedMouseListener);
        }

        graphicDeviceManager.initDisplayUsingFrame(this,true);

        setVisible(true);
        requestFocus();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_ESCAPE) && (!mainPanel.getGameEngine().hasRunningGame())) {

            LOG.info("Escape key has been pressed - Closing the main frame ..."); 
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } else if (delegatedKeyListener != null) {
            delegatedKeyListener.keyPressed(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (delegatedKeyListener != null) {
            delegatedKeyListener.keyTyped(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (delegatedKeyListener != null) {
            delegatedKeyListener.keyReleased(e);
        }
    }

    /**
     * Game application entry point.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {

        LOG.info("Launching the game ...");

        LaunchingParameters params = new LaunchingParameters();
        params.analyseParameters(args);
        
        if (params.hasErrors()) {
            System.exit(1);
        }

        new GameMainFrame(params.getConfigFileName(), params.isFullScreen());
		LOG.info("Game main fram is ready");
    }


}
