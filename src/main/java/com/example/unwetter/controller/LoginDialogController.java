package com.example.unwetter.controller;

import com.example.unwetter.dao.BenutzerDAO;
import com.example.unwetter.model.Benutzer;
import com.example.unwetter.controller.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class LoginDialogController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private StackPane rootPane;

    @FXML
    public void initialize() {
        Rectangle clip = new Rectangle();
        clip.setArcWidth(40);
        clip.setArcHeight(40);

        // Привязка размеров после загрузки
        clip.widthProperty().bind(rootPane.widthProperty());
        clip.heightProperty().bind(rootPane.heightProperty());

        rootPane.setClip(clip);
    }



    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        BenutzerDAO dao = new BenutzerDAO();
        Benutzer benutzer = dao.login(username, password);

        if (benutzer != null) {
            Session.setAktuellerBenutzer(benutzer);
            closeWindow();
        } else {
            errorLabel.setText("❌ Ungültige Zugangsdaten.");
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();  // Закрываем логин
    }


    private void closeWindow() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
