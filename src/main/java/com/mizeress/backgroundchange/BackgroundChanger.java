package com.mizeress.backgroundchange;
import java.awt.Image;

// Interface for changing the desktop background
// Implementations will handle OS-specific background changing
public interface BackgroundChanger {
    void changeBackground(Image image);
}
