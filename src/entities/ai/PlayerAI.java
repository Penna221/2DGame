package entities.ai;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.Clip;

import entities.Entity;
import entities.bows.Bows;
import entities.player.HUD;
import entities.player.Inventory;
import entities.player.Inventory.Slot;
import entities.potions.Potions;
import entities.staves.Staves;
import entities.swords.Swords;
import gfx.Factory;
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
    private static int buffer = 60;
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
        hudUpdateTimer = new Timer(50, t);

        if(inv==null){
            inv = new Inventory();
        }
        // inv.addItem(World.entityManager.generateSword(Swords.swords.get(0)));
        // inv.addItem(World.entityManager.generatePotion(Potions.potions.get(0)));
        // inv.addItem(World.entityManager.generatePotion(Potions.potions.get(1)));
        // inv.addItem(World.entityManager.generateBow(Bows.bows.get(0)));
        // inv.addItem(World.entityManager.generateStaff(Staves.staves.get(0)));
    }

    @Override
    public void lateInit() {
        
        e.calculateBounds();
        distance = 1200;
        viewRectangle = new Rectangle((int)(0),(int)(0),(int)(distance*2 + e.bounds.width),(int)(distance*2 + e.bounds.height));
        
        double scaleFactor = 20.0;
        e.currentAnimation = e.info.animations.get("idle");
        e.currentAnimation.restart();
    }

    @Override
    public void update() {
        e.slowdown(0.8);
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
            if(ee.bounds.intersects(viewRectangle)){
                
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
    public static void doActionWithSelectedItem(){
        Slot selectedSlot = inv.getSelectedSlot();
        if(selectedSlot.item==null){
            return;
        }
        int x1 = (int)(World.player.bounds.getCenterX());
        int y1 = (int)(World.player.bounds.getCenterY());
        Point2D origin = new Point(x1,y1);
        float rotation = World.getPlayerRotationToCursor();
        Random r = new Random();
        switch (selectedSlot.item.info.id) {
            case 26:
                //POTION
                //Consume potion
                Potions.consume(selectedSlot.item.potionInfo, World.player);
                selectedSlot.reduce((byte)1);
                break;
            case 27:
                //SWORD
                //SWING SWORD
                buffer *=-1;
                int swordDamage = Swords.swords.get(selectedSlot.item.subID).damage;
                int dam =  swordDamage;
                Swords.createSwordAttack(World.player, null, Swords.swing, World.getPlayerRotationToCursor(),dam,origin);
                
                break;
            case 35:
                //PROJECTILE
                //THROW PROJECTILE. IF ARROW
                
                World.generateProjectile(0, rotation, origin,World.player);
                SoundPlayer.playSound("throw");
                selectedSlot.reduce((byte)1);
                break;
            case 36:
                //STAFF
                if(inv.spellSlot.item!=null&&inv.spellSlot.item.info.type.equals("Spell")){
                    Entity spell = World.generateProjectile(4, rotation, origin,World.player);
                    spell.staffInfo = selectedSlot.item.staffInfo;
                    int r1 = 1+ r.nextInt(3);
                    SoundPlayer.playSound("magic_"+r1);
                }
                
                //CAST SPELL
                break;
            case 37:
                //BOW
                //Point bow at that direction.
                if(inv.arrowSlot.item!=null){
                    if(inv.arrowSlot.item.info.id==35&&inv.arrowSlot.item.projectileInfo.type.equals("Arrow")){
                        Entity arrow = World.generateProjectile(inv.arrowSlot.item.subID, World.getPlayerRotationToCursor(), origin,World.player);
                        arrow.bowInfo = selectedSlot.item.bowInfo;
                        int r2 = 1+ r.nextInt(3);
                        SoundPlayer.playSound("projectile_"+r2);
                        inv.arrowSlot.reduce((byte)1);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void render(Graphics g) {
        
       // e.drawBounds(g);
        // Coordinates
        //Draw highlight on tile player is on
        int currentTileX = (int)((e.bounds.x + e.bounds.width/2)/Tile.tileSize);
        int currentTileY = (int)((e.bounds.y + e.bounds.height/2)/Tile.tileSize); 
        g.setColor(new Color(255,255,255,40));
        g.drawRect((int)(currentTileX*Tile.tileSize-World.camera.getXOffset()), (int)(currentTileY*Tile.tileSize - World.camera.getYOffset()), Tile.tileSize, Tile.tileSize);
        g.setColor(Color.red);
        e.drawBounds(g);
        drawWeapon(g);
        hud.render(g);
    }
    private void drawWeapon(Graphics g){
        Slot selectedSlot = inv.getSelectedSlot();
        double rotation = 0;
        int xOffset = 0;
        int yOffset = 0;
        if(selectedSlot.item==null){
            return;
        }
        
        rotation = World.getPlayerRotationToCursor()-180;
        if(selectedSlot.item.info.id==27){
            rotation+=buffer;
        }
        double rads = Math.toRadians(rotation);
        double unitX = Math.cos(rads);
        double unitY = Math.sin(rads);

        
        switch (selectedSlot.item.info.id) {
            case 26:
                //POTION
                
                break;
            case 27:
                //SWORD
                //Spin sword to where cursor points.
                
                xOffset = (int)(-unitX * 50);
                yOffset = (int)(-unitY * 50);
                rotation += 180;
                // System.out.println(unitX + "  " + unitY);
                break;
            case 35:
                //PROJECTILE
                //NOTE!!!! CANNOT THROW ARROWS. NEEDS TO EQUIP BOW (CASE 37)
                //ROCKS AND OTHER PROJECTILES ARE FINE. :)
                rotation = 0;
                break;
            case 36:
                //STAFF
                //Staff stays straight
                xOffset = (int)(-unitX * 20);
                yOffset = (int)(-unitY * 20);
                rotation = 0;
                break;
            case 37:
                //BOW
                //Point bow at that direction.
                xOffset = (int)(-unitX * 20);
                yOffset = (int)(-unitY * 20);
                rotation += 180;
                break;
            default:
                rotation = 0;
                break;
        }
        // BufferedImage scaled = AssetStorage.scaleImage(selectedSlot.item.texture, 2);
        BufferedImage flipped = selectedSlot.item.texture;
        if(rotation >90 || rotation < -90){
            flipped = Factory.flipImage(selectedSlot.item.texture);
        }

        BufferedImage rotated = Factory.rotateImage(flipped, rotation-90);
        int drawX = (int)(e.bounds.x+e.bounds.width/2 -World.camera.getXOffset())+xOffset;
        int drawY = (int)(e.bounds.y+e.bounds.height/2 -World.camera.getYOffset())+yOffset;
        Factory.drawCenteredAt(g, rotated, new Point(drawX,drawY),2);
        // g.drawImage(rotated,drawX,drawY,null);
        //DRAW LINE Where player looking. MAYBE
    }
}
