package Match.Units;
import Match.GameBoard.GameBoard;
import Match.GameBoard.Square;
import Match.MythicBuilder;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Match.GameBoard.GameBoard.*;

public class ProcessUnit {
    GameBoard gameBoard = new GameBoard();

    public void accessGameBoard() {
        Gson gson = new Gson();
        String filepath = "C:/Users/Owner/Lucky-Defense-Bot-V2/Resources/gameBoardState.json";

        // hashmap for the unit regexes
        Map<String, Pattern> rarityPatterns = new HashMap<>();
        rarityPatterns.put("Legendary", Pattern.compile("War Machine|Storm Giant|Sheriff|Tiger Master"));
        rarityPatterns.put("Epic", Pattern.compile("Electro Robot|Tree|Wolf Warrior|Hunter|Eagle General"));
        rarityPatterns.put("Rare", Pattern.compile("Ranger|Sandman|Shock Robot|Paladin|Demon Soldier"));
        rarityPatterns.put("Common", Pattern.compile("Archer|Barbarian|Bandit|Water Elemental|Thrower"));

        try (FileReader reader = new FileReader(filepath)) {
            System.out.println("Attempting to read file.");

            // convert JSON to GameBoard object
            GameBoard gameBoardState = gson.fromJson(reader, GameBoard.class);

            // Iterate over rows and columns of the game board
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 6; ++j) {
                    // Retrieve the square at position (i, j)
                    Square square = gameBoardState.gameBoard[i][j];

                    // Ensure square is not null and contains a unit
                    if (square != null && square.getUnit() != null) {
                        Unit unit = square.getUnit(); // Get the unit from the square
                        String unitName = unit.getName(); // Get the unit's name

                        // Check for the unit rarity using the HashMap with compiled patterns
                        for (Map.Entry<String, Pattern> entry : rarityPatterns.entrySet()) {
                            String rarity = entry.getKey();
                            Pattern pattern = entry.getValue();
                            Matcher matcher = pattern.matcher(unitName);

                            if (matcher.find()) {
                                // Process the unit based on its rarity
                                processUnitByRarity(rarity, unit, i, j);
                                break; // Exit loop once the unit is processed
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("IOException occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void processUnitByRarity(String rarity, Unit unit, int i, int j) throws IOException, InterruptedException {
        switch (rarity) {
            case "Legendary":
                processUnitLegendary(unit, i, j);
                break;
            case "Epic":
                processUnitEpic(unit, i, j);
                break;
            case "Rare":
                processUnitRare(unit, i, j);
                break;
            case "Common":
                processUnitCommon(unit, i, j);
                break;
            default:
                System.out.println("Unknown rarity: " + rarity);
        }
    }


    public static void processUnitLegendary(Unit unit, int i, int j) throws IOException, InterruptedException {
        String unitName = unit.name;

        switch (unitName) {
            case "War Machine":
                moveUnit(i,j,0,3);
                break;
            case "Storm Giant":
                sellUnit(i,j);
                break;
            case "Sheriff":
                sellUnit(i,j);
                break;
            case "Tiger Master":
                if (MythicBuilder.canBuild("Batman")) {
                    MythicBuilder.buildMythic("Batman");
                } else {
                    moveUnit(i,j,0,1);
                }
                break;
            default:
                System.out.println("No specific handling for: " + unitName);
        }
    }

    public static void processUnitEpic(Unit unit, int i, int j) throws IOException, InterruptedException {
        String unitName = unit.name;

        switch (unitName) {
            case "Electro Robot":
                Square square = GameBoard.getSquare(0, 2);
                Unit unitE = square.getUnit(); // Get the unit from the square
                // Default to 0 if unitE is null to avoid NullPointerException
                int quantityE = (unitE != null) ? unitE.getQuantity() : 0;
                // Check if unitE is not null and is named "Electro Robot"
                if (unitE != null && unitE.getName().equals("Electro Robot")) {
                    if (quantityE == 3) {
                        // If the quantity is 3, merge the current unit
                        if (unit.getQuantity() == 3) {
                            mergeUnit(i, j);
                        } else {
                            // If the current unit's quantity is not 3, sell it
                            sellUnit(i, j);
                        }
                    }
                } else {
                    // If unitE is null or not named "Electro Robot", move the current unit
                    moveUnit(i, j, 0, 2);
                }
                break;
            case "Tree":
                break;
            case "Wolf Warrior":
                int quantityW = unit.getQuantity();
                if(quantityW==3){
                    mergeUnit(i,j);
                    break;
                }
                else{
                    sellUnit(i,j);
                    break;
                }
            case "Hunter":
                break;

            case "Eagle General":
                // TODO: Implement logic for Eagle General
                break;
            default:
                System.out.println("No specific handling for: " + unitName);
        }
    }
    public static void processUnitRare(Unit unit, int i, int j)
    {
        String unitName = unit.name;

        switch(unitName){
            case "Ranger":
                break;
            case "Sandman":
                break;
            case "Shock Robot":
                break;
            case "Paladin":
                break;
            case "Demon Soldier":
                break;
        }
    }
    public static void processUnitCommon(Unit unit, int i, int j)
    {
        String unitName = unit.name;

        switch (unitName){
            case "Archer":
                break;
            case "Barbarian":
                break;
            case "Bandit":
                break;
            case "Water Elemental":
                break;
            case "Thrower":
                break;

        }
    }



}
