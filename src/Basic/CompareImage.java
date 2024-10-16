package Basic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class CompareImage {

    private static final Logger LOGGER = Logger.getLogger(CompareImage.class.getName());
    private static final double SIMILARITY_THRESHOLD = 0.95; // 95% similarity required
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
}
