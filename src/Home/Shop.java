package Home;

import Basic.Coordinates;
import Basic.ImageReader;
import Basic.PixelColorChecker;
import Basic.Screenshot;

import java.awt.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shop {

    private static final int MAX_ITEMS = 5;
    private static final int MAX_RETRIES = 3;
    private static final int WAIT_TIME = 2000; // 2 seconds

    // Not really shop related
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

    public static String readPrice() throws IOException {
        Coordinates topLeft = new Coordinates(224, 620);
        Coordinates bottomRight = new Coordinates(315, 642);
        return ImageReader.readImage(topLeft, bottomRight);
    }

    public static void swipeToShop() throws IOException {
        try {
            Process process = Runtime.getRuntime().exec("adb shell input touchscreen swipe 260 850 260 230 750");
            Thread.sleep(WAIT_TIME);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        Screenshot.screenshotGameState();
    }

    public static void autoShop(int parameter) throws IOException, InterruptedException {
        ButtonsHome.pressShop();
        Thread.sleep(WAIT_TIME);
        swipeToShop();
        Thread.sleep(WAIT_TIME);

        for (int itemIndex = 1; itemIndex <= MAX_ITEMS; itemIndex++) {
            processItem(itemIndex, parameter);
        }

        ButtonsHome.pressReset();
    }

    private static void processItem(int itemIndex, int parameter) throws IOException, InterruptedException {
        for (int retry = 0; retry < MAX_RETRIES; retry++) {
            ButtonsHome.checkItem(itemIndex);
            Thread.sleep(WAIT_TIME);

            if (isUnitPressed()) {
                ButtonsHome.closeUnit();
                Thread.sleep(WAIT_TIME);
                continue;
            }

            if (isArtifactPressed()) {
                ButtonsHome.closeArtifact();
                Thread.sleep(WAIT_TIME);
                continue;
            }

            String price = readPrice();
            if (price == null || price.isEmpty()) {
                System.out.println("Failed to read price for item " + itemIndex + ". Retrying...");
                continue;
            }

            int intPrice = extractNumericValue(price);
            System.out.println("Extracted price for item " + itemIndex + ": " + intPrice);

            if (intPrice == -1) {
                System.out.println("Invalid price for item " + itemIndex + ". Skipping...");
                ButtonsHome.closeItem();
                return;
            }

            if (shouldPurchase(intPrice, parameter)) {
                purchase(intPrice);
            } else {
                ButtonsHome.closeItem();
            }
            return; // Successfully processed the item
        }

        System.out.println("Failed to process item " + itemIndex + " after " + MAX_RETRIES + " attempts. Skipping...");
        ButtonsHome.closeItem();
    }

    private static boolean shouldPurchase(int price, int parameter) {
        switch (parameter) {
            case 1: // Buy all invitations
                return price == 42 || price == 140 || price == 420 || price == 960 || price == 2800 || price == 3200 || price == 8400 || price == 9600;
            case 2: // Buy all invitations with coins only
                return price == 960 || price == 2800 || price == 3200 || price == 8400 || price == 9600;
            case 3: // Buy all items that cost coins only
                return price >= 960; // Ensure this condition checks correctly
            case 4: // Buy all items
                return true;
            default:
                return false;
        }
    }

    private static void purchase(int price) throws IOException, InterruptedException {
        System.out.println("Purchasing item for " + price + "...");
        ButtonsHome.purchaseItem();
        Thread.sleep(WAIT_TIME);
        ButtonsHome.pressAnywhere();
        Thread.sleep(WAIT_TIME);
    }

    private static int extractNumericValue(String value) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group());
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric value after extraction: " + matcher.group());
            }
        } else {
            System.out.println("No numeric value found in: " + value);
        }
        return -1;
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

    public static void main(String[] args) {
        System.out.println("Starting Auto Shop process...");

        try {
            // Buy all items that cost coins only
            System.out.println("Buying all items that cost coins only...");
            autoShop(3);

            System.out.println("Auto Shop process completed successfully.");
        } catch (IOException e) {
            System.err.println("An IO error occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("The process was interrupted: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
