package world;

import java.awt.Color;

public class LightSource {
    public int x,y,radius;
    public Color color;
    public double transparency;
    public LightSource(int x, int y, Color c,double transparency, int radius){
        this.x =x;
        this.y = y;
        this.color = c;
        this.transparency = transparency;
        this.radius = radius;
    }
    
}