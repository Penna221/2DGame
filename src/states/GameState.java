package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import gfx.AssetStorage;
import tiles.Tile;

public class GameState extends State{

    @Override
    public void update() {
        
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Serif",Font.BOLD,25));
        g.drawString("Game", 25, 50);
        g.drawImage(Tile.getTileByID(3).texture, 25, 25, null);
    }
    
}
