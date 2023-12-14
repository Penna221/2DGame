package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import gfx.Transition;
import main.Game;
import ui.Container;
import ui.Text;

public class MenuState extends State{
    private boolean alreadyInitialized = false;
    
    
    private Container container;
    private Text t;
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
        container.render(g);
        t.render(g);
        if(transition !=null){
            transition.render(g);
        }
    }

    @Override
    public void init() {
        container = new Container(20, 20, 500, 500);
        t = new Text("ABC CDFG PSÄÖ",100,100,100);
        if(!alreadyInitialized){
            alreadyInitialized = true;
        }
    }

    @Override
    public void updateOnceBetweenTransitions() {
        
    }
    
}
