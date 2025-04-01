package entities.effects;

import java.awt.image.BufferedImage;

import entities.Entity;
import tools.Timer;
import ui.Task;

public abstract class Effect {
    public Timer timer, t;
    public String name = "effect";
    public BufferedImage icon;
    public Effect(int duration, Entity e){
        Effect eff = this;
        Task task = new Task(){
            public void perform(){
                e.removeEffect(eff);
            }
        };
        timer = new Timer(duration, task);
    }
    public void update(){
        if(t!=null){
            t.update();
        }
        timer.update();
        
    }
}
