package com.example.studionagran;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
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
import static com.example.studionagran.main.scene;


@SuppressWarnings("ALL")
public class Controller {
@FXML
private Label warningLabel;
@FXML
private TextField loginTextField;
@FXML
private PasswordField passwordTextField;
@FXML
private Label goToAddSongLabel;
@FXML
public static Label appNameLabelMyPurchases;
    KeyCodeCombination saveCombination = new KeyCodeCombination(KeyCode.ENTER);

    @FXML
    private Button goBackButtonSettings;
    public void loginButtonOnAction() throws NoSuchAlgorithmException, IOException {

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
        Parent newRoot = FXMLLoader.load(getClass().getResource(main.getStoredLanguage() + "/registerForm.fxml"));
        primaryStage.getScene().setRoot(newRoot);
    }

    @FXML
private Button exitButton;

public void exitProgram (ActionEvent e){
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
}public void initialize() {
        // Dodajemy menu kontekstowe do całej sceny
        Platform.runLater(() -> {
            ContextMenuHelper.addContextMenuToScene(passwordTextField.getScene());
            // Dodanie obsługi skrótu klawiszowego
            scene.setOnKeyPressed(event -> {
                if (saveCombination.match(event)) {
                    try {
                        loginButtonOnAction();
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        });
        DatabaseChecker.main(new String[]{});
    }
public void validateLogin() throws NoSuchAlgorithmException {
    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();
    String password2 =  hashPassword(passwordTextField.getText());
    String verifyLogin = "SELECT COUNT(1),userid,Money,IsAdmin,autoryzowany, Name, SecondName FROM useraccounts WHERE userLogin='"+loginTextField.getText()+"'" +
            " AND userPassword='"+password2+"'";

    try{
    Statement statement = connectDB.createStatement();
    ResultSet queryResult= statement.executeQuery(verifyLogin);

    while (queryResult.next()){
        if (queryResult.getInt(1)==1){

            user.login= loginTextField.getText();
            user.password=passwordTextField.getText();
            user.isAdmin=queryResult.getBoolean("IsAdmin");
            user.id_now=queryResult.getInt("userid");
            user.karma=queryResult.getInt("Money");
            user.name=queryResult.getString("Name");
            user.Secondname=queryResult.getString("SecondName");
            user.autoryzowany=queryResult.getBoolean("autoryzowany");
            Stage primaryStage = (Stage) loginTextField.getScene().getWindow();
            Parent newRoot = FXMLLoader.load(getClass().getResource(main.getStoredLanguage() + "/addSongView.fxml"));
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


    public void goBackFromSettings(ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) goBackButtonSettings.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(MenuGoTo.class.getResource(main.getStoredLanguage() + "/addSongView.fxml")));
        primaryStage.getScene().setRoot(newRoot);
    }
}