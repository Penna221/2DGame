package ui;

import java.awt.Rectangle;
import java.awt.Graphics;
public abstract class UIElement {
    public int x, y;
    public Rectangle bounds;
    public int offsetX, offsetY;
    public UIData data;
    public UIElement(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        bounds = new Rectangle(x,y,width,height);
    }
    public UIElement(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void setPosition(int newx, int newy){
    
        this.x = newx;
        this.y = newy;
        bounds.x = x + offsetX;
        bounds.y = y + offsetY;
            
        if(this instanceof Container){
            Container c = (Container)this;
            for(UIElement e : c.elements){
                int setX = bounds.x;
                int setY = bounds.y;
                System.out.println("Setting child element pos to "+ setX + "  " + setY);
                e.setOffset(setX,setY);
            }
            
        }
        updateBounds();
    }
    public void setOffset(int xx, int yy){
        this.offsetX = xx;
        this.offsetY = yy;
    }
    public void updateBounds(){
        bounds.x = x + offsetX;
        bounds.y = y + offsetY;
        if(this instanceof Container){
            Container c = (Container)this;
            for(UIElement e : c.elements){
                e.updateBounds();
            }
            
        }
    }
    public abstract void render(Graphics g);
}
