package states;

import java.awt.Graphics;

import gfx.Transition;

public abstract class State {
    public static State currentState;
    public static State gameState, menuState;
    protected static boolean running = true;
    public static Transition transition;
    public static void createStates(){
        gameState = new GameState();
        gameState.init();
        menuState = new MenuState();
        menuState.init();
        currentState = menuState;
    }
    public static void setState(State s){
        
        s.init();
        running = false;

        transition = new Transition(2000){
            @Override
            public void task(){
                currentState = s;
                currentState.updateOnceBetweenTransitions();
            }
            @Override
            public void end(){
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
