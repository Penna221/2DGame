package cards;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.accessibility.AccessibleStateSet;

import entities.Entity;
import gfx.AssetStorage;
import gfx.Factory;
import json.DataType;
import json.JSON;
import json.KeyValuePair;
import ui.UIFactory;
import world.World;

public class Card {
    public String name;
    public BufferedImage texture,icon;
    public String[] info;
    public String rarity;
    public String type;
    public int id;
    public int itemID1, itemID2;
    public static HashMap<Integer, Card> weapon_cards = new HashMap<Integer,Card>();
    public static HashMap<Integer, Card> buff_cards = new HashMap<Integer,Card>();
    public static HashMap<Integer, Card> ability_cards = new HashMap<Integer,Card>();
    public int levelReq;
    public int cost;
    public Card(int id, String name, String rarity, String type, String[] info,int itemID1, int itemID2,BufferedImage text, int levelReq, int cost){
        this.id = id;
        this.name = name;
        this.info = info;
        this.rarity = rarity;
        this.type = type;
        this.itemID1 = itemID1;
        this.itemID2 = itemID2;
        if(text!=null){
            this.icon = text;
        }else{
            Entity e = World.entityManager.generateEntityWithID(itemID1, itemID2,0,0);
            this.icon = e.texture;
        }
        this.levelReq = levelReq;
        this.cost = cost;
        generateTexture();
    }
    private void generateTexture(){
        BufferedImage base;
        switch (rarity) {
            case "Common":
                base = AssetStorage.images.get("common_card");
                break;
            case "Rare":
                base = AssetStorage.images.get("rare_card");
                break;
            case "Curse":
                base = AssetStorage.images.get("curse_card");
                break;
            default:
                base = AssetStorage.images.get("common_card");
                break;
        }
        BufferedImage i = new BufferedImage(base.getWidth(),base.getHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics g = i.getGraphics();
        g.drawImage(base,0,0,null);
        int startX = 7;
        int startY = 27;
        int w = 31;
        int x = startX+(w/2) - icon.getWidth()/2;
        int y = startY+(w/2) - icon.getHeight()/2;
        g.drawImage(icon,x,y,null);
        texture = AssetStorage.scaleImage(i, 3.5f);
        int buffer = 10;
        int maxW = texture.getWidth()-buffer*2;
        g = texture.getGraphics();
        BufferedImage title = UIFactory.generateText(name, 400);
        if(title.getWidth()>maxW){
            title = UIFactory.scaleToWidth(title, maxW);
        }
        g.drawImage(title, buffer, 10, null);  
        BufferedImage manaball = generateManaBall();
        g.drawImage(manaball,texture.getWidth()-manaball.getWidth(),texture.getHeight()-manaball.getHeight(),null);
    }
    private BufferedImage generateManaBall(){
        BufferedImage manaball = AssetStorage.images.get("mana_ball");
        BufferedImage image = new BufferedImage(manaball.getWidth(), manaball.getHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.createGraphics();
        g.drawImage(manaball, 0,0,null);
        BufferedImage manacost = UIFactory.generateText(""+cost, 400);
        manacost =AssetStorage.scaleImage(manacost,0.5f);
        g.drawImage(manacost, image.getWidth()/2-manacost.getWidth()/2,image.getHeight()/2 - manacost.getHeight()/2, null);
        // image = AssetStorage.scaleImage(image, 2.3f);
        // BufferedImage manacost = UIFactory.generateIconWithAmount(manaball, cost, image.getWidth(),image.getHeight(),false,null);

        return image;
    }


    public static void loadCards(){
        File f = new File("res\\json\\cards.json");
        JSON json = new JSON(f);
        json.readFile(false);
        KeyValuePair kv = json.parse("json");
        ArrayList<KeyValuePair> weapons = kv.findChild("weapons").getObject();
        ArrayList<KeyValuePair> buffs = kv.findChild("buffs").getObject();
        ArrayList<KeyValuePair> abilities = kv.findChild("abilities").getObject();

        for(KeyValuePair weapon : weapons){
            Card c = loadCard(weapon);
            weapon_cards.put(c.id, c);
        }
        for(KeyValuePair buff : buffs){
            Card c = loadCard(buff);
            buff_cards.put(c.id, c);
        }
        for(KeyValuePair ability : abilities){
            Card c = loadCard(ability);
            ability_cards.put(c.id, c);
        }
    }
    private static Card loadCard(KeyValuePair card){
        int id = Integer.parseInt(card.getKey());
        String name = card.findChild("name").getString();
        String rarity = card.findChild("rarity").getString();
        String type = card.findChild("type").getString();
        DataType[] tt = card.findChild("info").getArray();
        String[] info = new String[tt.length];
        for(int i = 0; i < info.length; i++){
            info[i] = tt[i].getString();
        }
        DataType[] bb = card.findChild("item_id").getArray();
        int id1 = bb[0].getInteger();
        int id2 = bb[1].getInteger();
        
        KeyValuePair kv = card.findChild("texture");
        BufferedImage texture = null;
        if(kv!=null){
            texture = AssetStorage.images.get(kv.getString());
        }

        int levelReq = card.findChild("levelReq").getInteger();
        int cost = card.findChild("cost").getInteger();
        Card c = new Card(id,name,rarity,type,info,id1,id2,texture, levelReq,cost);
        return c;
    }
}
