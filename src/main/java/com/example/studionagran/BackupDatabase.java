package com.example.studionagran;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.concurrent.TimeUnit;
import java.util.zip.*;

public class BackupDatabase {

    private ProgressBar progressBar;
    private Alert alert;

    public BackupDatabase(ProgressBar progressBar, Alert alert) {
        this.progressBar = progressBar;
        this.alert = alert;
    }

    public void createBackup() {
        Task<Void> backupTask = new Task<>() {
            @Override
            protected Void call() throws IOException, SQLException {
                try {
                    updateProgress(0, 1);

                    // Ustawienia bazy danych
                    String dbName = "studionagran";
                    String dbUserName = "root";
                    String dbPassword = "";
                    // Nazwa folderu dla kopii zapasowej (będzie utworzony w folderze aplikacji)
                    String backupFolderName = "backup";

                    // Tworzenie połączenia z bazą danych
                    String url = "jdbc:mysql://localhost:3306/" + dbName + "?useSSL=false&serverTimezone=UTC";
                    Connection connection = DriverManager.getConnection(url, dbUserName, dbPassword);

                    // Pobieranie listy wszystkich tabel w bazie danych
                    DatabaseMetaData metaData = connection.getMetaData();
                    ResultSet resultSet = metaData.getTables(dbName, null, "%", new String[]{"TABLE"});

                    // Tworzenie folderu dla kopii zapasowej
                    String currentDir = System.getProperty("user.dir");
                    File backupFolder = new File(currentDir + File.separator + backupFolderName);
                    backupFolder.mkdirs();

                    // Usuwanie istniejącego pliku ZIP, jeśli istnieje
                    File existingZipFile = new File(backupFolder.getAbsolutePath() + ".zip");
                    if (existingZipFile.exists()) {
                        existingZipFile.delete();
                    }

                    // Iteracja przez wszystkie tabele i tworzenie plików SQL
                    int tableCount = 0;
                    while (resultSet.next()) {
                        String tableName = resultSet.getString(3);
                        String sql = "SELECT * FROM " + tableName;
                        try (Statement statement = connection.createStatement();
                             ResultSet tableResultSet = statement.executeQuery(sql)) {

                            // Tworzenie pliku SQL dla danej tabeli
                            File backupFile = new File(backupFolder.getAbsolutePath() + File.separator + tableName + ".sql");
                            backupFile.createNewFile();

                            // Zapis danych tabeli do pliku SQL
                            try (FileWriter writer = new FileWriter(backupFile)) {
                                while (tableResultSet.next()) {
                                    StringBuilder row = new StringBuilder();
                                    for (int i = 1; i <= tableResultSet.getMetaData().getColumnCount(); i++) {
                                        row.append(tableResultSet.getString(i)).append(",");
                                    }
                                    writer.write(row.toString() + "\n");
                                }
                            }
                        }
                        tableCount++;
                        updateProgress(tableCount, 0);
                    }

                    // Opóźnienie przed kompresowaniem plików do formatu ZIP
                    TimeUnit.SECONDS.sleep(5);

                    // Kompresowanie plików do formatu ZIP
                    zipFolder(backupFolder.getAbsolutePath(), existingZipFile.getAbsolutePath());

                    TimeUnit.SECONDS.sleep(5);

                    updateProgress(1, 1);
                    updateMessage("Backup created successfully");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                Platform.runLater(() -> {
                    progressBar.setProgress(1);
                    alert.setHeaderText("Backup created successfully");

                    //alert.hide();
                });
            }
        };



        new Thread(backupTask).start();
    }public static void zipFolder(String sourceFolderPath, String zipFilePath) throws IOException {
        Path sourceFolder = Paths.get(sourceFolderPath);
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFilePath)))) {
            Files.walk(sourceFolder)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceFolder.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            System.err.println(e);
                        }
                    });
        }
    }
}
