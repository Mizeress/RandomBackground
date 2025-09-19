package com.mizeress.backgroundchange;

import java.io.File;
import java.io.FileOutputStream;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

public class WindowsBackgroundChanger implements BackgroundChanger {
    
    // Implementation to change the desktop background on Windows
        public interface User32 extends com.sun.jna.Library {
            User32 INSTANCE = Native.load("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

            boolean SystemParametersInfo(
                int uiAction,
                int uiParam,
                String pvParam,
                int fWinIni
            );
        }
    
    // Windows-specific implementation to change the desktop background
    @Override
    public void changeBackground(java.awt.Image image) {
        
        File tempfile = null;
        try {
            tempfile = File.createTempFile("wallpaper", ".jpg");
            try (FileOutputStream output = new FileOutputStream(tempfile)) {
                javax.imageio.ImageIO.write((java.awt.image.RenderedImage) image, "jpg", output);
            }
            
            String path = tempfile.getAbsolutePath();
            System.out.println("Setting wallpaper to: " + path);

            int SPI_SETDESKWALLPAPER = 0x0014;
            int SPIF_UPDATEINIFILE = 0x01;
            int SPIF_SENDCHANGE = 0x02;

            boolean success = User32.INSTANCE.SystemParametersInfo(
                SPI_SETDESKWALLPAPER,
                0,
                path, // Path to the image file
                SPIF_UPDATEINIFILE | SPIF_SENDCHANGE
            );
        
            if (success) {
            System.out.println("Wallpaper changed successfully.");
            } else {
                System.out.println("Failed to change wallpaper.");
            }

        } catch (Exception e) {
            System.out.println("Error in saving image: " + e.getMessage());
        } finally {
            if (tempfile != null && tempfile.exists()) {
                tempfile.delete();
            }
        }  

    }
}
