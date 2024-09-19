package loot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import json.DataType;
import json.JSON;
import json.KeyValuePair;
import json.ObjectValue;

public class Market {
    public int id, id2, amount, amount2;
    public static HashMap<String,ArrayList<Market>> lists = new HashMap<String,ArrayList<Market>>();
    public Market(int id, int amount, int id2, int amount2){
        this.id = id;
        this.amount = amount;
        this.id2 = id2;
        this.amount2 = amount2;
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
                
                int id = -1;
                int amount = -1;
                int priceId = -1;
                int priceAmount = -1;
                for(KeyValuePair field : data){
                    String key = field.getKey();
                    switch (key) {
                        case "id":
                            id = field.getInteger();
                            break;
                        case "amount":
                            amount = field.getInteger();
                            break;
                        case "price":
                            priceId = field.findChild("item").getInteger();
                            priceAmount = field.findChild("amount").getInteger();
                            break;

                        default:
                            break;
                    }
                }
                if(id!=-1 && amount!=-1 && priceId!=-1 && priceAmount!=-1){
                    //All good.
                    itemsToSell.add(new Market(id,amount,priceId,priceAmount));
                }
                
            }
            lists.put(name,itemsToSell);
        }

    }
}
