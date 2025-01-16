package Match.Units;

import Match.GameBoard.GameBoard;
import Match.PlayGame;
import static Logger.Logger.log;
import static Match.GameBoard.GameBoard.*;

import java.io.IOException;
import java.util.*;

public class ProcessUnit {
    private enum UnitRarity {
        MYTHIC, LEGENDARY, EPIC, RARE, COMMON
    }

    private static class Position {
        final int x;
        final int y;
        final int priority;
        Position(int x, int y, int priority) {
            this.x = x;
            this.y = y;
            this.priority = priority;
        }
    }

    private static final Map<String, UnitRarity> unitRarityMap = new HashMap<>();
    private static final Map<String, List<Position>> optimalPositions = new HashMap<>();

    static {
        initializeUnitRarities();
        initializeOptimalPositions();
    }

    private static void initializeUnitRarities() {
        // Same as before...
        String[] mythicUnits = {"Bat Man", "Mama", "Ninja", "Graviton", "Orc Shaman",
                "Kitty Mage", "Coldy", "Blob", "Monopoly Man", "Frog Prince", "Vayne",
                "Lancelot", "Iron Meow(0)", "Iron Meow(1)", "Iron Meow(2)", "Dragon",
                "Drain", "Bomba", "Pulse Generator", "Indy", "Watt", "Tar", "Rocket Chu",
                "King Dian", "Overclock Rocket Chu", "Lazy Taoist", "Zap"};

        String[] legendaryUnits = {"War Machine", "Storm Giant", "Sheriff", "Tiger Master"};
        String[] epicUnits = {"Electro Robot", "Tree", "Wolf Warrior", "Hunter", "Eagle General"};
        String[] rareUnits = {"Ranger", "Sandman", "Shock Robot", "Paladin", "Demon Soldier"};
        String[] commonUnits = {"Archer", "Barbarian", "Bandit", "Water Elemental", "Thrower"};

        for (String unit : mythicUnits) unitRarityMap.put(unit, UnitRarity.MYTHIC);
        for (String unit : legendaryUnits) unitRarityMap.put(unit, UnitRarity.LEGENDARY);
        for (String unit : epicUnits) unitRarityMap.put(unit, UnitRarity.EPIC);
        for (String unit : rareUnits) unitRarityMap.put(unit, UnitRarity.RARE);
        for (String unit : commonUnits) unitRarityMap.put(unit, UnitRarity.COMMON);
    }

    private static void initializeOptimalPositions() {
        // Initialize with lists of positions in priority order
        optimalPositions.put("Frog Prince", Arrays.asList(
                new Position(0, 0, 1),
                new Position(0, 1, 2)
        ));

        optimalPositions.put("Dragon", Arrays.asList(
                new Position(1, 1, 1),
                new Position(1, 2, 2)
        ));

        optimalPositions.put("Bandit", Arrays.asList(
                new Position(1, 0, 1),
                new Position(2, 0, 2)
        ));

        optimalPositions.put("Electro Robot", Arrays.asList(
                new Position(0, 2, 1)
        ));

        optimalPositions.put("Tree", Arrays.asList(
                new Position(1, 4, 1)
        ));

        optimalPositions.put("Wolf Warrior", Arrays.asList(
                new Position(0, 4, 1)
        ));

        optimalPositions.put("Eagle General", Arrays.asList(
                new Position(2, 4, 1)
        ));

        optimalPositions.put("Thrower", Arrays.asList(
                new Position(1, 5, 1)
        ));

        optimalPositions.put("Barbarian", Arrays.asList(
                new Position(0, 5, 1)
        ));

        optimalPositions.put("Water Elemental", Arrays.asList(
                new Position(2, 5, 1)
        ));

        optimalPositions.put("Shock Robot", Arrays.asList(
                new Position(0, 3, 1)
        ));
    }

    public static void DetectUnitPlusProcess(GameBoard gameboard, int i, int j) throws IOException, InterruptedException {
        Unit unit = getSquare(i, j).getUnit();
        if (unit == null) return;

        String unitName = unit.name;
        UnitRarity rarity = unitRarityMap.get(unitName);

        if (rarity == null) {
            log("Unknown unit type: " + unitName);
            return;
        }

        processUnitByStrategy(unit, i, j, gameboard, rarity);
    }

    private static void processUnitByStrategy(Unit unit, int i, int j, GameBoard gameboard, UnitRarity rarity)
            throws IOException, InterruptedException {

        List<Position> positions = optimalPositions.get(unit.getName());
        boolean actionTaken = false;

        if (shouldSellBasedOnDependencies(unit, gameboard)) {
            sellUnitMultipleTimes(gameboard, i, j, unit.getQuantity());
            actionTaken = true;
        } else if (positions != null) {
            actionTaken = handleMultipleOptimalPositions(unit, i, j, positions, gameboard);
        } else {
            actionTaken = handleByRarity(unit, i, j, gameboard, rarity);
        }

        // Only check for rewards and golem if we actually did something
//        if (actionTaken) {
//            PlayGame.checkForRewards();
//            PlayGame.waitForGolem();
//        }
    }

