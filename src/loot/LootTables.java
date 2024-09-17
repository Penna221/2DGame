package loot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import json.JSON;
import json.KeyValuePair;

public class LootTables {
    public static HashMap<String,LootTable> lootTables = new HashMap<String,LootTable>();
    public static void loadLootTables(){
        System.out.println("Loading Loot Tables");
        JSON json = new JSON(new File("res\\json\\loot.json"));
        KeyValuePair s = json.parse("JSON");
        ArrayList<KeyValuePair> tables = s.getObject();
        for(KeyValuePair k : tables){
            ArrayList<KeyValuePair> items = k.getObject();
            ArrayList<LootItem> lis = new ArrayList<LootItem>();
            for(KeyValuePair item : items){
                String name = item.getKey();
                int min = item.findChild("min").getInteger();
                int max = item.findChild("max").getInteger();
                LootItem li = new LootItem(name, min, max);
                lis.add(li);
            }
            String lootTableName = k.getKey();
            LootTable table = new LootTable(lis);
            lootTables.put(lootTableName, table);
        }
    }
}
