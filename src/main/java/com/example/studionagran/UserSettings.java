package com.example.studionagran;

import javafx.application.Platform;
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
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import static java.sql.Types.NULL;

public class UserSettings {
    @FXML
    private Button changeAvatarButton;
    @FXML
    private Button deleteAvatarButton;
    @FXML
    private Button saveProfileButton;
    @FXML
    private Button goBackButtonSettings;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    File selectedFile;
    @FXML
    private TextField lastNameTextField1;
    @FXML
    private PasswordField changePasswordTextField;

    @FXML
    private ImageView avatar;
    public UserSettings() {

    }
    public void goBackFromSettings() {

        Stage primaryStage = (Stage) goBackButtonSettings.getScene().getWindow();
        Parent newRoot = null;
        try {
            newRoot = FXMLLoader.load(getClass().getResource(main.getStoredLanguage() + "/addSongView.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        primaryStage.getScene().setRoot(newRoot);
    }
    public void initialize() {
        // Dodajemy menu kontekstowe do całej sceny
        Platform.runLater(() -> {
            ContextMenuHelper.addContextMenuToScene(passwordTextField.getScene());
            nameTextField.setText(user.name);
            usernameTextField.setText(user.login);
            lastNameTextField1.setText(user.Secondname);
        });
    }public static byte[] convertImageToBlob(File imageFile) {
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
    }public void filechooser() throws IOException {
        Stage primaryStage = (Stage) nameTextField.getScene().getWindow();
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
            avatar.setImage(image);

            // Tutaj możesz przekazać selectedFile do funkcji uploadPhotoToDatabase
            // i użyć go jako pliku do konwersji na BLOB
            convertImageToBlob(selectedFile);
            InputStream inputStream = new ByteArrayInputStream(convertImageToBlob(selectedFile));
            FileOutputStream outputStream = new FileOutputStream(user.id_now+".jpg");

            byte[] buffer = new byte[1024];
            while (inputStream.read(buffer) > 0) {
                outputStream.write(buffer);
            }

            inputStream.close();
            outputStream.close();
        }
    }
    public void CLearIMG(){
        File selectedFile = new File("default.jpg");
        Image image = new Image(selectedFile.toURI().toString());
        avatar.setImage(image);
    }
    public void save() throws NoSuchAlgorithmException {
        String name = nameTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String secondName = lastNameTextField1.getText();

        DatabaseConnection connectNow=new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        try {

            String verifyLogin2 = "UPDATE `useraccounts` SET "; //name, album, author, Published, CoverImage, WhoAdded) VALUES (?, ?, ?, ?, ?, ?)
            if(!username.isEmpty()){
                verifyLogin2 =verifyLogin2+"userLogin="+"'"+username+"'";
                user.login=username;
            }else{
                verifyLogin2 =verifyLogin2+"userLogin="+"'"+user.login+"'";
            }
            if(!name.isEmpty()){
                verifyLogin2 =verifyLogin2+",name="+"'"+name+"'";
                user.name=name;
            }
            if(!secondName.isEmpty()){
                verifyLogin2 =verifyLogin2+",SecondName="+"'"+secondName+"'";
                user.Secondname=secondName;
            }
            if(!password.isEmpty()){
                verifyLogin2 =verifyLogin2+",userPassword="+"'"+SHAExample.hashPassword(password)+"'";
                user.password=SHAExample.hashPassword(password);
            }
            byte[] imageData;
            InputStream inputStream;
            if (selectedFile != null) {
                inputStream = new FileInputStream(selectedFile);
                imageData = inputStream.readAllBytes();
            }else{
                // Jeśli nie wybrano pliku, użyj zdjęcia domyślnego
                File defaultImageFile = new File("default.jpg");
                inputStream = new FileInputStream(defaultImageFile);
                imageData = inputStream.readAllBytes();
            }
            verifyLogin2 += ", Avatar = ?";
            verifyLogin2 += " WHERE userId = ?";

            try (PreparedStatement statement2 = connectDB.prepareStatement(verifyLogin2)) {
                statement2.setBytes(1, imageData); // Ustawienie obrazu jako wartości parametru
                statement2.setInt(2, user.id_now); // Ustawienie userId jako wartości parametru


                statement2.executeUpdate();
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Gratulacje!!!");
                info.setHeaderText("Dane zostały zmienione");
                info.setContentText("");
                info.showAndWait();

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
