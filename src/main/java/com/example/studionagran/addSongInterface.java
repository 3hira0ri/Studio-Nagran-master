package com.example.studionagran;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.studionagran.Userslist.pokazMuzyke;
import static com.example.studionagran.Userslist.pokazWiersze;

@SuppressWarnings("ALL")
public class addSongInterface {
    @FXML
    public static Label appNameLabel;
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
    //menu
    @FXML
    private Button goToUserSettingsButton;
    @FXML
    private Button goToPurchases;
    @FXML
    private Label addAlbumCoverLabel;
    @FXML
    private Label userHowMuchPoints;
    @FXML
    private Label userNameLabel;
    @FXML
    private Pane MyMusic;

    private AutoCompletionBinding<String> autoCompletionBinding;
    public void initialize() {

        userHowMuchPoints.setText("Points: "+String.valueOf(user.karma));
        userNameLabel.setText("Username: "+user.login);
        // Dodajemy menu kontekstowe do całej sceny
        Platform.runLater(() -> {
            ContextMenuHelper.addContextMenuToScene(dodaj.getScene());
        });
        Platform.runLater(() -> {
            try {
                Stage stage = (Stage) goToUserSettingsButton.getScene().getWindow();
                if(main.getStoredAppMode().equals(main.darkMode)) {
                    // Dodanie arkusza stylów do sceny
                    Scene scene = goToUserSettingsButton.getScene();
                    scene.getStylesheets().add(getClass().getResource(main.urlDarkmode).toExternalForm());
                    scene.getStylesheets().remove(getClass().getResource(main.urlLightmode).toExternalForm());
                }else if(main.getStoredAppMode().equals(main.lightMode)){
                    // Dodanie arkusza stylów do sceny
                    Scene scene = goToUserSettingsButton.getScene();
                    scene.getStylesheets().remove(getClass().getResource(main.urlDarkmode).toExternalForm());
                    scene.getStylesheets().add(getClass().getResource(main.urlLightmode).toExternalForm());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            DatabaseConnection connectNow = new DatabaseConnection();
            ArrayList albums = new ArrayList();
            try (Connection connectDB = connectNow.getConnection();
                 Statement statement = connectDB.createStatement()) {
                String query = "SELECT album FROM songs";
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    albums.add(resultSet.getString("album"));
                    System.out.println(resultSet.getString("album"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // W tym przykładzie dodaję sztuczne dane
            autoCompletionBinding = TextFields.bindAutoCompletion(singerTextField,albums);
            Button lista_users = new Button("Users");
            lista_users.setOnAction(event -> list());
            // Ustawienie położenia przycisku
            lista_users.setLayoutX(86.0);
            lista_users.setLayoutY(261.0);
            //styl przycisku
            String cssStyle = goToPurchases.getStyle();
            lista_users.setStyle(cssStyle);
            MyMusic.getChildren().add(lista_users);
        });


    }
    public void clearIMG(){
        File selectedFile = new File("default.jpg");
        Image image = new Image(selectedFile.toURI().toString());
        albumCoverImage.setImage(image);
    }
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
    public void list(){
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(main.getStoredLanguage() + "/addSongView.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Użytkownicy");
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
        Parent newRoot = FXMLLoader.load(getClass().getResource(main.getStoredLanguage() + "/User.fxml"));
        primaryStage.getScene().setRoot(newRoot);
    }

    public void dodawanie(){
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Gratulacje!!!");
        info.setHeaderText("Dane:"+titleTextField.getText()+" "+albumTextField.getText()+" "+singerTextField.getText());
        info.setContentText("Możesz je sprawdzić w zakładce 'Buy song'.");

        info.showAndWait();
        DatabaseConnection connectNow=new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        try {
            byte[] imageData;
            if (selectedFile != null) {
                FileInputStream inputStream = new FileInputStream(selectedFile);
                imageData = inputStream.readAllBytes();
            } else {
                // Jeśli nie wybrano pliku, użyj zdjęcia domyślnego
                File defaultImageFile = new File("default.jpg");
                FileInputStream inputStream = new FileInputStream(defaultImageFile);
                imageData = inputStream.readAllBytes();
            }

            String verifyLogin2 = "INSERT INTO songs (name, album, author, Published, CoverImage, WhoAdded) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement2 = connectDB.prepareStatement(verifyLogin2)) {
                statement2.setString(1, titleTextField.getText());
                statement2.setString(2, singerTextField.getText());
                statement2.setString(3, albumTextField.getText());
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
            newRoot = FXMLLoader.load(getClass().getResource(main.getStoredLanguage() + "/addSongInterface.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        primaryStage.getScene().setRoot(newRoot);
    }


    //chodzenie po meny
    public void goToAppSettings(ActionEvent event) throws IOException {

        Stage primaryStage = (Stage) goToPurchases.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(MenuGoTo.class.getResource(main.getStoredLanguage() + "/settings.fxml")));
        primaryStage.getScene().setRoot(newRoot);

    }
    public void goToAccountSettings(ActionEvent event) throws IOException {

        Stage primaryStage = (Stage) goToUserSettingsButton.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(MenuGoTo.class.getResource(main.getStoredLanguage() + "/accountSettings.fxml")));
        primaryStage.getScene().setRoot(newRoot);

    }
    public void goToMyPurchases(ActionEvent event) throws IOException {

        Stage primaryStage = (Stage) goToPurchases.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(MenuGoTo.class.getResource(main.getStoredLanguage() + "/myPurchases.fxml")));
        primaryStage.getScene().setRoot(newRoot);

    }


}

