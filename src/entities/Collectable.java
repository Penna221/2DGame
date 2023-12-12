package entities;

public abstract class Collectable extends Entity{

    public int value;
    public int rarity;
    public Collectable(double x, double y) {
        super(x, y);
        
    }
    public abstract void collect();
}
