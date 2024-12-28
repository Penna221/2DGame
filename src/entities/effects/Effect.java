package entities.effects;

import entities.Entity;
import tools.Timer;
import ui.Task;

public abstract class Effect {
    public Timer timer, t;
    public String name = "effect";
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
        t.update();
        timer.update();
        
    }
}
