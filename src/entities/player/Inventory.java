package entities.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entities.Entity;
import gfx.Factory;
import main.Game;
import world.World;

public class Inventory {
    
    public Slot[] slots;
    private int y = Game.w.getHeight() - 40;
    private int spacing = 30;
    private int slotSize = 50;
    private BufferedImage overlay;
    private Graphics g;
    public Inventory(){
        overlay = Factory.generateNewOverlayImage();
        g = overlay.createGraphics();
        slots = new Slot[5];
        load();
        draw();
    }
    public void load(){
        //Initialize
        for(int i = 0; i < slots.length; i++){
            slots[i] = new Slot();
        }
        //Load from saved data.
    }
    public boolean addItem(Entity c){
        
        boolean added = false;
        for(int i = 0; i < slots.length; i++){
            Slot s = slots[i];
            if(s.add(c)){
                added = true;
                System.out.println("Added item with id("+c.info.id+") to slot " + i);
                break;
            }
        }
        if(!added){
            System.out.println("no available slots.");
        }
        draw();
        return added;
    }

    public void draw(){
        overlay = Factory.generateNewOverlayImage();
        g = overlay.createGraphics();
        int startX = Game.w.getWidth()/2- (slots.length/2)*(spacing+slotSize);
        y = Game.w.getHeight() - 100;
        for(int i = 0; i < slots.length; i++){
            Slot s = slots[i];
            s.render(g, startX + i*(spacing+slotSize), y,slotSize,slotSize);
        }
    }
    public void render(){
        
        
        World.overlay.add(overlay);
    }
    //Inner class
    public class Slot{
        public Entity item;
        public int amount;
        public Slot(){}
        public boolean add(Entity i){
            if(item ==null){
                this.item = i;
                amount++;
                return true;
            }else{
                if(i.info.id==item.info.id){
                    amount++;
                    return true;
                }else{
                    System.out.println(i.info.id + " is not " + item.info.id);
                    return false;
                }
            }
        }
        public void reduce(int amount){
            this.amount -= amount;
            if(amount <= 0){
                clear();
            }
        }
        public void clear(){
            item = null;
            amount = 0;
        }
        public void render(Graphics g, int x, int y, int width, int height){
            g.setColor(new Color(0,0,0,180));
            g.fillRect(x, y, width, height);
            g.setColor(Color.red);
            g.drawRect(x, y,width,height);
            if(item!=null){
                g.drawImage(item.texture, x + width/2 -item.texture.getWidth()/2, y + height/2  - item.texture.getHeight()/2, null);
                g.setColor(Color.red);
                g.drawString(""+amount, x+width/2, y);
            }
        }
    }
}
