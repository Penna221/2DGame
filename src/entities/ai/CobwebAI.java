package entities.ai;

import java.awt.Graphics;
import java.awt.geom.Ellipse2D;

import entities.AttackBox;
import entities.Entity;
import entities.EntityManager;
import entities.effects.SlowEffect;
import tools.Timer;
import ui.Task;
import world.World;

public class CobwebAI extends AI{
    private Ellipse2D.Double attackRadius;
    private Timer attackTimer;
    public CobwebAI(Entity e){
        super(e);
    }

    @Override
    public void lateInit() {
        Task attackTask = new Task(){
            public void perform(){tryToAttack();}
        };
        attackTimer = new Timer(100,attackTask);
        attackRadius = e.generateSurroundingCircle(50);
    }
    private void tryToAttack(){
        for(Entity en : World.entityManager.newList){
            if(attackRadius.intersects(en.bounds)){
                if(!en.checkEffect("Slowness"))
                    en.applyEffect(new SlowEffect(1000, en, 0.4));
            }

            
        }
        
    }
    @Override
    public void update() {
        attackTimer.update();
    }

    @Override
    public void render(Graphics g) {
    }
    
}
