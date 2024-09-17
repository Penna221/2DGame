package loot;

public class LootItem {
    public String name;
    public int min, max;
    public int id;
    public LootItem(String name, int min, int max){
        this.name = name;
        this.min = min;
        this.max = max;

    }
}
