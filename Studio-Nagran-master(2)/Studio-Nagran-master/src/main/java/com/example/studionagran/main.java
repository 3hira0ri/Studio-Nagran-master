package com.example.studionagran;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.prefs.Preferences;

import java.io.IOException;
import java.util.Objects;

public class main extends Application {
    static final String appModeKey = "appMode";
    static final String darkMode = "darkMode";
    static final String lightMode = "lightMode";


    public static String urlLightmode= "/lightMode.css";
    public static String urlDarkmode= "/darkMode.css";
    public static Scene scene;


    @Override
    public void start(Stage stage) throws IOException {


            FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("view.fxml"));
            Parent root = fxmlLoader.load();
            scene = new Scene(root, 700, 400);
            getStoredAppMode();
            if (getStoredAppMode().equals(darkMode)) {
            scene.getStylesheets().add(urlDarkmode);
            } else {
            scene.getStylesheets().add(urlLightmode);
            }

            stage.initStyle(StageStyle.DECORATED);
            //stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);

    }

    public static void main(String[] args) {

        launch();
    }

    // Odczytaj tryb z Preferences
    static String getStoredAppMode() {
        Preferences preferences = Preferences.userNodeForPackage(main.class);
        return preferences.get(appModeKey, lightMode); // Domyślnie jasny tryb
    }

    // Zapisz tryb do Preferences
    public static void storeAppMode(String appMode) {
        Preferences preferences = Preferences.userNodeForPackage(main.class);
        preferences.put(appModeKey, appMode);
    }




}