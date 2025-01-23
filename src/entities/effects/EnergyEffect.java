package entities.effects;

import entities.Entity;
import entities.ai.PlayerAI;
import gfx.AssetStorage;
import tools.Timer;
import ui.Task;

public class EnergyEffect extends Effect{
    public EnergyEffect(int duration, Entity e){
        super(duration, e);
        name = "Extra Energy";
        icon = AssetStorage.images.get("extra_energy_icon");
        Task task = new Task(){
            public void perform(){
                PlayerAI.energy++;
            }
        };
        t = new Timer(700,task);
        t.backToStart();
    }
}
