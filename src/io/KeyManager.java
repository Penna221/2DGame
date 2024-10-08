package io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import entities.ai.PlayerAI;
import gfx.Transition;
import states.GameState;
import states.State;
import ui.PauseMenu;
import world.World;

public class KeyManager implements KeyListener{

    public static boolean up, down, left, right;
    public static boolean interactKey;
    @Override
    public void keyTyped(KeyEvent e) {
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
            case KeyEvent.VK_F5:
                PlayerAI.hud.update();
                break;
            
            case KeyEvent.VK_ESCAPE:
                if(State.getState() == State.gameState){
                    //If there is a container like talk dialog, close it. If there is a dialog, game is paused.
                    //If there is NOT a container, you are playing and need to get paused.
                    if(PauseMenu.currentContainer==null){
                        PauseMenu.setContainer(PauseMenu.containers.get("basic"));
                        GameState.paused = true;
                    }else{
                        PauseMenu.setContainer(null);
                        GameState.paused = false;
                    }
                }
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
