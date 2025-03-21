package ui;
import java.awt.Color;

import entities.ai.PlayerAI;
import entities.player.InfoPacket;
import io.KeyManager;
import main.Game;
import sound.SoundPlayer;
import world.World;
public abstract class FunctionalElement extends UIElement{
    public Color bg, brd;
    public boolean focused;
    public boolean press;
    public Task task;
    public boolean disabled = false;
    
    public InfoPacket infoPacket;
    public boolean hoverOver = false;
    public FunctionalElement(int x, int y) {
        super(x, y);
    }
    public void setInfoPacket(InfoPacket info){
        this.infoPacket = info;
    }
    public void update(){
        if(!disabled){
            if(KeyManager.textFieldFocus){
                if(!(this instanceof TextField)){
                    return;
                }
            }
            if(bounds.contains(Game.mm.mouseX,Game.mm.mouseY)){
                if(!focused){
                    focused = true;
                    // SoundPlayer.playSound("pick");
                }
                if(infoPacket!=null){
                    int ix = (int)((bounds.x + bounds.getWidth()/2) - infoPacket.c.bounds.getWidth()/2);
                    int iy = (int)(bounds.y+bounds.height + 20);
                    if(ix < 0){
                        ix = 0;
                    }
                    if(iy < 0){
                         iy = 0;
                    }
                    if(ix + infoPacket.c.bounds.getWidth() > Game.w.getWidth()){
                        ix = (int)(Game.w.getWidth()-infoPacket.c.bounds.getWidth());
                    }
                    if(iy + infoPacket.c.bounds.getHeight() > Game.w.getHeight()){
                        iy = (int)(Game.w.getHeight()-infoPacket.c.bounds.getHeight());
                    }
                    infoPacket.update(ix,iy);
                }
                bg = UIFactory.highlightedButtonData.bgColor;
                brd = UIFactory.highlightedButtonData.borderColor;
            }else{
                if(!KeyManager.textFieldFocus){
                    bg = UIFactory.buttonData.bgColor;
                    brd = UIFactory.buttonData.borderColor;
                    focused = false;
                }
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
        if(KeyManager.textFieldFocus){
            if(!(this instanceof TextField)){
                return false;
            }
        }
        if(focused){
            if(task!=null){
                SoundPlayer.playNonStoppableSound("pick2");
                task.perform();
                return true;
            }
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
