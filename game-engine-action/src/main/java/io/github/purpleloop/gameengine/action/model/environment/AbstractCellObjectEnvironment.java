package io.github.purpleloop.gameengine.action.model.environment;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.gameengine.action.model.interfaces.IEnvironmentObjet;
import io.github.purpleloop.gameengine.action.model.interfaces.ISession;
import io.github.purpleloop.gameengine.action.model.level.IGameLevel;
import io.github.purpleloop.gameengine.core.config.GameConfig;
import io.github.purpleloop.gameengine.core.util.EngineException;
import io.github.purpleloop.gameengine.core.util.Location;

/**
 * This class serves as basis for games where environment is a 2D rectangular
 * world. Locations are expressed in "base" units. The world is regularly
 * divided into square cells.
 * 
 * Each cell can have a static object content (wall, door, chest, plant, or
 * other things ...). This static content is set according to the level
 * structure.
 */
public abstract class AbstractCellObjectEnvironment extends AbstractObjectEnvironment {

	/** Logger of the class. */
	private static final Log LOG = LogFactory.getLog(AbstractCellObjectEnvironment.class);

	/** Cell size property. */
	private static final String ENVIRONMENT_CELL_SIZE = "environment.cellSize";

	/**
	 * Maximum number of tries before giving up in searches. Should be sufficiently
	 * large to allow dispersion and sufficiently small to prevent blocking.
	 */
	private static final int MAX_TRIES_FOR_SEARCHES = 10000;

	/** Width of the environment, expressed in base units. */
	protected int width;

	/** Height of the environment, expressed in base units. */
	protected int height;

	/** Size of a cell in location units. */
	protected int cellSize;

	/** Width of the environment, expressed in cells units. */
	protected int cellWidth;

	/** Height of the environment, expressed in cells units. */
	protected int cellHeight;

	/** The static content stored in each cell of the environment. */
	private ICellContents[][] storage;

	/**
	 * Creates an abstract cell object environment.
	 * 
	 * @param session session in which the environment evolves
	 * @param level   the game level as the static structure description used to
	 *                build this environment
	 * @throws EngineException in case of problems during environment creation
	 */
	protected AbstractCellObjectEnvironment(ISession session, IGameLevel level) throws EngineException {
		super(session, level);

		GameConfig config = session.getGameEngine().getConfig();
		cellSize = config.getIntProperty(ENVIRONMENT_CELL_SIZE);

		initFromGameLevel();
	}

	/**
	 * Initializes the storage for each cell.
	 * 
	 * @param cellWidth  width in cell units
	 * @param cellHeight height in cell units
	 */
	protected void initStorage(int cellWidth, int cellHeight) {

		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;

		this.width = cellWidth * cellSize;
		this.height = cellHeight * cellSize;
		LOG.debug("Creating the environment storage of " + cellWidth + " x " + cellHeight + " cells => " + width + " x "
				+ height + " base units");

		this.storage = new ICellContents[cellWidth][cellHeight];
	}

	/**
	 * @return the cell size expressed in base units
	 */
	public int getCellSize() {
		return cellSize;
	}

	/** @return the width of the environment, expressed in cell units */
	public int getCellWidth() {
		return cellWidth;
	}

	/** @return the height of the environment, expressed in cell units */
	public int getCellHeight() {
		return cellHeight;
	}

	/**
	 * Sets the content of a cell.
	 * 
	 * @param cx          abscissa of the cell
	 * @param cy          ordinate of the cell
	 * @param newContents the new content to set in this cell
	 */
	protected final void setCellContents(int cx, int cy, ICellContents newContents) {
		storage[cx][cy] = newContents;
	}

	/**
	 * Gets the content of a cell.
	 * 
	 * @param cx abscissa of the cell
	 * @param cy ordinate of the cell
	 * @return the content of this cell
	 */
	public final ICellContents getCellContents(int cx, int cy) {
		return storage[cx][cy];
	}

	/**
	 * Tests if a cell is within cell bounds of the environment.
	 * 
	 * @param cx abscissa of the cell
	 * @param cy ordinate of the cell
	 * @return true if the location (cx, cy) are the coordinates of a valid cell of
	 *         the environment, false otherwise
	 */
	protected boolean isValidCell(int cx, int cy) {
		return (cx >= 0) && (cy >= 0) && (cx < cellWidth) && (cy < cellHeight);
	}

