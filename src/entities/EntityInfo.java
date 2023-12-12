package entities;

import java.awt.image.BufferedImage;

public class EntityInfo {
    public int id;
    public String name;
    public int value;
    public int rarity;
    public BufferedImage texture;
    public int speed;
    public EntityInfo(int id, String name, int value, int rarity, BufferedImage texture){
        this.id = id;
        this.name = name;
        this.value = value;
        this.rarity = rarity;
        this.texture = texture;
    }
    public EntityInfo(int id, String name, BufferedImage texture, int speed){
        this.id = id;
        this.name = name;
        this.texture = texture;
        this.speed = speed;
    }
}
