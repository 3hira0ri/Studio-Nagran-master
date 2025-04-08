module com.example.studionagran {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires javafx.base;
    requires java.xml;
    requires java.sql;
    requires mysql.connector.java;
    requires java.desktop;
    requires java.prefs;

    opens com.example.studionagran to javafx.fxml;
    exports com.example.studionagran;
}