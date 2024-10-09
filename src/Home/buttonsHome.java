package Home;

import Basic.Coordinates;

import java.io.IOException;

public class buttonsHome {

    static Process process;

    // Generalized method to press a button
    private static void press(Coordinates topLeft, Coordinates bottomRight, String actionName) {
        Coordinates temp = Coordinates.makeRandomCoordinate(topLeft, bottomRight);
        try {
            process = Runtime.getRuntime().exec("adb shell input tap " + temp.toString());
            System.out.println(actionName + " pressed at: " + temp.toString());
        } catch (IOException i) {
            System.out.println("Pressing " + actionName + " failed.");
        }
    }

    // Specific actions with top-left and bottom-right coordinates
    /*
    format is (topLeft, bottomRight, actionName)
    just presses buttons
     */
    public static void pressBattle() {
        press(new Coordinates(215, 875), new Coordinates(300, 951), "Battle");
    }

    public static void pressGuardian() {
        press(new Coordinates(108, 875), new Coordinates(197, 951), "Guardian");
    }

    public static void pressShop() {
        press(new Coordinates(3, 875), new Coordinates(97, 951), "Shop");
    }

    public static void pressArtifact() {
        press(new Coordinates(340, 875), new Coordinates(433, 951), "Artifact");
    }

    public static void pressRecruit() {
        press(new Coordinates(360, 108), new Coordinates(513, 115), "Recruit");
    }

    public static void pressQuest() {
        press(new Coordinates(455, 189), new Coordinates(510, 240), "Quest");
    }

    public static void pressHuntPass() {
        press(new Coordinates(455, 300), new Coordinates(510, 355), "Hunt Pass");
    }

    public static void closeRecruit() {
        press(new Coordinates(35, 900), new Coordinates(70, 931), "Close Recruit");
    }

    public static void closeQuest() {
        press(new Coordinates(475, 111), new Coordinates(506, 136), "Close Quest");
    }

    public static void closeHuntPass() {
        press(new Coordinates(477, 39), new Coordinates(505, 61), "Close Hunt Pass");
    }

    public static void pressRecruit10X() {
        press(new Coordinates(62, 750), new Coordinates(256, 820), "Recruit 10X");
    }

    public static void pressRecruit1X() {
        press(new Coordinates(287, 740), new Coordinates(468, 816), "Recruit 1X");
    }
}

