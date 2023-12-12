package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import io.KeyManager;
import tiles.Tile;
import world.World;
public class Player extends Creature{

    private double speed;
    private Rectangle viewRectangle;
    public int distance;
    public Player(double x, double y) {
        super(x, y);
    }

    @Override
    public void init() {
        info = EntityManager.entityInfos.get("Player");
        texture = info.texture;
        speed = info.speed;
        health =info.health;
        calculateBounds();
        distance = 800;

        viewRectangle = new Rectangle((int)(0),(int)(0),(int)(distance*2 + bounds.width),(int)(distance*2 + bounds.height));
    }

    @Override
    public void update() {
        ySpeed = 0;
        xSpeed = 0;
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
        int currentTileX = (int)(bounds.x/Tile.tileSize);
        int currentTileY = (int)(bounds.y/Tile.tileSize);
        g.drawRect((int)(currentTileX*Tile.tileSize-World.camera.getXOffset()), (int)(currentTileY*Tile.tileSize - World.camera.getYOffset()), Tile.tileSize, Tile.tileSize);
        //g.drawRect((int)(viewRectangle.x - xOffset), (int)(viewRectangle.y-yOffset), viewRectangle.width,viewRectangle.height);
        
    }
    
}
