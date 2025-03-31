package entities.abilities;

import entities.Entity;
import entities.effects.NoCollisionEffect;

public class Abilities {
    public static void dash(Entity e, float angle){
        e.applyEffect(new NoCollisionEffect(500, e));
        e.giveMomentum(angle,50);
    }
}
