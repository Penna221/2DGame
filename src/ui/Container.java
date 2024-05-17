package ui;

import java.awt.Graphics;
import java.util.ArrayList;

public class Container extends UIElement{
    public ArrayList<UIElement> elements;
    public UIElement header;
    public Container(int x, int y, int width, int height) {
        super(x, y, width, height);
        elements = new ArrayList<UIElement>();
        data = UIFactory.containerData;
    }
    public void setHeader(UIElement el){
        this.header = el;
        int newX = bounds.x+bounds.width/2-header.bounds.width/2;
        int newY = bounds.y+header.bounds.height/3;
        header.bounds.setLocation(newX, newY);
    }
    public void centerElements(){
        for(UIElement e : elements){
            int w = e.bounds.width;
            
            e.bounds.x = bounds.x  + bounds.width/2-w/2;

        }
        if(header !=null){
            header.bounds.x = bounds.x + bounds.width/2 - header.bounds.width/2;
        }
    }
    public void spaceOutVertically(int buffer){
        int count = elements.size();
        int space = bounds.height;
        int startY = y;
        if(header!=null){
            startY = header.bounds.y+header.bounds.height+buffer*2;
        }
        for(UIElement e : elements){
            int h = e.bounds.height;
            e.bounds.y = startY;
            
            startY += buffer + h;
        }
        
    }
    public void makeGrid(int rows){
        int forRow = elements.size() / rows;
        
        int i = 0;
        int rowIndex = 0;
        int buffer = 300;
        int availableHeight = bounds.height-header.bounds.height;
        int ws = (bounds.width-buffer)/forRow;
        for(UIElement e: elements){
            if(i > forRow){
                rowIndex++;
                i-=forRow+1;
                
            }
            int w = e.bounds.width;
            int newX = bounds.x + buffer/2+(ws*i) - w/2;
            // System.out.println(newX);
            int startY = 0;
            int newY = 0;
            if(header!=null){
                startY = header.bounds.y + header.bounds.height*2;
            }
            newY = startY + (rowIndex*availableHeight/rows);
            e.bounds.setLocation(newX, newY);
            
            i++;
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
        if(header!=null)header.render(g);
        //Draw elements
        for(UIElement e : elements){
            e.render(g);
            
        }
    }
    public void update(){
        for(UIElement e : elements){
            if(e instanceof FunctionalElement){
                FunctionalElement ee = (FunctionalElement)e;
                ee.update();
            }
                
        }
    }
    
}
