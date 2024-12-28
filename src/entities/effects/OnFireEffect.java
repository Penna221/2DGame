package entities.effects;

import entities.Entity;
import tools.Timer;
import ui.Task;

public class OnFireEffect extends Effect{
    public OnFireEffect(int duration, Entity e){
        super(duration, e);
        name = "On Fire";
        Task task = new Task(){
            public void perform(){
                if(!e.checkEffect("Fire Resistance")){
                    e.harm(2);
                }
            }
        };
        t = new Timer(1500,task);
        t.backToStart();
    }
}
