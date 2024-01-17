package com.example.studionagran;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
    
public class main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 400);
        scene.getStylesheets().add(".\\src\\main\\resources\\styles\\lightMode.css");
        stage.initStyle(StageStyle.UNDECORATED);
        //stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);


    }



    public static void main(String[] args) {

        launch();
    }
}