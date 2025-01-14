package entities.ai;

import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import entities.AttackBox;
import entities.Entity;
import entities.EntityManager;
import entities.bows.Bow;
import entities.projectiles.Projectile;
import entities.staves.Staff;
import entities.swords.Sword;
import entities.swords.Swords;
import sound.SoundPlayer;
import tools.Timer;
import ui.Task;
import world.World;

public abstract class EnemyAI extends AI{
    public Timer chaseTimer;
    public Timer meleeTimer, rangedTimer, magicTimer;
    public Bow equippedBow;
    public Sword equippedSword;
    public Staff equippedStaff;
    public Projectile equippedProjectile;

    public Ellipse2D.Double meleeAttackRadius;
    public EnemyAI(Entity e){
        super(e);
        Task t = new Task(){
            public void perform(){
                chasePlayer();
            }
        };
        chaseTimer = new Timer(250,t);
        
        
        Task meleeAttack = new Task(){
            public void perform(){
                meleeAttack();
            }
        };
        meleeTimer = new Timer(1000,meleeAttack);
        
        Task rangedAttack = new Task(){
            public void perform(){
                rangedAttack();
            }
        };
        rangedTimer = new Timer(1000,rangedAttack);

        Task magicAttack = new Task(){
            public void perform(){
                magicAttack();
            }
        };
        magicTimer = new Timer(1000,magicAttack);

    }
    public float getDirectionTowardsPlayer(){
        float rot = World.getAngleBetween(World.player,e);
        float rads = (float) Math.toRadians(rot);
        return rads;
    }
    public void chasePlayer(){
        if(World.lineOfSightBetween(e, World.player)){
            float rads = getDirectionTowardsPlayer();
            e.xSpeed = e.speed*Math.cos(rads);
            e.ySpeed = e.speed*Math.sin(rads);
        }else{
            e.slowdown(0.7);
        }
    }

    public void meleeAttack(){
        if(World.lineOfSightBetween(e, World.player)){
            if(meleeAttackRadius.intersects(World.player.bounds)){
                int totalDamage = 1;
                if(equippedSword!=null){
                    totalDamage+= equippedSword.damage;
                    double dir = Math.toDegrees(getDirectionTowardsPlayer());
                    Swords.createSwordAttack(e, e, Swords.swing, (float)dir, totalDamage, new Point((int)e.bounds.getCenterX(),(int)e.bounds.getCenterY()));
                }else{
                    EntityManager.addAttackBox(new AttackBox(e, totalDamage, e.bounds,null,World.getAngleBetween(e,World.player),AttackBox.MELEE));
                }
                meleeTimer.backToStart();
            }
        }
    }
    public void rangedAttack(){

    }
    public void magicAttack(){
        World.generateProjectile(3, World.getAngleBetween(World.player, e), new Point((int)e.bounds.getCenterX(),(int)e.bounds.getCenterY()),e);
        Random r = new Random();
        int r1 = 1+ r.nextInt(3);
        SoundPlayer.playSound("magic_"+r1);
        int r2 = 1000 + (r.nextInt(4)*100);
        magicTimer.setTime(r2);
        magicTimer.backToStart();
    }
}
