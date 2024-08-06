package io.github.purpleloop.gameengine.workshop.ui.map;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import io.github.purpleloop.gameengine.workshop.ui.context.WorkshopContext;
import io.github.purpleloop.gameengine.workshop.ui.context.WorkshopPanel;

/** A map editor panel. */
public class MapEditorPanel extends WorkshopPanel {

    /** Serial tag. */
    private static final long serialVersionUID = -1940362304550322580L;

    /** Palette of tiles. */
    private TilesPalettePanel tilesPalettePanel;

    /** The tile map panel. */
    private TileMapPanel tileMapPanel;

    /**
     * Constructor of the panel.
     * 
     * @param workshopUi
     */
    public MapEditorPanel(WorkshopContext workshopContext) {

        super(workshopContext);

        setLayout(new BorderLayout());

        this.tilesPalettePanel = new TilesPalettePanel(workshopContext);

        add(new JScrollPane(tilesPalettePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.NORTH);

        this.tileMapPanel = new TileMapPanel(workshopContext);
        add(tileMapPanel, BorderLayout.CENTER);
    }

}
