package entities;

import java.awt.image.BufferedImage;

public class MushroomInfo {
    public String name;
    public int value;
    public int rarity;
    public BufferedImage texture;
    public MushroomInfo(String name, int value, int rarity, BufferedImage texture) {
        this.name = name;
        this.value = value;
        this.rarity = rarity;
        this.texture = texture;
    }
}
