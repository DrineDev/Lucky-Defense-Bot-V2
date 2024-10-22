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

/**
 * The Shop class handles operations related to the shop interface in the game.
 * It initializes coordinates for shop items, handles mail receiving, and provides methods to swipe through the game interface.
 */
public class Shop {
    private static final Logger LOGGER = Logger.getLogger(Shop.class.getName());
    private static final int WAIT_TIME = 3000; // Wait time in milliseconds (3 seconds) for certain operations
    private static String baseResourcePath = "Resources/"; // Base path for resource files

    // COORDINATES OF ITEM IMAGES
    private static Coordinates[] topLeftCoordinates; // Array to store top-left coordinates of item images
    private static Coordinates[] bottomRightCoordinates; // Array to store bottom-right coordinates of item images

    // List of shop item images to be processed
    private static final List<String> shopImages = Arrays.asList(
            "300", "100", "30", "Key"  // TODO: Add images for Bandit, Moneygun, and SafeBox
    );

    // Static initializer block for initializing coordinates
    static {
        try {
            if (true) {
                initializeCoordinates(); // Initialize coordinates
                boolean isInitialized = true; // This variable is not used
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize coordinates: " + e.getMessage());
        }
    }

    /**
     * Initializes the coordinates of the shop items based on the position of the refresh button in the game state.
     *
     * @throws IOException If there is an error finding the refresh button or initializing coordinates.
     */
    private static void initializeCoordinates() throws IOException {
        try {
            // Find the refresh button's coordinates in the game state image
//            Coordinates refreshCoords = CompareImage.findRefreshButtonInGameState("Resources/RefreshButtons/", "Resources/GameState.png");
//            if (refreshCoords.getY() == -1) {
//                throw new IOException("Failed to find RefreshButton");
//            }
            int topLeftRefresh = findTopLeft(); // Get the y-coordinate of the refresh button

            // Calculate offsets for the coordinates of shop items based on the refresh button position
            int Offset01 = topLeftRefresh - 375;
            int Offset234 = topLeftRefresh - 131;
            int Offset01Top = Offset01 - 99;
            int Offset234Top = Offset234 - 98;

            // Initialize top-left and bottom-right coordinates of shop items
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

    // Not really shop related; consider moving to a different class
    /**
     * Receives mail from the inbox if notifications are present.
     *
     * @throws IOException If there is an error taking screenshots or interacting with the game.
     * @throws InterruptedException If the thread is interrupted during sleep.
     */
    public static void receiveMailBox() throws IOException, InterruptedException {
        Screenshot.screenshotGameState(); // Take a screenshot of the current game state
        if (HomeNotifications.checkInboxNotification()) { // Check if there's a mail notification
            ButtonsHome.pressOptions(); // Open options menu
            Thread.sleep(WAIT_TIME); // Wait for the menu to load
            ButtonsHome.pressMailBox(); // Open the mailbox
            Thread.sleep(WAIT_TIME); // Wait for the mailbox to load
            ButtonsHome.pressReceiveAll(); // Receive all mail
            Thread.sleep(WAIT_TIME); // Wait for the action to complete
            ButtonsHome.pressAnywhere(); // Dismiss the mailbox
            ButtonsHome.closeMailBox(); // Close the mailbox

            Screenshot.screenshotGameState(); // Take another screenshot of the game state
            System.out.println("MailBox received...");
            return;
        }
        System.out.println("MailBox receiving failed...");
    }

    /**
     * Swipes the screen to navigate to the shop.
     *
     * @throws IOException If there is an error executing the swipe command.
     * @throws InterruptedException If the thread is interrupted during sleep.
     */
    public static void swipeToShop() throws IOException, InterruptedException {
        try {
            // Execute the swipe command using ADB (Android Debug Bridge)
            Process process = Runtime.getRuntime().exec("adb shell input touchscreen swipe 260 850 260 0 7500");
            Thread.sleep(7500); // Wait for the swipe to complete
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e); // Handle exceptions appropriately
        }
        Screenshot.screenshotGameState(); // Take a screenshot of the game state
    }

    /**
     * Swipes the screen to navigate to the top of the shop.
     *
     * @throws IOException If there is an error executing the swipe command.
     */
    public static void swipeToTop() throws IOException {
        try {
            // Execute the swipe command using ADB (Android Debug Bridge)
            Process process = Runtime.getRuntime().exec("adb shell input touchscreen swipe 260 300 260 850");
            Thread.sleep(5000); // Wait for the swipe to complete
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e); // Handle exceptions appropriately
        }
        Screenshot.screenshotGameState(); // Take a screenshot of the game state
    }

    /**
     * Swipes the screen to navigate to the keys section of the shop.
     *
     * @throws IOException If there is an error executing the swipe command.
     * @throws InterruptedException If the thread is interrupted during sleep.
     */
    public static void swipeToKeys() throws IOException, InterruptedException {
        swipeToShop(); // Navigate to the shop
        // Execute the swipe command to navigate to the keys section
        Process process = Runtime.getRuntime().exec("adb shell input touchscreen swipe 260 850 260 45 7500");
        Screenshot.screenshotGameState(); // Take a screenshot of the game state
    }

    /**
     * Checks if a specific unit is pressed by verifying the color of a pixel in the game state.
     *
     * @return true if the unit is pressed; false otherwise.
     * @throws IOException If there is an error reading the pixel color.
     */
    private static boolean isUnitPressed() throws IOException {
        Color expectedColor = new Color(98, 115, 143); // Expected color of the unit
        Color currentColor = PixelColorChecker.getPixelColor("Lucky-Defense-Bot-V2/Resources/GameState.png", new Coordinates(490, 104));
        return PixelColorChecker.isMatchingColor(expectedColor, currentColor, 5); // Check for color match
    }

    /**
     * Checks if a specific artifact is pressed by verifying the color of a pixel in the game state.
     *
     * @return true if the artifact is pressed; false otherwise.
     * @throws IOException If there is an error reading the pixel color.
     */
    private static boolean isArtifactPressed() throws IOException {
        Color expectedColor = new Color(98, 115, 143); // Expected color of the artifact
        Color currentColor = PixelColorChecker.getPixelColor("Lucky-Defense-Bot-V2/Resources/GameState.png", new Coordinates(475, 242));
        return PixelColorChecker.isMatchingColor(expectedColor, currentColor, 5); // Check for color match
    }

    /* TODO: Consider moving this method to a different class */
    /**
     * Loads the game state image from the specified file.
     *
     * @return The BufferedImage representation of the game state.
     * @throws IOException If there is an error loading the image file.
     */
    private static BufferedImage loadGameStateImage() throws IOException {
        try {
            return ImageIO.read(new File("Resources/GameState.png")); // Load the game state image
        } catch (IOException e) {
            throw new RuntimeException("Failed to load GameState.png", e); // Handle exceptions appropriately
        }
    }

    /**
     * Finds the top-left position of the refresh button in the game state by trying various image paths.
     *
     * @return The y-coordinate of the top-left position of the refresh button, or -1 if not found.
     * @throws IOException If there is an error reading the images or processing the results.
     */
    public static int findTopLeft() throws IOException {
        // Array of possible refresh button image paths in order of likelihood (or any order you prefer)
        String[] refreshButtonPaths = {
                "Resources/RefreshButtons/3xRefresh.png",
                "Resources/RefreshButtons/2xRefresh.png",
                "Resources/RefreshButtons/1xRefresh.png",
                "Resources/RefreshButtons/0xRefresh.png"
        };

        // Iterate through the array of refresh button image paths
        for (String refreshButtonPath : refreshButtonPaths) {
            try {
                String gameStatePath = "Resources/GameState.png"; // Provide the correct path

                // Find the coordinates of the refresh button in the game state image
                Coordinates coords = CompareImage.findRefreshButtonInGameState(refreshButtonPath, gameStatePath);
                if (coords.getX() != -1 && coords.getY() != -1) {
                    System.out.println("Found refresh button: " + refreshButtonPath); // Log the found refresh button
                    return coords.getY(); // Return the y-coordinate of the found refresh button
                }
            } catch (IOException e) {
                System.err.println("Error reading refresh button: " + refreshButtonPath);
                e.printStackTrace(); // Print stack trace for debugging
            }
        }

        System.out.println("No refresh button found."); // Log when no refresh button is found
        return -1; // Return -1 if no refresh button is found
    }

    /**
     * Temporary cropImage function for shop use
     * TODO : MOVE TO DIFFERENT CLASS
     * @param image
     * @param topLeft
     * @param bottomRight
     * @return
     */
    private static BufferedImage cropImage(BufferedImage image, Coordinates topLeft, Coordinates bottomRight) {
        int x = topLeft.getX();
        int y = topLeft.getY();
        int width = bottomRight.getX() - x;
        int height = bottomRight.getY() - y;
        return image.getSubimage(x, y, width, height);
    }

    /**
     * Process shopItems and see if it should be purchased, if purchaseable then buy
     * @param croppedImage
     * @param areaIndex
     * @throws IOException
     * @throws InterruptedException
     */
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

    /**
     * Helper function to buy items
     * @param areaIndex
     * @throws InterruptedException
     * @throws IOException
     */
    private static void buyItem(int areaIndex) throws InterruptedException, IOException {
        ButtonsHome.pressItem(areaIndex);   // Press the shop item
        Thread.sleep(1000);                  // Wait for a moment
        ButtonsHome.purchaseItem();         // Purchase the item
        Thread.sleep(1000);
        ButtonsHome.pressAnywhere();        // Press anywhere to dismiss the dialog
    }

    /**
     * Helper function to check if an item should be purchased
     * @throws IOException
     * @throws InterruptedException
     */
    private static void processShopItems() throws IOException, InterruptedException {
        BufferedImage gameStateImage = loadGameStateImage();
        for (int i = 0; i < topLeftCoordinates.length; i++) {
            BufferedImage croppedImage = cropImage(gameStateImage, topLeftCoordinates[i], bottomRightCoordinates[i]);
            checkAndBuyItem(croppedImage, i);
        }
    }

    /**
     * MAIN FUNCTION for autoshop
     * @throws IOException
     * @throws InterruptedException
     */
    public static void autoShop() throws IOException, InterruptedException {
        // You can press reset a maximum of 3 times, so loop through it 3 times
        for (int attempt = 0; attempt < 3; attempt++) {
            Screenshot.screenshotGameState();
            processShopItems();
            ButtonsHome.pressReset();
        }
    }

    /**
     * Main method for testing the Shop class functionality.
     * @param args Command line arguments (not used).
     * @throws IOException If there is an error during execution.
     */
    public static void main(String[] args) throws IOException, InterruptedException {

    }
}
