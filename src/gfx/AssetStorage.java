package gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import json.JSON;
import json.KeyValuePair;

public class AssetStorage {
    public HashMap<String,BufferedImage> images;
    public HashMap<String,String> texts;
    
    public static void loadImages(){
        JSON json = new JSON(new File("res\\json\\assets.json"));
        KeyValuePair s = json.parse("JSON");
        KeyValuePair imgObject = s.findChild("images");
        imgObject.printAll(0);
    }
    public static void loadTexts(){
        JSON json = new JSON(new File("res\\json\\assets.json"));
        KeyValuePair s = json.parse("JSON");
        KeyValuePair textObject = s.findChild("texts");
        textObject.printAll(0);
    }
}
