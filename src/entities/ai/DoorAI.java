package entities.ai;

import java.awt.Graphics;

import entities.Entity;
import gfx.Transition;
import states.State;
import world.World;

public class DoorAI extends AI{

    public DoorAI(Entity e) {
        super(e);
    }

    @Override
    public void lateInit() {
    }

    @Override
    public void update() {
        if(e.bounds.intersects(World.player.bounds)){
            World.load(e.info.tunnel);
            
        }
    }
    @Override
    public void render(Graphics g) {
    }
    
}
