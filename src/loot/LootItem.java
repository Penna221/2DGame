package loot;

public class LootItem {
    public String name;
    public int min, max;
    public int id,id2;
    public LootItem(String name, int id, int id2, int min, int max){
        this.name = name;
        this.min = min;
        this.max = max;
        this.id = id;
        this.id2 = id2;
    }
}
