package com.example.studionagran;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;


public class settings{
@FXML
private final ToggleGroup  appMode;
@FXML
private RadioButton radioButtonDarkMode, radioButtonLightMode;

    public settings(ToggleGroup appMode) {
        this.appMode = appMode;
    }

    @FXML
private void initialize() {

    radioButtonDarkMode.setToggleGroup(appMode);
    radioButtonLightMode.setToggleGroup(appMode);
    radioButtonLightMode.setSelected(true);
}


}