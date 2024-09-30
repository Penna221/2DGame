package entities.ai;

import java.awt.Graphics;
import java.awt.geom.Ellipse2D;

import entities.Entity;
import gfx.AssetStorage;
import world.World;

public class MoleAI extends AI{
    private Ellipse2D.Double detectCircle;
    public MoleAI(Entity e){
        super(e);
    }
    @Override
    public void lateInit() {
        detectCircle = e.generateSurroundingCircle(400);
    }

    @Override
    public void update() {
        if(detectCircle.intersects(World.player.bounds)){
            e.texture = AssetStorage.images.get("mole_1");
        }else{
            e.texture = AssetStorage.images.get("mole_0");
        }
        e.updateBounds();
    }

    @Override
    public void render(Graphics g) {
        // g.drawOval((int)(detectCircle.x - World.camera.getXOffset()),(int) (detectCircle.y - World.camera.getYOffset()), (int)detectCircle.width,(int)detectCircle.height);
    }
    
}
