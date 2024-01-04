package gfx;

import java.io.File;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import json.DataType;
import json.JSON;
import json.KeyValuePair;

public class Animations {
    public static HashMap<String,Animation> animations;
    public static void loadAnimations(){
        animations = new HashMap<String,Animation>();
        System.out.println("# Loading animations");
        JSON json = new JSON(new File("res\\json\\animations.json"));
        KeyValuePair s = json.parse("JSON");
        for(KeyValuePair animation : s.getObject()){
            String key = animation.getKey();
            System.out.println(key);
            KeyValuePair images = animation.findChild("images");
            KeyValuePair times = animation.findChild("times");
            
            DataType[] imgArray = images.getArray();
            DataType[] timeArray = times.getArray();
            
            int size = imgArray.length;
            BufferedImage[] arr1 = new BufferedImage[size];
            int[] arr2 = new int[size];
            
            for(int i = 0; i < size; i++){
                DataType d = imgArray[i];
                String name = d.getString();
                arr1[i] = AssetStorage.images.get(name);
            }
            for(int i = 0; i < size; i++){
                DataType d = timeArray[i];
                arr2[i] = d.getInteger();
            }
            animations.put(key,new Animation(arr1,arr2));
        }
    }
}
