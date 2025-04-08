package com.example.studionagran;

import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ContextMenuHelper {

    public static ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Brak pomysłu 1");
        MenuItem menuItem2 = new MenuItem("Brak pomysłu 2");
        contextMenu.getItems().addAll(menuItem1, menuItem2);
        return contextMenu;
    }

    public static void addContextMenuToScene(Scene node) {
        ContextMenu contextMenu = createContextMenu();
        node.setOnMouseClicked(event ->
        {
            if(event.getButton() == MouseButton.SECONDARY){
                contextMenu.show(node.getWindow(), event.getScreenX(), event.getScreenY());
            }
        });
    }
}
