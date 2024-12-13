package entities.potions;

import entities.BasicInfo;

public class Potion extends BasicInfo{
    public int id;
    public String name;
    public String texture;
    public String effect;
    public Potion(int id, String name, String texture, String effect){
        this.id = id;
        this.name = name;
        this.texture = texture;
        this.effect = effect;
    }
}
