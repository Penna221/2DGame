package ui;
import java.awt.Color;

import main.Game;
public abstract class FunctionalElement extends UIElement{
    public Color bg, brd;
    public boolean focused;
    public FunctionalElement(int x, int y) {
        super(x, y);
        UiHub.buttons.add(this);
    }
    public void update(){
        if(bounds.contains(Game.mm.mouseX,Game.mm.mouseY)){
            bg = UIFactory.highlightedButtonData.bgColor;
            brd = UIFactory.highlightedButtonData.borderColor;
            focused = true;
        }else{
            bg = UIFactory.buttonData.bgColor;
            brd = UIFactory.buttonData.borderColor;
            focused = false;
        }
        updateAdditional();
    }
    public void click(){
        if(focused){
            task();
        }
    }
    public abstract void updateAdditional();
    public abstract void task();
}
