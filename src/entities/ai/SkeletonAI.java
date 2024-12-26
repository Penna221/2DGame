package entities.ai;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import entities.AttackBox;
import entities.Entity;
import entities.EntityManager;
import tools.Timer;
import ui.Task;
import world.World;
public class SkeletonAI extends AI{
    private Ellipse2D.Double attackRadius;
    private Timer moveTimer, attackTimer;
    public SkeletonAI(Entity e){
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
        moveTimer = new Timer(500,moveTask);
        attackRadius = e.generateSurroundingCircle(50);
    }
    private void tryToAttack(){
        if(World.lineOfSightBetween(e, World.player)){
            if(attackRadius.intersects(World.player.bounds)){
                EntityManager.addAttackBox(new AttackBox(e, 1, e.bounds,null,World.getAngleBetween(e,World.player)));
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
        e.slowdown(0.9);
        moveTimer.update();
        attackTimer.update();
        e.move();
        e.updateBounds();
        attackRadius.x = e.x +e.bounds.width/2 - attackRadius.width/2;
        attackRadius.y = e.y+ e.bounds.height/2- attackRadius.height/2;
    }

    @Override
    public void render(Graphics g) {
        Point2D p1 = new Point((int)e.bounds.getCenterX(),(int)e.bounds.getCenterY());
        Point2D p2 = new Point((int)World.player.bounds.getCenterX(),(int)World.player.bounds.getCenterY());
        int drawX1 = (int) (p1.getX() - World.camera.getXOffset()); 
        int drawY1 = (int) (p1.getY() - World.camera.getYOffset()); 
        int drawX2 = (int) (p2.getX() - World.camera.getXOffset()); 
        int drawY2 = (int) (p2.getY() - World.camera.getYOffset()); 
        g.drawLine(drawX1,drawY1,drawX2,drawY2);
        g.drawOval((int)(attackRadius.x - World.camera.getXOffset()),(int) (attackRadius.y - World.camera.getYOffset()), (int)attackRadius.width,(int)attackRadius.height);
    }
    
}
