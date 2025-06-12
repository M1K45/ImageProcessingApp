package org.example.imageprocessingapp;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ModalThresholdController {

    @FXML
    private TextField thresholdTextField;

    @FXML
    private Button thresholdButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label communicatorLabel;

    private HelloController mainController;

//    private Image thresholdedImage;

    public ModalThresholdController(HelloController controller) {
        this.mainController = controller;
    }

    @FXML
    protected void initialize() {
        TextFormatter<String> formatterThreshold = createNumberFormatter();
        thresholdTextField.setTextFormatter(formatterThreshold);


        thresholdButton.setOnAction(event -> {thresholdButtonOnClick();});
        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });


    }

    private void thresholdButtonOnClick() {
        try {
            if (mainController.getProcessedImage() == null) {
                mainController.setProcessedImage(mainController.getOriginalImage());
            }
            Image inputImage = mainController.getProcessedImage();
            int width = (int) inputImage.getWidth();
            int height = (int) inputImage.getHeight();
            int threshold = Integer.parseInt(thresholdTextField.getText());
            double thresholdNormalized = threshold / 255.0;
            int numThreads = 4;

            WritableImage outputImage = new WritableImage(width, height);

            PixelReader reader = inputImage.getPixelReader();

            Thread[] threads = new Thread[numThreads];
            int chunkHeight = height / numThreads;

            for (int i = 0; i < numThreads; i++) {
                int startY = i * chunkHeight;
                int endY = (i == numThreads - 1) ? height : startY + chunkHeight;

                Runnable task = new ThresholdTask(reader, outputImage, width, startY, endY, thresholdNormalized);
                threads[i] = new Thread(task);
                threads[i].start();
            }

            try {
                for (Thread t : threads) {
                    t.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



//            PixelWriter writer = outputImage.getPixelWriter();
//
//            for (int y = 0; y < height; y++) {
//                for (int x = 0; x < width; x++) {
//                    Color color = reader.getColor(x, y);
//
//                    // Convert color to brightness using luminance formula
//                    double brightness = 0.2126 * color.getRed() +
//                            0.7152 * color.getGreen() +
//                            0.0722 * color.getBlue();
//
//                    Color binaryColor = (brightness >= thresholdNormalized)
//                            ? Color.WHITE
//                            : Color.BLACK;
//
//                    writer.setColor(x, y, binaryColor);
//                }
//            }

            mainController.setProcessedImage(outputImage);
            mainController.setImage(outputImage);

            communicatorLabel.setText("Progowanie zostało przeprowadzone pomyślnie");
            communicatorLabel.setVisible(true);
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> communicatorLabel.setVisible(false));
            pause.play();


        }
        catch (Exception e) {
            e.printStackTrace();

            communicatorLabel.setText("Nie udało się wykonać progowania");
            communicatorLabel.setVisible(true);
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> communicatorLabel.setVisible(false));
            pause.play();

        }

    }

    private TextFormatter<String> createNumberFormatter() {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) return change;

            if (!newText.matches("\\d*")) return null;

            try {
                int value = Integer.parseInt(newText);
                return (value >= 0 && value <= 255) ? change : null;
            } catch (NumberFormatException e) {
                return null;
            }
        });
    }




}
