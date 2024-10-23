package Match.Units;


import Match.GameBoard.GameBoard;
import Match.GameBoard.Square;
import Match.MythicBuilder;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Match.GameBoard.GameBoard.*;

public class ProcessUnit {

    static GameBoard gameboard = new GameBoard();

    /*
     * Detect units in gameBoardState.json and process.
     * @return
     */
    public static Boolean DetectUnitPlusProcess() {
        Gson gson = new Gson();
        String filePath = "Resources/gameBoardState.json";

        try (FileReader reader = new FileReader(filePath)) {
            System.out.println("Attempting to read file: " + filePath);

            // Convert JSON to GameBoard object
            GameBoard gameBoardState = gson.fromJson(reader, GameBoard.class);

            // Loop through gameBoard to process the units
            for (int i = 0; i < 3; i++) { // 3 rows
                for (int j = 0; j < 6; j++) { // 6 columns
                    if (gameBoardState.getSquare(i, j) != null && gameBoardState.getSquare(i, j).getUnit() != null) {
                        Unit unit = gameBoardState.getSquare(i, j).getUnit();
                        if (unit != null) {
                            String unitName = unit.name;
                            System.out.println("Processing unit: " + unitName + " at position: (" + i + ", " + j + ")");

                            // Check for rarity using regex matchers
                            if (isLegendary(unitName)) {
                                processUnitByRarity("Legendary", unit, i, j);
                            } else if (isEpic(unitName)) {
                                processUnitByRarity("Epic", unit, i, j);
                            } else if (isRare(unitName)) {
                                processUnitByRarity("Rare", unit, i, j);
                            } else if (isCommon(unitName)) {
                                processUnitByRarity("Common", unit, i, j);
                            } else if (isMythic(unitName)) {
                                processUnitByRarity("Mythic", unit, i, j);
                            } else {
                                System.out.println("No matching rarity for unit: " + unitName);
                            }
                        }
                    } else {
                        System.out.println("Square at (" + i + ", " + j + ") is empty or null.");
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
    public static boolean isLegendary(String unitName) {
        return Pattern.compile("War Machine|Storm Giant|Sheriff|Tiger Master").matcher(unitName).find();
    }

    public static boolean isEpic(String unitName) {
        return Pattern.compile("Electro Robot|Tree|Wolf Warrior|Hunter|Eagle General").matcher(unitName).find();
    }

    public static boolean isRare(String unitName) {
        return Pattern.compile("Ranger|Sandman|Shock Robot|Paladin|Demon Soldier").matcher(unitName).find();
    }

    public static boolean isCommon(String unitName) {
        return Pattern.compile("Archer|Barbarian|Bandit|Water Elemental|Thrower").matcher(unitName).find();
    }

    public static boolean isMythic(String unitName) {
        return Pattern.compile("Bat Man|Mama|Ninja|Graviton|Orc Shaman|Kitty Mage|Coldy|Blob|Monopoly Man|Frog Prince|Vayne|Lancelot|Iron Meow|Dragon|Bomba|Pulse Generator|Indy|Watt|Tar|Rocket Chu|King Dian|Overclock Rocket Chu").matcher(unitName).find();
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
            case "Mythic":
                processUnitMythic(unit,i,j);
            default:
                System.out.println("Unknown rarity: " + rarity);
        }
    }


    /*
     * Check what to do for Mythical Units
     * @param unit
     * @param i
     * @param j
     * @throws IOException
     * @throws InterruptedException
     */
    public static void processUnitMythic(Unit unit, int i, int j) throws IOException, InterruptedException{
        String unitName = unit.name;
        switch (unitName) {
            case "Bat Man":
                break;
            case "Mama":
                break;
            case "Ninja":
                break;
            case "Graviton":
                break;
            case "Orc Shaman":
                break;
            case "Kitty Mage":
                break;
            case "Coldy":
                break;
            case "Blob":
                break;
            case "Monopoly Man":
                break;
            case "Frog Prince":
                break;
            case "Vayne":
                break;
            case "Lancelot":
                break;
            case "Iron Meow":
                break;
            case "Dragon":
                break;
            case "Bomba":
                break;
            case "Pulse Generator":
                break;
            case "Indy":
                break;
            case "Watt":
                break;
            case "Tar":
                break;
            case "Rocket Chu":
                break;
            case "King Dian":
                break;
            case "Overclock Rocket Chu":
                break;
            default:
                // Handle case where unitName doesn't match any of the cases
                break;
        }

    }
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
                    sellUnit(i,j);
                }
                break;
            case "Sheriff":
                if (MythicBuilder.canBuild("Lancelot")) {
                    MythicBuilder.buildMythic("Lancelot");
                } else {
                    sellUnit(i,j);
                }
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

    /*
     * Check what to do for Epic Units
     * @param unit
     * @param i
     * @param j
     */
    public static void processUnitEpic(Unit unit, int i, int j) throws IOException, InterruptedException {
        String unitName = unit.name;

        switch (unitName) {
            case "Electro Robot":
                Unit unit1 = gameboard.getSquare(0,2).getUnit();
                if (unit1.getName().equals("Electro Robot")){
                    if(unit1.getQuantity()==3)
                    {
                        if(unit.quantity==3){
                            mergeUnit(i,j);
                        }
                        else{
                            sellUnit(i,j);
                        }
                    }else{
                        if(unit1.getQuantity()<unit.quantity){
                            moveUnit(i,j,0,2);
                        }else{
                            sellUnit(i,j);
                        }
                    }
                }
                break;
            case "Tree":
                Unit[] unitsT = new Unit[3];
                unitsT[0] = gameboard.getSquare(1, 2).getUnit();
                unitsT[1] = gameboard.getSquare(1, 3).getUnit();
                unitsT[2] = gameboard.getSquare(1, 4).getUnit();

                boolean canMerge = false;  // Flag to track if we can merge
                boolean shouldMove = false; // Flag to track if we should move
                int targetMoveCol = -1;     // Target column to move the unit if needed

                // Evaluate units in the specified squares
                for (int z = 0; z < 3; ++z) {
                    if (unitsT[z] != null && unitsT[z].getName().equals("Tree")) {
                        if (unitsT[z].getQuantity() == 3 && unit.quantity == 3) {
                            canMerge = true;  // Set the merge flag if quantities match for merging
                        } else if (unitsT[z].getQuantity() < unit.quantity) {
                            shouldMove = true;        // Set the move flag if the current unit has more quantity
                            targetMoveCol = z + 2;   // Set the target column to move to (adjusting for the current index)
                        }
                    }
                }

                // After evaluating all Tree units, make the final decision
                if (canMerge) {
                    mergeUnit(i, j);  // Merge if possible
                } else if (shouldMove && targetMoveCol != -1) {
                    moveUnit(i, j, 1, targetMoveCol);  // Move the unit if moving is needed
                } else {
                    sellUnit(i, j);  // If neither merge nor move is possible, sell the unit
                }

                break;
            case "Wolf Warrior", "Eagle General":
                if(unit.quantity==3)
                    mergeUnit(i,j);
                else {
                    sellUnit(i,j);
                }
                break;
            case "Hunter":
                Unit[] unitsH = new Unit[3];
                unitsH[0] = gameboard.getSquare(0, 5).getUnit();
                unitsH[1] = gameboard.getSquare(1, 5).getUnit();
                unitsH[2] = gameboard.getSquare(2, 5).getUnit();

                canMerge = false;
                shouldMove = false;
                int targetMoveRow = -1;     // Target row to move the unit if needed

                // Evaluate units in the specified squares
                for (int x = 0; x < 3; ++x) {
                    if (unitsH[x] != null && unitsH[x].getName().equals("Hunter")) {
                        if (unitsH[x].getQuantity() == 3 && unit.quantity == 3) {
                            canMerge = true;  // Set the merge flag if quantities match for merging
                        } else if (unitsH[x].getQuantity() < unit.quantity) {
                            shouldMove = true;        // Set the move flag if the current unit has more quantity
                            targetMoveRow = x;       // Set the target row to move to
                        }
                    }
                }

                // After evaluating all Hunter units, make the final decision
                if (canMerge) {
                    mergeUnit(i, j);  // Merge if possible
                } else if (shouldMove && targetMoveRow != -1) {
                    moveUnit(i, j, targetMoveRow, 5);  // Move the unit if moving is needed
                } else {
                    sellUnit(i, j);  // If neither merge nor move is possible, sell the unit
                }

                break;
            default:
                System.out.println("No specific handling for: " + unitName);
        }
    }

    /*
     * Check what to do for Rare/Uncommon Units
     * @param unit
     * @param i
     * @param j
     */
    public static void processUnitRare(Unit unit, int i, int j) throws IOException, InterruptedException {
        String unitName = unit.name;
        switch (unitName){
            case "Ranger", "Sandman", "Shock Robot", "Paladin", "Demon Soldier":
                if(unit.quantity==3)
                    mergeUnit(i,j);
                else {
                    sellUnit(i,j);
                }
                break;
            default:
                System.out.println("No specific handling for: " + unitName);
        }
    }

    /*
     * Check what to do for Common Units
     * @param unit
     * @param i
     * @param j
     */
    public static void processUnitCommon(Unit unit, int i, int j) throws IOException, InterruptedException {
        String unitName = unit.name;
        switch (unitName) {
            case "Archer", "Barbarian", "Water Elemental":
                if(unit.quantity==3)
                    mergeUnit(i,j);
                else {
                    sellUnit(i,j);
                }
                break;
            case "Bandit":
                Unit[] unitsB = new Unit[2];
                unitsB[0] = gameboard.getSquare(1, 0).getUnit();
                unitsB[1] = gameboard.getSquare(2, 0).getUnit();

                boolean shouldMove = false;
                boolean canMerge = false;
                int targetMoveRow = 0;

                for (int c = 0; c < 2; ++c) {
                    Unit currentUnit = unitsB[c];
                    if (currentUnit.getName().equals("Bandit")) {
                        if (unit.getQuantity() > currentUnit.getQuantity()) {
                            // Priority: Move the unit if it has a higher quantity than the Bandit in the square
                            shouldMove = true;
                            targetMoveRow = c + 1;
                        }
                        if (currentUnit.getQuantity() == 3 && unit.getQuantity() == 3) {
                            // Only check for merging after confirming the quantity is 3
                            canMerge = true;
                        }
                    }
                }

                if (shouldMove) {
                    moveUnit(i, j, targetMoveRow, 0);
                } else if (canMerge) {
                    mergeUnit(i, j);
                } else {
                    sellUnit(i, j);
                }
                break;
            case "Thrower":
                Unit[] unitsT = new Unit[2];
                unitsT[0] = gameboard.getSquare(2, 2).getUnit();
                unitsT[1] = gameboard.getSquare(2, 3).getUnit();

                canMerge = false;
                shouldMove = false;
                int targetMoveColumn = -1;  // Target column to move the unit if needed

                // Evaluate units in the specified squares
                for (int v = 0; v < 2; ++v) {
                    if (unitsT[v] != null && unitsT[v].getName().equals("Thrower")) {
                        if (unitsT[v].getQuantity() == 3 && unit.quantity == 3) {
                            canMerge = true;  // Set the merge flag if quantities match for merging
                        } else if (unitsT[v].getQuantity() < unit.quantity) {
                            shouldMove = true;       // Set the move flag if the current unit has more quantity
                            targetMoveColumn = v + 2; // Set the target column to move to
                        }
                    }
                }
                // After evaluating all Thrower units, make the final decision
                if (canMerge) {
                    mergeUnit(i, j);  // Merge if possible
                } else if (shouldMove) {
                    moveUnit(i, j, 2, targetMoveColumn);  // Move the unit if moving is needed
                } else {
                    sellUnit(i, j);  // If neither merge nor move is possible, sell the unit
                }
                break;
            default:
                // Handle case where unitName doesn't match any of the cases
                break;
        }

    }
}