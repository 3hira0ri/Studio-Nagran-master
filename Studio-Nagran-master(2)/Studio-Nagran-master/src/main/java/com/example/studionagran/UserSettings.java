package com.example.studionagran;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.Console;

public class UserSettings {
    @FXML
    private Button changeAvatarButton;
    @FXML
    private Button deleteAvatarButton;
    @FXML
    private Button saveProfileButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private TextField changeEmailTextField;
    @FXML
    private PasswordField changePasswordTextField;

    public void initialize(){
        changeEmailTextField.setText(user.login);
    }
    public void save(){
        String name = nameTextField.getText();
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        String secondName = changeEmailTextField.getText();
        String NewPassword = changePasswordTextField.getText();
        if(name != null){
            System.out.println(name);
        }
        if(username != null){
            System.out.println(username);
        }
        if(email != null){
            System.out.println(email);
        }
        if(password != null){
            System.out.println(password);
        }
        if(secondName != null){
            System.out.println(secondName);
        }
        if(NewPassword != null){
            System.out.println(NewPassword);
        }
    }
}
