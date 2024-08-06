package io.github.purpleloop.gameengine.workshop.ui.map;

/** A tile map. */
public class TileMap {

    /** The tiles composing the map. */
    private String[][] tiles;

    /** Width of the map. */
    private int width = 80;

    /** Height of the map. */
    private int height = 40;

    /** Constructor of the tileMap. */
    public TileMap() {
        tiles = new String[width][height];
    }

    /** @return Width of the map */
    public int getWidth() {
        return width;
    }

    /** @return Height of the map */
    public int getHeight() {
        return height;
    }

    /**
     * @param x abscissa of the tile
     * @param y ordinate of the tile
     * @return the tile name or null
     */
    public String getTile(int x, int y) {
        return tiles[x][y];
    }

    /**
     * Sets the tile at given coordinates.
     * 
     * @param x abscissa of the tile
     * @param y ordinate of the tile
     * @param tileName the tile name or null
     */
    public void setTile(int x, int y, String tileName) {
        tiles[x][y] = tileName;

    }

}
