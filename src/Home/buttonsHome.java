package Home;
import java.io.IOException;

public class buttonsHome {

    static Process process;

    public static void pressBattle() {
        Coordinates battleTopLeft = new Coordinates(215, 875);
        Coordinates battleBottomRight = new Coordinates(300, 951);
        Coordinates temp = Coordinates.makeRandomCoordinate(battleTopLeft, battleBottomRight);

        try {
            process = Runtime.getRuntime().exec("adb shell input tap " + temp.toString());
        } catch (IOException i) {
            System.out.println("Pressing failed.");
        }
    }

    public static void pressGuardian() {
        Coordinates guardianTopLeft = new Coordinates(108, 875);
        Coordinates guardianBottomRight = new Coordinates(197, 951);
        Coordinates temp = Coordinates.makeRandomCoordinate(guardianTopLeft, guardianBottomRight);

        try {
            process = Runtime.getRuntime().exec("adb shell input tap " + temp.toString());
        } catch (IOException i) {
            System.out.println("Pressing failed.");
        }
    }

    public static void pressShop() {
        Coordinates shopTopLeft = new Coordinates(3, 875);
        Coordinates shopBottomRight = new Coordinates(97, 951);
        Coordinates temp = Coordinates.makeRandomCoordinate(shopTopLeft, shopBottomRight);

        try {
            process = Runtime.getRuntime().exec("adb shell input tap " + temp.toString());
        } catch (IOException i) {
            System.out.println("Pressing failed.");
        }
    }

    public static void pressArtifact() {
        Coordinates artifactTopLeft = new Coordinates(340, 875);
        Coordinates artifactBottomRight = new Coordinates(433, 951);
        Coordinates temp = Coordinates.makeRandomCoordinate(artifactTopLeft, artifactBottomRight);

        try {
            process = Runtime.getRuntime().exec("adb shell input tap " + temp.toString());
        } catch (IOException i) {
            System.out.println("Pressing failed.");
        }
    }

    public static void pressRecruit() {
        Coordinates recruitTopLeft = new Coordinates(360, 108);
        Coordinates recruitBottomRight = new Coordinates(513, 115);
        Coordinates temp = Coordinates.makeRandomCoordinate(recruitTopLeft, recruitBottomRight);

        try {
            process = Runtime.getRuntime().exec("adb shell input tap " + temp.toString());
        } catch (IOException i) {
            System.out.println("Pressing failed.");
        }
    }

    public static void pressQuest() {
        Coordinates questTopLeft = new Coordinates(455, 189);
        Coordinates questBottomRight = new Coordinates(510, 240);
        Coordinates temp = Coordinates.makeRandomCoordinate(questTopLeft, questBottomRight);

        try {
            process = Runtime.getRuntime().exec("adb shell input tap " + temp.toString());
        } catch (IOException i) {
            System.out.println("Pressing failed.");
        }
    }

    public static void pressHuntPass() {
        Coordinates huntPassTopLeft = new Coordinates(455, 300);
        Coordinates huntPassBottomRight = new Coordinates(510, 355);
        Coordinates temp = Coordinates.makeRandomCoordinate(huntPassTopLeft, huntPassBottomRight);

        try {
            process = Runtime.getRuntime().exec("adb shell input tap " + temp.toString());
        } catch (IOException i) {
            System.out.println("Pressing failed.");
        }
    }

    public static void closeRecruit() {
        Coordinates closeRecruitTopLeft = new Coordinates(35, 900);
        Coordinates closeRecruitBottomRight = new Coordinates(70, 931);
        Coordinates temp = Coordinates.makeRandomCoordinate(closeRecruitTopLeft, closeRecruitBottomRight);

        try {
            process = Runtime.getRuntime().exec("adb shell input tap " + temp.toString());
        } catch (IOException i) {
            System.out.println("Pressing failed.");
        }
    }

    public static void closeQuest() {
        Coordinates closeQuestTopLeft = new Coordinates(475, 111);
        Coordinates closeQuestBottomRight = new Coordinates(506, 136);
        Coordinates temp = Coordinates.makeRandomCoordinate(closeQuestTopLeft, closeQuestBottomRight);

        try {
            process = Runtime.getRuntime().exec("adb shell input tap " + temp.toString());
        } catch (IOException i) {
            System.out.println("Pressing failed.");
        }
    }

    public static void closeHuntPass() {
        Coordinates closeHuntPassTopLeft = new Coordinates(477, 39);
        Coordinates closeHuntPassBottomRight = new Coordinates(505, 61);
        Coordinates temp = Coordinates.makeRandomCoordinate(closeHuntPassTopLeft, closeHuntPassBottomRight);

        try {
            process = Runtime.getRuntime().exec("adb shell input tap " + temp.toString());
        } catch (IOException i) {
            System.out.println("Pressing failed.");
        }
    }

    public static void pressRecruit10X() {
        Coordinates recruit10XTopLeft = new Coordinates(62, 750);
        Coordinates recruit10XBottomRight = new Coordinates(256 , 820);
        Coordinates temp = Coordinates.makeRandomCoordinate(recruit10XTopLeft, recruit10XBottomRight);

        try {
            process = Runtime.getRuntime().exec("adb shell input tap " + temp.toString());
        } catch (IOException i) {
            System.out.println("Pressing failed");
        }
    }

    public static void pressRecruit1X() {
        Coordinates recruit1XTopLeft = new Coordinates(287, 740);
        Coordinates recruit1XBottomRight = new Coordinates(468 , 816);
        Coordinates temp = Coordinates.makeRandomCoordinate(recruit1XTopLeft, recruit1XBottomRight);

        try {
            process = Runtime.getRuntime().exec("adb shell input tap " + temp.toString());
        } catch (IOException i) {
            System.out.println("Pressing failed");
        }
    }
}
