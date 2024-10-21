package Basic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.List;

public class CompareImage {

    private static final Logger LOGGER = Logger.getLogger(CompareImage.class.getName());
    private static final double SIMILARITY_THRESHOLD = 0.70; // 95% similarity required
    private static final int COLOR_TOLERANCE = 30; // Using the same tolerance as in your PixelColorChecker
    private static final List<String> REFRESH_BUTTON_FILES = Arrays.asList(
            "Shop1_cropped.png", "Shop2_cropped.png", "Shop3_cropped.png", "Shop4_cropped.png"
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

    private static Coordinates findImageInMainImage(BufferedImage mainImage, BufferedImage subImage) {
        int mainWidth = mainImage.getWidth();
        int mainHeight = mainImage.getHeight();
        int subWidth = subImage.getWidth();
        int subHeight = subImage.getHeight();

        // Adjust search area to avoid going out of bounds
        for (int y = Math.min(960, mainHeight - subHeight); y >= Math.max(540, 0); y--) {
            for (int x = 0; x <= mainWidth - subWidth; x++) {
                if (isMatchingRegion(mainImage, subImage, x, y, subWidth, subHeight)) {
                    return new Coordinates(x, y);
                }
            }
        }
        return new Coordinates(-1, -1);
    }

    // Helper function to check if a region in the main image matches the subImage
    private static boolean isMatchingRegion(BufferedImage mainImage, BufferedImage subImage, int startX, int startY, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int mainImagePixel = mainImage.getRGB(startX + x, startY + y);
                int subImagePixel = subImage.getRGB(x, y);

                // Compare pixels, you can add tolerance if needed
                if (mainImagePixel != subImagePixel) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Coordinates findRefreshButtonInGameState(String dirPath) throws IOException {
        File gameStateFile = new File("Resources/GameState.png");
        if (!gameStateFile.exists()) {
            LOGGER.severe("GameState.png does not exist at: " + gameStateFile.getAbsolutePath());
            throw new IOException("GameState.png not found");
        }

        BufferedImage mainImage;
        try {
            mainImage = ImageIO.read(gameStateFile);
        } catch (IOException e) {
            LOGGER.severe("Failed to read GameState.png: " + e.getMessage());
            throw e;
        }

        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            LOGGER.severe("RefreshButtons directory does not exist or is not a directory: " + dir.getAbsolutePath());
            throw new IOException("Invalid RefreshButtons directory");
        }

        File[] directoryListing = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".png"));
        if (directoryListing == null || directoryListing.length == 0) {
            LOGGER.warning("No PNG files found in RefreshButtons directory: " + dir.getAbsolutePath());
            return new Coordinates(-1, -1);
        }

        for (File file : directoryListing) {
            try {
                BufferedImage subImage = ImageIO.read(file);
                Coordinates coords = findImageInMainImage(mainImage, subImage);
                if (coords.getX() != -1 && coords.getY() != -1) {
                    LOGGER.info("Found refresh button using: " + file.getName());
                    return coords;
                }
            } catch (IOException e) {
                LOGGER.warning("Failed to read refresh button image: " + file.getName() + ". Error: " + e.getMessage());
            }
        }

        LOGGER.warning("No matching refresh button found in any of the images.");
        return new Coordinates(-1, -1);
    }

}
