package states;

import java.awt.Graphics;

import gfx.Transition;

import java.awt.Color;

import main.Game;
import ui.ClickButton;
import ui.Text;

public class BlacksmithState extends State {
    private ClickButton returnButton;
    @Override
    public void update() {
        if(running){
            returnButton.update();
        }else{
            State.transition.update();
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, Game.w.getWidth(), Game.w.getHeight());
        g.setColor(Color.red);
        g.drawString("Blacksmith State", 25, 25);
        returnButton.render(g);
        if(transition !=null){transition.render(g);}
    }

    @Override
    public void init() {
        returnButton = new ClickButton(10,10,new Text("Return",0,0,100,false)){
            @Override
            public void task(){
                State.setState(gameState, true);
                
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
    }

    @Override
    public void updateOnceBetweenTransitions() {

    }
    
}
