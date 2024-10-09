package Match;

import Basic.Coordinates;
import Basic.PixelColorChecker;
import java.io.IOException;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MatchBasic {
    private static final Logger LOGGER = Logger.getLogger(MatchBasic.class.getName());

    // check if color is a match
    public static boolean checkColorMatch(Coordinates coordinates, Color expectedColor, String screenshotPath, int tolerance) {
        try {
            Color pixelColor = PixelColorChecker.getPixelColor(screenshotPath, coordinates);

            if (pixelColor != null) {
                boolean isMatch = PixelColorChecker.isMatchingColor(pixelColor, expectedColor, tolerance);
                LOGGER.info(String.format("Color match: %b, Expected: %s, Actual: %s", isMatch, expectedColor, pixelColor));
                return isMatch;
            } else {
                LOGGER.warning("Failed to get pixel color");
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error checking color match", e);
        }

        return false;
    }

    public static boolean is90enemies() {
        Coordinates coordinates = new Coordinates(315, 122);
        Color expectedColor = new Color(194, 64, 73);
        String screenshotPath = "Resources/GameState.png";
        int tolerance = 5;

        return checkColorMatch(coordinates, expectedColor, screenshotPath, tolerance);
    }

    public static boolean isGolemPresent() {
        Coordinates coordinates = new Coordinates(315, 122);
        Color expectedColor = new Color(194, 64, 73);
        String screenshotPath = "Resources/GameState.png";
        int tolerance = 5;

        return checkColorMatch(coordinates, expectedColor, screenshotPath, tolerance);
    }
}
