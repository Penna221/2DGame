package states;

import java.awt.Graphics;

import gfx.Transition;
import ui.UiHub;

public abstract class State {
    public static State currentState;
    public static State gameState, menuState,nullState,settingsState;
    public static State traderState, blacksmithState;
    protected static boolean running = true;
    public static Transition transition;
    public static void createStates(){
        transition = new Transition(2500);
        transition.getRandomImage();
        gameState = new GameState();
        gameState.init();
        menuState = new MenuState();
        menuState.init();
        traderState = new TraderState();
        blacksmithState = new BlacksmithState();
        //NullState just because i want loading transition for game launching.
        nullState = new NullState();
        nullState.init();
        settingsState = new SettingsState();
        settingsState.init();
        currentState = nullState;
    }
    public static void setState(State s, boolean trans){
        
        if(currentState !=nullState){
            //System.out.println("clearing buttons");
        }
        if(!trans){
            s.init();
            currentState = s;
            currentState.updateOnceBetweenTransitions();
            
            UiHub.finalStep();
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
                System.out.println("end");
                State.running = true;
                UiHub.finalStep();
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
