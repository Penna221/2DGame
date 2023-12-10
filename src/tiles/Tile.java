package tiles;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import gfx.AssetStorage;
import json.JSON;
import json.KeyValuePair;

public class Tile {
    
    public int id;
    private static Tile[] tiles;
    public boolean solid;
    public String biome;
    public BufferedImage texture;
    public Tile(int id, boolean solid, String biome, BufferedImage img){
        this.id = id;
        this.solid = solid;
        this.biome = biome;
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
        System.out.println("Total of " + tiles.length + " tiles.");
        for(KeyValuePair i : arr){
            
            String name = i.getKey();
            if(name.equals("scale")){
                continue;
            }
            int id = i.findChild("id").getInteger();
            String textureKey = i.findChild("texture").getString();
            boolean solid = i.findChild("solid").getBoolean();
            String biome = i.findChild("biome").getString();
            System.out.println(name + " " + id);
            BufferedImage img = AssetStorage.images.get(textureKey);
            new Tile(id,solid,biome,img);
        }
    }
    public static Tile getTileByID(int id){
        if(id>=tiles.length){
            return tiles[0];
        }else{
            return tiles[id];
        }
    }
}
