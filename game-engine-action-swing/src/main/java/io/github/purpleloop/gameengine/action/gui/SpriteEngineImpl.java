package io.github.purpleloop.gameengine.action.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.swing.sprites.Sprite;
import io.github.purpleloop.commons.swing.sprites.SpriteSet;
import io.github.purpleloop.gameengine.core.config.GameConfig;
import io.github.purpleloop.gameengine.core.config.IDataFileProvider;
import io.github.purpleloop.gameengine.core.util.EngineException;

/** Implementation of the sprite engine. */
public class SpriteEngineImpl implements SpriteEngine {

    /** Class logger. */
    private static final Log LOG = LogFactory.getLog(SpriteEngineImpl.class);

    /** Name of the sprites file. */
    private static final String SPRITES_IMAGE_NAME = "sprites";

    /** The spriteset. */
    private SpriteSet spriteSet;

    @Override
    public void loadSpritesSource(GameConfig conf, IDataFileProvider dfp) {
        try {
            LOG.debug("Loading sprites");
            Image img = conf.getImage(dfp, SPRITES_IMAGE_NAME);
            spriteSet = new SpriteSet(img);

        } catch (EngineException e) {
            throw new RuntimeException("The sprite loading has failed.", e);
        }
    }

    @Override
    public void registerSprite(String name, int x, int y, int w, int h) {
        spriteSet.addSprite(new Sprite(name, x, y, w, h));
    }

    @Override
    public void registerSprite(Sprite desc) {
        registerSprite(desc.getName(), desc.getOx(), desc.getOy(), desc.getWidth(),
                desc.getHeight());
    }

    @Override
    public void putSprite(Graphics g, ImageObserver observer, String name, int xl, int yl) {
        spriteSet.putSprite(g, observer, name, xl, yl);
    }

}
