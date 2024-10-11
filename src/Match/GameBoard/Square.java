package Match.GameBoard;

import Match.Units.Unit;

public class Square {
    Unit unit;
    Integer quantity;

    Square() {
        unit = null;
        quantity = 0;
    }

    public void addUnit(Unit unit, Integer quantity) {
        this.unit = unit;
        this.quantity = quantity;
    }

    public void removeUnit() {
        unit = null;
        quantity = 0;
    }

    public void replaceUnit(Unit unit, Integer quantity) {
        this.unit = unit;
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit.toString() + quantity.toString();
    }

    public int getQuantity() {
        return quantity;
    }
}
