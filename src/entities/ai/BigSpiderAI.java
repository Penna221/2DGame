package entities.ai;

import java.awt.Graphics;
import java.awt.geom.Ellipse2D;

import entities.AttackBox;
import entities.Entity;
import entities.EntityManager;
import tools.Timer;
import ui.Task;
import world.World;

public class BigSpiderAI extends EnemyAI{
    private Ellipse2D.Double attackRadius;
    private Timer moveTimer, attackTimer;
    public BigSpiderAI(Entity e){
        super(e);
    }
    @Override
    public void lateInit() {
        e.currentAnimation = e.info.animations.get("idle");
        e.currentAnimation.restart();
        Task moveTask = new Task(){
            public void perform(){getNewDirection();}
        };
        Task attackTask = new Task(){
            public void perform(){tryToAttack();}
        };
        attackTimer = new Timer(1500,attackTask);
        moveTimer = new Timer(3000,moveTask);
        attackRadius = e.generateSurroundingCircle(100);
    }
    private void tryToAttack(){
        if(World.lineOfSightBetween(e, World.player)){
            if(attackRadius.intersects(World.player.bounds)){
                EntityManager.addAttackBox(new AttackBox(e, 5, e.bounds,null,World.getAngleBetween(e,World.player),AttackBox.MELEE));
            }
        }
    }
    private void getNewDirection(){
        if(attackRadius.intersects(World.player.bounds)){
            e.xSpeed = 0;
            e.ySpeed = 0;
        }else{
            if(World.lineOfSightBetween(e, World.player)){
                float rot = World.getAngleBetween(World.player,e);
                float rads = (float) Math.toRadians(rot);
                System.out.println(rot);
                e.xSpeed = e.speed*Math.cos(rads);
                e.ySpeed = e.speed*Math.sin(rads);
            }
        }
    }
    @Override
    public void update() {
        e.currentAnimation.animate();
        e.slowdown(0.89);
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
        e.drawBounds(g);
        g.drawOval((int)(attackRadius.x - World.camera.getXOffset()),(int)(attackRadius.y- World.camera.getYOffset()),(int)attackRadius.width,(int)attackRadius.height);
        
    }
    
}
