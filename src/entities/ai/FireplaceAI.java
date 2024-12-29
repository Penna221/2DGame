package entities.ai;

import java.awt.Graphics;
import java.awt.geom.Ellipse2D;

import entities.Entity;
import entities.effects.OnFireEffect;
import tools.Timer;
import ui.Task;
import world.World;

public class FireplaceAI extends AI {
    private Ellipse2D.Double attackRadius;
    private Timer attackTimer;
    public FireplaceAI(Entity e){
        super(e);
    }

    @Override
    public void lateInit() {
        e.currentAnimation = e.info.animations.get("idle");
        e.currentAnimation.restart();
        attackRadius = e.generateSurroundingCircle(50);
        Task attackTask = new Task(){
            public void perform(){tryToAttack();}
        };
        attackTimer = new Timer(200,attackTask);
    }
    private void tryToAttack(){
        for(Entity en : World.entityManager.newList){
            if(en== e){
                continue;
            }
            if(attackRadius.intersects(en.bounds)){
                en.applyEffect(new OnFireEffect(3000, en));
            }
        }
    }
    @Override
    public void update() {
        e.currentAnimation.animate();
        e.texture = e.currentAnimation.getFrame();
        attackTimer.update();
    }

    @Override
    public void render(Graphics g) {

    }
}
