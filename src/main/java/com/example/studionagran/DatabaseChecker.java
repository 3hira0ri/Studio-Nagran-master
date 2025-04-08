package com.example.studionagran;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseChecker {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "studionagran";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static final String BACKUP_FILE_PATH = System.getProperty("user.dir")+"\\studionagran.sql";

    public static void main(String[] args) {
        // Sprawdzenie istnienia bazy danych
        if (!checkDatabaseExists(DB_NAME)) {
            // Jeśli baza danych nie istnieje, utwórz nową bazę danych
            createDatabase(DB_NAME);
            System.out.println("Utworzono nową bazę danych: " + DB_NAME);
            importDatabaseBackup();
        } else {
            System.out.println(BACKUP_FILE_PATH);
            System.out.println("Baza danych już istnieje: " + DB_NAME);
        }
    }

    // Metoda sprawdzająca istnienie bazy danych
    private static boolean checkDatabaseExists(String dbName) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            // Sprawdzenie, czy baza danych istnieje
            String sql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" + dbName + "'";
            return stmt.executeQuery(sql).next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Metoda tworząca nową bazę danych
    private static void createDatabase(String dbName) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            // Utworzenie nowej bazy danych
            String sql = "CREATE DATABASE " + dbName;
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } private static void importDatabaseBackup() {
        try (Connection connection = DriverManager.getConnection(DB_URL+DB_NAME, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             BufferedReader reader = new BufferedReader(new FileReader(BACKUP_FILE_PATH))) {

            String line;
            StringBuilder query = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                query.append(line);
                if (line.trim().endsWith(";")) {
                    statement.execute(query.toString());
                    query.setLength(0);
                }
            }
            System.out.println("Import backupu zakończony pomyślnie.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
