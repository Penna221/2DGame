package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import entities.ai.PlayerAI;
import entities.player.Inventory;
import save.SavedGame;
import ui.PauseMenu;
import world.World;

public class GameState extends State{
    public static World world;
    public static boolean paused = false;
    private static boolean alreadyInitialized = false;
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
    public static void newGame(String name){
        // paused = false;
        paused = true;
        SavedGame.startNewSave(name);
        world = new World();
        World.load("lobby","");
        PlayerAI.inv = new Inventory();
        alreadyInitialized = true;
        State.setState(gameState, false);
    }
    public static void loadGame(String savedGame){
        paused = true;
        
        world = new World();
        World.load("lobby",savedGame);
        
        alreadyInitialized = true;
        State.setState(gameState, false);
    }
    @Override
    public void init() {
        System.out.println("Gamestate init");
        if(!alreadyInitialized){
            world = new World();
            World.load("lobby","");
            alreadyInitialized = true;
        }
        paused = false;
    }

    @Override
    public void updateOnceBetweenTransitions() {
        world.update();
    }
    
}
