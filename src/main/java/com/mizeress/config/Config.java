package com.mizeress.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles reading and writing the configuration file
 * Configuration options include:
 * - imageSource: "directory" or "API"
 * - imagePath: path to local directory or API endpoint
 * - changeInterval: time interval in minutes for changing the background
 */
public class Config {
    private final Object fileLock = new Object();   // Lock object
    private final Path configFile = Paths.get(System.getProperty("user.home"), ".mizeress", "config.properties");
    Properties properties;

    public Config() {
        properties = new Properties();
        reloadFromFile();
    }

    public final void reloadFromFile() {
        synchronized (fileLock) {
            Properties tempProperties = new Properties();

            // Load properties from file or set defaults
            try (InputStream input = Files.newInputStream(configFile)) {

                tempProperties.load(input);
            
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }            

            if (tempProperties.isEmpty()) {
                System.out.println("Setting defaults");
                // Set default properties if config file not found
                tempProperties.setProperty("imageSource", "directory");
                tempProperties.setProperty("imagePath", "C:/path/to/images");
                tempProperties.setProperty("changeInterval", "30"); // in minutes
            }
            properties = tempProperties;
        }
    }

    /**
     * Get a property value by key
     * @param key The setting to be retrieved
     * @return Property value or null if not found
     */
    public String getProperty(String key) {
        synchronized (fileLock) {
           return properties.getProperty(key); 
        }
    }

    /**
     * Set a property value by key
     * @param key The setting to be edited
     * @param value Value to set configuration setting to
     */
    public void setProperty(String key, String value) {
        synchronized (fileLock) {
            properties.setProperty(key, value);
            
            //Atomic write
            Path parent = configFile.toAbsolutePath().getParent();

            try {
                Files.createDirectories(parent);
                Path tempFile = Files.createTempFile(parent, "config", ".temp");
                
                try (OutputStream outputStream = Files.newOutputStream(tempFile)) {
                    properties.store(outputStream, null);
                } 

                Files.move(tempFile, configFile, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);

            } catch (Exception e) {
                System.out.println("Error saving config: " + e.getMessage());
            }   
        }
    }

    private final ExecutorService smallExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "config-writer");
        t.setDaemon(true);
        return t;
    });

    public CompletableFuture<Void> setPropertyAsync(String key, String value) {
        return CompletableFuture.runAsync(() -> setProperty(key, value), smallExecutor);
    }
}
