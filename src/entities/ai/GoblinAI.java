package entities.ai;

import java.awt.Graphics;
import java.awt.geom.Ellipse2D;

import entities.AttackBox;
import entities.Entity;
import entities.EntityManager;
import entities.swords.Swords;
import tools.Timer;
import ui.Task;
import world.World;

public class GoblinAI extends EnemyAI{
    
    public GoblinAI(Entity e){
        super(e);
    }

    @Override
    public void lateInit() {
        meleeAttackRadius = e.generateSurroundingCircle(50);
        equippedSword = Swords.swords.get(1);
    }
    
    
    @Override
    public void update() {
        e.currentAnimation.animate();
        
        if(meleeAttackRadius.intersects(World.player.bounds)){
            e.stop();
            meleeTimer.update();
        }else{
            if(World.getDistanceBetweenEntities(e, World.player)<200){
                chaseTimer.update();
            }
            
        }
        
        e.move(true);
        
        e.texture = e.currentAnimation.getFrame();
        e.updateBounds();
        meleeAttackRadius.x = e.x +e.bounds.width/2 - meleeAttackRadius.width/2;
        meleeAttackRadius.y = e.y +e.bounds.height/2 - meleeAttackRadius.height/2;
    }

    @Override
    public void render(Graphics g) {
    }
}
