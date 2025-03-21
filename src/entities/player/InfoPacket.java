package entities.player;

import java.awt.Color;
import java.awt.Graphics;

import ui.Container;
import ui.Text;

public class InfoPacket {
    public Container c;
    public int x, y;
    public String[] rows;
    public boolean focused = false;
    public InfoPacket(String[] info, int x, int y){
        this.x = x;
        this.y = y;
        if(info!=null){
            this.rows = info;
        }else{
            rows = new String[1];
            rows[0] = "Null";
        }
        generate();
    }
    private void generate(){
        c = new Container(0,0,800,250);
        for(String s : rows){
            Text t = new Text(s, 0,0,(int)(c.bounds.getWidth()),false,false);
            c.addElement(t);
        }
        c.fillBg = true;
        c.overrideColor = Color.red;
        c.spaceOutVertically(20);
        c.wrap();
    }
    public void update(int x, int y){
        this.x = x;
        this.y = y;
        c.setPosition(x, y);
        // draw = false;
        // if(e!=null){
        //     c = new Container(0,0,maxWidth,height);
        //     c.fillBg = true;
        //     Text itemName = new Text(e.name, 0, 1,maxWidth, false,false);
        //     itemName.fillBg = true;
        //     itemName.overrideColor = new Color(0,0,0,200);
        //     c.addElement(itemName);
        //     int amt = PlayerAI.inv.calculateAmount(e);
        //     Text amount = new Text("You have: "+amt, 0, itemName.bounds.height+10,maxWidth, false,false);
        //     c.addElement(amount);
            

        //     int d = e.getItemDamage();
        //     if(d!=0){
        //         Text damage = new Text("Damage: " + d, 0, amount.bounds.y+amount.bounds.height+10, maxWidth, false,false);
        //         damage.overrideColor = Color.RED;
        //         damage.fillBg = true;
        //         c.addElement(damage);
        //     }
        //     c.updateBounds();
        //     c.wrap();
        //     c.setPosition(x, y);
        //     if(center){
        //         c.setPosition(x-c.bounds.width/3, y-c.bounds.height-10);
        //     }
        //     draw = true;
        // }
    }
    public void render(Graphics g){
        if(c!=null){
            c.render(g);
        }
    }
}