	/**
	 * Tests if a standard object at location (x, y) is within bounds of the
	 * environment for a given standard object size (of a square cell).
	 * 
	 * @param x abscissa in base units
	 * @param y ordinate in base units
	 * @return true if the location is in bounds (for a standard object occupying
	 *         one cell), false otherwise
	 */
	public boolean isObjectInBounds(int x, int y) {
		return (x >= 0) && (y >= 0) && (x + cellSize - 1 < width) && (y + cellSize - 1 < height);
	}

	/**
	 * Tests if an environment object can be in a given location.
	 * 
	 * @param object the environment object to test
	 * @param x      abscissa in base units
	 * @param y      ordinate in base units
	 * @return true if the given object is allowed to be at the given location (x,
	 *         y) of the environment, false otherwise
	 */
	public boolean isObjectAllowedAtLocation(IEnvironmentObjet object, int x, int y) {

		int cx1 = x / cellSize;
		int cy1 = y / cellSize;
		int cx2 = (x + cellSize - 1) / cellSize;
		int cy2 = (y + cellSize - 1) / cellSize;

		// We check the rectangle (cx1,cy1)-(cx2,cx2)
		return isObjectAllowedAtCell(object, cx1, cy1) && isObjectAllowedAtCell(object, cx2, cy1)
				&& isObjectAllowedAtCell(object, cx1, cy2) && isObjectAllowedAtCell(object, cx2, cy2);
	}

	/**
	 * Tests if an environment object can be in a given cell.
	 * 
	 * @param object the environment object to test
	 * @param cx     abscissa of the cell
	 * @param cy     ordinate of the cell
	 * @return true if the given object is allowed to be at the given cell (cx, cy)
	 *         of the environment, false otherwise
	 */
	public abstract boolean isObjectAllowedAtCell(IEnvironmentObjet object, int cx, int cy);

	/**
	 * This method is called when an environment object reaches a given cell.
	 * 
	 * TODO : See if we can have a more event-driven approach here.
	 * 
	 * @param object the environment object
	 * @param cx     abscissa of the cell
	 * @param cy     ordinate of the cell
	 */
	public abstract void reachingCell(IEnvironmentObjet object, int cx, int cy);

	@Override
	public void dumpEnvironmentObjects() {
		super.dumpEnvironmentObjects();
		dumpStorage();
	}

	/** Dumps to logs the environment storage. */
	private void dumpStorage() {
		LOG.debug("Storage dump :");

		StringBuilder stringBuilder = new StringBuilder();
		for (int cy = 0; cy < cellHeight; cy++) {

			stringBuilder.delete(0, stringBuilder.length());

			for (int cx = 0; cx < cellWidth; cx++) {
				stringBuilder.append(storage[cx][cy].getLevelChar() + " ");
			}

			LOG.debug(stringBuilder.toString());
		}
	}

	/**
	 * Search for the first cell location matching the given contents.
	 * 
	 * @param contents cell content to match
	 * @return optional location of the first matching cell
	 */
	protected Optional<Location> findFirstCellLocationMatchingContents(ICellContents contents) {

		for (int y = 0; y < cellHeight; y++) {
			for (int x = 0; x < cellWidth; x++) {
				if (getCellContents(x, y).equals(contents)) {
					return Optional.of(Location.getLocation(x, y));
				}
			}
		}
		return Optional.empty();
	}

	/**
	 * Get an allowed random location for the given object.
	 * 
	 * @param object the tested object
	 * @return an optional allowed location
	 */
	public Optional<Location> findRandomAllowedLocationForObject(IEnvironmentObjet object) {

		int cellX;
		int cellY;
		int tries = 0;

		boolean locationFound = false;

		do {
			cellX = random.nextInt(cellWidth);
			cellY = random.nextInt(cellHeight);
			locationFound = isObjectAllowedAtCell(object, cellX, cellY);
			tries++;

		} while ((tries < MAX_TRIES_FOR_SEARCHES) && !locationFound);

		if (locationFound) {
			return Optional.of(Location.getLocation(cellX, cellY));
		} else {
			return Optional.empty();
		}
	}

}
