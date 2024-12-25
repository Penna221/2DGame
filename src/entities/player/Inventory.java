package entities.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import entities.Entity;
import gfx.AssetStorage;
import gfx.Factory;
import main.Game;
import ui.UIFactory;

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
    public Slot getSelectedSlot(){
        return hotbarSlots[selectedIndex];
    }
    public void updateInventory(){
        for(Slot s : inventorySlots){
            s.updateSlot();
        }
        for(Slot s : hotbarSlots){
            s.updateSlot();
        }
    }
    public void load(){
        //Initialize
        for(int i = 0; i < inventorySlots.length; i++){
            inventorySlots[i] = new Slot(slotSize,slotSize);
        }
        for(int i = 0; i < hotbarSlots.length; i++){
            hotbarSlots[i] = new Slot(slotSize,slotSize);
        }
        //Load from saved data.
    }
    public void select(int i){
        selectedIndex = i;
    }
    public void scroll(int i){
        selectedIndex += i;
        if(selectedIndex > hotbarSlots.length-1){
            selectedIndex = 0;
        }
        if(selectedIndex<0){
            selectedIndex = hotbarSlots.length-1;
        }
    }
    public int getSlotWithItemFromHotbarSlots(Entity e){
        for(int i = 0; i < hotbarSlots.length; i++){
            Slot s = hotbarSlots[i];
            if(s.item==null){
                continue;
            }
            boolean sameId = checkIfSameSubID(e, s.item);
            if(sameId){
                return i;
            }
        }
        return -1;
    }
    public int getSlotWithItemFromInventorySlots(Entity e){
        for(int i = 0; i < inventorySlots.length; i++){
            Slot s = inventorySlots[i];
            if(s.item==null){
                continue;
            }
            if(s.item.subID!=-1){
                boolean sameId = checkIfSameSubID(e, s.item);
                if(sameId){
                    return i;
                }
            }else{
                if(e.subID!=-1){
                    continue;
                }
                if(s.item.info.id == e.info.id){
                    return i;
                }
            }

        }
        return -1;
    }
    public static boolean checkIfSameSubID(Entity e1, Entity e2){
        if(e1.swordInfo!=null){
            if(e2.swordInfo!=null){
                if(e1.swordInfo.id == e2.swordInfo.id){
                    return true;
                }
            }else{
                return false;
            }
        }else if(e1.projectileInfo!=null){
            if(e2.projectileInfo!=null){
                if(e1.projectileInfo.id == e2.projectileInfo.id){
                    return true;
                }
            }else{
                return false;
            }
        }else if(e1.staffInfo!=null){
            if(e2.staffInfo!=null){
                if(e1.staffInfo.id == e2.staffInfo.id){
                    return true;
                }
            }else{
                return false;
            }
        }else if(e1.potionInfo!=null){
            if(e2.potionInfo!=null){
                if(e1.potionInfo.id == e2.potionInfo.id){
                    return true;
                }
            }else{
                return false;
            }
        }else{
            //DOES NOT HAVE SUB ID
            if(e1.info.id==e2.info.id){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }
    public boolean addItem(Entity c){
        
        boolean added = false;

        
        int slotA = getSlotWithItemFromHotbarSlots(c);
        if(slotA !=-1){
            
            added = hotbarSlots[slotA].add(c);
        }else{
            int slotB = getSlotWithItemFromInventorySlots(c);
            
            if(slotB!=-1){
                added = inventorySlots[slotB].add(c);
                
            }
        }
        if(!added){
            for(int i = 0; i < hotbarSlots.length; i++){
                Slot s = hotbarSlots[i];
                if(s.add(c)){
                    added = true;
                    System.out.println("Added item with id("+c.info.id+") to slot " + i);
                    break;
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
        }
        if(!added){
            System.out.println("no available slots.");
        }
        return added;
    }
    public boolean checkIfEnoughItemsWithID(int id, int id2, int amount){
        if(id2==-1){
            
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
        }else{
            int totalAmount = 0;
            for(Slot s : inventorySlots){
                if(s.item==null){
                    continue;
                }
                if(s.item.subID!=-1){
                    continue;
                }
                if(s.item.info.id!=id){
                    continue;
                }
                if(s.item.subID==id2){
                    totalAmount += s.amount;
                }
                if(totalAmount>=amount){
                    return true;
                }
            }
            for(Slot s : hotbarSlots){
                if(s.item==null){
                    continue;
                }
                if(s.item.subID!=-1){
                    continue;
                }
                if(s.item.info.id!=id){
                    continue;
                }
                if(s.item.subID==id2){
                    totalAmount += s.amount;
                }
                if(totalAmount>=amount){
                    return true;
                }
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
            s.render(g, startX + i*(spacing+slotSize), y);
        }

        g.drawImage(AssetStorage.images.get("frame"), startX + selectedIndex*(spacing+slotSize),y,slotSize+1,slotSize+1,null);
        return overlay;
    }
    //Inner class
    public class Slot{
        private byte maxStack = 99;
        public Entity item;
        public byte amount;
        public int width,height;
        public Slot(int width, int height){this.width=width;this.height=height;}
        public boolean highlight = false;
        public BufferedImage texture;
        public void updateSlot(){
            if(amount<=0){
                clear();
            }else{
                generateImage();
            }
        }
        public boolean add(Entity i){
            //IF item == null -> Slot does not have item.
            if(item ==null){
                this.item = i;
                amount++;
                generateImage();
                return true;
            }else{
                if(amount==maxStack){
                    generateImage();
                    return false;
                }
                //Slot has item. 
                boolean b = false;
                if(item.swordInfo!=null || item.projectileInfo!=null || item.staffInfo!=null || item.potionInfo!=null){
                    //Item has subID
                    b = checkIfSameSubID(item, i);
                    if(b){
                        amount++;
                        generateImage();
                        return true;
                    }else{
                        generateImage();
                        return false;
                    }
                }
                if(i.swordInfo!=null || i.projectileInfo!=null|| i.staffInfo!=null || i.potionInfo!=null){
                    generateImage();
                    return false;
                }
                //Not subID
                if(item.info.id == i.info.id){
                    amount++;
                    generateImage();
                    return true;
                }
            }
            generateImage();
            return false;
        }
        public void generateImage(){
            if(item!=null){
                texture = UIFactory.generateIconWithAmount(item.texture,amount,width,height,false,new Color(0,0,0,0));
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
        public void render(Graphics g, int x, int y){
            g.setColor(new Color(0,0,0,180));
            g.fillRect(x, y, width, height);
            g.setColor(Color.red);
            g.drawRect(x, y,width,height);
            if(item!=null){
                g.drawImage(texture, x, y, null);
                // g.drawImage(item.texture, x +3, y+3,width-6,height-6, null);
                // g.setColor(Color.red);
                // g.drawString(""+amount, x+width/2, y);
            }
            if(highlight){
                g.setColor(Color.white);
                g.drawRect(x-1, y-1, width+2, height+2);
            }
        }
    }
}
