package com.example.studionagran;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.awt.*;
import java.util.prefs.Preferences;

import java.io.IOException;

public class main extends Application {
    static final String appModeKey = "appMode";
    static final String darkMode = "darkMode";
    static final String lightMode = "lightMode";

    static final String languageKey = "language";
    static final String pl = "pl";
    static final String eng = "eng";

    public static String urlLightmode= "/lightMode.css";
    public static String urlDarkmode= "/darkMode.css";
    public static Scene scene;
    public static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    //nazwa aplikacji
    public static String appName = "Echotunes";




    @Override
    public void start(Stage stage) throws IOException, InterruptedException {

            FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource(getStoredLanguage() +"/view.fxml"));
            Parent root = fxmlLoader.load();
            scene = new Scene(root, 700, 400);

            getStoredAppMode();
            if (getStoredAppMode().equals(darkMode)) {
            scene.getStylesheets().add(urlDarkmode);
            } else {
            scene.getStylesheets().add(urlLightmode);
            }


        //toolbar icon
        Image appIcon;
        appIcon = new Image("/logo.jpg");
        stage.getIcons().add(appIcon);




//        Set icon on the taskbar/dock
            if (Taskbar.isTaskbarSupported()) {
            var taskbar = Taskbar.getTaskbar();

            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                var dockIcon = defaultToolkit.getImage(getClass().getResource("/logo.jpg"));
                taskbar.setIconImage(dockIcon);
            }}

            stage.initStyle(StageStyle.DECORATED);
            stage.setScene(scene);
            stage.show();
            stage.setTitle(getStoredName());
            stage.setResizable(false);

        Settings.addListenerForTitleChange((observable, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(() -> stage.setTitle(Settings.newTitleToMain));
            }
        });

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

    // Odczytaj jezyk z Preferences
    static String getStoredLanguage() {
        Preferences preferences = Preferences.userNodeForPackage(main.class);
        return preferences.get(languageKey, pl); // Domyslnie jezyk Polski
    }

    // Zapisz jezyk do Preferences
    public static void storeLanguage(String language) {
        Preferences preferences = Preferences.userNodeForPackage(main.class);
        preferences.put(languageKey, language);
    }

    public static void storeName(String name){
        Preferences preferences = Preferences.userNodeForPackage(main.class);
        preferences.put(appName, name);
    }
    public String getStoredName(){
        Preferences preferences = Preferences.userNodeForPackage(main.class);
        return preferences.get(appName, "Echotunes");
    }






}