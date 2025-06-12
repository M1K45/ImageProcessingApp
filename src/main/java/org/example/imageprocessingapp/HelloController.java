package org.example.imageprocessingapp;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.animation.PauseTransition;
import javafx.stage.Modality;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javafx.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class HelloController {
    @FXML
    private Label errorLabel;

    @FXML
    private Label dropdownActionMessage;

    @FXML
    private ComboBox dropdown;
    @FXML
    private ImageView imageDisplay;
    @FXML
    private Label imageInfoLabel;
    @FXML
    private Button executeButton;
    @FXML
    private Button saveButton;

    @FXML
    private Button scaleButton;

    private Pair<Double, Double> originalSize;

    private Image originalImage;
    private Image processedImage;

    private  boolean isModified;

    private File logFile;

    public enum LogLevel {
        INFO("[INFO]"),
        IMAGE_PROCESSING("[IMAGE_PROCESSING]"),
        SUCCESS("[SUCCESS]"),
        ERROR("[ERROR]");

        private final String tag;

        LogLevel (String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }
    }


//    private final Stage thisStage;

//    public HelloController(){
//        thisStage = new Stage();
//
//    }
// funkcja pomocnicza wykorzystywana do rozwoju aplikacji, automatycznie dodająca zdjęcie
// oraz aktywująca przyciski
    public void loadInitialImage() {
        // TODO: Sprawdzenie, czy jest możliwość ustawienia pobierania zdjęcia o danej nazwie
        // z pulpitu komputera niezależnie od nazwy użytkownika

        // ścieżka na laptopie
       // File fileImage = new File ("C:\\Users\\Asus\\Desktop\\corgi.jpg");

        // Scieżka na komputerze stacjonarnym
         File fileImage = new File ("C:\\Users\\Michal Kaszowski\\Desktop\\corgi.jpg.jpg");

         System.out.println(fileImage.getAbsolutePath());
        this.originalImage = new Image(fileImage.toURI().toString());
        imageDisplay.setImage(this.originalImage);

        // odblokowanie
        executeButton.setDisable(false);
        saveButton.setDisable(false);
        scaleButton.setDisable(false);

        this.originalSize = new Pair<Double, Double>(this.originalImage.getWidth(), this.originalImage.getHeight());
//        System.out.println("szerokosc: " + originalSize.getKey() + "\twysokosc: " + originalSize.getValue());

    }


    protected void logWrite(LogLevel level, String message) {
        try {
            FileWriter fw = new FileWriter(logFile.getAbsoluteFile(), true);
            LocalDateTime now = LocalDateTime.now();
            fw.write("[" + now + "] " + level.getTag() + " " + message + "\n");
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void stop() {
        logWrite(LogLevel.INFO, "Zamknięto aplikację");
    }

    @FXML
    protected void initialize() {
        try {
            this.logFile = new File("./src/main/java/org/example/imageprocessingapp/logs.txt");
            logFile.createNewFile();

            logWrite(LogLevel.INFO,"Uruchomiono aplikację");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.processedImage = null;

        scaleButton.setDisable(true);
        executeButton.setDisable(true);
        saveButton.setDisable(true);
        errorLabel.setVisible(false);

        errorLabel.setText("Nie wybrano operacji do wykonania");
        isModified = false;
        dropdown.getItems().add("Negatyw");
        dropdown.getItems().add("Progowanie");
        dropdown.getItems().add("Konturowanie");

        executeButton.setOnAction(event -> {executeButtonOnClick();});
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

        Stage stage = (Stage) imageDisplay.getScene().getWindow();

        File file = fileChooser.showOpenDialog(stage);
        try {

            if (file != null) {
                String fileName = file.getName().toLowerCase();

                if (fileName.endsWith(".jpg")) {
                    Image image = new Image(file.toURI().toString());

                    this.originalImage = image;
                    imageDisplay.setImage(this.originalImage);

                    imageDisplay.setPreserveRatio(true);
                    imageDisplay.setFitWidth(600);
                    executeButton.setDisable(false);
                    saveButton.setDisable(false);
                    scaleButton.setDisable(false);

                    this.originalSize = new Pair<>(image.getWidth(), image.getHeight());
//                    System.out.println("szerokosc: " + originalSize.getKey() + "\twysokosc: " + originalSize.getValue());

                    imageInfoLabel.setText("Pomyślnie załadowano plik");
                    imageInfoLabel.setVisible(true);
                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(event -> imageInfoLabel.setVisible(false));
                    pause.play();

                    logWrite(LogLevel.SUCCESS, "Załadowano plik");

                } else {
                    imageInfoLabel.setText("Niedozwolony format pliku");
                    imageInfoLabel.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(event -> imageInfoLabel.setVisible(false));
                    pause.play();

                    logWrite(LogLevel.ERROR, "Niedozwolony format pliku");
                }
            }


        }catch(Exception e){
            imageInfoLabel.setText("Nie udało się załadować pliku");
            imageInfoLabel.setVisible(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> imageInfoLabel.setVisible(false));
            pause.play();
            logWrite(LogLevel.ERROR, "Nie udało się załadować pliku");

        }
    }

    // trigger odpalania modala z chata, do poprawy ew.
    @FXML
    protected void saveImageButtonClick() {
        ModalWindowController controller = new ModalWindowController(this);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/imageprocessingapp/modal-save.fxml"));

            ModalWindowController modalController = new ModalWindowController(this);

            fxmlLoader.setController(modalController);

            Parent modalRoot = fxmlLoader.load();

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL); // albo WINDOW_MODAL
            modalStage.setTitle("Zapis obrazu");
            modalStage.setScene(new Scene(modalRoot));
            modalStage.showAndWait();

            logWrite(LogLevel.INFO,"Otworzono okno do zapisu obrazu");
        } catch (Exception e) {
            e.printStackTrace();
            logWrite(LogLevel.ERROR, "Błąd przy otworzeniu okna do zapisu obrazu");
        }
    }

    @FXML
    protected void scaleButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/imageprocessingapp/modal-scale.fxml"));
            Parent modalRoot = fxmlLoader.load();

            ModalScaleController modalController = fxmlLoader.getController();
            modalController.setMainController(this);
            modalController.setImage(imageDisplay.getImage());

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL); // albo WINDOW_MODAL
            modalStage.setTitle("Skalowanie obrazu");
            modalStage.setScene(new Scene(modalRoot));
            modalStage.showAndWait();
        }
        catch(Exception e){
            e.printStackTrace();
            logWrite(LogLevel.ERROR, "Nie udało się otworzyć modala do skalowania");
        }
    }

    public void setImage(Image image) {
        this.imageDisplay.setImage(image);
    }

    public void setIsModified(boolean isModified) {
        this.isModified = isModified;
    }

    public boolean getIsModified() {
        return isModified;
    }

    @FXML
    private void rotateLeft(){
        imageDisplay.setRotate(imageDisplay.getRotate() - 90);

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        Canvas canvas = new Canvas(100, 100);

        this.processedImage = imageDisplay.snapshot(params, null);
        canvas.getGraphicsContext2D().drawImage(this.processedImage, 0, 0);

        logWrite(LogLevel.IMAGE_PROCESSING, "Obrócono obraz w lewo");


    }

    private void executeButtonOnClick() {
        if (this.processedImage == null) {
            this.processedImage = this.originalImage;
        }
        String option = (String) dropdown.getValue();
        switch (option) {
            case "Negatyw":
                try {
                    PixelReader pixelReader = processedImage.getPixelReader();
                    int width = (int) processedImage.getWidth();
                    int height = (int) processedImage.getHeight();
                    int numThreads = 4;



                    WritableImage writableImage = new WritableImage(width, height);
                    PixelWriter pixelWriter = writableImage.getPixelWriter();

                    Thread[] threads = new Thread[numThreads];
                    int chunkHeight = height / numThreads;

                    for (int i = 0; i < numThreads; i++) {
                        int startY = i * chunkHeight;
                        int endY = (i == numThreads - 1) ? height : startY + chunkHeight;

                        Runnable task = new NegativeTask(pixelReader, writableImage, width, startY, endY);
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


                    this.processedImage = writableImage;
                    imageDisplay.setImage(this.processedImage);

                    dropdownActionMessage.setText("Negatyw został wygenerowany pomyślnie");
                    dropdownActionMessage.setVisible(true);
                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(event -> dropdownActionMessage.setVisible(false));
                    pause.play();

                    logWrite(LogLevel.IMAGE_PROCESSING, "Przeprowadzono negatyw");
                }
                catch (Exception e) {
                    e.printStackTrace();

                    dropdownActionMessage.setText("Nie udało się zapisać negatywu");
                    dropdownActionMessage.setVisible(true);
                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(event -> dropdownActionMessage.setVisible(false));
                    pause.play();

                    logWrite(LogLevel.ERROR, "Nie udało się przeprowadzić negatywu");
                }
                break;

            case "Progowanie":
                try{

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/imageprocessingapp/modal-threshold.fxml"));
                    ModalThresholdController modalController = new ModalThresholdController(this);

                    fxmlLoader.setController(modalController);

                    Parent modalRoot = fxmlLoader.load();

                    Stage modalStage = new Stage();
                    modalStage.initModality(Modality.APPLICATION_MODAL); // albo WINDOW_MODAL
                    modalStage.setTitle("Progowanie");
                    modalStage.setScene(new Scene(modalRoot));
                    modalStage.showAndWait();

                    logWrite(LogLevel.IMAGE_PROCESSING, "Przeprowadzono progowanie");


                }
                catch(Exception e){
                    e.printStackTrace();
                    logWrite(LogLevel.ERROR, "Nie udało się przeprowadzić progowania");

                }
                break;

            case "Konturowanie":
                try{
                    if(this.processedImage == null){
                        this.processedImage = this.originalImage;
                    }

                    Image inputImage = this.processedImage;
                    int width = (int) inputImage.getWidth();
                    int height = (int) inputImage.getHeight();
                    int numThreads = 4;
                    WritableImage outputImage = new WritableImage(width, height);

                    PixelReader reader = inputImage.getPixelReader();

                    Thread[] threads = new Thread[numThreads];
                    int chunkHeight = height / numThreads;

                    for (int i = 0; i < numThreads; i++) {
                        int startY = i * chunkHeight;
                        int endY = (i == numThreads - 1) ? height : startY + chunkHeight;

                        Runnable task = new EdgeDetectionTask(reader, outputImage, width, startY, endY);
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




                    this.setProcessedImage(outputImage);
                    this.setImage(this.processedImage);
                    logWrite(LogLevel.IMAGE_PROCESSING, "Przeprowadzono operację konturowania");
                }
                catch(Exception e){
                    logWrite(LogLevel.ERROR, "Nie udało się przeprowadzić operacji konturowania");
                }


        }
    }

    @FXML
    private void rotateRigth(){
        imageDisplay.setRotate(imageDisplay.getRotate() + 90);

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        Canvas canvas = new Canvas(100, 100);

        this.processedImage = imageDisplay.snapshot(params, null);
        canvas.getGraphicsContext2D().drawImage(this.processedImage, 0, 0);
        logWrite(LogLevel.IMAGE_PROCESSING, "Obrócono obraz w prawo");


    }

    public Pair<Double, Double> getOriginalSize() {
        return this.originalSize;
    }

    public void setOriginalImage(Image image) {
        this.originalImage = image;
    }

    public void setProcessedImage(Image image) {
        this.processedImage = image;
    }

    public Image getProcessedImage() {
        return this.processedImage;
    }

    public Image getOriginalImage() {
        return this.originalImage;
    }

    public static WritableImage applyEdgeDetectionSobel(Image inputImage) {
        int width = (int) inputImage.getWidth();
        int height = (int) inputImage.getHeight();

        PixelReader reader = inputImage.getPixelReader();
        WritableImage outputImage = new WritableImage(width, height);
        PixelWriter writer = outputImage.getPixelWriter();

        // Maski Sobela
        int[][] sobelX = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };

        int[][] sobelY = {
                {-1, -2, -1},
                { 0,  0,  0},
                { 1,  2,  1}
        };

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {

                double gx = 0;
                double gy = 0;

                // Iteracja 3x3
                for (int j = -1; j <= 1; j++) {
                    for (int i = -1; i <= 1; i++) {
                        Color color = reader.getColor(x + i, y + j);
                        double gray = 0.2126 * color.getRed() +
                                0.7152 * color.getGreen() +
                                0.0722 * color.getBlue();

                        gx += gray * sobelX[j + 1][i + 1];
                        gy += gray * sobelY[j + 1][i + 1];
                    }
                }

                // Obliczanie siły gradientu
                double g = Math.sqrt(gx * gx + gy * gy);

                // Przycięcie wartości (normalizacja)
                g = Math.min(1.0, g);

                Color edgeColor = new Color(g, g, g, 1.0);
                writer.setColor(x, y, edgeColor);
            }
        }

        return outputImage;
    }

}