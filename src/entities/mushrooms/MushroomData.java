package entities.mushrooms;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import gfx.AssetStorage;
import json.JSON;
import json.KeyValuePair;
public class MushroomData {
    private static MushroomData[] mushrooms;
    public int id, rarity, value;
    public String name;
    public BufferedImage texture;
    public MushroomData(int id, String name, int rarity, int value, BufferedImage texture){
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.value = value;
        this.texture = texture;
        mushrooms[id] = this;
    }

    public static void loadMushrooms(){
        JSON json = new JSON(new File("res\\json\\mushrooms.json"));
        KeyValuePair kv = json.parse("");
        ArrayList<KeyValuePair> arr = kv.getObject();
        mushrooms = new MushroomData[arr.size()];
        for(KeyValuePair c : kv.getObject()){
            int id = c.findChild("id").getInteger();
            String name = c.findChild("name").getString();
            int value = c.findChild("value").getInteger();
            int rarity = c.findChild("rarity").getInteger();
            BufferedImage texture = AssetStorage.images.get(c.findChild("texture").getString());
            new MushroomData(id, name, rarity, value, texture);
        }
    }
    public static MushroomData findWithID(int id){
        return mushrooms[id];
    }
}
