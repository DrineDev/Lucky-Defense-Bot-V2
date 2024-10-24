package Match.Units;

import Basic.CompareImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LegendaryUnit extends Unit {

    public LegendaryUnit() {
        name = "";
        quantity = 0;
    }

    /**
     * isWhatUnit function to check what kind of Legendary Unit is in, to be called by Square
     * @return
     */
    public static LegendaryUnit isWhatUnit() {
        BufferedImage subImage = null;
        try {
            subImage = ImageIO.read(new File("Resources/GameState.png"));
            subImage = subImage.getSubimage(52, 46, 113, 112);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> unitNames = Arrays.asList("Sheriff", "Storm Giant", "Tiger Master", "War Machine");
        LegendaryUnit temp = new LegendaryUnit();

        for (String unitName : unitNames) {
            for (int quantity = 1; quantity <= 3; quantity++) {
                // Point to the new directory structure
                String imagePath = String.format("Resources/Units/Legendary/%s%d.png", unitName, quantity);
                if (CompareImage.compareImage(subImage, imagePath)) {
                    System.out.println("Match found: " + unitName + " " + quantity + "x");
                    temp.setName(unitName);
                    temp.setQuantity(quantity);
                    return temp;
                }
            }
        }
        return temp; // Return empty if no match is found
    }
}
