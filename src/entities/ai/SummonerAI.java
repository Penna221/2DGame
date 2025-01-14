package entities.ai;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import entities.Entity;
import sound.SoundPlayer;
import tools.Timer;
import ui.Task;
import world.World;

public class SummonerAI extends EnemyAI{
    private boolean attacking = false;
    private Ellipse2D.Double attackRadius;
    private Timer attackTimer;
    public SummonerAI(Entity e){
        super(e);
    }

    @Override
    public void lateInit() {
        
        Task attackTask = new Task(){
            public void perform(){tryToAttack();}
        };
        attackTimer = new Timer(3000,attackTask);
        attackRadius = e.generateSurroundingCircle(500);
    }
    public void tryToAttack(){
        if(World.lineOfSightBetween(e, World.player)){
            attacking = true;
            Random r = new Random();
            int rr = r.nextInt(10);

            if(rr==1){
                summonAttack();
            }else{
                magicAttack();
            }
            
        }else{
            attacking = false;
        }
        
    }
    private void summonAttack(){
        int amount = 5;
        int angle = -180;
        int change = 360 /amount;
        for(int i = 0; i < 5; i++){
            double xOff = Math.cos(Math.toRadians(angle))*150;
            double yOff = Math.sin(Math.toRadians(angle))*150;
            angle += change;
            Entity b = World.entityManager.spawnEntity(15, -1, e.bounds.getCenterX()+xOff, e.bounds.getCenterY()+yOff);
            
            // e.homeRoom.addEntity(b);

        }
        attackTimer.setTime(10000);
        attackTimer.backToStart();
    }
    @Override
    public void update() {
        e.slowdown(0.9);
        e.move(false);
        if(attacking){
            e.currentAnimation = e.info.animations.get("attack");
        }
        e.updateBounds();
        e.currentAnimation.animate();
        e.texture = e.currentAnimation.getFrame();
        attackTimer.update();
    }

    @Override
    public void render(Graphics g) {

    }
    
}
