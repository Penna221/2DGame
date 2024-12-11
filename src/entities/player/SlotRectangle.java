package entities.player;

import java.awt.Rectangle;

import entities.player.Inventory.Slot;

public class SlotRectangle {
    public Rectangle rect;
    public Slot slot;
    public int id;
    public SlotRectangle(int id, Rectangle r, Slot s){
        this.id = id;
        this.rect = r;
        this.slot = s;
    }
}
