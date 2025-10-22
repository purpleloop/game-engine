package io.github.purpleloop.gameengine.workshop.ui.sprites;

import java.awt.Color;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import io.github.purpleloop.commons.exception.PurpleException;
import io.github.purpleloop.commons.swing.sprites.model.SpriteModel;
import io.github.purpleloop.gameengine.workshop.ui.ImageFileFilter;

/** A panel to edit the source image of a sprite model. */
public class SpriteImageSourcePanel extends JPanel {

    /** Serial tag. */
    private static final long serialVersionUID = -7617969527120663443L;

    /** Text field for the source image. */
    private JTextField tfSourceImageName;

    /** The sprite model. */
    private SpriteModel spriteModel;

    /** File chooser for images. */
    private JFileChooser fcImages;

    /** A filter for sprite images. */
    private FileFilter imageFilter = new ImageFileFilter();

    /** The associated SpriteSourcePanel for background color changes. */
    private SpriteSourcePanel associatedSpriteSourcePanel;

    /** Constructor of the panel. */
    public SpriteImageSourcePanel() {

        this.tfSourceImageName = new JTextField();

        add(new JLabel("Source image"));
        add(tfSourceImageName);

        JButton btChooseImage = new JButton("Select image");

        btChooseImage.addActionListener(e -> selectImage());
        add(btChooseImage);

        fcImages = new JFileChooser(new File("."));
        fcImages.setDialogTitle("Select the source image");
        fcImages.setFileFilter(imageFilter);

        JButton btChooseBgColor = new JButton("Choose background color");
        btChooseBgColor.addActionListener(e -> chooseBackgroundColor());
        add(btChooseBgColor);

    }

    /** Selects the source image. */
    private void selectImage() {

        fcImages.setCurrentDirectory(new File(spriteModel.getSourceImagePath()).getParentFile());

        int dialogResult = fcImages.showOpenDialog(this);

        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            File file = fcImages.getSelectedFile();

            String sourceImageFileName = file.getAbsolutePath();
            tfSourceImageName.setText(sourceImageFileName);
            try {
                spriteModel.setSourceImage(sourceImageFileName);
                repaint();
            } catch (PurpleException e) {
                JOptionPane.showMessageDialog(this,
                        "Error while updating the source image : " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    /**
     * Sets the associated SpriteSourcePanel.
     * 
     * @param spriteSourcePanel the SpriteSourcePanel
     */
    public void setAssociatedSpriteSourcePanel(SpriteSourcePanel spriteSourcePanel) {
        this.associatedSpriteSourcePanel = spriteSourcePanel;
    }

    /** Choose a new background color. */
    private void chooseBackgroundColor() {

        Color selectedColor = JColorChooser.showDialog(this, "Choose background color",
                associatedSpriteSourcePanel.getBackgroundColor());

        if (selectedColor != null) {
            associatedSpriteSourcePanel.setBackgroundColor(selectedColor);
        }

    }

    /**
     * Changes the sprite model.
     * 
     * @param spriteModel the sprite model
     */
    public void setSpriteModel(SpriteModel spriteModel) {
        this.spriteModel = spriteModel;
        fromModelToForm();
    }

    /** Fills the form from the model. */
    private void fromModelToForm() {
        this.tfSourceImageName.setText(spriteModel.getSourceImagePath());
    }

}
