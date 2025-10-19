package com.mizeress.gui;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Hello World JavaFX for testing
 */
public class JavaFXGUI extends Application {

    private static final AtomicReference<Stage> PRIMARY_STAGE_REF = new AtomicReference<>();
    private static final CompletableFuture<Void> STARTUP = new CompletableFuture<>();

     @Override
    public void start(Stage primaryStage) {
        // Create a Label control
        Label helloLabel = new Label("Hello, JavaFX!");

        // Create a StackPane as the root node for the scene
        // StackPane centers its children
        StackPane root = new StackPane();
        root.getChildren().add(helloLabel);

        // Create a Scene with the root node and specify its dimensions
        Scene scene = new Scene(root, 300, 200);

        // Set the title of the primary stage (window)
        primaryStage.setTitle("Simple JavaFX GUI");

        // Set the scene to the primary stage
        primaryStage.setScene(scene);

        //Continue running in background if main window is hidden
        Platform.setImplicitExit(false);

        // Hide on exit instead of close
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            primaryStage.hide();
        });

        PRIMARY_STAGE_REF.set(primaryStage);
        STARTUP.complete(null);
    }

    public static void launchGUI() {
        Application.launch();
    }

    public static void show() {
        Platform.runLater(() -> {
            Stage stage = PRIMARY_STAGE_REF.get();
            if (stage == null) {
                System.out.println("Stage not available");
                return;
            }
            if (!stage.isShowing()) stage.show();
            stage.toFront();
            stage.requestFocus();
        });
    }

    public static CompletableFuture<Void> startupFuture() {
        return STARTUP;
    }
}
