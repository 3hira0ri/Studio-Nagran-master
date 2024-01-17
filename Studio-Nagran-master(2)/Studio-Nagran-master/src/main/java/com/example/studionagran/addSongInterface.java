package com.example.studionagran;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class addSongInterface {
    @FXML
    private Button cofnij;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField albumTextField;
    @FXML
    private TextField singerTextField;

    public addSongInterface() {
    }

    public void cancel() throws IOException {
    Stage primaryStage = (Stage) cofnij.getScene().getWindow();
    Parent newRoot = FXMLLoader.load(getClass().getResource("User.fxml"));
    primaryStage.getScene().setRoot(newRoot);
}
    @FXML
    private Button dodaj;
    public void dodawanie(){
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Gratulacje!!!");
        info.setHeaderText("Dane:"+titleTextField.getText()+" "+albumTextField.getText()+" "+singerTextField.getText());
        info.setContentText("Teraz przejdziemy na ekran logowania.");

        info.showAndWait();
        DatabaseConnection connectNow=new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        //String verifyLogin2 = "INSERT INTO songs (`songName`, `songAlbum`, `songSinger`, `songReleaseDate`, `songUserAddDate`) VALUES ('"+title+"','"+album+"','"+autor+"','"+time()+"','"+time()+"');";
        String verifyLogin2 = "INSERT INTO songs (songName, songAlbum, songSinger) VALUES ('"+titleTextField.getText()+"','"+albumTextField.getText()+"','"+singerTextField.getText()+"');";
        try{

            Statement statement2 =connectDB.createStatement();
            statement2.executeUpdate(verifyLogin2);

        }
        catch(Exception e){
            e.printStackTrace();
        }
        /*Stage primaryStage = (Stage) dodaj.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(getClass().getResource("User.fxml"));
        primaryStage.getScene().setRoot(newRoot);*/
    }
}
