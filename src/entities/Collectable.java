package entities;

import entities.mushrooms.MushroomData;

public abstract class Collectable extends Entity{

    public int value;
    public int rarity;
    public Collectable(double x, double y) {
        super(x, y);
        
    }
    public void loadMushroomData(int id){
        MushroomData d = MushroomData.findWithID(id);
        if(d == null){
            //This mushroom does not exist

        }else{
            name = d.name;
            texture = d.texture;
            rarity = d.rarity;
            value = d.value;
        }
    }
    public abstract void collect();
}
