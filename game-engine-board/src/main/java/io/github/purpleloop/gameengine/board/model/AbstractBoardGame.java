package io.github.purpleloop.gameengine.board.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.swing.SwingUtils;
import io.github.purpleloop.gameengine.board.model.exception.BoardGameException;
import io.github.purpleloop.gameengine.board.model.interfaces.IBoardGameUI;
import io.github.purpleloop.gameengine.board.model.interfaces.IBoardPlay;
import io.github.purpleloop.gameengine.board.model.interfaces.IPlayerInfo;
import io.github.purpleloop.gameengine.board.model.interfaces.IPlayerRole;
import io.github.purpleloop.gameengine.core.util.Message;

/**
 * This abstract class manages the run of a board game within a game thread.
 */
public abstract class AbstractBoardGame implements Runnable {

    /** The class logger. */
    public static final Log LOG = LogFactory.getLog(AbstractBoardGame.class);

    /** Delay between turns (in milliseconds). */
    private static final long THREAD_SLEEP_DELAY = 1000;

    /** Abstract model of the game board. */
    protected AbstractBoard board;

    /** Set of game observers. */
    private Set<GameObserver> gameObservers;

    /** The board game user interface for a local player. */
    protected IBoardGameUI gameUi;

    /** Internal state of the game. */
    private BoardGameState state;

    /** List of all players. */
    protected List<IPlayerInfo> players;

    /** The current player. */
    protected IPlayerInfo currentPlayer;

    /** History of played actions. */
    protected Stack<IBoardPlay> history;

    /** Counter of played turns. */
    private int turnCounter;

    /**
     * Counter of players that passed in the current turn. The game ends if all
     * player pass in the same turn.
     */
    protected int passCounter;

    /** Role of each player in the game. */
    private Map<IPlayerInfo, IPlayerRole> roleMap;

    /**
     * Constructor of an abstract board game.
     * 
     * @param gameUi The local user interface
     * @param board the game board
     */
    protected AbstractBoardGame(IBoardGameUI gameUi, AbstractBoard board) {

        LOG.debug("Creating a new board game");

        this.gameObservers = new HashSet<>();
        this.turnCounter = 1;
        this.gameUi = gameUi;
        this.players = new ArrayList<>();
        this.roleMap = new HashMap<>();
        this.board = board;

        // Sets the game as idle
        setState(BoardGameState.IDLE);
    }

    /**
     * Changes the game state.
     * 
     * @param newState the new state
     */
    protected final synchronized void setState(BoardGameState newState) {
        this.state = newState;
        LOG.debug("Game state changed to " + this.state.getName());
        gameUi.setStatus(Message.getMessage("state.name." + this.state.getName()));
    }

    /**
     * @return the current game state
     */
    public final synchronized BoardGameState getState() {
        return state;
    }

    /**
     * @return the minimal number of players for the game, must be positive
     */
    public abstract int getMinPlayers();

    /**
     * @return the maximal number of players for the game, must be positive
     */
    public abstract int getMaxPlayers();

    /**
     * @return the maximal number of turns for the game, must be positive
     */
    public abstract int getMaxTurns();

    /**
     * Registers the players.
     * 
     * @param playerInfos list of players
     * @throws BoardGameException in case of problems
     */
    public final synchronized void setPlayers(List<IPlayerInfo> playerInfos)
            throws BoardGameException {

        LOG.debug("Setting up the player list");

        if (isActive()) {
            throw new BoardGameException(Message.getMessage("requires.game.idle"));
        }

        players.clear();
        players.addAll(playerInfos);
    }

    /**
     * @return list of all players of this game
     */
    public final synchronized List<IPlayerInfo> getPlayers() {
        return players;
    }

    /**
     * @return the current player
     */
    public final IPlayerInfo getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets the role played by the given player.
     * 
     * @param player the requested player
     * @return the role played by this player in the game
     * @throws BoardGameException in case no role is bound to the given player
     */
    public final IPlayerRole getRoleForPlayer(IPlayerInfo player) throws BoardGameException {
        IPlayerRole iPlayerRole = roleMap.get(player);

        if (iPlayerRole == null) {
            throw new BoardGameException("No role is mapped for player " + player);
        }

        return iPlayerRole;
    }

