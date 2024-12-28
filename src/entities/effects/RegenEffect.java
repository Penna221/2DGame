package entities.effects;


import entities.Entity;
import tools.Timer;
import ui.Task;

public class RegenEffect extends Effect{
    public Entity e;
    public int amount;
    public String name = "Regeneration";
    public RegenEffect(int duration, Entity e, int amount){
        super(duration, e);
        name = "Regeneration";
        this.e = e;
        this.amount = amount;
        Task task = new Task(){
            public void perform(){
                e.heal(amount);
            }
        };
        t = new Timer(1000,task);
        t.backToStart();
    }
}