package Home;

import Basic.Coordinates;
import Basic.Press;

public class buttonsHome {

    // Specific actions with top-left and bottom-right coordinates
    /*
    format is (topLeft, bottomRight, actionName)
    just presses buttons
     */
    public static void pressBattle() {
        Press.press(new Coordinates(215, 875), new Coordinates(300, 951), "Battle");
    }

    public static void pressGuardian() {
        Press.press(new Coordinates(108, 875), new Coordinates(197, 951), "Guardian");
    }

    public static void pressShop() {
        Press.press(new Coordinates(3, 875), new Coordinates(97, 951), "Shop");
    }

    public static void pressArtifact() {
        Press.press(new Coordinates(340, 875), new Coordinates(433, 951), "Artifact");
    }

    public static void pressRecruit() {
        Press.press(new Coordinates(360, 108), new Coordinates(513, 115), "Recruit");
    }

    public static void pressQuest() {
        Press.press(new Coordinates(455, 189), new Coordinates(510, 240), "Quest");
    }

    public static void pressHuntPass() {
        Press.press(new Coordinates(455, 300), new Coordinates(510, 355), "Hunt Pass");
    }

    public static void closeRecruit() {
        Press.press(new Coordinates(35, 900), new Coordinates(70, 931), "Close Recruit");
    }

    public static void closeQuest() {
        Press.press(new Coordinates(475, 111), new Coordinates(506, 136), "Close Quest");
    }

    public static void closeHuntPass() {
        Press.press(new Coordinates(477, 39), new Coordinates(505, 61), "Close Hunt Pass");
    }

    public static void pressRecruit10X() {
        Press.press(new Coordinates(62, 750), new Coordinates(256, 820), "Recruit 10X");
    }

    public static void pressRecruit1X() {
        Press.press(new Coordinates(287, 740), new Coordinates(468, 816), "Recruit 1X");
    }
}

