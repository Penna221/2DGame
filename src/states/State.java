package states;

import java.awt.Graphics;

public abstract class State {
    public static State currentState;
    public static State gameState, menuState;
    public static void createStates(){
        gameState = new GameState();
        menuState = new MenuState();
    }
    public static void setState(State s){currentState = s;}
    public static State getState(){return currentState;}
    public abstract void update();
    public abstract void render(Graphics g);
}
