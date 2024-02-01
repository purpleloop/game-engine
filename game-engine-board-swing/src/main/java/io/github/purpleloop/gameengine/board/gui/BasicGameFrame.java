package io.github.purpleloop.gameengine.board.gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.swing.SwingUtils;
import io.github.purpleloop.commons.ui.UiMessageType;
import io.github.purpleloop.gameengine.board.gui.dialog.PlayerSetupDialog;
import io.github.purpleloop.gameengine.board.model.AbstractBoardGame;
import io.github.purpleloop.gameengine.board.model.PlayerType;
import io.github.purpleloop.gameengine.board.model.SimplePlayerInfo;
import io.github.purpleloop.gameengine.board.model.exception.BoardGameException;
import io.github.purpleloop.gameengine.board.model.interfaces.IBoardGameUI;
import io.github.purpleloop.gameengine.board.model.interfaces.IPlayerInfo;
import io.github.purpleloop.gameengine.core.util.Message;

/** Base class for board game Swing frame interfaces. */
public abstract class BasicGameFrame extends JFrame implements ActionListener, IBoardGameUI {

    /** Serial tag. */
    private static final long serialVersionUID = 3902861172605004054L;

    /** Width of the communication area. */
    public static final int COM_AREA_HEIGHT = 10;

    /** Height of the communication area. */
    public static final int COM_AREA_WIDTH = 20;

    /** Class logger. */
    public static final Log LOG = LogFactory.getLog(BasicGameFrame.class);

    /** Command to start a new game. */
    protected static final String CMD_NEW_GAME = "CMD_NEW_GAME";

    /** Command to stop current game. */
    protected static final String CMD_STOP_GAME = "CMD_STOP_GAME";

    /** Command to load an existing game. */
    protected static final String CMD_LOAD_GAME = "CMD_LOAD_GAME";

    /** Command to save the current game. */
    protected static final String CMD_SAVE_GAME = "CMD_SAVE_GAME";

    /** Command to quit game. */
    protected static final String CMD_QUIT = "CMD_QUIT";

    /** Command to undo an action. */
    protected static final String CMD_UNDO = "CMD_UNDO";

    /** Command to configure players. */
    protected static final String CMD_SETUP_PLAYERS = "CMD_SETUP_PLAYERS";

    /** Command to pass the current turn. */
    protected static final String CMD_PASS = "CMD_PASS";

    /** Command to advise a move. */
    protected static final String CMD_ADVISE = "CMD_ADVISE";

    /** Binary kilo multiplier ... */
    private static final long BIARY_KILO_UNIT_MULTIPLIER = 1024;

    /** Number of steps do move the cursor in an advise show. */
    private static final double NUMBER_OF_STEPS_FOR_CURSOR_IN_ADVICE_SHOW = 50;

    /** Delay for moving the cursor in an advise show (in milliseconds). */
    private static final int DELAU_BEFORE_MOVING_CURSOR_IN_ADVICE_SHOW = 25;

    /** The board game. */
    private AbstractBoardGame game;

    /** Message area. */
    private JTextArea messageArea;

    /** Game status label. */
    private JLabel gameStatusLabel;

    /** Score label. */
    private JLabel scoreLabel;

    /** Label showing current selection. */
    private JLabel currentSelectionLabel;

