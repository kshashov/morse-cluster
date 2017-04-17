package com.cluster;

/**
 * Created by envoy on 07.03.2017.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        Parent root = null;
        //System.out.println(getClass().get);
        try {
            root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
       /// setUserAgentStylesheet(STYLESHEET_MODENA);

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Morse Cluster Solver");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
