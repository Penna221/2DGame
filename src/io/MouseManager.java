package io;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import entities.ai.PlayerAI;
import entities.player.InventoryBag;
import states.GameState;
import states.State;
import ui.PauseMenu;
public class MouseManager implements MouseListener, MouseMotionListener, MouseWheelListener{
    public int mouseX, mouseY;
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        if(State.currentState==State.inventoryState){
            
        }
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
                // Rectangle r = new Rectangle(World.player.bounds);
                // AttackBox b = new AttackBox(World.player, 3,r,World.player);
                // World.player.giveMomentum(45, 50);

                // Point2D p1 = new Point((int)(World.player.x),(int)(World.player.y));
                // Point2D p2 = new Point((int)(mouseX +World.camera.getXOffset()),(int)(mouseY+World.camera.getYOffset()));
                // System.out.println(p1);
                // System.out.println(p2);
                // float rotation = World.getAngleBetweenPoints(p2,p1);

                // Random r = new Random();
                // int random = r.nextInt(4);
                // World.generateProjectile(random, rotation, p1,World.player);
                PlayerAI.doActionWithSelectedItem();
                // EntityManager.addAttackBox(b);

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
        if(State.currentState==State.inventoryState){
            InventoryBag.pickSlot();
        }
        
        // if(PauseMenu.currentContainer!=null){
        //     PauseMenu.currentContainer.sendPress();
        // }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        // UiHub.sendToggle(false);
        if(State.currentState==State.inventoryState){
            InventoryBag.releaseSlot();
        }
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

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(State.currentState==State.gameState){
            int notches = e.getWheelRotation();
            if (notches < 0) {
                PlayerAI.inv.scroll(-1);
            } else {
                PlayerAI.inv.scroll(1);
            }
        }
    }
    
}