    /**
     * Get the role with the given index.
     * 
     * @param index role index
     * @return name of the role
     */
    public String getRoleName(int index) {
        return "Role " + index;
    }

    /**
     * @return is this the turn of an human player ?
     */
    public final synchronized boolean isHumanTurn() {
        return currentPlayer.isHumain();
    }

    /**
     * @return numbers of elapsed turns
     */
    protected final int getNumTour() {
        return turnCounter;
    }

    /** Handles the end of play turn for a player. */
    protected final void endOfPlay() {

        LOG.debug("End of turn for the player " + currentPlayer);
        int indexOfCurrentPlayer = players.indexOf(currentPlayer);
        indexOfCurrentPlayer++;
        if (indexOfCurrentPlayer == players.size()) {
            nextPlayTurn();
            indexOfCurrentPlayer = 0;
        }
        currentPlayer = players.get(indexOfCurrentPlayer);
        fireGameChanged();
    }

    /**
     * @return true if the game is running, false otherwise
     */
    public final synchronized boolean isActive() {
        return state == BoardGameState.INGAME;
    }

    /**
     * Handles the play of a player.
     * 
     * @param play the play to handle
     * @return true if the play was successful, false otherwise
     */
    public final boolean handlePlay(IBoardPlay play) {

        if (!isActive()) {
            // No game is running
            return false;
        }

        if (!isHumanTurn()) {
            gameUi.displayMessage(SwingUtils.MessageType.ERROR,
                    Message.getMessage("error.not_your_turn"));
            return false;
        }

        gameUi.displayMessage(SwingUtils.MessageType.TRACE,
                Message.getMessage("player.action", currentPlayer.getName(), play.toShortString()));
        LOG.debug("Play for player " + currentPlayer);

        try {
            return doHandlePlay(play);

        } catch (BoardGameException ex) {            
            gameUi.displayMessage(SwingUtils.MessageType.ERROR,
                    Message.getMessage("error.exception"));
            LOG.error("An exception occurred during the player action", ex);
            return false;
        }
    }

    /**
     * Handles the play of a player.
     * 
     * One must check if the play is legal. Then one must try to apply it to the
     * game In case of problems, return false (enhancement : explain why this is
     * false).
     * 
     * @param play the play to handle
     * @return true if the play was successfully taken into account, false
     *         otherwise.
     * @throws BoardGameException if an exception occurred
     */
    protected abstract boolean doHandlePlay(IBoardPlay play) throws BoardGameException;

    /** {@inheritDoc} */
    public final void run() {

        LOG.debug("Starting the game thread.");

        setState(BoardGameState.INGAME);
        IPlayerInfo awaitedPlayer = null;

        while (getState() == BoardGameState.INGAME) {

            try {
                if (isHumanTurn()) {

                    if (awaitedPlayer != currentPlayer) {
                        awaitedPlayer = currentPlayer;
                        gameUi.setStatus(
                                Message.getMessage("info.your_turn", currentPlayer.getName()));
                    }

                    try {
                        Thread.sleep(THREAD_SLEEP_DELAY);
                    } catch (InterruptedException e) {
                        LOG.error("Thread game was interrupted", e);
                        setState(BoardGameState.TERMINATED);
                        Thread.currentThread().interrupt();
                    }

                } else {

                    gameUi.setStatus("CPU plays for " + currentPlayer.getName() + " ... ");
                    cpuPlay(currentPlayer);
                    endOfPlay();

                    awaitedPlayer = null;
                }

            } catch (BoardGameException ex) {
                setState(BoardGameState.TERMINATED);
                LOG.error("Game will stop due to an exception", ex);
            }

            if (getState() != BoardGameState.TERMINATING) {

                if (passCounter >= players.size()) {
                    LOG.info("The game will terminate, all players have passed their turn.");
                    setState(BoardGameState.TERMINATING);
                }

                if (board.isTerminated()) {
                    LOG.info("The game will terminate, ending condition are satisfied.");
                    setState(BoardGameState.TERMINATING);
                }

                if ((turnCounter > getMaxTurns())) {
                    LOG.info("The game will terminate, the maximum number of turns ("
                            + getMaxTurns() + " has been reached.");
                    setState(BoardGameState.TERMINATING);
                }
            }

        }

        gameUi.setStatus("Game is terminated");
        LOG.debug("Game thread terminated");
        gameUi.displayMessage(SwingUtils.MessageType.INFO,
                "The game has ended : " + board.getWinningStatus());
    }

