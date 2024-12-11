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
        if(!slotSelected)
            return;

        int mouseX = Game.mm.mouseX;
        int mouseY = Game.mm.mouseY;
        for(SlotRectangle s : slotRectangles){
            System.out.println(selectedSlotID + "  " + s.id);
            if(selectedSlotID==s.id){
                continue;
            }
            Rectangle r = s.rect;
            Slot selected = slotRectangles[selectedSlotID].slot;
            int originalAmount = selected.amount;
            if(r.contains(mouseX,mouseY)){
                if(s.slot.item==null||s.slot.item.info.id == selected.item.info.id){
                    for(int i = 0; i < selected.amount;i++){
                        if(s.slot.add(selected.item)){
                            originalAmount--;
                        }else{
                            slotRectangles[selectedSlotID].slot.clear();
                            for(int j = 0; j < originalAmount;j++){
                                slotRectangles[selectedSlotID].slot.add(s.slot.item);
                            }
                            slotSelected = false;
                            selectedSlotID = -1;
                            return;
                        }
                    }
                    slotRectangles[selectedSlotID].slot.clear();
                }else{
                    //Swap
                    int tempID = s.slot.item.info.id;
                    int amount = s.slot.amount;
                    s.slot.clear();
                    for(int i = 0; i < selected.amount; i++){
                        s.slot.add(selected.item);
                    }
                    selected.clear();
                    Entity e = World.entityManager.generateWithID(tempID, 0,0);
                    
                    for(int i = 0; i < amount; i++){
                        selected.add(e);
                    }
                    World.entityManager.removeEntity(e);
                }
            }
        }
        slotSelected = false;
        selectedSlotID = -1;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.white);
        g.drawString("Inventory", 0, 25);
        int i = 0;
        
        for(SlotRectangle s : slotRectangles){
            Slot sl = s.slot;
            sl.render(g,s.rect.x, s.rect.y, s.rect.width, s.rect.height);
        }
        int mouseX = Game.mm.mouseX;
        int mouseY = Game.mm.mouseY;
        if(slotSelected){
            if(slotRectangles[selectedSlotID].slot.item!=null){
                Entity item = slotRectangles[selectedSlotID].slot.item;
                g.drawImage(item.texture, mouseX - item.texture.getWidth(), mouseY - item.texture.getHeight(), null);
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
        int hotbarlength = hotbarSlots.length;
        int invlength = inventorySlots.length;
        slotRectangles = new SlotRectangle[invlength + hotbarlength];
        //Create hotbar. Put them in their position.
        for(int i = 0; i < hotbarlength; i++){
            System.out.println(i);
            int y = Game.w.getHeight() - (int)(3*slotSize*1.3);
            int startX = Game.w.getWidth()/2- (hotbarSlots.length/2)*(spacing+slotSize);
            int x = startX + i*(spacing+slotSize);
            Rectangle r = new Rectangle(x,y,slotSize,slotSize);
            Slot s = hotbarSlots[i];
            SlotRectangle sr = new SlotRectangle(i,r, s);
            slotRectangles[i] = sr;
        }
        //Create Inventory. Put them in their position.
        int rows = 4;
        int perRow = inventorySlots.length/rows;
        System.out.println("Per row "+perRow);
        int remainder = inventorySlots.length%rows;
        int currentIndex = hotbarlength;
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
