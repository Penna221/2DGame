package entities.ai;

import java.awt.Graphics;

import entities.Entity;

public abstract class AI {
    public Entity e;
    public AI(Entity entity){
        this.e = entity;
        
    }
    public abstract void lateInit();
    public abstract void update();
    public abstract void render(Graphics g);
}
