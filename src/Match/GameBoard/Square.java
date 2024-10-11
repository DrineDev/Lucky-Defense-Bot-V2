package Match.GameBoard;

import Basic.Coordinates;
import Basic.Press;
import Match.Units.*;

public class Square {
    Unit unit;

    Square() {
        unit = new Unit();
    }

    public void addUnit(Unit unit) {
        this.unit = unit;
    }

    public void removeUnit() {
        unit = null;
    }

    public void replaceUnit(Unit unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit.toString();
    }

    public Unit getSquare(Coordinates topLeft, Coordinates bottomRight, String action) {
        Press.press(topLeft, bottomRight, action);
        int rarity = Unit.isWhatRarity();
        switch(rarity) {
            case 1:
                unit = CommonUnit.isWhatUnit();
                break;
            case 2:
                unit = UncommonUnit.isWhatUnit();
                break;
            case 3:
                unit = EpicUnit.isWhatUnit();
                break;
            case 4:
                unit = LegendaryUnit.isWhatUnit();
                break;
            case 5:
                unit = MythicalUnit.isWhatUnit();
                break;
            default:
                System.out.println("Failed to update square...");
                break;
        }
        return unit;
    }

    public void updateSquare(Coordinates topLeft, Coordinates bottomRight, String action) {
        int rarity = Unit.isWhatRarity();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        switch(rarity) {
            case 1:
                unit = CommonUnit.isWhatUnit();
                break;
            case 2:
                unit = UncommonUnit.isWhatUnit();
                break;
            case 3:
                unit = EpicUnit.isWhatUnit();
                break;
            case 4:
                unit = LegendaryUnit.isWhatUnit();
                break;
            case 5:
                unit = MythicalUnit.isWhatUnit();
                break;
            default:
                System.out.println("Failed to update square...");
                break;
        }
    }
}
