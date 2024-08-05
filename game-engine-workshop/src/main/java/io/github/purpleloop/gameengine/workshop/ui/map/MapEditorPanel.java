package io.github.purpleloop.gameengine.workshop.ui.map;

import java.awt.Graphics;
import java.util.Optional;

import javax.swing.JPanel;

import io.github.purpleloop.commons.swing.sprites.Sprite;
import io.github.purpleloop.commons.swing.sprites.SpriteSet;
import io.github.purpleloop.commons.swing.sprites.model.SpriteModel;
import io.github.purpleloop.gameengine.workshop.ui.context.WorkshopContext;

/** A map editor panel. */
public class MapEditorPanel extends JPanel {

    /** Serial tag. */
    private static final long serialVersionUID = -1940362304550322580L;

    /** The workshop context. */
    private WorkshopContext workshopContext;

    /** The current tileMap. */
    private TileMap tileMap;

    /**
     * Constructor of the panel.
     * 
     * @param workshopUi
     */
    public MapEditorPanel(WorkshopContext workshopContext) {

        this.workshopContext = workshopContext;
        this.tileMap = new TileMap();
    }

    public void paint(Graphics g) {
        Optional<SpriteModel> spriteModelOpt = workshopContext.get("spriteModel");

        if (spriteModelOpt.isPresent()) {
            SpriteSet spriteSet = spriteModelOpt.get().getSpriteSet();

            int x = 0;
            int y = 0;
            int maxHeight=0;

            for (String spriteName : spriteSet.getSpritesNames()) {

                Sprite sprite = spriteSet.getSprite(spriteName);

                spriteSet.putSprite(g, this, spriteName, x, y);

                x+=sprite.getWidth();
                if (sprite.getHeight()>maxHeight) {
                    maxHeight=sprite.getHeight();
                }
                
                if (x>1000) {
                    x=0;
                    y+=maxHeight;
                    maxHeight=0;
                }
                
            }
        }

    }
}
