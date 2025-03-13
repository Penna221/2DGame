package io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import entities.Entity;
import entities.ai.PlayerAI;
import gfx.Transition;
import save.SavedGame;
import states.GameState;
import states.State;
import ui.PauseMenu;
import world.Camera;
import world.World;

public class KeyManager implements KeyListener{

    public static boolean up, down, left, right;
    public static boolean interactKey;
    public static boolean textFieldFocus = false;
    public static boolean finished = true;
    public static String text = "";
    @Override
    public void keyTyped(KeyEvent e) {
        if(textFieldFocus){
            char c = e.getKeyChar();
            if(c=='\n'){
                finished = true;
            }else if(c=='\b'){
                if(text.length()>0){
                    text = text.substring(0,text.length()-1);
                }
            }else{
                if(text.length()<12){
                    text += c;
                }
                System.out.println(c);
            }
            PauseMenu.currentContainer.sendClick();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(textFieldFocus){
            // if(e.getKeyCode()== KeyEvent.VK_BACK_SPACE){
            //     text = text.substring(0,text.length()-1);
            // }
            return;
        }
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
            case KeyEvent.VK_F:
                interactKey = true;
                break;
            case KeyEvent.VK_E:
                if(State.currentState== State.gameState){
                    // GameState.paused = true;
                    // State.setState(State.inventoryState,true);
                    // Transition.canContinue2 = true;
                    // Transition.canFinish = true;
                }
                break;
            case KeyEvent.VK_F1:
                PlayerAI.hud.showExtra = !PlayerAI.hud.showExtra;
                break;
            case KeyEvent.VK_F2:
                // World.load("cave_goblin","");
                break;
            case KeyEvent.VK_F3:
                State.setState(State.menuState,true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
                break;
            case KeyEvent.VK_F4:
                // State.setState(State.gameState,false);
                // Transition.canContinue2 = true;
                // Transition.canFinish = true;
                break;
            case KeyEvent.VK_F5:
                // Camera.light = !Camera.light;
                break;
            case KeyEvent.VK_F6:
                Entity.drawBounds = !Entity.drawBounds;
                World.drawCollisionBoxes = !World.drawCollisionBoxes;
                break;
            case KeyEvent.VK_F7:
                SavedGame.saveGame();
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
                }else if(State.getState()==State.inventoryState){
                    State.setState(State.gameState, true);
                    Transition.canContinue2 = true;
                    Transition.canFinish = true;
                    GameState.paused = false;
                }

                break;
            case KeyEvent.VK_1:
                PlayerAI.inv.select(0);
                break;
            case KeyEvent.VK_2:
                PlayerAI.inv.select(1);
                break;
            case KeyEvent.VK_3:
                PlayerAI.inv.select(2);
                break;
            case KeyEvent.VK_4:
                PlayerAI.inv.select(3);
                break;
            case KeyEvent.VK_5:
                PlayerAI.inv.select(4);
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
            case KeyEvent.VK_F:
                interactKey = false;
                break;
            default:
                break;
        }
    }
    
}
