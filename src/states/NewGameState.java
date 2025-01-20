package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import gfx.Transition;
import ui.Container;
import ui.PauseMenu;

public class NewGameState extends State{
    private boolean alreadyInitialized = false;

    private Container container;
    @Override
    public void update() {
        if(running){
            container.update();
        }else{
            State.transition.update();
        }
    }

    @Override
    public void render(Graphics g) {
        container.render(g);
        if(transition !=null){
            transition.render(g);
        }
    }

    @Override
    public void init() {
        // UiHub.clear();
        System.out.println("New Game State init");
        container = PauseMenu.containers.get("newgame");
        PauseMenu.setContainer(container);
        if(!alreadyInitialized){
            alreadyInitialized = true;
        }
        Transition.canContinue = true;
    }

    @Override
    public void updateOnceBetweenTransitions() {
        
        container.update();

    }
    
}
