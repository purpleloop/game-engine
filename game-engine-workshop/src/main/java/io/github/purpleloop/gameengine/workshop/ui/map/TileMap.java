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

}
