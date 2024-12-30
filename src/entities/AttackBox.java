package entities;

import java.awt.Shape;

public class AttackBox {    
    public Entity source;
    public int damage;
    public Shape bounds;
    public Entity ignoreEntity;
    public float direction;
    public String type;
    public static final String MELEE = "Melee";
    public static final String MAGIC = "Magic";
    public static final String PROJECTILE = "Projectile";
    public AttackBox(Entity source, int damage, Shape bounds,Entity ignore, float direction, String type){
        this.source = source;
        this.damage = damage;
        this.bounds = bounds;
        this.ignoreEntity = ignore;
        this.direction = direction;
        this.type = type;
    }
}
