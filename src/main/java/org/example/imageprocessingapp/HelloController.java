package org.example.imageprocessingapp;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.PauseTransition;
import javafx.stage.Modality;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.File;

public class HelloController {
    @FXML
    private Label errorLabel;
    @FXML
    private ComboBox dropdown;
    @FXML
    private ImageView imageOriginal;
    @FXML
    private Label imageInfoLabel;
    @FXML
    private Button executeButton;
    @FXML
    private Button saveButton;

    private  boolean isModified;


    @FXML
    protected void initialize() {

        executeButton.setDisable(true);
        saveButton.setDisable(true);
        errorLabel.setVisible(false);
        errorLabel.setText("Nie wybrano operacji do wykonania");
        isModified = false;
        dropdown.getItems().add("Choice 1");
        dropdown.getItems().add("Choice 2");
        dropdown.getItems().add("Choice 3");
    }


    @FXML
    protected void onHelloButtonClick() {
        String value = (String) dropdown.getValue();
        if (value != null) {
            errorLabel.setVisible(false);

        } else {
            errorLabel.setVisible(true);
        }
    }

    @FXML
    protected void readImageButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz zdjęcie");

        // Filtry plików
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Obrazy JPG (*.jpg)", "*.jpg")
        );

        Stage stage = (Stage) imageOriginal.getScene().getWindow();

        File file = fileChooser.showOpenDialog(stage);
        try {

            if (file != null) {
                String fileName = file.getName().toLowerCase();

                if (fileName.endsWith(".jpg")) {
                    Image image = new Image(file.toURI().toString());
                    imageOriginal.setImage(image);

                    imageOriginal.setPreserveRatio(true);
                    imageOriginal.setFitWidth(600);
                    executeButton.setDisable(false);
                    saveButton.setDisable(false);

                    imageInfoLabel.setText("Pomyślnie załadowano plik");
                    imageInfoLabel.setVisible(true);
                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(event -> imageInfoLabel.setVisible(false));
                    pause.play();

                } else {
                    imageInfoLabel.setText("Niedozwolony format pliku");
                    imageInfoLabel.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(event -> imageInfoLabel.setVisible(false));
                    pause.play();
                }
            }


        }catch(Exception e){
            imageInfoLabel.setText("Nie udało się załadować pliku");
            imageInfoLabel.setVisible(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> imageInfoLabel.setVisible(false));
            pause.play();

        }
    }

    // trigger odpalania modala z chata, do poprawy ew.
    @FXML
    protected void saveImageButtonClick() {
        try {
            System.out.println("debug");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/imageprocessingapp/modal-window.fxml"));
            Parent modalRoot = fxmlLoader.load();

            ModalWindowController modalController = fxmlLoader.getController();
            modalController.setIsModified(isModified);
            modalController.setImage(imageOriginal.getImage());

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL); // albo WINDOW_MODAL
            modalStage.setTitle("Okno modalne");
            modalStage.setScene(new Scene(modalRoot));
            modalStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}