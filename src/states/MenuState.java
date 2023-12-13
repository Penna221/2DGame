package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import gfx.Transition;
import main.Game;

public class MenuState extends State{

    private boolean alreadyInitialized = false;
    @Override
    public void update() {
        if(running){
        }else{
            transition.update();
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Serif",Font.BOLD,25));
        g.drawString("MENU", 25, 50);
        if(transition !=null){
            transition.render(g);
        }
    }

    @Override
    public void init() {
        if(!alreadyInitialized){
            alreadyInitialized = true;
        }
    }

    @Override
    public void updateOnceBetweenTransitions() {
        
    }
    
}
