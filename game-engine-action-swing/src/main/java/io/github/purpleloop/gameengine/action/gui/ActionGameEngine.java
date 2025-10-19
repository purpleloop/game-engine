package io.github.purpleloop.gameengine.action.gui;

import java.util.Optional;
import java.util.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.exception.PurpleException;
import io.github.purpleloop.commons.lang.ReflexivityTools;
import io.github.purpleloop.commons.lang.ThreadObserver;
import io.github.purpleloop.gameengine.action.gui.keyboard.KeyboardController;
import io.github.purpleloop.gameengine.action.model.GameThread;
import io.github.purpleloop.gameengine.action.model.interfaces.IController;
import io.github.purpleloop.gameengine.action.model.interfaces.IDialogEngine;
import io.github.purpleloop.gameengine.action.model.interfaces.IGameEngine;
import io.github.purpleloop.gameengine.action.model.interfaces.IGameView;
import io.github.purpleloop.gameengine.action.model.interfaces.ISession;
import io.github.purpleloop.gameengine.action.model.interfaces.ISessionEnvironment;
import io.github.purpleloop.gameengine.action.model.level.ILevelManager;
import io.github.purpleloop.gameengine.action.model.level.SingleLevelManager;
import io.github.purpleloop.gameengine.action.model.session.SessionFactory;
import io.github.purpleloop.gameengine.core.config.ClassRole;
import io.github.purpleloop.gameengine.core.config.GameConfig;
import io.github.purpleloop.gameengine.core.config.IDataFileProvider;
import io.github.purpleloop.gameengine.core.sound.interfaces.MutableSoundEngine;
import io.github.purpleloop.gameengine.core.util.EngineException;
import io.github.purpleloop.gameengine.sound.BasicSoundEngine;
import io.github.purpleloop.gameengine.sound.MutableSoundEngineAdapter;

/** The action game engine. */
public class ActionGameEngine implements IGameEngine, ThreadObserver {

    /** Class logger. */
    private static final Log LOG = LogFactory.getLog(ActionGameEngine.class);

    /** Parameter for the game update delay (in milliseconds). */
    private static final String GAME_SESSION_UPDATE_DELAY = "gameSessionUpdateDelay";

    /** Timer for displaying the welcome screen in case of inactivity. */
    protected Timer idleTimer;

    /** The game session in progress, if it exists. */
    private ISession gameSession;

    /** The game thread that manages the game session, if it exists. */
    private GameThread gameThd;

    /** The game view, where to display the visible elements of the game. */
    private IGameView gameView;

    /** The game controller for the local player input. */
    private IController gameController;

    /** The sound engine, for playing sounds through the local sound system. */
    private MutableSoundEngine soundEngine;

    /** The game configuration. */
    private GameConfig config;

    /** The local data file provider. */
    protected IDataFileProvider dataFileProvider;

    /** The user interface the game is associated to. */
    private GameEngineUI ui;

    /** The level manager. */
    private ILevelManager levelManager;

    /** An optional dialog engine. */
    private Optional<IDialogEngine> dialogEngineOptional;

    /**
     * Creates the action game engine.
     * 
     * @param df data file provider
     * @param confFileName configuration file name
     * @param ui user interface for game engine control and playing
     * @throws EngineException in case of problem
     */
    public ActionGameEngine(IDataFileProvider df, String confFileName, GameEngineUI ui)
            throws EngineException {

        this.ui = ui;
        this.dataFileProvider = df;
        this.config = null;

        try {
            config = GameConfig.parse(dataFileProvider, confFileName);
        } catch (EngineException e) {
            LOG.error("Error while reading an configuring the game engine", e);
            System.exit(-1);
        }

        gameThd = null;
        gameSession = null;

        // Sub configuration methods
        configureSoundEngine();
        configureView(ui);
        configureLevelManager();
        configureController();
        configureOptionalDialogEngine();

    }

    /** Configures the sound engine. */
    private void configureSoundEngine() {
        LOG.debug("Configuration of the sound engine");
        soundEngine = new MutableSoundEngineAdapter(new BasicSoundEngine(config));
    }

    /**
     * Configures the game controller.
     * 
     * @throws EngineException in case of problem
     */
    private void configureController() throws EngineException {

        LOG.debug("Configuration of the game controller");

        Class<?>[] paramClasses = new Class<?>[1];
        paramClasses[0] = IGameEngine.class;

        Object[] paramValues = new Object[1];
        paramValues[0] = this;

        String className = config.getClassName(ClassRole.CONTROLLER,
                KeyboardController.class.getCanonicalName());

        LOG.debug("The game controller class is :" + className);

        try {
            gameController = ReflexivityTools.createInstance(className, paramClasses, paramValues);
        } catch (PurpleException e) {
            throw new EngineException("Error while creating the game controller.", e);
        }
    }

    /**
     * Configures the game view.
     * 
     * @param ui the game engine user interface
     * @throws EngineException in case of problem
     */
    private void configureView(GameEngineUI ui) throws EngineException {

        LOG.debug("Configures the game view");

        Class<?>[] paramClasses = new Class<?>[3];
        paramClasses[0] = GameConfig.class;
        paramClasses[1] = IDataFileProvider.class;
        paramClasses[2] = GamePanel.class;

        Object[] paramValues = new Object[3];
        paramValues[0] = config;
        paramValues[1] = dataFileProvider;
        paramValues[2] = ui.getGamePanel();

        try {
            gameView = ReflexivityTools.createInstance(
                    config.getClassName(ClassRole.VIEW, BaseGameView.class.getCanonicalName()),
                    paramClasses, paramValues);
        } catch (PurpleException e) {
            throw new EngineException("Error while creating the game view.", e);
        }
    }

