package Match.Units;


import Match.GameBoard.GameBoard;
import Match.MatchBasic;
import Match.MythicBuilder;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import static Match.GameBoard.GameBoard.*;

public class ProcessUnit {

    /**
     * MAIN FUNCTION
     * Detect units in gameBoardState.json and process.
     * @return true/false
     */
    public static GameBoard DetectUnitPlusProcess(GameBoard gameboard, int i, int j) throws IOException, InterruptedException {

        if (gameboard.getSquare(i, j) != null && gameboard.getSquare(i, j).getUnit() != null) {
            Unit unit = gameboard.getSquare(i, j).getUnit();
                if (unit != null) {
                    String unitName = unit.name;
                    System.out.println("Processing unit: " + unitName + " at position: (" + i + ", " + j + ")");

                    if (isLegendary(unitName)) {
                        gameboard = processUnitByRarity("Legendary", unit, i, j, gameboard);
                    } else if (isEpic(unitName)) {
                        gameboard = processUnitByRarity("Epic", unit, i, j, gameboard);
                    } else if (isRare(unitName)) {
                        gameboard = processUnitByRarity("Rare", unit, i, j, gameboard);
                    } else if (isCommon(unitName)) {
                        gameboard = processUnitByRarity("Common", unit, i, j, gameboard);
                    } else if (isMythic(unitName)) {
                        gameboard = processUnitByRarity("Mythic", unit, i, j, gameboard);
                    } else {
                        System.out.println("No matching rarity for unit: " + unitName);
                    }
                }
            } else
                System.out.println("Square at (" + i + ", " + j + ") is empty or null.");
        return gameboard;
    }

