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


    /**
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
                    sellUnit(gameboard, i, j);
                }
                break;
            case "Storm Giant":
                if (MythicBuilder.canBuild("Coldy")) {
                    MythicBuilder.buildMythic("Coldy");
                } else {
                    sellUnit(gameboard, i, j);
                }
                break;
            case "Sheriff":
                if (MythicBuilder.canBuild("Lancelot")) {
                    MythicBuilder.buildMythic("Lancelot");
                } else {
                    sellUnit(gameboard, i, j);
                }
                break;
            case "Tiger Master":
                if (MythicBuilder.canBuild("Batman")) {
                    MythicBuilder.buildMythic("Batman");
                } else {
                    gameboard.moveUnit(gameboard, i,j,0,1);
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
    public static void processUnitEpic(Unit unit, int i, int j) throws IOException, InterruptedException {
        String unitName = unit.name;
        switch (unitName){
            case "Wolf Warrior", "Eagle General":
                if (unit.quantity==3){
                    mergeUnit(i,j);
                }else if(unit.quantity==2){
                    sellUnit(gameboard, i, j);
                    sellUnit(gameboard, i, j);
                }else if(unit.quantity==1){
                    sellUnit(gameboard, i, j);
                }
                break;
            case "Electro Robot":
                Unit unit1 = gameboard.getSquare(0,2).getUnit();
                if(unit1.name.equals("Electro Robot")) {
                    if(unit1.quantity==3){
                        if(unit.quantity==3)
                            mergeUnit(i,j);
                        else
                            for (int Enum = unit1.getQuantity(); Enum >0; --Enum)
                                sellUnit(gameboard, i, j);
                    }else if (unit1.quantity< unit.quantity) gameboard.moveUnit(gameboard, i,j,0,2);
                }else{
                    gameboard.moveUnit(gameboard, i,j,0,2);
                }
                break;
            case "Hunter":
                Unit unit1H = gameboard.getSquare(0,5).getUnit();
                Unit unit2H = gameboard.getSquare(1,5).getUnit();
                Unit unit3H = gameboard.getSquare(2,5).getUnit();
                boolean canMerge = false;
                boolean shouldMove = false;
                boolean shouldSell = false;
                boolean processed = false;
                boolean processed2 = false;
                if(unit1H.name.equals("Tree")) {
                    if(unit1H.quantity==3){
                        if(unit.quantity==3)
                            canMerge = true;
                        else
                            shouldSell = true;
                    }else if (unit1H.quantity< unit.quantity)
                        shouldMove = true;
                }else{
                    gameboard.moveUnit(gameboard, i,j,0,5);
                    processed = true;
                    break;
                }

                if(!processed){
                    if(unit2H.name.equals("Tree")) {
                        if(unit2H.quantity==3){
                            if(unit.quantity==3)
                                canMerge = true;
                            else
                                shouldSell = true;
                        }else if (unit2H.quantity< unit.quantity)
                            shouldMove = true;
                    }else{
                        gameboard.moveUnit(gameboard, i, j, 1, 5);
                        processed2 = true;
                        break;
                    }
                }

                if(!processed2){
                    if(unit3H.name.equals("Tree")) {
                        if(unit3H.quantity==3){
                            if(unit.quantity==3)
                                canMerge = true;
                            else
                                shouldSell = true;
                        }else if (unit3H.quantity< unit.quantity)
                            shouldMove = true;
                    }else{
                        gameboard.moveUnit(gameboard, i, j, 2, 5);
                        break;
                    }
                }

                if(shouldMove){
                    if(unit1H.quantity< unit2H.quantity && unit1H.quantity<unit3H.quantity)
                        gameboard.moveUnit(gameboard, i, j, 0, 5);
                    else if(unit2H.quantity<unit1H.quantity&& unit2H.quantity<unit3H.quantity)
                        gameboard.moveUnit(gameboard, i, j, 1, 5);
                    else if (unit3H.quantity<unit1H.quantity&&unit3H.quantity<unit2H.quantity)
                        gameboard.moveUnit(gameboard,i,j,2,5);
                }else{
                    if(canMerge)
                        mergeUnit(i,j);
                    else if(shouldSell)
                        for (int Enum = unit.getQuantity(); Enum >0; --Enum)
                            sellUnit(gameboard, i, j);
                    break;
                }
                break;
            case "Tree":
                Unit unit1T = gameboard.getSquare(1,3).getUnit();
                Unit unit2T = gameboard.getSquare(1,4).getUnit();
                canMerge = false;
                shouldMove = false;
                shouldSell = false;
                processed = false;
                if(unit1T.name.equals("Tree")) {
                    if(unit1T.quantity==3){
                        if(unit.quantity==3)
                            canMerge = true;
                        else
                            shouldSell = true;
                    }else if (unit1T.quantity< unit.quantity)
                        shouldMove = true;
                }else{
                    gameboard.moveUnit(gameboard, i,j,1,3);
                    processed = true;
                    break;
                }

                if(!processed){
                    if(unit2T.name.equals("Tree")) {
                        if(unit2T.quantity==3){
                            if(unit.quantity==3)
                                canMerge = true;
                            else
                                shouldSell = true;
                        }else if (unit2T.quantity< unit.quantity)
                            shouldMove = true;
                    }else{
                        gameboard.moveUnit(gameboard, i, j, 1, 4);
                        processed = true;
                        break;
                    }
                }

                if(shouldMove){
                    if(unit1T.quantity< unit2T.quantity)
                        gameboard.moveUnit(gameboard, i, j, 1, 3);
                    else
                        gameboard.moveUnit(gameboard, i, j, 1, 4);
                }else{
                    if(canMerge)
                        mergeUnit(i,j);
                    else if(shouldSell)
                        for (int Enum = unit.getQuantity(); Enum >0; --Enum)
                            sellUnit(gameboard, i, j);
                    break;
                }
                break;
            default:
                System.out.println("No specific handling for this "+unitName);
                break;

        }
    }

    /**
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
                    for (int Enum = unit.quantity; Enum >0; --Enum)
                        sellUnit(gameboard, i, j);
                }
                break;
            default:
                System.out.println("No specific handling for: " + unitName);
        }
    }

    /**
     * Check what to do for Common Units
     * @param unit
     * @param i
     * @param j
     */
    public static void processUnitCommon(Unit unit, int i, int j) throws IOException, InterruptedException {
        String unitName = unit.name;
        switch (unitName){
            case "Bandit":
                Unit unit1 = gameboard.getSquare(1,0).getUnit();
                Unit unit2 = gameboard.getSquare(2,0).getUnit();
                boolean canMerge = false;
                boolean shouldMove = false;
                boolean shouldSell = false;
                boolean processed = false;
                if(unit1.name.equals("Bandit")) {
                    if(unit1.quantity==3){
                        if(unit.quantity==3)
                            canMerge = true;
                        else
                            shouldSell = true;
                    }else if (unit1.quantity< unit.quantity)
                        shouldMove = true;
                }else{
                    gameboard.moveUnit(gameboard, i,j,1,0);
                    processed = true;
                    break;
                }

                if(!processed){
                    if(unit2.name.equals("Bandit")) {
                        if(unit2.quantity==3){
                            if(unit.quantity==3)
                                canMerge = true;
                            else
                                shouldSell = true;
                        }else if (unit2.quantity< unit.quantity)
                            shouldMove = true;
                    }else{
                        gameboard.moveUnit(gameboard, i, j, 2, 0);
                        processed = true;
                        break;
                    }
                }

                if(shouldMove){
                    if(unit1.quantity<unit2.quantity)
                        gameboard.moveUnit(gameboard, i, j, 1, 0);
                    else
                        gameboard.moveUnit(gameboard, i, j, 2, 0);
                }else{
                    if(canMerge)
                        mergeUnit(i,j);
                    else if(shouldSell)
                        for (int Enum = unit.getQuantity(); Enum >0; --Enum)
                            sellUnit(gameboard, i, j);
                        break;
                }
                break;
            case "Thrower":
                unit1 = gameboard.getSquare(2,3).getUnit();

                if(unit1.name.equals("Thrower")) {
                    if(unit1.quantity==3){
                        if(unit.quantity==3)
                            mergeUnit(i,j);
                        else{
                            for (int Enum = unit.getQuantity(); Enum >0; --Enum)
                                sellUnit(gameboard, i, j);
                        }
                    }else if (unit1.quantity<unit.quantity)
                        gameboard.moveUnit(gameboard,i,j,2,3);
                }else{
                    gameboard.moveUnit(gameboard, i,j,2,3);
                    break;
                }
                break;
            case "Archer", "Barbarian", "Water Elemental","Imp":
                if(unit.quantity==3)
                    mergeUnit(i,j);
                else if(unit.quantity==2){
                    sellUnit(gameboard, i, j);
                    sellUnit(gameboard, i, j);
                }
                else{
                    sellUnit(gameboard, i, j);
                }
                break;


        }
    }
}