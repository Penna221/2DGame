package entities.ai;

import java.awt.Graphics;

import entities.Entity;
import sound.SoundPlayer;
import tools.Timer;
import ui.Task;
import world.World;

public class CollectableAI extends AI{
    private Timer timer;
    public CollectableAI(Entity e) {
        super(e);
        Task t = new Task(){
            public void perform(){
                checkForPickup();
            }
        };
        timer = new Timer(100,t);
    }

    @Override
    public void lateInit() {
        e.setTexture(e.texture);
        
    }
    private void checkForPickup(){
        if(e.bounds.intersects(World.player.bounds)){
            boolean added = PlayerAI.inv.addItem(e);
            if(added){
                World.entityManager.removeEntity(e);
                SoundPlayer.playSound("pickup");
            }
        }
    }
    @Override
    public void update() {
        // System.out.println(e.info.name);
        if(e.currentAnimation!=null){
            e.texture = e.currentAnimation.getFrame();
            e.currentAnimation.animate();
        }
        timer.update();
    }

    @Override
    public void render(Graphics g) {
        //e.drawHighLight(g);
    }

}
