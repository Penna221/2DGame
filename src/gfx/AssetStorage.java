package gfx;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import json.JSON;
import json.KeyValuePair;

public class AssetStorage {
    public static HashMap<String,BufferedImage> images = new HashMap<String,BufferedImage>();
    public HashMap<String,String> texts;
    
    public static void loadImages(){
        JSON json = new JSON(new File("res\\json\\assets.json"));
        KeyValuePair s = json.parse("JSON");
        KeyValuePair imgObject = s.findChild("images");
        for(KeyValuePair i : imgObject.getObject()){
            String name = i.getKey();
            String path = i.getString();
            BufferedImage img = ImageLoader.load(path);
            if(img!=null){
                System.out.println("Correctly loaded: " + name + "  " + path);
                images.put(name, img);
            }
        }
        //imgObject.printAll(0);
    }
    public static void loadTexts(){
        JSON json = new JSON(new File("res\\json\\assets.json"));
        KeyValuePair s = json.parse("JSON");
        KeyValuePair textObject = s.findChild("texts");
        //textObject.printAll(0);
    }
    public static void scaleImages(double scale){
        images.forEach((key,value)->{
            images.put(key, scaleImage(value,scale));
            
        });
    }
    private static BufferedImage scaleImage(BufferedImage img, double scale){
        int w = img.getWidth();
        int h = img.getHeight();
        int nw = (int)(scale*w);
        int nh = (int)(scale*h);
        System.out.println(nw + " "+ nh);
        BufferedImage newImage = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.createGraphics();
        g.drawImage(img, 0, 0,nw,nh, null);
        return newImage;
    }
}
