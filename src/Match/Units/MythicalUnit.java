package Match.Units;

import Basic.CompareImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MythicalUnit extends Unit {
    int form;

    public MythicalUnit() {
        name = "";
        quantity = 0;
        form = 0;
    }

    public static MythicalUnit isWhatUnit() {
        BufferedImage subImage = null;
        try {
            subImage = ImageIO.read(new File("Resources/GameState.png"));
            subImage = subImage.getSubimage(52, 46, 113, 112);
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

        // Debugging: Check regular mythical units
        for (String unitName : unitNames) {
            String imagePath = "Resources/Units/Mythical/" + unitName + ".png";
            if (CompareImage.compareImage(subImage, imagePath)) {
                System.out.println("Match found: " + unitName); // Debugging output
                temp.setName(unitName);
                temp.setQuantity(1); // Mythical units always have 1 quantity
                return temp;
            }
        }

        // Special case for BatMan
        for (int form = 1; form <= 4; form++) {
            String imagePath = String.format("Resources/Units/Mythical/BatMan%d.png", form);

            if (CompareImage.compareImage(subImage, imagePath)) {
                temp.setName("BatMan");
                temp.setQuantity(1);
                temp.setForm(form);
                return temp;
            }
        }

        // Special case for Tar
        for (int form = 1; form <= 3; form++) {
            String imagePath = String.format("Resources/Units/Mythical/Tar%d.png", form);

            if (CompareImage.compareImage(subImage, imagePath)) {
                System.out.println("Match found: Tar Form " + form); // Debugging output
                temp.setName("Tar");
                temp.setQuantity(1);
                temp.setForm(form);
                return temp;
            }
        }

        // Default fallback: No match found
        if (temp.getName().equals("")) {
            System.out.println("No match found, setting default: Tar with Form 4");
            temp.setName("Tar");
            temp.setQuantity(1);
            temp.setForm(4);

            return temp;
        }

        return null; // If nothing matched
    }

    public void setForm(int form) { this.form = form; }
}
