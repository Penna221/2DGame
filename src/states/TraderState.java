package states;

import java.awt.Color;
import java.awt.Graphics;

import gfx.Transition;
import main.Game;

public class TraderState extends State {

    @Override
    public void update() {
        if(running){
            
        }else{
            State.transition.update();
        }

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.blue);
        g.fillRect(0, 0, Game.w.getWidth(), Game.w.getHeight());
        g.setColor(Color.black);
        g.drawString("TraderState", 25, 25);
        if(transition !=null){transition.render(g);}
    }

    @Override
    public void init() {
        System.out.println("TraderState init");
        // Transition.canContinue = true;
    }

    @Override
    public void updateOnceBetweenTransitions() {
    
    }

}
