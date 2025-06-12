package org.example.imageprocessingapp;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class EdgeDetectionTask implements Runnable {
    private final PixelReader reader;
    private final WritableImage outputImage;
    private final int width;
    private final int startY;
    private final int endY;

    public EdgeDetectionTask(PixelReader reader, WritableImage outputImage,
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

        for (int y = startY + 1; y < endY -1 ; y++) {
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
    }
}
