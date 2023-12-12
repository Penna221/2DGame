package entities;

import java.awt.Graphics;

import world.World;

public class BrownMushroom extends Collectable{

    public BrownMushroom(double x, double y) {
        super(x, y);
    }

    @Override
    public void collect() {
        World.entityManager.removeEntity(this);
    }

    @Override
    public void init() {
        info = EntityManager.entityInfos.get("brownMushroom");
        rarity = info.rarity;
        value = info.value;
        texture = info.texture;
        calculateBounds();
    }

    @Override
    public void renderAdditional(Graphics g) {
        
    }

    @Override
    public void update() {
        if(bounds.intersects(World.player.bounds)){
            collect();
        }
    }
    
}
