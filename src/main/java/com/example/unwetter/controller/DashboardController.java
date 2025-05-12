package com.example.unwetter.controller;

import com.example.unwetter.model.Benutzer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class DashboardController {

    @FXML
    private StackPane contentArea;
    @FXML
    private Label usernameLabel;

    @FXML
    public void initialize() {
        showHomeView();
        updateBenutzerLabel();
    }

    public void showUnwetterView() {
        loadView("/com/example/unwetter/view/unwetter.fxml");
    }

    public void showHomeView() {
        loadView("/com/example/unwetter/view/home.fxml");
    }

    public void showSettingsView() {
        loadView("/com/example/unwetter/view/settings.fxml");
    }


    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleLoginClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/unwetter/view/loginDialog.fxml"));
            Parent root = loader.load();

            Stage dashboardStage = (Stage) contentArea.getScene().getWindow();
            dashboardStage.hide();

            Stage dialogStage = new Stage();
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            dashboardStage.show();

            // 🔄 Обновить метку пользователя
            updateBenutzerLabel();

            // 🔄 Обновить права в Unwetter View, если открыт
            refreshUnwetterPermissions();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateBenutzerLabel() {
        Benutzer benutzer = Session.getAktuellerBenutzer();
        if (usernameLabel != null && benutzer != null) {
            usernameLabel.setText("👤 " + benutzer.getBenutzername() + " (" + benutzer.getRolle() + ")");
        }
    }

    private void refreshUnwetterPermissions() {
        // Пытаемся получить контроллер Unwetter (если он открыт)
        try {
            for (Node node : contentArea.getChildren()) {
                Object controller = node.getProperties().get("controller");
                if (controller instanceof UnwetterController unwetterController) {
                    unwetterController.updatePermissions();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("UnwetterController not found (maybe not open)");
        }
    }
}
