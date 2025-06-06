package org.example.imageprocessingapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ModalWindowController {

    @FXML
    private Button closeButton;

    @FXML
    private void initialize() {
        closeButton.setOnAction(event -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
    }

    @FXML
    protected void saveButtonClick() {}
}