package com.example.studionagran;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

import static com.example.studionagran.Userslist.pokazWiersze;
import static com.example.studionagran.main.scene;

public class Settings {

    private static List<ChangeListener<Boolean>> titleChangeListeners = new ArrayList<>();

    public static void addListenerForTitleChange(ChangeListener<Boolean> listener) {
        titleChangeListeners.add(listener);
    }

    public static void removeListenerForTitleChange(ChangeListener<Boolean> listener) {
        titleChangeListeners.remove(listener);
    }

    private static void notifyTitleChangeListeners() {
        for (ChangeListener<Boolean> listener : titleChangeListeners) {
            listener.changed(null, false, true);
        }
    }
    public static String newTitleToMain;
    File selectedFile;
    String iconName="logo";

    @FXML
    private Button menuAddSongButton;
    @FXML
    private Button goToUserSettingsButton;
    @FXML
    private Button goToPurchases;
    @FXML
    private TextField changeAppNameTextField;
    public static ToggleGroup appMode;
    public static ToggleGroup language;
    @FXML
    private RadioButton radioButtonDarkMode;
    @FXML
    private RadioButton radioButtonLightMode;
    @FXML
    private RadioButton radioButtonPolishVersion;
    @FXML
    private RadioButton radioButtonEnglishVersion;
    @FXML
    private Button zip;
    @FXML
    private ImageView iconPreview;
    @FXML
    private Button changeIconButton;
    @FXML
    private Pane MyMusic;
    @FXML
    private Label userHowMuchPoints;
    @FXML
    private Label userNameLabel;
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
    @FXML
    private void initialize() {
        userHowMuchPoints.setText("Points: "+String.valueOf(user.karma));
        userNameLabel.setText("Username: "+user.login);

        appMode = new ToggleGroup();
        language = new ToggleGroup();

        radioButtonDarkMode.setToggleGroup(appMode);
        radioButtonLightMode.setToggleGroup(appMode);
        radioButtonPolishVersion.setToggleGroup(language);
        radioButtonEnglishVersion.setToggleGroup(language);

        if (main.getStoredAppMode().equals(main.darkMode)) {
            main.scene.getStylesheets().add(main.urlDarkmode);
            radioButtonDarkMode.setSelected(true);
            radioButtonDarkMode.setDisable(true);
        } else {
            main.scene.getStylesheets().add(main.urlLightmode);
            radioButtonLightMode.setSelected(true);
            radioButtonLightMode.setDisable(true);
        }
        if (main.getStoredAppMode().equals(main.darkMode)) {
            scene.getStylesheets().add(main.urlDarkmode);
        } else {
            scene.getStylesheets().add(main.urlLightmode);
        }
        Platform.runLater(() -> {
            Button lista_users = new Button("Users");
            lista_users.setOnAction(event -> list());
            // Ustawienie położenia przycisku
            lista_users.setLayoutX(86.0);
            lista_users.setLayoutY(261.0);
            //styl przycisku
            String cssStyle = goToPurchases.getStyle();
            lista_users.setStyle(cssStyle);
            MyMusic.getChildren().add(lista_users);
            try {
                Stage stage = (Stage) radioButtonPolishVersion.getScene().getWindow();
                if(main.getStoredAppMode().equals(main.darkMode)) {
                    // Dodanie arkusza stylów do sceny
                    Scene scene = radioButtonPolishVersion.getScene();
                    scene.getStylesheets().add(getClass().getResource(main.urlDarkmode).toExternalForm());
                    scene.getStylesheets().remove(getClass().getResource(main.urlLightmode).toExternalForm());
                }else if(main.getStoredAppMode().equals(main.lightMode)){
                    // Dodanie arkusza stylów do sceny
                    Scene scene = radioButtonPolishVersion.getScene();
                    scene.getStylesheets().remove(getClass().getResource(main.urlDarkmode).toExternalForm());
                    scene.getStylesheets().add(getClass().getResource(main.urlLightmode).toExternalForm());

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        if (main.getStoredLanguage().equals("pl")) {
            radioButtonPolishVersion.setSelected(true);
            radioButtonPolishVersion.setDisable(true);
        } else {
            radioButtonEnglishVersion.setSelected(true);
            radioButtonEnglishVersion.setDisable(true);
        }
        // Dodajemy menu kontekstowe do całej sceny
        Platform.runLater(() -> {
            ContextMenuHelper.addContextMenuToScene(menuAddSongButton.getScene());
        });


    }
    public void terms(){

        Stage primaryStage = new Stage();
        primaryStage.setTitle("File Viewer");
        String FILE_NAME = "terms.txt";
        // Utwórz kontrolkę TextArea do wyświetlania zawartości pliku
        TextArea textArea = new TextArea();
        textArea.setEditable(false);

        // Utwórz layout dla sceny
        VBox layout = new VBox();
        layout.getChildren().add(textArea);

        // Utwórz scenę i ustaw layout
        Scene scene = new Scene(layout, 400, 300);

        try {
            File file = new File(FILE_NAME);
            // Sprawdź czy plik istnieje
            if (!file.exists()) {
                // Jeśli plik nie istnieje, utwórz nowy
                file.createNewFile();
                // Możesz dodać początkową zawartość do pliku terms.txt, jeśli chcesz
                try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                    writer.println("To jest początkowa zawartość pliku.");
                }
            }

            // Wczytaj zawartość pliku terms.txt i ustaw ją w kontrolce TextArea
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            textArea.setText(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ustaw scenę i pokaż okno
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void changeAppMode(ActionEvent e) throws IOException {
        System.out.println("Change App Mode called");

        // Usuń wszystkie arkusze stylów
        main.scene.getStylesheets().clear();
        if(radioButtonLightMode.isSelected()){
            main.scene.getStylesheets().remove(main.urlDarkmode);
            main.scene.getStylesheets().add(main.urlLightmode);
            main.storeAppMode(main.lightMode);
            Stage primaryStage = (Stage) radioButtonLightMode.getScene().getWindow();
            Parent newRoot = FXMLLoader.load(Objects.requireNonNull(MenuGoTo.class.getResource(main.getStoredLanguage() + "/settings.fxml")));
            primaryStage.getScene().setRoot(newRoot);

        }
        else if(radioButtonDarkMode.isSelected()){
            main.scene.getStylesheets().remove(main.urlLightmode);
            main.scene.getStylesheets().add(main.urlDarkmode);
            main.storeAppMode(main.darkMode);
            Stage primaryStage = (Stage) radioButtonPolishVersion.getScene().getWindow();
            Parent newRoot = FXMLLoader.load(Objects.requireNonNull(MenuGoTo.class.getResource(main.getStoredLanguage() + "/settings.fxml")));
            primaryStage.getScene().setRoot(newRoot);
        }
    }

    public void changeAppLanguage(ActionEvent e) throws IOException {
        System.out.println("Change App Language called");

        if (radioButtonEnglishVersion.isSelected()){
            main.storeLanguage("eng");
        } else if (radioButtonPolishVersion.isSelected()){
            main.storeLanguage("pl");
        }
        Stage primaryStage = (Stage) radioButtonPolishVersion.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(MenuGoTo.class.getResource(main.getStoredLanguage() + "/settings.fxml")));
        primaryStage.getScene().setRoot(newRoot);

    }
    public  void changeAppName(ActionEvent e) {
        int lengthOfText = Integer.parseInt(String.valueOf(changeAppNameTextField.getLength()));
        if (lengthOfText <= 13) {
            //isTitleChanging = true;
            newTitleToMain = changeAppNameTextField.getText();
            notifyTitleChangeListeners();
            main.storeName(changeAppNameTextField.getText());
        } else if (lengthOfText>13) {
            changeAppNameTextField.clear();
            changeAppNameTextField.setText("MAX 13 CHARACTERS");
        }
    }


    public void goToAddSong(ActionEvent event) throws IOException {

        Stage primaryStage = (Stage) menuAddSongButton.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(MenuGoTo.class.getResource(main.getStoredLanguage() + "/addSongView.fxml")));
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

    public void MakeZip(ActionEvent event) {
        // Tworzenie okna dialogowego typu Alert z paskiem postępu
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Backup Progress");
        alert.setHeaderText("Creating Backup...");
        alert.initOwner(zip.getScene().getWindow());

        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(-1);
        alert.setGraphic(progressBar);

        // Tworzenie instancji klasy BackupCreator i uruchomienie procesu tworzenia kopii zapasowej
        BackupDatabase backupCreator = new BackupDatabase(progressBar,alert);
        backupCreator.createBackup();

        // Wyświetlenie okna dialogowego
        alert.show();
    }

    public void filechooser() throws IOException {
        Stage primaryStage = (Stage) changeIconButton.getScene().getWindow();

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
            iconPreview.setImage(image);

            try {
                // Kopia do folderu src/main/resources z nową nazwą
                java.nio.file.Path sourcePath = selectedFile.toPath();
                Path destinationPath = Path.of("src/main/resources/logo.jpg");
                Files.copy(sourcePath, (java.nio.file.Path) destinationPath, StandardCopyOption.REPLACE_EXISTING);

                // Usuwanie oryginalnego pliku
                Files.delete(sourcePath);

                System.out.println("Operacje zakończone pomyślnie.");

                // Aktualizacja ikony w klasie main
                updateMainIcon(selectedFile);
                primaryStage.setScene(main.scene);
                showConfirmationPopup();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Wystąpił błąd podczas operacji na plikach.");
            }
        }
    }

    public static void updateMainIcon(File selectedFile) {
        try {
            var appIcon = new Image(selectedFile.toURI().toString());
            Stage mainStage = main.getStage();
            if (mainStage != null) {
                mainStage.getIcons().clear();
                mainStage.getIcons().add(appIcon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showConfirmationPopup() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Exit Application");
        alert.setContentText("Are you sure you want to Exit the application? Restarting application is needed in order to update app's icon");


        ButtonType buttonTypeOK = new ButtonType("OK");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);


        alert.showAndWait().ifPresent(result -> {
            if (result == buttonTypeOK) {
                Platform.exit();
                System.out.println("exit application...");

            }
        });
    }


}