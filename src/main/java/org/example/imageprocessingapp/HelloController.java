package org.example.imageprocessingapp;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class HelloController {
    @FXML
    private Label errorLabel;
    @FXML
    private ComboBox dropdown;


    @FXML
    protected void initialize() {
        errorLabel.setVisible(false);
        errorLabel.setText("Nie wybrano operacji do wykonania");
        errorLabel.setVisible(false);

        dropdown.getItems().add("Choice 1");
        dropdown.getItems().add("Choice 2");
        dropdown.getItems().add("Choice 3");
    }


    @FXML
    protected void onHelloButtonClick() {

        String value = (String) dropdown.getValue();
        if (value != null) {
            errorLabel.setVisible(false);

        }
        else {
            errorLabel.setVisible(true);
        }
    }
}