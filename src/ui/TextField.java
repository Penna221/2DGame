package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import io.KeyManager;

public class TextField extends FunctionalElement{

    public boolean focus = false;
    public Text text;
    public TextField(int x, int y){
        super(x,y);
        bounds = new Rectangle(x,y,300,80);
        text = new Text("  ", bounds.x,bounds.y,0,false,false);
        task = new Task(){
            public void perform(){
                if(!focus){
                    focus = true;
                    KeyManager.finished = false;
                    KeyManager.textFieldFocus = true;
                    KeyManager.text = "";
                }else{
                    String a = KeyManager.text;
                    if(a.length()>0){
                        text = new Text(a, bounds.x,bounds.y,0,false,false);
                    }else{
                        text = new Text("  ", bounds.x,bounds.y,0,false,false);
                    }
                    text.updateBounds();
                    

                }
            }
        };
    }
    @Override
    public void render(Graphics g) {
        text.setPosition(x, (int)(y+bounds.height/2-text.bounds.getHeight()/2));
        if(focus){
            g.setColor(Color.red);
        }else{
            g.setColor(Color.white);
        }
        g.drawRect(bounds.x, bounds.y, bounds.width,bounds.height);
        text.render(g);
    }
    @Override
    public void updateAdditional() {
        if(KeyManager.finished){
            KeyManager.textFieldFocus = false;
            focus = false;
        }
    }
    
}
