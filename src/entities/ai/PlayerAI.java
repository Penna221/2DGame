package entities.ai;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Entity;
import entities.player.Inventory;
import gfx.Factory;
import io.KeyManager;
import main.Game;
import tiles.Tile;
import world.World;

public class PlayerAI extends AI{
    public static Inventory inv;
    private Rectangle viewRectangle;
    public int distance;
    private double angle;
    public PlayerAI(Entity entity) {
        super(entity);
        
        inv = new Inventory();
    }

    @Override
    public void lateInit() {
        
        e.calculateBounds();
        distance = 800;
        angle = 0;
        viewRectangle = new Rectangle((int)(0),(int)(0),(int)(distance*2 + e.bounds.width),(int)(distance*2 + e.bounds.height));
        
        e.currentAnimation = e.info.animations.get("idle");
        e.currentAnimation.restart();
    }

    @Override
    public void update() {
        e.ySpeed = 0;
        e.xSpeed = 0;
        angle += 2;
        // System.out.println("player update");
        e.texture = e.currentAnimation.getFrame();
        //texture = Factory.rotateImage(texture, angle);
        if(KeyManager.up){
            e.ySpeed = -e.speed;
        }
        if(KeyManager.down){
            e.ySpeed= e.speed;
        }
        if(KeyManager.left){
            e.xSpeed = -e.speed;
        }
        if(KeyManager.right){
            e.xSpeed = e.speed;
        }
        ArrayList<Entity> entities = World.entityManager.getEntities();
        for(Entity ee : entities){
            if(ee == e){
                continue;
            }
            if(ee.bounds.intersects(viewRectangle)&&World.lineOfSightBetween(e, ee)){
               
                ee.inView = true;
                
            }else{
                ee.inView = false;
            }
        }
        e.inView = true;
        
        viewRectangle.x = (int)(e.x- distance);
        viewRectangle.y = (int)(e.y - distance);
        
        e.move();
        e.updateBounds();
        e.currentAnimation.animate();

        
    }
    
    @Override
    public void render(Graphics g) {
        
       // e.drawBounds(g);
        // Coordinates
        int currentTileX = (int)((e.bounds.x + e.bounds.width/2)/Tile.tileSize);
        int currentTileY = (int)((e.bounds.y + e.bounds.height/2)/Tile.tileSize);
        int xTileWithOffset = currentTileX - World.map.map.length/2;
        int yTileWithOffset = currentTileY - World.map.map[0].length/2;
        g.setColor(Color.white);
        g.drawString("coordinates: " + xTileWithOffset +","+ yTileWithOffset, 25,Game.w.getHeight()-75);
        
        //Draw highlight on tile player is on
        g.setColor(new Color(255,255,255,40));
        g.drawRect((int)(currentTileX*Tile.tileSize-World.camera.getXOffset()), (int)(currentTileY*Tile.tileSize - World.camera.getYOffset()), Tile.tileSize, Tile.tileSize);
        //g.drawRect((int)(viewRectangle.x - xOffset), (int)(viewRectangle.y-yOffset), viewRectangle.width,viewRectangle.height);
        inv.render();
        // g.drawImage(e.currentAnimation.getFrame(),10,10,null);
        
    }
    
}