    public static void emergencySell(GameBoard gameBoard) throws IOException, InterruptedException
    {
        Pattern prio_units;
        prio_units = Pattern.compile("Bandit|Thrower|Hunter|Tree|Electro Robot|Tiger Master");
        System.out.println("Performing emergency sell");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 6; j++) {

                Unit unit = gameBoard.getSquare(i,j).getUnit();
                // Ensure the square contains a unit
                if (gameBoard.getSquare(i, j).getUnit() == null) {
                    continue;
                }

                if(!unit.getName().matches(String.valueOf(prio_units)))
                {
                    if (unit.getQuantity() == 3)
                        mergeUnit(i,j);
                    else
                        for(int k = unit.getQuantity(); k > 1; --k)
                            gameBoard = gameBoard.sellUnit(gameBoard,i,j);

                }else if (unit.getName().equals("Electro Robot")){
                    break;
                }else{
                    for(int k = unit.getQuantity(); k > 1; --k)
                        gameBoard = gameBoard.sellUnit(gameBoard,i,j);
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
        return Pattern.compile("Bat Man|Mama|Ninja|Graviton|Orc Shaman|Kitty Mage|Coldy|Blob|Monopoly Man|Frog Prince|Vayne|Lancelot|Iron Meow|Dragon|Bomba|Pulse Generator|Indy|Watt|Tar|Rocket Chu|King Dian|Overclock Rocket Chu").matcher(unitName).find();
    }

    /**
     * Main function to process each unit
     * @param rarity
     * @param unit
     * @param i
     * @param j
     * @throws IOException
     * @throws InterruptedException
     */
    public static GameBoard processUnitByRarity(String rarity, Unit unit, int i, int j, GameBoard gameboard) throws IOException, InterruptedException {
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

        return gameboard;
    }

    /**
     * Check what to do for Mythical Units
     * @param baseMythic
     * @param i
     * @param j
     * @throws IOException
     * @throws InterruptedException
     */
    public static GameBoard processUnitMythic(Unit baseMythic, int i, int j, GameBoard gameboard) throws IOException, InterruptedException{
        switch (baseMythic.getName()) {
            case "Bat Man":

                // CHECK BATMAN FORM, AND IF BAD, THEN UPGRADE
                while (baseMythic.form != 3 && baseMythic.form != 4) // UPGRADE 2 TIMES, TODO : CHANGE THIS ALGORITHM
                {
                    upgradeUnit(i, j);
                    upgradeUnit(i, j);
                    gameboard.updateBoard(i, j);
                    gameboard = ProcessUnit.DetectUnitPlusProcess(gameboard, i, j);
                    baseMythic.setForm(gameboard.getSquare(i, j).getUnit().getForm());
                }


                if(i == 0 && j == 1 || i == 0 && j == 3)
                    break; // IF UNIT IN MOST OPTIMAL POSITION, DO NOT TOUCH

                // CHECK OPTIMAL POSITIONS
                Unit unitBatman1 =  gameboard.getSquare(0, 1).getUnit();
                Unit unitBatman2 = gameboard.getSquare(0, 3).getUnit();
                Unit unitBatman3 = gameboard.getSquare(0, 4).getUnit();

                // MOVE UNITS TO OPTIMAL POSITION IF NO OPTIMAL UNIT FOUND
                if(!unitBatman1.getName().equals("Bat Man"))
                    gameboard = gameboard.moveUnit(gameboard, i, j, 0, 1);
                else if(!unitBatman2.getName().equals("Bat Man"))
                    gameboard = gameboard.moveUnit(gameboard, i, j, 0, 3);
                else if(!unitBatman3.getName().equals("Bat Man"))
                    gameboard = gameboard.moveUnit(gameboard, i, j, 0, 4);
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
                break;
        }
        return gameboard;
    }

    /**
     * Check what to do for Legendary Units
     * @param baseLegendary
     * @param i
     * @param j
     * @throws IOException
     * @throws InterruptedException
     */
    private static GameBoard processUnitLegendary(Unit baseLegendary, int i, int j, GameBoard gameboard) throws IOException, InterruptedException {
        switch (baseLegendary.getName()) {
            case "War Machine", "Storm Giant", "Sheriff":
                for(int k = baseLegendary.getQuantity(); k > 0; k--)
                    gameboard = sellUnit(gameboard, i, j);
                break;
            case "Tiger Master":
//                if(MythicBuilder.canBuild("Bat Man", gameboard)) {
//                    MatchBasic.pressAnywhere();
//                    Thread.sleep(750);
//                    MatchBasic.pressBuildFavoriteMythic();
//                    break;
//                }

                if(i == 0 && j == 0)
                    break; // IF UNIT IN MOST OPTIMAL POSITION, DO NOT MOVE

                Unit tigerMaster1 = gameboard.getSquare(0, 0).getUnit();
                if(tigerMaster1.getName().equals("Tiger Master")) {
                    if(tigerMaster1.getQuantity() == 3) {
                        if(baseLegendary.getQuantity() == 3)
                            mergeUnit(i, j);
                        else
                            for (int k = tigerMaster1.getQuantity(); k > 0; --k)
                                gameboard = sellUnit(gameboard, i, j);
                    } else if(tigerMaster1.getQuantity() < baseLegendary.getQuantity())
                        gameboard = gameboard.moveUnit(gameboard, i, j, 0, 0);
                } else
                    gameboard = gameboard.moveUnit(gameboard, i, j, 0, 0);
                break;

            default:
                System.out.println("No specific handling for: " + baseLegendary.getName());
        }

        return gameboard;
    }

    /**
     * Check what to do for Epic Units
     * @param baseEpic
     * @param i
     * @param j
     */
    public static GameBoard processUnitEpic(Unit baseEpic, int i, int j, GameBoard gameboard) throws IOException, InterruptedException {
        switch (baseEpic.getName()){
            case "Wolf Warrior", "Eagle General":
                if (baseEpic.getQuantity() == 3){
                    mergeUnit(i,j);
                } else if(baseEpic.getQuantity() == 2) {
                    break;
                } else if(baseEpic.getQuantity() == 1){
                    gameboard = sellUnit(gameboard, i, j);
                }
                break;

            case "Electro Robot":
                if(i == 0 && j == 2)
                    break; // IF UNIT IN OPTIMAL POSITION, DO NOT TOUCH

                Unit electroRobot1 = gameboard.getSquare(0,2).getUnit();
                if(electroRobot1.getName().equals("Electro Robot")) {
                    if(electroRobot1.getQuantity() == 3){
                        if(baseEpic.getQuantity() == 3)
                            mergeUnit(i,j);
                        else if(baseEpic.getQuantity() == 1)
                            gameboard = sellUnit(gameboard, i, j);
                    } else if (electroRobot1.getQuantity() < baseEpic.getQuantity())
                        gameboard = gameboard.moveUnit(gameboard, i,j,0,2);
                } else
                    gameboard = gameboard.moveUnit(gameboard, i,j,0,2);
                break;

            case "Hunter":
                if((i == 0 && j == 5) || (i == 1 && j == 5))
                    break; // IF UNIT IN OPTIMAL POSITION, DO NOT TOUCH

                Unit Hunter1 = gameboard.getSquare(0,5).getUnit();
                Unit Hunter2 = gameboard.getSquare(1,5).getUnit();
                boolean canMerge = false;
                boolean shouldMove = false;
                boolean shouldSell = false;
                boolean processed = false;

                if(Hunter1.getName().equals("Hunter")) {
                    if(Hunter1.getQuantity() == 3){
                        if(baseEpic.getQuantity() == 3)
                            canMerge = true;
                        else if(baseEpic.getQuantity() == 1)
                            shouldSell = true;
                    } else if(Hunter1.getQuantity() < baseEpic.getQuantity())
                        shouldMove = true;
                } else {
                    gameboard = gameboard.moveUnit(gameboard, i,j,0,5);
                    processed = true;
                }

                if(!processed){
                    if(Hunter2.getName().equals("Hunter")) {
                        if(Hunter2.getQuantity() == 3){
                            if(baseEpic.getQuantity() == 3)
                                canMerge = true;
                            else if(baseEpic.getQuantity() == 1)
                                shouldSell = true;
                        } else if (Hunter2.getQuantity() < baseEpic.getQuantity())
                            shouldMove = true;
                    } else {
                        gameboard = gameboard.moveUnit(gameboard, i, j, 1, 5);
                        processed = true;
                        break;
                    }
                }

                if(shouldMove){
                    if(Hunter1.getQuantity() < Hunter2.getQuantity())
                        gameboard = gameboard.moveUnit(gameboard, i, j, 0, 5);
                    else
                        gameboard = gameboard.moveUnit(gameboard, i, j, 1, 5);
                } else {
                    if(canMerge)
                        mergeUnit(i,j);
                    else if(shouldSell)
                        for (int k = baseEpic.getQuantity(); k > 0; --k)
                            gameboard = sellUnit(gameboard, i, j);
                }
                break;
            case "Tree":
//                if(MythicBuilder.canBuild("Bat Man", gameboard)) {
//                    MatchBasic.pressAnywhere();
//                    Thread.sleep(750);
//                    MatchBasic.pressBuildFavoriteMythic();
//                    break;
//                }

                if((i == 1 && j == 3))
                    break; // IF UNIT IN OPTIMAL POSITION, DO NOT TOUCH

                Unit tree1 = gameboard.getSquare(1,3).getUnit();
                if(tree1.getName().equals("Tree")) {
                    if(tree1.getQuantity() == 3){
                        if(baseEpic.getQuantity() == 3)
                            mergeUnit(i, j);
                        else if(baseEpic.getQuantity() == 1)
                            gameboard = sellUnit(gameboard, i ,j);
                    } else if(tree1.getQuantity() < baseEpic.getQuantity())
                        gameboard = gameboard.moveUnit(gameboard, i, j, 1, 3);
                } else {
                    gameboard = gameboard.moveUnit(gameboard, i,j,1,3);
                }
                break;
            default:
                System.out.println("No specific handling for this " + baseEpic.getName());
                break;
        }
        return gameboard;
    }

    /**
     * Check what to do for Rare/Uncommon Units
     * @param baseRare
     * @param i
     * @param j
     */
    public static GameBoard processUnitRare(Unit baseRare, int i, int j, GameBoard gameboard) throws IOException, InterruptedException {
        switch (baseRare.getName()){
            case "Ranger", "Sandman", "Shock Robot", "Paladin", "Demon Soldier":
                if(baseRare.getQuantity() == 3)
                    mergeUnit(i,j);
                else {
                    for (int k = baseRare.getQuantity(); k > 0; --k)
                        gameboard = sellUnit(gameboard, i, j);
                }
                break;
            default:
                System.out.println("No specific handling for: " + baseRare.getName());
        }
        return gameboard;
    }

    /**
     * Check what to do for Common Units
     * @param baseCommon
     * @param i
     * @param j
     */
    public static GameBoard processUnitCommon(Unit baseCommon, int i, int j, GameBoard gameboard) throws IOException, InterruptedException {
        switch (baseCommon.getName()){
            case "Bandit":
                if((i == 1 && j == 0) || (i == 2 && j == 0)) {
                    break; // IF UNIT IN MOST OPTIMAL POSITION, DO NOT TOUCH
                }

                boolean canMerge = false;
                boolean shouldMove = false;
                boolean shouldSell = false;
                boolean processed = false;
                Unit bandit1 = gameboard.getSquare(1,0).getUnit();
                Unit bandit2 = gameboard.getSquare(2,0).getUnit();

                if(bandit1.getName().equals("Bandit")) {
                    if(bandit1.getQuantity() == 3){
                        if(baseCommon.getQuantity() == 3)
                            canMerge = true;
                        else
                            shouldSell = true;
                    }else if (bandit1.getQuantity() < baseCommon.getQuantity())
                        shouldMove = true;
                }else{
                    gameboard = gameboard.moveUnit(gameboard, i,j,1,0);
                    processed = true;
                    break;
                }

                if(!processed){
                    if(bandit2.getName().equals("Bandit")) {
                        if(bandit2.getQuantity() == 3){
                            if(baseCommon.getQuantity() == 3)
                                canMerge = true;
                            else
                                shouldSell = true;
                        }else if (bandit2.getQuantity() < baseCommon.getQuantity())
                            shouldMove = true;
                    }else{
                        gameboard = gameboard.moveUnit(gameboard, i, j, 2, 0);
                        processed = true;
                        break;
                    }
                }

                if(shouldMove){
                    if(bandit1.getQuantity() < bandit2.getQuantity())
                        gameboard = gameboard.moveUnit(gameboard, i, j, 1, 0);
                    else
                        gameboard = gameboard.moveUnit(gameboard, i, j, 2, 0);
                } else {
                    if (canMerge)
                        mergeUnit(i, j);
                    else if (shouldSell)
                        for (int k = baseCommon.getQuantity(); k > 0; --k)
                            gameboard = sellUnit(gameboard, i, j);
                }
                break;

            case "Thrower":
//                if(MythicBuilder.canBuild("Bat Man", gameboard)) {
//                    MatchBasic.pressAnywhere();
//                    Thread.sleep(750);
//                    MatchBasic.pressBuildFavoriteMythic();
//                    break;
//                }

                if(i == 2 && j == 3) {
                    break; // IF UNIT IN MOST OPTIMAL POSITION, DO NOT TOUCH
                }

                Unit thrower1 = gameboard.getSquare(2,3).getUnit();

                if(thrower1.getName().equals("Thrower")) {
                    if(thrower1.getQuantity() == 3){
                        if(baseCommon.getQuantity() == 3)
                            mergeUnit(i,j);
                        else{
                            for (int k = baseCommon.getQuantity(); k > 0; --k)
                                gameboard = sellUnit(gameboard, i, j);
                        }
                    }else if (thrower1.getQuantity() < baseCommon.getQuantity())
                        gameboard = gameboard.moveUnit(gameboard,i,j,2,3);
                } else
                    gameboard = gameboard.moveUnit(gameboard, i,j,2,3);
                break;

            case "Archer", "Barbarian", "Water Elemental","Imp":
                if(baseCommon.getQuantity() == 3)
                    mergeUnit(i,j);
                else if(baseCommon.getQuantity() == 2){
                    gameboard = sellUnit(gameboard, i, j);
                    gameboard = sellUnit(gameboard, i, j);
                } else
                    gameboard = sellUnit(gameboard, i, j);
                break;
        }
        return gameboard;
    }
}