    /**
     * Configures the level manager.
     * 
     * @throws EngineException in case of problem
     */
    private void configureLevelManager() throws EngineException {
        Class<?>[] paramClassesLevelManager = new Class<?>[2];
        paramClassesLevelManager[0] = GameConfig.class;
        paramClassesLevelManager[1] = IDataFileProvider.class;

        Object[] paramValuesLevelManager = new Object[2];
        paramValuesLevelManager[0] = config;
        paramValuesLevelManager[1] = dataFileProvider;

        try {
            levelManager = ReflexivityTools.createInstance(
                    config.getClassName(ClassRole.LEVEL_PROVIDER,
                            SingleLevelManager.class.getCanonicalName()),
                    paramClassesLevelManager, paramValuesLevelManager);
        } catch (PurpleException e) {
            throw new EngineException("Error while creating the level manager.", e);
        }
    }

    /**
     * Configures the dialog engine, if needed.
     * 
     * @throws EngineException in case of problem
     */
    private void configureOptionalDialogEngine() throws EngineException {

        Optional<String> optionalClassName = config.getOptionalClassName(ClassRole.DIALOG_ENGINE);

        if (optionalClassName.isPresent()) {

            LOG.debug("Configuration of the dialog engine");

            Class<?>[] paramClasses = new Class<?>[1];
            paramClasses[0] = IGameEngine.class;

            Object[] paramValues = new Object[1];
            paramValues[0] = this;

            String className = optionalClassName.get();

            LOG.debug("The dialog engine class is : '" + className + "'");

            try {
                dialogEngineOptional = Optional
                        .of(ReflexivityTools.createInstance(className, paramClasses, paramValues));
            } catch (PurpleException e) {
                throw new EngineException("Error while creating the dialog engine.", e);
            }
        } else {

            LOG.debug("No dialog engine configured - ignore");
            dialogEngineOptional = Optional.empty();
        }

    }

    /**
     * @param ctrl the game controller used by the local player
     */
    public void setPlayerGameController(IController ctrl) {
        this.gameController = ctrl;
    }

    @Override
    public void startGame() throws EngineException {

        LOG.debug("Start game requested");

        if (!hasRunningGame()) {

            disableIdleMode();
            LOG.debug("Starting game ...");
            gameSession = SessionFactory.createSession(this);
            gameView.setSession(gameSession);
            gameThd = new GameThread(gameSession, config.getIntProperty(GAME_SESSION_UPDATE_DELAY));
            gameThd.setThreadObserver(this);
            gameThd.start();
        } else {

            LOG.info("Start game request rejected - Another game is still already running.");
            throw new EngineException(
                    "Unable to start a new game, another game thread is still running.");
        }
    }

    @Override
    public void stopGame() throws EngineException {

        LOG.debug("Stop game requested");

        if (hasRunningGame()) {
            LOG.debug("Stopping the game ...");
            gameThd.terminate();
            gameSession.cleanup();
            gameSession = null;
            gameView.setSession(null);
            ui.repaint();
        } else {
            LOG.info("Stop game request rejected - There is no running game to stop.");
            throw new EngineException("Unable to stop any game, no thread game is running.");
        }
    }

    @Override
    public void threadDeath(Thread sourceThread) {

        if (sourceThread == gameThd) {
            LOG.debug("The game terminates, switch to idle mode " + sourceThread);
            gameThd = null;
            setIdleMode();
        } else {
            LOG.error("Unexpected thread termination " + sourceThread);
        }
    }

    @Override
    public void threadMessage(Thread sourceThread, String message) {
    }

    /** {@inheritDoc} */
    public boolean pauseGame() {
        if (gameThd != null) {
            // Suspends current game thread
            LOG.debug("Game paused ...");
            return gameThd.pause();
        }
        return false;
    }

    /** {@inheritDoc} */
    public boolean hasRunningGame() {
        return gameThd != null;
    }

    /** {@inheritDoc} */
    public GameConfig getConfig() {
        return config;
    }

    /** {@inheritDoc} */
    public IGameView getView() {
        return gameView;
    }

    /** {@inheritDoc} */
    public MutableSoundEngine getSoundEngine() {
        return this.soundEngine;
    }

    /** {@inheritDoc} */
    public void playSound(String soundName) {
        soundEngine.playSound(soundName);
    }

    /** {@inheritDoc} */
    public void dumpObjects() {
        if (gameSession != null) {
            ISessionEnvironment environment = gameSession.getCurrentEnvironment();

            if (environment != null) {
                environment.dumpEnvironmentObjects();
            } else {
                LOG.debug("No active environment to dump.");
            }

        } else {
            LOG.debug("No active game session to dump.");
        }
    }

    /** {@inheritDoc} */
    public Optional<ISession> getSession() {

        if (gameSession != null) {
            return Optional.of(gameSession);
        }

        return Optional.empty();
    }

    /** {@inheritDoc} */
    public IController getController() {
        return gameController;
    }

    /** Remove the idle mode. */
    private void disableIdleMode() {
        LOG.debug("End of the idle mode");
    }

    /** Switches the engine in idle mode. */
    private void setIdleMode() {
        LOG.debug("Switching to idle mode");
    }

    /** Starts the game engine, put in idle mode. */
    public void startEngine() {
        LOG.debug("Starting the game engine.");
        setIdleMode();
    }

    /** {@inheritDoc} */
    @Override
    public ILevelManager getLevelManager() {
        return levelManager;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<IDialogEngine> getDialogEngine() {
        return dialogEngineOptional;
    }

}
