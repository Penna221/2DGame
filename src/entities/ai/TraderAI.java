package entities.ai;

import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import gfx.AssetStorage;
import io.KeyManager;
import states.GameState;
import ui.PauseMenu;
import world.World;

public class TraderAI extends AI{
    private Ellipse2D.Double interactCircle;
    private boolean talk = false;
    public TraderAI(Entity entity) {
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

        e.currentAnimation.animate();
    }

    private void openTradingMenu(){
        PauseMenu.generateNewContainer("enter_trader");
        PauseMenu.setContainer(PauseMenu.containers.get("enter_trader"));
        GameState.paused = true;
    }
    @Override
    public void render(Graphics g) {
        // g.drawOval((int)(interactCircle.x - World.camera.getXOffset()),(int) (interactCircle.y - World.camera.getYOffset()), (int)interactCircle.width,(int)interactCircle.height);
        if(talk){
            drawTalkBox(g);
        }
    }

    private void drawTalkBox(Graphics g){
        BufferedImage img = AssetStorage.images.get("interact_e");
        g.drawImage(img,(int) (e.x +e.bounds.width/2 - World.camera.getXOffset()-img.getWidth()/2 ), (int)(e.y-World.camera.getYOffset() - img.getHeight()), null);
    }
}
