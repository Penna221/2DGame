package entities.ai;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import gfx.Factory;
import world.World;

public class Enemy1AI extends EnemyAI{
    private int rot = 0;
    private boolean aiming = false;
    private Ellipse2D.Double tooCloseRect;
    private BufferedImage originalTexture;
    public Enemy1AI(Entity entity) {
        super(entity);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void lateInit() {
        originalTexture = e.info.texture;

        
    }

    @Override
    public void update() {
        e.texture = e.currentAnimation.getFrame();
        if(e.texture !=null){
            e.texture = Factory.rotateImage(e.texture, rot);
        }
        tooCloseRect = e.generateSurroundingCircle(500);
        if(tooCloseRect.intersects(World.player.bounds)){
            e.setAnimation(e.info.animations.get("hostile"));
            aiming = true;
            aimAtPlayer();
        }else{
            e.setAnimation(e.info.animations.get("idle"));
            aiming = false;
        }
        e.currentAnimation.animate();
    }
    private void aimAtPlayer(){
        
        rot = (int)World.getAngleBetween(World.player, e);
    }
    @Override
    public void render(Graphics g) {
        g.drawOval((int)(tooCloseRect.x - World.camera.getXOffset()),(int) (tooCloseRect.y - World.camera.getYOffset()), (int)tooCloseRect.width,(int)tooCloseRect.height);
        if(aiming){
            double px = World.player.x;
            double py = World.player.y;
            //CenterX and centerY
            double cx = e.x+e.bounds.width/2;
            double cy = e.y+e.bounds.height/2;
            g.drawLine((int)(cx- World.camera.getXOffset()), (int)(cy- World.camera.getYOffset()),(int)(px- World.camera.getXOffset()), (int)(py- World.camera.getYOffset()));
        }
    }
    
}
