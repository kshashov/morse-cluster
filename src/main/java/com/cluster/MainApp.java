package com.cluster;

/**
 * Created by envoy on 07.03.2017.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class MainApp extends Application {
    private static String DEFAULT_LOG = "log.txt";
    public static Stage primaryStage;
    public static String[] args;
    public static PrintStream log;

    private int WIDTH = 800;
    private int HEIGHT = 500;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/main.fxml"));
            stage.getIcons().add(new Image(getClass().getResource("/icon.png").toExternalForm()));
        } catch (IOException e) {
            logError(e);

        }
        setUserAgentStylesheet(STYLESHEET_MODENA);

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        stage.setMinHeight(HEIGHT);
        stage.setMinWidth(WIDTH - 500);
        stage.setTitle("Morse Cluster Solver");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try {
            log = new PrintStream(new File(System.getProperty("user.dir") + File.separator + DEFAULT_LOG));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        MainApp.args = args;
        launch(args);
    }

    public static void logError(Exception e) {
        e.printStackTrace(log);
    }
}
