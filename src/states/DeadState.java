package states;

import java.awt.Graphics;

import gfx.Transition;
import main.Game;
import ui.Container;
import ui.PauseMenu;

public class DeadState extends State{

    private boolean alreadyInitialized = false;
    private Container container;
    @Override
    public void update() {
        if(running){
            int x = Game.w.getWidth() /2 - container.bounds.width/2;
            int y = Game.w.getHeight() /2- container.bounds.height/2;
            container.setPosition(x, y);
            
            // container.centerElements();
            // container.spaceOutVertically(35);
            container.update();
        }else{
            State.transition.update();
        }
    }

    @Override
    public void render(Graphics g) {
        State.gameState.render(g);
        container.render(g);
        if(transition !=null){
            transition.render(g);
        }
    }

    @Override
    public void init() {
        System.out.println("DeadState init");
        container = PauseMenu.containers.get("dead");
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
