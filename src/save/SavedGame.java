package save;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import entities.Entity;
import entities.EntityManager;
import entities.ai.PlayerAI;
import entities.player.Inventory;
import json.DataType;
import json.JSON;
import json.KeyValuePair;
import states.GameState;
import world.World;

public class SavedGame {
    public Inventory inventory;
    public int playerHealth;
    public int playerMaxHealth;
    public static HashMap<String, SavedGame> savedGames = new HashMap<String, SavedGame>();
    public static SavedGame currentSave;
    public SavedGame(Inventory inv, int playerHealth, int maxHealth){
        this.inventory = inv;
        this.playerHealth = playerHealth;
        this.playerMaxHealth = maxHealth;
    }
    public static void tryLoad(String save){
        boolean loadSuccess = false;
        currentSave = savedGames.get(save);

        // GameState.world = currentSave.world;
        PlayerAI.inv = currentSave.inventory;
        PlayerAI.inv.updateInventory();

        World.player.health = currentSave.playerHealth;
        World.player.maxHealth = currentSave.playerMaxHealth;
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
            File unlockData = new File(folder, "unlocks.json");
            
            if(!playerData.exists()){
                System.out.println("playerdata file not found");
                System.out.println("Corrupted save: " + folder.getName());
                continue;
            }
            
            if(!unlockData.exists()){
                System.out.println("UnlockData file not found");
                System.out.println("Corrupted save: " + folder.getName());
                continue;
            }
            JSON json = new JSON(playerData);
            KeyValuePair s = json.parse("JSON");
            KeyValuePair stats = s.findChild("stats");
            int ph = stats.findChild("health").getInteger();
            int mph = stats.findChild("maxHealth").getInteger();
            KeyValuePair inv = s.findChild("inventory");
            Inventory newInventory = readInventory(inv);
            
            SavedGame sa = new SavedGame(newInventory,ph,mph);
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
        if(currentSave!=null){
            //Create save files in folder.
        }
    }
    
}
