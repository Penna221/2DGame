package main;

import utils.pennanen.Engine;

public class Game extends Engine{

    private int width, height;
    private String title;
    public Game(int width, int height, String title){
        this.width = width;
        this.height = height;
        this.title = title;
    }
    @Override
    public void init() {
        System.out.println("INIT");
        
    }

    @Override
    public void render() {
        
    }

    @Override
    public void update() {
        
    }
    
}
