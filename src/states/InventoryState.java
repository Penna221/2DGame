package states;

import java.awt.Graphics;
import java.util.ArrayList;

import cards.Card;
import entities.ai.PlayerAI;
import main.Game;
import ui.ClickButton;
import ui.Container;
import ui.PauseMenu;
import ui.Task;

public class InventoryState extends State{
    private Container container;
    private ArrayList<ClickButton> choises = new ArrayList<ClickButton>();
    
    private Card chosenOne = null;
    @Override
    public void update() {
        if(running){
            
            Container.focus = false;
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
        container = new Container(0, 0, Game.w.getWidth(), Game.w.getHeight());
        for(Card c : PlayerAI.cardBag){
            ClickButton ca = generateCardButton(c);
            container.addElement(ca);
            choises.add(ca);
        }
        // container.swap();
        container.spaceOutVertically(10);
        PauseMenu.setContainer(container);
    }
    private ClickButton generateCardButton(Card c){
        ClickButton b = new ClickButton(0, 0, c.texture);
        b.setTask(new Task(){
            public void perform(){
                for(ClickButton cb : choises){
                    cb.selected = false;
                }
                b.selected = true;
                System.out.println("picked " +c.name);
                chosenOne = c;
            }
        });
        return b;
    }
    @Override
    public void updateOnceBetweenTransitions() {
    }

}
