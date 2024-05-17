package ui;

import java.awt.Rectangle;
import java.awt.Graphics;
public abstract class UIElement {
    public int x, y;
    public Rectangle bounds;

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
    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
        bounds.x = x;
        bounds.y = y;
    }
    public abstract void render(Graphics g);
}
