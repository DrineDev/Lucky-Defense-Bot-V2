package Match;

import Basic.Coordinates;
import Basic.PixelColorChecker;
import Basic.Press;
import java.awt.Color;

public class MatchBasic {

    public static boolean is90enemies() {
        Coordinates coordinates = new Coordinates(315, 122);
        Color expectedColor = new Color(194, 64, 73);
        String screenshotPath = "Resources/GameState.png";
        int tolerance = 5;

        return PixelColorChecker.checkColorMatch(coordinates, expectedColor, screenshotPath, tolerance);
    }

    public static boolean isGolemPresent() {
        Coordinates coordinates = new Coordinates(481, 223);
        Color expectedColor = new Color(93, 71, 61);
        String screenshotPath = "Resources/GameState.png";
        int tolerance = 20;

        return PixelColorChecker.checkColorMatch(coordinates, expectedColor, screenshotPath, tolerance);
    }

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

}