    /**
     * Let the CPU play.
     * 
     * @param currentPlayer player to simulate
     * @throws BoardGameException if a problem occurred during CPU play
     */
    protected abstract void cpuPlay(IPlayerInfo currentPlayer) throws BoardGameException;

    /**
     * Starts a new game.
     * 
     * @throws BoardGameException In cas of problem
     */
    public final void newGame() throws BoardGameException {

        gameUi.setStatus(Message.getMessage("info.new_game"));
        LOG.info("Creating a new game");

        int playersCount = players.size();
        int minPlayers = getMinPlayers();
        int maxPlayers = getMaxPlayers();
        if (playersCount < minPlayers || playersCount > maxPlayers) {
            throw new BoardGameException(
                    Message.getMessage("error.players.count", minPlayers, maxPlayers));
        }

        board.initialize(this);
        gameUi.displayScore(board.getScore());
        passCounter = 0;
        turnCounter = 1;
        history = new Stack<>();

        // Determine the first player
        Optional<IPlayerInfo> sayWhoStarts = sayWhoStarts();
        if (sayWhoStarts.isEmpty()) {
            throw new BoardGameException(
                    "Unable to determine the first player that can start the game.");
        }

        currentPlayer = sayWhoStarts.get();
        LOG.info("It is to " + currentPlayer.getName() + " to start the game.");

        Thread gameThread = new Thread(this, "GameThread");
        gameThread.start();
        fireGameChanged();
    }

    /** Terminates the current game and the associated game thread. */
    public final synchronized void terminate() {
        setState(BoardGameState.TERMINATING);
    }

    /** Notifies the game observers that the game has changed. */
    protected final void fireGameChanged() {
        for (GameObserver gameObserver : gameObservers) {
            gameObserver.gameChanged();
        }
    }

    /**
     * This method allows to determines which player must start. It depends on
     * the concrete game. It can depend on the players themselves (the youngest
     * start) or of the board accessories (roll a dice).
     * 
     * @return the information about the player who must start
     */
    protected abstract Optional<IPlayerInfo> sayWhoStarts();

    /** Sets the next game playing turn. */
    protected final void nextPlayTurn() {

        turnCounter++;
        passCounter = 0;

        LOG.debug("Starting turn " + turnCounter);
    }

    /**
     * Assigns a role to a player.
     * 
     * @param player the player
     * @param role role in the game
     */
    public final void assignRoleToPlayer(IPlayerInfo player, IPlayerRole role) {
        this.roleMap.put(player, role);
    }

    /**
     * Finds the player that plays a given role.
     * 
     * @param role requested role
     * @return optional of the player that plays this role, null if it does not
     *         exist
     */
    protected final Optional<IPlayerInfo> getPlayerForRole(IPlayerRole role) {
        return roleMap.entrySet().stream().filter(entry -> entry.getValue().equals(role))
                .map(Map.Entry::getKey).findFirst();
    }

    /**
     * Loads the game state from a file.
     * 
     * @param file file from where to load the game state
     * @throws BoardGameException in case of problem
     */
    public void load(File file) throws BoardGameException {
        throw new BoardGameException(Message.getMessage("error.operation.unavailable"));
    }

    /**
     * Saves the game state to a file.
     * 
     * @param file file where to save the game state
     * @throws BoardGameException in case of problem
     */
    public void save(File file) throws BoardGameException {
        throw new BoardGameException(Message.getMessage("error.operation.unavailable"));
    }

    /**
     * Cancels the last played action of the game.
     * 
     * @throws BoardGameException in case of problem
     */
    public abstract void undo() throws BoardGameException;

    /** @return the game board */
    public final AbstractBoard getAbstractBoard() {
        return board;
    }

    /**
     * Add an observer to the game.
     * 
     * @param gameObserver the game observer to add
     */
    public void addObserver(GameObserver gameObserver) {
        this.gameObservers.add(gameObserver);
    }

}
