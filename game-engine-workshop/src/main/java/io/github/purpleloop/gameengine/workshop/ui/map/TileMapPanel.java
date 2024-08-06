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

/** A panel used for designing tile maps. */
public class TileMapPanel extends WorkshopPanel {

    /** Serial tag. */
    private static final long serialVersionUID = -4940616379737084696L;

    /** Graphic unit. */
    private static final int UG = 50;

    /** The current tileMap. */
    private TileMap tileMap;

    /** Mouse listener. */
    private MouseListener mouseListener = new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX() / UG;
            int y = e.getY() / UG;

            setTile(x, y);
        }

    };

    /**
     * The tile map panel constructor.
     * 
     * @param workshopContext the workshop context
     */
    public TileMapPanel(WorkshopContext workshopContext) {
        super(workshopContext);
        setPreferredSize(new Dimension(1200, 400));

        tileMap = new TileMap();

        addMouseListener(mouseListener);
    }

    /**
     * Set the tile at given coordinates.
     * 
     * @param x abscissa of the tile
     * @param y ordinate of the tile
     */
    protected void setTile(int x, int y) {

        Optional<String> selectedTileNameOpt = retrieve("selectedTileName");
        tileMap.setTile(x, y, selectedTileNameOpt.orElse(null));
        repaint();
    }

    @Override
    public void paint(Graphics g) {

        Optional<SpriteModel> spriteModelOpt = retrieve("spriteModel");

        if (spriteModelOpt.isPresent()) {
            SpriteSet spriteSet = spriteModelOpt.get().getSpriteSet();

            String tileName;

            for (int y = 0; y < tileMap.getHeight(); y++) {
                for (int x = 0; x < tileMap.getWidth(); x++) {
                    tileName = tileMap.getTile(x, y);

                    if (tileName != null) {
                        spriteSet.putSprite(g, this, tileName, x * UG, y * UG);
                    }

                }
            }
        }
    }
}
