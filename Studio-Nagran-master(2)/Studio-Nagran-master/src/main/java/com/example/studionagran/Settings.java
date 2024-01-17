package com.example.studionagran;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;


public class Settings {
    public static ToggleGroup appMode;
    @FXML
    private RadioButton radioButtonDarkMode;
    @FXML
    private RadioButton radioButtonLightMode;

    @FXML
    private void initialize() {
        appMode = new ToggleGroup();

        radioButtonDarkMode.setToggleGroup(appMode);
        radioButtonLightMode.setToggleGroup(appMode);

        if (main.getStoredAppMode().equals(main.darkMode)) {
            radioButtonDarkMode.setSelected(true);
        } else {
            radioButtonLightMode.setSelected(true);
        }


    }
    public void changeAppMode(ActionEvent e){
        System.out.println("Change App Mode called");

            if(radioButtonLightMode.isSelected()){
                main.scene.getStylesheets().remove(main.urlDarkmode);
                main.scene.getStylesheets().add(main.urlLightmode);
                main.storeAppMode(main.lightMode);
            }
            else if(radioButtonDarkMode.isSelected()){
                main.scene.getStylesheets().remove(main.urlLightmode);
                main.scene.getStylesheets().add(main.urlDarkmode);
                main.storeAppMode(main.darkMode);
            }



    }

}