package io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import states.State;
import world.World;

public class KeyManager implements KeyListener{

    public static boolean up, down, left, right;
    @Override
    public void keyTyped(KeyEvent e) {
        
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
            case KeyEvent.VK_F2:
                World.map.generate(World.FOREST);
                break;
            case KeyEvent.VK_F3:
                State.setState(State.menuState);
                break;
                case KeyEvent.VK_F4:
                State.setState(State.gameState);
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
            default:
                break;
        }
    }
    
}
