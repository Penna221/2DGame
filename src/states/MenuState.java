package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class MenuState extends State{

    @Override
    public void update() {
        
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Serif",Font.BOLD,25));
        g.drawString("MENU", 25, 50);
    }

    @Override
    public void init() {
        
    }
    
}
