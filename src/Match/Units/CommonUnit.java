package Match.Units;

import Basic.CompareImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CommonUnit extends Unit {
    public CommonUnit() {
        name = "";
        quantity = 0;
    }

    public CommonUnit isWhatUnit() {
        BufferedImage subImage = null;
        try {
            subImage = ImageIO.read(new File("Resources/GameState.png"));
            subImage = subImage.getSubimage(52, 46, 113, 112);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> unitNames = Arrays.asList("Archer", "Bandit", "Barbarian", "Thrower", "Water Elemental");
        CommonUnit temp = new CommonUnit();

        for (String unitName : unitNames) {
            for (int quantity = 1; quantity <= 3; quantity++) {
                // Point to the new directory structure
                String imagePath = String.format("Resources/Units/Common/%s%d.png", unitName, quantity);
                if (CompareImage.compareImage(subImage, imagePath)) {
                    temp.setName(unitName);
                    temp.setQuantity(quantity);
                    return temp;
                }
            }
        }
        return temp; // Return empty if no match is found
    }
}
