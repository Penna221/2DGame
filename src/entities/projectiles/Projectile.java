package entities.projectiles;

import entities.BasicInfo;

public class Projectile extends BasicInfo{
    public int damage;
    public float speed;
    public int max_distance;
    public String type;
    public String buff;
    public Projectile(int id, String name, int damage, float speed, int max_distance,String type, String buff, String texture){
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.speed = speed;
        this.max_distance = max_distance;
        this.type = type;
        this.buff = buff;
        this.texture = texture;
    }
    
}
