package Home;

import Basic.CompareImage;
import Basic.Coordinates;
import Basic.PixelColorChecker;
import Basic.Screenshot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Shop {

    private static final int MAX_ITEMS = 5;
    private static final int MAX_RETRIES = 3;
    private static final int WAIT_TIME = 3000; // 2 seconds

    private static Coordinates[] topLeftCoordinates = {
            new Coordinates(223, 324), // AREA 1
            new Coordinates(384, 324), // AREA 2
            new Coordinates(60, 568),  // AREA 3
            new Coordinates(222, 568), // AREA 4
            new Coordinates(384, 568)  // AREA 5
    };

    private static Coordinates[] bottomRightCoordinates = {
            new Coordinates(315, 424), // AREA 1
            new Coordinates(478, 424), // AREA 2
            new Coordinates(154, 668), // AREA 3
            new Coordinates(316, 668), // AREA 4
            new Coordinates(478, 668)  // AREA 5
    };

    private static final List<String> shopImages = Arrays.asList(
            "300", "100", "30", "Key", "Bandits", "Safebox", "MoneyGun"
    );

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

    public static void swipeToShop() throws IOException {
        try {
            Process process = Runtime.getRuntime().exec("adb shell input touchscreen swipe 260 850 260 0 7500");
            Thread.sleep(10000);
            process = Runtime.getRuntime().exec("adb shell input touchscreen swipe 260 850 260 775 7500");
            Thread.sleep(10000);
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

    public static void swipeToKeys() throws IOException {
        try {
            swipeToShop();
            Process process = Runtime.getRuntime().exec("adb shell input touchscreen swipe 260 850 260 45 7500");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
            BufferedImage croppedImage = cropImage(gameStateImage, topLeftCoordinates[i], bottomRightCoordinates[i]);
            checkAndBuyItem(croppedImage, i);
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
                break; // Exit loop after a successful match
            }
        }
    }

    private static void buyItem(int areaIndex) throws InterruptedException, IOException {
        ButtonsHome.pressItem(areaIndex);   // Press the shop item
        Thread.sleep(1000);                  // Wait for a moment
        ButtonsHome.purchaseItem();         // Purchase the item
        Thread.sleep(1000);
        ButtonsHome.pressAnywhere();        // Press anywhere to dismiss the dialog
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Thread.sleep(2000);
        autoShop();
    }
}
