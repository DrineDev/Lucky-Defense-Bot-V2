package Match.GameBoard;

import Basic.Coordinates;
import Basic.Press;
import Basic.Screenshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    public GameBoard() {
        initializeGameBoard();
    }

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

    public void updateBoard() throws IOException, InterruptedException {

        if (gameBoard == null) {
            System.out.println("Error: gameBoard is null. Reinitializing...");
            initializeGameBoard();
        }

        System.out.println("Updating board...");
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 6; j++) {
                String action = "Checking square " + i + ", " + j;
                Press.press(topLeftCoordinates[i][j], bottomRightCoordinates[i][j], action);
                Screenshot.screenshotGameState();
                Thread.sleep(750);
                gameBoard[i][j].updateSquare(topLeftCoordinates[i][j], bottomRightCoordinates[i][j], action);
            }
        }
        System.out.println("Board updated...");
    }

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

    public static void moveUnit(int i1, int j1, int i2, int j2) throws IOException, InterruptedException {
        Coordinates fromRandomCoordinates = Coordinates.makeRandomCoordinate(topLeftCoordinates[i1][j1], bottomRightCoordinates[i1][j1]);
        Coordinates toRandomCoordinates = Coordinates.makeRandomCoordinate(topLeftCoordinates[i2][j2], bottomRightCoordinates[i2][j2]);
        Process process = Runtime.getRuntime()
                .exec("adb shell input draganddropp " + fromRandomCoordinates.toString() + " " + toRandomCoordinates.toString());
        Thread.sleep(3000);
    }

    public static Coordinates[][] getTopLeftCoordinates() { return topLeftCoordinates; }
    public static Coordinates[][] getBottomRightCoordinates() { return bottomRightCoordinates; }
    public Square[][] getGameBoard() {
        return gameBoard;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting main method...");
        GameBoard gameBoard1 = new GameBoard();
        System.out.println("GameBoard created.");
        gameBoard1.updateBoard();
        System.out.println("Board updated.");
        gameBoard1.saveBoardState();
        System.out.println("Board state saved.");
    }
}

