package com.mizeress;

import java.util.concurrent.TimeUnit;

import com.mizeress.gui.GUI;

/**
 * Change the desktop background to a random image at a specified interval.
 * Interval and source of random image are configurable via config.properties file.
 * Source images can be local from a directory, or from an online source like Unsplash.
 */
public class Main
{
    public static void main( String[] args )
    {
        //Initialize system tray if available, otherwise spool up IPC
        GUI gui = new GUI();

        try {
            Thread.sleep(TimeUnit.HOURS.toMillis(1));
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted: " + e.getMessage());
        } // Keep the main thread alive for 1 hour (or indefinitely in a real app)

        System.out.println("Application exiting...");

    }

}