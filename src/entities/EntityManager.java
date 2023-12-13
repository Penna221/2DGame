package entities;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import gfx.AssetStorage;
import json.JSON;
import json.KeyValuePair;

public class EntityManager {
    private ArrayList<Entity> entities;
    private ArrayList<Entity> toRemove;
    private ArrayList<Entity> toAdd;
    private ArrayList<Entity> inView;
    public static HashMap<String,EntityInfo> entityInfos;
    public static HashMap<String,MushroomInfo> mushroomInfos;

    public EntityManager() {
        entities = new ArrayList<Entity>();
        toRemove = new ArrayList<Entity>();
        toAdd = new ArrayList<Entity>();
        inView = new ArrayList<Entity>();
        
    }
    public void loadEntityData(){
        entityInfos = new HashMap<String,EntityInfo>();
        JSON json2 = new JSON(new File("res\\json\\entities.json"));
        KeyValuePair kv2 = json2.parse("");
        for(KeyValuePair c : kv2.getObject()){
            String key = c.getKey();
            if(key.equals("scale")){
                double scale = c.getFloat();
                AssetStorage.scaleOthers(scale);
                continue;
            }
            String name = c.findChild("name").getString();
            
            double speed = c.findChild("speed").getFloat();
            int health = c.findChild("health").getInteger();
            
            BufferedImage texture = AssetStorage.images.get(c.findChild("texture").getString());
            entityInfos.put(key, new EntityInfo(name, texture,speed,health));
        }


    }
    public void loadMushroomData(){
        mushroomInfos = new HashMap<String,MushroomInfo>();
        JSON json = new JSON(new File("res\\json\\mushrooms.json"));
        KeyValuePair kv = json.parse("");
        for(KeyValuePair c : kv.getObject()){
            String key = c.getKey();
            String name = c.findChild("name").getString();
            int value = c.findChild("value").getInteger();
            int rarity = c.findChild("rarity").getInteger();
            BufferedImage texture = AssetStorage.images.get(c.findChild("texture").getString());
            mushroomInfos.put(key, new MushroomInfo(name, value, rarity, texture));
        }

    }
    public void addEntity(Entity e){
        toAdd.add(e);
    }
    public void removeEntity(Entity e){
        toRemove.add(e);
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
