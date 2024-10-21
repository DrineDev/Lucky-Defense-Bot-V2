package Basic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.List;

public class CompareImage {

    private static final Logger LOGGER = Logger.getLogger(CompareImage.class.getName());
    private static final double SIMILARITY_THRESHOLD = 0.70; // 95% similarity required
    private static final int COLOR_TOLERANCE = 30; // Using the same tolerance as in your PixelColorChecker
    private static final List<String> REFRESH_BUTTON_FILES = Arrays.asList(
            "3xRefresh.png", "2xRefresh.png", "1xRefresh.png", "0xRefresh.png"
    );

    public static boolean compareImage(BufferedImage mainImage, String path) {
        try {
            File imageFile = new File(path);
            if (!imageFile.exists()) {
                LOGGER.warning("Image file does not exist: " + path);
                return false;
            }

            BufferedImage imageToCompare = ImageIO.read(imageFile);

            // Check if dimensions are the same
            if (mainImage.getWidth() != imageToCompare.getWidth() ||
                    mainImage.getHeight() != imageToCompare.getHeight()) {
                LOGGER.warning("Image dimensions do not match for: " + path);
                LOGGER.info("Main image: " + mainImage.getWidth() + "x" + mainImage.getHeight());
                LOGGER.info("Compare image: " + imageToCompare.getWidth() + "x" + imageToCompare.getHeight());
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
            LOGGER.info("Similarity for " + path + ": " + String.format("%.2f", similarity * 100) + "%");

            return similarity >= SIMILARITY_THRESHOLD;
        } catch (IOException e) {
            LOGGER.severe("Error comparing images: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Helper function to check if a region in the main image matches the subImage
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

    public static Coordinates findRefreshButtonInGameState(String refreshButtonPath, String gameStatePath) throws IOException {
        System.out.println("Attempting to load GameState from: " + gameStatePath);
        BufferedImage gameState = ImageIO.read(new File(gameStatePath));

        System.out.println("Attempting to load RefreshButton from: " + refreshButtonPath);
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

        LOGGER.info("Searching GameState (" + gameStateWidth + "x" + gameStateHeight + ") for RefreshButton (" + buttonWidth + "x" + buttonHeight + ")");

        // Iterate over every pixel in GameState, treating each as a possible top-left corner of the button
        for (int y = 0; y <= gameStateHeight - buttonHeight; y++) {
            for (int x = 0; x <= gameStateWidth - buttonWidth; x++) {
                if (isMatchingRegion(gameState, refreshButton, x, y)) {
                    LOGGER.info("Found matching RefreshButton at (" + x + ", " + y + ")");
                    return new Coordinates(x, y);
                }
            }
        }

        LOGGER.warning("No matching refresh button found in the game state.");
        return new Coordinates(-1, -1); // No match found
    }
}