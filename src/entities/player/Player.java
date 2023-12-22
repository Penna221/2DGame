package entities.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import entities.Creature;
import entities.Entity;
import entities.EntityManager;
import gfx.Factory;
import io.KeyManager;
import tiles.Tile;
import world.World;
public class Player extends Creature{

    private double speed;
    private Rectangle viewRectangle;
    public int distance;
    private double angle;
    
    public Inventory inv;
    public Player(double x, double y) {
        super(x, y);
        inv = new Inventory();
    }

    @Override
    public void init() {
        info = EntityManager.entityInfos.get("player");
        texture = info.texture;
        speed = info.speed;
        health =info.health;
        calculateBounds();
        distance = 800;
        angle = 0;
        viewRectangle = new Rectangle((int)(0),(int)(0),(int)(distance*2 + bounds.width),(int)(distance*2 + bounds.height));
    }

    @Override
    public void update() {
        ySpeed = 0;
        xSpeed = 0;
        angle += 2;
        texture = Factory.rotateImage(info.texture, angle);
        if(KeyManager.up){
            ySpeed = -speed;
        }
        if(KeyManager.down){
            ySpeed= speed;
        }
        if(KeyManager.left){
            xSpeed = -speed;
        }
        if(KeyManager.right){
            xSpeed = speed;
        }
        ArrayList<Entity> entities = World.entityManager.getEntities();
        for(Entity e : entities){
            if(e.bounds.intersects(viewRectangle)){
                e.inView = true;
            }else{
                e.inView = false;
            }
        }
        
        viewRectangle.x = (int)(x- distance);
        viewRectangle.y = (int)(y - distance);
        
        move();
        updateBounds();
    }

    @Override
    public void renderAdditional(Graphics g) {
        drawBounds(g);
        g.setColor(new Color(255,255,255,40));
        int currentTileX = (int)((bounds.x + bounds.width/2)/Tile.tileSize);
        int currentTileY = (int)((bounds.y + bounds.height/2)/Tile.tileSize);
        g.drawRect((int)(currentTileX*Tile.tileSize-World.camera.getXOffset()), (int)(currentTileY*Tile.tileSize - World.camera.getYOffset()), Tile.tileSize, Tile.tileSize);
        //g.drawRect((int)(viewRectangle.x - xOffset), (int)(viewRectangle.y-yOffset), viewRectangle.width,viewRectangle.height);
        inv.render(g);
    }
    
}
