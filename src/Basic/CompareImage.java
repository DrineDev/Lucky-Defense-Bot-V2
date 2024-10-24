package Basic;

import GUI.MainFrame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class CompareImage {

    private static final Logger LOGGER = Logger.getLogger(CompareImage.class.getName());
    private static final double SIMILARITY_THRESHOLD = 0.975; // 95% similarity required
    private static final int COLOR_TOLERANCE = 30; // Using the same tolerance as in your PixelColorChecker
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    /** Takes a BufferedImage and a string path to the image to compare against
     *  Checks if the specified path exists,
     *  Reads the comparison image into a BufferedImage,
     *  Checks dimensions and must have the same dimensions else return
     *  Iterates through each pixel in the images,
     *  Calculates the similarity percentage
     *  Returns true if the similarity meets or exceeds the threshold
     *  */
    public static boolean compareImage(BufferedImage mainImage, String path) {
        try {
            File imageFile = new File(path);
            if (!imageFile.exists()) {
                return false;
            }

            BufferedImage imageToCompare = ImageIO.read(imageFile);

            // Check if dimensions are the same
            if (mainImage.getWidth() != imageToCompare.getWidth() ||
                    mainImage.getHeight() != imageToCompare.getHeight()) {
                return false;
            }

            int totalPixels = mainImage.getWidth() * mainImage.getHeight();
            int matchingPixels = 0;

            // Compare pixels
            for (int x = 0; x < mainImage.getWidth(); x++) {
                for (int y = 0; y < mainImage.getHeight(); y++) {
                    Color mainColor = new Color(mainImage.getRGB(x, y));
                    Color compareColor = new Color(imageToCompare.getRGB(x, y));

                    if (PixelColorChecker.isMatchingColor(mainColor, compareColor, COLOR_TOLERANCE)) {
                        matchingPixels++;
                    }
                }
            }

            double similarity = (double) matchingPixels / totalPixels;

            return similarity >= SIMILARITY_THRESHOLD;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Takes the mainImage, a sub-image(image to match) and the starting coordinates(topLeft) for comparison
     *  Checks that the sub-image fits within the bounds of the main image
     *  Compares each pixel in the sub-image with the corresponding pixel in the main image using PixelColorChecker
     * */
    private static boolean isMatchingRegion(BufferedImage mainImage, BufferedImage subImage, int startX, int startY) {
        int subImageWidth = subImage.getWidth();
        int subImageHeight = subImage.getHeight();
        int mainImageWidth = mainImage.getWidth();
        int mainImageHeight = mainImage.getHeight();

        // Check if the subImage fits within the bounds of the mainImage at the specified position
        if (startX + subImageWidth > mainImageWidth || startY + subImageHeight > mainImageHeight) {
            return false; // The region exceeds the bounds of the main image
        }

        // Compare each pixel in the subImage with the corresponding pixel in the mainImage
        for (int y = 0; y < subImageHeight; y++) {
            for (int x = 0; x < subImageWidth; x++) {
                Color mainColor = new Color(mainImage.getRGB(startX + x, startY + y));
                Color subColor = new Color(subImage.getRGB(x, y));

                if (!PixelColorChecker.isMatchingColor(mainColor, subColor, COLOR_TOLERANCE)) {
                    return false; // Region doesn't match, stop checking further
                }
            }
        }

        return true; // The region matches
    }

    /**  Takes the paths to the refreshButton images and GameState image
     *   Loads both images and checks for null values
     *   Logs the dimensions of both images
     *   Iterates over each pixel in the game state and considers it as a possible top-left corner for the refresh button
     *   Calls isMatchingRegion to check if the sub-image matches at that position
     *   If a match is found, return coordinates, else return (-1, -1) */
    public static Coordinates findRefreshButtonInGameState(String refreshButtonPath, String gameStatePath) throws IOException {
        String currentTime = LocalDateTime.now().format(dtf);
        System.out.println("[" + currentTime + "]" + " Attempting to load GameState from: " + gameStatePath);
        BufferedImage gameState = ImageIO.read(new File(gameStatePath));

        System.out.println("[" + currentTime + "]" + " Attempting to load RefreshButton from: " + refreshButtonPath);
        BufferedImage refreshButton = ImageIO.read(new File(refreshButtonPath));

        if (gameState == null) {
            throw new IOException("GameState image is null!");
        }

        if (refreshButton == null) {
            throw new IOException("RefreshButton image is null!");
        }

        // Get dimensions
        int buttonWidth = refreshButton.getWidth();
        int buttonHeight = refreshButton.getHeight();
        int gameStateWidth = gameState.getWidth();
        int gameStateHeight = gameState.getHeight();


        // Iterate over every pixel in GameState, treating each as a possible top-left corner of the button
        for (int y = 0; y <= gameStateHeight - buttonHeight; y++) {
            for (int x = 0; x <= gameStateWidth - buttonWidth; x++) {
                if (isMatchingRegion(gameState, refreshButton, x, y)) {
                    return new Coordinates(x, y);
                }
            }
        }

        return new Coordinates(-1, -1); // No match found
    }

}
