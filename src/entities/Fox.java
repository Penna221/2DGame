package entities;

import java.awt.Graphics;

import gfx.AssetStorage;

public class Fox extends Creature{

    public Fox(double x, double y) {
        super(x, y);
    }

    @Override
    public void init() {
        info = EntityManager.entityInfos.get("fox");
        texture = info.texture;
        speed = info.speed;
        health =info.health;
        calculateBounds();
    }

    @Override
    public void renderAdditional(Graphics g) {
        
    }

    @Override
    public void update() {
        
    }
    
}
