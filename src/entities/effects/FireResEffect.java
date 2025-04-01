package entities.effects;

import entities.Entity;
import gfx.AssetStorage;

public class FireResEffect extends Effect{
    public FireResEffect(int duration, Entity e){
        super(duration, e);
        name = "Fire Resistance";
        icon = AssetStorage.images.get("fire_res_icon");
        t.backToStart();
    }
}
