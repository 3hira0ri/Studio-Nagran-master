package com.example.studionagran;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.example.studionagran.SHAExample.hashPassword;


public class Controller {
@FXML
private Label warningLabel;
@FXML
private TextField loginTextField;
@FXML
private PasswordField passwordTextField;
@FXML
private Label goToAddSongLabel;


    public void loginButtonOnAction(ActionEvent e) throws NoSuchAlgorithmException, IOException {

            if (!loginTextField.getText().isBlank() && !passwordTextField.getText().isBlank()) {
                validateLogin();
            } else {
                warningLabel.setText("Fill in login and password");
            }


}

    public void RegisterButtonOnAction(ActionEvent e) throws Exception {
            registerForm();
    }
    @FXML
    Button registerUserButton;
    public void registerForm()throws Exception{
        Stage primaryStage = (Stage) registerUserButton.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(getClass().getResource("registerForm.fxml"));
        primaryStage.getScene().setRoot(newRoot);
    }

    @FXML
private Button exitButton;

public void exitProgram (ActionEvent e){
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
}
public void validateLogin() throws NoSuchAlgorithmException {
    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();
    String password2 =  hashPassword(passwordTextField.getText());
    String verifyLogin = "SELECT COUNT(1) FROM useraccounts WHERE userLogin='"+loginTextField.getText()+"'" +
            " AND userPassword='"+password2+"'";
    try{
    Statement statement = connectDB.createStatement();
    ResultSet queryResult= statement.executeQuery(verifyLogin);

    while (queryResult.next()){
        if (queryResult.getInt(1)==1){


            //próba dodania czasu ostatniego logowania który będzie mógł sprawdzić admin, chilowo nie działa z powodu typu zmiennych


            /* DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            String verifyLogin2 = "INSERT INTO useraccounts (Date) VALUES ('"+dtf.format(time())+"');";
            try{

                Statement statement2 =connectDB.createStatement();
                statement2.executeUpdate(verifyLogin2);

            }
            catch(Exception e){
                e.printStackTrace();
            }*/

            user.login= loginTextField.getText();
            user.password=passwordTextField.getText();
            Stage primaryStage = (Stage) loginTextField.getScene().getWindow();
            Parent newRoot = FXMLLoader.load(getClass().getResource("User.fxml"));
            primaryStage.getScene().setRoot(newRoot);

        }
        else{
            warningLabel.setText("Wrong Username");
        }
    }

    }
    catch(Exception e){
    e.printStackTrace();
    }
}



}