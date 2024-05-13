package tiles;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.w3c.dom.events.MouseEvent;

import gfx.AssetStorage;
import json.DataType;
import json.JSON;
import json.KeyValuePair;

public class Tile {
    public static int tileSize;
    public int id;
    private static Tile[] tiles;
    public boolean solid;
    public BufferedImage texture;
    public String name;
    public Tile(int id, boolean solid, BufferedImage img, String name){
        this.id = id;
        this.solid = solid;
        this.texture = img;
        this.name = name;
        tiles[id] = this;
    }
    public static void loadTiles(){
        JSON json = new JSON(new File("res\\json\\tiles.json"));
        KeyValuePair kv = json.parse("JSON");
        ArrayList<KeyValuePair> arr = kv.getObject();
        double scale = kv.findChild("scale").getFloat();
        int size = arr.size();
        tiles = new Tile[size-1]; // Minus 1 because scale is not tile.

        AssetStorage.scaleTiles(scale);
        tileSize = AssetStorage.images.get("voidTile").getWidth();
        for(KeyValuePair i : arr){
            
            String name = i.getKey();
            if(name.equals("scale")){
                continue;
            }
            int id = i.findChild("id").getInteger();
            // String textureKey = i.findChild("texture").getString();
            String textureKey = name;
            boolean solid = i.findChild("solid").getBoolean();
            
            BufferedImage img = AssetStorage.images.get(textureKey);
            new Tile(id,solid,img,name);
        }
    }
    public static void loadTilesV2(){
        File f = new File("res\\json\\tiles_v2.json");
        boolean ok = true;
        String path = f.getParent();
        System.out.println(path);
        JSON json = new JSON(f);
        json.readFile(false);
        KeyValuePair kv = json.parse("json");
        KeyValuePair info = kv.findChild("info");
        //info.printAll(1);
        String tileSheet = info.findChild("tileSheet").getString();
        int maxWidth = info.findChild("width").getInteger();
        int maxHeight = info.findChild("height").getInteger();
        int tileSize = info.findChild("tileSize").getInteger();
        int scale = info.findChild("scale").getInteger();
        Tile.tileSize = tileSize*scale;
        //  TILES
        KeyValuePair tiles2 = kv.findChild("tiles");
        
        BufferedImage img = AssetStorage.images.get(tileSheet);
        DataType[] array = tiles2.getArray();
        tiles = new Tile[array.length];
        for(DataType a : array){
            int id = a.getObject().get(0).getInteger();
            String name = a.getObject().get(1).getString();
            int x = a.getObject().get(2).getInteger();
            int y = a.getObject().get(3).getInteger();
            boolean solid = a.getObject().get(4).getBoolean();
            BufferedImage cropped = img.getSubimage(x*tileSize, y*tileSize, tileSize, tileSize);
            BufferedImage scaled = AssetStorage.scaleImage(cropped, scale);
            AssetStorage.images.put(name, scaled);
            new Tile(id,solid,cropped,name);
            
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
