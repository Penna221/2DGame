package entities;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import gfx.Animation;

public class EntityInfo {
    public int id;
    public String type;
    public String name;
    public BufferedImage texture;
    public double speed;
    public int health;
    public HashMap<String,Animation> animations;
    public String ai;
    public int width, height;
    public String tunnel;
    public EntityInfo(int id,String name,String type, BufferedImage texture, double speed, int health,HashMap<String,Animation> animations,String ai, int width, int height, String tunnel){
        this.name = name;
        this.id = id;
        this.type = type;
        this.texture = texture;
        this.speed = speed;
        this.health = health;
        this.animations = animations;
        this.ai = ai;
        this.width = width;
        this.height = height;
        this.tunnel = tunnel;
    }
}
