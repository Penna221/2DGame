package entities;

import java.awt.Graphics;

import tiles.Tile;

public class SpruceTree extends Creature{

    public SpruceTree(double x, double y) {
        super(x, y);
    }

    @Override
    public void init() {
        info = EntityManager.entityInfos.get("spruceTree");
        texture = info.texture;
        speed = info.speed;
        health =info.health;
        x += texture.getWidth()/2 - Tile.tileSize/2;
        y -= texture.getHeight() + Tile.tileSize/2;
        calculateBounds();
    }

    @Override
    public void renderAdditional(Graphics g) {
        drawBounds(g);
    }

    @Override
    public void update() {
        
    }
    
}
