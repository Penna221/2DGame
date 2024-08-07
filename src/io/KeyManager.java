package io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gfx.Transition;
import states.GameState;
import states.State;
import world.World;

public class KeyManager implements KeyListener{

    public static boolean up, down, left, right;
    public static boolean interactKey;
    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("You Pressed: " + e.getKeyChar());
        char c = e.getKeyChar();
        char c2 = Character.toLowerCase(c);
        if(c2 == 'e'){

        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_A:
                left = true;
                break;
            case KeyEvent.VK_D:
                right = true;
                break;
            case KeyEvent.VK_W:
                up = true;
                break;
            case KeyEvent.VK_S:
                down = true;
                break;
            case KeyEvent.VK_E:
                interactKey = true;
                break;
            case KeyEvent.VK_F2:
                World.load("dungeon_lvl2");
                break;
            case KeyEvent.VK_F3:
                State.setState(State.menuState,true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
                break;
            case KeyEvent.VK_F4:
                State.setState(State.gameState,false);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
                break;
            case KeyEvent.VK_ESCAPE:
                if(State.getState() == State.gameState)
                    GameState.paused = !GameState.paused;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_A:
                left = false;
                break;
            case KeyEvent.VK_D:
                right = false;
                break;
            case KeyEvent.VK_W:
                up = false;
                break;
            case KeyEvent.VK_S:
                down = false;
                break;
            case KeyEvent.VK_E:
                interactKey = false;
                break;
            default:
                break;
        }
    }
    
}
