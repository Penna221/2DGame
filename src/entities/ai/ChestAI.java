package entities.ai;

import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entities.Entity;
import gfx.AssetStorage;
import io.KeyManager;
import world.World;

public class ChestAI extends AI{
    private Ellipse2D.Double interactCircle;
    private boolean talk = false;
    public ChestAI(Entity entity) {
        super(entity);
        
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
                openBox();
            }
        }else{
            talk = false;
        }
    }
    private void openBox(){
        if(e.info.name.equals("Gold Chest")){
            System.out.println("Opening Gold box");
            int i = generateRandomAmount(2, 10);
            for(int j = 0; j<i; j++){
                int randomXOffset = generateRandomAmount(-40, 40);
                int randomYOffset = generateRandomAmount(-40, 40);
                Entity e2 = World.entityManager.generateWithID(2, (int)e.x+randomXOffset, (int)e.y+randomYOffset);
                boolean success = PlayerAI.inv.addItem(e2);
                if(success){
                    World.entityManager.removeEntity(e2);
                    
                }
            }
        }
        World.entityManager.removeEntity(e);
    }
    private int generateRandomAmount(int lowerLimit,int maxLimit){
        Random r = new Random();
        return r.nextInt((maxLimit - lowerLimit) + 1) + lowerLimit;
    }
    @Override
    public void render(Graphics g) {
        if(talk){
            BufferedImage img = AssetStorage.images.get("interact_e");
            g.drawImage(img,(int) (e.x +e.bounds.width/2 - World.camera.getXOffset()-img.getWidth()/2 ), (int)(e.y-World.camera.getYOffset() - img.getHeight()), null);

        }
    }
    
}
