package Match;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class MatchBasic {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void closeGamble() {
        Press.press(new Coordinates(459, 645), new Coordinates(496, 678), "Closing Gamble");
    }

    public static void closeUpgrade() {
        Press.press(new Coordinates(487, 653), new Coordinates(524, 686), "Closing Upgrade");
    }

    public static void closeMythic() {
        Press.press(new Coordinates(456, 162), new Coordinates(493, 196), "Closing Upgrade");
    }

    public static boolean isFindingMatch() {
        Coordinates coordinates = new Coordinates(0, 0);
        Color expectedColor = new Color(90, 76, 65);
        String screenshotPath = "Resources/GameState.png";
        int tolerance = 5;

        return PixelColorChecker.checkColorMatch(coordinates, expectedColor, screenshotPath, tolerance);
    }

    public static boolean isInLobby() {
        Coordinates coordinates = new Coordinates(0, 0);
        Color expectedColor = new Color(84, 123, 44);
        String screenshotPath = "Resources/GameState.png";
        int tolerance = 5;

        return PixelColorChecker.checkColorMatch(coordinates, expectedColor, screenshotPath, tolerance);
    }

    public static boolean isIngame() throws IOException {
        Screenshot.screenshotGameState();
        Coordinates coordinates = new Coordinates(0, 0);
        Color expectedColor = new Color(98, 130, 42);
        String screenshotPath = "Resources/GameState.png";
        int tolerance = 75;

        return PixelColorChecker.checkColorMatch(coordinates, expectedColor, screenshotPath, tolerance);
    }

    public static boolean isOver() throws IOException {
        Screenshot.screenshotGameState();
        Coordinates coordinates = new Coordinates(0, 0);
        Color expectedColor = new Color(90, 73, 66);
        String screenshotPath = "Resources/GameState.png";
        int tolerance = 20;

        return PixelColorChecker.checkColorMatch(coordinates, expectedColor, screenshotPath, tolerance);
    }

    public static boolean isLoading() throws IOException {
        Screenshot.screenshotGameState();
        Coordinates coordinates = new Coordinates(0, 0);
        Color expectedColor = new Color(107, 81, 70);
        String screenshotPath = "Resources/GameState.png";
        int tolerance = 20;

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

    public static void pressSummon10X() {
        Coordinates topLeft = new Coordinates(175, 789);
        Coordinates bottomRight = new Coordinates(364, 880);
        String action = "Pressing Summon";

        Coordinates temp = Coordinates.makeRandomCoordinate(topLeft, bottomRight);
        Press.press(temp, action);
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

    public static void pressAnywhere() {
        Press.press(new Coordinates(540, 0), "Pressing anywhere");
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

    public static void pressBuildFavoriteMythic() {
        Press.press(new Coordinates(30, 175), new Coordinates(91, 237), "Building favorite mythic");
    }

    public static int checkLuckyStones() throws InterruptedException, IOException {
        pressGamble();
        Thread.sleep(1500);
        Screenshot.screenshotGameState();

        // Load the GameState image
        Mat gameState = Imgcodecs.imread("Resources/GameState.png");

        // Define the coordinates for the sub-image
        // Replace these with your actual coordinates
        int topLeftX = 194; // Example X coordinate for the top-left corner
        int topLeftY = 644; // Example Y coordinate for the top-left corner
        int bottomRightX = 264; // Example X coordinate for the bottom-right corner
        int bottomRightY = 674; // Example Y coordinate for the bottom-right corner

        // Create a rectangle based on the coordinates
        Rect roi = new Rect(topLeftX, topLeftY, bottomRightX - topLeftX, bottomRightY - topLeftY);

        // Crop the sub-image
        Mat subImage = new Mat(gameState, roi);

        // Process the sub-image and convert it to an int
        int luckyStonesValue;
        luckyStonesValue = Integer.parseInt(processSubImage(subImage));

        Thread.sleep(2000);

        return luckyStonesValue;
    }
    public static boolean isBossClear() throws IOException {
        Screenshot.screenshotGameState();
        Coordinates coordinates = new Coordinates(0, 0);
        Color expectedColor = new Color(92, 81, 62);
        String screenshotPath = "Resources/GameState.png";
        int tolerance = 5;

        return PixelColorChecker.checkColorMatch(coordinates, expectedColor, screenshotPath, tolerance);
    }
    public static void pressSelect() throws IOException {
        Press.press(new Coordinates(172, 633), new Coordinates(367, 714), "Confirming selection...");
        Screenshot.screenshotGameState();
    }
    public static void pressMostRight() throws IOException {
        Press.press(new Coordinates(363, 314), new Coordinates(493, 479), "Selecting most right...");
        Screenshot.screenshotGameState();
    }
    public static boolean checkIfMax() throws InterruptedException, IOException {
        pressGamble();
        Thread.sleep(1500);
        Screenshot.screenshotGameState();

        // Load the GameState image
        Mat gameState = Imgcodecs.imread("Resources/GameState.png");

        // Define the coordinates for the sub-image
        int topLeftX = 293; // X coordinate for the top-left corner
        int topLeftY = 644; // Y coordinate for the top-left corner
        int bottomRightX = 374; // X coordinate for the bottom-right corner
        int bottomRightY = 673; // Y coordinate for the bottom-right corner

        // Create a rectangle based on the coordinates
        Rect roi = new Rect(topLeftX, topLeftY, bottomRightX - topLeftX, bottomRightY - topLeftY);

        // Crop the sub-image
        Mat subImage = new Mat(gameState, roi);

        // Process the sub-image and get the result string
        String string = processSubImage(subImage);

        // Pattern to match "<num>/<num>" format
        Pattern pattern = Pattern.compile("(\\d+)/(\\d+)");
        Matcher matcher = pattern.matcher(string);

        // Check if it matches the pattern
        if (matcher.find()) {
            // Extract the two numbers
            int firstNum = Integer.parseInt(matcher.group(1));
            int secondNum = Integer.parseInt(matcher.group(2));

            // Compare the two numbers and return true if they are equal
            return firstNum == secondNum;
        }

        closeGamble();
        // Return false if the format is incorrect
        return false;
    }

    private static String processSubImage(Mat subImage) {
        // Convert the Mat to BufferedImage
        BufferedImage bufferedImage = matToBufferedImage(subImage);

        // Initialize Tesseract
        Tesseract tesseract = new Tesseract();
        tesseract.setTessVariable("tessedit_char_whitelist", "0123456789/");
        tesseract.setDatapath("lib/Tess4j/tessdata"); // Set the path to the tessdata directory
        tesseract.setLanguage("eng"); // Set the language you want to use

        String extractedText = "";
        try {
            // Perform OCR on the buffered image
            extractedText = tesseract.doOCR(bufferedImage);
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        try {
            return extractedText.trim();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "0"; // or some default value/error code
        }
    }

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
}
