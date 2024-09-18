package ui;
import java.awt.Color;

import main.Game;
import sound.SoundPlayer;
public abstract class FunctionalElement extends UIElement{
    public Color bg, brd;
    public boolean focused;
    public boolean press;
    public Task task;
    public FunctionalElement(int x, int y) {
        super(x, y);
    }
    public void update(){
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
    }
    public void setTask(Task t){
        this.task = t;
    }
    public void click(){
        if(focused){
            if(task!=null){
                task.perform();
            }
            SoundPlayer.playSound("pick2");
        }
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
