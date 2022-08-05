package io.github.purpleloop.gameengine.workshop.ui.sprites;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import io.github.purpleloop.commons.swing.sprites.model.IndexedSpriteSet;
import io.github.purpleloop.commons.swing.sprites.model.SpriteGridIndex;
import io.github.purpleloop.commons.swing.sprites.model.SpriteModel;

public class SpriteGridIndexPanel extends JPanel implements ChangeListener {

    /** Serial tag. */
    private static final long serialVersionUID = 2523579431719531974L;

    /** X location of grid start. */
    private JSpinner startXTf;

    /** Y location of grid start. */
    private JSpinner startYTf;

    /** Width of sprites in the grid. */
    private JSpinner spriteWidthTf;

    /** Height of sprites in the grid. */
    private JSpinner spriteHeightTf;

    /** Number lines in the grid. */
    private JSpinner nbLinesTf;

    /** Number of sprites per line in the grid. */
    private JSpinner spritesPerLineTf;

    /** Horizontal spacing between sprites. */
    private JSpinner paddingXTf;

    /** Vertical spacing between sprites. */
    private JSpinner paddingYTf;

    /** The sprite model associated to this panel. */
    private SpriteModel spriteModel;

    /** Has the form been loaded (says if the panel can change the model). */
    private boolean formLoaded = false;

    /** The sprite set editor panel (for repaints). */
    private SpriteSetEditorPanel spriteSetEditorPanel;

    public SpriteGridIndexPanel(SpriteSetEditorPanel spriteSetEditorPanel) {
        super();

        this.spriteSetEditorPanel = spriteSetEditorPanel;

        add(new JLabel("Location start x"));

        startXTf = addSpinner(5, 0, 2000);

        add(new JLabel(", y"));

        startYTf = addSpinner(5, 0, 2000);

        add(new JLabel("Sprite width"));

        spriteWidthTf = addSpinner(10, 1, 2000);

        add(new JLabel(", height"));
        spriteHeightTf = addSpinner(10, 1, 2000);

        add(new JLabel("Number of lines"));
        nbLinesTf = addSpinner(1, 1, 100);

        add(new JLabel("Number of sprites per line"));
        spritesPerLineTf = addSpinner(1, 1, 100);

        add(new JLabel("Horizontal space between sprites"));
        paddingXTf = addSpinner(5, 0, 2000);

        add(new JLabel("Vertical space between sprites"));
        paddingYTf = addSpinner(5, 0, 2000);

    }

    /**
     * Add a JSpinner to the panel.
     * 
     * @param initialValue initial value
     * @param minValue minimal value
     * @param maxValue maximal value
     */
    private JSpinner addSpinner(int initialValue, int minValue, int maxValue) {
        SpinnerModel model = new SpinnerNumberModel(initialValue, minValue, maxValue, 1);
        JSpinner spinner = new JSpinner(model);

        add(spinner);
        spinner.addChangeListener(this);
        return spinner;
    }

    public void setModel(SpriteModel spriteModel) {
        this.spriteModel = spriteModel;

        formLoaded = false;

        List<IndexedSpriteSet> indexes = spriteModel.getIndexes();
        if (indexes.size() > 0) {

            // FIXME must get the selected index here
            IndexedSpriteSet index = indexes.get(0);
            if (index instanceof SpriteGridIndex) {
                modelToForm(index);
            }
        }

    }

    private void modelToForm(IndexedSpriteSet index) {
        SpriteGridIndex gridIndex = (SpriteGridIndex) index;
        startXTf.setValue(gridIndex.getStartPoint().x);
        startYTf.setValue(gridIndex.getStartPoint().y);
        spriteWidthTf.setValue(gridIndex.getCellWidth());
        spriteHeightTf.setValue(gridIndex.getCellHeight());
        spritesPerLineTf.setValue(gridIndex.getNumColumns());
        nbLinesTf.setValue(gridIndex.getNumRows());
        paddingXTf.setValue(gridIndex.getCellHorizontalSpacing());
        paddingYTf.setValue(gridIndex.getCellVerticalSpacing());

        formLoaded = true;
    }

    @Override
    public void stateChanged(ChangeEvent e) {

        List<IndexedSpriteSet> indexes = spriteModel.getIndexes();
        if (indexes.size() > 0) {

            // FIXME manage index selection here
            IndexedSpriteSet index = indexes.get(0);
            if (formLoaded && index instanceof SpriteGridIndex) {
                formToModel(index);
            }
        }

    }

    private void formToModel(IndexedSpriteSet index) {
        SpriteGridIndex gridIndex = (SpriteGridIndex) index;

        gridIndex.getStartPoint().x = (int) startXTf.getValue();
        gridIndex.getStartPoint().y = (int) startYTf.getValue();
        gridIndex.setCellWidth((int) spriteWidthTf.getValue());
        gridIndex.setCellHeight((int) spriteHeightTf.getValue());
        gridIndex.setNumColumns((int) spritesPerLineTf.getValue());
        gridIndex.setNumRows((int) nbLinesTf.getValue());
        gridIndex.setCellHorizontalSpacing((int) paddingXTf.getValue());
        gridIndex.setCellVerticalSpacing((int) paddingYTf.getValue());

        // TODO improve with an index observer ?
        spriteSetEditorPanel.repaint();
    }

}
