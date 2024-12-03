package entities.ai;

import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.Point;
import entities.Entity;
import gfx.AssetStorage;
import tools.Timer;
import ui.Task;
import world.World;

public class MoleAI extends AI{
    private Ellipse2D.Double detectCircle;
    private Timer attackTimer;
    public MoleAI(Entity e){
        super(e);
    }
    @Override
    public void lateInit() {
        detectCircle = e.generateSurroundingCircle(800);
        Task attackTask = new Task(){
            public void perform(){throwRock();}
        };
        attackTimer = new Timer(1000,attackTask);
    }

    @Override
    public void update() {
        if(detectCircle.intersects(World.player.bounds)){
            e.texture = AssetStorage.images.get("mole_1");
            attackTimer.update();
        }else{
            e.texture = AssetStorage.images.get("mole_0");
            attackTimer.backToStart();
        }
        e.updateBounds();
    }
    private void throwRock(){
        Point2D p1 = new Point((int)e.x,(int)(e.y));
        Point2D p2 = new Point((int)(World.player.x),(int)(World.player.y));
        float rotation = World.getAngleBetweenPoints(p2,p1);
        World.generateProjectile(35, rotation, p1,e);
    }
    @Override
    public void render(Graphics g) {
        // g.drawOval((int)(detectCircle.x - World.camera.getXOffset()),(int) (detectCircle.y - World.camera.getYOffset()), (int)detectCircle.width,(int)detectCircle.height);
    }
    
}
