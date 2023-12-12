package tiles;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import gfx.AssetStorage;
import json.JSON;
import json.KeyValuePair;

public class Tile {
    public static int tileSize;
    public int id;
    private static Tile[] tiles;
    public boolean solid;
    public BufferedImage texture;
    public Tile(int id, boolean solid, BufferedImage img){
        this.id = id;
        this.solid = solid;
        this.texture = img;
        tiles[id] = this;
    }
    public static void loadTiles(){
        JSON json = new JSON(new File("res\\json\\tiles.json"));
        KeyValuePair kv = json.parse("JSON");
        ArrayList<KeyValuePair> arr = kv.getObject();
        double scale = kv.findChild("scale").getFloat();
        int size = arr.size();
        tiles = new Tile[size-1]; // Minus 1 because scale is not tile.
        AssetStorage.scaleImages(scale);
        tileSize = AssetStorage.images.get("void").getWidth();
        System.out.println("Total of " + tiles.length + " tiles.");
        for(KeyValuePair i : arr){
            
            String name = i.getKey();
            if(name.equals("scale")){
                continue;
            }
            int id = i.findChild("id").getInteger();
            String textureKey = i.findChild("texture").getString();
            boolean solid = i.findChild("solid").getBoolean();
            
            System.out.println(name + " " + id);
            BufferedImage img = AssetStorage.images.get(textureKey);
            new Tile(id,solid,img);
        }
    }
    public static Tile getTileByID(int id){
        if(id>=tiles.length){
            return tiles[0];
        }else{
            return tiles[id];
        }
    }
    public static Tile[] getBiomeSpecificTiles(String biome){
        return null;
    }
}
