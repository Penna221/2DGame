package world;

import java.io.File;
import java.util.HashMap;

import json.DataType;
import json.JSON;
import json.KeyValuePair;
import tiles.Tile;

public class Biome {
    public String name;
    public Tile[] nonSolidTiles;
    public Tile[] solidTiles;
    public static HashMap<String,Biome> biomes;
    //public Entity[] entitites;
    //public Mushroom[] mushrooms;

    public Biome(String name, Tile[] nonSolidTiles,Tile[] solidTiles){
        this.nonSolidTiles = nonSolidTiles;
        this.solidTiles = solidTiles;
        this.name = name;

    }
    public static void loadBiomeData(){
        biomes = new HashMap<String,Biome>();
        JSON json = new JSON(new File("res\\json\\biomes.json"));
        KeyValuePair kv = json.parse("json");
        for(KeyValuePair bi : kv.getObject()){
            String name = bi.getKey();
            DataType[] nonsolid = bi.findChild("nonSolidTiles").getArray();
            DataType[] solid = bi.findChild("solidTiles").getArray();
            int len1 = nonsolid.length;
            int len2 = solid.length;
            Tile[] ns = new Tile[len1];
            Tile[] s = new Tile[len2];
            for(int i = 0; i < len1; i++){
                ns[i] = Tile.getTileByID(nonsolid[i].getInteger());
            }
            for(int i = 0; i < len2; i++){
                s[i] = Tile.getTileByID(solid[i].getInteger());
            }
            biomes.put(name, new Biome(name,ns,s));

        }
    }

}
