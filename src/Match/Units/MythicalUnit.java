package Match.Units;

import Basic.CompareImage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class MythicalUnit extends Unit {
    private static final Logger LOGGER = Logger.getLogger(MythicalUnit.class.getName());

    /**
     * isWhatUnit function to check what kind of Mythical Unit is in, to be called by Square
     * @return
     */
    public static MythicalUnit isWhatUnit() {
        BufferedImage subImage = null;
        try {
            subImage = ImageIO.read(new File("Resources/GameState.png"));
            subImage = subImage.getSubimage(50, 36, 113, 112);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MythicalUnit temp = new MythicalUnit();

        // Mythical Units without special forms
        List<String> unitNames = Arrays.asList(
                "Blob", "Bomba", "Coldy", "Dragon", "Dragon Egg", "Frog Prince", "Graviton",
                "Indy", "Iron Meow (evo)", "Iron Meow (non evo)", "King Dian", "Kitty Mage",
                "Lancelot", "Mama", "Monopoly Man", "Ninja", "Orc Shaman", "Overclocked Rocket Chu",
                "Pulse Generator", "Rocket Chu", "Vayne", "Watt"
        );

        for (String unitName : unitNames) {
            String imagePath = "Resources/Units/Mythical/" + unitName + ".png";
            if (CompareImage.compareImage(subImage, imagePath)) {
                temp.setName(switch (unitName) {
                    case "Dragon Egg" -> "Dragon";
                    case "Iron Meow (evo)" -> "Iron Meow(0)";
                    case "Iron Meow (non evo)" -> "Iron Meow(1)";
                    default -> unitName;
                });
                temp.setQuantity(1); // Mythical units always have 1 quantity
                return temp;
            }
        }

        // Special cases for Bat Man
        for (int form = 0; form <= 4; form++) {
            String imagePath = String.format("Resources/Units/Mythical/Bat Man%d.png", form);
            if (CompareImage.compareImage(subImage, imagePath)) {
                temp.setName("Bat Man");
                temp.setQuantity(1);
                temp.setForm(form);
                return temp;
            }
        }

        // Special cases for Tar
        for (int form = 1; form <= 3; form++) {
            String imagePath = String.format("Resources/Units/Mythical/Tar%d.png", form);
            if (CompareImage.compareImage(subImage, imagePath)) {
                temp.setName("Tar");
                temp.setQuantity(1);
                temp.setForm(form);
                return temp;
            }
        }

        // Default fallback
        temp.setName("Tar");
        temp.setQuantity(1);
        temp.setForm(4);
        return temp;
    }


    public void setForm(int i) { form = i; }
}
