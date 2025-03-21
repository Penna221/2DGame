package entities.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import cards.Card;
import entities.Entity;
import entities.bows.Bows;
import entities.swords.Swords;
import gfx.AssetStorage;
import gfx.Factory;
import main.Game;
import ui.UIFactory;
import world.World;

public class Inventory {
    
    public Slot[] inventorySlots;
    public Slot[] hotbarSlots;
    private int y = Game.w.getHeight() - 40;
    private int spacing = 30;
    private int slotSize = 40;
    private BufferedImage overlay;
    private Graphics g;
    private int selectedIndex = 0;
    public Slot[] specialSlots;
    public Slot arrowSlot, spellSlot;
    public Inventory(){
        overlay = Factory.generateNewOverlayImage();
        g = overlay.createGraphics();
        inventorySlots = new Slot[10];
        hotbarSlots = new Slot[3];
        specialSlots = new Slot[2];
        init();
    }
    public void clearInventory(){
        init();
    }

    public void applyCards(ArrayList<Card> cards){
        clearInventory();
        for(Card c : cards){
            if(c.type.equals("Weapon")){
                handleWeaponCard(c);
            }else{
                handleBuffCard(c);
            }
        }
        updateInventory();
    }
    private void handleWeaponCard(Card c){
        int id = c.id;
        switch (id) {
            case 0:
                //Bow
                addItem(World.entityManager.generateEntityWithID(c.itemID1, c.itemID2, 0,0));
                Entity arrow = World.entityManager.generateEntityWithID(35,1, 0,0);
                
                setArrows(arrow, (byte)50);
                break;
            case 1:
                //Dagger
                addItem(World.entityManager.generateEntityWithID(c.itemID1, c.itemID2, 0,0));
                break;
            case 2:
            //Magic wand
                addItem(World.entityManager.generateEntityWithID(c.itemID1, c.itemID2, 0,0));
                setSpell(World.entityManager.generateEntityWithID(39, 0, 0,0));
                break;
            case 3:
                //Sword
                addItem(World.entityManager.generateEntityWithID(c.itemID1, c.itemID2, 0,0));
                break;
            case 4:
            //Stick
                addItem(World.entityManager.generateEntityWithID(c.itemID1, c.itemID2, 0,0));
                break;
            default:
                break;
        }
        
    }
    private void handleBuffCard(Card c){
        int id = c.id;
        switch (id) {
            case 0:
                addItem(World.entityManager.generateEntityWithID(c.itemID1, c.itemID2, 0,0));
                break;
            case 1:
            //Goblin master
                System.out.println("Goblin master card functionality not implemented yet.");
                break;
            case 2:
                System.out.println("Sword Double Damage card functionality not implemented yet.");
                //Sword double damage
                
                break;
            case 3:
                System.out.println("Bow Double Damage card functionality not implemented yet.");
                //Bow double damage
                
                break;
                
            default:
                break;
        }
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
        for(Slot s : specialSlots){
            s.updateSlot();
        }
    }
    public void init(){
        //Initialize
        for(int i = 0; i < inventorySlots.length; i++){
            inventorySlots[i] = new Slot(slotSize,slotSize);
        }
        for(int i = 0; i < hotbarSlots.length; i++){
            hotbarSlots[i] = new Slot(slotSize,slotSize);
        }
        //SPECIAL SLOTS
        arrowSlot = new Slot(slotSize,slotSize);
        arrowSlot.bgTexture = UIFactory.scaleToHeight(AssetStorage.images.get("arrow_placeholder"), slotSize);
        spellSlot = new Slot(slotSize,slotSize);
        spellSlot.bgTexture = UIFactory.scaleToHeight(AssetStorage.images.get("spell_placeholder"), slotSize);
        specialSlots[0] = arrowSlot;
        specialSlots[1] = spellSlot;

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
            System.out.println(e.name);
            boolean sameType = checkIfSameType(e, s.item);
            System.out.println("Sametype: " + sameType);
            if(sameType){
                boolean sameId = checkIfSameSubID(e, s.item);
                if(sameId){
                    return i;
                }
            }
        }
        return -1;
    }
    public Slot getEmptyHotbarSlot(){
        for(int i = 0; i < hotbarSlots.length; i++){
            Slot s = hotbarSlots[i];
            if(s.item==null){
                return s;
            }
        }
        return null;
    }
    public Slot getEmptyInventorySlot(){
        for(int i = 0; i < inventorySlots.length; i++){
            Slot s = inventorySlots[i];
            if(s.item==null){
                return s;
            }
        }
        return null;
    }
    public int getSlotWithItemFromInventorySlots(Entity e){
        for(int i = 0; i < inventorySlots.length; i++){
            Slot s = inventorySlots[i];
            if(s.item==null){
                continue;
            }
            boolean sameType = checkIfSameType(e, s.item);
            if(sameType){
                boolean sameId = checkIfSameSubID(e, s.item);
                if(sameId){
                    return i;
                }
            }

        }
        return -1;
    }
    public static boolean checkIfSameSubID(Entity e1, Entity e2){
        //ASSUMING e1 and e2 has same info.id.
        //FIRST USE checkIfSameType(e1, e2);
        
        if(e1.subID== e2.subID){
            return true;
        }
        return false;
    }
    public static boolean checkIfSameType(Entity e1, Entity e2){
        if(e1.swordInfo!=null && e2.swordInfo!=null){
            return true;
        }else if(e1.bowInfo!=null && e2.bowInfo!=null){
            return true;
        }else if(e1.projectileInfo!=null && e2.projectileInfo!=null){
            return true;
        }else if(e1.potionInfo!=null&&e2.potionInfo!=null){
            return true;
        }else if(e1.staffInfo!=null&&e2.staffInfo!=null){
            return true;
        }else if(e1.info.id == e2.info.id){
            return true;
        }else{
            return false;
        }
        
    }
    public void setArrows(Entity a, byte amount){
        arrowSlot.item = a;
        arrowSlot.amount = amount;
    }
    public void setSpell(Entity e){
        spellSlot.item = e;
        spellSlot.amount = 1;
    }
    public boolean tryAddToArrows(Entity c){
        boolean added = false;
        if(arrowSlot.item==null){
            added = false;
        }else{
            if(c.subID==arrowSlot.item.subID){
                added = arrowSlot.add(c);
            }
        }
        return added;
    }
    public boolean addItem(Entity c){
        boolean added = false;
        
        if(c.info.id == 35){
            added = tryAddToArrows(c);
        }
        if(!added){
            //HOTBAR
            //Check hotbar for slots with "c" item.
            int slotA = getSlotWithItemFromHotbarSlots(c);

            if(slotA == -1){
                //IF hotbar doesnot have sameitems check inventory.
                int slotB = getSlotWithItemFromInventorySlots(c);
                System.out.println("Hotbar does not have item "+c.name+". -> Checking inventory");
                if(slotB == -1){
                    System.out.println("inventory does not have item. Finding empty slot.");
                    //Inventory does not have same item.
                    //IF added returns false, slot is full.
                    //-> check emptyslots.
                    Slot s = getEmptySlot();
                    if(s == null){
                        //NO AVAILABLE SLOTS. 
                        System.out.println("No available slots.");
                        return false;
                    }else{
                        System.out.println("Found empty slot. Adding there.");
                        s.add(c);
                        return true;
                    }
                }else{
                    //Inventory has slot with item "c"
                    added = inventorySlots[slotB].add(c);
                    //IF added returns false, slot is full.
                    //-> check emptyslots.
                    if(added){
                        return true;
                    }

                    Slot s = getEmptySlot();
                    if(s == null){
                        //NO AVAILABLE SLOTS. 
                        return false;
                    }else{
                        s.add(c);
                        return true;
                    }
                }
                
            }else{
                System.out.println("Hotbar has item ");
                //HOTBAR DOES HAVE slot with item "c".
                added = hotbarSlots[slotA].add(c);
                if(added){
                    return true;
                }
                //IF added returns false, slot is full.
                //-> check emptyslots.
                Slot s = getEmptySlot();
                if(s == null){
                    //NO AVAILABLE SLOTS. 
                    return false;
                }else{
                    s.add(c);
                    return true;
                }
            }
        }
        return false;
    }
    public Slot getEmptySlot(){
        Slot s1 = getEmptyHotbarSlot();
        if(s1 == null){
            Slot s2 = getEmptyInventorySlot();
            if(s2 == null){
                return null;
            }else{
                return s2;
            }
        }else{
            return s1;
        }
        
    }
    public boolean checkIfEnoughItemsWithID(int id, int id2, int amount){
        int totalAmount = calculateAmountWithID(id,id2);
        if(totalAmount>=amount){
            return true;
        }
        return false;
    }
    public int calculateAmount(Entity e){
        int id = e.info.id;
        int id2 = e.subID;
        int totalAmount = calculateAmountWithID(id, id2);
        return totalAmount;
    }
    public int calculateAmountWithID(int id, int id2){
        int totalAmount = 0;

        if(id2==-1){    
            for(Slot s : inventorySlots){
                if(s.item==null){
                    continue;
                }
                
                if(s.item.info.id!=id){
                    continue;
                }
                totalAmount += s.amount;
            }
            for(Slot s : hotbarSlots){
                if(s.item==null){
                    continue;
                }
                if(s.item.info.id!=id){
                    continue;
                }
                totalAmount += s.amount;
            }
        }else{
            for(Slot s : inventorySlots){
                if(s.item==null){
                    continue;
                }
                if(s.item.subID==-1){
                    continue;
                }
                if(s.item.info.id!=id){
                    continue;
                }
                if(s.item.subID==id2){
                    totalAmount += s.amount;
                }
            }
            for(Slot s : hotbarSlots){
                if(s.item==null){
                    continue;
                }
                if(s.item.subID==-1){
                    continue;
                }
                if(s.item.info.id!=id){
                    continue;
                }
                if(s.item.subID==id2){
                    totalAmount += s.amount;
                }
            }
        }
        return totalAmount;
    }
    public void pay(int id, int amount){
        for(Slot s : inventorySlots){
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
        y = Game.w.getHeight() - 50;
        int latestX = startX;
        for(int i = 0; i < toShow; i++){
            Slot s = hotbarSlots[i];
            latestX = startX + i*(spacing+slotSize);
            s.setPosition(latestX, y);
            s.render(g);
        }
        arrowSlot.setPosition(latestX+100, y);
        arrowSlot.render(g);
        spellSlot.setPosition(latestX+100+slotSize, y);
        spellSlot.render(g);
        g.drawImage(AssetStorage.images.get("frame"), startX + selectedIndex*(spacing+slotSize),y,slotSize+1,slotSize+1,null);
        return overlay;
    }
    //Inner class
    public class Slot{
        private byte maxStack = 99;
        public Entity item;
        public byte amount;
        public int width,height;
        public boolean highlight = false;
        public BufferedImage texture;
        public BufferedImage bgTexture;
        private int x, y;
        public InfoPacket infoPacket;
        public Slot(int width, int height){this.width=width;this.height=height;infoPacket = new InfoPacket(null,0,0);}
        public void setPosition(int x, int y){
            this.x = x;
            this.y = y;
            if(!InventoryBag.slotSelected){
                infoPacket.update(400,70);
            }
        }
        public void setItem(Entity i, byte amount){
            i.loadBasicInfo();
            this.item = i;

            this.amount = amount;
        }
        public void updateSlot(){
            if(amount<=0){
                clear();
            }else{
                generateImage();
                
            }
        }
        public boolean add(Entity i){
            i.loadBasicInfo();
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
                if(item.swordInfo!=null || item.projectileInfo!=null || item.staffInfo!=null || item.potionInfo!=null|| item.bowInfo!=null){
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
                if(i.swordInfo!=null || i.projectileInfo!=null|| i.staffInfo!=null || i.potionInfo!=null|| i.bowInfo!=null){
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
                if(!InventoryBag.slotSelected)
                    infoPacket.update(400,70);
                
            }
        }
        public void reduce(byte amount){
            this.amount -= amount;
            if(amount <= 0){
                clear();
            }
            updateSlot();
        }
        public void clear(){
            item = null;
            amount = 0;
            infoPacket.update(400,70);
        }
        public void render(Graphics g){
            g.setColor(new Color(0,0,0,180));
            g.fillRect(x, y, width, height);
            g.setColor(Color.red);
            g.drawRect(x, y,width,height);
            
            if(item!=null){
                g.drawImage(texture, x, y, null);
                // g.drawImage(item.texture, x +3, y+3,width-6,height-6, null);
                // g.setColor(Color.red);
                // g.drawString(""+amount, x+width/2, y);
            }else{
                if(bgTexture!=null){
                    g.drawImage(bgTexture, x, y, null);
                }
            }
            if(highlight){
                g.setColor(Color.white);
                g.drawRect(x-1, y-1, width+2, height+2);
                if(!InventoryBag.slotSelected)
                    infoPacket.render(g);
            }
        }
    }
}
