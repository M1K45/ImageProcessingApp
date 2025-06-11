package org.example.imageprocessingapp;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.control.Button;

import javax.imageio.ImageIO;
import java.io.File;

public class ModalScaleController {

    @FXML
    private Button cancelButton;

    @FXML
    private TextField heigthTextField;

    @FXML
    private TextField widthTextField;

    @FXML
    private Label widthRequiredLabel;

    @FXML
    private Label heightRequiredLabel;

    @FXML
    private Image image;

    private HelloController mainController;

    public void setMainController(HelloController controller) {
        this.mainController = controller;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @FXML
    private void initialize() {
        widthRequiredLabel.setVisible(false);
        heightRequiredLabel.setVisible(false);
        TextFormatter<String> formatterWidth = createNumberFormatter();
        TextFormatter<String> formatterHeigth = createNumberFormatter();
// Przypisz formatter do pola tekstowego

        widthTextField.setTextFormatter(formatterWidth);
        heigthTextField.setTextFormatter(formatterHeigth);


        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
    }

    @FXML
    protected void changeSizeButtonOnClick() throws IOException {

        if (heigthTextField.getText().trim().equals("")){
            heightRequiredLabel.setVisible(true);
        }
        else{
            heightRequiredLabel.setVisible(false);
        }
        if (widthTextField.getText().trim().equals("")){
            widthRequiredLabel.setVisible(true);
        } else {
            widthRequiredLabel.setVisible(false);
        }


        int targetWidth = Integer.parseInt(widthTextField.getText());
        int targetHeigth = Integer.parseInt(heigthTextField.getText());
        // Stwórz pusty bufor obrazu
        WritableImage resizedImage = new WritableImage(targetWidth, targetHeigth);

        // Użyj Canvas i GraphicsContext do przeskalowania
        Canvas canvas = new Canvas(targetWidth, targetHeigth);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.drawImage(image, 0, 0, targetWidth, targetHeigth);

        // Skopiuj dane z canvasu do WritableImage
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        canvas.snapshot(params, resizedImage);

        mainController.setImage(resizedImage);
        mainController.setIsModified(true);

        // problem z rgb ogolnie z kolorami

    }

    @FXML
    protected void cancelButtonOnClick(){}


    private TextFormatter<String> createNumberFormatter() {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) return change;

            if (!newText.matches("\\d*")) return null;

            try {
                int value = Integer.parseInt(newText);
                return (value >= 0 && value <= 3000) ? change : null;
            } catch (NumberFormatException e) {
                return null;
            }
        });
    }


}
