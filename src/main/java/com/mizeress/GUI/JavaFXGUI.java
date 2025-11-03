package com.mizeress.gui;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import com.mizeress.backgroundchange.Scheduler;
import com.mizeress.config.Config;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Hello World JavaFX for testing
 */
public class JavaFXGUI extends Application {

    private static final AtomicReference<Stage> PRIMARY_STAGE_REF = new AtomicReference<>();
    private static final CompletableFuture<Void> STARTUP = new CompletableFuture<>();
    private Config config = new Config();
    private Scheduler scheduler;

     @Override
    public void start(Stage primaryStage) {
        // Create a Label control
        Label mainLabel = new Label("Random Background Changer");

        // Create a StackPane as the root node for the scene
        // StackPane centers its children
        StackPane root = new StackPane();
        root.getChildren().add(mainLabel);

        // Set the title of the primary stage (window)
        primaryStage.setTitle("Random Background Changer");

        // Hide on exit instead of close
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            primaryStage.hide();
        });


        //Controls
        ComboBox<String> imageSourceCombo = new ComboBox<>();
        imageSourceCombo.getItems().addAll("directory", "API");
        TextField imagePathField = new TextField();
        TextField changeIntervalField = new TextField();

        // Buttons
        Button btnSave = new Button("Save");
        Button btnCancel = new Button("Cancel");
        Button btnRun = new Button("Run");
        Button btnStop = new Button("Stop");

        HBox actions = new HBox(8, btnSave, btnCancel, btnRun, btnStop);
        actions.setPadding(new Insets(8, 0, 0, 0));

        // Layout
        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setPadding(new Insets(12));
        ColumnConstraints left = new ColumnConstraints();
        left.setPercentWidth(30);
        ColumnConstraints right = new ColumnConstraints();
        right.setPercentWidth(70);
        grid.getColumnConstraints().addAll(left, right);

        grid.add(new Label("Image Source"), 0, 0);
        grid.add(imageSourceCombo, 1, 0);
        grid.add(new Label("Image Path or URL"), 0, 1);
        grid.add(imagePathField, 1, 1); //TODO Replace with a brose to file when image source is directory
        grid.add(new Label("Change Interval (minutes)"), 0, 2);
        grid.add(changeIntervalField, 1, 2);
        grid.add(actions, 1, 3);

        // Button Handlers
        btnSave.setOnAction(event -> {
            //Disable button
            btnSave.setDisable(true);
            btnRun.setDisable(true);
            btnStop.setDisable(true);

            System.out.println("Saving...");
            // Save config properties to file
            CompletableFuture<?> pathCompletableFuture = config.setPropertyAsync("imagePath", imagePathField.getText().trim());
            CompletableFuture<?> sourceCompletableFuture = config.setPropertyAsync("imageSource", imageSourceCombo.getValue());
            CompletableFuture<?> intervalCompletableFuture = config.setPropertyAsync("changeInterval", changeIntervalField.getText().trim());

            CompletableFuture.allOf(pathCompletableFuture, sourceCompletableFuture, intervalCompletableFuture).thenRun(() -> {
                Platform.runLater(()->{
                    btnSave.setDisable(false);
                    btnRun.setDisable(false);
                    btnStop.setDisable(false);
                    btnStop.fire();
                    btnRun.fire();
                });
            }).exceptionally(ex->{
                Platform.runLater(()->{
                    btnSave.setDisable(false);
                    btnRun.setDisable(false);
                    btnStop.setDisable(false);
                });
                return null;
            });
        });

        btnCancel.setOnAction(event -> {
            System.out.println("Cancelling");
            // Reset values to what is on disk
            imagePathField.setText(config.getProperty("imagePath"));
            imageSourceCombo.setValue(config.getProperty("imageSource"));
            changeIntervalField.setText(config.getProperty("changeInterval"));
        });

        btnRun.setOnAction(event -> {
            btnRun.setDisable(true);
            btnStop.setDisable(false);
            if(scheduler != null) {
                scheduler.stop();
            }
            scheduler = new Scheduler(Integer.parseInt(config.getProperty("changeInterval")));
        });

        btnStop.setOnAction(event -> {
            btnStop.setDisable(true);
            btnRun.setDisable(false);
            if(scheduler != null) {
                scheduler.stop();
            }
        });

        //Initialize fields from disk
        imagePathField.setText(config.getProperty("imagePath"));
        imageSourceCombo.setValue(config.getProperty("imageSource"));
        changeIntervalField.setText(config.getProperty("changeInterval"));

        //Start running automatically
        btnRun.setDisable(true);
        btnStop.setDisable(false);
        if(scheduler != null) {
            scheduler.stop();
        }
        scheduler = new Scheduler(Integer.parseInt(config.getProperty("changeInterval")));

        // Create a Scene with the root node and specify its dimensions
        Scene scene = new Scene(grid, 650, 200);

        // Set the scene to the primary stage
        primaryStage.setScene(scene);

        //Continue running in background if main window is hidden
        Platform.setImplicitExit(false);

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
