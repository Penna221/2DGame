package entities.effects;

import entities.Entity;
import gfx.AssetStorage;
import tools.Timer;
import ui.Task;

public class FireResEffect extends Effect{
    public FireResEffect(int duration, Entity e){
        super(duration, e);
        name = "Fire Resistance";
        icon = AssetStorage.images.get("fire_res_icon");
        Task task = new Task(){
            public void perform(){
                
            }
        };
        t = new Timer(1500,task);
        t.backToStart();
    }
}
