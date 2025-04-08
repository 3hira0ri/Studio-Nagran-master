package com.example.studionagran;

import javafx.scene.Scene;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;

public class ZoomHandler {

    public static void addZoomHandler(Scene scene, Pane pane) {
        Scale scaleTransform = new Scale(1, 1);

        pane.getTransforms().add(scaleTransform);

        scene.addEventFilter(ScrollEvent.SCROLL, event -> {
            double scaleFactor = (event.getDeltaY() > 0) ? 1.1 : 0.9; // Zmiana skali

            double currentScaleX = scaleTransform.getX();
            double currentScaleY = scaleTransform.getY();

            double newScaleX = Math.max(0.1, Math.min(10, currentScaleX * scaleFactor));
            double newScaleY = Math.max(0.1, Math.min(10, currentScaleY * scaleFactor));

            scaleTransform.setX(newScaleX);
            scaleTransform.setY(newScaleY);

            event.consume();
        });
    }
}
