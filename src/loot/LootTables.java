package loot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import entities.Entity;
import entities.ai.CollectableAI;
import entities.ai.PlayerAI;
import json.JSON;
import json.KeyValuePair;
import world.World;

public class LootTables {
    public static HashMap<String,LootTable> lootTables = new HashMap<String,LootTable>();
    public static void loadLootTables() throws Exception{
        System.out.println("Loading Loot Tables");
        JSON json = new JSON(new File("res\\json\\loot.json"));
        KeyValuePair s = json.parse("JSON");
        ArrayList<KeyValuePair> tables = s.getObject();
        for(KeyValuePair k : tables){
            ArrayList<KeyValuePair> items = k.getObject();
            ArrayList<LootItem> lis = new ArrayList<LootItem>();
            for(KeyValuePair item : items){
                String name = item.getKey();
                int id = item.findChild("id").getInteger();
                int id2 = item.findChild("id2").getInteger();
                int min = item.findChild("min").getInteger();
                int max = item.findChild("max").getInteger();
                LootItem li = new LootItem(name, id,id2,min, max);
                lis.add(li);
            }
            String lootTableName = k.getKey();
            LootTable table = new LootTable(lis);
            lootTables.put(lootTableName, table);
        }
    }
    public static void generateLoot(String searchVar, double x, double y){
        LootTable lt = LootTables.lootTables.get(searchVar);
        if(lt ==null){
            System.out.println("No loot for: " + searchVar);
            return;
        }
        for(int i = 0; i < lt.items.size(); i++){
            LootItem item = lt.items.get(i);
            int id = item.id;
            int id2 = item.id2;
            if(id==-1){
                System.out.println("cannot find item with name: " + item.name);
                continue;
            }
            int min = item.min;
            int max = item.max;
            int amount = generateRandomAmount(min, max);
            for(int j = 0; j < amount; j++){
                int randomXOffset = generateRandomAmount(-40, 40);
                int randomYOffset = generateRandomAmount(-40, 40);
                Entity e2 = World.entityManager.spawnEntity(id,id2, (int)x+randomXOffset, (int)y+randomYOffset);
                e2.ai = new CollectableAI(e2);
                // boolean success = PlayerAI.inv.addItem(e2);
                // if(success){
                //     World.entityManager.removeEntity(e2);
                // }
            }
        }
    }
    private static int generateRandomAmount(int lowerLimit,int maxLimit){
        Random r = new Random();
        return r.nextInt((maxLimit - lowerLimit) + 1) + lowerLimit;
    }
}
