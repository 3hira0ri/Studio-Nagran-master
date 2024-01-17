package com.example.studionagran;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Objects;

public class Userslist{
    public static void pokaz(Stage primaryStage) {
        TableView<Person> tableView = new TableView<>();
        ObservableList<Person> data = FXCollections.observableArrayList();

        TableColumn<Person, String> firstNameCol = new TableColumn<>("Login");
        TableColumn<Person, String> lastNameCol = new TableColumn<>("Password");
        TableColumn<Person, String> actionColumn = new TableColumn<>("Butto");
        tableView.getColumns().addAll(firstNameCol, lastNameCol,actionColumn);
        DatabaseConnection connectNow=new DatabaseConnection();
        // Create a cell factory to add a button to the action column
        actionColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Person, String> call(TableColumn<Person, String> column) {
                return new TableCell<>() {
                    final Button button = new Button("Delete");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            button.setOnAction(event -> {
                                // Handle the button click action here
                                // For example, you can access the associated person object:
                                Person person = getTableView().getItems().get(getIndex());
                                Connection connectDB = connectNow.getConnection(); //System.out.println("Button clicked for " + person.firstNameProperty().toString());
                                String verifyLogin2 = "DELETE FROM useraccounts WHERE userId = " + person.IdProperty().get();

                                try {

                                    Statement statement2 = connectDB.createStatement();
                                    statement2.executeUpdate(verifyLogin2);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                pokaz(primaryStage);
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
            String query = "SELECT userId, userLogin, userPassword FROM useraccounts";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String Id = resultSet.getString("userId");
                String firstName = resultSet.getString("userLogin");
                String lastName = resultSet.getString("userPassword");
                data.add(new Person(Id, firstName, lastName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        firstNameCol.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameCol.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        tableView.setItems(data);

        // Create a logout button
        Button CancelButton = new Button("Go back");
        CancelButton.setOnAction(event -> {
            Parent newRoot;
            try {
                newRoot = FXMLLoader.load(Objects.requireNonNull(Userslist.class.getResource("User.fxml")));
                primaryStage.getScene().setRoot(newRoot);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> {
            Parent newRoot;
            try {
                newRoot = FXMLLoader.load(Objects.requireNonNull(Userslist.class.getResource("view.fxml")));
                primaryStage.getScene().setRoot(newRoot);
                user.login = null;
                user.password = null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        // Create a VBox to stack the logout button on top of the TableView
        HBox Buttonbox = new HBox(CancelButton, logoutButton);
        VBox vBox = new VBox(Buttonbox, tableView);
        Scene scene = new Scene(vBox,600, 400);
        ZoomHandler.addZoomHandler(scene, vBox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Database TableView Example");
        primaryStage.setFullScreen(false);
        primaryStage.show();
    }

    public static class Person {
        private final SimpleStringProperty Id;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;

        public Person(String Id, String firstName, String lastName) {
            this.Id = new SimpleStringProperty(Id);
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
        }
        public SimpleStringProperty IdProperty() {
            return Id;
        }
        public SimpleStringProperty firstNameProperty() {
            return firstName;
        }

        public SimpleStringProperty lastNameProperty() {
            return lastName;
        }
    }
    public static void pokazMuzyke(Stage primaryStage) {
        TableView<music> tableView = new TableView<>();
        ObservableList<music> data = FXCollections.observableArrayList();
        TableColumn<music, ImageView> coverColumn = new TableColumn<>("Cover");
        TableColumn<music, String> NameCol = new TableColumn<>("Name");
        TableColumn<music, String> albumCol = new TableColumn<>("album");
        TableColumn<music, String> autorColumn = new TableColumn<>("autor");
        TableColumn<music, String> publishedColumn = new TableColumn<>("published");
        TableColumn<music, String> addedColumn = new TableColumn<>("WhoAdded");
        TableColumn<music, String> whenColumn = new TableColumn<>("WhenAdded");
        TableColumn<music, String> actionColumn = new TableColumn<>("Button");
        tableView.getColumns().addAll(coverColumn ,NameCol, albumCol,autorColumn,publishedColumn,addedColumn,whenColumn,actionColumn);
        DatabaseConnection connectNow=new DatabaseConnection();
        // Create a cell factory to add a button to the action column
        actionColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<music, String> call(TableColumn<music, String> column) {
                return new TableCell<>() {
                    final Button button = new Button("Delete");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            button.setOnAction(event -> {
                                // Handle the button click action here
                                // For example, you can access the associated person object:
                               // music Music = getTableView().getItems().get(getIndex());
                                Connection connectDB = connectNow.getConnection();
                                music musicEntry = getTableView().getItems().get(getIndex());
                                String verifyLogin2 = "DELETE FROM songs WHERE Id = " + musicEntry.IdProperty().get();

                                try {

                                    Statement statement2 = connectDB.createStatement();
                                    statement2.executeUpdate(verifyLogin2);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }pokazMuzyke(primaryStage);

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
            String query = "SELECT id, name, album , author,Published,CoverImage, WhoAdded,WhenAdded FROM songs";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {

                int Id = resultSet.getInt("id");
                String Name = resultSet.getString("name");
                String album = resultSet.getString("album");
                String autor = resultSet.getString("author");
                String published = resultSet.getString("Published");
                String added = resultSet.getString("WhoAdded");
                String when = resultSet.getString("WhenAdded");
                //poczatek
                // Pobierz zdjęcie z bazy danych (zmień 1 na rzeczywisty identyfikator zdjęcia)
                DatabaseManager.downloadPhoto(Id);

                // Załaduj zdjęcie z pliku
               Image image = new Image("file:"+Id+".jpg");

                // Wyświetl zdjęcie w kontrolce ImageView
               // ImageView imageView = new ImageView(image);
                //koniec
                data.add(new music(Id, Name, album,autor,published,added,when, image));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        coverColumn.setCellValueFactory(cellData -> {
            Image image = cellData.getValue().getImage().getImage();
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(50); // Ustaw odpowiednią wysokość obrazu
            imageView.setFitWidth(50);  // Ustaw odpowiednią szerokość obrazu
            return new SimpleObjectProperty<>(imageView);
        });
        NameCol.setCellValueFactory(cellData -> cellData.getValue().NameProperty());
        albumCol.setCellValueFactory(cellData -> cellData.getValue().albumProperty());
        autorColumn.setCellValueFactory(cellData -> cellData.getValue().autorProperty());
        publishedColumn.setCellValueFactory(cellData -> cellData.getValue().publishedProperty());
        addedColumn.setCellValueFactory(cellData -> cellData.getValue().addedProperty());
        whenColumn.setCellValueFactory(cellData -> cellData.getValue().whenProperty());

        tableView.setItems(data);
        // Create a VBox to stack the logout button on top of the TableView
        HBox Buttonbox = new HBox();
        VBox vBox = new VBox(Buttonbox, tableView);
        Scene scene = new Scene(vBox,600, 400);
        ZoomHandler.addZoomHandler(scene, vBox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Baza piosenek");
        primaryStage.setFullScreen(false);
        primaryStage.show();
    }
    public static class music {
        private final SimpleIntegerProperty Id;
        private final SimpleStringProperty Name;
        private final SimpleStringProperty album;
        private final SimpleStringProperty autor;
        private final SimpleStringProperty published;
        private final SimpleStringProperty added;
        private final SimpleStringProperty when;
        private final ImageView image;

        public music(int Id, String Name, String album, String autor, String published, String added, String when, Image image) {
            this.Id = new SimpleIntegerProperty(Id);
            this.Name = new SimpleStringProperty(Name);
            this.album = new SimpleStringProperty(album);
            this.autor = new SimpleStringProperty(autor);
            this.published = new SimpleStringProperty(published);
            this.added = new SimpleStringProperty(added);
            this.when = new SimpleStringProperty(when);
            this.image = new ImageView(image);
        }

        public SimpleIntegerProperty IdProperty() {
            return Id;
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
    public static class DatabaseManager {
        public static void downloadPhoto(int photoId) {
            try {
                // Wczytaj sterownik JDBC
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Połącz z bazą danych
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/studionagran", "root", "");

                // Przygotuj zapytanie SQL
                String sql = "SELECT CoverImage FROM songs WHERE id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    // Ustaw identyfikator zdjęcia
                    preparedStatement.setInt(1, photoId);

                    // Wykonaj zapytanie
                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Jeśli znaleziono zdjęcie, zapisz je do pliku
                    if (resultSet.next()) {
                        InputStream inputStream = resultSet.getBinaryStream("CoverImage");
                        FileOutputStream outputStream = new FileOutputStream(photoId+".jpg");

                        byte[] buffer = new byte[1024];
                        while (inputStream.read(buffer) > 0) {
                            outputStream.write(buffer);
                        }

                        inputStream.close();
                        outputStream.close();
                    }
                }

                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
