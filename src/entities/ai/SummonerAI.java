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

public class SummonerAI extends AI{
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
            magicAttack();
        }else{
            attacking = false;
        }
        
    }
    
    private void magicAttack(){
        World.generateProjectile(3, World.getAngleBetween(World.player, e), new Point((int)e.x,(int)e.y),e);
        Random r = new Random();
        int r1 = 1+ r.nextInt(3);
        SoundPlayer.playSound("magic_"+r1);
    }
    private void summonAttack(){

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
