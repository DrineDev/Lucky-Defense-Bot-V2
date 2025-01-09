package Match.Units;

import Basic.CompareImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UncommonUnit extends Unit {

    public UncommonUnit() {
        name = "";
        quantity = 0;
    }
    /**
     * isWhatUnit function to check what kind of Uncommon Unit is in, to be called by Square
     * @return
     */
    public static UncommonUnit isWhatUnit() {
        BufferedImage subImage = null;
        try {
            subImage = ImageIO.read(new File("Resources/GameState.png"));
            subImage = subImage.getSubimage(50, 36, 113, 112);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> unitNames = Arrays.asList("Ranger", "Paladin", "Sandman", "Demon Soldier", "Shock Robot");
        UncommonUnit temp = new UncommonUnit();

        for (String unitName : unitNames) {
            for (int quantity = 1; quantity <= 3; quantity++) {
                // Point to the new directory structure
                String imagePath = String.format("Resources/Units/Uncommon/%s%d.png", unitName, quantity);
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
