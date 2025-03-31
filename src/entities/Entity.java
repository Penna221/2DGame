package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import entities.ai.AI;
import entities.ai.BigGoblinAI;
import entities.ai.BigSpiderAI;
import entities.ai.BlacksmithAI;
import entities.ai.BossAI;
import entities.ai.CardTraderAI;
import entities.ai.ChestAI;
import entities.ai.CobwebAI;
import entities.ai.CollectableAI;
import entities.ai.DoorAI;
import entities.ai.EmptyAI;
import entities.ai.FireplaceAI;
import entities.ai.GoblinAI;
import entities.ai.GuideAI;
import entities.ai.LockAI;
import entities.ai.MoleAI;
import entities.ai.PlayerAI;
import entities.ai.ProjectileAI;
import entities.ai.SkeletonAI;
import entities.ai.SpiderAI;
import entities.ai.SummonerAI;
import entities.ai.TraderAI;
import entities.ai.WitchAI;
import entities.bows.Bow;
import entities.collision.CollisionBox;
import entities.effects.Effect;
import entities.potions.Potion;
import entities.projectiles.Projectile;
import entities.staves.Staff;
import entities.swords.Sword;
import gfx.Animation;
import gfx.AssetStorage;
import gfx.Factory;
import gfx.Transition;
import loot.LootTables;
import main.Game;
import particles.Particle;
import save.SavedGame;
import sound.SoundPlayer;
import states.State;
import tiles.Tile;
import tools.Timer;
import ui.Task;
import world.Room;
import world.World;

public class Entity {
    public double x, y;
    public double scale;
    public Rectangle bounds;
    public boolean focused;
    public int health, maxHealth;
    public String name;
    public BufferedImage texture, highlight;
    public boolean inView = true;
    public EntityInfo info;
    public AI ai;
    public Animation currentAnimation;
    public int rotation;
    public double xSpeed, ySpeed;
    public double speed;
    public double speedBuff = 1;
    public Entity source;
    public ArrayList<Integer> receivedHits = new ArrayList<Integer>();
    public Projectile projectileInfo;
    public Sword swordInfo;
    public Staff staffInfo;
    public Bow bowInfo;
    public Potion potionInfo;
    public int subID = -1;
    public Timer invincibleTimer,invincibleTimer2;
    public boolean noHit = false;
    public boolean draw =true;
    public ArrayList<Effect> effects, toAdd,toRemove;
    public Timer particleGenerationTimer;
    public static boolean drawBounds = false;
    public Room homeRoom;
    public boolean noCollision = false;



