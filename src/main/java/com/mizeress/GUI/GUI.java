package com.mizeress.gui;

// Graphical User Interface for controlling the Configuration File
public class GUI {
    // Windows and some Linux distos support System Tray Icons that can be used to have a closeable UI, but GNOME no longer supports it
    // Options:
    // - System Tray Icon (Cons: not supported on all OSes, limited UI capabilities)
    // - Minimize to dock/taskbar (Cons: takes up space, less convenient)
    // - Seperate app with inter-process communication (IPC) to control background app (Cons: more complex, harder to implement)

    // May need to make the Config.java class thread safe to prevent write conflicts between GUI and background changer
}
