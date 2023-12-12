package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import io.KeyManager;
import world.World;
public class Player extends Entity{

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
        id = info.id;
        speed = info.speed;
        calculateBounds();
        distance = 200;

        viewRectangle = new Rectangle((int)(0),(int)(0),(int)(distance*2 + bounds.width),(int)(distance*2 + bounds.height));
    }

    @Override
    public void update() {
        if(KeyManager.up){
            y-= speed;
        }
        if(KeyManager.down){
            y+= speed;
        }
        if(KeyManager.left){
            x-= speed;
        }
        if(KeyManager.right){
            x+= speed;
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
        
        updateBounds();
    }

    @Override
    public void renderAdditional(Graphics g) {
        g.setColor(Color.white);
        double xOffset = World.camera.getXOffset();
        double yOffset = World.camera.getYOffset();
        g.drawRect((int)(bounds.x-xOffset), (int)(bounds.y-yOffset), bounds.width,bounds.height);
        g.drawRect((int)(viewRectangle.x - xOffset), (int)(viewRectangle.y-yOffset), viewRectangle.width,viewRectangle.height);
    }
    
}
