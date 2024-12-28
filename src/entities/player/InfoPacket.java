package entities.player;

import java.awt.Graphics;

import entities.Entity;
import ui.Container;
import ui.Text;

public class InfoPacket {
    private Container c;
    private Text itemName;
    private Text damage;
    public int x, y;
    public InfoPacket(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void update(Entity e,int x, int y){
        this.x = x;
        this.y = y;
        c = null;
        if(e!=null){
            int maxWidth = 300;
            int height = 70;
            c = new Container((x-maxWidth/3), y-height-10, maxWidth, height);
            c.fillBg = true;
            itemName = new Text(e.name, 0, 0,maxWidth, false);
            c.addElement(itemName);
            int d = e.getItemDamage();
            if(d!=0){
                damage = new Text("Damage: " + d, 0, itemName.bounds.height+10, maxWidth, false);
                c.addElement(damage);
            }
            c.updateBounds();
            c.wrap();
            c.setPosition((x-c.bounds.width/3), y-c.bounds.height-10);
        }
    }
    public void render(Graphics g){
        if(c!=null){
            c.render(g);
        }
    }
}
