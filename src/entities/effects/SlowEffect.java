package entities.effects;

import entities.Entity;
import gfx.AssetStorage;
import tools.Timer;
import ui.Task;

public class SlowEffect extends Effect{
    public Entity e;
    public double percentage;
    public SlowEffect(int duration, Entity e,double percentage){
        super(duration, e);
        name = "Slowness";
        icon = AssetStorage.images.get("slow_icon");
        this.e = e;
        this.percentage = percentage;
        e.speedBuff = percentage;

        Task task = new Task(){
            public void perform(){
            }
        };
        t = new Timer(1000,task);
        t.backToStart();
    }
}
