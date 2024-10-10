package Match.Units;

public abstract class Unit {
    protected String name;
    protected Rarity rarity;
    protected int quantity;
    protected int value;

    public Unit(String name, Rarity rarity) {
        this.name = name;
        this.rarity = rarity;
    }

    public enum Rarity {
        COMMON, UNCOMMON, EPIC, LEGENDARY, MYTHICAL
    }
}
