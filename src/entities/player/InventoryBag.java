package entities.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import entities.Entity;
import entities.ai.PlayerAI;
import entities.player.Inventory.Slot;
import gfx.Transition;
import main.Game;
import world.World;

public class InventoryBag{
    private int spacing = 30;
    private int slotSize = 50;
    public Slot[] inventorySlots;
    public Slot[] hotbarSlots;
    public Slot[] specialSlots;
    public static boolean slotSelected = false;
    private static int selectedSlotID = -1;
    private static SlotRectangle[] slotRectangles;
    
    

    public void update() {
        
        int mouseX = Game.mm.mouseX;
        int mouseY = Game.mm.mouseY;
        for(SlotRectangle s : slotRectangles){
            Rectangle r = s.rect;
            if(r.contains(mouseX,mouseY)){
                s.slot.highlight = true;
            }else{
                s.slot.highlight = false;
            }
        }
    }
    public static void pickSlot(){
        if(slotSelected)
            return;

        int mouseX = Game.mm.mouseX;
        int mouseY = Game.mm.mouseY;
        for(SlotRectangle s : slotRectangles){
            Rectangle r = s.rect;
            if(r.contains(mouseX,mouseY)){
                if(s.slot.item!=null){
                    selectedSlotID = s.id;
                    slotSelected = true;   
                }
            }
        }
    }
    public static void releaseSlot(){
        //DRAGGING SLOT AND RELEASING IT ON SOME SLOT.

        if(!slotSelected||selectedSlotID==-1)
            return;

        int mouseX = Game.mm.mouseX;
        int mouseY = Game.mm.mouseY;
        for(SlotRectangle s : slotRectangles){
            if(selectedSlotID==s.id){
                continue;
            }
            Rectangle r = s.rect;
            Slot selected = slotRectangles[selectedSlotID].slot;
            if(r.contains(mouseX,mouseY)){
                //IF Slot has no item, put it there.
                //IF SLOT HAS SAME item, combine.
                if(s.slot.item==null){
                    //This slot is FREE. Put selected items in there.
                    int remainder = putItemsInSlot(s.slot,selected.item,selected.amount);
                    
                    slotRectangles[selectedSlotID].slot.clear();
                    slotSelected = false;
                    selectedSlotID = -1;
                    break;
                }else{
                    //Slot has something in it.
                    //Check if item has same type.
                    boolean sameType = Inventory.checkIfSameType(selected.item, s.slot.item);
                    if(sameType){
                        boolean sameSubId = Inventory.checkIfSameSubID(selected.item, s.slot.item);
                        //SAMETYPE & same SubID so add items together. If remander == selected.amount or not zero, put rest back to selected slot
                        if(sameSubId){
                            int remainder = putItemsInSlot(s.slot,selected.item,selected.amount);
                            if(remainder!=0){
                                selected.amount = (byte)remainder;
                            }
                        }else{
                            //Not Same item, SWAP has to happen.
                            swapSlots(selected, s.slot);
                        }
                    }
                    else{
                        //not same item. SWAP has to happen.
                        swapSlots(selected, s.slot);
                    }
                }
            }
        }
        slotSelected = false;
        selectedSlotID = -1;
        PlayerAI.inv.updateInventory();
    }
    private static void swapSlots(Slot a, Slot b){
        int tempID = a.item.info.id;
        int tempSubID = a.item.subID;
        int amount = a.amount;
        a.clear();
        
        int tempID2 = b.item.info.id;
        int tempSubID2 = b.item.subID;
        int amount2 = b.amount;
        b.clear();

        Entity e1 = World.entityManager.generateEntityWithID(tempID, tempSubID, 0,0);
        Entity e2 = World.entityManager.generateEntityWithID(tempID2, tempSubID2, 0,0);

        System.out.println("E1: " + e1.name + " " + e1.info.id + " " + e1.subID);
        System.out.println("E2: " + e2.name + " " + e2.info.id + " " + e2.subID);
        for(int i = 0; i < amount; i++){
            b.add(e1);
        }
        for(int i = 0; i < amount2; i++){
            a.add(e2);
        }
    }
    private static int putItemsInSlot(Slot s, Entity e, int amount){
        int originalAmount = amount;
        for(int i = 0; i < amount;i++){
            //So long as you can put items in slot, keep doing it. return the number of remaining items.
            if(s.add(e)){
                originalAmount--;
            }else{
                break;
            }
        }
        return originalAmount;
    }
    public void render(Graphics g) {
        g.setColor(Color.white);
        g.drawString("Inventory", 0, 25);
        int i = 0;
        
        for(SlotRectangle s : slotRectangles){
            Slot sl = s.slot;
            sl.setPosition(s.rect.x, s.rect.y);
            sl.render(g);
        }
        int mouseX = Game.mm.mouseX;
        int mouseY = Game.mm.mouseY;
        if(slotSelected){
            if(slotRectangles[selectedSlotID].slot.item!=null){
                g.drawImage(slotRectangles[selectedSlotID].slot.texture, mouseX - (int)(slotSize*0.5), mouseY - (int)(slotSize*0.5),(int)(slotSize*0.8),(int)(slotSize*0.8), null);
            }
        }
    }

