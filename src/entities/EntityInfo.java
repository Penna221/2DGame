package entities;

import java.awt.Color;
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

    public boolean light_source;
    public int light_radius;
    public Color light_color;
    public double light_transparency;

    public int invisTime;
    public String[] immune;
    public boolean solid;
    public boolean movable;
    public EntityInfo(int id,String name,String type, BufferedImage texture, double speed, int health,HashMap<String,Animation> animations,String ai, int width, int height, String tunnel, boolean lightSource, int light_radius,Color light_color,double light_transparency, int invisTime, String[] immune, boolean solid,boolean movable){
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
        this.light_source = lightSource;
        this.light_radius = light_radius;
        this.light_color = light_color;
        this.light_transparency = light_transparency;
        this.invisTime = invisTime;
        this.immune = immune;
        this.solid = solid;
        this.movable = movable;
    }
}
