package Match.Units;


import Match.GameBoard.GameBoard;
import Match.PlayGame;

import static Logger.Logger.log;
import static Match.GameBoard.GameBoard.*;

import java.io.IOException;
import java.util.regex.Pattern;

public class ProcessUnit {

    /**
     * MAIN FUNCTION
     * Detect units in gameBoardState.json and process.
     */
    public static void DetectUnitPlusProcess(GameBoard gameboard, int i, int j) throws IOException, InterruptedException {

        if (getSquare(i, j) != null && getSquare(i, j).getUnit() != null) {
            Unit unit = getSquare(i, j).getUnit();
                if (unit != null) {
                    String unitName = unit.name;
                    log("Processing unit: " + unitName + " at (" + i + ", " + j + ").");

                    if (isLegendary(unitName)) {
                        processUnitByRarity("Legendary", unit, i, j, gameboard);
                    } else if (isEpic(unitName)) {
                        processUnitByRarity("Epic", unit, i, j, gameboard);
                    } else if (isRare(unitName)) {
                        processUnitByRarity("Rare", unit, i, j, gameboard);
                    } else if (isCommon(unitName)) {
                        processUnitByRarity("Common", unit, i, j, gameboard);
                    } else if (isMythic(unitName)) {
                        processUnitByRarity("Mythic", unit, i, j, gameboard);
                    } else {
                        log("No matching rarity for unit: " + unitName + ".");
                    }
                }
            } else
                log("Square at (" + i + ", " + j + ") is empty or null.");
    }

