package io.github.purpleloop.gameengine.workshop.ui.map;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;

import io.github.purpleloop.commons.swing.sprites.SpriteSet;
import io.github.purpleloop.commons.swing.sprites.model.SpriteModel;
import io.github.purpleloop.gameengine.workshop.ui.context.WorkshopContext;
import io.github.purpleloop.gameengine.workshop.ui.context.WorkshopPanel;

/** A panel used to pickup tiles for a tile map. */
public class TilesPalettePanel extends WorkshopPanel {

    /** Serial tag. */
    private static final long serialVersionUID = -3492587339603064393L;

    /** The scaled graphic unit for tiles. */
    private static final int SCALED_GRAPHIC_UNIT = 50;

    /** Width of the border around tiles. */
    private static final int BORDER_WIDTH = 5;

    /** Mouse listener. */
    private MouseListener mouseListener = new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
            int x = (e.getX() - BORDER_WIDTH) / (SCALED_GRAPHIC_UNIT + BORDER_WIDTH);
            int y = (e.getY() - BORDER_WIDTH) / (SCALED_GRAPHIC_UNIT + BORDER_WIDTH);

            selectTileIn(x, y);
        }

    };

    /**
     * Constructor for the panel.
     * 
     * @param workshopContext the context
     */
    public TilesPalettePanel(WorkshopContext workshopContext) {
        super(workshopContext);
        setPreferredSize(new Dimension(1200, 60));
        addMouseListener(mouseListener);
    }

    /**
     * Select the tile at given coordinates.
     * 
     * @param x abscissa of the tile
     * @param y ordinate of the tile
     */
    protected void selectTileIn(int x, int y) {

        Optional<SpriteModel> spriteModelOpt = retrieve("spriteModel");

        if (spriteModelOpt.isPresent()) {
            SpriteSet spriteSet = spriteModelOpt.get().getSpriteSet();

            store("selectedTileName", spriteSet.getSpritesNames().toArray()[x]);
        }
    }

    @Override
    public void paint(Graphics g) {
        Optional<SpriteModel> spriteModelOpt = retrieve("spriteModel");

        if (spriteModelOpt.isPresent()) {
            SpriteSet spriteSet = spriteModelOpt.get().getSpriteSet();

            int x = BORDER_WIDTH;
            int y = BORDER_WIDTH;

            int wMax = spriteSet.widest().orElse(SCALED_GRAPHIC_UNIT);
            int hMax = spriteSet.highest().orElse(SCALED_GRAPHIC_UNIT);
            double factor = ((double) SCALED_GRAPHIC_UNIT) / Math.max(wMax, hMax);

            for (String spriteName : spriteSet.getSpritesNames()) {

                spriteSet.putSprite(g, this, spriteName, x, y, factor);
                x += SCALED_GRAPHIC_UNIT + BORDER_WIDTH;

            }
        }

    }

}
