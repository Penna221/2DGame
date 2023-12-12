package entities;

import java.awt.image.BufferedImage;

public class EntityInfo {
    
    public String name;
    public int value;
    public int rarity;
    public BufferedImage texture;
    public double speed;
    public int health;
    public EntityInfo(String name, int value, int rarity, BufferedImage texture){
        
        this.name = name;
        this.value = value;
        this.rarity = rarity;
        this.texture = texture;
    }
    public EntityInfo(String name, BufferedImage texture, double speed, int health){
        
        this.name = name;
        this.texture = texture;
        this.speed = speed;
        this.health = health;
    }
}
