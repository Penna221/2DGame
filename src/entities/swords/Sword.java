package entities.swords;

import entities.BasicInfo;

public class Sword extends BasicInfo{
    public int damage;
    public float speed;
    public int reach;
    public String buff;
    public Sword(int id, String name, int damage, float speed, int reach,String buff,String texture){
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.speed = speed;
        this.reach =reach;
        this.buff = buff;
        this.texture = texture;
    }
}
