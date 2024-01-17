package com.example.studionagran;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

public class Userslist{
    public static void pokaz(Stage primaryStage) {
        TableView<Person> tableView = new TableView<>();
        ObservableList<Person> data = FXCollections.observableArrayList();

        TableColumn<Person, String> firstNameCol = new TableColumn<>("Login");
        TableColumn<Person, String> lastNameCol = new TableColumn<>("Password");
        TableColumn<Person, String> actionColumn = new TableColumn("Butto");
        tableView.getColumns().addAll(firstNameCol, lastNameCol,actionColumn);
        DatabaseConnection connectNow=new DatabaseConnection();
        // Create a cell factory to add a button to the action column
        actionColumn.setCellFactory(new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {
            @Override
            public TableCell<Person, String> call(TableColumn<Person, String> column) {
                return new TableCell<Person, String>() {
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
                                String verifyLogin2 = "DELETE FROM useraccounts WHERE userId = "+person.IdProperty().get();

                                try{

                                    Statement statement2 =connectDB.createStatement();
                                    statement2.executeUpdate(verifyLogin2);

                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
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
            Parent newRoot = null;
            try {
                newRoot = FXMLLoader.load(Userslist.class.getResource("User.fxml"));
                primaryStage.getScene().setRoot(newRoot);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> {
            Parent newRoot = null;
            try {
                newRoot = FXMLLoader.load(Userslist.class.getResource("view.fxml"));
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
}