    /**
     * Creates the game frame.
     * 
     * @throws BoardGameException exception in case of problem
     */
    protected BasicGameFrame() throws BoardGameException {
        super();
        setTitle(getGameName());

        // The main panel
        JPanel mainPanel = new JPanel();
        setContentPane(mainPanel);
        mainPanel.setLayout(new BorderLayout());

        // Information panel
        JPanel infoPanel = new JPanel();
        gameStatusLabel = new JLabel("");
        gameStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoPanel.add(gameStatusLabel);
        scoreLabel = new JLabel("");
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoPanel.add(scoreLabel);
        currentSelectionLabel = new JLabel("");
        currentSelectionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoPanel.add(currentSelectionLabel);
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Specific panel for the game played in the engine
        JPanel hostedGameArea = setupGamePanel();
        mainPanel.add(
                new JScrollPane(hostedGameArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED),
                BorderLayout.CENTER);

        // Commands
        JPanel commandPanel = new JPanel();

        commandPanel.setLayout(new BoxLayout(commandPanel, BoxLayout.Y_AXIS));

        JPanel commonCmdPanel = new JPanel();
        SwingUtils.createButton(Message.getMessage("action.pass"), CMD_PASS, commonCmdPanel, this,
                true);
        SwingUtils.createButton(Message.getMessage("action.advise"), CMD_ADVISE, commonCmdPanel,
                this, true);
        SwingUtils.createButton(Message.getMessage("action.undo"), CMD_UNDO, commonCmdPanel, this,
                true);

        commandPanel.add(commonCmdPanel);

        setupCommands(commandPanel);

        mainPanel.add(commandPanel, BorderLayout.EAST);

        messageArea = new JTextArea(COM_AREA_HEIGHT, COM_AREA_WIDTH);
        mainPanel.add(new JScrollPane(messageArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.SOUTH);

        // Composition of menus
        JMenuBar menus = new JMenuBar();
        JMenu mGame = new JMenu(Message.getMessage("menu.game"));
        SwingUtils.addMenuItem(Message.getMessage("action.new"), CMD_NEW_GAME, this, mGame);
        SwingUtils.addMenuItem(Message.getMessage("action.stop"), CMD_STOP_GAME, this, mGame);
        SwingUtils.addMenuItem(Message.getMessage("action.open_game"), CMD_LOAD_GAME, this, mGame);
        SwingUtils.addMenuItem(Message.getMessage("action.save_game"), CMD_SAVE_GAME, this, mGame);
        SwingUtils.addMenuItem("Quitter", CMD_QUIT, this, mGame);
        menus.add(mGame);

        JMenu menuOptions = new JMenu(Message.getMessage("menu.options"));
        menus.add(menuOptions);

        SwingUtils.addMenuItem(Message.getMessage("action.setup_players"), CMD_SETUP_PLAYERS, this,
                menuOptions);
        SwingUtils.addMenuItem(Message.getMessage("action.advise"), CMD_ADVISE, this, menuOptions);
        SwingUtils.addMenuItem(Message.getMessage("action.undo"), CMD_UNDO, this, menuOptions);

        // Extra menu contributions menus
        setupMenus(menus);
        setJMenuBar(menus);

        setupDefaultPlayers();

        pack();
    }

    /**
     * Associates a game to this frame.
     * 
     * @param game the game to associate
     */
    protected final void setGame(AbstractBoardGame game) {
        this.game = game;
    }

    /** @return the name of the game frame. */
    protected String getGameName() {
        return Message.getMessage("game.noname");
    }

    /**
     * Setup extra menus.
     * 
     * @param menus menu bar to complete
     */
    protected void setupMenus(JMenuBar menus) {
    }

    /**
     * Setup extra commands.
     * 
     * @param commandPanel a panel for additional commands
     */
    protected void setupCommands(JPanel commandPanel) {
    }

    /**
     * Setup the game panel.
     * 
     * @return the game panel
     */
    protected abstract JPanel setupGamePanel() throws BoardGameException;

    @Override
    public final void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();

        if (command != null) {

            try {
                if (command.equals(CMD_NEW_GAME)) {
                    newGame();
                } else if (command.equals(CMD_STOP_GAME)) {
                    stopGame();
                } else if (command.equals(CMD_LOAD_GAME)) {
                    loadGame();
                } else if (command.equals(CMD_SAVE_GAME)) {
                    saveGame();
                } else if (command.equals(CMD_SETUP_PLAYERS)) {

                    setupPlayers();

                } else if (command.equals(CMD_QUIT)) {

                    stopGame();

                } else if (!processCommand(command)) {
                    displayMessage(UiMessageType.ERROR,
                            Message.getMessage("command.unknown", command));
                    LOG.error("The command '" + command + "' is not managed");
                }

            } catch (BoardGameException ex) {
                LOG.error("An exception occurred when processing the command.", ex);
            }
        } else {
            LOG.debug("Action without command from " + e.getSource());
        }

    }

    /**
     * Handles a command.
     * 
     * @param command command to process
     * @return true if the command has been processed, false otherwise
     * @throws BoardGameException if an exception occurred
     */
    protected boolean processCommand(String command) throws BoardGameException {
        return false;
    }

    /**
     * Updates interface with informations on status.
     * 
     * @param status status
     */
    public void setStatus(String status) {
        Runtime rt = Runtime.getRuntime();
        long free = rt.freeMemory();
        gameStatusLabel.setText(status + "|"
                + Message.getMessage("info.free_mem", free / BIARY_KILO_UNIT_MULTIPLIER));
    }

    /**
     * Display score informations.
     * 
     * @param score the score to display
     */
    public void displayScore(String score) {
        scoreLabel.setText(score);
    }

    /**
     * Display informations about the current selection.
     * 
     * @param currentSelectionInfo informations about the current selection
     */
    public void setSelectionInfo(String currentSelectionInfo) {
        currentSelectionLabel.setText(currentSelectionInfo);
    }

