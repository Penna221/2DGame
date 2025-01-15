package entities.ai;

import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import gfx.AssetStorage;
import gfx.Transition;
import io.KeyManager;
import states.GameState;
import states.State;
import ui.Container;
import ui.PauseMenu;
import ui.Task;
import world.World;

public class DoorAI extends AI{
    private Ellipse2D.Double interactCircle;
    private boolean talk = false;
    public DoorAI(Entity e) {
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
        Task t = new Task(){
            public void perform(){
                PauseMenu.setContainer(null);
                World.load(e.info.tunnel);
                GameState.paused = false;
            }
        };
        Container c = PauseMenu.createTunnelEntryDialog(t);
        PauseMenu.setContainer(c);
        GameState.paused = true;
    }
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
