package entities.effects;

import entities.Entity;
import tools.Timer;
import ui.Task;

public class FireResEffect extends Effect{
    public FireResEffect(int duration, Entity e){
        super(duration, e);
        name = "Fire Resistance";
        Task task = new Task(){
            public void perform(){
                
            }
        };
        t = new Timer(1500,task);
        t.backToStart();
    }
}