    /**
     * Put the focus on the point in (x,y) by moving the cursor at these
     * coordinates.
     * 
     * @param destX abscissa
     * @param destY ordinate
     */
    public void putFocusOnPoint(double destX, double destY) {

        Robot bot;
        try {

            LOG.debug(destX + "," + destY);

            Point current = MouseInfo.getPointerInfo().getLocation();
            double ddx = (destX - current.x) / NUMBER_OF_STEPS_FOR_CURSOR_IN_ADVICE_SHOW;
            double ddy = (destY - current.y) / NUMBER_OF_STEPS_FOR_CURSOR_IN_ADVICE_SHOW;

            double nx = current.x;
            double ny = current.y;

            bot = new Robot();
            for (int i = 0; i < NUMBER_OF_STEPS_FOR_CURSOR_IN_ADVICE_SHOW; i++) {

                nx = nx + ddx;
                ny = ny + ddy;
                bot.mouseMove((int) (nx), (int) (ny));
                bot.delay(DELAU_BEFORE_MOVING_CURSOR_IN_ADVICE_SHOW);
            }

        } catch (AWTException e) {
            LOG.error("Failed to move the cursor in (" + destX + "," + destY + ") : ", e);
        }
    }

    @Override
    public void displayMessage(String message) {
        displayMessage(UiMessageType.TRACE, message);
    }

    @Override
    public void displayMessage(UiMessageType type, String message) {
        SwingUtils.displayMessage(this, type, message, messageArea);

    }

    /**
     * Setup a default player configuration to allow a quick start of the game.
     */
    protected void setupDefaultPlayers() {

        List<IPlayerInfo> playerList = new ArrayList<>();

        for (int playerIndex = 0; playerIndex < game.getMinPlayers(); playerIndex++) {
            playerList.add(new SimplePlayerInfo("dummy" + playerIndex, PlayerType.HUMAIN));
        }
        try {
            game.setPlayers(playerList);
        } catch (BoardGameException e) {
            LOG.error("Error while setting default players", e);
        }
    }

    /** Configure players for the game. */
    protected final void setupPlayers() {

        if (game.isActive()) {
            displayMessage(UiMessageType.INFO, Message.getMessage("requires.game.idle"));
            return;
        }

        PlayerSetupDialog playerSetupDialog = new PlayerSetupDialog(this, game);
        playerSetupDialog.setModal(true);
        playerSetupDialog.setVisible(true);

        try {
            if (playerSetupDialog.isFormValid()) {
                List<IPlayerInfo> playerList = playerSetupDialog.getPlayerInfoList();
                game.setPlayers(playerList);
            }
        } catch (BoardGameException e) {
            LOG.error("Unable to setup platers", e);
            displayMessage(UiMessageType.ERROR,
                    Message.getMessage("error.cannot.apply.because", e.getMessage()));
        } finally {
            playerSetupDialog.dispose();
        }
    }

    /**
     * Save the game state.
     */
    public void saveGame() {

        if (!game.isActive()) {
            displayMessage(UiMessageType.INFO, Message.getMessage("requires.game.active"));
            return;
        }

        JFileChooser fileChooser = new JFileChooser(".");
        int fileChoosingResult = fileChooser.showSaveDialog(this);
        if (fileChoosingResult == JFileChooser.APPROVE_OPTION) {
            try {
                game.save(fileChooser.getSelectedFile());
            } catch (BoardGameException e) {
                LOG.error("Failed to save the game", e);
                displayMessage(UiMessageType.ERROR, "Unable to save the game :\n" + e.getMessage());
            }
        }
    }

    /**
     * Load the game state.
     */
    public void loadGame() {

        if (game.isActive()) {
            displayMessage(UiMessageType.INFO, Message.getMessage("requires.game.active"));
            return;
        }

        JFileChooser fileChooser = new JFileChooser(".");
        int fileChoosingResult = fileChooser.showOpenDialog(this);
        if (fileChoosingResult == JFileChooser.APPROVE_OPTION) {

            try {
                game.load(fileChooser.getSelectedFile());
            } catch (BoardGameException e) {
                LOG.error("Game loading failed", e);
                displayMessage(UiMessageType.ERROR,
                        Message.getMessage("error.cannot.apply.because", e.getMessage()));
            }
        }
    }

    /** Create a new game. */
    public void newGame() {

        LOG.debug("New game requested");

        if (game.isActive()) {
            displayMessage(UiMessageType.INFO,
                    "A game is active, please terminate it before creating another one ...");

            return;
        }

        try {
            // FIXME To be handled in a BoardGameEngine
            game.newGame();

            Thread gameThread = new Thread(game, "GameThread");
            gameThread.start();

        } catch (BoardGameException e) {
            LOG.error("The creation of the new game has failed.", e);
            displayMessage(UiMessageType.ERROR,
                    Message.getMessage("error.cannot.apply.because", e.getMessage()));
        }

    }

    /** Stop the current game. */
    protected void stopGame() {

        LOG.debug("Stop game requested");

        if (!game.isActive()) {
            displayMessage(UiMessageType.INFO, Message.getMessage("requires.game.active"));
            return;
        }
        game.terminate();
    }

    @Override
    public void dispose() {
        if (game.isActive()) {
            game.terminate();
        }
        super.dispose();
    }

}
