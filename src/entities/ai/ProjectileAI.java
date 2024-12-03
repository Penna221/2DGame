package entities.ai;

import java.awt.Graphics;

import entities.AttackBox;
import entities.Entity;
import entities.EntityManager;

public class ProjectileAI extends AI{
    private AttackBox attackBox;
    public ProjectileAI(Entity e){
        super(e);
    }
    @Override
    public void lateInit() {
    }
    @Override
    public void update() {
        e.moveProjectile();
        e.updateBounds();
        attackBox = new AttackBox(e, 1, e.bounds,e.source);
        EntityManager.addAttackBox(attackBox);
        
    }

    @Override
    public void render(Graphics g) {
        e.drawBounds(g);
    }
    
}
