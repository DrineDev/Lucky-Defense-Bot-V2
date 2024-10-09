package Basic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.IOException;


public class PixelColorChecker {

    public static Color getPixelColor(String resourcePath, Coordinates coordinates) throws IOException {
        // Load the image from the resources using class loader
        File file = new File(resourcePath);
        FileInputStream fis = new FileInputStream(file);
        BufferedImage image = ImageIO.read(fis);

        // Check if the image is loaded correctly
        if (image == null) {
            System.out.println("Failed to load the image. Please check the resource path: " + resourcePath);
            return null;
        }

        // Ensure the coordinates are within the bounds of the image
        if (coordinates.getX() < 0 || coordinates.getY() < 0 || coordinates.getX() >= image.getWidth() || coordinates.getY() >= image.getHeight()) {
            System.out.println("Coordinates are out of bounds.");
            return null;
        }

        // Get the pixel color from the specified coordinates
        int pixel = image.getRGB(coordinates.getX(), coordinates.getY());
        return new Color(pixel);
    }

    public static boolean isMatchingColor(Color pixelColor, Color expectedColor, int tolerance) {
        int redDiff = Math.abs(pixelColor.getRed() - expectedColor.getRed());
        int greenDiff = Math.abs(pixelColor.getGreen() - expectedColor.getGreen());
        int blueDiff = Math.abs(pixelColor.getBlue() - expectedColor.getBlue());

        return redDiff <= tolerance && greenDiff <= tolerance && blueDiff <= tolerance;
    }
}
