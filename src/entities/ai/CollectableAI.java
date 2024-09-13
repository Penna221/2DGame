package entities.ai;

import java.awt.Graphics;

import entities.Entity;
import entities.EntityManager;
import entities.player.Inventory;
import sound.SoundPlayer;
import world.World;

public class CollectableAI extends AI{

    public CollectableAI(Entity e) {
        super(e);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void lateInit() {
        e.setTexture(e.texture);
    }

    @Override
    public void update() {
        e.texture = e.currentAnimation.getFrame();
        if(e.bounds.intersects(World.player.bounds)){
            PlayerAI.inv.addItem(e);
            World.entityManager.removeEntity(e);
            SoundPlayer.playSound("pickup");
        }
        e.currentAnimation.animate();
    }

    @Override
    public void render(Graphics g) {
        //e.drawHighLight(g);
    }

}
