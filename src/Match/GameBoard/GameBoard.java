package Match.GameBoard;

import Basic.Coordinates;
import Basic.Press;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;

public class GameBoard {

    private Square[][] gameBoard = new Square[3][6];

    private final static Coordinates[][] topLeftCoordinates = {
            { new Coordinates(75, 450), new Coordinates(145, 450), new Coordinates(215, 450), new Coordinates(285, 450), new Coordinates(355, 450), new Coordinates(425, 450) },
            { new Coordinates(75, 501), new Coordinates(145, 501), new Coordinates(215, 501), new Coordinates(285, 501), new Coordinates(355, 501), new Coordinates(425, 501) },
            { new Coordinates(75, 551), new Coordinates(145, 551), new Coordinates(215, 551), new Coordinates(285, 551), new Coordinates(355, 551), new Coordinates(425, 551) }
    };

    private final static Coordinates[][] bottomRightCoordinates = {
            { new Coordinates(143, 500), new Coordinates(213, 500), new Coordinates(283, 500), new Coordinates(353, 500), new Coordinates(423, 500), new Coordinates(493, 500) },
            { new Coordinates(143, 551), new Coordinates(213, 551), new Coordinates(283, 551), new Coordinates(353, 551), new Coordinates(423, 551), new Coordinates(493, 551) },
            { new Coordinates(143, 601), new Coordinates(213, 601), new Coordinates(283, 601), new Coordinates(353, 601), new Coordinates(423, 601), new Coordinates(493, 601) }
    };

    public GameBoard() {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 6; j++) {
                gameBoard[i][j] = new Square();
            }
        }
    }

    public void updateBoard() {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 6; j++) {
                String action = "Checking square " + i + ", " + j;
                Press.press(topLeftCoordinates[i][j], bottomRightCoordinates[i][j], action);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                gameBoard[i][j].updateSquare(topLeftCoordinates[i][j], bottomRightCoordinates[i][j], action);
            }
        }
        System.out.println("Board updated...");
    }

    public void saveBoardState() {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this.gameBoard);

        try (FileWriter writer = new FileWriter("Resources/gameBoardState.json")) {
            writer.write(json);
        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Board saved to json...");
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
}

