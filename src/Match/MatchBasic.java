package Match;

import Basic.Coordinates;
import Basic.PixelColorChecker;
import Basic.Press;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class MatchBasic {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * Check if there are 90 enemies
     * @return
     */
    public static boolean is90enemies() {
        Coordinates coordinates = new Coordinates(315, 122);
        Color expectedColor = new Color(194, 64, 73);
        String screenshotPath = "Resources/GameState.png";
        int tolerance = 5;

        return PixelColorChecker.checkColorMatch(coordinates, expectedColor, screenshotPath, tolerance);
    }

    /**
     * Check if Golem can be summoned
     * @return
     */
    public static boolean isGolemPresent() {
        Coordinates coordinates = new Coordinates(481, 223);
        Color expectedColor = new Color(93, 71, 61);
        String screenshotPath = "Resources/GameState.png";
        int tolerance = 20;

        return PixelColorChecker.checkColorMatch(coordinates, expectedColor, screenshotPath, tolerance);
    }

    /** Press functions */
    public static void pressSummon() {
        Coordinates topLeft = new Coordinates(175, 789);
        Coordinates bottomRight = new Coordinates(364, 880);
        String action = "Pressing Summon";
        Press.press(topLeft, bottomRight, action);
    }

    public static void pressGamble() {
        Coordinates topLeft = new Coordinates(410, 806);
        Coordinates bottomRight = new Coordinates(488, 875);
        String action = "Pressing Gamble";
        Press.press(topLeft, bottomRight, action);
    }

    public static void pressMythic() {
        Coordinates topLeft = new Coordinates(52, 805);
        Coordinates bottomRight = new Coordinates(130, 874);
        String action = "Pressing Mythic";
        Press.press(topLeft, bottomRight, action);
    }

    public static void pressUpgrade() {
        Coordinates topLeft = new Coordinates(175, 898);
        Coordinates bottomRight = new Coordinates(365, 946);
        String action = "Pressing Upgrade";
        Press.press(topLeft, bottomRight, action);
    }

    public static void pressGolem() {
        Coordinates topLeft = new Coordinates(448, 178);
        Coordinates bottomRight = new Coordinates(514, 239);
        String action = "Pressing Golem";
        Press.press(topLeft, bottomRight, action);
    }

    public static void challengeGolem() {
        Coordinates topLeft = new Coordinates(181, 550);
        Coordinates bottomRight = new Coordinates(358, 621);
        String action = "Challenging Golem";
        Press.press(topLeft, bottomRight, action);
    }

    public static void pressChallenge() {
        Coordinates topLeft = new Coordinates(448, 178);
        Coordinates bottomRight = new Coordinates(514, 239);
        String action = "Pressing Golem";
        Press.press(topLeft, bottomRight, action);
    }

    public static void pressUncommonGamble() {
        Coordinates topLeft = new Coordinates(103, 831);
        Coordinates bottomRight = new Coordinates(194, 867);
        String action = "Pressing Uncommon Gamble";
        Press.press(topLeft, bottomRight, action);
    }

    public static void pressEpicGamble() {
        Coordinates topLeft = new Coordinates(223, 831);
        Coordinates bottomRight = new Coordinates(315, 867);
        String action = "Pressing Epic Gamble";
        Press.press(topLeft, bottomRight, action);
    }

    public static void pressLegendaryGamble() {
        Coordinates topLeft = new Coordinates(346, 831);
        Coordinates bottomRight = new Coordinates(437, 867);
        String action = "Pressing Legendary Gamble";
        Press.press(topLeft, bottomRight, action);
    }

    public static void pressUpgradeMythic() {
        Press.press(new Coordinates(285, 821), new Coordinates(375, 857), "Upgrading Mythic");
    }

    public static void pressUpgradeSummoning() {
        Press.press(new Coordinates(409, 822), new Coordinates(502, 857), "Upgrading Summoning");
    }

    public static void pressUpgradeEpic() {
        Press.press(new Coordinates(161, 821), new Coordinates(254, 858), "Upgrading Epic");
    }

    public static void pressUpgradeCommon() {
        Press.press(new Coordinates(39, 823), new Coordinates(129, 858), "Upgrading Common");
    }

    /**
     * Check game for Lucky Stones quantity then return
     * @return
     */
    public static int checkLuckyStones() {
        pressUpgrade();

        // Load the GameState image
        Mat gameState = Imgcodecs.imread("Resources/GameState.png");

        // Define the coordinates for the sub-image
        // Replace these with your actual coordinates
        int topLeftX = 305; // Example X coordinate for the top-left corner
        int topLeftY = 645; // Example Y coordinate for the top-left corner
        int bottomRightX = 393; // Example X coordinate for the bottom-right corner
        int bottomRightY = 674; // Example Y coordinate for the bottom-right corner

        // Create a rectangle based on the coordinates
        Rect roi = new Rect(topLeftX, topLeftY, bottomRightX - topLeftX, bottomRightY - topLeftY);

        // Crop the sub-image
        Mat subImage = new Mat(gameState, roi);

        // Process the sub-image and convert it to an int
        int luckyStonesValue = processSubImage(subImage);

        return luckyStonesValue;
    }

    /**
     * Helper function to process subImage and return the read text as int
     * @param subImage
     * @return
     */
    private static int processSubImage(Mat subImage) {
        // Convert the Mat to BufferedImage
        BufferedImage bufferedImage = matToBufferedImage(subImage);

        // Initialize Tesseract
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("lib/Tess4j/tessdata"); // Set the path to the tessdata directory
        tesseract.setLanguage("eng"); // Set the language you want to use

        String extractedText = "";
        try {
            // Perform OCR on the buffered image
            extractedText = tesseract.doOCR(bufferedImage);
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        // Parse the extracted text into an integer
        // You may need to adjust this logic based on the format of the extracted text
        try {
            return Integer.parseInt(extractedText.trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0; // or some default value/error code
        }
    }

    /**
     * Helper function to turn matrix to BufferedImage then return the image
     * @param matrix
     * @return
     */
    private static BufferedImage matToBufferedImage(Mat matrix) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (matrix.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = matrix.channels() * matrix.cols() * matrix.rows();
        byte[] b = new byte[bufferSize];
        matrix.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);
        image.getRaster().setDataElements(0, 0, matrix.cols(), matrix.rows(), b);
        return image;
    }

    /**
     * Test function for checking Lucky Stones... Comment out
     * @param args
     */
    public static void main(String[] args) {
        int temp = checkLuckyStones();

        System.out.println(temp);
    }
}
