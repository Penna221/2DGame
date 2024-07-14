package entities.ai;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import gfx.Factory;
import world.World;

public class Enemy1AI extends AI {
    private int rot = 0;
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
        rot++;
        if(e.texture !=null){
            e.texture = Factory.rotateImage(e.texture, rot);
        }
        tooCloseRect = e.generateSurroundingCircle(200);
        if(tooCloseRect.intersects(World.player.bounds)){
            e.setAnimation(e.info.animations.get("hostile"));
        }else{
            e.setAnimation(e.info.animations.get("idle"));
        }
        e.currentAnimation.animate();
    }

    @Override
    public void render(Graphics g) {
        g.drawOval((int)(tooCloseRect.x - World.camera.getXOffset()),(int) (tooCloseRect.y - World.camera.getYOffset()), (int)tooCloseRect.width,(int)tooCloseRect.height);
        
    }
    
}
