package com.mizeress.imagegeneration;

import java.awt.Image;

import com.mizeress.config.Config;

/**
 * Interface for image sources Directory or API
 */
public interface ImageSource {
    /**
     * Get an image from the source.
     * @return Java AWT Image
     */
    Image getImage();

    /**
     * Factory method to create an ImageSource instance based on configuration.
     * @return ImageSource instance
     */
    static ImageSource create() {
        Config config = new Config();
        String sourceType = config.getProperty("imageSource");
        switch (sourceType) {
            case "directory" -> {
                return new DirectoryImageSource();
            }
            case "API" -> {
                return new APIImageSource();
            }
            default -> throw new IllegalArgumentException("Unknown image source type: " + sourceType);

        }
    }
}
