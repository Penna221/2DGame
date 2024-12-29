package entities.potions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import entities.Entity;
import entities.effects.FireResEffect;
import entities.effects.OnFireEffect;
import entities.effects.RegenEffect;
import entities.effects.SlowEffect;
import json.JSON;
import json.KeyValuePair;
import sound.SoundPlayer;


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
    public static void consume(Potion p, Entity target){
        switch (p.effect) {
            case "heal10":
                target.heal(10);
                break;
            case "regen":
                target.applyEffect(new RegenEffect(10000, target, 1));
                break;
            case "speed1":
                target.applyEffect(new SlowEffect(10000, target, 1.5));
                break;
            case "fire_res":
                target.applyEffect(new FireResEffect(60000, target));
                break;
            case "random":
                target.applyEffect(new OnFireEffect(10000, target));
                break;
            
            default:
                break;
        }
        SoundPlayer.playSound("potion");
    }
}
