package entities.effects;

import entities.Entity;
import gfx.AssetStorage;
import tools.Timer;
import ui.Task;

public class NoCollisionEffect extends Effect {
    public NoCollisionEffect(int duration, Entity e){
        super(duration, e);
        e.noCollision = true;
        name = "No Collision";
        icon = AssetStorage.images.get("no_collision_icon");
        Task task = new Task(){
            public void perform(){
                e.noCollision = false;
            }
        };
        t = new Timer(duration,task);
        t.backToStart();
    }
}
