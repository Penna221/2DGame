package states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import entities.Entity;
import entities.ai.PlayerAI;
import entities.player.Inventory;
import entities.player.Inventory.Slot;
import entities.player.SlotRectangle;
import gfx.Transition;
import main.Game;
import world.World;

public class InventoryState extends State{
    public Inventory inv;
    private int spacing = 30;
    private int slotSize = 50;
    public Slot[] inventorySlots;
    public Slot[] hotbarSlots;
    public Slot[] specialSlots;
    private static boolean slotSelected = false;
    private static int selectedSlotID = -1;
    private static SlotRectangle[] slotRectangles;
        @Override
        public void update() {
            if(running){
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
    
            }else{
                State.transition.update();
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
        if(!slotSelected||selectedSlotID==-1)
            return;

        int mouseX = Game.mm.mouseX;
        int mouseY = Game.mm.mouseY;
        for(SlotRectangle s : slotRectangles){
            System.out.println(selectedSlotID + "  " + s.id);
            if(selectedSlotID==s.id){
                continue;
            }
            Rectangle r = s.rect;
            System.out.println("selectedSlot: " + selectedSlotID);
            Slot selected = slotRectangles[selectedSlotID].slot;
            if(r.contains(mouseX,mouseY)){
                //IF Slot has no item, put it there.
                //IF SLOT HAS SAME item, combine.
                if(s.slot.item==null){
                    int remainder = putItemsInSlot(s.slot,selected.item,selected.amount);
                    
                    slotRectangles[selectedSlotID].slot.clear();
                    slotSelected = false;
                    selectedSlotID = -1;
                    break;
                }else{
                    int remainder = putItemsInSlot(s.slot,selected.item,selected.amount);
                    //IF same amount, probably different items.
                    if(remainder == selected.amount){
                        int tempID = s.slot.item.info.id;
                        int tempSubID = s.slot.item.subID;
                        int amount = s.slot.amount;
                        s.slot.clear();
                        for(int i = 0; i < selected.amount; i++){
                            s.slot.add(selected.item);
                        }
                        selected.clear();
                        Entity e = World.entityManager.spawnEntity(tempID, tempSubID,0,0);
                        for(int i = 0; i < amount; i++){
                            selected.add(e);
                        }
                        World.entityManager.removeEntity(e);
                        break;

                    }else{
                        //Copy selected
                        int copyID = selected.item.info.id;
                        int copyID2 = selected.item.subID;
                        Entity e = World.entityManager.spawnEntity(copyID, copyID2,0,0);
                        selected.clear();
                        putItemsInSlot(slotRectangles[selectedSlotID].slot, e,remainder);
                        slotSelected = false;
                        selectedSlotID = -1;
                        break;
                    }
                }
            }
        }
        slotSelected = false;
        selectedSlotID = -1;
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
    @Override
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
        if(transition !=null){
            transition.render(g);
        }
    }

    @Override
    public void init() {
        System.out.println("Inventory init");
        inv = PlayerAI.inv;
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
            System.out.println(i);
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
            System.out.println(i);
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
        System.out.println("Per row "+perRow);
        int remainder = inventorySlots.length%rows;
        
        int y = Game.w.getHeight()/3 - (int)(2*slotSize*1.3);
        int startX = Game.w.getWidth()/2- (perRow/2)*(spacing+slotSize);
        for(int row = 0; row < rows; row++){
            System.out.println("next Row: " + row);
            for(int i = 0; i < perRow; i++){
                Rectangle r = new Rectangle(startX + i*(slotSize+spacing),y + row*(slotSize+spacing),slotSize,slotSize);
                int lookUp = (row*perRow)+i;
                System.out.println("*"+lookUp);
                Slot s = inventorySlots[lookUp];
                SlotRectangle sr = new SlotRectangle(currentIndex,r, s);
                slotRectangles[currentIndex] = sr;
                currentIndex++;
            }
        }
        System.out.println("remainder " + remainder);
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

    @Override
    public void updateOnceBetweenTransitions() {
        //Save inventory?
    }
    
}
