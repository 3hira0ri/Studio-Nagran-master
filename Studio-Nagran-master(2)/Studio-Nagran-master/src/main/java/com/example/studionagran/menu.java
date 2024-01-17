package com.example.studionagran;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;

import static com.example.studionagran.SHAExample.hashPassword;


public class menu {
@FXML
private TextField loginTextField;
@FXML
private PasswordField passwordTextField;
    @FXML
    Button loginButton;
    public void LoginForm()throws Exception{
        Stage primaryStage = (Stage) loginButton.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("view.fxml")));
        primaryStage.getScene().setRoot(newRoot);
    }
public void exitProgram (){
    Stage stage = (Stage) passwordTextField.getScene().getWindow();
    stage.close();
}

    public void RegisterButtonOnAction() {
        if(!loginTextField.getText().isBlank() && !passwordTextField.getText().isBlank()){

            validateRegister();
        }
        else{
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Warning!!!");
            war.setHeaderText("Please fill all informations!");
            war.setContentText("If you want to you go to login page.");

            war.showAndWait();
        }
    }
public void validateRegister(){
    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();
    String verifyLogin = "SELECT COUNT(1) FROM useraccounts WHERE userLogin='"+loginTextField.getText()+"';";
    try{
        Statement statement =connectDB.createStatement();
        ResultSet queryResult= statement.executeQuery(verifyLogin);

    if(!loginTextField.getText().isBlank() && !passwordTextField.getText().isBlank()){
        String login = loginTextField.getText();
        String password = passwordTextField.getText();
        while (queryResult.next()){
            if (queryResult.getInt(1)==0){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Are you sure you want to create account with these informations?" + "\nLogin: "+login+"\nPassword: "+password);
                alert.setContentText("Are you ok with this?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                   String password2 =  hashPassword(password);
                    String verifyLogin2 = "INSERT INTO useraccounts (userLogin, userPassword) VALUES ('"+login+"', '"+password2+"');";
                    try{

                        Statement statement2 =connectDB.createStatement();
                        statement2.executeUpdate(verifyLogin2);

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Gratulacje!!!");
                    info.setHeaderText("Twoje konto zostało założone!");
                    info.setContentText("Teraz przejdziemy na ekran logowania.");

                    info.showAndWait();
                    LoginForm();
                } else {
                    Alert war = new Alert(Alert.AlertType.WARNING);
                    war.setTitle("Warning!!!");
                    war.setHeaderText("Creating account was canceled!");
                    war.setContentText("If you want to you can try again, or go to login page.");

                    war.showAndWait();
                }


            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning!!!");
                alert.setHeaderText("Account with that login already exists!");
                alert.setContentText("Please use diffrent login and try again!");

                alert.showAndWait();            }
        }
    }
   }catch(Exception e){
        e.printStackTrace();
    }
}
}