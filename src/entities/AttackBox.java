package entities;

import java.awt.Rectangle;

public class AttackBox {    
    public Entity source;
    public int amount;
    public Rectangle bounds;
    public Entity ignoreEntity;
    public AttackBox(Entity source, int amount, Rectangle bounds,Entity ignore){
        this.source = source;
        this.amount = amount;
        this.bounds = bounds;
        this.ignoreEntity = ignore;
    }
}
