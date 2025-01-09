package Match.GameBoard;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import javax.imageio.ImageIO;

import Basic.CompareImage;
import Basic.Coordinates;
import Basic.Screenshot;
import Match.MatchBasic;
import Match.Units.Unit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static Logger.Logger.log;
import static Match.Units.ProcessUnit.sellUnitMultipleTimes;


public class GameBoard {

    public static Square[][] gameBoard;

    private final static Coordinates[][] topLeftCoordinates = {
            { new Coordinates(85, 460), new Coordinates(150, 460), new Coordinates(215, 460), new Coordinates(280, 460),
                    new Coordinates(345, 460), new Coordinates(410, 460) },
            { new Coordinates(85, 513), new Coordinates(150, 513), new Coordinates(215, 513), new Coordinates(280, 513),
                    new Coordinates(345, 513), new Coordinates(410, 513) },
            { new Coordinates(85, 566), new Coordinates(150, 566), new Coordinates(215, 566), new Coordinates(280, 566),
                    new Coordinates(345, 566), new Coordinates(410, 566) }
    };

    private final static Coordinates[][] bottomRightCoordinates = {
            { new Coordinates(135, 495), new Coordinates(200, 495), new Coordinates(265, 495),
                    new Coordinates(330, 495), new Coordinates(395, 495), new Coordinates(460, 495) },
            { new Coordinates(135, 548), new Coordinates(200, 548), new Coordinates(265, 548),
                    new Coordinates(330, 548), new Coordinates(395, 548), new Coordinates(460, 548) },
            { new Coordinates(135, 601), new Coordinates(200, 601), new Coordinates(265, 601),
                    new Coordinates(330, 601), new Coordinates(395, 601), new Coordinates(460, 601) }
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
        log("Initializing game board...");
        gameBoard = new Square[3][6];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 6; j++) {
                gameBoard[i][j] = new Square();
            }
        }
        log("Game board initialization complete.");
    }

    public static Square getSquare(int i, int j) {
        return gameBoard[i][j];
    }

    /**
     * Keep the values to 3x6 Squares
     */
    public void updateBoard(int i, int j) throws IOException, InterruptedException {
        if (gameBoard == null) {
            log("Board is null.");
        }

        String action = "Checking square " + i + ", " + j;
        gameBoard[i][j].updateSquare(topLeftCoordinates[i][j], bottomRightCoordinates[i][j], action);
        log("Board " + i + " " + j + " updated.");
    }

    /**
     * Save board state to JSON
     */
    public void saveBoardState() {
        if (gameBoard == null) {
            log("[Error] gameBoard is null. Cannot save board state.");
            return;
        }

        log("Saving board state...");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Build a custom structure that better represents the state
        List<List<Map<String, Object>>> boardState = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            List<Map<String, Object>> row = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                Unit unit = gameBoard[i][j].getUnit();
                if (unit != null && unit.getName() != null && !unit.getName().isEmpty()) {
                    Map<String, Object> unitData = new HashMap<>();
                    unitData.put("name", unit.getName());
                    unitData.put("quantity", unit.getQuantity());
                    row.add(unitData);
                } else {
                    row.add(null); // Empty square represented as null
                }
            }
            boardState.add(row);
        }

        // Convert the custom structure to JSON
        String json = gson.toJson(Collections.singletonMap("gameBoard", boardState));

