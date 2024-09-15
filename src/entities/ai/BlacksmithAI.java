package entities.ai;

import java.awt.Graphics;

import entities.Entity;

public class BlacksmithAI extends AI {

    public BlacksmithAI(Entity entity) {
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
        e.updateBounds();
        e.currentAnimation.animate();
    }

    @Override
    public void render(Graphics g) {

    }
    
}
