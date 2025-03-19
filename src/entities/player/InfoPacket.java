package entities.player;

import java.awt.Color;
import java.awt.Graphics;

import entities.Entity;
import entities.ai.PlayerAI;
import ui.Container;
import ui.Text;

public class InfoPacket {
    public Container c;
    public int x, y;
    public boolean draw = false;
    public InfoPacket(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void update(Entity e,int x, int y, int maxWidth, int height, boolean center){
        this.x = x;
        this.y = y;
        draw = false;
        if(e!=null){
            c = new Container(0,0,maxWidth,height);
            c.fillBg = true;
            Text itemName = new Text(e.name, 0, 1,maxWidth, false,false);
            itemName.fillBg = true;
            itemName.overrideColor = new Color(0,0,0,200);
            c.addElement(itemName);
            int amt = PlayerAI.inv.calculateAmount(e);
            Text amount = new Text("You have: "+amt, 0, itemName.bounds.height+10,maxWidth, false,false);
            c.addElement(amount);
            

            int d = e.getItemDamage();
            if(d!=0){
                Text damage = new Text("Damage: " + d, 0, amount.bounds.y+amount.bounds.height+10, maxWidth, false,false);
                damage.overrideColor = Color.RED;
                damage.fillBg = true;
                c.addElement(damage);
            }
            c.updateBounds();
            c.wrap();
            c.setPosition(x, y);
            if(center){
                c.setPosition(x-c.bounds.width/3, y-c.bounds.height-10);
            }
            draw = true;
        }
    }
    public void render(Graphics g){
        if(c!=null){
            if(draw){
                c.render(g);
            }
        }
    }
}
