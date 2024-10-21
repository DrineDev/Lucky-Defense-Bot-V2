package Match.Units;

import Match.GameBoard.GameBoard;
import Match.MythicBuilder;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static Match.GameBoard.GameBoard.mergeUnit;
import static Match.GameBoard.GameBoard.sellUnit;

public class ProcessUnit {

    // Inner classes nga same sa json
    public static class Cell {
        Unit unit;
    }

    public static class GameBoard {
        Cell[][] gameBoard; // 2D array kay 3x6 mn ang board
    }

    /**
     * Detect units in gameBoardState.json and process.
     * @return
     */
    public static Boolean DetectUnitPlusProcess() {
        Gson gson = new Gson();
        String filePath = "Resources/gameBoardState.json";

        // HashMap for unit rarity with compiled patterns
        // ang keys sa hashmap kay ang rarity nya ang value kay ang regex pattern
        Map<String, Pattern> rarityPatterns = new HashMap<>();
        rarityPatterns.put("Legendary", Pattern.compile("War Machine|Storm Giant|Sheriff|Tiger Master"));
        rarityPatterns.put("Epic", Pattern.compile("Electro Robot|Tree|Wolf Warrior|Hunter|Eagle General"));
        rarityPatterns.put("Rare", Pattern.compile("Ranger|Sandman|Shock Robot|Paladin|Demon Soldier"));
        rarityPatterns.put("Common", Pattern.compile("Archer|Barbarian|Bandit|Water Elemental|Thrower"));

        try (FileReader reader = new FileReader(filePath)) {
            System.out.println("Attempting to read file: " + filePath);

            // Convert JSON to GameBoard object
            GameBoard gameBoardState = gson.fromJson(reader, GameBoard.class);

            // Loop through gameBoard to process the units
            for (int i = 0; i < 3; i++) { // 3 rows
                for (int j = 0; j < 6; j++) { // 6 columns
                    //check if empty ang square or out of bounds
                    if (gameBoardState.gameBoard[i][j] != null && gameBoardState.gameBoard[i][j].unit != null) {
                        Unit unit = gameBoardState.gameBoard[i][j].unit; //assign ang unit sa i and j to a temp unit holder
                        String unitName = unit.name;

                        // Check for the unit rarity using the HashMap with compiled patterns
                        //loop thru the hashmap rana pero chuy
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
            return true;
        } catch (IOException e) {
            System.out.println("IOException occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
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


    /**
     * Check what to do for Mythical Units
     * @param unit
     * @param i
     * @param j
     * @throws IOException
     * @throws InterruptedException
     */
    public static void processUnitLegendary(Unit unit, int i, int j) throws IOException, InterruptedException {
        String unitName = unit.name;

        switch (unitName) {
            case "War Machine":
                if (MythicBuilder.canBuild("RocketChu")) {
                    MythicBuilder.buildMythic("RocketChu");
                } else {
                    sellUnit(i, j);
                }
                break;
            case "Storm Giant":
                if (MythicBuilder.canBuild("Coldy")) {
                    MythicBuilder.buildMythic("Coldy");
                } else {
                    try {
                        sellUnit(i, j);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case "Sheriff":
                if (MythicBuilder.canBuild("Lancelot")) {
                    MythicBuilder.buildMythic("Lancelot");
                } else {

                }
                break;
            case "Tiger Master":
                if (MythicBuilder.canBuild("Batman")) {
                    MythicBuilder.buildMythic("Batman");
                } else {

                }
                break;
            default:
                System.out.println("No specific handling for: " + unitName);
        }
    }

    /**
     * Check what to do for Epic Units
     * @param unit
     * @param i
     * @param j
     */
    public static void processUnitEpic(Unit unit, int i, int j)
    {
        String unitName = unit.name;

        switch (unitName) {
            case "Electro Robot":
                // TODO: Implement logic for Electro Robot
                break;
            case "Tree":
                // TODO: Implement logic for Tree
                break;
            case "Wolf Warrior":
                // TODO: Implement logic for Wolf Warrior
                break;
            case "Hunter":
                // TODO: Implement logic for Hunter
                break;
            case "Eagle General":
                // TODO: Implement logic for Eagle General
                break;
            default:
                System.out.println("No specific handling for: " + unitName);
        }
    }

    /**
     * Check what to do for Rare/Uncommon Units
     * @param unit
     * @param i
     * @param j
     */
    public static void processUnitRare(Unit unit, int i, int j)
    {
        // TODO: Implement rare unit process
    }

    /**
     * Check what to do for Common Units
     * @param unit
     * @param i
     * @param j
     */
    public static void processUnitCommon(Unit unit, int i, int j)
    {
        // TODO: Implement common unit process
    }
}
