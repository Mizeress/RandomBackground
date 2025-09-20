package com.mizeress.backgroundchange;
import java.awt.Image;

/**
 * Interface for changing the desktop background.
 * Implementations should handle OS-specific details.
 */
public interface BackgroundChanger {
    /**
     * Change the desktop background to the specified image.
     * @param image
     */
    void changeBackground(Image image);

    /**
     * Factory method to create an OS-specific BackgroundChanger instance.
     * @return BackgroundChanger instance
     */
    static BackgroundChanger create() {
        String osName = System.getProperty("os.name").toLowerCase();
        switch (osName) {
           case "windows 11", "windows 10", "windows 8", "windows 7" -> {
                return new WindowsBackgroundChanger();
           }

           default -> {
                throw new UnsupportedOperationException("Unsupported OS: " + osName);
           }
        }
    }
}
