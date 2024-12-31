package entities.effects;

import entities.Entity;
import gfx.AssetStorage;
import tools.Timer;
import ui.Task;

public class SpeedEffect extends Effect{
    public Entity e;
    public double percentage;
    public SpeedEffect(int duration, Entity e,double percentage){
        super(duration, e);
        name = "Speed";
        icon = AssetStorage.images.get("speed_icon");
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
