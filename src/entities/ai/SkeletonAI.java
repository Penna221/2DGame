package entities.ai;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

import entities.Entity;
import entities.swords.Swords;
import tools.Timer;
import ui.Task;
import world.World;
public class SkeletonAI extends EnemyAI{
    private Ellipse2D.Double attackRadius;
    private Timer moveTimer, attackTimer;
    private boolean animate = true;
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
        attackTimer = new Timer(1500,attackTask);
        moveTimer = new Timer(250,moveTask);
        attackRadius = e.generateSurroundingCircle(100);
    }
    private void tryToAttack(){
        if(World.lineOfSightBetween(e, World.player)){
            if(attackRadius.intersects(World.player.bounds)){
                animate = false;
                float rot = World.getAngleBetween(World.player,e);
                float calcRot = rot+180;
                
                if(calcRot <90 || calcRot > 270){
                    e.setAnimation(e.info.animations.get("attack_left"));
                }else{
                    e.setAnimation(e.info.animations.get("attack_right"));
                }
                
                e.texture = e.currentAnimation.getFrame();
                Swords.createSwordAttack(e, null, Swords.poke, rot, 1, new Point((int)(e.bounds.getCenterX()),(int)(e.bounds.getCenterY())));
                // EntityManager.addAttackBox(new AttackBox(e, 1, e.bounds,null,World.getAngleBetween(e,World.player),AttackBox.MELEE));
            }else{
                animate = true;
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
                e.xSpeed = e.speed*Math.cos(rads);
                e.ySpeed = e.speed*Math.sin(rads);
            }else{
                e.slowdown(0.8);
            }
        }
    }
    @Override
    public void update() {
        e.move(animate);
        e.currentAnimation.animate();
        e.texture = e.currentAnimation.getFrame();
        moveTimer.update();
        attackTimer.update();
        e.updateBounds();
        attackRadius.x = e.x +e.bounds.width/2 - attackRadius.width/2;
        attackRadius.y = e.y +e.bounds.height/2 - attackRadius.height/2;
    }

    @Override
    public void render(Graphics g) {
        // Point2D p1 = new Point((int)e.bounds.getCenterX(),(int)e.bounds.getCenterY());
        // Point2D p2 = new Point((int)World.player.bounds.getCenterX(),(int)World.player.bounds.getCenterY());
        // int drawX1 = (int) (p1.getX() - World.camera.getXOffset()); 
        // int drawY1 = (int) (p1.getY() - World.camera.getYOffset()); 
        // int drawX2 = (int) (p2.getX() - World.camera.getXOffset()); 
        // int drawY2 = (int) (p2.getY() - World.camera.getYOffset()); 
        // g.drawLine(drawX1,drawY1,drawX2,drawY2);
        // g.drawOval((int)(attackRadius.x - World.camera.getXOffset()),(int) (attackRadius.y - World.camera.getYOffset()), (int)attackRadius.width,(int)attackRadius.height);
    }
    
}
