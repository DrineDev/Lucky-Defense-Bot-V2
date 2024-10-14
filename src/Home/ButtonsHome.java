package Home;

import Basic.Coordinates;
import Basic.Press;
import Basic.Screenshot;

import java.io.IOException;

public class ButtonsHome {

    // Specific actions with top-left and bottom-right coordinates
    /*
    format is (topLeft, bottomRight, actionName)
    just presses buttons
     */
    public static void pressBattle() throws IOException {
        Press.press(new Coordinates(215, 875), new Coordinates(300, 951), "Battle");
        Screenshot.screenshotGameState();
    }

    public static void pressGuardian() throws IOException {
        Press.press(new Coordinates(108, 875), new Coordinates(197, 951), "Guardian");
        Screenshot.screenshotGameState();
    }

    public static void pressShop() throws IOException {
        Press.press(new Coordinates(3, 875), new Coordinates(97, 951), "Shop");
        Screenshot.screenshotGameState();
    }

    public static void pressArtifact() throws IOException {
        Press.press(new Coordinates(340, 875), new Coordinates(433, 951), "Artifact");
        Screenshot.screenshotGameState();
    }

    public static void pressRecruit() throws IOException {
        Press.press(new Coordinates(360, 108), new Coordinates(513, 115), "Recruit");
        Screenshot.screenshotGameState();
    }

    public static void pressQuest() throws IOException {
        Press.press(new Coordinates(455, 189), new Coordinates(510, 240), "Quest");
        Screenshot.screenshotGameState();
    }

    public static void pressHuntPass() throws IOException {
        Press.press(new Coordinates(455, 300), new Coordinates(510, 355), "Hunt Pass");
        Screenshot.screenshotGameState();
    }

    public static void closeRecruit() throws IOException {
        Press.press(new Coordinates(35, 900), new Coordinates(70, 931), "Close Recruit");
        Screenshot.screenshotGameState();
    }

    public static void closeQuest() throws IOException {
        Press.press(new Coordinates(475, 111), new Coordinates(506, 136), "Close Quest");
        Screenshot.screenshotGameState();
    }

    public static void closeHuntPass() throws IOException {
        Press.press(new Coordinates(477, 39), new Coordinates(505, 61), "Close Hunt Pass");
        Screenshot.screenshotGameState();
    }

    public static void pressRecruit10X() throws IOException {
        Press.press(new Coordinates(62, 750), new Coordinates(256, 820), "Recruit 10X");
        Screenshot.screenshotGameState();
    }

    public static void pressRecruit1X() throws IOException {
        Press.press(new Coordinates(287, 740), new Coordinates(468, 816), "Recruit 1X");
        Screenshot.screenshotGameState();
    }

    public static void pressOptions() throws IOException {
        Press.press(new Coordinates(481, 50), new Coordinates(519, 88), "Options");
        Screenshot.screenshotGameState();
    }

    public static void pressMailBox() throws IOException {
        Press.press(new Coordinates(321, 325), new Coordinates(505, 382), "MailBox");
        Screenshot.screenshotGameState();
    }

    public static void pressOneMore() throws IOException {
        Press.press(new Coordinates(169, 734), new Coordinates(370, 826), "One More");
        Screenshot.screenshotGameState();
    }

    public static void pressReceiveAll() throws IOException {
        Press.press(new Coordinates(169, 651), new Coordinates(368, 720), "Receive");
        Screenshot.screenshotGameState();
    }

    public static void pressAnywhere() throws IOException {
        Press.press(new Coordinates(0, 0), new Coordinates(540, 960), "Anywhere");
        Screenshot.screenshotGameState();
    }

    public static void closeMailBox() throws IOException {
        Press.press(new Coordinates(474, 170), new Coordinates(510, 203), "Close MailBox");
        Screenshot.screenshotGameState();
    }

    public static void checkItem(int itemIndex) throws IOException {
        Coordinates topLeft, bottomRight;
        String itemName;

        switch (itemIndex) {
            case 1:
                topLeft = new Coordinates(205, 270);
                bottomRight = new Coordinates(338, 442);
                itemName = "Item 1";
                break;
            case 2:
                topLeft = new Coordinates(365, 270);
                bottomRight = new Coordinates(500, 442);
                itemName = "Item 2";
                break;
            case 3:
                topLeft = new Coordinates(38, 511);
                bottomRight = new Coordinates(172, 692);
                itemName = "Item 3";
                break;
            case 4:
                topLeft = new Coordinates(205, 511);
                bottomRight = new Coordinates(335, 692);
                itemName = "Item 4";
                break;
            case 5:
                topLeft = new Coordinates(365, 511);
                bottomRight = new Coordinates(500, 692);
                itemName = "Item 5";
                break;
            default:
                throw new IllegalArgumentException("Invalid item index: " + itemIndex);
        }

        Press.press(topLeft, bottomRight, "Opening " + itemName);
        Screenshot.screenshotGameState();
    }

    public static void purchaseItem() throws IOException {
        Press.press(new Coordinates(172, 577), new Coordinates(360, 645), "Purchasing item");
        Screenshot.screenshotGameState();
    }

    public static void closeItem() throws IOException {
        Press.press(new Coordinates(456, 276), new Coordinates(493, 308), "Closing item");
        Screenshot.screenshotGameState();
    }

    public static void pressReset() throws IOException {
        Press.press(new Coordinates(254, 784), new Coordinates(403, 826), "Resetting shop");
        Screenshot.screenshotGameState();
    }

    public static void closeUnit() throws IOException {
        Press.press(new Coordinates(475, 100), new Coordinates(505, 128), "Closing unit");
        Screenshot.screenshotGameState();
    }

    public static void closeArtifact() throws IOException {
        Press.press(new Coordinates(456, 237), new Coordinates(493, 270), "Closing artifact");
        Screenshot.screenshotGameState();
    }
}
