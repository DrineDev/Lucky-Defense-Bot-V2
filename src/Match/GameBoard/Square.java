package Match.GameBoard;

import Match.Units.Unit;

public class Square {
    Unit unit;

    Square() {
        unit = null;
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
}
