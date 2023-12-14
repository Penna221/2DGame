package entities.mushrooms;

import java.awt.Graphics;

import entities.Collectable;
import world.World;

public class ChantarelleMushroom extends Collectable{

    public ChantarelleMushroom(double x, double y) {
        super(x, y);
    }

    @Override
    public void collect() {
        World.entityManager.removeEntity(this);
    }

    @Override
    public void init() {
        loadMushroomData(3);
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
