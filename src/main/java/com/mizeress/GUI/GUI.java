package com.mizeress.gui;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

// Graphical User Interface for controlling the Configuration File
public class GUI {
    // Windows and some Linux distos support System Tray Icons that can be used to have a closeable UI, but GNOME no longer supports it
    // Options:
    // - System Tray Icon (Cons: not supported on all OSes, limited UI capabilities)
    // - Minimize to dock/taskbar (Cons: takes up space, less convenient)
    // - Seperate app with inter-process communication (IPC) to control background app (Cons: more complex, harder to implement)

    // May need to make the Config.java class thread safe to prevent write conflicts between GUI and background changer

    // Choice: Use System Tray when available and fallback to IPC using local server socket for GNOME and other unsupported environments


    public GUI() {
        if(SystemTray.isSupported()) {

            Thread fxThread = new Thread(() -> {
                try {
                    JavaFXGUI.launchGUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "javafx-launcher");
            fxThread.setDaemon(true);
            fxThread.start();
            
            JavaFXGUI.startupFuture().thenRun(() -> {
                EventQueue.invokeLater(this::SystemTraySetup);
            }).exceptionally(e -> {
                e.printStackTrace();
                return null;
            });

        } else {
            // Start local server socket for IPC and launch separate GUI application
        }
    }

    private void SystemTraySetup() {
        Image image = Toolkit.getDefaultToolkit().getImage("src/main/resources/icon.jpg");
        
        //Show GUI
        PopupMenu popup = new PopupMenu();
        MenuItem showGUI = new MenuItem("Edit Config");
        showGUI.addActionListener( e -> openConfigWindow());
        popup.add(showGUI);

        TrayIcon trayIcon = new TrayIcon(image, "Random Background Changer", popup);

        trayIcon.setImageAutoSize(true);
        trayIcon.addActionListener(e -> openConfigWindow());

        try {
            SystemTray tray = SystemTray.getSystemTray();
            tray.add(trayIcon);
        } catch (Exception e) {
            System.out.println("Error setting up system tray: " + e.getMessage());
        }
    }

    /**
     * Show JavaFX GUI
     */
    private void openConfigWindow() {
        // Open JavaFX GUI for Config Control
        // Allow user to change image source, path, and interval
        JavaFXGUI.show();
    }

}
