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
    public int[] collectables;
    public int[] entities;
    public String[] structures;
    public static HashMap<String,Biome> biomes;
    //public Entity[] entitites;
    //public Mushroom[] mushrooms;

    public Biome(String name, Tile[] nonSolidTiles,Tile[] solidTiles, Tile[] borderTiles, int[]collectables,int[] entities, String[] structures){
        this.nonSolidTiles = nonSolidTiles;
        this.solidTiles = solidTiles;
        this.borderTiles = borderTiles;
        this.name = name;
        this.collectables = collectables;
        this.entities = entities;
        this.structures = structures;
    }
    public static void loadBiomeData() throws Exception{
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
            //Collectables  | like coins and stuff
            DataType[] collectables = bi.findChild("collectables").getArray();
            int len4 = collectables.length;
            //cIds = Collectable IDs
            int[] cIds = new int[len4];
            for(int i = 0; i < len4; i++){
                cIds[i] = collectables[i].getInteger();
            }

            DataType[] enti = bi.findChild("entities").getArray();
            int len5 = enti.length;
            //cIds = Collectable IDs
            int[] entIds = new int[len5];
            for(int i = 0; i < len5; i++){
                entIds[i] = enti[i].getInteger();
            }
            DataType[] struct = bi.findChild("structures").getArray();
            int len6 = struct.length;
            //cIds = Collectable IDs
            String[] stus = new String[len6];
            for(int i = 0; i < len6; i++){
                stus[i] = struct[i].getString();
            }

            biomes.put(name, new Biome(name,ns,s,b,cIds,entIds,stus));

        }
    }
}
