package entities;

import entities.mushrooms.MushroomData;
import gfx.Factory;

public abstract class Collectable extends Entity{

    public int value;
    public int rarity;
    public int id;
    public Collectable(double x, double y) {
        super(x, y);
        
    }
    public void loadMushroomData(int i){
        MushroomData d = MushroomData.findWithID(i);
        if(d == null){
            //This mushroom does not exist

        }else{
            id = d.id;
            name = d.name;
            texture = d.texture;
            highlight = Factory.highlightEdges(texture);
            rarity = d.rarity;
            value = d.value;
        }
    }
    public abstract void collect();
}
