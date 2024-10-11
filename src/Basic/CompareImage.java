package Basic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CompareImage {

    public static boolean compareImage(BufferedImage mainImage, String path) {
        try {
            BufferedImage imageToCompare = ImageIO.read(new File(path));

            // Check if dimensions are the same
            if (mainImage.getWidth() != imageToCompare.getWidth() ||
                    mainImage.getHeight() != imageToCompare.getHeight()) {
                return false;
            }

            // Compare pixels
            for (int x = 0; x < mainImage.getWidth(); x++) {
                for (int y = 0; y < mainImage.getHeight(); y++) {
                    if (mainImage.getRGB(x, y) != imageToCompare.getRGB(x, y)) {
                        return false;
                    }
                }
            }

            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
