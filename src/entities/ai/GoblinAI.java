package entities.ai;

import java.awt.Graphics;
import java.awt.geom.Ellipse2D;

import entities.AttackBox;
import entities.Entity;
import entities.EntityManager;
import tools.Timer;
import ui.Task;
import world.World;

public class GoblinAI extends AI{
    private Ellipse2D.Double attackRadius;
    private Timer moveTimer, attackTimer;
    public GoblinAI(Entity e){
        super(e);
    }

    @Override
    public void lateInit() {
        Task moveTask = new Task(){
            public void perform(){getNewDirection();}
        };
        Task attackTask = new Task(){
            public void perform(){tryToAttack();}
        };
        attackTimer = new Timer(1000,attackTask);
        moveTimer = new Timer(250,moveTask);
        attackRadius = e.generateSurroundingCircle(50);
    }
    private void tryToAttack(){
        if(World.lineOfSightBetween(e, World.player)){
            if(attackRadius.intersects(World.player.bounds)){
                EntityManager.addAttackBox(new AttackBox(e, 1, e.bounds,null,World.getAngleBetween(e,World.player),AttackBox.MELEE));
            }
        }
    }
    private void getNewDirection(){
        if(attackRadius.intersects(World.player.bounds)){
            e.stop();
        }else{
            moveTimer.backToStart();
            if(World.lineOfSightBetween(e, World.player)){
                float rot = World.getAngleBetween(World.player,e);
                float rads = (float) Math.toRadians(rot);
                System.out.println(rot);
                e.xSpeed = e.speed*Math.cos(rads);
                e.ySpeed = e.speed*Math.sin(rads);
            }else{
                e.slowdown(0.8);
            }
        }
    }
    @Override
    public void update() {
        e.currentAnimation.animate();
        moveTimer.update();
        attackTimer.update();
        e.move(true);
        
        e.texture = e.currentAnimation.getFrame();
        e.updateBounds();
        attackRadius.x = e.x +e.bounds.width/2 - attackRadius.width/2;
        attackRadius.y = e.y +e.bounds.height/2 - attackRadius.height/2;
    }

    @Override
    public void render(Graphics g) {
    }
}
