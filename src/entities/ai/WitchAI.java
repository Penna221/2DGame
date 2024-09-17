package entities.ai;

import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import gfx.AssetStorage;
import gfx.Transition;
import io.KeyManager;
import states.State;
import world.World;

public class WitchAI extends AI{

    private Ellipse2D.Double interactCircle;
    private boolean talk = false;
    public WitchAI(Entity entity) {
        super(entity);
    }

    @Override
    public void lateInit() {
        
        interactCircle = e.generateSurroundingCircle(150);
        e.currentAnimation = e.info.animations.get("idle");
        e.currentAnimation.restart();
    }

    @Override
    public void update() {
        e.texture = e.currentAnimation.getFrame();


    if(interactCircle.intersects(World.player.bounds)){
        talk = true;
        if(KeyManager.interactKey){
            openTradingMenu();
        }
    }else{
        talk = false;
    }


        e.updateBounds();
        e.currentAnimation.animate();
    }

    private void openTradingMenu(){
        State.setState(State.witchState, true);
        Transition.canContinue2 = true;
        Transition.canFinish = true;
    }
    @Override
    public void render(Graphics g) {
        g.drawOval((int)(interactCircle.x - World.camera.getXOffset()),(int) (interactCircle.y - World.camera.getYOffset()), (int)interactCircle.width,(int)interactCircle.height);
        if(talk){
            drawTalkBox(g);
        }
    }
    private void drawTalkBox(Graphics g){
        BufferedImage img = AssetStorage.images.get("interact_e");
        g.drawImage(img,(int) (e.x +e.bounds.width/2 - World.camera.getXOffset()-img.getWidth()/2 ), (int)(e.y-World.camera.getYOffset() - img.getHeight()), null);
    }
}
