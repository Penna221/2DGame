package entities;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import entities.bows.Bow;
import entities.bows.Bows;
import entities.potions.Potion;
import entities.potions.Potions;
import entities.projectiles.Projectile;
import entities.projectiles.Projectiles;
import entities.staves.Staff;
import entities.staves.Staves;
import entities.swords.Sword;
import entities.swords.Swords;
import gfx.Animation;
import gfx.Animations;
import gfx.AssetStorage;
import json.DataType;
import json.JSON;
import json.KeyValuePair;
import tiles.Tile;
import world.Camera;
import world.World;

public class EntityManager {
    private static ArrayList<AttackBox> attackBoxes;
    private static ArrayList<AttackBox> newAttackBoxes;
    private ArrayList<Entity> entities;
    private ArrayList<Entity> toRemove;
    private ArrayList<Entity> toAdd;
    public ArrayList<Entity> inView;
    public static int scale = 1;
    public static HashMap<Integer,EntityInfo> entityInfos;


    private ArrayList<Entity> toView;
    public ArrayList<Entity> newList;
    public boolean loading = false;
    public EntityManager() {
        entities = new ArrayList<Entity>();
        toRemove = new ArrayList<Entity>();
        toAdd = new ArrayList<Entity>();
        inView = new ArrayList<Entity>();
        toView = new ArrayList<Entity>();
        attackBoxes = new ArrayList<AttackBox>();
        newAttackBoxes = new ArrayList<AttackBox>();
    }
    public void loadEntityData(){
        entityInfos = new HashMap<Integer,EntityInfo>();
        JSON json2 = new JSON(new File("res\\json\\entities.json"));
        KeyValuePair kv2 = json2.parse("");
        for(KeyValuePair c : kv2.getObject()){
            int id = c.findChild("id").getInteger();
            String name = c.findChild("name").getString();
            
            double speed = c.findChild("speed").getFloat();
            int health = c.findChild("health").getInteger();
            String type = c.findChild("type").getString();
            String tunnel = "";
            

            String ai = c.findChild("ai").getString();
            if(ai.equals("door")){
                System.out.println("****************");
                tunnel = c.findChild("tunnel").getString();
                System.out.println(tunnel);
            }
            // System.out.println("SCALE IS " + scale);
            int width = (int) (c.findChild("width").getInteger()*scale);
            int height = (int) (c.findChild("height").getInteger()*scale);
            boolean isLight = c.findChild("light_source").getBoolean();
            int light_radius = c.findChild("light_radius").getInteger();
            String color_string = c.findChild("light_color").getString();
            Color light_color = Color.decode(color_string);
            double light_transparency = c.findChild("light_transparency").getFloat();
            int invisTime = c.findChild("invisTime").getInteger();
            DataType[] imm = c.findChild("immune").getArray();
            String[] immuneList = new String[imm.length];
            
            for(int i = 0; i < imm.length; i++){
                immuneList[i] = imm[i].getString();
            }

            HashMap<String, Animation> hashMap = new HashMap<String,Animation>();
            KeyValuePair animations = c.findChild("animations");
            
            for(KeyValuePair ans: animations.getObject()){
                String a = ans.getKey();
                String b = ans.getString();
                hashMap.put(a, Animations.animations.get(b));    
            }
            BufferedImage texture = AssetStorage.images.get(c.findChild("texture").getString());
            entityInfos.put(id, new EntityInfo(id,name,type, texture,speed,health,hashMap,ai,width,height,tunnel,isLight,light_radius,light_color,light_transparency,invisTime, immuneList));
        }


    }
    public Entity spawnEntity(int id,int subID, double x, double y){
        boolean solid = World.map.checkIfSolid((int)(x/Tile.tileSize), (int)(y/Tile.tileSize));
        Entity c = generateEntityWithID(id, subID, x, y);
        if(solid && c.info.type=="Creature"){
            return null;
        }
        if(id==0){
            if(checkForPlayers()){
                return null;
            }
            World.player = c;
            Camera.setEntityToCenter(c);
        }
        toAdd.add(c);
        return c;
    }
    public Entity generateProjectile(Projectile info, int x, int y, int rotation){
        EntityInfo i = entityInfos.get(35);
        Entity c = new Entity(i,x,y);
        c.projectileInfo = info;
        c.rotation = rotation;
        c.ai.lateInit();
        toAdd.add(c);
        return c;
    }
    public Entity generateSword(Sword info){
        EntityInfo i = entityInfos.get(27);
        Entity c = new Entity(i,0,0);
        c.swordInfo = info;
        c.loadBasicInfo();
        return c;
    }
    public Entity generateStaff(Staff info){
        EntityInfo i = entityInfos.get(36);
        Entity c = new Entity(i,0,0);
        c.staffInfo = info;
        c.loadBasicInfo();
        return c;
    }
    public Entity generateBow(Bow info){
        EntityInfo i = entityInfos.get(37);
        Entity c = new Entity(i,0,0);
        c.bowInfo = info;
        c.loadBasicInfo();
        return c;
    }
    public Entity generatePotion(Potion info){
        EntityInfo i = entityInfos.get(26);
        Entity c = new Entity(i,0,0);
        c.potionInfo = info;
        c.loadBasicInfo();
        return c;

    }
    public Entity generateEntityWithID(int id, int subID, double x, double y){
        Entity c = new Entity(entityInfos.get(id),x,y);
        switch (id) {
            case 26:
                c.potionInfo = Potions.potions.get(subID);
                break;
            case 27:
                c.swordInfo = Swords.swords.get(subID);
                break;
            case 35:
                c.projectileInfo = Projectiles.projectiles.get(subID);
                break;
            case 36:
                c.staffInfo = Staves.staves.get(subID);
                break;
            case 37:
                c.bowInfo = Bows.bows.get(subID);
                break;
            default:
                break;
        }
        c.loadBasicInfo();
        return c;
    }
    private boolean checkForPlayers(){
        for(Entity e: entities){
            if(e.info.id==0){
                return true;
            }
        }
        for(Entity e : toAdd){
            if(e.info.id==0){
                return true;
            }
        }
        return false;
    }
    public static int findIDWithName(String name){
        for (Map.Entry<Integer, EntityInfo> entry : entityInfos.entrySet()) {
            EntityInfo entityInfo = entry.getValue();
            if(entityInfo.name.equals(name)) {
                return entityInfo.id;
            }
        }
        return -1;
    }
    public void addEntity(Entity e){
        toAdd.add(e);
    }
    public void removeEntity(Entity e){
        toRemove.add(e);
    }
    public void clearEntities(){
        for(Entity e : entities){
            toRemove.add(e);
        }
    }

