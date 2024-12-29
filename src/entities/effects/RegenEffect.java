package entities.effects;


import entities.Entity;
import gfx.AssetStorage;
import tools.Timer;
import ui.Task;

public class RegenEffect extends Effect{
    public Entity e;
    public int amount;
    public RegenEffect(int duration, Entity e, int amount){
        super(duration, e);
        name = "Regeneration";
        icon = AssetStorage.images.get("regen_icon");
        this.e = e;
        this.amount = amount;
        Task task = new Task(){
            public void perform(){
                e.heal(amount);
            }
        };
        t = new Timer(1500,task);
        t.backToStart();
    }
}
