package entities.ai;

import java.awt.Graphics;

import entities.Entity;

public class TraderAI extends AI{

    public TraderAI(Entity entity) {
        super(entity);
    }

    @Override
    public void lateInit() {
        e.currentAnimation = e.info.animations.get("idle");
        e.currentAnimation.restart();
    }

    @Override
    public void update() {
        e.texture = e.currentAnimation.getFrame();
        e.currentAnimation.animate();
    }

    @Override
    public void render(Graphics g) {
        
    }
    
}
