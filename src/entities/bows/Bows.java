package entities.bows;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import entities.staves.Staff;
import json.JSON;
import json.KeyValuePair;


public class Bows {
    public static HashMap<Integer, Bow> bows= new HashMap<Integer,Bow>();
    public static void load() throws Exception{
        File f = new File("res\\json\\bows.json");
        String path = f.getParent();
        System.out.println(path);
        JSON json = new JSON(f);
        json.readFile(false);
        KeyValuePair kv = json.parse("json");
        ArrayList<KeyValuePair> ps = kv.getObject();
        for(KeyValuePair bws : ps){
            int id = bws.findChild("id").getInteger();
            String name = bws.findChild("name").getString();
            int damage = bws.findChild("damage").getInteger();
            float speed = bws.findChild("speed").getFloat();
            int max_distance = bws.findChild("max_distance").getInteger();
            String type = bws.findChild("type").getString();
            String buff = bws.findChild("buff").getString();
            String texture = bws.findChild("texture").getString();
            Bow b = new Bow(id,name,damage,speed,max_distance,type,buff,texture);
            bows.put(id, b);
        }
    }
}
