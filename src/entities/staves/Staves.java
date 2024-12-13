package entities.staves;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import json.JSON;
import json.KeyValuePair;

public class Staves {
    public static HashMap<Integer, Staff> staves= new HashMap<Integer,Staff>();
    public static void load() throws Exception{
        File f = new File("res\\json\\staves.json");
        String path = f.getParent();
        System.out.println(path);
        JSON json = new JSON(f);
        json.readFile(false);
        KeyValuePair kv = json.parse("json");
        ArrayList<KeyValuePair> ps = kv.getObject();
        for(KeyValuePair staff : ps){
            int id = staff.findChild("id").getInteger();
            String name = staff.findChild("name").getString();
            int damage = staff.findChild("damage").getInteger();
            float speed = staff.findChild("speed").getFloat();
            int max_distance = staff.findChild("max_distance").getInteger();
            String type = staff.findChild("type").getString();
            String buff = staff.findChild("buff").getString();
            String texture = staff.findChild("texture").getString();
            Staff s = new Staff(id,name,damage,speed,max_distance,type,buff,texture);
            staves.put(id, s);
        }
    }
    
}
