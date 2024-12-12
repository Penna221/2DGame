package entities.swords;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import json.JSON;
import json.KeyValuePair;

public class Swords {
    public static HashMap<Integer, Sword> swords = new HashMap<Integer,Sword>();
    public static void load() throws Exception{
         File f = new File("res\\json\\swords.json");
        String path = f.getParent();
        System.out.println(path);
        JSON json = new JSON(f);
        json.readFile(false);
        KeyValuePair kv = json.parse("json");
        ArrayList<KeyValuePair> ps = kv.getObject();
        for(KeyValuePair projectile : ps){
            int id = projectile.findChild("id").getInteger();
            String name = projectile.findChild("name").getString();
            int damage = projectile.findChild("damage").getInteger();
            float speed = projectile.findChild("speed").getFloat();
            int reach = projectile.findChild("reach").getInteger();
            String buff = projectile.findChild("buff").getString();
            String texture = projectile.findChild("texture").getString();
            Sword s = new Sword(id,name,damage,speed,reach,buff,texture);
            swords.put(id, s);
        }
    }
}