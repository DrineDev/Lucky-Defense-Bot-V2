package Match.Units;

import Basic.Coordinates;
import Basic.PixelColorChecker;
import Basic.Screenshot;

import java.awt.*;
import java.io.IOException;

/**
 * General class
 */
public class Unit {
    protected String name;
    protected int quantity;
    protected int form;
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
        // MYTHICAL = 5
        // LEGENDARY = 4;
        // EPIC = 3;
        // UNCOMMON = 2;
        // COMMON = 1
        // MUST PRESS UNIT IN SQUARE BEFORE EXECUTING
        // MUST ALSO UPDATE GameState.png

        // RARITY COLORS
        Color mythicalColor = new Color(181, 90, 45);
        Color commonColor = new Color(176, 165, 136);
        Color uncommonColor = new Color(59, 76, 136);
        Color epicColor = new Color(111, 56, 146);
        Color legendaryColor = new Color(219, 161, 50);

        Coordinates checkCoordinates = new Coordinates(64, 147);
        Screenshot.screenshotGameState();

        try {
            Color currentColor = PixelColorChecker.getPixelColor("Resources/GameState.png", checkCoordinates);

            if(PixelColorChecker.isMatchingColor(mythicalColor, currentColor, 5))
                return 5;
            if(PixelColorChecker.isMatchingColor(legendaryColor, currentColor, 5))
                return 4;
            if(PixelColorChecker.isMatchingColor(epicColor, currentColor, 5))
                return 3;
            if(PixelColorChecker.isMatchingColor(uncommonColor, currentColor, 5))
                return 2;
            if(PixelColorChecker.isMatchingColor(commonColor, currentColor, 5))
                return 1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return 0; // error
    }


    /** GETTERS & SETTERS */
    public void setName(String name) { this.name = name; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
}