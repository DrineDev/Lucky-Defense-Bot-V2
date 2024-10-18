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

    public static void pressItem(int itemIndex) throws IOException {
        Coordinates topLeft, bottomRight;
        String itemName;

        // Restricted Areas MUST NOT PRESS
        Coordinates[] restrictedTopLeftCoordinates = {
                new Coordinates(223, 324), // AREA 1
                new Coordinates(384, 324), // AREA 2
                new Coordinates(60, 568),  // AREA 3
                new Coordinates(222, 568), // AREA 4
                new Coordinates(384, 568)  // AREA 5
        };
        Coordinates[] restrictedBottomRightCoordinates = {
                new Coordinates(315, 424), // AREA 1
                new Coordinates(478, 424), // AREA 2
                new Coordinates(154, 668), // AREA 3
                new Coordinates(316, 668), // AREA 4
                new Coordinates(478, 668)  // AREA 5
        };

        switch (itemIndex) {
            case 0: // AREA 1
                topLeft = new Coordinates(205, 270);
                bottomRight = new Coordinates(338, 442);
                itemName = "Item 1";
                break;
            case 1: // AREA 2
                topLeft = new Coordinates(359, 279);
                bottomRight = new Coordinates(507, 493);
                itemName = "Item 2";
                break;
            case 2: // AREA 3
                topLeft = new Coordinates(33, 522);
                bottomRight = new Coordinates(180, 738);
                itemName = "Item 3";
                break;
            case 3: // AREA 4
                topLeft = new Coordinates(195, 522);
                bottomRight = new Coordinates(342, 736);
                itemName = "Item 4";
                break;
            case 4: // AREA 5
                topLeft = new Coordinates(355, 524);
                bottomRight = new Coordinates(503, 736);
                itemName = "Item 5";
                break;
            default:
                throw new IllegalArgumentException("Invalid item index: " + itemIndex);
        }

        // Generate a random press coordinate and check it doesn't overlap with restricted areas
        Coordinates pressCoordinate = new Coordinates();
        do {
            pressCoordinate = Coordinates.makeRandomCoordinate(topLeft, bottomRight);
        } while (Coordinates.isCoordinateInRestrictedArea(pressCoordinate, restrictedTopLeftCoordinates, restrictedBottomRightCoordinates));

        // Perform the press using the valid coordinate
        Press.press(pressCoordinate, "Opening " + itemName);

        // Take a screenshot after pressing
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

    public static void pressTopQuest() throws IOException {
        Press.press(new Coordinates(71, 330), new Coordinates(468, 410), "Top Quest");
        Screenshot.screenshotGameState();
    }

    public static void pressAchievement() throws IOException {
        Press.press(new Coordinates(273, 780), new Coordinates(484, 825), "Achievements");
        Screenshot.screenshotGameState();
    }

    public static void pressTopAchievement() throws IOException {
        Press.press(new Coordinates(70, 200), new Coordinates(468, 293), "Top Achievements");
        Screenshot.screenshotGameState();
    }

}

