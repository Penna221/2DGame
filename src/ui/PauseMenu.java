package ui;
import java.awt.Graphics;

import gfx.Transition;
import main.Game;
import sound.SoundPlayer;
import states.GameState;
import states.State;

public class PauseMenu {
    
    private Container c;
    private int x, y,w,h;
    private ClickButton returnButton, settingsButton, exitButton;
    public PauseMenu(){}
    public void init(){
        w = 600;
        h = 700;
        x = Game.w.getWidth() /2 - w/2;
        y = Game.w.getHeight() /2 - h/2;
        c = new Container(x,y,w,h);

        
        returnButton = new ClickButton(0,250,new Text("Return to game",w/2,0,150,false)){
            @Override
            public void task(){
                // GameState.paused = false;
                SoundPlayer.playSound("test3");
            }
        };
        settingsButton = new ClickButton(0,250,new Text("Settings",w/2,0,150,false)){
            @Override
            public void task(){
                SoundPlayer.playSound("test");
            }
        };
        exitButton = new ClickButton(0,250,new Text("Save and exit",w/2,0,150,false)){
            @Override
            public void task(){
                GameState.paused = false;
                State.setState(State.menuState,true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
        c.addElement(returnButton);
        c.addElement(settingsButton);
        c.addElement(exitButton);
        c.centerElements();
        c.spaceOutVertically(35);
    }
    public void update(){
        x = Game.w.getWidth() /2 - w/2;
        y = Game.w.getHeight() /2 - h/2;
        c.setPosition(x,y);
        c.centerElements();
        c.update();
    }
    public void render(Graphics g){
        c.render(g);
    }
}
