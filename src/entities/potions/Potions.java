package entities.potions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import json.JSON;
import json.KeyValuePair;


public class Potions {
    public static HashMap<Integer, Potion> potions = new HashMap<Integer,Potion>();    
    public static void load() throws Exception{
        File f = new File("res\\json\\potions.json");
        String path = f.getParent();
        System.out.println(path);
        JSON json = new JSON(f);
        json.readFile(false);
        KeyValuePair kv = json.parse("json");
        ArrayList<KeyValuePair> ps = kv.getObject();
        for(KeyValuePair pots : ps){
            int id = pots.findChild("id").getInteger();
            String name = pots.findChild("name").getString();
            String texture = pots.findChild("texture").getString();
            String effect = pots.findChild("effect").getString();
            Potion p = new Potion(id,name,texture,effect);
            potions.put(id, p);
        }
    }
}
