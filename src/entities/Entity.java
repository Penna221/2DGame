package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.ai.AI;
import entities.ai.BlacksmithAI;
import entities.ai.BossAI;
import entities.ai.ChestAI;
import entities.ai.CollectableAI;
import entities.ai.DoorAI;
import entities.ai.EmptyAI;
import entities.ai.Enemy1AI;
import entities.ai.GuideAI;
import entities.ai.MoleAI;
import entities.ai.PlayerAI;
import entities.ai.ProjectileAI;
import entities.ai.SkeletonAI;
import entities.ai.TraderAI;
import entities.ai.WitchAI;
import entities.bows.Bow;
import entities.potions.Potion;
import entities.projectiles.Projectile;
import entities.staves.Staff;
import entities.swords.Sword;
import gfx.Animation;
import gfx.AssetStorage;
import gfx.Factory;
import gfx.Transition;
import main.Game;
import states.State;
import tiles.Tile;
import world.World;

public class Entity {
    public double x, y;
    public double scale;
    public Rectangle bounds;
    public boolean focused;
    public int health;
    public String name;
    public BufferedImage texture, highlight;
    public boolean inView = true;
    public EntityInfo info;
    public AI ai;
    public Animation currentAnimation;
    public int rotation;
    public double xSpeed, ySpeed;
    public double speed;
    public Entity source;
    public ArrayList<Integer> receivedHits = new ArrayList<Integer>();
    public Projectile projectileInfo;
    public Sword swordInfo;
    public Staff staffInfo;
    public Bow bowInfo;
    public Potion potionInfo;
    public int subID = -1;
    public Entity(EntityInfo info, double x, double y){
        this.x = x;
        this.y = y;
        this.info = info;
        this.scale = EntityManager.scale;
        init();
    }
    public void setAI(){
        switch(info.ai){
            case "Player":
                ai = new PlayerAI(this);
                break;
            case "empty":
                ai = new EmptyAI(this);
                break;
            case "Collectable":
                ai = new CollectableAI(this);
                break;
            case "door":
                ai = new DoorAI(this);
                break;
            case "enemy_1":
                ai = new Enemy1AI(this);
                break;
            case "trader":
                ai = new TraderAI(this);
                break;
            case "boss":
                ai = new BossAI(this);
                break;
            case "blacksmith":
                ai = new BlacksmithAI(this);
                break;
            case "chest":
                ai = new ChestAI(this);
                break;
            case "witch":
                ai = new WitchAI(this);
                break;
            case "guide":
                ai = new GuideAI(this);
                break;
            case "skeleton":
                ai = new SkeletonAI(this);
                break;
            case "mole":
                ai = new MoleAI(this);
                break;
            case "projectile":
                ai = new ProjectileAI(this);
                break;
            default:
                ai = new EmptyAI(this);
        }
        if(info.animations.get("idle")!=null){
            // System.out.println("Idle animation found for " + info.name);
            currentAnimation = info.animations.get("idle");
            currentAnimation.restart();
        }
    }
    public void giveMomentum(float angle, int amount){
        double rads = Math.toRadians(angle);
        double xAmount = Math.cos(rads)*amount;
        double yAmount = Math.sin(rads)*amount;
        xSpeed += xAmount;
        ySpeed += yAmount;
    }
    public void checkIfHit(){
        ArrayList<Integer> copy = new ArrayList<Integer>();
        copy.addAll(receivedHits);
        if(copy.size()>0){
            for(int i : copy){
                System.out.println("Getting hit with " + i);
                health -= i;
            }
        }
        if(health<=0){
            die();
        }
        receivedHits.clear();
    }
    public void die(){
        if(name.equals("Player")&&info.id==0){
            State.setState(State.menuState, true);
            Transition.canContinue2 = true;
            Transition.canFinish = true;
        }else{
            World.entityManager.removeEntity(this);
        }

    }
    public void harm(int amount){
        receivedHits.add(amount);
    }
    public void heal(int amount){
        health += amount;
    }
    public void setScale(double d){
        this.scale = d;
    }
    public void setTexture(BufferedImage img){
        this.texture = img;
        this.highlight = Factory.highlightEdges(texture);
    }
    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void setAnimation(Animation a){
        if(a == null){
            return;
        }
        if(a.equals(currentAnimation)){
            //no change.
            return;
        }
        currentAnimation = a;
        currentAnimation.restart();
    }

    public void setSource(Entity s){
        this.source = s;
    }

