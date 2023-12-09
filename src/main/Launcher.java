package main;

import java.io.File;

import json.JSON;
import json.KeyValuePair;

public class Launcher {

    public Launcher(){

    }
    public static void main(String[] args) {
        File f = new File("res\\init.json");
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
            Game g = new Game(width,height,title);
            g.start();
        }
    }
}