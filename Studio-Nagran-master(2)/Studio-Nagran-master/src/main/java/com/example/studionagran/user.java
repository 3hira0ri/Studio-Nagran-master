package com.example.studionagran;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.example.studionagran.Userslist.pokaz;
import static com.example.studionagran.Userslist.pokazMuzyke;

public class user{
    public static String login;
    public static String password;

    @FXML
    private Label loginTextField;
    @FXML
    private Label passwordTextField;
    @FXML
    private Label DateTextField;
    public void LoginForm() {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("addSongView.fxml")));
            Stage stage = new Stage();
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(root, 900, 900));
            stage.show();
            // Hide this current window (if this is what you want)
           // ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void UserSetting()throws Exception{
        Stage primaryStage = (Stage) DateTextField.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("accountSettings.fxml")));
        primaryStage.getScene().setRoot(newRoot);
    }
    public void initialize() {
        loginTextField.setText(login);
        passwordTextField.setText(password);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        DateTextField.setText(dtf.format(time()));
    }
    public void exitProgram (){
        Stage stage = (Stage) passwordTextField.getScene().getWindow();
        stage.close();
    }
    public void logout () throws IOException {
        login = "";
        password="";
        Stage primaryStage = (Stage) DateTextField.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("view.fxml")));
        primaryStage.getScene().setRoot(newRoot);
    }
    public static LocalDateTime time(){
        return LocalDateTime.now();
    }
    public void list(){
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("addSongView.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Dodaj utwór");
            stage.setScene(new Scene(root, 450, 450));
            pokaz(stage);
            // Hide this current window (if this is what you want)
            // ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void listMusic(){
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("addSongView.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Lista utworów");
            stage.setScene(new Scene(root, 450, 450));
            pokazMuzyke(stage);
            // Hide this current window (if this is what you want)
            // ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

