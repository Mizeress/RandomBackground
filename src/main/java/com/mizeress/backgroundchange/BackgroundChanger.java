package com.mizeress.backgroundchange;
import java.awt.Image;

// Interface for changing the desktop background
// Implementations will handle OS-specific background changing
public interface BackgroundChanger {
    void changeBackground(Image image);

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
