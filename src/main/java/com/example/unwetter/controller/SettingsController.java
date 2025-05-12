package com.example.unwetter.controller;

import com.example.unwetter.dao.BenutzerDAO;
import com.example.unwetter.dao.UnwetterDAO;
import com.example.unwetter.model.Benutzer;
import com.example.unwetter.model.Unwetter;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class SettingsController {

    @FXML private Label usernameLabel;
    @FXML private BorderPane rootPane;

    @FXML
    public void initialize() {
        if (Session.getAktuellerBenutzer() != null) {
            var benutzer = Session.getAktuellerBenutzer();
            usernameLabel.setText("Benutzer: " + benutzer.getBenutzername() + " (" + benutzer.getRolle() + ")");
        }
    }

    @FXML
    private void handleChangePassword() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Passwort ändern");
        dialog.setHeaderText("Neues Passwort eingeben");
        dialog.setContentText("Neues Passwort:");

        dialog.showAndWait().ifPresent(newPassword -> {
            if (newPassword.trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Fehler", "Das Passwort darf nicht leer sein.");
                return;
            }

            Benutzer aktuellerBenutzer = Session.getAktuellerBenutzer();
            if (aktuellerBenutzer != null) {
                aktuellerBenutzer.setPasswort(newPassword);

                // Сохраняем в БД
                BenutzerDAO dao = new BenutzerDAO();
                dao.updatePasswort(aktuellerBenutzer.getId(), newPassword);

                showAlert(Alert.AlertType.INFORMATION, "Erfolg", "Passwort erfolgreich geändert.");
            }
        });
    }

    @FXML
    private void handleExportCSV() {
        try {
            List<Unwetter> daten = new UnwetterDAO().getAllUnwetter();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("CSV speichern");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Dateien", "*.csv"));

            File file = fileChooser.showSaveDialog(rootPane.getScene().getWindow());
            if (file != null) {
                try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
                    // UTF-8 BOM для корректного отображения в Excel
                    writer.write('\uFEFF');

                    writer.println("ID;Art;Datum;Ort;Schadenhöhe;Bemerkung");
                    for (Unwetter u : daten) {
                        writer.printf("%d;%s;%s;%s;%.2f;%s%n",
                                u.getId(),
                                u.getUnwetterart(),
                                u.getDatum().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                u.getOrt(),
                                u.getSchadenhoehe(),
                                u.getBemerkung());
                    }
                }
                showAlert(Alert.AlertType.INFORMATION, "Export", "CSV-Datei wurde erfolgreich gespeichert.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler beim Exportieren.");
        }


    }
    @FXML
    private void handleReload() {
        System.out.println("🔄 Reload clicked");

        Benutzer aktuellerBenutzer = Session.getAktuellerBenutzer();

        if (aktuellerBenutzer != null) {
            // Перезагрузить данные пользователя из БД (по имени)
            BenutzerDAO dao = new BenutzerDAO();
            Benutzer aktualisierterBenutzer = dao.login(aktuellerBenutzer.getBenutzername(), aktuellerBenutzer.getPasswort());

            if (aktualisierterBenutzer != null) {
                Session.setAktuellerBenutzer(aktualisierterBenutzer);

                // Обновить лейбл с именем и ролью
                usernameLabel.setText("Benutzer: " + aktualisierterBenutzer.getBenutzername() + " (" + aktualisierterBenutzer.getRolle() + ")");
            }
        }
    }



    @FXML
    private void handleClearTestData() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Möchten Sie wirklich alle Testdaten löschen?", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Bestätigung");
        confirm.setHeaderText(null);

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                UnwetterDAO dao = new UnwetterDAO();
                dao.deleteTestdaten();

                showAlert(Alert.AlertType.INFORMATION, "Löschen", "Testdaten wurden gelöscht.");
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
