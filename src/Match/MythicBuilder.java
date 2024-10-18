package Match;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import Basic.Coordinates;
import Basic.Press;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class MythicBuilder {

    // ALL MYTHICS ELEMENT
    private List<String> Batman = Arrays.asList("Tiger Master", "Tree", "Thrower", "Thrower");
    private List<String> Blob = Arrays.asList("Hunter", "Eagle General", "Bandit");
    private List<String> Bomba = Arrays.asList("Tiger Master", "Wolf Warrior", "Barbarian");
    private List<String> Mama = Arrays.asList("Hunter", "Tree", "Electro Robot");
    private List<String> Coldy = Arrays.asList("Storm Giant", "Sandman", "Water Elemental");
    private List<String> Dragon = Arrays.asList("Eagle General", "Eagle General", "Water Elemental");
    private List<String> Ninja = Arrays.asList("Wolf Warrior", "Paladin", "Demon Soldier");
    private List<String> OrcShaman = Arrays.asList("Hunter", "Electro Robot", "Demon Soldier");
    private List<String> PulseGenerator = Arrays.asList("Electro Robot", "Tree", "Archer", "Archer");
    private List<String> Indy = Arrays.asList("Sheriff", "Wolf Warrior", "Sandman");
    private List<String> Watt = Arrays.asList("Storm Giant", "Electro Robot", "Demon Soldier");
    private List<String> Tar = Arrays.asList("Wolf Warrior", "Hunter", "Sandman", "Barbarian");
    private List<String> Graviton = Arrays.asList("Electro Robot", "Shock Robot", "Thrower", "Thrower");
    private List<String> RocketChu = Arrays.asList("War Machine", "Shock Robot", "Thrower");
    private List<String> Vayne = Arrays.asList("Storm Giant", "Hunter", "Ranger", "Archer");
    private List<String> MonopolyMan = Arrays.asList("Wolf Warrior", "Tree", "Demon Soldier");
    private List<String> IronMeow = Arrays.asList("War Machine", "Bandit", "Bandit");
    private List<String> Lancelot = Arrays.asList("Sheriff", "Hunter", "Paladin");
    private List<String> KittyMage = Arrays.asList("Eagle General", "Archer", "Water Elemental", "Water Elemental");
    private List<String> FrogPrince = Arrays.asList("Wolf Warrior", "Tree", "Barbarian", "Thrower");

    public static boolean canBuild(String name) {
        // Load the game board from JSON
        List<List<Map<String, Object>>> gameBoard = loadGameBoardState();

        if (gameBoard == null) {
            System.out.println("Unable to load game board state.");
            return false;
        }

        // Get the requirements for the unit to be built
        Map<String, Integer> requirements = getRequirementsForUnit(name);
        if (requirements == null) {
            System.out.println("No such unit exists.");
            return false;
        }

        // Get the available units on the board along with their quantities
        Map<String, Integer> unitsOnBoard = getAllUnitsFromBoard(gameBoard);

        // Check if all the required units are present on the board in the needed quantities
        for (Map.Entry<String, Integer> entry : requirements.entrySet()) {
            String requiredUnit = entry.getKey();
            int requiredQuantity = entry.getValue();
            int availableQuantity = unitsOnBoard.getOrDefault(requiredUnit, 0);

            // Check if available quantity is less than required quantity
            if (availableQuantity < requiredQuantity) {
                System.out.println("Not enough of unit: " + requiredUnit);
                return false;
            }
        }

        return true; // All required units and their quantities are available
    }

    // Helper method to get requirements based on the unit name
    private static Map<String, Integer> getRequirementsForUnit(String name) {
        Map<String, Integer> requirements = new HashMap<>();
        switch (name) {
            case "Batman":
                requirements.put("Tiger Master", 1);
                requirements.put("Tree", 1);
                requirements.put("Thrower", 2); // Need 2 Throwers
                break;
            case "Blob":
                requirements.put("Hunter", 1);
                requirements.put("Eagle General", 1);
                requirements.put("Bandit", 1);
                break;
            case "Bomba":
                requirements.put("Tiger Master", 1);
                requirements.put("Wolf Warrior", 1);
                requirements.put("Barbarian", 1);
                break;
            case "Mama":
                requirements.put("Hunter", 1);
                requirements.put("Tree", 1);
                requirements.put("Electro Robot", 1);
                break;
            case "Coldy":
                requirements.put("Storm Giant", 1);
                requirements.put("Sandman", 1);
                requirements.put("Water Elemental", 1);
                break;
            case "Dragon":
                requirements.put("Eagle General", 2); // Need 2 Eagle Generals
                requirements.put("Water Elemental", 1);
                break;
            case "Ninja":
                requirements.put("Wolf Warrior", 1);
                requirements.put("Paladin", 1);
                requirements.put("Demon Soldier", 1);
                break;
            case "OrcShaman":
                requirements.put("Hunter", 1);
                requirements.put("Electro Robot", 1);
                requirements.put("Demon Soldier", 1);
                break;
            case "PulseGenerator":
                requirements.put("Electro Robot", 1);
                requirements.put("Tree", 1);
                requirements.put("Archer", 2); // Need 2 Archers
                break;
            case "Indy":
                requirements.put("Sheriff", 1);
                requirements.put("Wolf Warrior", 1);
                requirements.put("Sandman", 1);
                break;
            case "Watt":
                requirements.put("Storm Giant", 1);
                requirements.put("Electro Robot", 1);
                requirements.put("Demon Soldier", 1);
                break;
            case "Tar":
                requirements.put("Wolf Warrior", 1);
                requirements.put("Hunter", 1);
                requirements.put("Sandman", 1);
                requirements.put("Barbarian", 1);
                break;
            case "Graviton":
                requirements.put("Electro Robot", 1);
                requirements.put("Shock Robot", 1);
                requirements.put("Thrower", 2); // Need 2 Throwers
                break;
            case "RocketChu":
                requirements.put("War Machine", 1);
                requirements.put("Shock Robot", 1);
                requirements.put("Thrower", 1);
                break;
            case "Vayne":
                requirements.put("Storm Giant", 1);
                requirements.put("Hunter", 1);
                requirements.put("Ranger", 1);
                requirements.put("Archer", 1);
                break;
            case "MonopolyMan":
                requirements.put("Wolf Warrior", 1);
                requirements.put("Tree", 1);
                requirements.put("Demon Soldier", 1);
                break;
            case "IronMeow":
                requirements.put("War Machine", 1);
                requirements.put("Bandit", 2); // Need 2 Bandits
                break;
            case "Lancelot":
                requirements.put("Sheriff", 1);
                requirements.put("Hunter", 1);
                requirements.put("Paladin", 1);
                break;
            case "KittyMage":
                requirements.put("Eagle General", 1);
                requirements.put("Archer", 1);
                requirements.put("Water Elemental", 2); // Need 2 Water Elementals
                break;
            case "FrogPrince":
                requirements.put("Wolf Warrior", 1);
                requirements.put("Tree", 1);
                requirements.put("Barbarian", 1);
                requirements.put("Thrower", 1);
                break;
            default:
                return null; // Return null if the unit doesn't exist
        }
        return requirements;
    }

    // Helper method to flatten the game board and extract all units
    private static Map<String, Integer> getAllUnitsFromBoard(List<List<Map<String, Object>>> gameBoard) {
        Map<String, Integer> units = new HashMap<>();
        for (List<Map<String, Object>> row : gameBoard) {
            for (Map<String, Object> cell : row) {
                Map<String, Object> unit = (Map<String, Object>) cell.get("unit");
                String unitName = (String) unit.get("name");
                int unitQuantity = ((Double) unit.get("quantity")).intValue();

                if (unitName != null && !unitName.isEmpty()) {
                    units.put(unitName, units.getOrDefault(unitName, 0) + unitQuantity);
                }
            }
        }
        return units;
    }

    // Method to load the game board state from a JSON file
    private static List<List<Map<String, Object>>> loadGameBoardState() {
        Gson gson = new Gson();
        Path path = Paths.get("Resources/gameBoardState.json").toAbsolutePath();
        try (FileReader reader = new FileReader(path.toFile())) {
            Type type = new TypeToken<List<List<Map<String, Object>>>>() {}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // TODO : BUILD MYTHICAL IN GAME FUNCTIONS...
    public static void buildMythic(String unit) {
        if(canBuild(unit)) {
            Press.press(new Coordinates(), new Coordinates(), "Building " + unit);

        }
    }

    public static void main(String[] args) {
        System.out.println(MythicBuilder.canBuild("Batman"));
    }
}

