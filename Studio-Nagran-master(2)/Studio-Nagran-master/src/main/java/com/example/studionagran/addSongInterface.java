package com.example.studionagran;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
public class addSongInterface {
    File selectedFile;
    @FXML
    private Button cofnij;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField albumTextField;
    @FXML
    private TextField singerTextField;
    @FXML
    private TextField publicationDateTextField;
    @FXML
    private ImageView albumCoverImage;
    @FXML
    private Button dodaj;
    // Pozostałe pola

    @FXML
    private Label addAlbumCoverLabel;

    // Metoda do konwersji obrazu w formacie JPG do formatu BLOB
    public static byte[] convertImageToBlob(File imageFile) {
        try {
            // Wczytaj obraz z pliku
            BufferedImage bufferedImage = ImageIO.read(imageFile);

            // Utwórz strumień wyjściowy bajtów
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            // Zapisz obraz do strumienia w formacie JPG
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);

            // Pobierz tablicę bajtów z strumienia
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void filechooser() throws IOException {
            Stage primaryStage = (Stage) dodaj.getScene().getWindow();
        // Tworzenie obiektu FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");

        // Ustawienie filtrowania tylko na pliki z rozszerzeniem JPG
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);

        // Pokaż okno dialogowe wyboru pliku
        selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            // Aktualizacja ImageView
            Image image = new Image(selectedFile.toURI().toString());
            albumCoverImage.setImage(image);

            // Tutaj możesz przekazać selectedFile do funkcji uploadPhotoToDatabase
            // i użyć go jako pliku do konwersji na BLOB
            convertImageToBlob(selectedFile);
            InputStream inputStream = new ByteArrayInputStream(convertImageToBlob(selectedFile));
            FileOutputStream outputStream = new FileOutputStream(titleTextField.getText()+".jpg");

            byte[] buffer = new byte[1024];
            while (inputStream.read(buffer) > 0) {
                outputStream.write(buffer);
            }

            inputStream.close();
            outputStream.close();
        }
    }



    public void cancel() throws IOException {
    Stage primaryStage = (Stage) cofnij.getScene().getWindow();
    Parent newRoot = FXMLLoader.load(getClass().getResource("User.fxml"));
    primaryStage.getScene().setRoot(newRoot);
}

    public void dodawanie(){
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Gratulacje!!!");
        info.setHeaderText("Dane:"+titleTextField.getText()+" "+albumTextField.getText()+" "+singerTextField.getText());
        info.setContentText("Teraz przejdziemy na ekran logowania.");

        info.showAndWait();
        DatabaseConnection connectNow=new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        try {
            FileInputStream inputStream = new FileInputStream(selectedFile);

            byte[] imageData = inputStream.readAllBytes();

            String verifyLogin2 = "INSERT INTO songs (name, album, author, Published, CoverImage, WhoAdded) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement2 = connectDB.prepareStatement(verifyLogin2)) {
                statement2.setString(1, titleTextField.getText());
                statement2.setString(2, albumTextField.getText());
                statement2.setString(3, singerTextField.getText());
                statement2.setString(4, publicationDateTextField.getText());
                statement2.setBytes(5, imageData);
                statement2.setString(6, user.login);

                statement2.executeUpdate();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //String verifyLogin2 = "INSERT INTO songs (`songName`, `songAlbum`, `songSinger`, `songReleaseDate`, `songUserAddDate`) VALUES ('"+title+"','"+album+"','"+autor+"','"+time()+"','"+time()+"');";
        Stage primaryStage = (Stage) dodaj.getScene().getWindow();
        Parent newRoot = null;
        try {
            newRoot = FXMLLoader.load(getClass().getResource("User.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        primaryStage.getScene().setRoot(newRoot);
    }



        }

