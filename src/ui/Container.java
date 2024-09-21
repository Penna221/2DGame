package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Container extends UIElement{
    public ArrayList<UIElement> elements;
    public ArrayList<UIElement> change;
    public boolean changing = false;

    public UIElement header;
    public boolean fillBg = false;
    public boolean drawBounds = false;
    public int start=0;
    
    public boolean isList = false;
    public Color overrideColor;
    public Container(int x, int y, int width, int height) {
        super(x, y, width, height);
        elements = new ArrayList<UIElement>();
        change = new ArrayList<UIElement>();
        data = UIFactory.containerData;
    }
    public void sendClick(){
        for(UIElement e : elements){
            if(changing){
                break;
            }
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
            int newX = bounds.width/2-w/2;
            e.setPosition(newX, e.y);
            
        }
        if(header !=null){
            header.bounds.x = bounds.x + bounds.width/2 - header.bounds.width/2;
        }
    }
    public void spaceOutVertically(int buffer){
        int startY = y;
        if(header!=null){
            startY = header.bounds.y+header.bounds.height+buffer*2;
        }
        for(UIElement e : elements){
            int h = e.bounds.height;
            e.setPosition(e.x, startY);
            startY += buffer + h;
        }
        
    }
    public void centerVertically(){
        for(UIElement e : elements){
            e.setPosition(e.x, bounds.height/2 - e.bounds.height/2);
        }
    }
    public void wrap(){
        int maxX = 0;
        int maxY = 0;

        for(UIElement e : elements){
            int ex = e.x + e.bounds.width;
            int ey = e.y + e.bounds.height;
            if(ex>maxX){
                maxX = ex;
            }
            if(ey > maxY){
                maxY = ey;
            }
        }
        
        bounds.setSize(maxX, maxY);
    }
    public void centerAndSpaceHorizontally(int buffer){
        int center = bounds.width/2;
        int els = elements.size();
        int availableSpace = bounds.width;

        int elementsCombinedWidth = 0;
        for(UIElement e : elements){
            int w = e.bounds.width;
            elementsCombinedWidth+= w;
        }
        int neededBufferSpace = (elements.size()-1)*buffer;
        int totalWidth = elementsCombinedWidth + neededBufferSpace;
        int startX = center - totalWidth/2;

        for(UIElement e : elements){
            e.setPosition(startX, e.y);
            startX += e.bounds.getWidth()+buffer;
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
            e.setPosition(currentX, e.y);
            currentX += e.bounds.getWidth()+buffer;
        }
        
    }
    public void updateList(){
        int lastY = 0;
        for(int i = 0; i < elements.size(); i++){
            UIElement e = elements.get(i);
            e.setPosition(0, lastY);
            lastY += e.bounds.getHeight()+20;
        }
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
    public void addLate(UIElement e){
        e.setOffset(x, y);
        change.add(e);
    }
    @Override
    public void render(Graphics g) {
        //Draw container
        if(fillBg){
            g.setColor(data.bgColor);
            if(overrideColor!=null){
                g.setColor(overrideColor);
            }
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
        if(drawBounds){
            g.setColor(Color.WHITE);
            g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

        }
        if(header!=null)header.render(g);
        //Draw elements
        if(!changing){
            for(UIElement e : elements){
                if(changing){
                    break;
                }
                e.render(g);
            }
        }
    }
    public void swap(){
        elements.clear();
        elements.addAll(change);
        updateList();
        updateBounds();
        changing = false;
        change.clear();
    }
    public void update(){
    
        for(UIElement e : elements){
            if(changing){
                break;
            }
            // System.out.println("update from elements forloop");
            if(e instanceof FunctionalElement){
                FunctionalElement ee = (FunctionalElement)e;
                ee.update();
            }else if(e instanceof Container){
                ((Container)e).update();
            }  
        }
    }
    
}
