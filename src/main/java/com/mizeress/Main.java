package com.mizeress;

import java.util.concurrent.TimeUnit;

import com.mizeress.backgroundchange.Scheduler;
import com.mizeress.config.Config;

/**
 * Change the desktop background to a random image at a specified interval.
 * Interval and source of random image are configurable via config.properties file.
 * Source images can be local from a directory, or from an online source like Unsplash.
 */
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