    public void calculateBounds(){
        bounds = new Rectangle((int)x,(int)y,info.width,info.height);
    }
    public void updateBounds(){
        bounds.x = (int)x ;
        bounds.y = (int)y;
    }
    public void checkFocus(){
        if(bounds.contains(Game.mm.mouseX+World.camera.getXOffset(),Game.mm.mouseY+World.camera.getYOffset())){
            focused = true;
        }
        else{
            focused = false;
        }
    }
    public void render(Graphics g){
        double xOffset = World.camera.getXOffset();
        double yOffset = World.camera.getYOffset();
        Point p = new Point((int)(x - xOffset + bounds.getWidth()/2),(int)((y - yOffset +bounds.getHeight()/2)));
        Factory.drawCenteredAt(g, texture, p, scale);
        // g.drawImage(texture, (int)(x - xOffset), (int)(y - yOffset), null);
        renderAdditional(g);
        // drawBounds(g);
        if(focused){
            if(highlight!=null){
                drawHighLight(g);
            }
        }
    }
    public void drawBounds(Graphics g){
        g.setColor(Color.white);
        double xOffset = World.camera.getXOffset();
        double yOffset = World.camera.getYOffset();
        g.drawRect((int)(x - xOffset), (int)(y - yOffset), bounds.width,bounds.height);
    }
    public void drawHighLight(Graphics g){
        double xOffset = World.camera.getXOffset();
        double yOffset = World.camera.getYOffset();
        Point p = new Point((int)(x - xOffset + bounds.getWidth()/2),(int)((y - yOffset +bounds.getHeight()/2)));
        Factory.drawCenteredAt(g, highlight, p,scale);
    }


    public void moveProjectile(){
        
        if(!moveX() ||!moveY()){
            World.entityManager.removeEntity(this);
        }
    }
    public void move(){
        
        //Can you move?
        //are you frozen?
        //Collision?
        
        // -- Set correct animation
        //east
        if(xSpeed >0){
            if(ySpeed ==0){
                setAnimation(info.animations.get("walk_east"));
            }else{
                if(ySpeed>0){
                    setAnimation(info.animations.get("walk_south_east"));
                    
                }else{
                    setAnimation(info.animations.get("walk_north_east"));
                }
            }
        }
        //west
        if(xSpeed <0){
            if(ySpeed ==0){
                setAnimation(info.animations.get("walk_west"));
            }else{
                if(ySpeed>0){
                    setAnimation(info.animations.get("walk_south_west"));
                    
                }else{
                    setAnimation(info.animations.get("walk_north_west"));
                }
            }
        }
        //south
        if(ySpeed >0){
            if(xSpeed ==0){
                setAnimation(info.animations.get("walk_south"));
            }
        }
        
        //north
        if(ySpeed <0){
            if(xSpeed ==0){
                setAnimation(info.animations.get("walk_north"));
            }
        }
        

        moveX();
        moveY();
        if(xSpeed == 0 && ySpeed == 0){
            setAnimation(info.animations.get("idle"));
        }
        
    }
    public void slowdown(double factor){
        xSpeed *= factor;
        ySpeed *= factor;
        if(Math.abs(xSpeed) < 0.4){
            xSpeed = 0;
        }
        if(Math.abs(ySpeed) < 0.4){
            ySpeed = 0;
        }
    }
    private boolean moveX(){
        boolean canMove = true;
        
        canMove = checkTilesX();
        
        if(canMove){
            x += xSpeed;
        }
        return canMove;
        
    }
    private boolean moveY(){
        boolean canMove = true;
        canMove = checkTilesY();

        if(canMove){
            y+= ySpeed;
        }
        return canMove;
    }
    private boolean checkTilesX(){
        int currentTileX = (int)(bounds.x/Tile.tileSize);
        int currentTileY = (int)(bounds.y/Tile.tileSize);

        if(xSpeed > 0){
            currentTileX = (int)((bounds.x + bounds.width)/Tile.tileSize);
        }

        int currentTileY2 = (int)((bounds.y+bounds.height)/Tile.tileSize);
        int nextTileX = (int)((bounds.x+xSpeed)/Tile.tileSize);
        if(xSpeed > 0){
            nextTileX = (int)((bounds.x+bounds.width+xSpeed)/Tile.tileSize);
        }
        //Out of bounds check
        if(nextTileX < 0 || nextTileX > World.map.binaryMap.length||currentTileY < 0||currentTileY > World.map.binaryMap[0].length||currentTileY2 < 0||currentTileY2 > World.map.binaryMap[0].length){
            
            return false;
            
        }
        boolean currentSolid = World.map.binaryMap[currentTileX][currentTileY];
        boolean currentSolid2 = World.map.binaryMap[currentTileX][currentTileY2];
        if(!currentSolid&&!currentSolid2){
            return false;
        }
        
        boolean nextSolid = World.map.binaryMap[nextTileX][currentTileY];
        boolean nextSolid2 = World.map.binaryMap[nextTileX][currentTileY2];
        if(!nextSolid||!nextSolid2){
            if(xSpeed < 0){
                int pos = nextTileX*Tile.tileSize + Tile.tileSize+1;
                x = pos;
                
            }else{
                int pos = nextTileX*Tile.tileSize -bounds.width-1;
                x = pos;
            }
            return false;
        }else{
            return true;
        }
    }
    
