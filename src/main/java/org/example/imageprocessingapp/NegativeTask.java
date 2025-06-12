package org.example.imageprocessingapp;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class NegativeTask implements Runnable {

    private final PixelReader reader;
    private final WritableImage outputImage;
    private final int width;
    private final int startY;
    private final int endY;

    public NegativeTask(PixelReader reader, WritableImage outputImage,
                         int width, int startY, int endY) {
        this.reader = reader;
        this.outputImage = outputImage;
        this.width = width;
        this.startY = startY;
        this.endY = endY;
    }

    @Override
    public void run() {
        PixelWriter writer = outputImage.getPixelWriter();

        for (int y = startY; y < endY; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                writer.setColor(x, y, color.invert());

                // PixelWriter nie jest thread-safe, więc ostrożnie
            }
        }
    }

}
