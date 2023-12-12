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

    public static HashMap<String,EntityInfo> entityInfos;

    public EntityManager() {
        entities = new ArrayList<Entity>();
        toRemove = new ArrayList<Entity>();
        toAdd = new ArrayList<Entity>();
        
    }
    public void loadEntityData(){
        entityInfos = new HashMap<String,EntityInfo>();
        JSON json = new JSON(new File("res\\json\\mushrooms.json"));
        KeyValuePair kv = json.parse("");
        for(KeyValuePair c : kv.getObject()){
            String key = c.getKey();
            String name = c.findChild("name").getString();
            int id = c.findChild("id").getInteger();
            int value = c.findChild("value").getInteger();
            int rarity = c.findChild("rarity").getInteger();
            BufferedImage texture = AssetStorage.images.get(c.findChild("texture").getString());
            entityInfos.put(key, new EntityInfo(id, name, value, rarity, texture));
        }

        JSON json2 = new JSON(new File("res\\json\\entities.json"));
        KeyValuePair kv2 = json2.parse("");
        for(KeyValuePair c : kv2.getObject()){
            String key = c.getKey();
            String name = c.findChild("name").getString();
            int id = c.findChild("id").getInteger();
            int speed = c.findChild("speed").getInteger();
            
            BufferedImage texture = AssetStorage.images.get(c.findChild("texture").getString());
            entityInfos.put(key, new EntityInfo(id, name, texture,speed));
        }


    }
    public void addEntity(Entity e){
        toAdd.add(e);
    }
    public void removeEntity(Entity e){
        toRemove.add(e);
    }
    public void render(Graphics g){
        for(Entity e : entities){
            if(e.inView)
            e.render(g);
        }
    }
    public void update(){
        
        for(Entity e : entities){
            if(e.inView)
                e.update();
        }
        entities.removeAll(toRemove);
        entities.addAll(toAdd);
        toRemove.clear();
        toAdd.clear();

    }
    public ArrayList<Entity> getEntities(){return entities;}
}
