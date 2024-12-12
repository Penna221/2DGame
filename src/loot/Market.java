package loot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import json.DataType;
import json.JSON;
import json.KeyValuePair;
import json.ObjectValue;

public class Market {
    public MarketItem sellItem;
    public ArrayList<MarketItem> priceItems;
    public static HashMap<String,ArrayList<Market>> lists = new HashMap<String,ArrayList<Market>>();
    public Market(MarketItem sell, ArrayList<MarketItem> priceItems){
        this.sellItem =sell;
        this.priceItems = priceItems;
    }
    public static void load() throws Exception{
        JSON json = new JSON(new File("res\\json\\market.json"));
        json.readFile(false);
        KeyValuePair kv = json.parse("json");
        ArrayList<KeyValuePair> market = kv.getObject();
        
        for(KeyValuePair k : market){
            ArrayList<Market> itemsToSell = new ArrayList<Market>();
            String name = k.getKey();
            DataType[] items = k.getArray();
            for(DataType d : items){
                ObjectValue obj = (ObjectValue)d;
            
                ArrayList<KeyValuePair> data =  obj.getObject();
                Market m = process(data);
                itemsToSell.add(m);
            }
            lists.put(name,itemsToSell);
        }

    }
    private static Market process(ArrayList<KeyValuePair> data){
        DataType[] ids = data.get(0).getArray();

        int id = ids[0].getInteger();
        int subId = -1;
        int subId2 = -1;
        if(ids.length>1){
            //Has sub id.
            subId = ids[1].getInteger();
        }
        int amount = data.get(1).getInteger();
        DataType[] array = data.get(2).getArray();
        ArrayList<MarketItem> priceItems = new ArrayList<MarketItem>();
        for(DataType d : array){
            ObjectValue obj = (ObjectValue)d;
            ArrayList<KeyValuePair> items = obj.getObject();
            DataType[] ids2 = items.get(0).getArray();
            int id2 = ids2[0].getInteger();
            subId2 = -1;
            if(ids2.length>1){
                //Has sub Id
                subId2 = ids2[1].getInteger();
            }

            

            int amount2 = items.get(1).getInteger();
            MarketItem i = new MarketItem(id2, subId2,amount2);
            priceItems.add(i);
        }

        MarketItem toSell = new MarketItem(id, subId,amount);
        Market m = new Market(toSell,priceItems);
                
        return m;
    }
    
}
