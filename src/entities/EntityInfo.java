package entities;

import java.awt.image.BufferedImage;

public class EntityInfo {
    public String name;
    public BufferedImage texture;
    public double speed;
    public int health;
    public EntityInfo(String name, BufferedImage texture, double speed, int health){
        this.name = name;
        this.texture = texture;
        this.speed = speed;
        this.health = health;
    }
}
