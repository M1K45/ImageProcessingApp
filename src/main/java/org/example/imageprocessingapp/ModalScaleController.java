package org.example.imageprocessingapp;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class ModalScaleController {

    @FXML
    private Button cancelButton;


    @FXML
    private void initialize() {
        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
    }

    @FXML
    protected void changeSizeButtonOnClick(){}

    @FXML
    protected void cancelButtonOnClick(){}
}
