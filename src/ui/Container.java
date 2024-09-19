package ui;

import java.awt.Graphics;
import java.util.ArrayList;

public class Container extends UIElement{
    public ArrayList<UIElement> elements;
    public ArrayList<UIElement> toShow;
    public UIElement header;
    public boolean fillBg = false;
    public int start=0, end;
    public boolean isList = false;
    public Container(int x, int y, int width, int height) {
        super(x, y, width, height);
        elements = new ArrayList<UIElement>();
        data = UIFactory.containerData;
    }
    public void sendClick(){
        for(UIElement e : elements){
            if(e instanceof Container){
                ((Container)e).sendClick();
            }
            if(e instanceof FunctionalElement){
                FunctionalElement ee = (FunctionalElement)e;
                ee.click();
            }
                
        }
    }
    public void sendPress(){
        for(UIElement e : elements){
            if(e instanceof FunctionalElement){
                FunctionalElement ee = (FunctionalElement)e;
                ee.press();
            }
                
        }
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
    public void centerVertically(){
        for(UIElement e : elements){
            e.setPosition(e.x, bounds.height/2 - e.bounds.height/2);
        }
    }
    public void spaceHorizintally(int buffer){
        int totalWidth = 0;
        for(UIElement e : elements){
            totalWidth += e.bounds.getWidth();
        }
        int c = (int)bounds.getWidth()/2;
        int currentX = 0;
        for(UIElement e : elements){
            e.setPosition(currentX, 0);
            currentX += e.bounds.getWidth();
        }
        
    }
    public void updateList(){
        end = start+3;
        int max = elements.size()-2;
        if(end > max){
            end = max;
            start= end-3;
        }
        if(start < 0){
            start = 0;

        }
        toShow = new ArrayList<UIElement>();
        int lastY = 0;
        System.out.println("Showing list from " + start + " to " +end);
        for(int i = start; i < end; i++){
            
            UIElement e = elements.get(i);
            e.setPosition(0, lastY);
            e.updateBounds();
            lastY += e.bounds.getHeight()+20;
            toShow.add(e);
        }

        toShow.add(elements.get(elements.size()-1));
        toShow.add(elements.get(elements.size()-2));
        updateBounds();
    }
    public void calculateBounds(){
        int currentMaxX = 0;
        int currentMaxY = 0;
        for(UIElement e : elements){
            int xx = e.bounds.x + e.bounds.width;
            int yy = e.bounds.y + e.bounds.height;
            if(xx > currentMaxX){
                currentMaxX = xx;
            }
            if(yy > currentMaxY){
                currentMaxY = yy;
            }
        }
        bounds.width = currentMaxX;
        bounds.height = currentMaxY;
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
        e.setOffset(x, y);
        elements.add(e);
    }

    @Override
    public void render(Graphics g) {
        //Draw container
        if(fillBg){
            g.setColor(data.bgColor);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

        }
        if(header!=null)header.render(g);
        //Draw elements

        if(isList){
            ArrayList<UIElement> copy = new ArrayList<UIElement>();
            copy.addAll(toShow);
            for(UIElement e :copy){
                e.render(g);
            }

        }else{
            for(UIElement e : elements){
                e.render(g);
            }
        }
    }
    public void update(){
        if(isList){
            ArrayList<UIElement> copy = new ArrayList<UIElement>();
            copy.addAll(toShow);
            
            for(UIElement e : copy){
                if(e instanceof FunctionalElement){
                    FunctionalElement ee = (FunctionalElement)e;
                    ee.update();
                }else if(e instanceof Container){
                    ((Container)e).update();
                }
            }
        }else{
            for(UIElement e : elements){
                
                if(e instanceof FunctionalElement){
                    FunctionalElement ee = (FunctionalElement)e;
                    ee.update();
                }else if(e instanceof Container){
                    ((Container)e).update();
                }
                    
            }
            
        }
    }
    
}
