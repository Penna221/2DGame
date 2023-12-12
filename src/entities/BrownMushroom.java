package entities;

import java.awt.Graphics;

public class BrownMushroom extends Collectable{

    public BrownMushroom(double x, double y) {
        super(x, y);
    }

    @Override
    public void collect() {
        
    }

    @Override
    public void init() {
        info = EntityManager.entityInfos.get("brownMushroom");
        id = info.id;
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
        
    }
    
}
