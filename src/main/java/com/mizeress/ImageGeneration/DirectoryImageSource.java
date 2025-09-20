package com.mizeress.imagegeneration;

import java.awt.Image;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import com.mizeress.config.Config;

/**
 * Class to get a random image from a local directory
 */
public class DirectoryImageSource implements ImageSource {
    /**
     * Get an image from the source.
     * @return Java AWT Image
     */
    @Override
    public Image getImage() {
        // Implementation to get an image from a local directory
        Config config = new Config();
        Path imagePath = Paths.get(config.getProperty("imagePath"));
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(imagePath, "*.{jpg,jpeg,png,gif}")) {
            List<Path> imageFiles = new ArrayList<>();
            for (Path entry : stream) {
                imageFiles.add(entry);
            }
            if (imageFiles.isEmpty()) {
                throw new RuntimeException("No images found in directory: " + imagePath);
            }
            Path randomImagePath = imageFiles.get(new Random().nextInt(imageFiles.size()));
            return ImageIO.read(randomImagePath.toFile());
        } catch (Exception e) {
            System.out.println("Error reading images from directory: " + e.getMessage());
            throw new RuntimeException("Error reading images from directory: " + imagePath, e);
        }
    }

}
