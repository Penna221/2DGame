package main;

import utils.pennanen.Engine;
import java.awt.image.BufferStrategy;

import states.State;

import java.awt.Graphics;
import java.awt.Color;
public class Game extends Engine{

    private int width, height;
    private String title;
    public static Window w;

    private BufferStrategy bs;
    private Graphics g;
    
    public Game(int width, int height, String title){
        this.width = width;
        this.height = height;
        this.title = title;
    }
    @Override
    public void init() {
        System.out.println("Creating Window");
        w = new Window(title,width,height);
        System.out.println("Creating States");
        State.createStates();
        State.setState(State.gameState);
    }

    @Override
    public void render() {
        bs = w.getCanvas().getBufferStrategy();
        if(bs == null){
            w.getCanvas().createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        g.clearRect(0, 0, w.getWidth(), w.getHeight());
        g.setColor(Color.black);
        g.fillRect(0, 0, w.getWidth(), w.getHeight());

        if(State.getState()!=null){
            State.getState().render(g);
        }
        g.dispose();
        bs.show();
    }

    @Override
    public void update() {
        
        if(State.getState()!=null){
            State.getState().update();
        }
    }
    
}
