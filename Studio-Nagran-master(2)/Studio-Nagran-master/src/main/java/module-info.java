module com.example.studionagran {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.sql;
    requires mysql.connector.java;
    requires java.desktop;


    opens com.example.studionagran to javafx.fxml;
    exports com.example.studionagran;
}