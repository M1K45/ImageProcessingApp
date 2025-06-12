package org.example.imageprocessingapp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ModalWindowController {

    @FXML
    private Label charactersMinimumLabel;

    @FXML
    private Label successLabel;


    @FXML
    private ImageView debugImg;

    private Image savedImage;


    @FXML
    private Button closeButton;

    @FXML
    private Button saveButton;

    @FXML
    private Label modalCommunicator;

    private boolean isModified;

    @FXML
    private TextField modalTextField;

    private  HelloController mainController;

    public void setMainController(HelloController controller) {
        this.mainController = controller;
    }

    private Stage thisStage;

    private int maxLength, minLength;

    public void setIsModified(boolean isModified) {
        this.isModified = isModified;
    }

    public void setImage(Image image) {
        this.savedImage = image;
    }

    public ModalWindowController(HelloController controller) {
        this.mainController = controller;



        // Load the FXML file
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().
//                    getResource("/org/example/imageprocessingapp/modal-save.fxml"));
//
//            // Set this class as the controller
//            loader.setController(this);
//
//            // Load the scene
//            thisStage.setScene(new Scene(loader.load()));
//
//            // Setup the window/stage
//            thisStage.setTitle("Passing Controllers Example - Layout2");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void showStage() {
        thisStage.showAndWait();
    }


    @FXML
    private void initialize() {
        successLabel.setVisible(false);
        charactersMinimumLabel.setVisible(false);
        maxLength = 100;
        minLength = 3;
        modalCommunicator.setVisible(false);
        closeButton.setOnAction(event -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });

        saveButton.setOnAction(event -> {saveButtonOnClick();});


        this.savedImage = mainController.getProcessedImage();

        if (savedImage == null) {
            this.savedImage = mainController.getOriginalImage();
            modalCommunicator.setText("Na pliku nie zostały wykonane żadne operacje");
            modalCommunicator.setTextFill(Color.ORANGE);
            modalCommunicator.setVisible(true);
        }



        modalTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > maxLength) {
                modalTextField.setText(newValue.substring(0, maxLength));
            }
        });


    }

    @FXML
    protected void saveButtonOnClick() {
        debugImg.setImage(savedImage);

        charactersMinimumLabel.setVisible(false);

        if (modalTextField.getText().length() < minLength) {
            charactersMinimumLabel.setVisible(true);
            return;
        }
        // TODO: sparametryzować ścieżkę (patrz: HelloController.java)

        // Scieżka do obrazów na komputerze stacjonarnym
        File file  = new File("C:\\Users\\Michal Kaszowski\\Pictures\\"+modalTextField.getText()+".png");

        // Ścieżka do obrazów na laptopie
//        File file  = new File("C:\\Users\\Asus\\Pictures\\"+modalTextField.getText()+".png");
        if (file.exists()) {

            successLabel.setText("Plik " + modalTextField.getText() + ".jpg już istnieje w systemie. Podaj inną nazwę pliku!");
            successLabel.setVisible(true);
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> successLabel.setVisible(false));
            pause.play();

            return;
        }

        try{
            System.out.println("DEBUG | rozpoczynanie zapisu");


            BufferedImage image = SwingFXUtils.fromFXImage(savedImage, null);
            ImageIO.write(image, "png", file);

            System.out.println("DEBUG | Czy plik istnieje " + file.exists());

            successLabel.setText("Zapisano obraz w pliku " + modalTextField.getText() + ".jpg");
            successLabel.setVisible(true);
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> successLabel.setVisible(false));
            pause.play();

            mainController.logWrite(HelloController.LogLevel.SUCCESS, "Zapisano obraz");

        }catch (Exception e){
            successLabel.setText("Nie udało się zapisać pliku " + modalTextField.getText()+ ".jpg");
            successLabel.setVisible(true);
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> successLabel.setVisible(false));
            pause.play();

            mainController.logWrite(HelloController.LogLevel.ERROR, "Nie udało się zapisać obraz");
        }

    }
}