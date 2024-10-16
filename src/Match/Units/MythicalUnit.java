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
    int form = 0;

    public static MythicalUnit isWhatUnit() {
        BufferedImage subImage = null;
        try {
            File gameStateFile = new File("Resources/GameState.png");
            LOGGER.info("GameState file exists: " + gameStateFile.exists());
            LOGGER.info("GameState file path: " + gameStateFile.getAbsolutePath());

            BufferedImage fullImage = ImageIO.read(gameStateFile);
            subImage = fullImage.getSubimage(52, 46, 113, 112);
            LOGGER.info("Subimage extracted successfully. Size: " + subImage.getWidth() + "x" + subImage.getHeight());

            // Save subimage for debugging
            ImageIO.write(subImage, "png", new File("Resources/Debug_Subimage.png"));
            LOGGER.info("Debug subimage saved to Resources/Debug_Subimage.png");
        } catch (IOException e) {
            LOGGER.severe("Error reading GameState image: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        MythicalUnit temp = new MythicalUnit();

        // Mythical Units without special forms
        List<String> unitNames = Arrays.asList(
                "Blob", "Bomba", "Coldy", "Dragon", "Dragon Egg", "Frog Prince", "Graviton",
                "Indy", "Iron Meow (evo)", "Iron Meow (non evo)", "King Dian", "Kitty Mage",
                "Lancelot", "Mama", "Monopoly Man", "Ninja", "Orc Shaman", "Overclocked Rocket Chu",
                "Pulse Generator", "Rocket Chu", "Vayne", "Watt"
        );

        // Check regular mythical units
        for (String unitName : unitNames) {
            String imagePath = "Resources/Units/Mythical/" + unitName + ".png";
            if (CompareImage.compareImage(subImage, imagePath)) {
                LOGGER.info("Match found: " + unitName);
                temp.setName(unitName);
                temp.setQuantity(1); // Mythical units always have 1 quantity
                return temp;
            }
        }

        // Special case for BatMan
        for (int form = 1; form <= 4; form++) {
            String imagePath = String.format("Resources/Units/Mythical/BatMan%d.png", form);
            if (CompareImage.compareImage(subImage, imagePath)) {
                LOGGER.info("Match found: BatMan Form " + form);
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
                LOGGER.info("Match found: Tar Form " + form);
                temp.setName("Tar");
                temp.setQuantity(1);
                temp.setForm(form);
                return temp;
            }
        }

        // Default fallback: No match found
        LOGGER.warning("No match found, setting default: Tar with Form 4");
        temp.setName("Tar");
        temp.setQuantity(1);
        temp.setForm(4);

        return temp;
    }

    public void setForm(int i) { form = 1; }
}