    public static void addAttackBox(AttackBox a){
        newAttackBoxes.add(a);
    }
    public void render(Graphics g){
        
        if(World.player==null){
            return;
        }
        if(newList!=null){
            toView.clear();
            toView.addAll(newList);
        }
        Iterator<Entity> iterator = toView.iterator();
        while(iterator.hasNext()){
            Entity e = iterator.next();
            
            if(e==null){
                return;
            }
            e.render(g);
        }
        
    }
    public void update(){
        if(!loading){
            newList = new ArrayList<Entity>();
            newList.add(World.player);
            for(Entity e : entities){
                if(e.inView){
                    if(e.equals(World.player)){
                        continue;
                    }
                    newList.add(e);
                    e.update();
                }
            }
            if(World.player!=null){
                World.player.update();
            }
        }
        entities.removeAll(toRemove);
        entities.addAll(toAdd);
        toRemove.clear();
        toAdd.clear();
        attackBoxes.addAll(newAttackBoxes);
        newAttackBoxes.clear();
        if(!loading){

            for(AttackBox a : attackBoxes){
                for(Entity e : entities){
                    if(e.equals(a.source) || e.equals(a.ignoreEntity)){
                        continue;
                    }
                    if(a.source.projectileInfo!=null&&e.projectileInfo!=null){
                        if(a.source.projectileInfo.id==e.projectileInfo.id){
                            continue;
                        }
                    }
                    if(!e.info.type.equals("Creature")){
                        continue;
                    }
                    if(a.bounds.intersects(e.bounds)){
                        boolean canharm = true;
                        System.out.println("Type of damage: " + a.type);
                        
                        for(String s : e.info.immune){
                            System.out.println("Immune to: " + s);
                            if(a.type.equals(s)){
                                canharm = false;
                                break;
                            }
                        }
                        
                        if(canharm){
                            System.out.println("Dealing ["+a.damage+"] damage to " + e.name);
                            e.harm(a.damage);
                            e.giveMomentum(a.direction, 10);
                            if(a.source.info.type.equals("Projectile")){
                                removeEntity(a.source);
                            }
                        }
                    }
                }
            }
        }
        entities.removeAll(toRemove);
        attackBoxes.clear();
        
    }
    public ArrayList<Entity> getEntities(){return entities;}
}
