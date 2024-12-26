package entities.ai;

import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entities.Entity;
import entities.EntityManager;
import gfx.AssetStorage;
import io.KeyManager;
import loot.LootItem;
import loot.LootTable;
import loot.LootTables;
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
        
        String searchVar = "gold_chest";
        switch (e.info.name) {
            case "Gold Chest":
                searchVar = "gold_chest";
                break;
            default:
                break;
        }
        LootTables.generateLoot(searchVar, e.x, e.y);
        World.entityManager.removeEntity(e);
    }
    
    @Override
    public void render(Graphics g) {
        if(talk){
            BufferedImage img = AssetStorage.images.get("interact_e");
            g.drawImage(img,(int) (e.x +e.bounds.width/2 - World.camera.getXOffset()-img.getWidth()/2 ), (int)(e.y-World.camera.getYOffset() - img.getHeight()), null);

        }
    }
    
}
