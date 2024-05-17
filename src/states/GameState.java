package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import ui.PauseMenu;
import ui.UiHub;
import world.World;

public class GameState extends State{
    public World world;
    //private ToggleButton button2;
    private PauseMenu pauseMenu;
    public static boolean paused = false;;
    private boolean alreadyInitialized = false;
    @Override
    public void update() {
        if(running){
            if(!paused){
                world.update();
            }else{
                pauseMenu.update();
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
        if(paused)
            pauseMenu.render(g);
        
        if(transition !=null){
            transition.render(g);
        }
    }

    @Override
    public void init() {
        UiHub.clear();
        System.out.println("Gamestate init");
        if(!alreadyInitialized){
            // world = new World(World.FOREST);
            world = new World();
            World.load("lobby");
            pauseMenu = new PauseMenu();
            alreadyInitialized = true;
        }
        pauseMenu.init();
    }

    @Override
    public void updateOnceBetweenTransitions() {
        world.update();
    }
    
}
