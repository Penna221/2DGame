package ui;
import java.awt.Color;

import main.Game;
import sound.SoundPlayer;
public abstract class FunctionalElement extends UIElement{
    public Color bg, brd;
    public boolean focused;
    public boolean press;
    public Task task;
    public boolean disabled = false;
    public FunctionalElement(int x, int y) {
        super(x, y);
    }
    public void update(){
        if(!disabled){
            if(bounds.contains(Game.mm.mouseX,Game.mm.mouseY)){
                if(!focused){
                    focused = true;
                    SoundPlayer.playSound("pick");
                }
                bg = UIFactory.highlightedButtonData.bgColor;
                brd = UIFactory.highlightedButtonData.borderColor;
            }else{
                bg = UIFactory.buttonData.bgColor;
                brd = UIFactory.buttonData.borderColor;
                focused = false;
            }
            updateAdditional();
        }else{
            bg = Color.red;
            brd = Color.red;
        }
    }
    public void setTask(Task t){
        this.task = t;
    }
    public boolean click(){
        if(focused){
            if(task!=null){
                task.perform();
                return true;
            }
            SoundPlayer.playSound("pick2");
        }
        return false;
    }
    public void press(){
        if(focused){
            if(task!=null){
                task.perform();
            }
            // SoundPlayer.playSound("pick2");
        }
    }
    public void toggle(boolean b){
        press = b;
    }
    public abstract void updateAdditional();
}
