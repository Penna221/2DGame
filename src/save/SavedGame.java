package save;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;

import cards.Card;
import entities.Entity;
import entities.ai.PlayerAI;
import entities.player.Inventory;
import entities.player.Inventory.Slot;
import json.ArrayValue;
import json.BooleanValue;
import json.DataType;
import json.JSON;
import json.KeyValuePair;
import json.NumberValue;
import json.ObjectValue;
import json.StringValue;
import world.World;

public class SavedGame {
    public String saveName;
    public Inventory inventory;
    public int dungeonCounter;
    public int questPoints;
    public int mana;

    public ArrayList<Card> ownedCards = new ArrayList<Card>();
    
    public static HashMap<String, SavedGame> savedGames = new HashMap<String, SavedGame>();
    public static SavedGame currentSave;
    public int playerLevel;
    private ArrayList<Card> wc, bc, ac;
    public SavedGame(String saveName, Inventory inv,int dungeonCounter,
     int playerLevel, int questPoints,ArrayList<Card> wc, ArrayList<Card> bc, ArrayList<Card> ac,
     int mana){
        this.saveName = saveName;
        this.inventory = inv;
        this.dungeonCounter = dungeonCounter;
        this.playerLevel = playerLevel;
        this.questPoints = questPoints;
        this.wc = wc;
        this.bc = bc;
        this.ac = ac;
        this.mana = mana;
    }
    public static void startNewSave(String name){
        currentSave = new SavedGame(name, new Inventory(), 1,1, 1,null,null,null,3);
    }
    public static void tryLoad(String save){
        currentSave = savedGames.get(save);

        // GameState.world = currentSave.world;
        PlayerAI.inv = currentSave.inventory;
        PlayerAI.inv.updateInventory();

        World.dungeonCounter = currentSave.dungeonCounter;
        World.playerLevel = currentSave.playerLevel;
        PlayerAI.questPoints = currentSave.questPoints;
        PlayerAI.buffCards = currentSave.bc;
        PlayerAI.weaponCards = currentSave.wc;
        PlayerAI.abilityCards = currentSave.ac;
        PlayerAI.mana = currentSave.mana;
        //Load Player Stats
        // - Inventory
        // - currenthealth
        
        //Load World 
        // - map player is in

        //Load Unlocked characters
    }
    public static void deleteSave(String save){
        File f = new File("savedGames");
        File file = new File(f,save);
        if(file.exists()){
            System.out.println("Deleting file: " + file.getAbsolutePath());
            try {
            Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);  // Delete each file
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);  // Delete directory after files inside are deleted
                    return FileVisitResult.CONTINUE;
                }
            });

            System.out.println("Folder deleted successfully.");
        } catch (IOException e) {
            System.err.println("Failed to delete folder: " + e.getMessage());
        }
        }
    }
    public static void loadSavedGames()throws Exception{
        savedGames.clear();
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
            int dungeonCounter = stats.findChild("dungeonCounter").getInteger();
            int playerLevel = stats.findChild("playerLevel").getInteger();
            int questPoints = stats.findChild("questPoints").getInteger();
            int mana = stats.findChild("mana").getInteger();
            KeyValuePair inv = s.findChild("inventory");
            // Inventory newInventory = readInventory(inv);
            Inventory newInventory = new Inventory();
            KeyValuePair cards = s.findChild("cards");
            DataType[] wc = cards.findChild("weapons").getArray();
            DataType[] bc = cards.findChild("buffs").getArray();
            DataType[] ac = cards.findChild("abilities").getArray();
            ArrayList<Card> weaponCards = readCards(wc,"Weapon");
            ArrayList<Card> buffCards = readCards(bc, "Buff");
            ArrayList<Card> abilityCards = readCards(ac, "Ability");
            SavedGame sa = new SavedGame(folder.getName(),newInventory,dungeonCounter,playerLevel,questPoints,weaponCards,buffCards, abilityCards, mana);
            savedGames.put(folder.getName(),sa);

        }
    } 
    private static ArrayList<Card> readCards(DataType[] cards, String type){
        ArrayList<Card> a = new ArrayList<Card>();
        
        if(cards[0].getString()!=null && cards[0].getString().equals("empty")){
            return a;
        }
        for(DataType d : cards){
            int v = d.getInteger();
            if(type.equals("Weapon")){
                a.add(Card.weapon_cards.get(v));
            }else if(type.equals("Buff")){
                a.add(Card.buff_cards.get(v));
            }else if(type.equals("Ability")){
                a.add(Card.ability_cards.get(v));
            }
        }
        return a;
        
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
        ArrayList<KeyValuePair> keyValuePairs = new ArrayList<KeyValuePair>();
        KeyValuePair dungeonCounter_val = new KeyValuePair("dungeonCounter",new NumberValue(World.dungeonCounter));
        KeyValuePair playerLevel_val = new KeyValuePair("playerLevel",new NumberValue(World.playerLevel));
        KeyValuePair questPoints_val = new KeyValuePair("questPoints",new NumberValue(PlayerAI.questPoints));
        KeyValuePair mana_val = new KeyValuePair("mana",new NumberValue(PlayerAI.mana));
        ArrayList<KeyValuePair> stats_vals = new ArrayList<KeyValuePair>();
        stats_vals.add(dungeonCounter_val);
        stats_vals.add(playerLevel_val);
        stats_vals.add(questPoints_val);
        stats_vals.add(mana_val);
        KeyValuePair statsObj = new KeyValuePair("stats", new ObjectValue(stats_vals));
        keyValuePairs.add(statsObj);
        
        // KeyValuePair inventoryObj = new KeyValuePair("inventory", new ObjectValue(saveInventory()));
        // keyValuePairs.add(inventoryObj);
        
        
        
        //Cards
        ArrayList<KeyValuePair> cardArrays = new ArrayList<KeyValuePair>();

        
        ArrayValue weapons = new ArrayValue(getCardArray(PlayerAI.weaponCards));
        cardArrays.add(new KeyValuePair("weapons", weapons));
       
        ArrayValue buffs = new ArrayValue(getCardArray(PlayerAI.buffCards));
        cardArrays.add(new KeyValuePair("buffs", buffs));

        ArrayValue abilities = new ArrayValue(getCardArray(PlayerAI.abilityCards));
        cardArrays.add(new KeyValuePair("abilities", abilities));



        KeyValuePair cardObject = new KeyValuePair("cards", new ObjectValue(cardArrays));
        keyValuePairs.add(cardObject);

        


        ObjectValue objectContainer = new ObjectValue(keyValuePairs);
        KeyValuePair obj = new KeyValuePair("", objectContainer);
        p_data.setKeyValuePair(obj);
        try {
            
            p_data.writeFile(new File(folder, "player_data.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static DataType[] getCardArray(ArrayList<Card> caca){
        DataType[] wa;
        if(caca.size()==0){
            wa = new DataType[1];
            wa[0] = new StringValue("empty");
        }else{
            wa = new DataType[caca.size()];
            for(int i = 0; i < wa.length; i++){
                int id = caca.get(i).id;
                wa[i] = new NumberValue(id);
            }
        }
        return wa;
    }
    private static ArrayList<KeyValuePair> saveInventory(){
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



        return allInventoryArrays;
        
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
