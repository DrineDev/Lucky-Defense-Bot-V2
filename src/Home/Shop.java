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
    private static int topLeftRefresh;
    private static int Offset01;
    private static int Offset234;
    private static int Offset01Top;
    private static int Offset234Top;
    private static boolean isInitialized = false;
    private static String baseResourcePath = "Lucky-Defense-Bot-V2/Lucky-Defense-Bot-V2/Resources/";

    // COORDINATES OF ITEM IMAGES
    private static Coordinates[] topLeftCoordinates;
    private static Coordinates[] bottomRightCoordinates;

    private static final List<String> shopImages = Arrays.asList(
            "300", "100", "30", "Key"  // TODO : GET BANDIT, MONEYGUN, AND SAFEBOX IMAGES
    );

    static {
        try {
            logEnvironmentInfo();
            if(verifyResources()) {
                initializeCoordinates();
                isInitialized = true;
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize coordinates: " + e.getMessage());
        }
    }

    private static void logEnvironmentInfo() {
        String currentDir = System.getProperty("user.dir");
        String absoluteResourcePath = new File(baseResourcePath).getAbsolutePath();

        LOGGER.info("Current working directory: " + currentDir);
        LOGGER.info("Absolute path to resources: " + absoluteResourcePath);
        LOGGER.info("Java classpath: " + System.getProperty("java.class.path"));
    }

    private static void initializeCoordinates() throws IOException {
        try {
            Coordinates refreshCoords = CompareImage.findRefreshButtonInGameState("Lucky-Defense-Bot-V2/Lucky-Defense-Bot-V2/Resources/RefreshButtons/");
            if (refreshCoords.getY() == -1) {
                throw new IOException("Failed to find RefreshButton");
            }
            topLeftRefresh = refreshCoords.getY();

            Offset01 = topLeftRefresh - 375;
            Offset234 = topLeftRefresh - 131;
            Offset01Top = Offset01 - 99;
            Offset234Top = Offset234 - 98;

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
            LOGGER.severe("Shop coordinates not initialized properly");
            throw new IllegalStateException("Shop coordinates not initialized properly");
        }
        // You can press reset a maximum of 3 times, so loop through it 3 times
        for (int attempt = 0; attempt < 3; attempt++) {
            Screenshot.screenshotGameState();
            processShopItems();
            ButtonsHome.pressReset();
            Thread.sleep(WAIT_TIME);
        }
    }

    private static void processShopItems() throws IOException, InterruptedException {
        String inputImagePath = "Lucky-Defense-Bot-V2/Resources/GameState.png";
        BufferedImage gameStateImage = ImageIO.read(new File(inputImagePath));

        for (int i = 0; i < topLeftCoordinates.length; i++) {
            System.out.println("Processing item " + i + "...");

            // Generating the output path for each cropped image
            String outputImagePath = "Resources/shopFiles/Cropped_Item_" + i + ".png";

            // Perform cropping and save the cropped image
            cropAndSaveShopImage(inputImagePath, outputImagePath, topLeftCoordinates[i], bottomRightCoordinates[i]);

            // Load the cropped image and check for matches in the shopFiles directory
            BufferedImage croppedImage = ImageIO.read(new File(outputImagePath));
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
    private static void cropAndSaveShopImage(String inputImagePath, String outputImagePath, Coordinates topLeft, Coordinates bottomRight) throws IOException {
        File inputFile = new File(inputImagePath);
        if (!inputFile.exists()) {
            throw new IOException("Input file does not exist: " + inputImagePath);
        }

        BufferedImage originalImage = ImageIO.read(inputFile);

        // Ensuring the cropping area stays within image bounds
        int x = Math.max(0, topLeft.getX());
        int y = Math.max(0, topLeft.getY());
        int width = Math.min(bottomRight.getX() - x, originalImage.getWidth() - x);
        int height = Math.min(bottomRight.getY() - y, originalImage.getHeight() - y);

        if (width <= 0 || height <= 0) {
            throw new IOException("Invalid crop dimensions for " + inputImagePath + ": x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);
        }

        System.out.println("Cropping " + inputImagePath + " with area: x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);

        // Perform cropping
        BufferedImage croppedImage = originalImage.getSubimage(x, y, width, height);

        // Save the cropped image
        File outputFile = new File(outputImagePath);
        ImageIO.write(croppedImage, "png", outputFile);

        System.out.println("Saved cropped image to: " + outputImagePath);
    }


    private static void buyItem(int areaIndex) throws InterruptedException, IOException {
        ButtonsHome.pressItem(areaIndex);
        Thread.sleep(WAIT_TIME);

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

    private static void checkAndBuyItem(BufferedImage croppedImage, int areaIndex) throws IOException, InterruptedException {
        File shopFilesDir = new File("Resources/shopFiles/");
        File[] shopFiles = shopFilesDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

        if (shopFiles != null) {
            for (File shopFile : shopFiles) {
                BufferedImage shopImage = ImageIO.read(shopFile);
                if (CompareImage.compareImage(croppedImage, shopFile.getAbsolutePath())) {
                    System.out.println("Found matching item: " + shopFile.getName() + " in area " + areaIndex);
                    buyItem(areaIndex);
                    return;
                }
            }
        }
        System.out.println("No matching item found in area " + areaIndex);
    }

    public static int findTopLeft() throws IOException {
        String refreshButtonsPath = "Resources/RefreshButtons/";
        Coordinates topLeft = CompareImage.findRefreshButtonInGameState(refreshButtonsPath);

        if (topLeft.getX() != -1 && topLeft.getY() != -1) {
            System.out.println("Found refresh button at: " + topLeft.getX() + ", " + topLeft.getY());
            return topLeft.getY();
        } else {
            System.out.println("No refresh button found.");
            return -1;
        }
    }

    // CHECK IF FILE PATHS ARE VALID
    private static boolean verifyResources() {
        File gameStateFile = new File(baseResourcePath + "GameState.png");
        File refreshButtonsDir = new File(baseResourcePath + "RefreshButtons");
        File shopFilesDir = new File(baseResourcePath + "shopFiles");

        LOGGER.info("Checking for GameState.png at: " + gameStateFile.getAbsolutePath());
        LOGGER.info("Checking for RefreshButtons directory at: " + refreshButtonsDir.getAbsolutePath());
        LOGGER.info("Checking for shopFiles directory at: " + shopFilesDir.getAbsolutePath());

        if (!gameStateFile.exists()) {
            LOGGER.severe("GameState.png does not exist at: " + gameStateFile.getAbsolutePath());
            return false;
        }

        if (!refreshButtonsDir.exists() || !refreshButtonsDir.isDirectory()) {
            LOGGER.severe("RefreshButtons directory does not exist or is not a directory: " + refreshButtonsDir.getAbsolutePath());
            return false;
        }

        if (!shopFilesDir.exists() || !shopFilesDir.isDirectory()) {
            LOGGER.severe("shopFiles directory does not exist or is not a directory: " + shopFilesDir.getAbsolutePath());
            return false;
        }

        LOGGER.info("All required resources verified successfully.");
        return true;
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
