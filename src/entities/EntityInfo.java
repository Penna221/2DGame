package entities;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import gfx.Animation;

public class EntityInfo {
    public String name;
    public BufferedImage texture;
    public double speed;
    public int health;
    public HashMap<String,Animation> animations;
    public EntityInfo(String name, BufferedImage texture, double speed, int health,HashMap<String,Animation> animations){
        this.name = name;
        this.texture = texture;
        this.speed = speed;
        this.health = health;
        this.animations = animations;
    }
}
