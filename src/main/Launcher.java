package main;

import java.io.File;

import json.JSON;
import json.KeyValuePair;

public class Launcher {
    public static Game g;
    public Launcher(int w, int h, String t){
        g = new Game(w,h,t);
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
            System.out.println("Width:" + width);
            System.out.println("Height:" + height);
            System.out.println("Title:" + title);
            new Launcher(width,height,title);
            
        }
    }
}