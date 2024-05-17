package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import ui.UiHub;

public class SettingsState extends State{

    @Override
    public void update() {
        if(running){
            
        }else{
            State.transition.update();
        }
    }

    @Override
    public void render(Graphics g) {
        UiHub.clear();
        g.setColor(Color.white);
        g.setFont(new Font("Serif",Font.BOLD,25));
        g.drawString("Settings", 25, 50);
        
        if(transition !=null){
            transition.render(g);
        }
    }

    @Override
    public void init() {
        System.out.println("Menustate init");
    }

    @Override
    public void updateOnceBetweenTransitions() {
        
    }
    
}
