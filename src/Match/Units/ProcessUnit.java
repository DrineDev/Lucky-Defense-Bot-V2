package Match.Units;


import Match.GameBoard.GameBoard;
import Match.MythicBuilder;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import static Match.GameBoard.GameBoard.*;

public class ProcessUnit {

    public static GameBoard gameboard = new GameBoard();

    /**
     * MAIN FUNCTION
     * Detect units in gameBoardState.json and process.
     * @return
     */
    public static Boolean DetectUnitPlusProcess() {
        Gson gson = new Gson();
        String filePath = "Resources/gameBoardState.json";

        try (FileReader reader = new FileReader(filePath)) {
            System.out.println("Attempting to read file: " + filePath);

            // Convert JSON to GameBoard object
            gameboard = gson.fromJson(reader, GameBoard.class);

            // Loop through gameBoard to process the units
            for (int i = 0; i < 3; i++) { // 3 rows
                for (int j = 0; j < 6; j++) { // 6 columns
                    if (gameboard.getSquare(i, j) != null && gameboard.getSquare(i, j).getUnit() != null) {
                        Unit unit = gameboard.getSquare(i, j).getUnit();
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

    /**
     * FUNCTIONS to detect a unit rarity
     * @param unitName
     * @return
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


    /**
     * Check what to do for Mythical Units
     * @param baseMythic
     * @param i
     * @param j
     * @throws IOException
     * @throws InterruptedException
     */
    public static void processUnitMythic(Unit baseMythic, int i, int j) throws IOException, InterruptedException{
        switch (baseMythic.getName()) {
            case "BatMan":
                if(i == 0 && j == 1 || i == 0 && j == 3)
                    break; // IF UNIT IN MOST OPTIMAL POSITION, DO NOT TOUCH

                // CHECK OPTIMAL POSITIONS
                MythicalUnit unitBatman1 = (MythicalUnit) gameboard.getSquare(0, 1).getUnit();
                MythicalUnit unitBatman2 = (MythicalUnit) gameboard.getSquare(0, 3).getUnit();
                MythicalUnit unitBatman3 = (MythicalUnit) gameboard.getSquare(0, 4).getUnit();

                // MOVE UNITS TO OPTIMAL POSITION IF NO OPTIMAL UNIT FOUND
                if(!unitBatman1.getName().equals("BatMan"))
                    gameboard = gameboard.moveUnit(gameboard, i, j, 0, 1);
                else if(!unitBatman2.getName().equals("BatMan"))
                    gameboard = gameboard.moveUnit(gameboard, i, j, 0, 3);
                else if(!unitBatman3.getName().equals("BatMan"))
                    gameboard = gameboard.moveUnit(gameboard, i, j, 0, 4);

                // CHECK BATMAN FORM, AND IF BAD, THEN UPGRADE
                MythicalUnit unitBatman = (MythicalUnit) baseMythic;
                if(unitBatman.getForm() != 3)
                    for(int k = 0; k < 5; k++)
                        upgradeUnit(i, j); // UPGRADE FIVE TIMES, TODO : CHANGE THIS ALGORITHM
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

    }

    /**
     * Check what to do for Legendary Units
     * @param baseLegendary
     * @param i
     * @param j
     * @throws IOException
     * @throws InterruptedException
     */
    public static void processUnitLegendary(Unit baseLegendary, int i, int j) throws IOException, InterruptedException {
        switch (baseLegendary.getName()) {
            case "War Machine":
                break;
            case "Storm Giant":
                break;
            case "Sheriff":
                break;

            case "Tiger Master":
                if(i == 0 && j == 0)
                    break; // IF UNIT IN MOST OPTIMAL POSITION, DO NOT MOVE
                // TODO : CHECK IF BATMAN CAN BE BUILT..
                break;
            default:
                System.out.println("No specific handling for: " + baseLegendary.getName());
        }
    }

    /**
     * Check what to do for Epic Units
     * @param baseEpic
     * @param i
     * @param j
     */
    public static void processUnitEpic(Unit baseEpic, int i, int j) throws IOException, InterruptedException {
        switch (baseEpic.getName()){
            case "Wolf Warrior", "Eagle General":
                if (baseEpic.getQuantity() == 3){
                    mergeUnit(i,j);
                } else if(baseEpic.getQuantity() == 2) {
                    gameboard = sellUnit(gameboard, i, j);
                    gameboard = sellUnit(gameboard, i, j);
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
                        else
                            for (int k = electroRobot1.getQuantity(); k > 0; --k)
                                gameboard = sellUnit(gameboard, i, j);
                    } else if (electroRobot1.getQuantity() < electroRobot1.getQuantity())
                        gameboard = gameboard.moveUnit(gameboard, i,j,0,2);
                } else
                    gameboard = gameboard.moveUnit(gameboard, i,j,0,2);
                break;

            case "Hunter":
                if((i == 0 && j == 5) || (i == 1 && j == 5) || (i == 2 & j == 5))
                    break; // IF UNIT IN MOST OPTIMAL POSITION, DO NOT TOUCH

                boolean canMerge = false;
                boolean shouldMove = false;
                boolean shouldSell = false;
                boolean processed = false;
                boolean processed2 = false;
                Unit hunter1 = gameboard.getSquare(0,5).getUnit();
                Unit hunter2 = gameboard.getSquare(1,5).getUnit();
                Unit hunter3 = gameboard.getSquare(2,5).getUnit();

                if(hunter1.getName().equals("Hunter")) {
                    if(hunter1.getQuantity() == 3){
                        if(baseEpic.getQuantity() == 3)
                            canMerge = true;
                        else
                            shouldSell = true;
                    } else if (hunter1.getQuantity() < baseEpic.getQuantity())
                        shouldMove = true;
                }else{
                    gameboard = gameboard.moveUnit(gameboard, i,j,0,5);
                    processed = true;
                    break;
                }

                if(!processed){
                    if(hunter2.getName().equals("Hunter")) {
                        if(hunter2.getQuantity() == 3){
                            if(baseEpic.getQuantity() == 3)
                                canMerge = true;
                            else
                                shouldSell = true;
                        }else if (hunter2.getQuantity() < baseEpic.getQuantity())
                            shouldMove = true;
                    }else{
                        gameboard = gameboard.moveUnit(gameboard, i, j, 1, 5);
                        processed2 = true;
                        break;
                    }
                }

                if(!processed2){
                    if(hunter3.getName().equals("Hunter")) {
                        if(hunter3.getQuantity() == 3){
                            if(baseEpic.getQuantity() == 3)
                                canMerge = true;
                            else
                                shouldSell = true;
                        }else if (hunter3.getQuantity() < baseEpic.getQuantity())
                            shouldMove = true;
                    }else{
                        gameboard = gameboard.moveUnit(gameboard, i, j, 2, 5);
                        break;
                    }
                }

                if(shouldMove) {
                    if(hunter1.getQuantity() < hunter2.getQuantity() && hunter1.getQuantity() < hunter3.getQuantity())
                        gameboard = gameboard.moveUnit(gameboard, i, j, 0, 5);
                    else if(hunter2.getQuantity() < hunter1.getQuantity() && hunter2.getQuantity() < hunter3.getQuantity())
                        gameboard = gameboard.moveUnit(gameboard, i, j, 1, 5);
                    else if(hunter3.getQuantity() < hunter1.getQuantity() && hunter3.getQuantity() < hunter2.getQuantity())
                        gameboard = gameboard.moveUnit(gameboard,i,j,2,5);
                } else {
                    if(canMerge)
                        mergeUnit(i,j);
                    else if(shouldSell)
                        for (int k = baseEpic.getQuantity(); k >0; --k)
                            gameboard = sellUnit(gameboard, i, j);
                }
                break;
            case "Tree":
                if((i == 1 && j == 3) || (i == 1 && j == 4))
                    break; // IF UNIT IN OPTIMAL POSITION, DO NOT TOUCH

                Unit tree1 = gameboard.getSquare(1,3).getUnit();
                Unit tree2 = gameboard.getSquare(1,4).getUnit();
                canMerge = false;
                shouldMove = false;
                shouldSell = false;
                processed = false;

                if(tree1.getName().equals("Tree")) {
                    if(tree1.getQuantity() == 3){
                        if(baseEpic.getQuantity() == 3)
                            canMerge = true;
                        else
                            shouldSell = true;
                    } else if(tree1.getQuantity() < baseEpic.getQuantity())
                        shouldMove = true;
                } else {
                    gameboard = gameboard.moveUnit(gameboard, i,j,1,3);
                    processed = true;
                }

                if(!processed){
                    if(tree2.getName().equals("Tree")) {
                        if(tree2.getQuantity() == 3){
                            if(baseEpic.getQuantity() == 3)
                                canMerge = true;
                            else
                                shouldSell = true;
                        } else if (tree2.getQuantity() < baseEpic.getQuantity())
                            shouldMove = true;
                    } else {
                        gameboard = gameboard.moveUnit(gameboard, i, j, 1, 4);
                        processed = true;
                        break;
                    }
                }

                if(shouldMove){
                    if(tree1.getQuantity() < tree2.getQuantity())
                        gameboard = gameboard.moveUnit(gameboard, i, j, 1, 3);
                    else
                        gameboard = gameboard.moveUnit(gameboard, i, j, 1, 4);
                } else {
                    if(canMerge)
                        mergeUnit(i,j);
                    else if(shouldSell)
                        for (int k = baseEpic.getQuantity(); k > 0; --k)
                            gameboard = sellUnit(gameboard, i, j);
                }
                break;
            default:
                System.out.println("No specific handling for this " + baseEpic.getName());
                break;
        }
    }

    /**
     * Check what to do for Rare/Uncommon Units
     * @param baseRare
     * @param i
     * @param j
     */
    public static void processUnitRare(Unit baseRare, int i, int j) throws IOException, InterruptedException {
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
    }

    /**
     * Check what to do for Common Units
     * @param baseCommon
     * @param i
     * @param j
     */
    public static void processUnitCommon(Unit baseCommon, int i, int j) throws IOException, InterruptedException {
        switch (baseCommon.getName()){
            case "Bandit":
                if((i == 1 && j == 0) || (i == 2 && j == 0))
                    break; // IF UNIT IN MOST OPTIMAL POSITION, DO NOT TOUCH

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
                if(i == 2 && j == 3)
                    break; // IF UNIT IN MOST OPTIMAL POSITION, DO NOT TOUCH

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
    }
}