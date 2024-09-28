package entities.ai;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.sound.sampled.Clip;

import entities.Entity;
import entities.player.HUD;
import entities.player.Inventory;
import io.KeyManager;
import sound.SoundPlayer;
import tiles.Tile;
import tools.Timer;
import ui.Task;
import world.World;

public class PlayerAI extends AI{
    public static Inventory inv;
    private Rectangle viewRectangle;
    public int distance;
    private boolean moving = false;
    private Clip walkClip;
    public static HUD hud;
    public Timer hudUpdateTimer;
    public PlayerAI(Entity entity) {
        super(entity);
        if(hud==null){
            hud = new HUD();
        }
        Task t = new Task(){
            public void perform(){
                hud.update();    
            }
        };
        hudUpdateTimer = new Timer(200, t);

        if(inv==null){
            inv = new Inventory();
        }
    }

    @Override
    public void lateInit() {
        
        e.calculateBounds();
        distance = 1200;
        viewRectangle = new Rectangle((int)(0),(int)(0),(int)(distance*2 + e.bounds.width),(int)(distance*2 + e.bounds.height));
        
        e.currentAnimation = e.info.animations.get("idle");
        e.currentAnimation.restart();
    }

    @Override
    public void update() {
        e.ySpeed = 0;
        e.xSpeed = 0;
        // System.out.println("player update");
        e.texture = e.currentAnimation.getFrame();
        //texture = Factory.rotateImage(texture, angle);
        moving = false;
        if(KeyManager.up){
            e.ySpeed = -e.speed;
            moving = true;
        }
        if(KeyManager.down){
            e.ySpeed= e.speed;
            moving = true;
        }
        if(KeyManager.left){
            e.xSpeed = -e.speed;
            moving = true;
        }
        if(KeyManager.right){
            e.xSpeed = e.speed;
            moving = true;
        }
        if(moving){
            if(walkClip==null){
                walkClip = SoundPlayer.loopSound("walk");
            }
            
        }else{
            if(walkClip!=null){
                walkClip.stop();
                walkClip = null;
            }
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
        
        viewRectangle.x = (int)(e.x- distance);
        viewRectangle.y = (int)(e.y - distance);
        
        e.move();
        e.updateBounds();
        e.currentAnimation.animate();
        hudUpdateTimer.update();
        
    }
    
    @Override
    public void render(Graphics g) {
        
       // e.drawBounds(g);
        // Coordinates
        int currentTileX = (int)((e.bounds.x + e.bounds.width/2)/Tile.tileSize);
        int currentTileY = (int)((e.bounds.y + e.bounds.height/2)/Tile.tileSize); 
        //Draw highlight on tile player is on
        g.setColor(new Color(255,255,255,40));
        g.drawRect((int)(currentTileX*Tile.tileSize-World.camera.getXOffset()), (int)(currentTileY*Tile.tileSize - World.camera.getYOffset()), Tile.tileSize, Tile.tileSize);
        //g.drawRect((int)(viewRectangle.x - xOffset), (int)(viewRectangle.y-yOffset), viewRectangle.width,viewRectangle.height);
        // g.drawImage(e.currentAnimation.getFrame(),10,10,null);
        hud.render(g);
    }
    
}
