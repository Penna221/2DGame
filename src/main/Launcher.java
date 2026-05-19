package main;

import java.io.File;

import entities.EntityManager;
import json.JSON;
import json.KeyValuePair;
import tiles.Tile;
import utils.pennanen.GameInstance;

public class Launcher {
    public static Game g;
    public Launcher(int w, int h, String t){

        g = new Game(w,h,t);
        GameInstance game = new GameInstance(g,600,400,"My Game");
        game.engine.start();
        // System.out.println("OpenGL Enabled: " + System.getProperty("sun.java2d.opengl"));
        // System.exit(0);
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
            float overall_scale = kv.findChild("overall_scale").getFloat();
            Tile.scale = overall_scale;
            EntityManager.scale = overall_scale;
            System.out.println("Width:" + width);
            System.out.println("Height:" + height);
            System.out.println("Title:" + title);
            System.out.println("Over All Scale: " + overall_scale);
            new Launcher(width,height,title);
            
        }
    }
}