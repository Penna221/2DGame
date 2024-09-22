package states;

import java.awt.Color;
import java.awt.Graphics;

import main.Game;
import ui.Container;
import ui.PauseMenu;

public class WitchState extends State{
    private Container c;
    @Override
    public void update() {
        if(running){
            c.update();
        }else{
            State.transition.update();
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, Game.w.getWidth(), Game.w.getHeight());
        g.setColor(Color.red);
        g.drawString("Witch State", 25, 25);
        c.render(g);
        if(transition !=null){transition.render(g);}
    }

    @Override
    public void init() {
        PauseMenu.generateNewContainer("witch");
        c = PauseMenu.containers.get("witch");
        PauseMenu.setContainer(c);
    }

    @Override
    public void updateOnceBetweenTransitions() {

    }
    
}
