package Match.Units;

import Basic.Coordinates;
import Basic.PixelColorChecker;
import Basic.Screenshot;

import java.awt.*;
import java.io.IOException;
import java.util.Map;

import static Logger.Logger.log;

/**
 * General class
 */
public class Unit {
    protected String name;
    protected int quantity;
    public int form;
    // protected int value;

    public Unit() {
        name = "";
        quantity = 0;
        form = 0;
    }

    /**
     * Function to check for rarity for easy Unit searching
     * @return
     * @throws IOException
     */
    public static int isWhatRarity() throws IOException {
        // Define rarity levels
        final int MYTHICAL = 5;
        final int LEGENDARY = 4;
        final int EPIC = 3;
        final int UNCOMMON = 2;
        final int COMMON = 1;

        // Define rarity colors
        Map<Integer, Color> rarityColors = Map.of(
                MYTHICAL, new Color(181, 90, 45),
                LEGENDARY, new Color(219, 161, 50),
                EPIC, new Color(111, 56, 146),
                UNCOMMON, new Color(60, 76, 134),
                COMMON, new Color(177, 166, 137)
        );

        Coordinates checkCoordinates = new Coordinates(63, 121); // Location to check pixel color

        try {
            // Get the current color at the specified coordinates
            Color currentColor = PixelColorChecker.getPixelColor("Resources/GameState.png", checkCoordinates);

            // Find the rarity that matches the color within tolerance
            return rarityColors.entrySet().stream()
                    .filter(entry -> PixelColorChecker.isMatchingColor(entry.getValue(), currentColor, 10))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(MYTHICAL); // Default to Mythical if no match is found
        } catch (IOException e) {
            log("Error reading pixel color: " + e.getMessage());
            throw e; // Re-throw the exception for higher-level handling
        }
    }



    /** GETTERS & SETTERS */
    public void setName(String name) { this.name = name; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setForm(int form) { this.form = form; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public int getForm() { return form; }
}