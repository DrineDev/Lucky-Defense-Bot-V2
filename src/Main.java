import Basic.Coordinates;
import Basic.ImageReader;
import Basic.Screenshot;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Screenshot.screenshotGameState();
        Coordinates topLeft1 = new Coordinates(257, 620);
        Coordinates bottomRight1 = new Coordinates(310, 642);

        String text = ImageReader.readImage(topLeft1, bottomRight1);

        System.out.println(text);
    }
}
