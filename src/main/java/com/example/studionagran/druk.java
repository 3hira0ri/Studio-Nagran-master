package com.example.studionagran;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class druk {
    private static final int COLUMN_COUNT = 8;

    private static final String[] COLUMN_NAMES = {"ID", "Name", "Album", "Author", "Published", "Who Added", "When Added", "Cost"};

    public static void printDatabaseTableToPDF(Connection connection, File outputFile, int userId) {
        List<String[]> tableData = fetchDataFromDatabase(connection, userId);
        createPDF(outputFile, tableData);
    }

    public static List<String[]> fetchDataFromDatabase(Connection connection, int userId) {
        List<String[]> tableData = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            String query = "SELECT s.id, s.name, s.album, s.author, s.Published, " +
                    " s.WhoAdded, s.WhenAdded, s.cost " +
                    "FROM songs s " +
                    "JOIN kupione k ON s.id = k.co " +
                    "WHERE k.kto = " + userId;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String[] rowData = new String[COLUMN_COUNT];
                for (int i = 0; i < COLUMN_COUNT; i++) {
                    rowData[i] = resultSet.getString(i + 1);
                }
                tableData.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tableData;
    }

    private static void createPDF(File outputFile, List<String[]> tableData) {
        File uniqueFile = getUniqueFileName(outputFile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(uniqueFile))) {
            // Dodaj nagłówki tabeli
            for (String header : tableData.get(0)) {
                writer.write(header + "\t");
            }
            writer.write("\n");

            // Dodaj dane tabeli
            for (int i = 1; i < tableData.size(); i++) {
                String[] rowData = tableData.get(i);
                for (String cellData : rowData) {
                    writer.write(cellData + "\t");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File getUniqueFileName(File file) {
        String fileName = file.getName();
        String parentPath = file.getParent();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        int counter = 0;
        File uniqueFile = new File(parentPath, fileName);
        while (uniqueFile.exists()) {
            counter++;
            String newName = baseName + "_" + counter + extension;
            uniqueFile = new File(parentPath, newName);
        }
        return uniqueFile;
    }

    public static void main(String[] args) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();
        File outputFile = new File("database_table.txt");
        int userId = user.id_now; // Tutaj należy podać właściwy identyfikator użytkownika
        printDatabaseTableToPDF(connection, outputFile, userId);
    }
}
