package org.example.imageprocessingapp;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ThresholdTask implements Runnable {
    private final PixelReader reader;
    private final WritableImage outputImage;
    private final int width;
    private final int startY;
    private final int endY;
    private final double threshold;

    public ThresholdTask(PixelReader reader, WritableImage outputImage,
                         int width, int startY, int endY, double threshold) {
        this.reader = reader;
        this.outputImage = outputImage;
        this.width = width;
        this.startY = startY;
        this.endY = endY;
        this.threshold = threshold;
    }

    @Override
    public void run() {
        PixelWriter writer = outputImage.getPixelWriter();

        for (int y = startY; y < endY; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                double brightness = 0.2126 * color.getRed()
                        + 0.7152 * color.getGreen()
                        + 0.0722 * color.getBlue();

                Color result = (brightness >= threshold) ? Color.WHITE : Color.BLACK;

                // PixelWriter nie jest thread-safe, więc ostrożnie
                synchronized (writer) {
                    writer.setColor(x, y, result);
                }
            }
        }
    }
}
