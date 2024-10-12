package Basic;

import net.sourceforge.tess4j.*;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageReader {

    // Load the OpenCV library
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static String readImage(Coordinates topLeft, Coordinates bottomRight) throws IOException {
        Screenshot.screenshotGameState(); // Capture the game state

        Tesseract tesseract = new Tesseract();
        int width = bottomRight.getX() - topLeft.getX();
        int height = bottomRight.getY() - topLeft.getY();

        try {
            tesseract.setDatapath("lib/Tess4j/tessdata");

            // Read the screenshot image
            BufferedImage image = ImageIO.read(new File("Resources/GameState.png"));

            // Get the subimage for OCR
            BufferedImage subImage = image.getSubimage(topLeft.getX(), topLeft.getY(), width, height);

            // Preprocess the image using OpenCV
            Mat preprocessedImage = preprocessImage(subImage);

            // Save the preprocessed image for debugging (optional)
            Imgcodecs.imwrite("Resources/PreprocessedGameState.png", preprocessedImage);

            // Convert the preprocessed Mat back to BufferedImage for Tesseract
            BufferedImage ocrImage = matToBufferedImage(preprocessedImage);

            // Perform OCR on the preprocessed image
            String text = tesseract.doOCR(ocrImage);

            return text;
        } catch (TesseractException e) {
            System.out.println("Tesseract OCR failed...");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Failed to read the image file...");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid coordinates for subimage...");
            e.printStackTrace();
        }

        return null;
    }

    private static Mat preprocessImage(BufferedImage image) {
        // Convert BufferedImage to Mat
        Mat src = bufferedImageToMat(image);
        Mat gray = new Mat();
        Mat binary = new Mat();

        // Convert to grayscale
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        // Apply Gaussian blur
        Imgproc.GaussianBlur(gray, gray, new Size(5, 5), 0);
        // Apply thresholding
        Imgproc.threshold(gray, binary, 128, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

        return binary; // Return the preprocessed binary image
    }

    private static Mat bufferedImageToMat(BufferedImage image) {
        // Convert BufferedImage to Mat
        int width = image.getWidth();
        int height = image.getHeight();
        Mat mat = new Mat(height, width, CvType.CV_8UC3);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                double[] data = new double[3];
                data[0] = (rgb >> 16) & 0xFF; // Red
                data[1] = (rgb >> 8) & 0xFF;  // Green
                data[2] = rgb & 0xFF;         // Blue
                mat.put(y, x, data);
            }
        }
        return mat;
    }

    private static BufferedImage matToBufferedImage(Mat mat) {
        // Convert Mat to BufferedImage
        int width = mat.cols();
        int height = mat.rows();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double[] data = mat.get(y, x);
                int rgb = ((int) data[0] << 16) | ((int) data[1] << 8) | (int) data[2];
                image.setRGB(x, y, rgb);
            }
        }
        return image;
    }
}