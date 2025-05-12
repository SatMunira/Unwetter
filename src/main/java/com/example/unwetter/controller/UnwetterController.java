package com.example.unwetter.controller;

import com.example.unwetter.dao.OrtDAO;
import com.example.unwetter.dao.UnwetterDAO;
import com.example.unwetter.dao.UnwetterartDAO;
import com.example.unwetter.model.Benutzer;
import com.example.unwetter.model.Ort;
import com.example.unwetter.model.Unwetter;
import com.example.unwetter.model.Unwetterart;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UnwetterController {

    @FXML private ComboBox<Unwetterart> unwetterartComboBox;
    @FXML private DatePicker datumPicker;
    @FXML private ComboBox<Ort> ortComboBox;
    @FXML private TextField schadenField;
    @FXML private TextField bemerkungField;
    @FXML private HBox inputBox;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @FXML private TableView<Unwetter> unwetterTable;
    @FXML private TableColumn<Unwetter, Integer> idColumn;
    @FXML private TableColumn<Unwetter, String> artColumn;
    @FXML private TableColumn<Unwetter, LocalDate> datumColumn;
    @FXML private TableColumn<Unwetter, String> ortColumn;
    @FXML private TableColumn<Unwetter, BigDecimal> schadenColumn;
    @FXML private TableColumn<Unwetter, String> bemerkungColumn;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private ComboBox<String> sortComboBox;

    private List<Unwetter> alleUnwetter;

    @FXML
    public void initialize() {
        Benutzer benutzer = Session.getAktuellerBenutzer();
        boolean darfBearbeiten = benutzer != null && (
                benutzer.getRolle().equalsIgnoreCase("Admin") ||
                        benutzer.getRolle().equalsIgnoreCase("ReadWriter")
        );

        inputBox.setVisible(darfBearbeiten);
        inputBox.setManaged(darfBearbeiten);

        editButton.setVisible(darfBearbeiten);
        deleteButton.setVisible(darfBearbeiten);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        artColumn.setCellValueFactory(new PropertyValueFactory<>("unwetterart"));
        datumColumn.setCellValueFactory(new PropertyValueFactory<>("datum"));
        ortColumn.setCellValueFactory(new PropertyValueFactory<>("ort"));
        schadenColumn.setCellValueFactory(new PropertyValueFactory<>("schadenhoehe"));
        bemerkungColumn.setCellValueFactory(new PropertyValueFactory<>("bemerkung"));

        loadComboBoxData();
        loadUnwetter();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> updateFilteredList());
        filterComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateFilteredList());
        sortComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateFilteredList());
    }

    private void loadComboBoxData() {
        unwetterartComboBox.getItems().setAll(new UnwetterartDAO().getAllUnwetterarten());
        ortComboBox.getItems().setAll(new OrtDAO().getAllOrte());
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
                .filter(u -> u.getUnwetterart().toLowerCase().contains(searchText) ||
                        u.getOrt().toLowerCase().contains(searchText) ||
                        u.getDatum().toString().toLowerCase().contains(searchText) ||
                        (u.getBemerkung() != null && u.getBemerkung().toLowerCase().contains(searchText)))
                .filter(u -> filterArt.equals("Alle") || u.getUnwetterart().equalsIgnoreCase(filterArt))
                .collect(Collectors.toList());

        if (sortOrder != null) {
            switch (sortOrder) {
                case "Datum (Neueste)" -> gefiltert.sort(Comparator.comparing(Unwetter::getDatum).reversed());
                case "Datum (Älteste)" -> gefiltert.sort(Comparator.comparing(Unwetter::getDatum));
                case "Schadenhöhe (↑)" -> gefiltert.sort(Comparator.comparing(u -> u.getSchadenhoehe().doubleValue()));
                case "Schadenhöhe (↓)" -> gefiltert.sort(Comparator.comparing((Unwetter u) -> u.getSchadenhoehe().doubleValue()).reversed());
            }
        }

        unwetterTable.getItems().setAll(gefiltert);
    }

    @FXML
    private void handleAddOrUpdateUnwetter() {
        try {
            Unwetterart selectedArt = unwetterartComboBox.getValue();
            Ort selectedOrt = ortComboBox.getValue();
            LocalDate datum = datumPicker.getValue();
            String schadenText = schadenField.getText() != null ? schadenField.getText().trim() : "";
            String bemerkung = bemerkungField.getText();

            if (selectedArt == null || selectedOrt == null || datum == null || schadenText.isEmpty()) {
                System.out.println("Bitte alle Pflichtfelder ausfüllen.");
                return;
            }

            BigDecimal schaden;
            try {
                schaden = new BigDecimal(schadenText);
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Schadenhöhe: " + schadenText);
                return;
            }

            Unwetter selected = unwetterTable.getSelectionModel().getSelectedItem();

            if (selected != null) {
                // UPDATE
                selected.setUnwetterartId(selectedArt.getId());
                selected.setUnwetterart(selectedArt.getBezeichnung());
                selected.setOrtId(selectedOrt.getId());
                selected.setOrt(selectedOrt.getName());
                selected.setDatum(datum);
                selected.setSchadenhoehe(schaden);
                selected.setBemerkung(bemerkung);

                new UnwetterDAO().updateUnwetter(selected);
            }


            clearFields();
            loadUnwetter();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void clearFields() {
        unwetterartComboBox.getSelectionModel().clearSelection();
        ortComboBox.getSelectionModel().clearSelection();
        datumPicker.setValue(null);
        schadenField.clear();
        bemerkungField.clear();
    }



    @FXML
    private void handleDeleteUnwetter() {
        Unwetter selected = unwetterTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            new UnwetterDAO().deleteUnwetter(selected.getId());
            loadUnwetter();
        }
    }

    @FXML
    private void handleReload() {
        Benutzer benutzer = Session.getAktuellerBenutzer();
        boolean darfBearbeiten = benutzer != null && (
                benutzer.getRolle().equalsIgnoreCase("Admin") ||
                        benutzer.getRolle().equalsIgnoreCase("ReadWriter")
        );

        inputBox.setVisible(darfBearbeiten);
        editButton.setVisible(darfBearbeiten);
        deleteButton.setVisible(darfBearbeiten);

        loadUnwetter();
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

    @FXML
    private void handleEditUnwetter() {
        Unwetter selected = unwetterTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Установить выбранное в поля ввода
            Unwetterart selectedArt = unwetterartComboBox.getItems().stream()
                    .filter(art -> art.getBezeichnung().equals(selected.getUnwetterart()))
                    .findFirst().orElse(null);

            Ort selectedOrt = ortComboBox.getItems().stream()
                    .filter(ort -> ort.getName().equals(selected.getOrt()))
                    .findFirst().orElse(null);

            unwetterartComboBox.getSelectionModel().select(selectedArt);
            ortComboBox.getSelectionModel().select(selectedOrt);
            datumPicker.setValue(selected.getDatum());
            schadenField.setText(selected.getSchadenhoehe().toString());
            bemerkungField.setText(selected.getBemerkung() != null ? selected.getBemerkung() : "");
        } else {
            System.out.println("Keine Zeile ausgewählt");
        }
    }

}
