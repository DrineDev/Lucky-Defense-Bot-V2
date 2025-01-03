package Match.GameBoard;

import Basic.Coordinates;
import Basic.Press;
import Basic.Screenshot;
import Match.Units.*;

import java.io.IOException;

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

    public Unit getUnit() {
        return unit;
    }

    /*
     * update square to add unitName and unitQuantity, to be called by GameBoard
     * @param topLeft
     * @param bottomRight
     * @param action
     * @throws IOException
     */
    public void updateSquare(Coordinates topLeft, Coordinates bottomRight, String action) throws IOException, InterruptedException {
        Thread.sleep(1000);
        Press.press(topLeft, bottomRight, action);
        Thread.sleep(2000);
        Screenshot.screenshotGameState();
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
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

}
