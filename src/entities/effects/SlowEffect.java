package entities.effects;

import entities.Entity;
import tools.Timer;
import ui.Task;

public class SlowEffect extends Effect{
    public Entity e;
    public double percentage;
    public SlowEffect(int duration, Entity e,double percentage){
        super(duration, e);
        name = "Slowness";
        this.e = e;
        this.percentage = percentage;

        Task task = new Task(){
            public void perform(){
                e.speedBuff = percentage;
            }
        };
        t = new Timer(1000,task);
        t.backToStart();
    }
}
