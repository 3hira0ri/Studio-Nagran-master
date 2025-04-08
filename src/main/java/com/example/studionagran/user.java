package com.example.studionagran;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.example.studionagran.Userslist.pokazWiersze;
import static com.example.studionagran.Userslist.pokazMuzyke;
import static java.sql.Types.NULL;

public class user{
    public static String login;
    public static String password;
    public static String name;
    public static String Secondname;

    public static int id_now;
    public static boolean isAdmin;
    public static boolean autoryzowany;
    public static int karma;

    @FXML
    private Label loginTextField;
    @FXML
    private Label passwordTextField;

    public void LoginForm() {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(main.getStoredLanguage() + "/addSongView.fxml")));
            Stage stage = new Stage();
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(root, 900, 900));
            stage.show();
            // Hide this current window (if this is what you want)
            // ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }
    public void UserSetting()throws Exception{
        Stage primaryStage = (Stage) loginTextField.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(main.getStoredLanguage() + "/accountSettings.fxml")));
        primaryStage.getScene().setRoot(newRoot);
    }
    public void initialize() {
        loginTextField.setText(login);
        passwordTextField.setText(password);
            // Dodajemy menu kontekstowe do całej sceny
            Platform.runLater(() -> {
                ContextMenuHelper.addContextMenuToScene(passwordTextField.getScene());
            });

    }
    public void exitProgram (){
        Stage stage = (Stage) loginTextField.getScene().getWindow();
        stage.close();
    }
    public void logout () throws IOException {
        login = "";
        password="";
        id_now=NULL;
        Stage primaryStage = (Stage) loginTextField.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(main.getStoredLanguage() + "/view.fxml")));
        primaryStage.getScene().setRoot(newRoot);
    }
    public static LocalDateTime time(){
        return LocalDateTime.now();
    }
    public void list(){
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(main.getStoredLanguage() + "/addSongView.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Dodaj utwór");
            stage.setScene(new Scene(root, 450, 450));
            pokazWiersze(stage,user.isAdmin);
            // Hide this current window (if this is what you want)
            // ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void listMusic(){
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(main.getStoredLanguage() + "/addSongView.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Lista utworów");
            stage.setScene(new Scene(root, 450, 450));
            pokazMuzyke(stage,user.isAdmin);
            // Hide this current window (if this is what you want)
            // ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

