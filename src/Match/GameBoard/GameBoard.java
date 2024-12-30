package Match.GameBoard;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import Basic.CompareImage;
import Basic.Coordinates;
import Match.MatchBasic;
import Match.Units.Unit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
        System.out.println("Initializing game board...");
        gameBoard = new Square[3][6];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 6; j++) {
                gameBoard[i][j] = new Square();
            }
        }
        System.out.println("Game board initialization complete.");
    }

    public static Square getSquare(int i, int j) {
        return gameBoard[i][j];
    }

    /**
     * Keep the values to 3x6 Squares
     */
    public void updateBoard(int i, int j) throws IOException, InterruptedException {
        if (gameBoard == null) {
            System.out.println("Board is null...");
        }

        String action = "Checking square " + i + ", " + j;
        gameBoard[i][j].updateSquare(topLeftCoordinates[i][j], bottomRightCoordinates[i][j], action);
        System.out.println("Board " + i + " " + j + " updated...");
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
        System.out.println("Selling the unit at: " + sellRandomCoordinates);
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
        System.out.println("Merging the unit at: " + mergeRandomCoordinates);
        Thread.sleep(500);
        Process process2 = Runtime.getRuntime()
                .exec("adb shell input tap " + mergeRandomCoordinates.getX() + " " + mergeRandomCoordinates.getY());
    }

    /**
     * Upgrade units for mythics like Batman, Tar, Lancelot, etc.
     */
    public static void upgradeUnit(int i, int j) throws IOException, InterruptedException {
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
        System.out.println("Clicking the unit to upgrade at: " + fromRandomCoordinates);
        Process process1 = Runtime.getRuntime()
                .exec("adb shell input tap " + fromRandomCoordinates.getX() + " " + fromRandomCoordinates.getY());
        Thread.sleep(500); // Click merge
        System.out.println("Upgrading the unit at: " + mergeRandomCoordinates);
        Process process2 = Runtime.getRuntime()
                .exec("adb shell input tap " + mergeRandomCoordinates.getX() + " " + mergeRandomCoordinates.getY());
        Thread.sleep(500);
    }

    public GameBoard updateGameBoard(GameBoard gameBoard, int i1, int j1, int i2, int j2) {
        // IF EQUAL, JUST TURN TO NULL TO BE CALLED BY SELL UNIT
        if (i1 == i2 && j1 == j2) {
            gameBoard.setSquare(new Square(), i1, j1);
            return gameBoard;
        }

        // TEMP HOLDER FOR SQUARE AND UNIT
        Square temp = new Square();
        Unit tempUnit;

        // I1 J1 TO TEMP
        temp = getSquare(i1, j1);
        // I1 J1 UNIT TO TEMP
        tempUnit = getSquare(i1, j1).getUnit();

        // PLACE SQUARE I2 J2 TO I1 J1
        gameBoard.setSquare(getSquare(i2, j2), i1, j1);

        // PUT TEMP TO I2 J2
        gameBoard.setSquare(temp, i2, j2);

        return gameBoard;
    }

    public static HashMap<Integer, Integer> getNonEmptySquares() throws IOException {
        if (!MatchBasic.isIngame()) {
            System.out.println("Currently not in game...");
            return null;
        }

        HashMap<Integer, Integer> nonEmptySquares = new HashMap<>();
        BufferedImage baseState = ImageIO.read(new File("Resources/defaultGameBoard.png"));
        BufferedImage gameState = ImageIO.read(new File("Resources/GameState.png"));

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                int topLeftX = topLeftCoordinates[i][j].getX();
                int topLeftY = topLeftCoordinates[i][j].getY();
                int bottomRightX = bottomRightCoordinates[i][j].getX();
                int bottomRightY = bottomRightCoordinates[i][j].getY();
                if (CompareImage.isMatchingRegion(baseState, gameState, topLeftX, topLeftY, bottomRightX, bottomRightY)) {
                    nonEmptySquares.put(i, j);
                }
            }
        }

        return nonEmptySquares;
    }

    public boolean isBoardComplete() {
        if(!getSquare(0, 0).getUnit().getName().equals("Frog Prince"))
            return false;
        if(!getSquare(1, 0).getUnit().getName().equals("Bandit"))
            return false;
        if(!getSquare(2, 0).getUnit().getName().equals("Bandit"))
            return false;
        if(!getSquare(1, 0).getUnit().getName().equals("Frog Prince"))
            return false;
        if(!getSquare(1, 1).getUnit().getName().equals("Dragon"))
            return false;
        if(!getSquare(1, 2).getUnit().getName().equals("Dragon"))
            return false;
        if(!getSquare(0, 2).getUnit().getName().equals("Electro Robot"))
            return false;

        return true;
    }

    public int getTotalUnits(String unitName) {
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
     * 
     * @param row
     * @param column
     * @return
     */

    public void setSquare(Square square, int row, int column) {
        gameBoard[row][column] = square;
    }

    /**
     * GETTERS
     *
     * @return
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
