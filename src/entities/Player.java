package entities;

import java.awt.Graphics;

import gfx.AssetStorage;
import io.KeyManager;
import world.World;

public class Player extends Entity{

    private double speed = 10;
    public Player(double x, double y) {
        super(x, y);
    }

    @Override
    public void init() {
        texture = AssetStorage.images.get("void");


        calculateBounds();
    }

    @Override
    public void update() {
        if(KeyManager.up){
            y-= speed;
        }
        if(KeyManager.down){
            y+= speed;
        }
        if(KeyManager.left){
            x-= speed;
        }
        if(KeyManager.right){
            x+= speed;
        }
    }

    @Override
    public void renderAdditional(Graphics g) {
        
    }
    
}
