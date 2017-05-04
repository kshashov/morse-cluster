package com.cluster;

/**
 * Created by envoy on 07.03.2017.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    public static Stage primaryStage;
    public static String[] args;
    private int WIDTH = 800;
    private int HEIGHT = 500;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        Parent root = null;
        //System.out.println(getClass().get);
        try {
            root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
       /// setUserAgentStylesheet(STYLESHEET_MODENA);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setMinHeight(HEIGHT);
        stage.setMinWidth(WIDTH);
        stage.setTitle("Morse Cluster Solver");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        MainApp.args = args;
        launch(args);
    }
}
