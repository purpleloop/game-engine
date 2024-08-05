package io.github.purpleloop.gameengine.workshop.ui;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

/** A file filter for images. */
public class ImageFileFilter extends FileFilter {

    @Override
    public String getDescription() {
        return "Images";
    }

    @Override
    public boolean accept(File f) {
        String[] supportedSuffixes = ImageIO.getReaderFileSuffixes();

        boolean supported = false;

        String absolutePath = f.getAbsolutePath();
        for (String supportedSuffix : supportedSuffixes) {
            supported |= absolutePath.endsWith(supportedSuffix);
        }

        return f.isDirectory() || supported;
    }

}
