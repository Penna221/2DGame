package entities.player;

import java.util.HashMap;

import entities.Entity;
import entities.effects.NoCollisionEffect;
import ui.Task;
import world.World;

public class Ability {
    public Task t;
    public static HashMap<Integer,Ability> abilities;
    public Ability(Task t){
        this.t = t;
    }
    public static void createAbilities(){
        abilities = new HashMap<Integer,Ability>();
        Task dash = new Task(){public void perform(){
            World.player.applyEffect(new NoCollisionEffect(500, World.player));
            World.player.giveMomentum(World.getPlayerRotationToCursor(), 50);
        }};
        abilities.put(0, new Ability(dash));




    }
    public void perform(){
        t.perform();
    }
}
