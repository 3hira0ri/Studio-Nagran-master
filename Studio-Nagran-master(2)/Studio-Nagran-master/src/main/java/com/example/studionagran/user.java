package com.example.studionagran;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.sql.Connection;

import static com.example.studionagran.Userslist.pokaz;

public class user{
    public static String login;
    public static String password;


    @FXML
    private Label loginTextField;
    @FXML
    private Label passwordTextField;
    @FXML
    private Label DateTextField;
    public void LoginForm()throws Exception{
        Stage primaryStage = (Stage) DateTextField.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(getClass().getResource("addSongView.fxml"));
        primaryStage.getScene().setRoot(newRoot);
    }
    public void UserSetting()throws Exception{
        Stage primaryStage = (Stage) DateTextField.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(getClass().getResource("accountSettings.fxml"));
        primaryStage.getScene().setRoot(newRoot);
    }
    public void initialize()throws Exception{
        /////////////////////////////////////////////////////////////////////////////
 /*       DatabaseConnection connectNow=new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String verifyLogin = "SELECT COUNT(1) FROM useraccounts WHERE userLogin='"+user.login+"'" +
                " AND userPassword='"+user.password+"'";
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult= statement.executeQuery(verifyLogin);

            while (queryResult.next()){
                if (queryResult.getInt(1)!=1){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Alert!");
                    alert.setHeaderText("You disconnected from server, or were logged out");
                    alert.setContentText("Are you ok with this?");
                    Stage primaryStage = (Stage) DateTextField.getScene().getWindow();
                    Parent newRoot = FXMLLoader.load(getClass().getResource("User.fxml"));
                    primaryStage.getScene().setRoot(newRoot);
                }
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }*/
        ///////////////////////////////////////////////////////////////////////////////
        loginTextField.setText(login);
        passwordTextField.setText(password);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        DateTextField.setText(dtf.format(time()));
    }
    public void exitProgram (ActionEvent e){
        Stage stage = (Stage) passwordTextField.getScene().getWindow();
        stage.close();
    }
    public void logout (ActionEvent e) throws IOException {
        login = "";
        password="";
        Stage primaryStage = (Stage) DateTextField.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(getClass().getResource("view.fxml"));
        primaryStage.getScene().setRoot(newRoot);
        primaryStage.getScene().setRoot(newRoot);
    }
    public static LocalDateTime time(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return now;
    }
    public void list(){
        Stage primaryStage = (Stage) DateTextField.getScene().getWindow();
        pokaz(primaryStage);
        }
}

