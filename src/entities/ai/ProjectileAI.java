package entities.ai;

import java.awt.Graphics;

import entities.AttackBox;
import entities.Entity;
import entities.EntityManager;
import gfx.AssetStorage;
import gfx.Factory;
import world.World;

public class ProjectileAI extends AI{
    private AttackBox attackBox;
    private int id;
    private String name;
    private int damage;
    private float speed;
    private int maxDistance;
    private String type;
    private String buff;
    private String texture;
    private double startX, startY;
    public ProjectileAI(Entity e){
        super(e);
    }
    @Override
    public void lateInit() {
        if(e.projectileInfo==null){
            return;
        }
        id = e.projectileInfo.id;
        name = e.projectileInfo.name;
        damage = e.projectileInfo.damage;
        speed = e.projectileInfo.speed;
        maxDistance = e.projectileInfo.max_distance;
        type = e.projectileInfo.type;
        buff = e.projectileInfo.buff;
        texture = e.projectileInfo.texture;
        e.texture = Factory.rotateImage(AssetStorage.images.get(texture),e.rotation);
        e.name = name;
        e.speed = speed;
        startX = e.x;
        startY = e.y;
        
    }
    @Override
    public void update() {
        e.moveProjectile();
        e.updateBounds();
        attackBox = new AttackBox(e, damage, e.bounds,e.source,e.rotation);
        EntityManager.addAttackBox(attackBox);
        
    }

    @Override
    public void render(Graphics g) {
        // e.drawBounds(g);
    }
    
}
