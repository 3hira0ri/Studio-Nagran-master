package com.example.studionagran;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Optional;

public class Userslist extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        pokazWiersze(primaryStage, user.isAdmin);
    }

    public static void pokazWiersze(Stage primaryStage, boolean isAdmin) {
        TableView<Person> tableView = new TableView<>();
        ObservableList<Person> data = FXCollections.observableArrayList();
        System.out.println(isAdmin);
        TableColumn<Person, ImageView> AvatarCol = new TableColumn<>("Avatar");
        TableColumn<Person, String> LoginCol = new TableColumn<>("Login");
        TableColumn<Person, String> PasswordCol = new TableColumn<>("Password");
        TableColumn<Person, String> NameCol = new TableColumn<>("Name");
        TableColumn<Person, String> lastNameCol = new TableColumn<>("lastName");
        TableColumn<Person, String> IsAdminCol = new TableColumn<>("role");
        TableColumn<Person, String> MoneyCol = new TableColumn<>("KarmaPoints");
        TableColumn<Person, String> actionColumn = new TableColumn<>("Delete");
        TableColumn<Person, String> showColumn = new TableColumn<>("Pokaż");

        tableView.getColumns().addAll(AvatarCol, LoginCol, PasswordCol, NameCol, lastNameCol, IsAdminCol, MoneyCol, actionColumn, showColumn);

        DatabaseConnection connectNow = new DatabaseConnection();

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
                                Person person = getTableView().getItems().get(getIndex());
                                Connection connectDB = connectNow.getConnection();
                                String verifyLogin2 = "DELETE FROM useraccounts WHERE userId = " + person.IdProperty().get();

                                try {
                                    Statement statement2 = connectDB.createStatement();
                                    statement2.executeUpdate(verifyLogin2);
                                    menu.checkAndUpdate();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                pokazWiersze(primaryStage, isAdmin);
                            });
                            button.setVisible(isAdmin); // Ustawienie widoczności guzika na podstawie isAdmin
                            setGraphic(button);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });


        showColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Person, String> call(TableColumn<Person, String> column) {
                return new TableCell<>() {
                    final Button button = new Button("Pokaż");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            button.setOnAction(event -> {
                                Person person = getTableView().getItems().get(getIndex());
                                pokazSzczegolyWiersza(primaryStage, person);
                            });
                            button.setVisible(isAdmin);
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
            String query = "SELECT * FROM useraccounts";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int Id = resultSet.getInt("userId");
                String firstName = resultSet.getString("Name");
                String lastName = resultSet.getString("SecondName");
                String login = resultSet.getString("userLogin");
                String password = resultSet.getString("userPassword");
                String IsAdmin = resultSet.getString("IsAdmin");
                String Money = resultSet.getString("Money");

                DatabaseManager.downloadPhoto(Id, 1);

                Image Avatar = new Image("file:user" + Id + ".jpg");

                data.add(new Person(Id, login, password, firstName, lastName, IsAdmin, Money, Avatar));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        AvatarCol.setCellValueFactory(cellData -> {
            Image Avatar = cellData.getValue().getImage().getImage();
            ImageView imageView = new ImageView(Avatar);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            return new SimpleObjectProperty<>(imageView);
        });

        NameCol.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameCol.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        PasswordCol.setCellValueFactory(cellData -> cellData.getValue().PasswordProperty());
        MoneyCol.setCellValueFactory(cellData -> cellData.getValue().MoneyProperty());
        IsAdminCol.setCellValueFactory(cellData -> cellData.getValue().IsAdminProperty());
        LoginCol.setCellValueFactory(cellData -> cellData.getValue().LoginProperty());

        tableView.setItems(data);
        VBox vBox = new VBox(tableView);
        Scene scene = new Scene(vBox, 1400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Database TableView Example");
        primaryStage.setFullScreen(false);
        primaryStage.show();
    }

    private static void pokazSzczegolyWiersza(Stage primaryStage, Person person) {
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Szczegóły użytkownika");

        VBox vbox = new VBox();
        vbox.getChildren().add(new Label("ID: " + person.IdProperty().get()));
        vbox.getChildren().add(new Label("Login: " + person.LoginProperty().get()));
        vbox.getChildren().add(new Label("Password: " + person.PasswordProperty().get()));
        vbox.getChildren().add(new Label("Name: " + person.firstNameProperty().get()));
        vbox.getChildren().add(new Label("Last Name: " + person.lastNameProperty().get()));
        vbox.getChildren().add(new Label("Is Admin: " + person.IsAdminProperty().get()));
        vbox.getChildren().add(new Label("Money: " + person.MoneyProperty().get()));
        // Dodaj przycisk "Nadaj admina" do VBox
        Button oddajAdminaButton = new Button("Oddaj admina");
        oddajAdminaButton.setOnAction(event -> {
            // Wywołaj metodę aktualizacji statusu admina
            updateUserAdminStatus(person.IdProperty().get(), Boolean.parseBoolean(person.IsAdminProperty().get()),secondaryStage,primaryStage);
        });
        Button oddajAdminaButton2 = new Button("Nadaj admina");
        oddajAdminaButton2.setOnAction(event -> {
            // Wywołaj metodę aktualizacji statusu admina
            updateUserAdminStatus2(person.IdProperty().get(), Boolean.parseBoolean(person.IsAdminProperty().get()),secondaryStage,primaryStage,person.LoginProperty().get());
        });
        // Dodaj przycisk "Usuń admina" do VBox
        Button oddajAdminaButton3 = new Button("Usuń admina");
        oddajAdminaButton3.setOnAction(event -> {
            // Wywołaj metodę aktualizacji statusu admina
            updateUserAdminStatus3(person.IdProperty().get(), Boolean.parseBoolean(person.IsAdminProperty().get()),secondaryStage,primaryStage,person.LoginProperty().get());
        });
        vbox.getChildren().add(oddajAdminaButton);
        vbox.getChildren().add(oddajAdminaButton2);
        vbox.getChildren().add(oddajAdminaButton3);
        Scene secondaryScene = new Scene(vbox, 300, 200);
        secondaryStage.setScene(secondaryScene);
        secondaryStage.show();
    }private static void updateUserAdminStatus(int userId, boolean isAdmin,Stage secondaryStage,Stage primaryStage) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Oddanie rangi");
        alert.setHeaderText("Jesteś pewien że chcesz oddać swoją rangę?");
        alert.setContentText("Oddanie rangi spowoduje stracenie uprawnień administratora!");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
        String query = "UPDATE useraccounts SET IsAdmin = ? WHERE userId = ?";
        try (Connection connectDB = connectNow.getConnection();
             PreparedStatement preparedStatement = connectDB.prepareStatement(query)){
            preparedStatement.setString(1, "1");
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
            preparedStatement.setString(1, "0");
            preparedStatement.setInt(2, user.id_now);
            preparedStatement.executeUpdate();
            user.isAdmin=false;
            secondaryStage.hide();
            pokazWiersze(primaryStage,user.isAdmin);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd podczas aktualizacji uprawnień administratora.");
        }}else {
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Warning!!!");
            war.setHeaderText("Oddanie rangi zostało anulowane!");
            war.setContentText("Twoja ranga, tak jak osoby którą wybrałeś, zostają bez zmiany :)");

            war.showAndWait();

        }
    }
    private static void updateUserAdminStatus2(int userId, boolean isAdmin,Stage secondaryStage,Stage primaryStage,String login) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Nadanie rangi");
        alert.setHeaderText("Jesteś pewien że chcesz nadać rangę administratora użytkownikowi?\n"+login);
        alert.setContentText("Z wielką mocą wiąże się wielka odpowiedzialność :)!");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            String query = "UPDATE useraccounts SET IsAdmin = ? WHERE userId = ?";
            try (Connection connectDB = connectNow.getConnection();
                 PreparedStatement preparedStatement = connectDB.prepareStatement(query)) {
                preparedStatement.setString(1, "1");
                preparedStatement.setInt(2, userId);
                preparedStatement.executeUpdate();
                secondaryStage.hide();
                pokazWiersze(primaryStage, user.isAdmin);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd podczas aktualizacji uprawnień administratora.");
            }
        } else {
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Warning!!!");
            war.setHeaderText("Nadanie rangi zostało anulowane!");
            war.setContentText("Ranga osoby którą wybrałeś zostaje bez zmiany, lowly peasant :)");
            war.showAndWait();

        }
    }
    private static void updateUserAdminStatus3(int userId, boolean isAdmin,Stage secondaryStage,Stage primaryStage,String login) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Usunięcie rangi");
        alert.setHeaderText("Jesteś pewien że chcesz usunąć rangę administratora użytkownikowi?\n"+login);
        alert.setContentText("Z wielką mocą wiąże się wielka odpowiedzialność, ale nie każdy jest ją spełnia :)!");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK && userId > user.id_now) {
            String query = "UPDATE useraccounts SET IsAdmin = ? WHERE userId = ?";
            try (Connection connectDB = connectNow.getConnection();
                 PreparedStatement preparedStatement = connectDB.prepareStatement(query)) {
                preparedStatement.setString(1, "0");
                preparedStatement.setInt(2, userId);
                preparedStatement.executeUpdate();
                secondaryStage.hide();
                pokazWiersze(primaryStage, user.isAdmin);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd podczas aktualizacji uprawnień administratora.");
            }
        } else if (result.get() == ButtonType.OK) {
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Warning!!!");
            war.setHeaderText("Nadanie rangi zostało anulowane!");
            war.setContentText("Wygląda na to że ta osoba jest starsza stażem (a może próbujesz usunąć rangę sobie?)");
            war.showAndWait();
            pokazWiersze(primaryStage,user.isAdmin);
            }else{
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Warning!!!");
            war.setHeaderText("Nadanie rangi zostało anulowane!");
            war.setContentText("Niech jeszcze nacieszy się tą rangą :D");
            war.showAndWait();
            pokazWiersze(primaryStage,user.isAdmin);
            }
        }{
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Warning!!!");
            war.setHeaderText("Nadanie rangi zostało anulowane!");
            war.setContentText("Ranga osoby którą wybrałeś zostaje bez zmiany, lowly peasant :)");
            war.showAndWait();

        }
    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static class Person {
        private final SimpleIntegerProperty Id;
        private final SimpleStringProperty login;
        private final SimpleStringProperty password;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty IsAdmin;
        private final SimpleStringProperty Money;
        private final ImageView Avatar;

        public Person(int Id,String login,String password, String firstName, String lastName, String IsAdmin,String Money, Image Avatar) {
            this.Id = new SimpleIntegerProperty(Id);
            this.login = new SimpleStringProperty(login);
            this.password = new SimpleStringProperty(password);
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.IsAdmin = new SimpleStringProperty(IsAdmin);
            this.Money = new SimpleStringProperty(Money);
            this.Avatar = new ImageView(Avatar);
        }
        public SimpleIntegerProperty IdProperty() {
            return Id;
        }
        public SimpleStringProperty firstNameProperty() {
            return firstName;
        }
        public SimpleStringProperty lastNameProperty() {
            return lastName;
        }
        public SimpleStringProperty LoginProperty() {
            return login;
        }
        public SimpleStringProperty PasswordProperty() {
            return password;
        }
        public SimpleStringProperty IsAdminProperty() {
            return IsAdmin;
        }
        public SimpleStringProperty MoneyProperty() {
            return Money;
        }
        public ImageView getImage() {
            return Avatar;
        }

    }
    public static void pokazMuzyke(Stage primaryStage, boolean isAdmin) {
        TableView<Music> tableView = new TableView<>();
        ObservableList<Music> data = FXCollections.observableArrayList();

        TableColumn<Music, ImageView> coverColumn = new TableColumn<>("Cover");
        TableColumn<Music, String> NameCol = new TableColumn<>("Name");
        TableColumn<Music, String> albumCol = new TableColumn<>("album");
        TableColumn<Music, String> autorColumn = new TableColumn<>("autor");
        TableColumn<Music, String> publishedColumn = new TableColumn<>("published");
        TableColumn<Music, String> addedColumn = new TableColumn<>("WhoAdded");
        TableColumn<Music, Integer> costColumn = new TableColumn<>("Cost");
        TableColumn<Music, String> actionColumn = new TableColumn<>("Delete");
        TableColumn<Music, String> showColumn = new TableColumn<>("Pokaż");

        tableView.getColumns().addAll(coverColumn, NameCol, albumCol, autorColumn, publishedColumn,costColumn, addedColumn, actionColumn, showColumn);

        DatabaseConnection connectNow = new DatabaseConnection();

        actionColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Music, String> call(TableColumn<Music, String> column) {
                return new TableCell<>() {
                    final Button button = new Button("Delete");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            button.setOnAction(event -> {
                                Music musicEntry = getTableView().getItems().get(getIndex());
                                Connection connectDB = connectNow.getConnection();
                                String verifyLogin2 = "DELETE FROM songs WHERE Id = " + musicEntry.IdProperty().get();
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Delete song");
                                alert.setHeaderText("Are you sure you want to delete song with these informations?" + "\nName: "+musicEntry.NameProperty().get()+"\nBy: "+musicEntry.autorProperty().get());
                                alert.setContentText("Are you ok with this?");

                                Optional<ButtonType> resultinfo = alert.showAndWait();
                                if (resultinfo.get() == ButtonType.OK){
                                try {
                                    Statement statement2 = connectDB.createStatement();
                                    statement2.executeUpdate(verifyLogin2);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                                    info.setTitle("Delete song");
                                    info.setHeaderText("Deleting song ended succesfully!");
                                    info.setContentText("Let's refresh song list.");
                                    info.showAndWait();
                                pokazMuzyke(primaryStage,user.isAdmin);}else{
                                    Alert war = new Alert(Alert.AlertType.WARNING);
                                    war.setTitle("Deleting song canceled!!!");
                                    war.setHeaderText("Deleting song ended in failure!");
                                    war.setContentText("Songs wasn't deleted.");
                                    war.showAndWait();
                                }
                            });
                            button.setVisible(isAdmin);
                            setGraphic(button);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        showColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Music, String> call(TableColumn<Music, String> column) {
                return new TableCell<>() {
                    final Button button = new Button("Pokaż");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            button.setOnAction(event -> {
                                Music musicEntry = getTableView().getItems().get(getIndex());
                                pokazSzczegolyPiosenki(primaryStage, musicEntry);
                            });
                            button.setVisible(isAdmin);
                            setGraphic(button);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        TableColumn<Music, String> buttonColumn = new TableColumn<>("Kup");
        buttonColumn.setCellFactory(column -> {
            return new TableCell<Music, String>() {
                final Button button = new Button("Zakup");

                {
                    button.setOnAction(event -> {
                        Music musicEntry2 = getTableView().getItems().get(getIndex());
                        Kup(primaryStage, musicEntry2);
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(button);
                    }
                }
            };
        });

        tableView.getColumns().add(buttonColumn);
        try (Connection connectDB = connectNow.getConnection();
             Statement statement = connectDB.createStatement()) {
            String query = "SELECT id, name, album , author,Published,CoverImage, WhoAdded,WhenAdded,cost FROM songs";
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
                DatabaseManager.downloadPhoto(Id, 2);
                System.out.println("baza danych: "+published);
                Image image = new Image("file:song" + Id + ".jpg");

                data.add(new Music(Id, Name, album, autor, published, added, when, image, cost));
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
        VBox vBox = new VBox(tableView);
        Scene scene = new Scene(vBox, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Baza piosenek");
        primaryStage.setFullScreen(false);
        primaryStage.show();
    }

    private static void pokazSzczegolyPiosenki(Stage primaryStage, Music musicEntry) {
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
        System.out.println("Music entry"+musicEntry.publishedProperty().get());
        Scene secondaryScene = new Scene(vbox, 300, 200);
        secondaryStage.setScene(secondaryScene);
        secondaryStage.show();
    }

    private static void Kup(Stage primaryStage, Music musicEntry) {
        if(user.karma>=musicEntry.costProperty().get()){
            user.karma=user.karma-musicEntry.costProperty().get();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String verifyLogin2 = "INSERT INTO `kupione` (`id`, `co`, `kto`) VALUES (NULL, '"+musicEntry.IdProperty().get()+"', '"+user.id_now+"')";
        String UpdateKarma = "UPDATE `useraccounts` SET `Money` = '"+user.karma+"' WHERE `useraccounts`.`userId` ='"+user.id_now+"'";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Buy song");
            alert.setHeaderText("Are you sure you want to Buy song with these informations?" + "\nName: "+musicEntry.NameProperty().get()+"\nBy: "+musicEntry.autorProperty().get());
            alert.setContentText("It cost: "+musicEntry.costProperty().get()+"and you have: "+user.karma);

            Optional<ButtonType> resultinfo = alert.showAndWait();
            if (resultinfo.get() == ButtonType.OK) {
                try {
                    Statement statement2 = connectDB.createStatement();
                    statement2.executeUpdate(verifyLogin2);
                    statement2.executeUpdate(UpdateKarma);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Congratulations!!!");
                info.setHeaderText("This song was added to your collection!");
                info.setContentText("You can check it in my purchases.");
                info.showAndWait();
            }
        pokazMuzyke(primaryStage,user.isAdmin);}else{

            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Warning!!!");
            war.setHeaderText("Próba zakupienia nieudana!");
            war.setContentText("Kwota potrzebna do zakupu: "+musicEntry.costProperty().get()+", posiadana karma:"+user.karma+".");

            war.showAndWait();
        }
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
    @SuppressWarnings("CallToPrintStackTrace")
    public static class DatabaseManager {
        public static void downloadPhoto(int photoId, int select) {

            if (select == 1) {
                try {
                    // Wczytaj sterownik JDBC
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    // Połącz z bazą danych
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/studionagran", "root", "");
                    // Przygotuj zapytanie SQL
                    String sql = "SELECT Avatar FROM useraccounts WHERE userid = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        // Ustaw identyfikator zdjęcia
                        preparedStatement.setInt(1, photoId);

                        // Wykonaj zapytanie
                        ResultSet resultSet = preparedStatement.executeQuery();

                        // Jeśli znaleziono zdjęcie, zapisz je do pliku
                        if (resultSet.next()) {
                            InputStream inputStream = resultSet.getBinaryStream("Avatar");
                            FileOutputStream outputStream = new FileOutputStream("user"+photoId + ".jpg");

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
            } else if (select == 2) {

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
                            FileOutputStream outputStream = new FileOutputStream("song"+photoId + ".jpg");

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

}
