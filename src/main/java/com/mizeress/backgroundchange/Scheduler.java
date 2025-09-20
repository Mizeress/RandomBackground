package com.mizeress.backgroundchange;

import java.awt.Image;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.mizeress.ImageGeneration.ImageSource;

// Schedules background changes at specified intervals
public class Scheduler {
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public Scheduler(int intervalMinutes) {
        scheduler.scheduleAtFixedRate(new ChangeBackgroundTask(), 0, intervalMinutes, java.util.concurrent.TimeUnit.MINUTES);
        
    }

    public void stop() {
        System.out.println("Stopping scheduler...");
        scheduler.shutdown();
    }

}

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