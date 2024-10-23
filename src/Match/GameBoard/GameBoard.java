package Match.GameBoard;

import Basic.Coordinates;
import Basic.Press;
import Basic.Screenshot;
import Match.MythicBuilder;
import Match.Units.ProcessUnit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static Match.Units.ProcessUnit.gameboard;

public class GameBoard {

    private Square[][] gameBoard;

    private final static Coordinates[][] topLeftCoordinates = {
            { new Coordinates(85, 460), new Coordinates(150, 460), new Coordinates(215, 460), new Coordinates(280, 460), new Coordinates(345, 460), new Coordinates(410, 460) },
            { new Coordinates(85, 513), new Coordinates(150, 513), new Coordinates(215, 513), new Coordinates(280, 513), new Coordinates(345, 513), new Coordinates(410, 513) },
            { new Coordinates(85, 566), new Coordinates(150, 566), new Coordinates(215, 566), new Coordinates(280, 566), new Coordinates(345, 566), new Coordinates(410, 566) }
    };

    private final static Coordinates[][] bottomRightCoordinates = {
            { new Coordinates(135, 495), new Coordinates(200, 495), new Coordinates(265, 495), new Coordinates(330, 495), new Coordinates(395, 495), new Coordinates(460, 495) },
            { new Coordinates(135, 548), new Coordinates(200, 548), new Coordinates(265, 548), new Coordinates(330, 548), new Coordinates(395, 548), new Coordinates(460, 548) },
            { new Coordinates(135, 601), new Coordinates(200, 601), new Coordinates(265, 601), new Coordinates(330, 601), new Coordinates(395, 601), new Coordinates(460, 601) }
    };

    /**
     * Construct gameBoard
     */
    public GameBoard() {
        initializeGameBoard();
    }