    public Entity(EntityInfo info, double x, double y){
        this.x = x;
        this.y = y;
        this.info = info;
        this.scale = EntityManager.scale;
        init();
    }
    public void setHomeRoom(Room r){
        this.homeRoom = r;
    }
    public void stop(){
        xSpeed = 0;
        ySpeed = 0;
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
            case "spider":
                ai = new SpiderAI(this);
                break;
            case "big_spider":
                ai = new BigSpiderAI(this);
                break;
            case "cobweb":
                ai = new CobwebAI(this);
                break;
            case "fireplace":
                ai = new FireplaceAI(this);
                break;
            case "goblin":
                ai = new GoblinAI(this);
                break;
            case "summoner":
                ai = new SummonerAI(this);
                break;
            case "big_goblin":
                ai = new BigGoblinAI(this);
                break;
            case "lock":
                ai = new LockAI(this);
                break;
            case "card_trader":
                ai = new CardTraderAI(this);
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
    public void giveMomentum(float angle, float amount){
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
                if(!noHit){
                    hit(i);
                    
                }
            }
            Random r = new Random();
            int r1 = 1+ r.nextInt(3);
            SoundPlayer.playSound("hit_"+r1,true,false);
        }
        if(health<=0){
            die();
        }
        receivedHits.clear();
    }
    public void die(){
        if(name.equals("Player")&&info.id==0){
            State.setState(State.deadState, false);
            Transition.canContinue2 = true;
            Transition.canFinish = true;
        }else{
            //LOOT?
            LootTables.generateLoot(name, x, y);
            World.entityManager.removeEntity(this);
        }

    }
    public void hit(int amount){
        System.out.println("Getting hit with " + amount);
        health -= amount;
        noHit=true;
        invincibleTimer.backToStart();
        invincibleTimer2.backToStart();
    }
    public void harm(int amount){
        receivedHits.add(amount);
    }
    public void heal(int amount){
        health += amount;
        if(health>=maxHealth){
            health = maxHealth;
        }
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
        if(draw){
            double xOffset = World.camera.getXOffset();
            double yOffset = World.camera.getYOffset();
            Point p = new Point((int)(x - xOffset + bounds.getWidth()/2),(int)((y - yOffset +bounds.getHeight()/2)));
            Factory.drawCenteredAt(g, texture, p, scale);
            
        }
        renderAdditional(g);
        // drawBounds(g);
        if(focused){
            if(highlight!=null){
                drawHighLight(g);
            }
        }
        // g.drawImage(texture, (int)(x - xOffset), (int)(y - yOffset), null);
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

    public void drawEffects(){
        for(Effect e : effects){
            switch (e.name) {
                case "Regeneration":
                    Particle p1 = new Particle("regen",300,-90);
                    p1.setPosition((int)bounds.getCenterX(),(int)bounds.getCenterY());
                    World.particleManager.addParticle(p1);
                    break;
                case "On Fire":
                    Particle p2 = new Particle("fire",300,-90);
                    p2.setPosition((int)bounds.getCenterX(),(int)bounds.getCenterY());
                    World.particleManager.addParticle(p2);
                    break;
                default:
                    break;
            }
        }
    }

    public void moveProjectile(){
        
        //If cannot move anymore, make one last attackbox.

        if(!moveX() ||!moveY()){
            int expansion = (int)projectileInfo.speed;
            Rectangle b = new Rectangle(bounds.x-expansion, bounds.y-expansion, bounds.width+(expansion*2), bounds.height+(expansion*2));
            int totalDamage = calculateTotalDamage();
            AttackBox attackBox;
            if(projectileInfo.type.equals("Arrow")){
                attackBox = new AttackBox(this, totalDamage, b,source,rotation,AttackBox.PROJECTILE);
            }else if(projectileInfo.type.equals("Magic")){
                attackBox = new AttackBox(this, totalDamage, b,source,rotation,AttackBox.MAGIC);
            }else{
                attackBox = new AttackBox(this, totalDamage, b,source,rotation,AttackBox.PROJECTILE);
            }
            EntityManager.addAttackBox(attackBox);
            World.entityManager.removeEntity(this);
        }
    }
    public int calculateTotalDamage(){
        int totalDamage = projectileInfo.damage;
        switch (projectileInfo.type) {
            case "Arrow":
                if(bowInfo!=null){
                    totalDamage+= bowInfo.damage;
                }
                break;
            case "Magic":
                if(staffInfo!=null){
                    totalDamage+= staffInfo.damage;
                }
                break;    
            default:
                break;
        }
        return totalDamage;
    }
    public void move(boolean animate){
        
        //Can you move?
        //are you frozen?
        //Collision?
        
        if(animate){
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
            
            
        }
        // -- Set correct animation
        //east
        
        moveX();
        moveY();
        
        if(animate){
            if(xSpeed == 0 && ySpeed == 0){
                setAnimation(info.animations.get("idle"));
            }
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
        
        boolean inMap = checkIfInWorldBounds();
        if(inMap){
            // canMove = checkTilesX();
            if(!noCollision){
                canMove = checkCollisionX();
            }
        }else{
            canMove = true;
        }
        
        if(canMove){
            double newSpeed = xSpeed;
            if(checkEffect("Slowness") || checkEffect("Speed")){
                newSpeed *= speedBuff;
                x+= newSpeed;
            }else{
                x += xSpeed;
            }
        }else{
            xSpeed = 0;
        }
        return canMove;
        
    }
    private boolean moveY(){
        boolean canMove = true;
        boolean inMap = checkIfInWorldBounds();
        if(inMap){
            // canMove = checkTilesY();
            if(!noCollision){
                canMove = checkCollisionY();
            }
        }else{
            canMove = true;
        }

        if(canMove){
            double newSpeed = ySpeed;
            if(checkEffect("Slowness") || checkEffect("Speed")){
                newSpeed *= speedBuff;
                y+= newSpeed;
            }else{
                y+= ySpeed;

            }
        }else{
            ySpeed = 0;
        }
        return canMove;
    }
    
    private boolean checkCollisionX(){
        
        // System.out.println("collision box: " + World.collisionBoxes.size());
        for(CollisionBox b : World.collisionBoxes){
            if(!b.solid){
                continue;
            }
            if(b.source == this){
                continue;
            }
            if(checkProjectileCollisionSource(b)){
                continue;
            }
            if(xSpeed >0){
                //Moving right
                int nextX = (bounds.x + bounds.width)+(int)xSpeed;
                Point p1 = new Point(nextX, bounds.y);
                Point p2 = new Point(nextX, bounds.y + bounds.height);
                if(b.r.contains(p1)||b.r.contains(p2)){
                    //point is in collision box. Move entity leftSide of box;
                    int pos = b.r.x-bounds.width-1;
                    x = pos;
                    if(b.source!=null){
                        if(b.source.info.movable){
                            b.source.giveMomentum(0f, 1);
                        }
                    }
                    return false;
                }else{
                    // System.out.println("no collision");
                    continue;
                }
            }else if(xSpeed<0){
                //Moving left;
                int nextX = (bounds.x)+(int)xSpeed;
                Point p1 = new Point(nextX, bounds.y);
                Point p2 = new Point(nextX, bounds.y + bounds.height);
                if(b.r.contains(p1)||b.r.contains(p2)){
                    //point is in collision box. Move entity leftSide of box;
                    int pos = b.r.x+b.r.width+1;
                    x = pos;
                    if(b.source!=null){
                        if(b.source.info.movable){
                            b.source.giveMomentum(180f, 1);
                        }
                    }
                    return false;
                }else{
                    continue;
                }
            }
        }

        return true;
    }
    private boolean checkCollisionY(){
        for(CollisionBox b : World.collisionBoxes){
            if(!b.solid){
                continue;
            }
            if(b.source == this){
                continue;
            }
            if(checkProjectileCollisionSource(b)){
                continue;
            }
            if(ySpeed >0){
                //Moving down
                int nextY = (bounds.y + bounds.height)+(int)ySpeed;
                Point p1 = new Point(bounds.x, nextY);
                Point p2 = new Point(bounds.x + bounds.width,nextY);
                if(b.r.contains(p1)||b.r.contains(p2)){
                    //point is in collision box. Move entity leftSide of box;
                    // System.out.println("collision");
                    int pos = b.r.y-bounds.height-1;
                    y = pos;
                    if(b.source!=null){
                        if(b.source.info.movable){
                            b.source.giveMomentum(90f, 1);
                        }
                    }
                    return false;
                }else{
                    // System.out.println("no collision");
                    continue;
                }
            }else if(ySpeed<0){
                //Moving up;
                int nextY = (bounds.y)+(int)ySpeed;
                Point p1 = new Point(bounds.x, nextY);
                Point p2 = new Point(bounds.x + bounds.width,nextY);
                if(b.r.contains(p1)||b.r.contains(p2)){
                    //point is in collision box. Move entity leftSide of box;
                    int pos = b.r.y+b.r.height+1;
                    y = pos;
                    if(b.source!=null){
                        if(b.source.info.movable){
                            b.source.giveMomentum(-90f, 1);
                        }
                    }
                    return false;
                }else{
                    continue;
                }
            }
        }

        return true;
    }
    private boolean checkProjectileCollisionSource(CollisionBox b){
        if(info.type.equals("Projectile")){
            if(b.source!=null){
                if(b.source.equals(source)){
                    return true;
                }
            }
            
        }
        return false;
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
        //False means its solid.
        boolean nextSolid = World.map.binaryMap[nextTileX][currentTileY];
        boolean nextSolid2 = World.map.binaryMap[nextTileX][currentTileY2];
        if(!nextSolid||!nextSolid2){
            if(xSpeed < 0){
                int pos = nextTileX*Tile.tileSize + Tile.tileSize+1;
                x = pos;
                // x+= xSpeed;
            }else if(xSpeed>0){
                int pos = nextTileX*Tile.tileSize -bounds.width-1;
                x = pos;
                // x+= xSpeed;
            }else{return true;}
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
                
            }else if(ySpeed >0){
                int pos = nextTileY*Tile.tileSize -bounds.height-1;
                y = pos;
            }else{
                return true;
            }
            return false;
        }else{
            return true;
        }
    }
    private boolean checkIfInWorldBounds(){
        int w = (World.map.binaryMap.length * Tile.tileSize) - Tile.tileSize;
        int h = (World.map.binaryMap[0].length * Tile.tileSize)-Tile.tileSize;
        if(bounds.x+bounds.width > w || x < Tile.tileSize){
            return false;
        }
        if(bounds.y+bounds.height > h || y <Tile.tileSize){
            return false;
        }
        return true;
    }
    public void init() {
        setAI();
        texture = info.texture;
        speed = info.speed;
        health =info.health;
        maxHealth = health;
        if(info.name.equals("Player")){
            if(SavedGame.currentSave!=null){
                health = SavedGame.currentSave.playerHealth;
                maxHealth = SavedGame.currentSave.playerMaxHealth;
            }
        }
        name = info.name;
        calculateBounds();
        Task par = new Task(){
            public void perform(){
                drawEffects();
            }
        };
        particleGenerationTimer = new Timer(700,par);
        updateInvisTime(info.invisTime);
        effects = new ArrayList<Effect>();
        toRemove = new ArrayList<Effect>();
        toAdd = new ArrayList<Effect>();
        ai.lateInit();

    }
    public int getItemDamage(){
        int damage = 0;
        if(swordInfo==null && projectileInfo== null && staffInfo==null && bowInfo==null){
            return damage;
        }
        switch (info.id) {
            case 27:
                damage = swordInfo.damage;
                break;
            case 35:
                damage = projectileInfo.damage;
                break;
            case 36:
                damage = staffInfo.damage;
                break;
            case 37:
                damage = bowInfo.damage;
                break;
            default:
                break;
        }
        return damage;
    }
    public void updateInvisTime(int time){
        noHit = false;
        draw = true;
        Task t = new Task(){
            public void perform(){
                noHit = false;
                draw = true;
            }
        };
        Task t2 = new Task(){
            public void perform(){
                draw = !draw;
            }
        };
        invincibleTimer = new Timer(time,t);
        invincibleTimer2 = new Timer(100,t2);
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
        if(health != maxHealth){
            showHealth(g);
        }
        if(drawBounds){
            drawBounds(g);
        }
    }
    public void showHealth(Graphics g){
        int maxWidth = 100;
        float percentage = (float)health / (float)maxHealth;
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
    public void applyEffect(Effect effect){
        if(!info.type.equals("Creature")){
            return;
        }
        if(!checkEffect(effect.name)){
            toAdd.add(effect);
        }else{
            reApplyEffect(effect.name);
        }
    }
    public void removeEffect(Effect effect){
        toRemove.add(effect);
    }
    public Rectangle generateSurroundingBox(int size){
        return new Rectangle((int)(bounds.x +bounds.width/2- size/2),(int) (bounds.y + bounds.height/2- size/2 ), size, size);
        
    }
    public Ellipse2D.Double generateSurroundingCircle(int size){
        return new Ellipse2D.Double((int)(bounds.x +bounds.width/2- size/2),(int) (bounds.y + bounds.height/2- size/2 ), size, size);
        
    }
    public boolean checkEffect(String name){
        for(Effect eff : effects){
            if(eff.name.equals(name)){
                return true;
            }
        }
        return false;
    }
    public void reApplyEffect(String name){
        for(Effect eff : effects){
            if(eff.name.equals(name)){
                eff.timer.backToStart();
            }
        }    
    }
    public void update() {
        if(info.ai!="empty"){
            checkIfHit();
            
        }
        
        effects.addAll(toAdd);
        toAdd.clear();
        for(Effect e : effects){
            e.update();
        }
        effects.removeAll(toRemove);
        toRemove.clear();
        particleGenerationTimer.update();
        ai.update();
        if(noHit){
            invincibleTimer2.update();
            invincibleTimer.update();
        }
    }
}