//        log("JSON content to be saved:");
//        log(json);

        String filePath = "Resources/gameBoardState.json";
        File file = new File(filePath);

        try {
            // Ensure the directory exists
            file.getParentFile().mkdirs();

            // Write the JSON to the file
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(json);
                log("Board state saved successfully to: " + file.getAbsolutePath() + ".");
            }

            // Verify the file was written
            if (file.exists() && file.length() > 0) {
                log("File exists and is not empty.");
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
//                log("File content:");
//                log(content);
            } else {
                log("[Error] File does not exist or is empty after writing.");
            }
        } catch (IOException e) {
            log("[Error] cannot save board state: " + e.getMessage() + ".");
            e.printStackTrace();
        }
    }

    /**
     * Move units
     */
    public GameBoard moveUnit(GameBoard gameBoard, int i1, int j1, int i2, int j2)
            throws IOException, InterruptedException {
        Coordinates fromRandomCoordinates = Coordinates.makeRandomCoordinate(topLeftCoordinates[i1][j1],
                bottomRightCoordinates[i1][j1]);
        Coordinates toRandomCoordinates = Coordinates.makeRandomCoordinate(topLeftCoordinates[i2][j2],
                bottomRightCoordinates[i2][j2]);
        Thread.sleep(2000);
        Process process = Runtime.getRuntime()
                .exec("adb shell input draganddrop " + fromRandomCoordinates.toString() + " "
                        + toRandomCoordinates.toString());
        Thread.sleep(3000);

        gameBoard = gameBoard.updateGameBoard(gameBoard, i1, j1, i2, j2);

        return gameBoard;
    }

    /**
     * Sell units
     */
    public static GameBoard sellUnit(GameBoard gameboard, int i, int j) throws IOException, InterruptedException {
        // Click the unit at the specified game board coordinates
        Coordinates fromRandomCoordinates = Coordinates.makeRandomCoordinate(topLeftCoordinates[i][j],
                bottomRightCoordinates[i][j]);

        // Top left coordinates of sell button 3x6 array siya like the grid in game
        Coordinates[][] sellTopLeft = {
                { new Coordinates(95, 404), new Coordinates(160, 404), new Coordinates(224, 404),
                        new Coordinates(286, 404), new Coordinates(348, 404), new Coordinates(412, 404) }, // Row A
                { new Coordinates(98, 451), new Coordinates(161, 451), new Coordinates(225, 451),
                        new Coordinates(285, 451), new Coordinates(345, 451), new Coordinates(410, 451) }, // Row B
                { new Coordinates(95, 503), new Coordinates(161, 503), new Coordinates(225, 503),
                        new Coordinates(285, 503), new Coordinates(345, 503), new Coordinates(410, 503) } // Row C
        };
        // Bottom right coordinates of sell button 3x6 array siya like the grid in game
        Coordinates[][] sellBottomRight = {
                { new Coordinates(131, 434), new Coordinates(191, 434), new Coordinates(253, 434),
                        new Coordinates(318, 434), new Coordinates(380, 434), new Coordinates(442, 434) }, // Row A
                { new Coordinates(128, 483), new Coordinates(192, 483), new Coordinates(254, 483),
                        new Coordinates(318, 483), new Coordinates(380, 483), new Coordinates(442, 483) }, // Row B
                { new Coordinates(130, 533), new Coordinates(192, 533), new Coordinates(254, 533),
                        new Coordinates(318, 533), new Coordinates(380, 533), new Coordinates(442, 533) } // Row C
        };

        // Generate a random cell within the given top left and bottom right coordinates
        Coordinates sellRandomCoordinates = Coordinates.makeRandomCoordinate(sellTopLeft[i][j], sellBottomRight[i][j]);

        // click sell
        log("Selling the unit at: " + sellRandomCoordinates + ".");
        Thread.sleep(500);
        Process process2 = Runtime.getRuntime()
                .exec("adb shell input tap " + sellRandomCoordinates.getX() + " " + sellRandomCoordinates.getY());

        gameboard = gameboard.updateGameBoard(gameboard, i, j, i, j);

        return gameboard;
    }

    /**
     * Merge units
     */
    public static void mergeUnit(int i, int j) throws IOException, InterruptedException {
        log("Merging units at (" + i + ", " + j + ").");
        // Location sa specific unit e merge sa gameboard
        Coordinates fromRandomCoordinates = Coordinates.makeRandomCoordinate(topLeftCoordinates[i][j],
                bottomRightCoordinates[i][j]);

        // Top left coordinates sa merge button per unit 3x6 array siya like the grid in
        // game
        Coordinates[][] mergeTopLeft = {
                { new Coordinates(75, 531), new Coordinates(138, 530), new Coordinates(201, 530),
                        new Coordinates(264, 530), new Coordinates(325, 530), new Coordinates(388, 530) }, // Row A
                { new Coordinates(76, 582), new Coordinates(140, 582), new Coordinates(202, 582),
                        new Coordinates(264, 582), new Coordinates(325, 582), new Coordinates(388, 582) }, // Row B
                { new Coordinates(78, 633), new Coordinates(138, 633), new Coordinates(202, 633),
                        new Coordinates(264, 633), new Coordinates(325, 633), new Coordinates(388, 633) } // Row C
        };
        // BottomRight coordinates sa merge button per unit 3x6 array siya like the grid
        // in game
        Coordinates[][] mergeBottomRight = {
                { new Coordinates(152, 567), new Coordinates(216, 566), new Coordinates(278, 567),
                        new Coordinates(339, 567), new Coordinates(400, 567), new Coordinates(463, 567) }, // Row A
                { new Coordinates(152, 614), new Coordinates(216, 614), new Coordinates(278, 614),
                        new Coordinates(339, 614), new Coordinates(400, 614), new Coordinates(463, 614) }, // Row B
                { new Coordinates(152, 664), new Coordinates(216, 664), new Coordinates(278, 664),
                        new Coordinates(339, 664), new Coordinates(400, 664), new Coordinates(463, 664) } // Row C
        };

        // Generate a random cell within the given top left and bottom right coordinates
        Coordinates mergeRandomCoordinates = Coordinates.makeRandomCoordinate(mergeTopLeft[i][j],
                mergeBottomRight[i][j]);

        // Click merge
        Thread.sleep(500);
        Process process2 = Runtime.getRuntime()
                .exec("adb shell input tap " + mergeRandomCoordinates.getX() + " " + mergeRandomCoordinates.getY());
    }

    /**
     * Upgrade units for mythics like Batman, Tar, Lancelot, etc.
     */
    public static void upgradeUnit(int i, int j) throws IOException, InterruptedException {
        log("Upgrading unit at (" + i + ", " + j + ").");
        // Location sa specific unit e merge sa gameboard
        Coordinates fromRandomCoordinates = Coordinates.makeRandomCoordinate(topLeftCoordinates[i][j],
                bottomRightCoordinates[i][j]);

        // Top left coordinates sa merge button per unit 3x6 array siya like the grid in
        // game
        Coordinates[][] mergeTopLeft = {
                { new Coordinates(75, 531), new Coordinates(138, 530), new Coordinates(201, 530),
                        new Coordinates(264, 530), new Coordinates(325, 530), new Coordinates(388, 530) }, // Row A
                { new Coordinates(76, 582), new Coordinates(140, 582), new Coordinates(202, 582),
                        new Coordinates(264, 582), new Coordinates(325, 582), new Coordinates(388, 582) }, // Row B
                { new Coordinates(78, 633), new Coordinates(138, 633), new Coordinates(202, 633),
                        new Coordinates(264, 633), new Coordinates(325, 633), new Coordinates(388, 633) } // Row C
        };
        // BottomRight coordinates sa merge button per unit 3x6 array siya like the grid
        // in game
        Coordinates[][] mergeBottomRight = {
                { new Coordinates(152, 567), new Coordinates(216, 566), new Coordinates(278, 567),
                        new Coordinates(339, 567), new Coordinates(400, 567), new Coordinates(463, 567) }, // Row A
                { new Coordinates(152, 614), new Coordinates(216, 614), new Coordinates(278, 614),
                        new Coordinates(339, 614), new Coordinates(400, 614), new Coordinates(463, 614) }, // Row B
                { new Coordinates(152, 664), new Coordinates(216, 664), new Coordinates(278, 664),
                        new Coordinates(339, 664), new Coordinates(400, 664), new Coordinates(463, 664) } // Row C
        };

        // Generate a random cell within the given top left and bottom right coordinates
        Coordinates mergeRandomCoordinates = Coordinates.makeRandomCoordinate(mergeTopLeft[i][j],
                mergeBottomRight[i][j]);

        // click unit
        Process process1 = Runtime.getRuntime()
                .exec("adb shell input tap " + fromRandomCoordinates.getX() + " " + fromRandomCoordinates.getY());
        Thread.sleep(500); // Click merge
        Process process2 = Runtime.getRuntime()
                .exec("adb shell input tap " + mergeRandomCoordinates.getX() + " " + mergeRandomCoordinates.getY());
        Thread.sleep(500);
    }

    public GameBoard updateGameBoard(GameBoard gameBoard, int i1, int j1, int i2, int j2) {
        log("Updating game board...");

        // If the same square, clear it (for selling a unit)
        if (i1 == i2 && j1 == j2) {
            gameBoard.setSquare(new Square(), i1, j1);
            return gameBoard;
        }

        // Swap squares
        Square sourceSquare = getSquare(i1, j1);
        Square targetSquare = getSquare(i2, j2);

        gameBoard.setSquare(targetSquare, i1, j1);
        gameBoard.setSquare(sourceSquare, i2, j2);

        return gameBoard;
    }

    /**
     * compare square coordiantes in defaultGameBoard.png and gameState.png.
     * If gameState.png's square is not the same with default gameBoard, it should add that to the HashMap.
     * If it is the same, skip.
     * Process those squares with validateRemainingSquares and if the specific square has the correct unit, remove it from the HashMap
     * If it does not have the correct unit then keep in the HashMap
     */
    public static List<int[]> getNonEmptySquares() throws IOException {
        log("Performing checks for valid squares...");
        Screenshot.screenshotGameState();

        if (!MatchBasic.isIngame()) {
            log("Currently not in game.");
            return null;
        }

        log("Currently in game.");

        List<int[]> nonEmptySquares = new ArrayList<>();
        BufferedImage baseState = ImageIO.read(new File("Resources/defaultGameBoard.png"));
        BufferedImage gameState = ImageIO.read(new File("Resources/GameState.png"));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 6; j++) {
                int topLeftX = topLeftCoordinates[i][j].getX();
                int topLeftY = topLeftCoordinates[i][j].getY();
                int bottomRightX = bottomRightCoordinates[i][j].getX();
                int bottomRightY = bottomRightCoordinates[i][j].getY();

                if (!CompareImage.isMatchingRegion(baseState, gameState, topLeftX, topLeftY, bottomRightX, bottomRightY)) {
                    nonEmptySquares.add(new int[]{i, j});
                }
            }
        }

        validateRemainingSquares(nonEmptySquares);
        return nonEmptySquares;
    }

    /**
     * Check the nonEmptySquares and remove squares that already have the correct units in them.
     */
    private static void validateRemainingSquares(List<int[]> nonEmptySquares) {
        if (nonEmptySquares == null || nonEmptySquares.isEmpty()) {
            log("No non-empty squares to validate.");
            return;
        }

        Iterator<int[]> iterator = nonEmptySquares.iterator();
        while (iterator.hasNext()) {
            int[] square = iterator.next();
            int i = square[0];
            int j = square[1];

            String expectedUnit = getExpectedUnit(i, j);
            if (expectedUnit == null) {
                log("No expected unit for square: (" + i + ", " + j + ")");
                continue;
            }

            if (getSquare(i, j).getUnit().getName().equals(expectedUnit)) {
                iterator.remove();
            }
        }
    }

    private static String getExpectedUnit(int i, int j) {
        if (i == 0 && j == 0) return "Frog Prince";
        if (i == 1 && j == 0) return "Bandit";
        if (i == 2 && j == 0) return "Bandit";
        if (i == 1 && j == 1) return "Dragon";
        if (i == 1 && j == 2) return "Dragon";
        if (i == 0 && j == 2) return "Electro Robot";
        if (i == 0 && j == 4) return "Wolf Warrior";
        if (i == 0 && j == 5) return "Barbarian";
        if (i == 1 && j == 4) return "Eagle General";
        if (i == 2 && j == 4) return "Tree";
        if (i == 1 && j == 5) return "Thrower";
        if (i == 2 && j == 5) return "Water Elemental";

        return null; // No specific unit expected
    }

    public boolean isBoardComplete() {
        if(!getSquare(0, 0).getUnit().getName().equals("Frog Prince"))
            return false;
        if(!getSquare(0, 1).getUnit().getName().equals("Frog Prince"))
            return false;
        if(!getSquare(1, 1).getUnit().getName().equals("Dragon"))
            return false;
        if(!getSquare(1, 2).getUnit().getName().equals("Dragon"))
            return false;
        if(!getSquare(0, 2).getUnit().getName().equals("Electro Robot"))
            return false;

        return true;
    }

    public boolean shouldSummon() throws IOException, InterruptedException {
        // Get the non-empty squares
        List<int[]> nonEmptySquares = getNonEmptySquares();
        if (nonEmptySquares == null || nonEmptySquares.isEmpty()) {
            return true; // No units on the board, summon is required
        }

        // Check if the required Mythics are built
        boolean hasFrogPrince1 = false, hasFrogPrince2 = false;
        boolean hasDragon1 = false, hasDragon2 = false;
        boolean hasElectroRobot = false;

        for (int[] square : nonEmptySquares) {
            int i = square[0];
            int j = square[1];
            String unitName = getSquare(i, j).getUnit().getName();

            if (i == 0 && j == 0 && unitName.equals("Frog Prince")) hasFrogPrince1 = true;
            if (i == 1 && j == 0 && unitName.equals("Frog Prince")) hasFrogPrince2 = true;
            if (i == 1 && j == 1 && unitName.equals("Dragon")) hasDragon1 = true;
            if (i == 1 && j == 2 && unitName.equals("Dragon")) hasDragon2 = true;
            if (i == 0 && j == 2 && unitName.equals("Electro Robot")) hasElectroRobot = true;
        }

        if (hasFrogPrince1 && hasFrogPrince2 && hasDragon1 && hasDragon2 && hasElectroRobot) {
            return false; // All required Mythics are built
        }

        // Count the required units from the non-empty squares
        int wolfWarriors = 0, trees = 0, barbarians = 0, throwers = 0;
        int eagleGenerals = 0, waterElementals = 0;

        for (int[] square : nonEmptySquares) {
            int i = square[0];
            int j = square[1];
            String unitName = getSquare(i, j).getUnit().getName();

            switch (unitName) {
                case "Wolf Warrior":
                    wolfWarriors++;
                    break;
                case "Tree":
                    trees++;
                    break;
                case "Barbarian":
                    barbarians++;
                    break;
                case "Thrower":
                    throwers++;
                    break;
                case "Eagle General":
                    eagleGenerals++;
                    break;
                case "Water Elemental":
                    waterElementals++;
                    break;
            }
        }

        // Check if there are enough ingredients
        if (wolfWarriors >= 2 && trees >= 2 && barbarians >= 2 && throwers >= 2 &&
                eagleGenerals >= 4 && waterElementals >= 2) {
            return false; // Enough ingredients for Frog Princes and Dragons
        }

        return true; // Not enough ingredients or Mythics not built
    }


    public static int getTotalUnits(String unitName) {
        int totalQuantity = 0;

        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                Unit unit = gameBoard[i][j].getUnit();
                if (unit != null && unit.getName().equals(unitName)) {
                    totalQuantity += unit.getQuantity();
                }
            }
        }

        return totalQuantity;
    }

    /**
     * Return square;
     */
    public void setSquare(Square square, int row, int column) {
        gameBoard[row][column] = square;
    }

    /**
     * GETTERS
     */
    public static Coordinates[][] getTopLeftCoordinates() {
        return topLeftCoordinates;
    }

    public static Coordinates[][] getBottomRightCoordinates() {
        return bottomRightCoordinates;
    }

    public Square[][] getGameBoard() {
        return gameBoard;
    }
}
