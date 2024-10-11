import Match.Units.CommonUnit;
import Match.Units.MythicalUnit;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        MythicalUnit unit = MythicalUnit.isWhatUnit();
        System.out.println(unit.getName());
        try {
            BufferedImage newImage = ImageIO.read(new File("Resources/GameState.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
