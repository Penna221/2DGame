package entities.collision;

import java.awt.Rectangle;

import entities.Entity;

public class CollisionBox {
    public Rectangle r;
    public boolean solid;
    public Entity source;
    public CollisionBox(Entity source, Rectangle r, boolean solid){
        this.source = source;
        this.r = r;
        this.solid = solid;
    }
}
