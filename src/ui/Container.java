package ui;

import java.awt.Graphics;
import java.util.ArrayList;

public class Container extends UIElement{
    public ArrayList<UIElement> elements;
    public Container(int x, int y, int width, int height) {
        super(x, y, width, height);
        elements = new ArrayList<UIElement>();
        data = UIFactory.containerData;
    }

    public void centerElements(){
        for(UIElement e : elements){
            int w = e.bounds.width;
            e.bounds.x = bounds.width/2-w/2;
        }
    }

    public void addElement(UIElement e){
        elements.add(e);
    }

    @Override
    public void render(Graphics g) {
        //Draw container
        g.setColor(data.bgColor);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

        //Draw elements
        for(UIElement e : elements){
            e.render(g);
            
        }
    }
    
}
