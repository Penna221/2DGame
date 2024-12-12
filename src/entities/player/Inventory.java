package entities.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import entities.Entity;
import gfx.AssetStorage;
import gfx.Factory;
import main.Game;

public class Inventory {
    
    public Slot[] inventorySlots;
    public Slot[] hotbarSlots;
    private int y = Game.w.getHeight() - 40;
    private int spacing = 30;
    private int slotSize = 50;
    private BufferedImage overlay;
    private Graphics g;
    private int selectedIndex = 0;
    public Inventory(){
        overlay = Factory.generateNewOverlayImage();
        g = overlay.createGraphics();
        inventorySlots = new Slot[20];
        hotbarSlots = new Slot[5];
        load();
    }
    public void addSlot(byte amount){
        int newSize = inventorySlots.length+amount;
        inventorySlots = Arrays.copyOf(inventorySlots, newSize);
    }
    public void load(){
        //Initialize
        for(int i = 0; i < inventorySlots.length; i++){
            inventorySlots[i] = new Slot();
        }
        for(int i = 0; i < hotbarSlots.length; i++){
            hotbarSlots[i] = new Slot();
        }
        //Load from saved data.
    }
    public void select(int i){
        selectedIndex = i;
    }
    public int getSlotWithItemFromHotbarSlots(int id){
        for(int i = 0; i < hotbarSlots.length; i++){
            Slot s = hotbarSlots[i];
            if(s.item==null){
                continue;
            }
            if(s.item.info.id== id){
                return i;
            }
        }
        return -1;
    }
    public int getSlotWithItemFromInventorySlots(int id){
        for(int i = 0; i < inventorySlots.length; i++){
            Slot s = inventorySlots[i];
            if(s.item==null){
                continue;
            }
            if(s.item.info.id== id){
                return i;
            }
        }
        return -1;
    }
    public boolean addItem(Entity c){
        
        boolean added = false;

        int slotA = getSlotWithItemFromInventorySlots(c.info.id);
        if(slotA !=-1){
            inventorySlots[slotA].add(c);
            added = true;
        }else{
            int slotB = getSlotWithItemFromHotbarSlots(c.info.id);
            if(slotB!=-1){
                hotbarSlots[slotB].add(c);
                added = true;
            }
        }
        if(!added){
            for(int i = 0; i < inventorySlots.length; i++){
                Slot s = inventorySlots[i];
                if(s.add(c)){
                    added = true;
                    System.out.println("Added item with id("+c.info.id+") to slot " + i);
                    break;
                }
            }
        }
        if(!added){
            System.out.println("no available slots.");
        }
        return added;
    }
    public boolean checkIfEnoughItemsWithID(int id, int amount){
        int totalAmount = 0;
        for(Slot s : inventorySlots){
            if(s.item==null){
                continue;
            }
            if(s.item.info.id!=id){
                continue;
            }
            totalAmount += s.amount;
            if(totalAmount>=amount){
                return true;
            }
        }
        for(Slot s : hotbarSlots){
            if(s.item==null){
                continue;
            }
            if(s.item.info.id!=id){
                continue;
            }
            totalAmount += s.amount;
            if(totalAmount>=amount){
                return true;
            }
        }
        return false;
    }
    public void pay(int id, int amount){
        for(Slot s : inventorySlots){
            System.out.println("nextSlot");
            if(s.item==null){
                continue;
            }
            if(s.item.info.id!=id){
                continue;
            }
            if(s.amount>=amount){
                s.reduce((byte)amount);
                amount = 0;
                return;
            }else{
                amount -= s.amount;
                s.clear();
                continue;
            }
        }
        for(Slot s : hotbarSlots){
            if(s.item==null){
                continue;
            }
            if(s.item.info.id!=id){
                continue;
            }
            if(s.amount>=amount){
                s.reduce((byte)amount);
                amount = 0;
                return;
            }else{
                amount -= s.amount;
                s.clear();
                continue;
            }
            
        }
    }
    public BufferedImage drawHotBar(){
        int toShow = hotbarSlots.length;
        overlay = Factory.generateNewOverlayImage();
        g = overlay.createGraphics();
        int startX = Game.w.getWidth()/2- (toShow/2)*(spacing+slotSize);
        y = Game.w.getHeight() - 100;
        for(int i = 0; i < toShow; i++){
            Slot s = hotbarSlots[i];
            s.render(g, startX + i*(spacing+slotSize), y,slotSize,slotSize);
        }

        g.drawImage(AssetStorage.images.get("frame"), startX + selectedIndex*(spacing+slotSize),y,slotSize+1,slotSize+1,null);
        return overlay;
    }
    //Inner class
    public class Slot{
        private byte maxStack = 99;
        public Entity item;
        public byte amount;
        public Slot(){}
        public boolean highlight = false;
        public boolean add(Entity i){
            if(item ==null){
                this.item = i;
                amount++;
                return true;
            }else{
                if(i.info.id==item.info.id){
                    if(amount==maxStack){
                        return false;
                    }
                    amount++;
                    return true;
                }else{
                    return false;
                }
            }
        }
        public void reduce(byte amount){
            this.amount -= amount;
            if(amount <= 0){
                clear();
            }
        }
        public void clear(){
            System.out.println("clearing");
            item = null;
            amount = 0;
        }
        public void render(Graphics g, int x, int y, int width, int height){
            g.setColor(new Color(0,0,0,180));
            g.fillRect(x, y, width, height);
            g.setColor(Color.red);
            g.drawRect(x, y,width,height);
            if(item!=null){
                g.drawImage(item.texture, x , y,width,height, null);
                g.setColor(Color.red);
                g.drawString(""+amount, x+width/2, y);
            }
            if(highlight){
                g.setColor(Color.white);
                g.drawRect(x-1, y-1, width+2, height+2);
            }
        }
    }
}
