package entities.projectiles;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import json.JSON;
import json.KeyValuePair;

public class Projectiles {
    public static HashMap<Integer, Projectile> projectiles = new HashMap<Integer,Projectile>();
    public static void load() throws Exception{
        File f = new File("res\\json\\projectiles.json");
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
            int max_distance = projectile.findChild("max_distance").getInteger();
            String type = projectile.findChild("type").getString();
            String buff = projectile.findChild("buff").getString();
            String texture = projectile.findChild("texture").getString();
            Projectile p = new Projectile(id,name,damage,speed,max_distance,type,buff,texture);
            projectiles.put(id, p);
        }
    }
}