    /**
     * Function for selling units when units are full
     */
    public static void emergencySell(GameBoard gameBoard) throws IOException, InterruptedException
    {
        Pattern priorityUnits;
        priorityUnits = Pattern.compile("Bandit|Thrower|Barbarian|Water Elemental|Shock Robot|Wolf Warrior|Tree|Electro Robot|Eagle General");
        log("Performing emergency sell...");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 6; j++) {

                Unit unit = getSquare(i,j).getUnit();
                // Ensure the square contains a unit
                if (getSquare(i, j).getUnit() == null) {
                    continue;
                }

                if(!unit.getName().matches(String.valueOf(priorityUnits)))
                {
                    if (unit.getQuantity() == 3)
                        mergeUnit(i,j);
                    else
                        for(int k = unit.getQuantity(); k > 1; --k)
                            gameBoard = sellUnit(gameBoard,i,j);

                }else if (unit.getName().equals("Electro Robot")){
                    break;
                } else{
                    for(int k = unit.getQuantity(); k > 1; --k)
                        gameBoard = sellUnit(gameBoard,i,j);
                }
            }
        }
    }

    /**
     * FUNCTIONS to detect a unit rarity
     * @return true/false
     */
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
        return Pattern.compile("Bat Man|Mama|Ninja|Graviton|Orc Shaman|Kitty Mage|Coldy|Blob|Monopoly Man|Frog Prince|Vayne|Lancelot|Iron Meow(0)|Iron Meow(1)|Iron Meow(2)|Dragon|Drain|Bomba|Pulse Generator|Indy|Watt|Tar|Rocket Chu|King Dian|Overclock Rocket Chu|Lazy Taoist|Zap").matcher(unitName).find();
    }

    /**
     * Main function to process each unit
     */
    public static void processUnitByRarity(String rarity, Unit unit, int i, int j, GameBoard gameboard) throws IOException, InterruptedException {
        switch (rarity) {
            case "Legendary":
                beforeProcessRoutine(gameboard);
                processUnitLegendary(unit, i, j, gameboard);
                break;
            case "Epic":
                beforeProcessRoutine(gameboard);
                processUnitEpic(unit, i, j, gameboard);
                break;
            case "Rare":
                beforeProcessRoutine(gameboard);
                processUnitRare(unit, i, j, gameboard);
                break;
            case "Common":
                beforeProcessRoutine(gameboard);
                processUnitCommon(unit, i, j, gameboard);
                break;
            case "Mythic":
                beforeProcessRoutine(gameboard);
                processUnitMythic(unit, i, j, gameboard);
            default:
                log("[Error] Cannot determine rarity.");
        }

    }

    private static void beforeProcessRoutine(GameBoard gameBoard) throws IOException, InterruptedException {
        PlayGame.checkForRewards();
        PlayGame.waitForGolem();
    }
    /**
     * Check what to do for Mythical Units
     */
    public static void processUnitMythic(Unit baseMythic, int i, int j, GameBoard gameboard) throws IOException, InterruptedException{
        switch (baseMythic.getName()) {
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
                // unit already in optimal position
                if(i == 0 && j == 0 || i == 0 && j == 1)
                    break;

                String unitIn00 = getSquare(0, 0).getUnit().getName();
                String unitIn01 = getSquare(0, 1).getUnit().getName();

                if(!unitIn01.equals("Frog Prince"))
                    gameboard = gameboard.moveUnit(gameboard, i , j, 0, 1);
                else if(!unitIn00.equals("Frog Prince"))
                    gameboard = gameboard.moveUnit(gameboard, i, j, 0, 0);
                break;

            case "Vayne":
                break;
            case "Lancelot":
                break;
            case "Iron Meow(0)":
                break;
            case "Iron Meow(1)":
                break;
            case "Dragon":
                // units already in optimal position
                if(i == 1 && j == 1 || i == 1 && j == 2)
                    break;

                String unitIn11 = getSquare(1, 1).getUnit().getName();
                String unitIn12 = getSquare(1, 2).getUnit().getName();

                if(!unitIn11.equals("Dragon"))
                    gameboard = gameboard.moveUnit(gameboard, i, j, 1, 1);
                else if(!unitIn12.equals("Dragon"))
                    gameboard = gameboard.moveUnit(gameboard, i, j, 1, 2);
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
            case "Drain":
                break;
            case "Iron Meow(2)":
                break;
            case "Lazy Taoist":
                break;
            case "Zap":
                break;
            default:
                break;
        }
    }

    /**
     * Check what to do for Legendary Units
     */
    private static void processUnitLegendary(Unit baseLegendary, int i, int j, GameBoard gameBoard) throws IOException, InterruptedException {
        String unitName = baseLegendary.getName();

        if (isSellableLegendary(unitName)) {
            sellUnitMultipleTimes(gameBoard, i, j, baseLegendary.getQuantity());
        } else {
            log("No specific handling for: " + unitName);
        }
    }

    // Check if a legendary unit is sellable
    private static boolean isSellableLegendary(String unitName) {
        return switch (unitName) {
            case "War Machine", "Storm Giant", "Sheriff", "Tiger Master" -> true;
            default -> false;
        };
    }

    /**
     * Check what to do for Epic Units
     */
    private static void processUnitEpic(Unit baseEpic, int i, int j, GameBoard gameBoard) throws IOException, InterruptedException {
        String unitName = baseEpic.getName();
        int unitQuantity = baseEpic.getQuantity();

        switch (unitName) {
            case "Wolf Warrior":
                PlayGame.handleMythicBuilding(gameBoard);
                handleEpicUnit(gameBoard, i, j, unitQuantity, "Frog Prince", 2, "Wolf Warrior", 0, 4);
                break;

            case "Eagle General":
                PlayGame.handleMythicBuilding(gameBoard);
                handleEpicUnit(gameBoard, i, j, unitQuantity, "Dragon", 2, "Eagle General", 1, 4);
                break;

            case "Electro Robot":
                handleElectroRobot(gameBoard, i, j, baseEpic);
                break;

            case "Hunter":
                gameBoard = sellUnit(gameBoard, i, j);
                break;

            case "Tree":
                PlayGame.handleMythicBuilding(gameBoard);
                handleEpicUnit(gameBoard, i, j, unitQuantity, "Frog Prince", 2, "Tree", 2, 4);
                break;

            default:
                log("No specific handling for: " + unitName);
        }
    }

    // Handle general Epic units (Wolf Warrior, Eagle General, Tree)
    private static void handleEpicUnit(GameBoard gameBoard, int i, int j, int quantity, String dependentUnit, int dependentCount, String targetUnit, int targetX, int targetY) throws IOException, InterruptedException {
        // Check if dependent unit condition is met
        if (getTotalUnits(dependentUnit) == dependentCount) {
            sellUnitMultipleTimes(gameBoard, i, j, quantity);
            return;
        }

        // Check if the unit is already in the optimal position
        if (i == targetX && j == targetY) {
            return;
        }

        // Move to optimal position or sell if already present
        String unitInTarget = getSquare(targetX, targetY).getUnit().getName();
        if (unitInTarget.equals(targetUnit)) {
            sellUnitMultipleTimes(gameBoard, i, j, quantity);
        } else {
            gameBoard.moveUnit(gameBoard, i, j, targetX, targetY);
        }
    }

    // Handle Electro Robot specific logic
    private static void handleElectroRobot(GameBoard gameBoard, int i, int j, Unit baseEpic) throws IOException, InterruptedException {
        if (i == 0 && j == 2) {
            return; // Already in the optimal position
        }

        Unit targetUnit = getSquare(0, 2).getUnit();
        if (targetUnit.getName().equals("Electro Robot")) {
            if (targetUnit.getQuantity() == 3) {
                if (baseEpic.getQuantity() == 3) {
                    mergeUnit(i, j);
                } else if (baseEpic.getQuantity() == 1) {
                    gameBoard = sellUnit(gameBoard, i, j);
                }
            } else if (targetUnit.getQuantity() < baseEpic.getQuantity()) {
                gameBoard.moveUnit(gameBoard, i, j, 0, 2);
            }
        } else {
            gameBoard.moveUnit(gameBoard, i, j, 0, 2);
        }
    }

    /**
     * Check what to do for Rare/Uncommon Units
     *
     */
    public static void processUnitRare(Unit baseRare, int i, int j, GameBoard gameBoard) throws IOException, InterruptedException {
        String unitName = baseRare.getName();
        int unitQuantity = baseRare.getQuantity();

        // Handle common cases for specific rare units
        switch (unitName) {
            case "Ranger", "Sandman", "Paladin", "Demon Soldier":
                handleCommonRareUnits(gameBoard, i, j, unitQuantity);
                break;

            case "Shock Robot":
                handleShockRobot(gameBoard, i, j, unitName, unitQuantity);
                break;

            default:
                log("No specific handling for: " + unitName);
        }
    }

    // Handle units that are sold or merged
    private static void handleCommonRareUnits(GameBoard gameBoard, int i, int j, int quantity) throws IOException, InterruptedException {

        if (quantity == 3) {
            mergeUnit(i, j);
        } else {
            gameBoard = sellUnit(gameBoard, i, j);
            gameBoard = sellUnit(gameBoard, i, j);
        }
    }

    // Handle the Shock Robot unit logic
    private static void handleShockRobot(GameBoard gameBoard, int i, int j, String unitName, int quantity) throws IOException, InterruptedException {

        // Define optimal positions
        int[][] optimalPositions = {{0, 3}, {1, 3}, {2, 3}};

        // Check if the unit is already in one of the optimal positions
        for (int[] position : optimalPositions) {
            int x = position[0];
            int y = position[1];

            // If the unit is already in an optimal position, do nothing and return
            if (getSquare(x, y).getUnit().getName().equals(unitName)) {
                return;
            }
        }

        // Check and move to optimal positions if needed
        for (int[] position : optimalPositions) {
            int x = position[0];
            int y = position[1];

            if (!getSquare(x, y).getUnit().getName().equals(unitName)) {
                gameBoard.moveUnit(gameBoard, i, j, x, y);
                return;
            }
        }

        // Sell unit if all optimal positions are occupied
        for (int k = 0; k < quantity; k++) {
            gameBoard = sellUnit(gameBoard, i, j);
        }
    }


    /**
     * Check what to do for Common Units
     */
    public static void processUnitCommon(Unit baseCommon, int i, int j, GameBoard gameBoard) throws IOException, InterruptedException {
        switch (baseCommon.getName()) {
            case "Bandit":
                handleBandit(baseCommon, i, j, gameBoard);
                break;
            case "Thrower":
                handleThrower(baseCommon, i, j, gameBoard);
                break;
            case "Barbarian":
                handleBarbarian(baseCommon, i, j, gameBoard);
                break;
            case "Water Elemental":
                handleWaterElemental(baseCommon, i, j, gameBoard);
                break;
            case "Archer":
                handleArcher(baseCommon, i, j, gameBoard);
                break;
            case "Imp":
                handleImp(baseCommon, i, j, gameBoard);
            default:
                log("No specific handling for: " + baseCommon.getName());
                break;
        }
    }

    private static void handleBandit(Unit baseCommon, int i, int j, GameBoard gameBoard) throws IOException, InterruptedException {
        // If already in optimal position, do nothing
        if ((i == 1 && j == 0) || (i == 2 && j == 0)) {
            return;
        }

        // Check for existing Bandits in optimal positions
        Unit bandit1 = getSquare(1, 0).getUnit();
        Unit bandit2 = getSquare(2, 0).getUnit();

        // Move to an empty position if available
        if (!bandit1.getName().equals("Bandit")) {
            gameBoard.moveUnit(gameBoard, i, j, 1, 0);
            return;
        }
        if (!bandit2.getName().equals("Bandit")) {
            gameBoard.moveUnit(gameBoard, i, j, 2, 0);
            return;
        }

        // Handle Bandit quantity logic
        handleBanditQuantity(baseCommon, i, j, gameBoard);
    }

    private static void handleBanditQuantity(Unit baseCommon, int i, int j, GameBoard gameBoard) throws IOException, InterruptedException {
        PlayGame.handleMythicBuilding(gameBoard);

        Unit currentBandit = getSquare(i, j).getUnit();

        if (currentBandit.getQuantity() == 3) {
            if (baseCommon.getQuantity() == 3) {
                mergeUnit(i, j);
            } else {
                sellUnitMultipleTimes(gameBoard, i, j, currentBandit.getQuantity());
            }
        } else {
            sellUnitMultipleTimes(gameBoard, i, j, currentBandit.getQuantity());
        }
    }

    private static void handleThrower(Unit baseCommon, int i, int j, GameBoard gameBoard) throws IOException, InterruptedException {
        PlayGame.handleMythicBuilding(gameBoard);

        if (getTotalUnits("Frog Prince") == 2) {
            sellUnitMultipleTimes(gameBoard, i, j, baseCommon.getQuantity());
            return;
        }

        // Check if already in optimal position
        if (i == 1 && j == 5) return;

        // Get the units in the optimal spot
        String unitIn15 = getSquare(1, 5).getUnit().getName();

        // Move or sell excess units
        if (!unitIn15.equals("Thrower")) {
            gameBoard.moveUnit(gameBoard, i, j, 1, 5);
        } else {
            int throwerCount = getTotalUnits("Thrower");
            if (throwerCount >= 3) {
                // If there are 3 or more Throwers, sell excess units
                sellUnitMultipleTimes(gameBoard, i, j, throwerCount - 2); // Sell excess
            } else {
                sellUnitMultipleTimes(gameBoard, i, j, baseCommon.getQuantity());
            }
        }
    }

    private static void handleBarbarian(Unit baseCommon, int i, int j, GameBoard gameBoard) throws IOException, InterruptedException {
        PlayGame.handleMythicBuilding(gameBoard);

        if (getTotalUnits("Frog Prince") == 2) {
            sellUnitMultipleTimes(gameBoard, i, j, baseCommon.getQuantity());
            return;
        }

        // Check if already in optimal position
        if (i == 0 && j == 5) return;

        // Get the units in the optimal spot
        String unitIn05 = getSquare(0, 5).getUnit().getName();

        // Move or sell excess units
        if (!unitIn05.equals("Barbarian")) {
            gameBoard.moveUnit(gameBoard, i, j, 0, 5);
        } else {
            int barbarianCount = getTotalUnits("Barbarian");
            if (barbarianCount >= 3) {
                // If there are 3 or more Barbarians, sell excess units
                sellUnitMultipleTimes(gameBoard, i, j, barbarianCount - 2); // Sell excess
            } else {
                sellUnitMultipleTimes(gameBoard, i, j, baseCommon.getQuantity());
            }
        }
    }

    private static void handleWaterElemental(Unit baseCommon, int i, int j, GameBoard gameBoard) throws IOException, InterruptedException {
        PlayGame.handleMythicBuilding(gameBoard);

        if (getTotalUnits("Dragon") == 2) {
            sellUnitMultipleTimes(gameBoard, i, j, baseCommon.getQuantity());
            return;
        }

        // Check if already in optimal position
        if (i == 2 && j == 5) return;

        // Get the units in the optimal spot
        String unitIn25 = getSquare(2, 5).getUnit().getName();

        // Move or sell excess units
        if (!unitIn25.equals("Water Elemental")) {
            gameBoard.moveUnit(gameBoard, i, j, 2, 5);
        } else {
            int waterElementalCount = getTotalUnits("Water Elemental");
            if (waterElementalCount >= 3) {
                // If there are 3 or more Water Elementals, sell excess units
                sellUnitMultipleTimes(gameBoard, i, j, waterElementalCount - 2); // Sell excess
            } else {
                sellUnitMultipleTimes(gameBoard, i, j, baseCommon.getQuantity());
            }
        }
    }

    private static void handleArcher(Unit baseCommon, int i, int j, GameBoard gameBoard) throws IOException, InterruptedException {
        if (baseCommon.getQuantity() == 3) {
            mergeUnit(i, j);
        } else {
            sellUnitMultipleTimes(gameBoard, i, j, baseCommon.getQuantity());
        }
    }

    private static void handleImp(Unit baseCommon, int i, int j, GameBoard gameBoard) throws IOException, InterruptedException {
        if (baseCommon.getQuantity() == 3) {
            mergeUnit(i, j);
        } else {
            sellUnitMultipleTimes(gameBoard, i, j, baseCommon.getQuantity());
        }
    }

    // Utility method for selling units multiple times
    public static void sellUnitMultipleTimes(GameBoard gameBoard, int i, int j, int quantity) throws IOException, InterruptedException {
        for (int k = 0; k < quantity; k++) {
            gameBoard = sellUnit(gameBoard, i, j);
        }
    }
}