    private boolean checkTilesY(){
        int currentTileX = (int)(bounds.x/Tile.tileSize);
        int currentTileY = (int)(bounds.y/Tile.tileSize);

        if(ySpeed > 0){
            currentTileY = (int)((bounds.y + bounds.height)/Tile.tileSize);
        }

        int currentTileX2 = (int)((bounds.x+bounds.width)/Tile.tileSize);
        int nextTileY = (int)((bounds.y+ySpeed)/Tile.tileSize);
        if(ySpeed > 0){
            nextTileY = (int)((bounds.y+bounds.height+ySpeed)/Tile.tileSize);
        }
        //Out of bounds check
        if(nextTileY < 0 || nextTileY > World.map.binaryMap[0].length||currentTileY < 0||currentTileY > World.map.binaryMap[0].length||currentTileX2 < 0||currentTileX2 > World.map.binaryMap.length){
            
            return false;
            
        }
        boolean currentSolid = World.map.binaryMap[currentTileX][currentTileY];
        boolean currentSolid2 = World.map.binaryMap[currentTileX2][currentTileY];
        if(!currentSolid&&!currentSolid2){
            return false;
        }
        
        boolean nextSolid = World.map.binaryMap[currentTileX][nextTileY];
        boolean nextSolid2 = World.map.binaryMap[currentTileX2][nextTileY];
        if(!nextSolid||!nextSolid2){
            if(ySpeed < 0){
                int pos = nextTileY*Tile.tileSize + Tile.tileSize+1;
                y = pos;
                
            }else{
                int pos = nextTileY*Tile.tileSize -bounds.height-1;
                y = pos;
            }
            return false;
        }else{
            return true;
        }
    }
    public void init() {
        setAI();
        texture = info.texture;
        speed = info.speed;
        health =info.health;
        name = info.name;
        calculateBounds();
        ai.lateInit();
    }
    public void loadBasicInfo(){
        
        if(swordInfo!=null){
            name = swordInfo.name;
            texture = AssetStorage.images.get(swordInfo.texture);
            subID = swordInfo.id;
        }else if(projectileInfo!=null){
            name = projectileInfo.name;
            texture = AssetStorage.images.get(projectileInfo.texture);
            subID = projectileInfo.id;
        }else if(staffInfo!=null){
            name = staffInfo.name;
            texture = AssetStorage.images.get(staffInfo.texture);
            subID = staffInfo.id;
        }else if(bowInfo!=null){
            name = bowInfo.name;
            texture = AssetStorage.images.get(bowInfo.texture);
            subID = bowInfo.id;
        }else if(potionInfo!=null){
            name = potionInfo.name;
            texture = AssetStorage.images.get(potionInfo.texture);
            subID = potionInfo.id;
        }
    }
    public void renderAdditional(Graphics g) {
        ai.render(g);
        if(health != info.health){
            showHealth(g);
        }
    }
    public void showHealth(Graphics g){
        int maxWidth = 100;
        float percentage = (float)health / (float)info.health;
        float w = maxWidth * percentage;
        double xOffset = World.camera.getXOffset();
        double yOffset = World.camera.getYOffset();
        int drawX = (int)(bounds.getCenterX()-50 - xOffset);
        int drawY = (int)(bounds.y-20 - yOffset);
        g.setColor(Color.black);
        g.fillRect(drawX, drawY, maxWidth,20);
        g.setColor(Color.red);
        g.fillRect(drawX, drawY, (int)w,20);

    }
    public Rectangle generateSurroundingBox(int size){
        return new Rectangle((int)(bounds.x +bounds.width/2- size/2),(int) (bounds.y + bounds.height/2- size/2 ), size, size);
        
    }
    public Ellipse2D.Double generateSurroundingCircle(int size){
        return new Ellipse2D.Double((int)(bounds.x +bounds.width/2- size/2),(int) (bounds.y + bounds.height/2- size/2 ), size, size);
        
    }
    public void update() {
        if(info.ai!="empty"){
            checkIfHit();
        }
        ai.update();
    }
}
