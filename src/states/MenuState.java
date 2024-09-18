package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import gfx.Transition;
import main.Game;
import ui.Container;
import ui.PauseMenu;
import ui.UiHub;

public class MenuState extends State{
    private boolean alreadyInitialized = false;
    
    
    private Container container;
    @Override
    public void update() {
        if(running){
            int x = Game.w.getWidth() /2 - container.bounds.width/2;
            int y = Game.w.getHeight() /2- container.bounds.height/2;
            container.setPosition(x, y);
            
            container.centerElements();
            container.spaceOutVertically(35);
            container.update();
        }else{
            State.transition.update();
        }
    }

    @Override
    public void render(Graphics g) {
        container.render(g);
        g.setColor(Color.white);
        g.setFont(new Font("Serif",Font.BOLD,25));
        g.drawString("MENU", 25, 50);
        if(transition !=null){
            transition.render(g);
        }
    }

    @Override
    public void init() {
        // UiHub.clear();
        System.out.println("Menustate init");
        container = PauseMenu.containers.get("main");
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
