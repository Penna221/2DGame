package states;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import entities.EntityInfo;
import entities.EntityManager;
import loot.Market;
import main.Game;
import ui.Container;
import ui.PauseMenu;
import utils.pennanen.GameInstance;

public class BlacksmithState extends State {
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
        g.fillRect(0, 0, GameInstance.window.width, GameInstance.window.height);
        g.setColor(Color.red);
        g.drawString("Blacksmith State", 25, 25);
        c.render(g);
        if(transition !=null){transition.render(g);}
    }

    @Override
    public void init() {
        PauseMenu.generateNewContainer("blacksmith");
        c = PauseMenu.containers.get("blacksmith");
        PauseMenu.setContainer(c);
    }

    @Override
    public void updateOnceBetweenTransitions() {

    }
    
}
