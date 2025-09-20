package com.mizeress.ImageGeneration;

import java.awt.Image;

import com.mizeress.config.Config;

// Interface for different image sources (e.g., local directory, online API)
public interface ImageSource {
    Image getImage();

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
