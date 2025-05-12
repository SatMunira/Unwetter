module com.example.unwetter {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires atlantafx.base;

    exports com.example.unwetter;
    opens com.example.unwetter.controller to javafx.fxml;
    opens com.example.unwetter.model to javafx.base;
}
