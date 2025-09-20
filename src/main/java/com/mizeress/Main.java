package com.mizeress;
// Overall idea: At given intervals, change the background image of the user's desktop to a random image
// The image can be sourced from either a local directory or an online API, based on user configuration
// The configuation file will be controlled by a GUI
// At the interval specified in the config file, the app will change the background image to a random image from the selected source
// The app will run in the background and start on system startup
// The app will eventually be cross-platform (Windows, Mac, Linux) and will use only open-source libraries

// The code will be structured as follows:
// Main class: Contains the main method and handles the overall flow of the app
// ImageSource interface: Defines the methods that any image source must implement
// DirectoryImageSource class: Implements the ImageSource interface and handles getting images from a local directory
// APIImageSource class: Implements the ImageSource interface and handles getting images from an online API
// Config class: Handles reading and writing the configuration file
// GUI class: Handles the graphical user interface for the app
// BackgroundChanger class: Handles changing the desktop background image
// Scheduler class: Handles scheduling the background image changes at the specified intervals
// Utils class: Contains utility methods that are used throughout the app

import java.util.concurrent.TimeUnit;

import com.mizeress.backgroundchange.Scheduler;
import com.mizeress.config.Config;

public class Main
{
    public static void main( String[] args )
    {
        Config config = new Config();
        int intervalMinutes = Integer.parseInt(config.getProperty("changeInterval"));

        @SuppressWarnings("unused")
        Scheduler scheduler = new Scheduler(intervalMinutes); //Scheduler Background changes at specified intervals

        try {
            Thread.sleep(TimeUnit.HOURS.toMillis(1));
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted: " + e.getMessage());
        } // Keep the main thread alive for 1 hour (or indefinitely in a real app)

        scheduler.stop(); // Stop the scheduler when the app is closing
        System.out.println("Application exiting...");
    }

}