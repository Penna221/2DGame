package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import ui.PauseMenu;
import world.World;

public class GameState extends State{
    public World world;
    //private ToggleButton button2;
    public static boolean paused = false;
    private boolean alreadyInitialized = false;
    @Override
    public void update() {
        if(running){
            if(!paused){
                world.update();
            }else{
                if(PauseMenu.currentContainer!=null){
                    PauseMenu.currentContainer.update();
                }
            }
            
        }else{
            State.transition.update();
        }
    }

    @Override
    public void render(Graphics g) {
        
        world.render(g);
        g.setColor(Color.white);
        g.setFont(new Font("Serif",Font.BOLD,25));
        g.drawString("Game", 25, 50);
        if(paused){
            if(PauseMenu.currentContainer!=null){
                PauseMenu.currentContainer.render(g);
            }
        }
        
        if(transition !=null){
            transition.render(g);
        }
    }

    @Override
    public void init() {
        // UiHub.clear();
        System.out.println("Gamestate init");
        paused = false;
        if(!alreadyInitialized){
            // world = new World(World.FOREST);
            world = new World();
            World.load("lobby");
            alreadyInitialized = true;
        }
    }

    @Override
    public void updateOnceBetweenTransitions() {
        world.update();
    }
    
}
