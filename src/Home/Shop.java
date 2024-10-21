package Home;

import Basic.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Shop {
    private static final Logger LOGGER = Logger.getLogger(Shop.class.getName());
    private static final int WAIT_TIME = 3000; // 3 seconds
    private static String baseResourcePath = "/Users/macbookpro/Lucky-Defense-Bot-V2/Resources/";

    // COORDINATES OF ITEM IMAGES
    private static Coordinates[] topLeftCoordinates;
    private static Coordinates[] bottomRightCoordinates;

    private static final List<String> shopImages = Arrays.asList(
            "300", "100", "30", "Key"  // TODO : GET BANDIT, MONEYGUN, AND SAFEBOX IMAGES
    );

    static {
        try {
            if(true) {
                initializeCoordinates();
                boolean isInitialized = true;
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize coordinates: " + e.getMessage());
        }
    }

    private static void initializeCoordinates() throws IOException {
        try {
            Coordinates refreshCoords = CompareImage.findRefreshButtonInGameState("Resources/RefreshButtons/", "Resources/GameState.png");
            if (refreshCoords.getY() == -1) {
                throw new IOException("Failed to find RefreshButton");
            }
            int topLeftRefresh = refreshCoords.getY();

            int Offset01 = topLeftRefresh - 375;
            int Offset234 = topLeftRefresh - 131;
            int Offset01Top = Offset01 - 99;
            int Offset234Top = Offset234 - 98;

            topLeftCoordinates = new Coordinates[]{
                new Coordinates(223, Offset01Top),
                new Coordinates(385, Offset01Top),
                new Coordinates(61, Offset234Top),
                new Coordinates(223, Offset234Top),
                new Coordinates(385, Offset234Top)
            };

            bottomRightCoordinates = new Coordinates[]{
                new Coordinates(316, Offset01),
                new Coordinates(478, Offset01),
                new Coordinates(154, Offset234),
                new Coordinates(316, Offset234),
                new Coordinates(478, Offset234)
            };

            LOGGER.info("Shop coordinates initialized successfully");
        } catch (IOException e) {
            LOGGER.severe("Error initializing coordinates: " + e.getMessage());
            throw new IOException("Failed to initialize shop coordinates", e);
        }
    }

    // Not really shop related
    // TODO : MOVE TO A DIFFERENT CLASS...
    public static void receiveMailBox() throws IOException, InterruptedException {
        Screenshot.screenshotGameState();
        if (HomeNotifications.checkInboxNotification()) {
            ButtonsHome.pressOptions();
            Thread.sleep(WAIT_TIME);
            ButtonsHome.pressMailBox();
            Thread.sleep(WAIT_TIME);
            ButtonsHome.pressReceiveAll();
            Thread.sleep(WAIT_TIME);
            ButtonsHome.pressAnywhere();
            ButtonsHome.closeMailBox();

            Screenshot.screenshotGameState();
            System.out.println("MailBox received...");
            return;
        }
        System.out.println("MailBox receiving failed...");
    }

    public static void swipeToShop() throws IOException, InterruptedException {
        try {
            Process process = Runtime.getRuntime().exec("adb shell input touchscreen swipe 260 850 260 0 7500");
            Thread.sleep(7500);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        Screenshot.screenshotGameState();
    }

    public static void swipeToTop() throws IOException {
        try {
            Process process = Runtime.getRuntime().exec("adb shell input touchscreen swipe 260 300 260 850");
            Thread.sleep(5000);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        Screenshot.screenshotGameState();
    }

    public static void swipeToKeys() throws IOException, InterruptedException {
        swipeToShop();
        Process process = Runtime.getRuntime().exec("adb shell input touchscreen swipe 260 850 260 45 7500");

        Screenshot.screenshotGameState();
    }

    private static boolean isUnitPressed() throws IOException {
        Color expectedColor = new Color(98, 115, 143);
        Color currentColor = PixelColorChecker.getPixelColor("Lucky-Defense-Bot-V2/Resources/GameState.png", new Coordinates(490, 104));
        return PixelColorChecker.isMatchingColor(expectedColor, currentColor, 5);
    }

    private static boolean isArtifactPressed() throws IOException {
        Color expectedColor = new Color(98, 115, 143);
        Color currentColor = PixelColorChecker.getPixelColor("Lucky-Defense-Bot-V2/Resources/GameState.png", new Coordinates(475, 242));
        return PixelColorChecker.isMatchingColor(expectedColor, currentColor, 5);
    }

    // TODO : MOVE TO DIFFERENT CLASS...
    private static BufferedImage loadGameStateImage() throws IOException {
        try {
            return ImageIO.read(new File("Resources/GameState.png"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load GameState.png", e);
        }
    }

    public static int findTopLeft() throws IOException {
        try {
            String refreshButtonPath = "Resources/RefreshButtons/3xRefresh.png"; // Provide the correct path
            String gameStatePath = "Resources/GameState.png"; // Provide the correct path

            Coordinates coords = CompareImage.findRefreshButtonInGameState(refreshButtonPath, gameStatePath);
            if (coords.getX() != -1 && coords.getY() != -1) {
                return coords.getY();
            } else {
                System.out.println("No refresh button found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static void main(String[] args) throws IOException {
//        logEnvironmentInfo();
//
//        // Uncomment and modify this line if you need to set a custom resource path
//        // baseResourcePath = "/Users/macbookpro/Lucky-Defense-Bot-V2/Resources/";
//
//        if (!verifyResources()) {
//            LOGGER.severe("Required resources are missing or inaccessible. Please check the resource directories and files.");
//            return;
//        }
//
//        try {
//            autoShop();
//        } catch (IOException e) {
//            LOGGER.severe("IO error occurred during shop automation: " + e.getMessage());
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            LOGGER.severe("Shop automation was interrupted: " + e.getMessage());
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            LOGGER.severe("Shop automation failed due to initialization error: " + e.getMessage());
//            e.printStackTrace();
//        }
        File gameStateFile = new File(baseResourcePath + "GameState.png");
        System.out.println("Absolute path to GameState.png: " + gameStateFile.getAbsolutePath());

        int temp = findTopLeft();
        System.out.println(temp);
    }
}
