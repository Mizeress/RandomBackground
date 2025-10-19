package com.mizeress.backgroundchange;

import java.awt.Image;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.mizeress.imagegeneration.ImageSource;

/**
 * Schedules periodic background changes using a ScheduledExecutorService.
 * The interval is configurable via the config.properties file.
 */
public class Scheduler {
    
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * Constructor to initialize the scheduler with a fixed interval.
     * @param intervalMinutes
     */
    public Scheduler(int intervalMinutes) {
        scheduler.scheduleAtFixedRate(new ChangeBackgroundTask(), 0, intervalMinutes, java.util.concurrent.TimeUnit.MINUTES);
    }

    /**
     * Stop the scheduler and shutdown the executor service.
     */
    public void stop() {
        System.out.println("Stopping scheduler...");
        scheduler.shutdown();
    }

}


/**
 * Task to change the background image.
 * This task is run periodically by the scheduler.
 */
class ChangeBackgroundTask implements Runnable {
    @Override
    public void run() {
        try {
        ImageSource imgSrc = ImageSource.create(); // Obtain the image source based on configuration
        Image img = imgSrc.getImage();

        BackgroundChanger bkgChanger = BackgroundChanger.create(); // Obtain the background changer based on OS
        bkgChanger.changeBackground(img);
        } catch (Exception e) {
            System.out.println("Error in ChangeBackgroundTask: " + e.getMessage());
        }
    }
}