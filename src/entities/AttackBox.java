package entities;

import java.awt.Shape;

public class AttackBox {    
    public Entity source;
    public int damage;
    public Shape bounds;
    public Entity ignoreEntity;
    public float direction;
    public AttackBox(Entity source, int damage, Shape bounds,Entity ignore, float direction){
        this.source = source;
        this.damage = damage;
        this.bounds = bounds;
        this.ignoreEntity = ignore;
        this.direction = direction;
    }
}
