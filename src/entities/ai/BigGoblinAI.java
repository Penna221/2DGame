package entities.ai;

import java.awt.Graphics;

import entities.Entity;

public class BigGoblinAI extends AI{
    public BigGoblinAI(Entity e){
        super(e);
        e.setAnimation(e.info.animations.get("idle"));
    }

    @Override
    public void lateInit() {

    }

    @Override
    public void update() {
        e.slowdown(0.7);
        e.move(true);
        e.currentAnimation.animate();
        e.texture = e.currentAnimation.getFrame();
    }

    @Override
    public void render(Graphics g) {

    }
}
