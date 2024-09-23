package io;
import java.awt.event.MouseMotionListener;

import entities.AttackBox;
import entities.EntityManager;
import states.GameState;
import states.State;
import ui.PauseMenu;
import world.World;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
public class MouseManager implements MouseListener, MouseMotionListener{
    public int mouseX, mouseY;
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        if(State.getState()==State.gameState){
            if(!GameState.paused){
                Rectangle r = new Rectangle(World.player.bounds);
                AttackBox b = new AttackBox(null, 1,r);
                EntityManager.addAttackBox(b);

            }
        }
        if(PauseMenu.currentContainer!=null){
            PauseMenu.currentContainer.sendClick();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        // if(PauseMenu.currentContainer!=null){
        //     PauseMenu.currentContainer.sendPress();
        // }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        // UiHub.sendToggle(false);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
    
}
