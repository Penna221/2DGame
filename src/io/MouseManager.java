package io;
import java.awt.event.MouseMotionListener;

import ui.UiHub;

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
        System.out.println("sending click messsage");
        UiHub.sendClick();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        UiHub.sendToggle(true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        UiHub.sendToggle(false);
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
