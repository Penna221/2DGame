package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

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
        g.setColor(Color.white);
        g.setFont(new Font("Serif",Font.BOLD,25));
        g.drawString("Settings", 25, 50);
        
        if(transition !=null){
            transition.render(g);
        }
    }

    @Override
    public void init() {
        
    }

    @Override
    public void updateOnceBetweenTransitions() {
        
    }
    
}
