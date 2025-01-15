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
    public World world;
    public static HashMap<String, SavedGame> savedGames = new HashMap<String, SavedGame>();
    public static SavedGame currentSave;
    public SavedGame(Inventory inv, World w){
        this.inventory = inv;
        this.world = w;
    }
    public static void tryLoad(String save){
        boolean loadSuccess = false;
        currentSave = savedGames.get(save);

        // GameState.world = currentSave.world;
        PlayerAI.inv = currentSave.inventory;
        PlayerAI.inv.updateInventory();
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
            //Build saved game objects from files.
            if(playerData.exists()){
                System.out.println("json exists");
            }else{
                System.out.println("playerdata file not found");
                throw new Exception("Player Data file not found in folder: " + folder.getAbsolutePath());
            }
            Inventory inv = readInventory(playerData);
            
            SavedGame s = new SavedGame(inv,null);
            savedGames.put(folder.getName(),s);

        }
    } 
    private static Inventory readInventory(File f){
        System.out.println("Reading inventory file json");
        
        Inventory newInventory = new Inventory();
        JSON json = new JSON(f);
        System.out.println("0");
        KeyValuePair s = json.parse("JSON");
        System.out.println("a");
        KeyValuePair inv = s.findChild("inventory");
        System.out.println("1");
        KeyValuePair invSlots = inv.findChild("inventorySlots");
        System.out.println("2");
        KeyValuePair hotbarSlots = inv.findChild("hotbarSlots");
        System.out.println("3");
        KeyValuePair spellSlot = inv.findChild("spellSlot");
        System.out.println("4");
        KeyValuePair arrowSlot = inv.findChild("arrowSlot");
        System.out.println("5");
        //INVENTORY SLOTS
        for(KeyValuePair slot : invSlots.getObject()){
            if(slot.getKey().equals("empty")){
                continue;
            }
            int slotID = Integer.parseInt(slot.getKey());
            int itemID = slot.findChild("itemID").getInteger();
            int itemSubID = slot.findChild("itemSubID").getInteger();
            int itemAmount = slot.findChild("amount").getInteger();
            System.out.println("entitymanager");
            Entity e = World.entityManager.generateEntityWithID(itemID, itemSubID, 0,0);
            newInventory.inventorySlots[slotID].setItem(e, (byte)itemAmount);
        }
        System.out.println("6");
        //HotBAR SLOTS
        for(KeyValuePair slot : hotbarSlots.getObject()){
            if(slot.getKey().equals("empty")){
                continue;
            }
            System.out.println("slotID");
            int slotID = Integer.parseInt(slot.getKey());
            System.out.println("ItemID");
            int itemID = slot.findChild("itemID").getInteger();
            System.out.println("SubID");
            int itemSubID = slot.findChild("itemSubID").getInteger();
            System.out.println("amount");
            int itemAmount = slot.findChild("amount").getInteger();
            Entity e = World.entityManager.generateEntityWithID(itemID, itemSubID, 0,0);
            newInventory.hotbarSlots[slotID].setItem(e, (byte)itemAmount);
        }
        System.out.println("7");
        if(!arrowSlot.findChild("empty").getBoolean()){
            int arrowID = arrowSlot.findChild("itemID").getInteger();
            int arrowSubID = arrowSlot.findChild("itemSubID").getInteger();
            int arrowAmount = arrowSlot.findChild("amount").getInteger();
            Entity arrow = World.entityManager.generateEntityWithID(arrowID, arrowSubID,0,0);
            newInventory.specialSlots[0].setItem(arrow, (byte)arrowAmount);
        }
        
        System.out.println("8");
        if(!spellSlot.findChild("empty").getBoolean()){
            int spellID = spellSlot.findChild("itemID").getInteger();
            int spellSubID = spellSlot.findChild("itemSubID").getInteger();
            int spellAmount = spellSlot.findChild("amount").getInteger();
            Entity spell = World.entityManager.generateEntityWithID(spellID, spellSubID,0,0);
            newInventory.specialSlots[1].setItem(spell, (byte)spellAmount);
        }
        System.out.println("9");
        
        
        
        return newInventory;
    }
    public static void saveGame(){
        if(currentSave!=null){
            //Create save files in folder.
        }
    }
    
}