    public void init() {
        System.out.println("Inventory init");
        Inventory inv = PlayerAI.inv;
        inventorySlots = inv.inventorySlots;
        hotbarSlots = inv.hotbarSlots;
        specialSlots = inv.specialSlots;
        int hotbarlength = hotbarSlots.length;
        int invlength = inventorySlots.length;
        int specialLength = specialSlots.length;
        slotRectangles = new SlotRectangle[invlength + hotbarlength + specialLength];
        
        //Create hotbar. Put them in their position.
        int latestX = 0;
        for(int i = 0; i < hotbarlength; i++){
            int y = Game.w.getHeight() - (int)(3*slotSize*1.3);
            int startX = Game.w.getWidth()/2- (hotbarSlots.length/2)*(spacing+slotSize);
            int x = startX + i*(spacing+slotSize);
            latestX = x;
            Rectangle r = new Rectangle(x,y,slotSize,slotSize);
            Slot s = hotbarSlots[i];
            SlotRectangle sr = new SlotRectangle(i,r, s);
            slotRectangles[i] = sr;
        }
        int currentIndex = hotbarlength;
        for(int i = 0; i < specialLength; i++){
            int yy = Game.w.getHeight() - (int)(3*slotSize*1.3);;
            int xx = latestX +spacing+(slotSize*2)+ (i*(spacing+slotSize))+spacing;
            Rectangle r = new Rectangle(xx,yy,slotSize,slotSize);
            Slot s = specialSlots[i];
            SlotRectangle sr = new SlotRectangle(currentIndex,r, s);
            slotRectangles[currentIndex] = sr;
            currentIndex++;
        }
        //Create Inventory. Put them in their position.
        int rows = 4;
        int perRow = inventorySlots.length/rows;
        int remainder = inventorySlots.length%rows;
        
        int y = Game.w.getHeight()/3 - (int)(2*slotSize*1.3);
        int startX = Game.w.getWidth()/2- (perRow/2)*(spacing+slotSize);
        for(int row = 0; row < rows; row++){
            for(int i = 0; i < perRow; i++){
                Rectangle r = new Rectangle(startX + i*(slotSize+spacing),y + row*(slotSize+spacing),slotSize,slotSize);
                int lookUp = (row*perRow)+i;
                Slot s = inventorySlots[lookUp];
                SlotRectangle sr = new SlotRectangle(currentIndex,r, s);
                slotRectangles[currentIndex] = sr;
                currentIndex++;
            }
        }
        if(remainder!=0){
            for(int i = 0; i < remainder; i++){
                Rectangle r = new Rectangle(startX + i*(slotSize+spacing),y + (rows+1)*(slotSize+spacing),slotSize,slotSize);
                Slot s = inventorySlots[(rows)*perRow+i];
                SlotRectangle sr = new SlotRectangle(currentIndex,r, s);
                slotRectangles[currentIndex] = sr;
                currentIndex++;
            }
        }
        
        Transition.canContinue = true;
    }
    
}
