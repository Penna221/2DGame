package entities.ai;

import java.awt.Graphics;

import entities.Entity;

public class EmptyAI extends AI{

    public EmptyAI(Entity entity) {
        super(entity);
    }

    @Override
    public void lateInit() {
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics g) {
        
    }
    
}
