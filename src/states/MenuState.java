package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main.Game;
import ui.ClickButton;
import ui.Container;
import ui.Text;

public class MenuState extends State{
    private boolean alreadyInitialized = false;
    
    private ClickButton button1;
    
    private Container container;
    private Text t, title;
    @Override
    public void update() {
        if(running){
            button1.update();
        }else{
            State.transition.update();
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Serif",Font.BOLD,25));
        g.drawString("MENU", 25, 50);
        container.render(g);
        if(transition !=null){
            transition.render(g);
        }
    }

    @Override
    public void init() {
        container = new Container(0, 0, Game.w.getWidth(),Game.w.getHeight());
        t = new Text("Start",100,100,100);
        title = new Text("Game title",0,100,100);

        button1 = new ClickButton(100, 200, t){
            @Override
            public void task(){
                State.setState(State.gameState);
            }
        };
        container.addElement(title);
        container.addElement(button1);
        container.centerElements();
        if(!alreadyInitialized){
            alreadyInitialized = true;
        }
    }

    @Override
    public void updateOnceBetweenTransitions() {
        
    }
    
}
