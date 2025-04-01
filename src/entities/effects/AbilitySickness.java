package entities.effects;

import entities.Entity;
import gfx.AssetStorage;

public class AbilitySickness extends Effect{

    public AbilitySickness(int duration, Entity e) {
        super(duration, e);
        name = "Ability Sickness";
        icon = AssetStorage.images.get("abort");
    }
    
}
