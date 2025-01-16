package save;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import entities.Entity;
import entities.ai.PlayerAI;
import entities.player.Inventory;
import entities.player.Inventory.Slot;
import json.BooleanValue;
import json.JSON;
import json.KeyValuePair;
import json.NumberValue;
import json.ObjectValue;
import world.World;

public class SavedGame {
    public String saveName;
    public Inventory inventory;
    public int playerHealth;
    public int playerMaxHealth;
    public int dungeonCounter;
    public static HashMap<String, SavedGame> savedGames = new HashMap<String, SavedGame>();
    public static SavedGame currentSave;
    public int dungeonLevel;
    public SavedGame(String saveName, Inventory inv, int playerHealth, int maxHealth,int dungeonCounter, int dungeonLevel){
        this.saveName = saveName;
        this.inventory = inv;
        this.playerHealth = playerHealth;
        this.playerMaxHealth = maxHealth;
        this.dungeonCounter = dungeonCounter;
        this.dungeonLevel = dungeonLevel;
    }
    public static void startNewSave(String name){
        currentSave = new SavedGame(name, new Inventory(), 10, 10, 1,1);
    }
    public static void tryLoad(String save){
        currentSave = savedGames.get(save);

        // GameState.world = currentSave.world;
        PlayerAI.inv = currentSave.inventory;
        PlayerAI.inv.updateInventory();

        World.player.health = currentSave.playerHealth;
        World.player.maxHealth = currentSave.playerMaxHealth;
        World.dungeonCounter = currentSave.dungeonCounter;
        World.dungeonLevel = currentSave.dungeonLevel;
        //Load Player Stats
        // - Inventory
        // - currenthealth
        
        //Load World 
        // - map player is in

        //Load Unlocked characters
    }
    public static void loadSavedGames()throws Exception{
        //Check if save folder exists.
        File f = new File("savedGames");
        if(f.isDirectory()&& f.exists()){
            System.out.println("Save Folder exists");
        }else{
            System.out.println("Lets create Save Folder");
            if(f.mkdir()){
                System.out.println("Created Save Folder to: " + f.getAbsolutePath());
                return;
            }else{
                throw new Exception("Error occured while trying to create Save Folder");
            }
        }
        //Save folder exists.
        //It contains folders for each saved game instance.
        for(File folder : f.listFiles()){
            if(!folder.isDirectory()){
                continue;
            }
            File playerData = new File(folder, "player_data.json");
            
            if(!playerData.exists()){
                System.out.println("playerdata file not found");
                System.out.println("Corrupted save: " + folder.getName());
                continue;
            }
            
            JSON json = new JSON(playerData);
            KeyValuePair s = json.parse("JSON");
            KeyValuePair stats = s.findChild("stats");
            int ph = stats.findChild("health").getInteger();
            int mph = stats.findChild("maxHealth").getInteger();
            int dungeonCounter = stats.findChild("dungeonCounter").getInteger();
            int dungeonLevel = stats.findChild("dungeonLevel").getInteger();
            KeyValuePair inv = s.findChild("inventory");
            Inventory newInventory = readInventory(inv);
            
            SavedGame sa = new SavedGame(folder.getName(),newInventory,ph,mph,dungeonCounter,dungeonLevel);
            savedGames.put(folder.getName(),sa);

        }
    } 
    private static Inventory readInventory(KeyValuePair inv){
        System.out.println("Reading inventory file json");
        Inventory newInventory = new Inventory();
        
        KeyValuePair invSlots = inv.findChild("inventorySlots");
        KeyValuePair hotbarSlots = inv.findChild("hotbarSlots");
        KeyValuePair spellSlot = inv.findChild("spellSlot");
        KeyValuePair arrowSlot = inv.findChild("arrowSlot");
        //INVENTORY SLOTS
        for(KeyValuePair slot : invSlots.getObject()){
            if(slot.getKey().equals("empty")){
                continue;
            }
            int slotID = Integer.parseInt(slot.getKey());
            int itemID = slot.findChild("itemID").getInteger();
            int itemSubID = slot.findChild("itemSubID").getInteger();
            int itemAmount = slot.findChild("amount").getInteger();
            Entity e = World.entityManager.generateEntityWithID(itemID, itemSubID, 0,0);
            newInventory.inventorySlots[slotID].setItem(e, (byte)itemAmount);
        }
        //HotBAR SLOTS
        for(KeyValuePair slot : hotbarSlots.getObject()){
            if(slot.getKey().equals("empty")){
                continue;
            }
            int slotID = Integer.parseInt(slot.getKey());
            int itemID = slot.findChild("itemID").getInteger();
            int itemSubID = slot.findChild("itemSubID").getInteger();
            int itemAmount = slot.findChild("amount").getInteger();
            Entity e = World.entityManager.generateEntityWithID(itemID, itemSubID, 0,0);
            newInventory.hotbarSlots[slotID].setItem(e, (byte)itemAmount);
        }
        if(!arrowSlot.findChild("empty").getBoolean()){
            int arrowID = arrowSlot.findChild("itemID").getInteger();
            int arrowSubID = arrowSlot.findChild("itemSubID").getInteger();
            int arrowAmount = arrowSlot.findChild("amount").getInteger();
            Entity arrow = World.entityManager.generateEntityWithID(arrowID, arrowSubID,0,0);
            newInventory.specialSlots[0].setItem(arrow, (byte)arrowAmount);
        }
        
        if(!spellSlot.findChild("empty").getBoolean()){
            int spellID = spellSlot.findChild("itemID").getInteger();
            int spellSubID = spellSlot.findChild("itemSubID").getInteger();
            int spellAmount = spellSlot.findChild("amount").getInteger();
            Entity spell = World.entityManager.generateEntityWithID(spellID, spellSubID,0,0);
            newInventory.specialSlots[1].setItem(spell, (byte)spellAmount);
        }
        
        
        
        return newInventory;
    }
    public static void saveGame(){
        System.out.println("Saving game...");
        File folder;
        if(currentSave==null){
            System.out.println("something went wrong.");
            return;
        }else{
            folder = new File("savedGames/"+currentSave.saveName);
            if(!folder.exists()){
                folder.mkdirs();
            }
        }
        savePlayerStats(folder);

        System.out.println("DONE!");
    }
    private static void savePlayerStats(File folder){
        
        
        JSON p_data = new JSON();
        
        //Stats
        currentSave.playerHealth = World.player.health;
        currentSave.playerMaxHealth = World.player.maxHealth;
        ArrayList<KeyValuePair> keyValuePairs = new ArrayList<KeyValuePair>();
        KeyValuePair health_val = new KeyValuePair("health",new NumberValue(World.player.health));
        KeyValuePair maxhealth_val = new KeyValuePair("maxHealth",new NumberValue(World.player.maxHealth));
        KeyValuePair dungeonCounter_val = new KeyValuePair("dungeonCounter",new NumberValue(World.dungeonCounter));
        KeyValuePair dungeonLevel_val = new KeyValuePair("dungeonLevel",new NumberValue(World.dungeonLevel));
        ArrayList<KeyValuePair> stats_vals = new ArrayList<KeyValuePair>();
        stats_vals.add(health_val);
        stats_vals.add(maxhealth_val);
        stats_vals.add(dungeonCounter_val);
        stats_vals.add(dungeonLevel_val);
        KeyValuePair statsObj = new KeyValuePair("stats", new ObjectValue(stats_vals));
        keyValuePairs.add(statsObj);
        
        
        
        
        ArrayList<KeyValuePair> allInventoryArrays = new ArrayList<KeyValuePair>();
        //INVENTORY inventorySlots
        ArrayList<KeyValuePair> inventorySlot = new ArrayList<KeyValuePair>();
        for(int i = 0; i < PlayerAI.inv.inventorySlots.length;i++){
            Slot s = PlayerAI.inv.inventorySlots[i];
            ArrayList<KeyValuePair> slotObject = slotToKeyValuePair(s);
            if(slotObject!=null){
                System.out.println("Slot [" + i + "] not empty");
                ObjectValue inventorySlots = new ObjectValue(slotObject);
                String slotName = ""+i;
                inventorySlot.add(new KeyValuePair(slotName,inventorySlots));
            }else{
                System.out.println("Slot [" + i + "] empty");
            }
        }
        if(inventorySlot.size()==0){
            //Create Empty thing
            inventorySlot.add(generateEmptySlot());
        }
        KeyValuePair inventorySlotsObject = new KeyValuePair("inventorySlots", new ObjectValue(inventorySlot));
        allInventoryArrays.add(inventorySlotsObject);
        
        
        //HotbarSlots
        ArrayList<KeyValuePair> hotbarSlot = new ArrayList<KeyValuePair>();
        for(int i = 0; i < PlayerAI.inv.hotbarSlots.length;i++){
            Slot s = PlayerAI.inv.hotbarSlots[i];
            ArrayList<KeyValuePair> slotObject = slotToKeyValuePair(s);
            if(slotObject!=null){
                System.out.println("Slot [" + i + "] not empty");
                ObjectValue inventorySlots = new ObjectValue(slotObject);
                String slotName = ""+i;
                hotbarSlot.add(new KeyValuePair(slotName,inventorySlots));
            }else{
                System.out.println("Slot [" + i + "] empty");
            }
        }
        if(hotbarSlot.size()==0){
            //Create Empty thing
            hotbarSlot.add(generateEmptySlot());
        }
        KeyValuePair hotbarSlotsObject = new KeyValuePair("hotbarSlots", new ObjectValue(hotbarSlot));
        allInventoryArrays.add(hotbarSlotsObject);

        //SpellSlot
        ArrayList<KeyValuePair> spellSlot = new ArrayList<KeyValuePair>();
        boolean spellEmpty = false;
        int spellID = 0;
        int spellSubID = 0;
        int spellAmount = 0;
        if(PlayerAI.inv.specialSlots[1].item==null){
            spellEmpty = true;
        }else{
            spellID = PlayerAI.inv.specialSlots[1].item.info.id;
            spellSubID = PlayerAI.inv.specialSlots[1].item.subID;
            spellAmount = PlayerAI.inv.specialSlots[1].amount;
        }
        spellSlot.add(new KeyValuePair("empty",new BooleanValue(spellEmpty)));
        spellSlot.add(new KeyValuePair("itemID",new NumberValue(spellID)));
        spellSlot.add(new KeyValuePair("itemSubID",new NumberValue(spellSubID)));
        spellSlot.add(new KeyValuePair("amount",new NumberValue(spellAmount)));
        ObjectValue spellObj = new ObjectValue(spellSlot);

        allInventoryArrays.add(new KeyValuePair("spellSlot", spellObj));
        
        //arrowSlot
        ArrayList<KeyValuePair> arrowSlot = new ArrayList<KeyValuePair>();
        boolean arrowEmpty = false;
        int arrowID = 0;
        int arrowSubID = 0;
        int arrowAmount = 0;
        if(PlayerAI.inv.specialSlots[0].item==null){
            arrowEmpty = true;
        }else{
            arrowID = PlayerAI.inv.specialSlots[0].item.info.id;
            arrowSubID = PlayerAI.inv.specialSlots[0].item.subID;
            arrowAmount = PlayerAI.inv.specialSlots[0].amount;
        }
        arrowSlot.add(new KeyValuePair("empty",new BooleanValue(arrowEmpty)));
        arrowSlot.add(new KeyValuePair("itemID",new NumberValue(arrowID)));
        arrowSlot.add(new KeyValuePair("itemSubID",new NumberValue(arrowSubID)));
        arrowSlot.add(new KeyValuePair("amount",new NumberValue(arrowAmount)));
        ObjectValue arrowObj = new ObjectValue(arrowSlot);

        allInventoryArrays.add(new KeyValuePair("arrowSlot", arrowObj));



        KeyValuePair inventoryObj = new KeyValuePair("inventory", new ObjectValue(allInventoryArrays));
        keyValuePairs.add(inventoryObj);
        ObjectValue objectContainer = new ObjectValue(keyValuePairs);
        KeyValuePair obj = new KeyValuePair("", objectContainer);
        p_data.setKeyValuePair(obj);
        try {
            
            p_data.writeFile(new File(folder, "player_data.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static KeyValuePair generateEmptySlot(){
        KeyValuePair empty = new KeyValuePair("empty", new BooleanValue(true));
        return empty;
    }
    private static ArrayList<KeyValuePair> slotToKeyValuePair(Slot s){
        ArrayList<KeyValuePair> slotInfo = new ArrayList<KeyValuePair>();
        Entity item = s.item;
        
        if(item == null){
            return null;
        }
        int id = item.info.id;
        int subID = item.subID;
        int amt = s.amount;
        KeyValuePair itemID = new KeyValuePair("itemID", new NumberValue(id));
        KeyValuePair itemSubID = new KeyValuePair("itemSubID", new NumberValue(subID));
        KeyValuePair amount = new KeyValuePair("amount", new NumberValue(amt));
        slotInfo.add(itemID);
        slotInfo.add(itemSubID);
        slotInfo.add(amount);
        return slotInfo;
    }   
}
