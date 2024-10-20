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

    public static Coordinates findImageInMainImage(BufferedImage mainImage, BufferedImage subImage) {
        int subWidth = subImage.getWidth();
        int subHeight = subImage.getHeight();
        int mainWidth = mainImage.getWidth();
        int mainHeight = mainImage.getHeight();

        // Iterate from the bottom right to the top left
        for (int y = mainHeight - subHeight; y >= 0; y--) {
            for (int x = mainWidth - subWidth; x >= 0; x--) {
                if (isMatchingRegion(mainImage, subImage, x, y, subWidth, subHeight)) {
                    System.out.println("Match found at: " + x + ", " + y);
                    return new Coordinates(x, y);
                }
            }
        }
        System.out.println("No match found for this subimage");
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

    public static Coordinates findRefreshButtonsInGameState(String dirPath) throws IOException {
        BufferedImage mainImage = ImageIO.read(new File("Resources/GameState.png"));
        System.out.println("Main image size: " + mainImage.getWidth() + "x" + mainImage.getHeight());

        for (String fileName : REFRESH_BUTTON_FILES) {
            File file = new File(dirPath, fileName);
            if (!file.exists()) {
                System.out.println("File not found: " + file.getAbsolutePath());
                continue;
            }

            BufferedImage subImage = ImageIO.read(file);
            System.out.println("Checking refresh button: " + fileName + " (size: " + subImage.getWidth() + "x" + subImage.getHeight() + ")");

            Coordinates coords = findImageInMainImage(mainImage, subImage);
            if (coords.getX() != -1 && coords.getY() != -1) {
                return coords;
            }
        }

        System.out.println("No refresh button found in any of the specified images.");
        return new Coordinates(-1, -1);
    }
}
