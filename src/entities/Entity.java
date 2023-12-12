package entities;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import world.World;

public abstract class Entity {
    public double x, y;
    public Rectangle bounds;

    public String name;
    public BufferedImage texture;
    public boolean inView = false;
    public Entity(double x, double y){
        this.x = x;
        this.y = y;
        init();
    }
    public void setTexture(BufferedImage img){
        this.texture = img;
    }
    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }
    public void calculateBounds(){
        bounds = new Rectangle((int)x,(int)y,texture.getWidth(),texture.getHeight());
    }
    public void updateBounds(){
        bounds.x = (int)x;
        bounds.y = (int)y;
    }
    public abstract void init();
    public abstract void renderAdditional(Graphics g);
    public abstract void update();
    public void render(Graphics g){
        double xOffset = World.camera.getXOffset();
        double yOffset = World.camera.getYOffset();
        g.drawImage(texture, (int)(x - xOffset), (int)(y - yOffset), null);
        renderAdditional(g);
    }
}
