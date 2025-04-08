package com.example.studionagran;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Optional;

import static com.example.studionagran.SHAExample.hashPassword;


@SuppressWarnings("ALL")
public class menu {

@FXML
private TextField loginTextField;
@FXML
private PasswordField passwordTextField;
@FXML
private TextField firstNameTextfield;
@FXML
private TextField lastNameTextfield;
@FXML
Button loginButton;
    public void LoginForm()throws Exception{
        Stage primaryStage = (Stage) loginButton.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(getClass().getResource(main.getStoredLanguage() + "/view.fxml"));
        primaryStage.getScene().setRoot(newRoot);
    }


   /* public void RegisterButtonOnAction(ActionEvent e) throws Exception {
            registerForm();
    }
    @FXML
    Button registerUserButton;
    public void registerForm()throws Exception{
        Stage primaryStage = (Stage) registerUserButton.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(getClass().getResource("registerForm.fxml"));
        primaryStage.getScene().setRoot(newRoot);
    }*/



public void exitProgram (ActionEvent e){
    Stage stage = (Stage) passwordTextField.getScene().getWindow();
    stage.close();
}
@FXML
private Label warningLabel;
    public void RegisterButtonOnAction(ActionEvent e) throws Exception {
        if(loginTextField.getText().isBlank()==false && passwordTextField.getText().isBlank()==false){

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
                alert.setHeaderText("Are you sure you want to create account with these informations?" + "\nLogin: "+login+"\nPassword: "+password+"\nName: "+firstNameTextfield.getText()+"\nSecond Name: "+firstNameTextfield.getText());
                alert.setContentText("Are you ok with this?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                   String password2 =  hashPassword(password);
                    String verifyLogin2 = "INSERT INTO useraccounts (userLogin, userPassword, Name,SecondName ) VALUES ('"+login+"', '"+password2+"','"+firstNameTextfield.getText()+"','"+lastNameTextfield.getText()+"');";
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
                    checkAndUpdate();
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
}     public void initialize() {
        // Dodajemy menu kontekstowe do całej sceny
        Platform.runLater(() -> {
        ContextMenuHelper.addContextMenuToScene(loginButton.getScene());
        });
    }
    public static void checkAndUpdate() {
        String url = "jdbc:mysql://localhost/studionagran";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Sprawdzenie, czy istnieje rekord z kolumną isAdmin równą 1
            String checkQuery = "SELECT COUNT(*) AS count FROM useraccounts WHERE isAdmin = 1";
            Statement checkStatement = conn.createStatement();
            ResultSet resultSet = checkStatement.executeQuery(checkQuery);
            resultSet.next();
            int count = resultSet.getInt("count");

            if (count == 0) {
                // Pobranie ID najmniejszego rekordu
                String minIdQuery = "SELECT MIN(userId) AS minId FROM useraccounts";
                Statement minIdStatement = conn.createStatement();
                ResultSet minIdResult = minIdStatement.executeQuery(minIdQuery);
                minIdResult.next();
                int minId = minIdResult.getInt("minId");

                // Aktualizacja rekordu o najmniejszym ID
                String updateQuery = "UPDATE useraccounts SET isAdmin = 1 WHERE userId = ?";
                PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
                updateStatement.setInt(1, minId);
                int rowsAffected = updateStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Wiersz o ID " + minId + " został zaktualizowany. isAdmin ustawione na 1.");
                } else {
                    System.out.println("Nie udało się zaktualizować wiersza.");
                }
            } else {
                System.out.println("Istnieje co najmniej jeden rekord z kolumną isAdmin równą 1.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}