    private static boolean handleMultipleOptimalPositions(Unit unit, int i, int j, List<Position> positions, GameBoard gameboard)
            throws IOException, InterruptedException {

        // Check if unit is already in any of its optimal positions
        for (Position pos : positions) {
            if (i == pos.x && j == pos.y) {
                return false; // Already in position, no action taken
            }
        }

        // Try to move to the highest priority position first
        for (Position pos : positions) {
            Unit targetUnit = getSquare(pos.x, pos.y).getUnit();

            if (!targetUnit.getName().equals(unit.getName())) {
                // Position is free or occupied by different unit
                gameboard.moveUnit(gameboard, i, j, pos.x, pos.y);
                return true;
            } else {
                // Same unit type in this position
                if (targetUnit.getQuantity() < unit.getQuantity()) {
                    // Current unit has more quantity, should replace
                    gameboard.moveUnit(gameboard, i, j, pos.x, pos.y);
                    return true;
                }
            }
        }

        // If we get here, all optimal positions are occupied by same or better units
        if (unit.getQuantity() == 3) {
            mergeUnit(i, j);
            return true;
        } else {
            sellUnitMultipleTimes(gameboard, i, j, unit.getQuantity());
            return true;
        }
    }

    private static boolean handleByRarity(Unit unit, int i, int j, GameBoard gameboard, UnitRarity rarity)
            throws IOException, InterruptedException {
        return switch (rarity) {
            case MYTHIC -> handleMythicUnit(unit, i, j, gameboard);
            case LEGENDARY -> {
                sellUnitMultipleTimes(gameboard, i, j, unit.getQuantity());
                yield true;
            }
            case EPIC -> handleEpicUnit(unit, i, j, gameboard);
            case RARE, COMMON -> handleBasicUnit(unit, i, j, gameboard);
            default -> false;
        };
    }

    private static boolean handleBasicUnit(Unit unit, int i, int j, GameBoard gameboard)
            throws IOException, InterruptedException {
        if (unit.getQuantity() == 3) {
            mergeUnit(i, j);
            return true;
        } else {
            sellUnitMultipleTimes(gameboard, i, j, unit.getQuantity());
            return true;
        }
    }

    private static boolean handleMythicUnit(Unit unit, int i, int j, GameBoard gameboard)
            throws IOException, InterruptedException {
        // Add specific mythic unit logic if needed
        return false; // Return true if action taken
    }

    private static boolean handleEpicUnit(Unit unit, int i, int j, GameBoard gameboard)
            throws IOException, InterruptedException {
        if (unit.getName().equals("Hunter")) {
            gameboard = sellUnit(gameboard, i, j);
            return true;
        }
        return false;
    }

    public static void emergencySell(GameBoard gameBoard) throws IOException, InterruptedException {
        boolean actionTaken = false;
        String[] priorityUnits = {"Bandit", "Thrower", "Barbarian", "Water Elemental",
                "Shock Robot", "Wolf Warrior", "Tree", "Electro Robot", "Eagle General"};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 6; j++) {
                Unit unit = getSquare(i,j).getUnit();
                if (unit == null) continue;

                boolean isPriority = false;
                for (String priorityUnit : priorityUnits) {
                    if (unit.getName().equals(priorityUnit)) {
                        isPriority = true;
                        break;
                    }
                }

                if (!isPriority) {
                    if (unit.getQuantity() == 3) {
                        mergeUnit(i,j);
                        actionTaken = true;
                    } else {
                        sellUnitMultipleTimes(gameBoard, i, j, unit.getQuantity() - 1);
                        actionTaken = true;
                    }
                }
            }
        }

        if (actionTaken) {
            PlayGame.checkForRewards();
            PlayGame.waitForGolem();
        }
    }

    public static void sellUnitMultipleTimes(GameBoard gameBoard, int i, int j, int quantity) throws IOException, InterruptedException {
        for(int k = 0; k < quantity; k++) {
            gameBoard = sellUnit(gameBoard, i, j);
        }
    }

    private static boolean shouldSellBasedOnDependencies(Unit unit, GameBoard gameboard) {
        switch (unit.getName()) {
            case "Wolf Warrior":
            case "Tree":
                return getTotalUnits("Frog Prince") == 2;
            case "Eagle General":
            case "Water Elemental":
                return getTotalUnits("Dragon") == 2;
            case "Thrower":
            case "Barbarian":
                return getTotalUnits("Frog Prince") == 2;
            default:
                return false;
        }
    }
}
