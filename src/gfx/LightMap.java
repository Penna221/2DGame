package gfx;

import java.util.ArrayList;
import java.awt.Polygon;
import java.awt.Rectangle;

public class LightMap {
    public Polygon p;
    public ArrayList<Rectangle> boxes;
    public LightMap(Polygon p, ArrayList<Rectangle> boxes){
        this.p = p;
        this.boxes =boxes;
    }
}
