package com.example.studionagran;


import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import static com.example.studionagran.Userslist.pokazWiersze;

public class MyPurchasesController {

    @FXML
    private Button menuAddSongButton;
    @FXML
    private Button goToUserSettingsButton;
    @FXML
    private Button goToPurchases;
    @FXML
    private VBox lista;
    @FXML
    private Label userHowMuchPoints;
    @FXML
    private Label userNameLabel;
    @FXML
    private Pane MyMusic;

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
    public void goToAddSong(ActionEvent event) throws IOException {

        Stage primaryStage = (Stage) menuAddSongButton.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(MenuGoTo.class.getResource(main.getStoredLanguage() + "/addSongView.fxml")));
        primaryStage.getScene().setRoot(newRoot);

    }public void initialize() {
        // Dodajemy menu kontekstowe do całej sceny
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
            ContextMenuHelper.addContextMenuToScene(menuAddSongButton.getScene());
            uzupelnij((Stage) menuAddSongButton.getScene().getWindow());
            userHowMuchPoints.setText("Points: "+String.valueOf(user.karma));
            userNameLabel.setText("Username: "+user.login);

        });

    }

    public void goToAccountSettings(ActionEvent event) throws IOException {

        Stage primaryStage = (Stage) goToUserSettingsButton.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(MenuGoTo.class.getResource(main.getStoredLanguage() + "/accountSettings.fxml")));
        primaryStage.getScene().setRoot(newRoot);

    }
    public void goToAppSettings(ActionEvent event) throws IOException {

        Stage primaryStage = (Stage) goToPurchases.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(MenuGoTo.class.getResource(main.getStoredLanguage() + "/settings.fxml")));
        primaryStage.getScene().setRoot(newRoot);

    }
    public void uzupelnij(Stage primaryStage) {
        System.out.println("Niby działa");
        pokazMuzyke(primaryStage);
    }
    public void pokazMuzyke(Stage primaryStage) {
        TableView<Userslist.Music> tableView = new TableView<>();
        ObservableList<Userslist.Music> data = FXCollections.observableArrayList();
        TableColumn<Userslist.Music, ImageView> coverColumn;
        TableColumn<Userslist.Music, String> NameCol;
        TableColumn<Userslist.Music, String> albumCol;
        TableColumn<Userslist.Music, String> autorColumn;
        TableColumn<Userslist.Music, String> publishedColumn;
        TableColumn<Userslist.Music, String> addedColumn;
        TableColumn<Userslist.Music, Integer> costColumn;
        TableColumn<Userslist.Music, String> showColumn;

        if (main.getStoredLanguage().equals("eng")) {
            coverColumn = new TableColumn<>("Cover");
            NameCol = new TableColumn<>("Name");
            albumCol = new TableColumn<>("album");
            autorColumn = new TableColumn<>("autor");
            publishedColumn = new TableColumn<>("published");
            addedColumn = new TableColumn<>("WhoAdded");
            costColumn = new TableColumn<>("Cost");
            //  TableColumn<Userslist.Music, String> actionColumn = new TableColumn<>("Delete");
            showColumn = new TableColumn<>("Show");

        } else {
            coverColumn = new TableColumn<>("Okładka");
            NameCol = new TableColumn<>("Nazwa");
            albumCol = new TableColumn<>("Album");
            autorColumn = new TableColumn<>("Autor");
            publishedColumn = new TableColumn<>("Data publikacji");
            addedColumn = new TableColumn<>("Kto dodał");
            costColumn = new TableColumn<>("koszt");
            //  TableColumn<Userslist.Music, String> actionColumn = new TableColumn<>("Usuń");
            showColumn = new TableColumn<>("Pokaż");
        }

        tableView.getColumns().addAll(coverColumn, NameCol, albumCol, autorColumn, publishedColumn,costColumn, addedColumn, showColumn);
        DatabaseConnection connectNow = new DatabaseConnection();


        showColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Userslist.Music, String> call(TableColumn<Userslist.Music, String> column) {
                return new TableCell<>() {
                    final Button button = new Button("Pokaż");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            button.setOnAction(event -> {
                                Userslist.Music musicEntry = getTableView().getItems().get(getIndex());
                                pokazSzczegolyPiosenki(primaryStage, musicEntry);
                            });
                            setGraphic(button);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        try (Connection connectDB = connectNow.getConnection();
             Statement statement = connectDB.createStatement()) {
            //String query = "SELECT id, name, album , author,Published,CoverImage, WhoAdded,WhenAdded,cost FROM songs";
            String query = "SELECT s.id, s.name, s.album, s.author, s.Published, " +
                    "s.CoverImage, s.WhoAdded, s.WhenAdded, s.cost " +
                    "FROM songs s " +
                    "JOIN kupione k ON s.id = k.co " +
                    "WHERE k.kto = " + user.id_now;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int Id = resultSet.getInt("id");
                String Name = resultSet.getString("name");
                String album = resultSet.getString("album");
                String autor = resultSet.getString("author");
                String published = resultSet.getString("Published");
                String added = resultSet.getString("WhoAdded");
                String when = resultSet.getString("WhenAdded");
                int cost = resultSet.getInt("cost");
                Userslist.DatabaseManager.downloadPhoto(Id, 2);

                Image image = new Image("file:song" + Id + ".jpg");

                data.add(new Userslist.Music(Id, Name, album, autor, published, added, when, image, cost));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        coverColumn.setCellValueFactory(cellData -> {
            Image image = cellData.getValue().getImage().getImage();
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            return new SimpleObjectProperty<>(imageView);
        });

        NameCol.setCellValueFactory(cellData -> cellData.getValue().NameProperty());
        albumCol.setCellValueFactory(cellData -> cellData.getValue().albumProperty());
        autorColumn.setCellValueFactory(cellData -> cellData.getValue().autorProperty());
        publishedColumn.setCellValueFactory(cellData -> cellData.getValue().publishedProperty());
        addedColumn.setCellValueFactory(cellData -> cellData.getValue().addedProperty());
        costColumn.setCellValueFactory(cellData -> cellData.getValue().costProperty().asObject());
        costColumn.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Integer cost, boolean empty) {
                super.updateItem(cost, empty);
                if (empty || cost == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(cost));
                }
            }
        });
        tableView.setItems(data);
        lista.getChildren().add(tableView);
        if (!tableView.getItems().isEmpty()) {
            Button button = new Button("Wydrukuj paragon");
            button.setOnAction(event -> drukuj());
            lista.getChildren().add(button);
        }
    }public void drukuj(){
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Operacja ukończona!");
        info.setHeaderText("Paragon został zapisany w postaci pliku txt!");
        info.setContentText("Możesz je sprawdzić w folderze aplikacji");

        info.showAndWait();
        druk.main(new String[]{});
    }
    private static void pokazSzczegolyPiosenki(Stage primaryStage, Userslist.Music musicEntry) {
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Szczegóły piosenki");

        VBox vbox = new VBox();
        vbox.getChildren().add(new Label("ID: " + musicEntry.IdProperty().get()));
        vbox.getChildren().add(new Label("Name: " + musicEntry.NameProperty().get()));
        vbox.getChildren().add(new Label("Album: " + musicEntry.albumProperty().get()));
        vbox.getChildren().add(new Label("Autor: " + musicEntry.autorProperty().get()));
        vbox.getChildren().add(new Label("Published: " + musicEntry.publishedProperty().get()));
        vbox.getChildren().add(new Label("Who Added: " + musicEntry.addedProperty().get()));
        vbox.getChildren().add(new Label("When Added: " + musicEntry.whenProperty().get()));
        vbox.getChildren().add(new Label("Cost: " + musicEntry.costProperty().get()));
        Scene secondaryScene = new Scene(vbox, 300, 200);
        secondaryStage.setScene(secondaryScene);
        secondaryStage.show();
    }
    public static class Music {
        private final SimpleIntegerProperty Id;
        private final SimpleIntegerProperty cost;
        private final SimpleStringProperty Name;
        private final SimpleStringProperty album;
        private final SimpleStringProperty autor;
        private final SimpleStringProperty published;
        private final SimpleStringProperty added;
        private final SimpleStringProperty when;
        private final ImageView image;

        public Music(int Id, String Name, String album, String autor, String published, String added, String when, Image image, int cost) {
            this.Id = new SimpleIntegerProperty(Id);
            this.Name = new SimpleStringProperty(Name);
            this.album = new SimpleStringProperty(album);
            this.autor = new SimpleStringProperty(autor);
            this.published = new SimpleStringProperty(published);
            this.added = new SimpleStringProperty(added);
            this.when = new SimpleStringProperty(when);
            this.cost = new SimpleIntegerProperty(cost);
            this.image = new ImageView(image);
        }
        public SimpleIntegerProperty IdProperty() {
            return Id;
        }
        public SimpleIntegerProperty costProperty() {
            return cost;
        }
        public SimpleStringProperty NameProperty() {
            return Name;
        }
        public SimpleStringProperty albumProperty() {
            return album;
        }
        public SimpleStringProperty autorProperty() {
            return autor;
        }
        public SimpleStringProperty publishedProperty() {
            return published;
        }
        public SimpleStringProperty addedProperty() {
            return added;
        }
        public SimpleStringProperty whenProperty() {
            return when;
        }
        public ImageView getImage() {
            return image;
        }
    }



}
