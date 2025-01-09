package Basic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.IOException;

import static Logger.Logger.log;

public class PixelColorChecker {

    /**
     * return Color from image in resourcePath and coordinates
     * @param resourcePath
     * @param coordinates
     * @return
     * @throws IOException
     */


    public static Color getPixelColor(String resourcePath, Coordinates coordinates) throws IOException {
        // Load the image from the resources using class loader
        File file = new File(resourcePath);
        FileInputStream fis = new FileInputStream(file);
        BufferedImage image = ImageIO.read(fis);

        // Check if the image is loaded correctly
        if (image == null) {
            log("Failed to load the image. Please check the resource path: " + resourcePath + ".");
            return null;
        }

        // Ensure the coordinates are within the bounds of the image
        if (coordinates.getX() < 0 || coordinates.getY() < 0 || coordinates.getX() >= image.getWidth() || coordinates.getY() >= image.getHeight()) {
            log("Coordinates are out of bounds.");
            return null;
        }

        // Get the pixel color from the specified coordinates
        int pixel = image.getRGB(coordinates.getX(), coordinates.getY());
        return new Color(pixel);
    }

    /**
     * return true if pixelColor matches expectedColor within tolerance
     * @param pixelColor
     * @param expectedColor
     * @param tolerance
     * @return
     */
    public static boolean isMatchingColor(Color pixelColor, Color expectedColor, int tolerance) {
        int redDiff = Math.abs(pixelColor.getRed() - expectedColor.getRed());
        int greenDiff = Math.abs(pixelColor.getGreen() - expectedColor.getGreen());
        int blueDiff = Math.abs(pixelColor.getBlue() - expectedColor.getBlue());

        return redDiff <= tolerance && greenDiff <= tolerance && blueDiff <= tolerance;
    }


    /**
     * Check if color matches but with more parameters
     * @param coordinates
     * @param expectedColor
     * @param screenshotPath
     * @param tolerance
     * @return
     */
    public static boolean checkColorMatch(Coordinates coordinates, Color expectedColor, String screenshotPath, int tolerance) {

        try {
            Color pixelColor = PixelColorChecker.getPixelColor(screenshotPath, coordinates);

            if (pixelColor != null) {
                boolean isMatch = PixelColorChecker.isMatchingColor(pixelColor, expectedColor, tolerance);
//                log("Color match: " + isMatch + "Expected: " + expectedColor + "Actual: " + pixelColor + ".");
                return isMatch;
            } else {
                log("Failed to get pixel color.");
            }

        } catch (IOException e) {
            log("[Error] cannot check color match.");
        }

        return false;
    }

}
