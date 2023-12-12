package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import world.World;

public class GameState extends State{
    public World world;
    private boolean alreadyInitialized = false;
    @Override
    public void update() {
        world.update();
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Serif",Font.BOLD,25));
        g.drawString("Game", 25, 50);
        world.render(g);
    }

    @Override
    public void init() {
        if(!alreadyInitialized){
            world = new World(World.FOREST);
        }
    }
    
}
