package com.example.studionagran;

import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProgressDialog {

    private ProgressBar progressBar;
    private Label statusLabel;
    private Stage stage;

    public void bindToTask(Task<?> task) {
        progressBar.progressProperty().bind(task.progressProperty());
        statusLabel.textProperty().bind(task.messageProperty());
    }

    public void showProgressBar() {
        VBox root = new VBox(10);
        root.setStyle("-fx-background-color: white; -fx-padding: 10px;");
        progressBar = new ProgressBar();
        statusLabel = new Label();

        root.getChildren().addAll(progressBar, statusLabel);

        Scene scene = new Scene(root, 300, 100);
        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
    }

    public void hideProgressBar() {
        if (stage != null) {
            stage.close();
        }
    }
}
