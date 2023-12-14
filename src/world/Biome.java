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
    public Tile[] borderTiles;
    public int[] mushroomIds;
    public static HashMap<String,Biome> biomes;
    //public Entity[] entitites;
    //public Mushroom[] mushrooms;

    public Biome(String name, Tile[] nonSolidTiles,Tile[] solidTiles, Tile[] borderTiles, int[]mushroomIds){
        this.nonSolidTiles = nonSolidTiles;
        this.solidTiles = solidTiles;
        this.borderTiles = borderTiles;
        this.name = name;
        this.mushroomIds = mushroomIds;
    }
    public static void loadBiomeData(){
        biomes = new HashMap<String,Biome>();
        JSON json = new JSON(new File("res\\json\\biomes.json"));
        KeyValuePair kv = json.parse("json");
        for(KeyValuePair bi : kv.getObject()){
            String name = bi.getKey();
            //Tile stuff
            DataType[] nonsolid = bi.findChild("nonSolidTiles").getArray();
            DataType[] border = bi.findChild("borderTiles").getArray();
            DataType[] solid = bi.findChild("solidTiles").getArray();
            int len1 = nonsolid.length;
            int len2 = solid.length;
            int len3 = border.length;
            Tile[] ns = new Tile[len1];
            Tile[] s = new Tile[len2];
            Tile[] b = new Tile[len3];
            for(int i = 0; i < len1; i++){
                ns[i] = Tile.getTileByID(nonsolid[i].getInteger());
            }
            for(int i = 0; i < len2; i++){
                s[i] = Tile.getTileByID(solid[i].getInteger());
            }
            for(int i = 0; i < len3; i++){
                b[i] = Tile.getTileByID(border[i].getInteger());
            }
            
            //Mushroom stuff
            DataType[] mushrooms = bi.findChild("mushrooms").getArray();
            int len4 = mushrooms.length;
            int[] mids = new int[len4];
            for(int i = 0; i < len4; i++){
                mids[i] = mushrooms[i].getInteger();
            }

            biomes.put(name, new Biome(name,ns,s,b,mids));

        }
    }

}
