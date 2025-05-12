package com.example.unwetter;

import atlantafx.base.theme.PrimerLight;
import com.example.unwetter.controller.Session;
import com.example.unwetter.dao.UnwetterDAO;
import com.example.unwetter.model.Benutzer;
import com.example.unwetter.model.Unwetter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;
import atlantafx.base.theme.PrimerLight;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        Session.setAktuellerBenutzer(new Benutzer(0, "Gast", "", "Reader"));


        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/unwetter/view/dashboard.fxml")));
        primaryStage.setTitle("Unwetterdatenbank");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/unwetter/images/storm-icon.png"))));
        primaryStage.setScene(new Scene(root, 780, 550));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

