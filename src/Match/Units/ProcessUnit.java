package Match.Units;


import Match.GameBoard.GameBoard;

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
                    System.out.println("Processing unit: " + unitName + " at position: (" + i + ", " + j + ")");

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
                        System.out.println("No matching rarity for unit: " + unitName);
                    }
                }
            } else
                System.out.println("Square at (" + i + ", " + j + ") is empty or null.");
    }

    /**
     * Function for selling units when units are full
     */
    public static void emergencySell(GameBoard gameBoard) throws IOException, InterruptedException
    {
        Pattern priorityUnits;
        priorityUnits = Pattern.compile("Bandit|Thrower|Barbarian|Water Elemental|Shock Robot|Wolf Warrior|Tree|Electro Robot|Eagle General");
        System.out.println("Performing emergency sell");
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
        return Pattern.compile("Bat Man|Mama|Ninja|Graviton|Orc Shaman|Kitty Mage|Coldy|Blob|Monopoly Man|Frog Prince|Vayne|Lancelot|Iron Meow(0)|Iron Meow(1)|Dragon|Bomba|Pulse Generator|Indy|Watt|Tar|Rocket Chu|King Dian|Overclock Rocket Chu").matcher(unitName).find();
    }

    /**
     * Main function to process each unit
     */
    public static void processUnitByRarity(String rarity, Unit unit, int i, int j, GameBoard gameboard) throws IOException, InterruptedException {
        switch (rarity) {
            case "Legendary":
                processUnitLegendary(unit, i, j, gameboard);
                break;
            case "Epic":
                processUnitEpic(unit, i, j, gameboard);
                break;
            case "Rare":
                processUnitRare(unit, i, j, gameboard);
                break;
            case "Common":
                processUnitCommon(unit, i, j, gameboard);
                break;
            case "Mythic":
                processUnitMythic(unit, i, j, gameboard);
            default:
                System.out.println("Unknown rarity: " + rarity);
        }

    }

    /**
     * Check what to do for Mythical Units
     */
    public static void processUnitMythic(Unit baseMythic, int i, int j, GameBoard gameboard) throws IOException, InterruptedException{
        switch (baseMythic.getName()) {
            case "Bat Man":
// UNCOMMENT FOR BAT MAN ACTIONS
//                // CHECK BATMAN FORM, AND IF BAD, THEN UPGRADE
//                while (baseMythic.form != 3 && baseMythic.form != 4) // UPGRADE 2 TIMES,
//                {
//                    upgradeUnit(i, j);
//                    upgradeUnit(i, j);
//                    gameboard.updateBoard(i, j);
//                    gameboard = ProcessUnit.DetectUnitPlusProcess(gameboard, i, j);
//                    baseMythic.setForm(gameboard.getSquare(i, j).getUnit().getForm());
//                }
//
//
//                if(i == 0 && j == 1 || i == 0 && j == 3)
//                    break; // IF UNIT IN MOST OPTIMAL POSITION, DO NOT TOUCH
//
//                // CHECK OPTIMAL POSITIONS
//                Unit unitBatman1 =  gameboard.getSquare(0, 1).getUnit();
//                Unit unitBatman2 = gameboard.getSquare(0, 3).getUnit();
//                Unit unitBatman3 = gameboard.getSquare(0, 4).getUnit();
//
//                // MOVE UNITS TO OPTIMAL POSITION IF NO OPTIMAL UNIT FOUND
//                if(!unitBatman1.getName().equals("Bat Man"))
//                    gameboard = gameboard.moveUnit(gameboard, i, j, 0, 1);
//                else if(!unitBatman2.getName().equals("Bat Man"))
//                    gameboard = gameboard.moveUnit(gameboard, i, j, 0, 3);
//                else if(!unitBatman3.getName().equals("Bat Man"))
//                    gameboard = gameboard.moveUnit(gameboard, i, j, 0, 4);
//                break;
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
            default:
                break;
        }
    }

    /**
     * Check what to do for Legendary Units
     */
    private static void processUnitLegendary(Unit baseLegendary, int i, int j, GameBoard gameboard) throws IOException, InterruptedException {
        switch (baseLegendary.getName()) {
            case "War Machine", "Storm Giant", "Sheriff", "Tiger Master":
                for(int k = baseLegendary.getQuantity(); k > 0; k--)
                    gameboard = sellUnit(gameboard, i, j);
                break;
                // REMOVE TIGER MASTER FROM CASE 1 AND UNCOMMENT FOR BATMAN
//            case "Tiger Master":
//                if(i == 0 && j == 0)
//                    break; // IF UNIT IN MOST OPTIMAL POSITION, DO NOT MOVE
//
//                Unit tigerMaster1 = gameboard.getSquare(0, 0).getUnit();
//                if(tigerMaster1.getName().equals("Tiger Master")) {
//                    if(tigerMaster1.getQuantity() == 3) {
//                        if(baseLegendary.getQuantity() == 3)
//                            mergeUnit(i, j);
//                        else
//                            for (int k = tigerMaster1.getQuantity(); k > 0; --k)
//                                gameboard = sellUnit(gameboard, i, j);
//                    } else if(tigerMaster1.getQuantity() < baseLegendary.getQuantity())
//                        gameboard = gameboard.moveUnit(gameboard, i, j, 0, 0);
//                } else
//                    gameboard = gameboard.moveUnit(gameboard, i, j, 0, 0);
//                break;

            default:
                System.out.println("No specific handling for: " + baseLegendary.getName());
        }
    }

    /**
     * Check what to do for Epic Units
     */
    private static void processUnitEpic(Unit baseEpic, int i, int j, GameBoard gameboard) throws IOException, InterruptedException {
        switch (baseEpic.getName()){
            case "Wolf Warrior":
                // IF 2 FROG PRINCE ALREADY EXISTS
                if (gameboard.getTotalUnits("Frog Prince") == 2) {
                    for(int k = 0; k < baseEpic.getQuantity(); k++)
                        gameboard = sellUnit(gameboard, i, j);
                    break;
                }

                if (i == 0 && j == 4)
                    break;

                String unitIn04 = getSquare(0, 4).getUnit().getName();

                if(unitIn04.equals("Wolf Warrior"))
                    for(int k = 0; k < baseEpic.getQuantity(); k++)
                        gameboard = sellUnit(gameboard, i, j);
                else
                   gameboard.moveUnit(gameboard, i, j, 0, 4);

                break;
            case "Eagle General":
                // IF DRAGON ALREADY EXISTS
                if (gameboard.getTotalUnits("Dragon") == 2) {
                    for(int k = 0; k < baseEpic.getQuantity(); k++)
                        gameboard = sellUnit(gameboard, i, j);
                    break;
                }

                if (i == 1 && j == 4)
                    break;

                String unitIn14 = getSquare(1, 4).getUnit().getName();

                if(unitIn14.equals("Eagle General"))
                    for(int k = 0; k < baseEpic.getQuantity(); k++)
                        gameboard = sellUnit(gameboard, i, j);
                else
                    gameboard.moveUnit(gameboard, i, j, 1, 4);

                break;

// UNCOMMENT FOR BATMAN
//                if (baseEpic.getQuantity() == 3){
//                    mergeUnit(i,j);
//                } else if(baseEpic.getQuantity() == 2) {
//                    break;
//                } else if(baseEpic.getQuantity() == 1){
//                    gameboard = sellUnit(gameboard, i, j);
//                }
//                break;

            case "Electro Robot":
                if(i == 0 && j == 2)
                    break; // IF UNIT IN OPTIMAL POSITION, DO NOT TOUCH

                Unit electroRobot1 = getSquare(0,2).getUnit();

                if(electroRobot1.getName().equals("Electro Robot")) {
                    if(electroRobot1.getQuantity() == 3){
                        if(baseEpic.getQuantity() == 3)
                            mergeUnit(i,j);
                        else if(baseEpic.getQuantity() == 1)
                            gameboard = sellUnit(gameboard, i, j);
                    } else if (electroRobot1.getQuantity() < baseEpic.getQuantity())
                        gameboard = gameboard.moveUnit(gameboard, i, j,0,2);
                } else
                    gameboard = gameboard.moveUnit(gameboard, i,j,0,2);
                break;

            case "Hunter":
                gameboard = sellUnit(gameboard, i, j);
// UNCOMMENT FOR BATMAN
//                if((i == 0 && j == 5) || (i == 1 && j == 5))
//                    break; // IF UNIT IN OPTIMAL POSITION, DO NOT TOUCH
//
//                Unit Hunter1 = gameboard.getSquare(0,5).getUnit();
//                Unit Hunter2 = gameboard.getSquare(1,5).getUnit();
//                boolean canMerge = false;
//                boolean shouldMove = false;
//                boolean shouldSell = false;
//                boolean processed = false;
//
//                if(Hunter1.getName().equals("Hunter")) {
//                    if(Hunter1.getQuantity() == 3){
//                        if(baseEpic.getQuantity() == 3)
//                            canMerge = true;
//                        else if(baseEpic.getQuantity() == 1)
//                            shouldSell = true;
//                    } else if(Hunter1.getQuantity() < baseEpic.getQuantity())
//                        shouldMove = true;
//                } else {
//                    gameboard = gameboard.moveUnit(gameboard, i,j,0,5);
//                    processed = true;
//                }
//
//                if(!processed){
//                    if(Hunter2.getName().equals("Hunter")) {
//                        if(Hunter2.getQuantity() == 3){
//                            if(baseEpic.getQuantity() == 3)
//                                canMerge = true;
//                            else if(baseEpic.getQuantity() == 1)
//                                shouldSell = true;
//                        } else if (Hunter2.getQuantity() < baseEpic.getQuantity())
//                            shouldMove = true;
//                    } else {
//                        gameboard = gameboard.moveUnit(gameboard, i, j, 1, 5);
//                        processed = true;
//                        break;
//                    }
//                }
//
//                if(shouldMove){
//                    if(Hunter1.getQuantity() < Hunter2.getQuantity())
//                        gameboard = gameboard.moveUnit(gameboard, i, j, 0, 5);
//                    else
//                        gameboard = gameboard.moveUnit(gameboard, i, j, 1, 5);
//                } else {
//                    if(canMerge)
//                        mergeUnit(i,j);
//                    else if(shouldSell)
//                        for (int k = baseEpic.getQuantity(); k > 0; --k)
//                            gameboard = sellUnit(gameboard, i, j);
//                }
                break;
            case "Tree":
                // IF 2 FROG PRINCE ALREADY EXISTS
                if (gameboard.getTotalUnits("Frog Prince") == 2) {
                    for(int k = 0; k < baseEpic.getQuantity(); k++)
                        gameboard = sellUnit(gameboard, i, j);
                    break;
                }

                if (i == 2 && j == 4)
                    break;

                String unitIn24 = getSquare(2, 4).getUnit().getName();

                if(unitIn24.equals("Tree"))
                    for(int k = 0; k < baseEpic.getQuantity(); k++)
                        gameboard = sellUnit(gameboard, i, j);
                else
                    gameboard = gameboard.moveUnit(gameboard, i, j, 2, 4);

                break;

// UNCOMMENT FOR BATMAN
//                if(MythicBuilder.canBuild("Bat Man", gameboard)) {
//                    MatchBasic.pressAnywhere();
//                    Thread.sleep(750);
//                    MatchBasic.pressBuildFavoriteMythic();
//                    break;
//                }
//
//                if((i == 1 && j == 3))
//                    break; // IF UNIT IN OPTIMAL POSITION, DO NOT TOUCH
//
//                Unit tree1 = getSquare(1,3).getUnit();
//                if(tree1.getName().equals("Tree")) {
//                    if(tree1.getQuantity() == 3){
//                        if(baseEpic.getQuantity() == 3)
//                            mergeUnit(i, j);
//                        else if(baseEpic.getQuantity() == 1)
//                            gameboard = sellUnit(gameboard, i ,j);
//                    } else if(tree1.getQuantity() < baseEpic.getQuantity())
//                        gameboard = gameboard.moveUnit(gameboard, i, j, 1, 3);
//                } else {
//                    gameboard = gameboard.moveUnit(gameboard, i,j,1,3);
//                }
//                break;
            default:
                System.out.println("No specific handling for this " + baseEpic.getName());
                break;
        }
    }

    /**
     * Check what to do for Rare/Uncommon Units
     *
     */
    public static void processUnitRare(Unit baseRare, int i, int j, GameBoard gameboard) throws IOException, InterruptedException {
        switch (baseRare.getName()) {
            case "Ranger", "Sandman", "Paladin", "Demon Soldier":
                if (baseRare.getQuantity() == 3)
                    mergeUnit(i, j);
                else {
                    for (int k = baseRare.getQuantity(); k > 0; --k)
                        gameboard = sellUnit(gameboard, i, j);
                }
                break;
            case "Shock Robot":
                if (i == 0 && j == 3 || i == 1 && j == 3 || i == 2 && j == 3)
                    break;

                String unitIn03 = getSquare(0, 3).getUnit().getName();
                String unitIn13 = getSquare(1, 3).getUnit().getName();
                String unitIn23 = getSquare(2, 3).getUnit().getName();

                if (!unitIn03.equals("Shock Robot")) {
                    gameboard.moveUnit(gameboard, i, j, 0, 3);
                    break;
                } else if(!unitIn13.equals("Shock Robot")) {
                    gameboard.moveUnit(gameboard, i, j, 1, 3);
                    break;
                } else if(!unitIn23.equals("Shock Robot")) {
                    gameboard.moveUnit(gameboard, i, j, 2, 3);
                    break;
                }

                for(int k = 0; baseRare.getQuantity() > k; k++)
                    gameboard = sellUnit(gameboard, i, j);

                break;

            default:
                System.out.println("No specific handling for: " + baseRare.getName());
        }
    }

    /**
     * Check what to do for Common Units
     */
    public static void processUnitCommon(Unit baseCommon, int i, int j, GameBoard gameboard) throws IOException, InterruptedException {
        switch (baseCommon.getName()){
            case "Bandit":
                if((i == 1 && j == 0) || (i == 2 && j == 0)) {
                    break; // IF UNIT IN MOST OPTIMAL POSITION, DO NOT TOUCH
                }

                boolean canMerge = false;
                boolean shouldMove = false;
                boolean shouldSell = false;
                boolean processed = false;
                Unit bandit1 = getSquare(1,0).getUnit();
                Unit bandit2 = getSquare(2,0).getUnit();

                if(bandit1.getName().equals("Bandit")) {
                    if(bandit1.getQuantity() == 3){
                        if(baseCommon.getQuantity() == 3)
                            canMerge = true;
                        else
                            shouldSell = true;
                    }else if (bandit1.getQuantity() < baseCommon.getQuantity())
                        shouldMove = true;
                }else{
                    gameboard.moveUnit(gameboard, i,j,1,0);
                    break;
                }

                if (bandit2.getName().equals("Bandit")) {
                    if (bandit2.getQuantity() == 3) {
                        if (baseCommon.getQuantity() == 3)
                            canMerge = true;
                        else
                            shouldSell = true;
                    } else if (bandit2.getQuantity() < baseCommon.getQuantity())
                        shouldMove = true;
                } else {
                    gameboard.moveUnit(gameboard, i, j, 2, 0);
                    break;
                }

                if(shouldMove){
                    if(bandit1.getQuantity() < bandit2.getQuantity())
                        gameboard.moveUnit(gameboard, i, j, 1, 0);
                    else
                        gameboard.moveUnit(gameboard, i, j, 2, 0);
                } else {
                    if (canMerge)
                        mergeUnit(i, j);
                    else if (shouldSell)
                        for (int k = baseCommon.getQuantity(); k > 0; --k)
                            gameboard = sellUnit(gameboard, i, j);
                }
                break;

            case "Thrower":
                // IF FROG PRINCE ALREADY EXISTS
                if (gameboard.getTotalUnits("Frog Prince") == 2) {
                    for(int k = 0; k < baseCommon.getQuantity(); k++)
                        gameboard = sellUnit(gameboard, i, j);
                    break;
                }

                if(i == 1 && j == 5)
                    break;

                String unitIn15 = getSquare(1, 5).getUnit().getName();

                if(!unitIn15.equals("Thrower"))
                    gameboard = gameboard.moveUnit(gameboard, i, j, 1, 5);
                else {
                    for(int k = 0; k < baseCommon.getQuantity(); k++)
                        gameboard = sellUnit(gameboard, i, j);
                }

                break;

// UNCOMMENT FOR BATMAN
//                if(MythicBuilder.canBuild("Bat Man", gameboard)) {
//                    MatchBasic.pressAnywhere();
//                    Thread.sleep(750);
//                    MatchBasic.pressBuildFavoriteMythic();
//                    break;
//                }
//
//                if(i == 2 && j == 3) {
//                    break; // IF UNIT IN MOST OPTIMAL POSITION, DO NOT TOUCH
//                }
//
//                Unit thrower1 = getSquare(2,3).getUnit();
//
//                if(thrower1.getName().equals("Thrower")) {
//                    if(thrower1.getQuantity() == 3){
//                        if(baseCommon.getQuantity() == 3)
//                            mergeUnit(i,j);
//                        else{
//                            for (int k = baseCommon.getQuantity(); k > 0; --k)
//                                gameboard = sellUnit(gameboard, i, j);
//                        }
//                    }else if (thrower1.getQuantity() < baseCommon.getQuantity())
//                        gameboard = gameboard.moveUnit(gameboard,i,j,2,3);
//                } else
//                    gameboard = gameboard.moveUnit(gameboard, i,j,2,3);
//                break;

            case "Barbarian":
                // IF FROG PRINCE ALREADY EXISTS
                if (gameboard.getTotalUnits("Frog Prince") == 2) {
                    for(int k = 0; k < baseCommon.getQuantity(); k++)
                        gameboard = sellUnit(gameboard, i, j);
                    break;
                }

                if(i == 0 && j == 5)
                    break;

                String unitIn05 = getSquare(0, 5).getUnit().getName();

                if(!unitIn05.equals("Barbarian"))
                    gameboard = gameboard.moveUnit(gameboard, i, j, 0, 5);
                else {
                    for(int k = 0; k < baseCommon.getQuantity(); k++)
                        gameboard = sellUnit(gameboard, i, j);
                }
                break;
            case "Water Elemental":
                // IF DRAGON ALREADY EXISTS
                if (gameboard.getTotalUnits("Dragon") == 2) {
                    for(int k = 0; k < baseCommon.getQuantity(); k++)
                        gameboard = sellUnit(gameboard, i, j);
                    break;
                }

                if(i == 2 && j == 5)
                    break;

                String unitIn25 = getSquare(2, 5).getUnit().getName();

                if(!unitIn25.equals("Water Elemental"))
                    gameboard.moveUnit(gameboard, i, j, 2, 5);
                else {
                    for(int k = 0; k < baseCommon.getQuantity(); k++)
                        sellUnit(gameboard, i, j);
                }

                break;
            case "Archer", "Imp":
                if(baseCommon.getQuantity() == 3)
                    mergeUnit(i,j);
                else if(baseCommon.getQuantity() == 2){
                    gameboard = sellUnit(gameboard, i, j);
                    gameboard = sellUnit(gameboard, i, j);
                } else
                    gameboard = sellUnit(gameboard, i, j);
                break;
        }
    }
}
