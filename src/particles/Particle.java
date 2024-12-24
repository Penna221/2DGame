package particles;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import gfx.Animation;
import gfx.Animations;
import gfx.Factory;
import tools.Timer;
import ui.Task;
import world.World;

public class Particle {
    public String animation;
    public int lifespan;
    private Animation anim;
    private BufferedImage currentFrame;
    private int x, y;
    private Timer killTime;
    public float rotation;
    public Particle(String animation, int lifespan,float rotation){
        this.animation = animation;
        this.lifespan = lifespan;
        this.rotation = rotation;
        Task t1 = new Task(){
            public void perform(){
                remove();
            }
        };
        killTime = new Timer(lifespan,t1);
        anim = Animations.animations.get(animation);
        currentFrame = anim.getFrame();
    }
    public void remove(){
        World.particleManager.removeParticle(this);
    }
    public void setPosition(int x, int y){
       
        this.x = x;
        this.y = y;
    }
    public void render(Graphics g){
        currentFrame = anim.getFrame();
        if(currentFrame!=null){
            BufferedImage img = Factory.rotateImage(currentFrame, rotation);
            int drawX = (int)(x-World.camera.getXOffset());
            int drawY = (int)(y-World.camera.getYOffset());
            Factory.drawCenteredAt(g, img, new Point(drawX,drawY), 1);
            // g.drawImage(img, (int)(x - World.camera.getXOffset()),(int)(y-World.camera.getYOffset()),null);
        }
    }
    public void update(){
        if(anim!=null){
            anim.animate();
        }
        killTime.update();
            
    }
}
