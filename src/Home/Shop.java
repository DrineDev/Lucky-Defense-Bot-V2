package Home;

import Basic.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Shop {

    private static final int WAIT_TIME = 3000; // 3 seconds
    private static int topLeftRefresh;
    private static int Offset01;
    private static int Offset234;
    private static int Offset01Top;
    private static int Offset234Top;
    private static boolean isInitialized = false;

    // COORDINATES OF ITEM IMAGES
    private static Coordinates[] topLeftCoordinates;
    private static Coordinates[] bottomRightCoordinates;

    private static final List<String> shopImages = Arrays.asList(
            "300", "100", "30", "Key", "Bandits", "Safebox", "MoneyGun"
    );

    static {
        try {
            initializeCoordinates();
            isInitialized = true;
        } catch (IOException e) {
            System.err.println("Failed to initialize coordinates: " + e.getMessage());
        }
    }

    private static void initializeCoordinates() throws IOException {
        topLeftRefresh = findTopLeft();
        if (topLeftRefresh == -1) {
            throw new IOException("Failed to find RefreshButton");
        }
        Offset01 = topLeftRefresh - 375;
        Offset234 = topLeftRefresh - 131;
        Offset01Top = Offset01 - 99;
        Offset234Top = Offset234 - 98;

        topLeftCoordinates = new Coordinates[]{
                new Coordinates(223, Offset01Top), // AREA 1
                new Coordinates(385, Offset01Top), // AREA 2
                new Coordinates(61, Offset234Top),  // AREA 3
                new Coordinates(223, Offset234Top), // AREA 4
                new Coordinates(385, Offset234Top)  // AREA 5
        };

        bottomRightCoordinates = new Coordinates[]{
                new Coordinates(316, Offset01), // AREA 1
                new Coordinates(478, Offset01), // AREA 2
                new Coordinates(154, Offset234), // AREA 3
                new Coordinates(316, Offset234), // AREA 4
                new Coordinates(478, Offset234)  // AREA 5
        };
    }

    public Shop() throws IOException {
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
        Color currentColor = PixelColorChecker.getPixelColor("Resources/GameState.png", new Coordinates(490, 104));
        return PixelColorChecker.isMatchingColor(expectedColor, currentColor, 5);
    }

    private static boolean isArtifactPressed() throws IOException {
        Color expectedColor = new Color(98, 115, 143);
        Color currentColor = PixelColorChecker.getPixelColor("Resources/GameState.png", new Coordinates(475, 242));
        return PixelColorChecker.isMatchingColor(expectedColor, currentColor, 5);
    }

    // MAIN FUNCTION
    public static void autoShop() throws IOException, InterruptedException {
        if (!isInitialized) {
            throw new IllegalStateException("Shop coordinates not initialized properly");
        }
        // You can press reset a maximum of 3 times, so loop through it 3 times
        for (int attempt = 0; attempt < 3; attempt++) {
            Screenshot.screenshotGameState();
            processShopItems();
            ButtonsHome.pressReset();
        }
    }

    private static void processShopItems() throws IOException, InterruptedException {
        BufferedImage gameStateImage = loadGameStateImage();
        for (int i = 0; i < topLeftCoordinates.length; i++) {
            System.out.println("Processing item " + i + "...");
            BufferedImage croppedImage = cropImage(gameStateImage, topLeftCoordinates[i], bottomRightCoordinates[i]);
            checkAndBuyItem(croppedImage, i);
            Thread.sleep(WAIT_TIME);
        }
    }

    // TODO : MOVE TO DIFFERENT CLASS...
    private static BufferedImage loadGameStateImage() throws IOException {
        try {
            return ImageIO.read(new File("Resources/GameState.png"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load GameState.png", e);
        }
    }

    // TODO : MOVE TO DIFFERENT CLASS...
    private static BufferedImage cropImage(BufferedImage image, Coordinates topLeft, Coordinates bottomRight) {
        int x = topLeft.getX();
        int y = topLeft.getY();
        int width = bottomRight.getX() - x;
        int height = bottomRight.getY() - y;
        return image.getSubimage(x, y, width, height);
    }

    private static void checkAndBuyItem(BufferedImage croppedImage, int areaIndex) throws IOException, InterruptedException {
        for (String shopImage : shopImages) {
            String imagePath = String.format("Resources/ShopFiles/%s.png", shopImage);
            if (CompareImage.compareImage(croppedImage, imagePath)) {
                System.out.println("Match found in Area " + (areaIndex) + " for shop item: " + shopImage);
                buyItem(areaIndex);
                break;
            }
        }
    }

    private static void buyItem(int areaIndex) throws InterruptedException, IOException {
        // Press the yellow area below the item instead of the item itself
        ButtonsHome.pressItem(areaIndex);
        Thread.sleep(WAIT_TIME);

        // Check if we unintentionally opened an artifact or unit
        if (isArtifactPressed() || isUnitPressed()) {
            System.out.println("Unintended press detected. Closing and retrying.");
            if (isArtifactPressed()) {
                ButtonsHome.closeArtifact();
            } else {
                ButtonsHome.closeUnit();
            }
            Thread.sleep(WAIT_TIME);
            return;
        }

        ButtonsHome.purchaseItem();
        Thread.sleep(WAIT_TIME);
        ButtonsHome.pressAnywhere();
        Thread.sleep(WAIT_TIME);
    }

    public static int findTopLeft() throws IOException {
        String refreshButtonsPath = "Resources/RefreshButtons/";
        Coordinates topLeft = CompareImage.findRefreshButtonsInGameState(refreshButtonsPath);

        if (topLeft.getX() != -1 && topLeft.getY() != -1) {
            System.out.println("Found refresh button at: " + topLeft.getX() + ", " + topLeft.getY());
            return topLeft.getY();
        } else {
            System.out.println("No refresh button found. Please check the following:");
            System.out.println("1. Ensure 'Resources/GameState.png' is up to date and contains the shop screen.");
            System.out.println("2. Verify that all refresh button images (Shop1_cropped.png to Shop4_cropped.png) are present in " + refreshButtonsPath);
            System.out.println("3. Make sure the refresh button images accurately represent what's in the GameState.png");
            return -1;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

    }
}
