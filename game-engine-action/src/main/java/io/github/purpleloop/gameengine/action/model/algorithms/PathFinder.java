package io.github.purpleloop.gameengine.action.model.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.direction.Direction;
import io.github.purpleloop.commons.direction.Direction4;
import io.github.purpleloop.gameengine.action.model.environment.AbstractCellObjectEnvironment;
import io.github.purpleloop.gameengine.action.model.interfaces.IEnvironmentObjet;
import io.github.purpleloop.gameengine.core.util.Location;

/** A path finder, usable to move objects along paths in cell environments. */
public class PathFinder {

    /** Logger of the class. */
    private static final Log LOG = LogFactory.getLog(PathFinder.class);

    /** The target value (maximal). */
    private static final int TARGET_VALUE = 9999;

    /** A value array, used to store the value of a given cell. */
    private int[][] value;

    /** Open locations (to explore). */
    private List<Location> openLocations;

    /** Closed locations (already explored). */
    private List<Location> closedLocations;

    /** The environment, on which the path finding occurs. */
    private AbstractCellObjectEnvironment environment;

    /** The width of the environment (in cells units). */
    private int cellWidth;

    /** The height of the environment (in cells units). */
    private int cellHeight;

    /** The object to move along the path. */
    private IEnvironmentObjet object;

    /**
     * Creates a new path finder for the given environment.
     * 
     * @param environment the environment to explore
     * @param object the object to control
     */
    public PathFinder(AbstractCellObjectEnvironment environment, IEnvironmentObjet object) {

        this.object = object;
        this.environment = environment;
        this.cellWidth = environment.getCellWidth();
        this.cellHeight = environment.getCellHeight();

        this.value = new int[cellWidth][cellHeight];
        this.openLocations = new ArrayList<>();
        this.closedLocations = new ArrayList<>();
    }

    /** Resets the path finder for a new search session. */
    public void reset() {

        // Clears the value array
        for (int x = 0; x < cellWidth; x++) {
            for (int y = 0; y < cellHeight; y++) {
                value[x][y] = 0;
            }
        }

        // Clears the lists
        openLocations.clear();
        closedLocations.clear();
    }

    /**
     * Set the target location to reach.
     * 
     * @param location the target location
     */
    public void setTarget(Location location) {
        openLocations.add(location);
        value[location.getX()][location.getY()] = TARGET_VALUE;
    }

    /**
     * Propagation of the target value through the 2D space, taking environment
     * obstacles into account.
     */
    public void propagate() {

        Location locationToExplore;

        int currentLocX;
        int currentLocY;
        int currentValue;

        int neighborLocX;
        int neighborLocY;
        Location neighborLocation;

        // While there are locations to explore
        while (!openLocations.isEmpty()) {

            // Pick the next one, mark it as seen and explore from it
            locationToExplore = openLocations.remove(0);
            closedLocations.add(locationToExplore);

            currentLocX = locationToExplore.getX();
            currentLocY = locationToExplore.getY();
            currentValue = value[currentLocX][currentLocY];

            // For each neighbor
            for (Direction dir : Direction4.values()) {

                neighborLocX = (int) (currentLocX + dir.getXStep());
                neighborLocY = (int) (currentLocY + dir.getYStep());
                neighborLocation = Location.getLocation(neighborLocX, neighborLocY);

                // If neighbor is reachable, propagates the value with a decay
                if (environment.isObjectAllowedAtCell(object, neighborLocX, neighborLocY)
                        && (value[neighborLocX][neighborLocY] < currentValue - 1)
                        && !closedLocations.contains(neighborLocation)) {
                    openLocations.add(neighborLocation);
                    value[neighborLocX][neighborLocY] = currentValue - 1;
                }
            }
        }
    }

    /**
     * Searches for the better direction to move the object
     * 
     * Warning, the object must be at exact cell location and be no larger than
     * a cell.
     * 
     * @return the direction to take, a value in {@link Direction4}
     */
    public Direction findBetterDirection() {

        int cellSize = environment.getCellSize();

        // Here we expect an exact division
        int cellX = object.getXLoc() / cellSize;
        int cellY = object.getYLoc() / cellSize;

        int bestValue = value[cellX][cellY];
        Direction bestDirection = Direction.NONE;

        int testedX;
        int testedY;
        int testedValue;

        // Try each direction get
        for (Direction testedDirection : Direction4.values()) {

            testedX = (int) (cellX + testedDirection.getXStep());
            testedY = (int) (cellY + testedDirection.getYStep());

            if (environment.isObjectAllowedAtCell(object, testedX, testedY)) {

                // Get the direction that minimizes the distance to the target
                testedValue = value[testedX][testedY];

                if (testedValue > bestValue) {
                    bestValue = testedValue;
                    bestDirection = testedDirection;
                }
            }
        }

        return bestDirection;
    }

    /** Logs the state of the path finding. */
    public void logValues() {

        StringBuilder dumpString = new StringBuilder();

        dumpString.append("\n         ");
        for (int x = 0; x < cellWidth; x++) {
            dumpString.append(String.format(" % 5d", x));
        }

        dumpString.append("\n");

        for (int y = 0; y < cellHeight; y++) {
            dumpString.append(String.format(" % 5d | ", y));

            for (int x = 0; x < cellWidth; x++) {
                dumpString.append(String.format(" % 5d", value[x][y]));
            }

            dumpString.append("\n");
        }
        
        LOG.debug(dumpString.toString());
    }
}
