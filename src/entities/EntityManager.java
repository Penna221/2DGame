package entities;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import gfx.Animation;
import gfx.Animations;
import gfx.AssetStorage;
import json.JSON;
import json.KeyValuePair;
import tiles.Tile;

public class EntityManager {
    private ArrayList<Entity> entities;
    private ArrayList<Entity> toRemove;
    private ArrayList<Entity> toAdd;
    private ArrayList<Entity> inView;
    public static HashMap<Integer,EntityInfo> entityInfos;
    public EntityManager() {
        entities = new ArrayList<Entity>();
        toRemove = new ArrayList<Entity>();
        toAdd = new ArrayList<Entity>();
        inView = new ArrayList<Entity>();
        
    }
    public void loadEntityData(){
        entityInfos = new HashMap<Integer,EntityInfo>();
        JSON json2 = new JSON(new File("res\\json\\entities.json"));
        KeyValuePair kv2 = json2.parse("");
        double scale = 1;
        for(KeyValuePair c : kv2.getObject()){
            String key = c.getKey();
            if(key.equals("scale")){
                scale = c.getFloat();
                AssetStorage.scaleOthers(scale);
                continue;
            }
            int id = c.findChild("id").getInteger();
            String name = c.findChild("name").getString();
            
            double speed = c.findChild("speed").getFloat();
            int health = c.findChild("health").getInteger();
            String type = c.findChild("type").getString();
            String tunnel = "";
            if(type.equals("door")){
                System.out.println("****************");
                tunnel = c.findChild("tunnel").getString();
                System.out.println(tunnel);
            }

            String ai = c.findChild("ai").getString();
            System.out.println("SCALE IS " + scale);
            int width = (int) (c.findChild("width").getInteger()*scale);
            int height = (int) (c.findChild("height").getInteger()*scale);

            
            HashMap<String, Animation> hashMap = new HashMap<String,Animation>();
            KeyValuePair animations = c.findChild("animations");
            for(KeyValuePair ans: animations.getObject()){
                String a = ans.getKey();
                String b = ans.getString();
                // System.out.println("Animation nameName: " + a);
                // System.out.println("Animation val: " + b);
                hashMap.put(a, Animations.animations.get(b));    
            }
            BufferedImage texture = AssetStorage.images.get(c.findChild("texture").getString());
            entityInfos.put(id, new EntityInfo(id,name,type, texture,speed,health,hashMap,ai,width,height,tunnel));
        }


    }
    public Entity generateWithID(int id, int x, int y){
        EntityInfo e = entityInfos.get(id);
        System.out.println("Added: " + e.name);
        Entity c = new Entity(e,x,y);
        entities.add(c);
        return c;
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
    public void render(Graphics g){
        for(Entity e : inView){
            if(e.inView)
                e.render(g);
        }
    }
    public void update(){
        inView.clear();
        for(Entity e : entities){
            if(e.inView)
            inView.add(e);
        }
        // System.out.println(inView.size());
        
        for(Entity e : inView){
            e.update();
        }
        entities.removeAll(toRemove);
        entities.addAll(toAdd);
        toRemove.clear();
        toAdd.clear();

        
    }
    public ArrayList<Entity> getEntities(){return entities;}
}