    /**
     * Construct the 3x6 squares
     */
    private void initializeGameBoard() {
        System.out.println("Initializing game board...");
        gameBoard = new Square[3][6];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 6; j++) {
                gameBoard[i][j] = new Square();
                System.out.println("Initialized square at [" + i + "][" + j + "]");
            }
        }
        System.out.println("Game board initialization complete.");
    }

    /**
     * Keep the values to 3x6 Squares
     * @throws IOException
     * @throws InterruptedException
     */
    public void updateBoard() throws IOException, InterruptedException {

        if (gameBoard == null) {
            System.out.println("Error: gameBoard is null. Reinitializing...");
            initializeGameBoard();
        }

        System.out.println("Updating board...");
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 6; j++) {
                String action = "Checking square " + i + ", " + j;
                gameBoard[i][j].updateSquare(topLeftCoordinates[i][j], bottomRightCoordinates[i][j], action);
            }
        }
        System.out.println("Board updated...");
    }

    /**
     * Save board state to JSON
     */
    public void saveBoardState() {
        if (gameBoard == null) {
            System.out.println("Error: gameBoard is null. Cannot save board state.");
            return;
        }

        System.out.println("Saving board state...");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);

        System.out.println("JSON content to be saved:");
        System.out.println(json);

        String filePath = "Resources/gameBoardState.json";
        File file = new File(filePath);

        try {
            // Ensure the directory exists
            file.getParentFile().mkdirs();

            // Write the JSON to the file
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(json);
                System.out.println("Board state saved successfully to: " + file.getAbsolutePath());
            }

            // Verify the file was written
            if (file.exists() && file.length() > 0) {
                System.out.println("File exists and is not empty.");
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                System.out.println("File content:");
                System.out.println(content);
            } else {
                System.out.println("Error: File does not exist or is empty after writing.");
            }
        } catch (IOException e) {
            System.out.println("Error saving board state: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Move units
     * @param i1
     * @param j1
     * @param i2
     * @param j2
     * @throws IOException
     * @throws InterruptedException
     */
    public static void moveUnit(int i1, int j1, int i2, int j2) throws IOException, InterruptedException {
        Coordinates fromRandomCoordinates = Coordinates.makeRandomCoordinate(topLeftCoordinates[i1][j1], bottomRightCoordinates[i1][j1]);
        Coordinates toRandomCoordinates = Coordinates.makeRandomCoordinate(topLeftCoordinates[i2][j2], bottomRightCoordinates[i2][j2]);
        Thread.sleep(2000);
        Process process = Runtime.getRuntime()
                .exec("adb shell input draganddrop " + fromRandomCoordinates.toString() + " " + toRandomCoordinates.toString());
        Thread.sleep(3000);
    }

    /**
     * GETTERS
     * @return
     */
    public static Coordinates[][] getTopLeftCoordinates() { return topLeftCoordinates; }
    public static Coordinates[][] getBottomRightCoordinates() { return bottomRightCoordinates; }
    public Square[][] getGameBoard() {
        return gameBoard;
    }


    /**
     * Sell units
     * @param i
     * @param j
     * @throws IOException
     * @throws InterruptedException
     */
    public static void sellUnit(int i, int j) throws IOException, InterruptedException {
        // Click the unit at the specified game board coordinates
        Coordinates fromRandomCoordinates = Coordinates.makeRandomCoordinate(topLeftCoordinates[i][j], bottomRightCoordinates[i][j]);

        // Top left coordinates of sell button 3x6 array siya like the grid in game
        Coordinates[][] sellTopLeft = {
                { new Coordinates(95, 404), new Coordinates(160, 404), new Coordinates(224, 404), new Coordinates(286, 404), new Coordinates(348, 404), new Coordinates(412, 404) }, // Row A
                { new Coordinates(98, 451), new Coordinates(161, 451), new Coordinates(225, 451), new Coordinates(285, 451), new Coordinates(345, 451), new Coordinates(410, 451) }, // Row B
                { new Coordinates(95, 503), new Coordinates(161, 503), new Coordinates(225, 503), new Coordinates(285, 503), new Coordinates(345, 503), new Coordinates(410, 503) }  // Row C
        };
        //Bottom right coordinates of sell button 3x6 array siya like the grid in game
        Coordinates[][] sellBottomRight = {
                { new Coordinates(131, 434), new Coordinates(191, 434), new Coordinates(253, 434), new Coordinates(318, 434), new Coordinates(380, 434), new Coordinates(442, 434) }, // Row A
                { new Coordinates(128, 483), new Coordinates(192, 483), new Coordinates(254, 483), new Coordinates(318, 483), new Coordinates(380, 483), new Coordinates(442, 483) }, // Row B
                { new Coordinates(130, 533), new Coordinates(192, 533), new Coordinates(254, 533), new Coordinates(318, 533), new Coordinates(380, 533), new Coordinates(442, 533) }  // Row C
        };

        // Generate a random cell within the given top left and bottom right coordinates
        Coordinates sellRandomCoordinates = Coordinates.makeRandomCoordinate(sellTopLeft[i][j], sellBottomRight[i][j]);

        // click unit
        System.out.println("Clicking the unit at: " + fromRandomCoordinates);
        Process process1 = Runtime.getRuntime().exec("adb shell input tap " + fromRandomCoordinates.getX() + " " + fromRandomCoordinates.getY());
        Thread.sleep(500);

        // click sell
        System.out.println("Selling the unit at: " + sellRandomCoordinates);
        Process process2 = Runtime.getRuntime().exec("adb shell input tap " + sellRandomCoordinates.getX() + " " + sellRandomCoordinates.getY());
        Thread.sleep(500);
    }

    /**
     * Merge units
     * @param i
     * @param j
     * @throws IOException
     * @throws InterruptedException
     */
    public static void mergeUnit(int i, int j) throws IOException, InterruptedException {
        //Location sa specific unit e merge sa gameboard
        Coordinates fromRandomCoordinates = Coordinates.makeRandomCoordinate(topLeftCoordinates[i][j], bottomRightCoordinates[i][j]);

        // Top left coordinates sa merge button per unit 3x6 array siya like the grid in game
        Coordinates[][] mergeTopLeft = {
                { new Coordinates(75, 531), new Coordinates(138, 530), new Coordinates(201, 530), new Coordinates(264, 530), new Coordinates(325, 530), new Coordinates(388, 530) }, // Row A
                { new Coordinates(76, 582), new Coordinates(140, 582), new Coordinates(202, 582), new Coordinates(264, 582), new Coordinates(325, 582), new Coordinates(388, 582) }, // Row B
                { new Coordinates(78, 633), new Coordinates(138, 633), new Coordinates(202, 633), new Coordinates(264, 633), new Coordinates(325, 633), new Coordinates(388, 633) }  // Row C
        };
        // BottomRight coordinates sa merge button per unit 3x6 array siya like the grid in game
        Coordinates[][] mergeBottomRight = {
                { new Coordinates(152, 567), new Coordinates(216, 566), new Coordinates(278, 567), new Coordinates(339, 567), new Coordinates(400, 567), new Coordinates(463, 567) }, // Row A
                { new Coordinates(152, 614), new Coordinates(216, 614), new Coordinates(278, 614), new Coordinates(339, 614), new Coordinates(400, 614), new Coordinates(463, 614) }, // Row B
                { new Coordinates(152, 664), new Coordinates(216, 664), new Coordinates(278, 664), new Coordinates(339, 664), new Coordinates(400, 664), new Coordinates(463, 664) }  // Row C
        };

        // Generate a random cell within the given top left and bottom right coordinates
        Coordinates mergeRandomCoordinates = Coordinates.makeRandomCoordinate(mergeTopLeft[i][j], mergeBottomRight[i][j]);

        // click unit
        System.out.println("Clicking the unit to merge at: " + fromRandomCoordinates);
        Process process1 = Runtime.getRuntime().exec("adb shell input tap " + fromRandomCoordinates.getX() + " " + fromRandomCoordinates.getY());
        Thread.sleep(500);

        //Click merge
        System.out.println("Merging the unit at: " + mergeRandomCoordinates);
        Process process2 = Runtime.getRuntime().exec("adb shell input tap " + mergeRandomCoordinates.getX() + " " + mergeRandomCoordinates.getY());
        Thread.sleep(500);
    }

    /**
     * Upgrade units for mythics like Batman, Tar, Lancelot, etc.
     * @param i
     * @param j
     * @throws IOException
     * @throws InterruptedException
     */
    public static void upgradeUnit(int i, int j) throws IOException, InterruptedException {
        //Location sa specific unit e merge sa gameboard
        Coordinates fromRandomCoordinates = Coordinates.makeRandomCoordinate(topLeftCoordinates[i][j], bottomRightCoordinates[i][j]);

        // Top left coordinates sa merge button per unit 3x6 array siya like the grid in game
        Coordinates[][] mergeTopLeft = {
                { new Coordinates(75, 531), new Coordinates(138, 530), new Coordinates(201, 530), new Coordinates(264, 530), new Coordinates(325, 530), new Coordinates(388, 530) }, // Row A
                { new Coordinates(76, 582), new Coordinates(140, 582), new Coordinates(202, 582), new Coordinates(264, 582), new Coordinates(325, 582), new Coordinates(388, 582) }, // Row B
                { new Coordinates(78, 633), new Coordinates(138, 633), new Coordinates(202, 633), new Coordinates(264, 633), new Coordinates(325, 633), new Coordinates(388, 633) }  // Row C
        };
        // BottomRight coordinates sa merge button per unit 3x6 array siya like the grid in game
        Coordinates[][] mergeBottomRight = {
                { new Coordinates(152, 567), new Coordinates(216, 566), new Coordinates(278, 567), new Coordinates(339, 567), new Coordinates(400, 567), new Coordinates(463, 567) }, // Row A
                { new Coordinates(152, 614), new Coordinates(216, 614), new Coordinates(278, 614), new Coordinates(339, 614), new Coordinates(400, 614), new Coordinates(463, 614) }, // Row B
                { new Coordinates(152, 664), new Coordinates(216, 664), new Coordinates(278, 664), new Coordinates(339, 664), new Coordinates(400, 664), new Coordinates(463, 664) }  // Row C
        };

        // Generate a random cell within the given top left and bottom right coordinates
        Coordinates mergeRandomCoordinates = Coordinates.makeRandomCoordinate(mergeTopLeft[i][j], mergeBottomRight[i][j]);

        // click unit
        System.out.println("Clicking the unit to merge at: " + fromRandomCoordinates);
        Process process1 = Runtime.getRuntime().exec("adb shell input tap " + fromRandomCoordinates.getX() + " " + fromRandomCoordinates.getY());
        Thread.sleep(500);

        //Click merge
        System.out.println("Merging the unit at: " + mergeRandomCoordinates);
        Process process2 = Runtime.getRuntime().exec("adb shell input tap " + mergeRandomCoordinates.getX() + " " + mergeRandomCoordinates.getY());
        Thread.sleep(500);
    }

    public Square getSquare(int row, int column) {
        // Check for out-of-bounds access
        if (row >= 0 && row < gameBoard.length && column >= 0 && column < gameBoard[0].length) {
            return gameBoard[row][column];
        } else {
            return null; // Return null if the indices are out of bounds
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        GameBoard gameBoard1 = new GameBoard();
        gameBoard1.updateBoard();
        gameBoard1.saveBoardState();

//        MythicBuilder.canBuild("Batman");
        boolean success = ProcessUnit.DetectUnitPlusProcess();

        // Check the result and print whether processing was successful
        if (success) {
            System.out.println("Unit processing completed successfully.");
        } else {
            System.out.println("Unit processing failed.");
        }
    }
}

