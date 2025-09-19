package com.mizeress.ImageGeneration;

import java.awt.Image;
// Interface for different image sources (e.g., local directory, online API)
public interface ImageSource {
    Image getImage();
}
