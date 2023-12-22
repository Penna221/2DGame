package entities.mushrooms;

import java.awt.Graphics;

import entities.Collectable;
import world.World;

public class RussulaPedulosaMushroom extends Collectable{

    public RussulaPedulosaMushroom(double x, double y) {
        super(x, y);
    }

    @Override
    public void collect() {
        boolean b = World.player.inv.addItem(this);
        if(b){
            World.entityManager.removeEntity(this);
        }
    }

    @Override
    public void init() {
        loadMushroomData(4);
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
        checkFocus();
    }
    
}
