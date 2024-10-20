package Basic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class CompareImage {

    private static final Logger LOGGER = Logger.getLogger(CompareImage.class.getName());
    private static final double SIMILARITY_THRESHOLD = 0.70; // 95% similarity required
    private static final int COLOR_TOLERANCE = 30; // Using the same tolerance as in your PixelColorChecker

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

    public Coordinates findImageInMainImage(BufferedImage subImage, int width, int height) throws IOException {
        Screenshot.screenshotGameState();
        Coordinates coordinates = new Coordinates(-1, -1);  // -1, -1 indicates no match found
        BufferedImage mainImage = ImageIO.read(new File("Resources/GameState.png"));

        // Iterate from the bottom left (0, 960) upwards to (0, 540)
        for (int y = 960; y >= 540; y--) {
            for (int x = 0; x <= mainImage.getWidth() - width; x++) {
                if (isMatchingRegion(mainImage, subImage, x, y, width, height)) {
                    return new Coordinates(x, y);
                }
            }
        }
        return coordinates;
    }

    // Helper function to check if a region in the main image matches the subImage
    private boolean isMatchingRegion(BufferedImage mainImage, BufferedImage subImage, int startX, int startY, int width, int height) {
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

    public Coordinates findRefreshButtonsInMainImage() throws IOException {
        Path refreshButtonsDir = Paths.get("Resources/RefreshButtons");

        try (Stream<Path> paths = Files.walk(refreshButtonsDir)) {
            return paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".png"))
                    .map(path -> {
                        try {
                            BufferedImage subImage = ImageIO.read(path.toFile());
                            return findImageInMainImage(subImage, subImage.getWidth(), subImage.getHeight());
                        } catch (IOException e) {
                            e.printStackTrace();
                            return new Coordinates(-1, -1);
                        }
                    })
                    .filter(coordinates -> coordinates.getX() != -1 && coordinates.getY() != -1) // Only keep valid matches
                    .findFirst()
                    .orElse(new Coordinates(-1, -1)); // Return -1, -1 if no match is found
        }
    }
}
