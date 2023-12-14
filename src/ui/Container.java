package ui;

import java.awt.Graphics;

public class Container extends UIElement{

    public Container(int x, int y, int width, int height) {
        super(x, y, width, height);
        data = UIFactory.containerData;
    }


    @Override
    public void render(Graphics g) {
        g.setColor(data.bgColor);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }
    
}
