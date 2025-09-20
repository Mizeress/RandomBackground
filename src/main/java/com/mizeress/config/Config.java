package com.mizeress.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Handles reading and writing the configuration file
 * Configuration options include:
 * - imageSource: "directory" or "API"
 * - imagePath: path to local directory or API endpoint
 * - changeInterval: time interval in minutes for changing the background
 */
public class Config {
    Properties properties;

    public Config() {
        properties = new Properties();
        
        // Load properties from file or set defaults
        try (InputStream input = new FileInputStream("src\\main\\resources\\config.properties")) {

            properties.load(input);
            
        } catch (Exception e) {
            // Set default properties if config file not found
                properties.setProperty("imageSource", "directory");
                properties.setProperty("imagePath", "C:/path/to/images");
                properties.setProperty("changeInterval", "30"); // in minutes
        }
    }

    /**
     * Get a property value by key
     * @param key The setting to be retrieved
     * @return Property value or null if not found
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Set a property value by key
     * @param key The setting to be edited
     * @param value Value to set configuration setting to
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        // Optionally, save properties back to file
        try (OutputStream output = new FileOutputStream("src\\main\\resources\\config.properties")) {
            if (output != null) {
                properties.store(output, null);
            }
        } catch (Exception e) {
            System.out.println("Error saving config: " + e.getMessage());
        }
    }
}
