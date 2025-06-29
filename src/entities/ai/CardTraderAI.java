package entities.ai;

import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import gfx.AssetStorage;
import io.KeyManager;
import world.World;

/*
 * This is NOT USED
 */
public class CardTraderAI extends AI{
    private Ellipse2D.Double interactCircle;
    private boolean talk = false;
    public CardTraderAI(Entity e){
        super(e);
    }

    @Override
    public void lateInit() {
        interactCircle = e.generateSurroundingCircle(150);
    }

    @Override
    public void update() {
        if(interactCircle.intersects(World.player.bounds)){
            talk = true;
            if(KeyManager.interactKey){
                openTradingMenu();
            }
        }else{
            talk = false;
        }
    }
    private void openTradingMenu(){
        // PauseMenu.generateNewContainer("enter_card_trader");
        // PauseMenu.setContainer(PauseMenu.containers.get("enter_card_trader"));
        // GameState.paused = true;
    }
    @Override
    public void render(Graphics g) {
        if(talk){
            drawTalkBox(g);
        }
    }
    private void drawTalkBox(Graphics g){
        BufferedImage img = AssetStorage.images.get("interact_e");
        g.drawImage(img,(int) (e.x +e.bounds.width/2 - World.camera.getXOffset()-img.getWidth()/2 ), (int)(e.y-World.camera.getYOffset() - img.getHeight()), null);
    }
    
}
