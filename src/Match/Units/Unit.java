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
        // MYTHICAL = 5
        // LEGENDARY = 4;
        // EPIC = 3;
        // UNCOMMON = 2;
        // COMMON = 1
        // MUST PRESS UNIT IN SQUARE BEFORE EXECUTING
        // MUST ALSO UPDATE GameState.png

        // RARITY COLORS
        Color mythicalColor = new Color(181, 90, 45);
        Color commonColor = new Color(177, 166, 137);
        Color uncommonColor = new Color(60, 76, 135);
        Color epicColor = new Color(111, 56, 146);
        Color legendaryColor = new Color(219, 161, 50);

        Coordinates checkCoordinates = new Coordinates(63, 121);
        Screenshot.screenshotGameState();

        try {
            Color currentColor = PixelColorChecker.getPixelColor("Resources/GameState.png", checkCoordinates);

            if(PixelColorChecker.isMatchingColor(legendaryColor, currentColor, 10))
                return 4;
            else if(PixelColorChecker.isMatchingColor(epicColor, currentColor, 10))
                return 3;
            else if(PixelColorChecker.isMatchingColor(uncommonColor, currentColor, 10))
                return 2;
            else if(PixelColorChecker.isMatchingColor(commonColor, currentColor, 10))
                return 1;
            else return 5;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // error
    }


    /** GETTERS & SETTERS */
    public void setName(String name) { this.name = name; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setForm(int form) { this.form = form; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public int getForm() { return form; }
}