package main;

import java.io.File;

import entities.EntityManager;
import json.JSON;
import json.KeyValuePair;
import tiles.Tile;

public class Launcher {
    public static Game g;
    public Launcher(int w, int h, String t){
        g = new Game(w,h,t);
        // System.out.println("OpenGL Enabled: " + System.getProperty("sun.java2d.opengl"));
        // System.exit(0);
        g.start();
    }
    public static void main(String[] args) {
        File f = new File("res\\json\\init.json");
        if(!f.exists()){
            System.out.println("Init file not found.");
        }else{
            JSON json = new JSON(f);
            KeyValuePair kv = json.parse("JSON");
            
            int width = kv.findChild("width").getInteger();
            int height = kv.findChild("height").getInteger();
            String title = kv.findChild("title").getString();
            int overall_scale = kv.findChild("overall_scale").getInteger();
            int tile_scale = kv.findChild("tile_scale").getInteger();
            Tile.scale = tile_scale*overall_scale;
            int entity_scale = kv.findChild("entity_scale").getInteger();
            EntityManager.scale = entity_scale*overall_scale;
            System.out.println("Width:" + width);
            System.out.println("Height:" + height);
            System.out.println("Title:" + title);
            new Launcher(width,height,title);
            
        }
    }
}