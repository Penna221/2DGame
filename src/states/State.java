package states;

import java.awt.Graphics;

import gfx.Transition;
import main.Game;
import sound.SoundPlayer;
import ui.Container;
import ui.PauseMenu;

public abstract class State {
    public static State currentState;
    public static State gameState, menuState,nullState,settingsState,loadState,newGameState;
    public static State inventoryState;
    public static State traderState, blacksmithState,witchState;
    public static State deadState;
    protected static boolean running = true;
    public static Transition transition;
    public static void createStates() throws Exception{
        transition = new Transition(2500);
        transition.getRandomImage();
        gameState = new GameState();
        gameState.init();
        menuState = new MenuState();
        loadState = new LoadSaveState();
        traderState = new TraderState();
        blacksmithState = new BlacksmithState();
        witchState = new WitchState();
        settingsState = new SettingsState();
        deadState = new DeadState();
        inventoryState = new InventoryState();
        newGameState = new NewGameState();
        nullState = new NullState();
        nullState.init();
        currentState = nullState;
    }
    public static void setState(State s, boolean trans){
        PauseMenu.setContainer(null);
        Container.focus = false;
        Game.updateCursor("default_cursor");
        if(currentState !=nullState){
            //System.out.println("clearing buttons");
        }
        if(!trans){
            s.init();
            currentState = s;
            currentState.updateOnceBetweenTransitions();
            
            return;
        }
        
        running = false;
        transition = new Transition(2000){
            @Override
            public void task(){
                Thread t = new Thread(){
                    @Override
                    public void run(){
                        s.init();
                        currentState = s;
                        currentState.updateOnceBetweenTransitions();
                        canContinue = true;
                    }
                };
                t.start();
            }
            @Override
            public void end(){
                SoundPlayer.stopAllSounds();
                State.running = true;
            }
        };
        transition.start();
    }
    public static State getState(){return currentState;}
    public abstract void update();
    public abstract void render(Graphics g);
    public abstract void init();
    public abstract void updateOnceBetweenTransitions();
}
