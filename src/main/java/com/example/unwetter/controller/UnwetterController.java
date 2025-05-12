package com.example.unwetter.controller;

import com.example.unwetter.model.Unwetter;
import com.example.unwetter.dao.UnwetterDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.unwetter.model.Benutzer;
import javafx.scene.layout.HBox;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UnwetterController {

    @FXML private TextField artField;
    @FXML private DatePicker datumPicker;
    @FXML private TextField ortField;
    @FXML private TextField schadenField;
    @FXML private TextField bemerkungField;
    @FXML
    private HBox inputBox;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @FXML private TableView<Unwetter> unwetterTable;
    @FXML private TableColumn<Unwetter, Integer> idColumn;
    @FXML private TableColumn<Unwetter, String> artColumn;
    @FXML private TableColumn<Unwetter, String> datumColumn;
    @FXML private TableColumn<Unwetter, String> ortColumn;
    @FXML private TableColumn<Unwetter, String> schadenColumn;
    @FXML private TableColumn<Unwetter, String> bemerkungColumn;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private ComboBox<String> sortComboBox;

    private List<Unwetter> alleUnwetter;  // Оригинальный список для фильтрации


    @FXML
    public void initialize() {
        Benutzer benutzer = Session.getAktuellerBenutzer();
        System.out.println("Aktueller Benutzer: " + (benutzer != null ? benutzer.getBenutzername() + " (" + benutzer.getRolle() + ")" : "null"));

        boolean darfBearbeiten = benutzer != null && (
                benutzer.getRolle().equalsIgnoreCase("Admin") ||
                        benutzer.getRolle().equalsIgnoreCase("ReadWriter")
        );

        if (!darfBearbeiten) {
            inputBox.setVisible(false);
            inputBox.setManaged(false);
        }

        // Показать / спрятать кнопки Edit/Delete
        editButton.setVisible(darfBearbeiten);
        editButton.setManaged(darfBearbeiten);

        deleteButton.setVisible(darfBearbeiten);
        deleteButton.setManaged(darfBearbeiten);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        artColumn.setCellValueFactory(new PropertyValueFactory<>("unwetterart"));
        datumColumn.setCellValueFactory(new PropertyValueFactory<>("datum"));
        ortColumn.setCellValueFactory(new PropertyValueFactory<>("ort"));
        schadenColumn.setCellValueFactory(new PropertyValueFactory<>("schadenhoehe"));
        bemerkungColumn.setCellValueFactory(new PropertyValueFactory<>("bemerkung"));

        loadUnwetter();
        handleReload();

        // Слушатели на изменения поиска/фильтра/сортировки
        searchField.textProperty().addListener((obs, oldVal, newVal) -> updateFilteredList());
        filterComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateFilteredList());
        sortComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateFilteredList());
    }


    private void loadUnwetter() {
        alleUnwetter = new UnwetterDAO().getAllUnwetter();
        updateFilteredList();
    }

    private void updateFilteredList() {
        String searchText = searchField.getText() != null ? searchField.getText().toLowerCase() : "";
        String filterArt = filterComboBox.getValue() != null ? filterComboBox.getValue() : "Alle";
        String sortOrder = sortComboBox.getValue();

        List<Unwetter> gefiltert = alleUnwetter.stream()
                // Поиск по всем полям (art, ort, datum, bemerkung)
                .filter(u -> u.getUnwetterart().toLowerCase().contains(searchText) ||
                        u.getOrt().toLowerCase().contains(searchText) ||
                        u.getDatum().toString().toLowerCase().contains(searchText) ||
                        (u.getBemerkung() != null && u.getBemerkung().toLowerCase().contains(searchText)))
                // Фильтрация по Art
                .filter(u -> filterArt.equals("Alle") || u.getUnwetterart().equalsIgnoreCase(filterArt))
                .collect(Collectors.toList());

        // Сортировка
        if (sortOrder != null) {
            switch (sortOrder) {
                case "Datum (Neueste)":
                    gefiltert.sort(Comparator.comparing(Unwetter::getDatum).reversed());
                    break;
                case "Datum (Älteste)":
                    gefiltert.sort(Comparator.comparing(Unwetter::getDatum));
                    break;
                case "Schadenhöhe (↑)":
                    gefiltert.sort(Comparator.comparing(u -> u.getSchadenhoehe().doubleValue()));
                    break;
                case "Schadenhöhe (↓)":
                    gefiltert.sort(Comparator.comparing((Unwetter u) -> u.getSchadenhoehe().doubleValue()).reversed());
                    break;
            }
        }

        unwetterTable.getItems().setAll(gefiltert);
    }


    @FXML
    private void handleAddUnwetter() {
        try {
            String art = artField.getText();
            LocalDate datum = datumPicker.getValue();
            String ort = ortField.getText();
            BigDecimal schaden = new BigDecimal(schadenField.getText());
            String bemerkung = bemerkungField.getText();

            Unwetter neu = new Unwetter(art, datum, ort, schaden, bemerkung);
            UnwetterDAO dao = new UnwetterDAO();
            dao.insertUnwetter(neu);

            clearFields();
            loadUnwetter();
        } catch (Exception e) {
            e.printStackTrace();
            // Можно добавить Alert
        }
    }

    private void clearFields() {
        artField.clear();
        datumPicker.setValue(null);
        ortField.clear();
        schadenField.clear();
        bemerkungField.clear();
    }

    @FXML
    private void handleDeleteUnwetter() {
        Unwetter selected = unwetterTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            new UnwetterDAO().deleteUnwetter(selected.getId());
            loadUnwetter();
        } else {
            System.out.println("Keine Zeile ausgewählt");
        }
    }
    @FXML
    private void handleReload() {
        System.out.println("🔄 Reload clicked");

        Benutzer benutzer = Session.getAktuellerBenutzer();

        boolean darfBearbeiten = benutzer != null && (
                benutzer.getRolle().equalsIgnoreCase("Admin") ||
                        benutzer.getRolle().equalsIgnoreCase("ReadWriter")
        );

        // Обновляем доступность inputBox
        if (inputBox != null) {
            inputBox.setVisible(darfBearbeiten);
            inputBox.setManaged(darfBearbeiten);
        }

        // Обновляем доступность кнопок удаления/редактирования (если есть)
        if (deleteButton != null) {
            deleteButton.setVisible(darfBearbeiten);
            deleteButton.setManaged(darfBearbeiten);
        }

        if (editButton != null) {
            editButton.setVisible(darfBearbeiten);
            editButton.setManaged(darfBearbeiten);
        }

        // Перезагрузить таблицу данных
        loadUnwetter();
    }




    @FXML
    private void handleEditUnwetter() {
        Unwetter selected = unwetterTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            artField.setText(selected.getUnwetterart());
            datumPicker.setValue(selected.getDatum());
            ortField.setText(selected.getOrt());
            schadenField.setText(selected.getSchadenhoehe().toString());
            bemerkungField.setText(selected.getBemerkung());

            // удаляем старую запись и потом добавляем новую
            new UnwetterDAO().deleteUnwetter(selected.getId());
            loadUnwetter();
        } else {
            System.out.println("Keine Zeile ausgewählt");
        }
    }

    public void updatePermissions() {
        Benutzer benutzer = Session.getAktuellerBenutzer();
        boolean darfBearbeiten = benutzer != null && (
                benutzer.getRolle().equalsIgnoreCase("Admin") ||
                        benutzer.getRolle().equalsIgnoreCase("ReadWriter")
        );

        inputBox.setVisible(darfBearbeiten);
        inputBox.setManaged(darfBearbeiten);

        editButton.setVisible(darfBearbeiten);
        editButton.setManaged(darfBearbeiten);

        deleteButton.setVisible(darfBearbeiten);
        deleteButton.setManaged(darfBearbeiten);
    }

}