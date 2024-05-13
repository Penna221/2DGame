package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import gfx.AssetStorage;
import ui.ClickButton;
import world.World;

public class GameState extends State{
    public World world;
    private ClickButton button1;
    //private ToggleButton button2;
    private boolean alreadyInitialized = false;
    @Override
    public void update() {
        if(running){
            world.update();
            button1.update();
            //button2.update();
        }else{
            State.transition.update();
        }
    }

    @Override
    public void render(Graphics g) {
        world.render(g);
        g.setColor(Color.white);
        g.setFont(new Font("Serif",Font.BOLD,25));
        g.drawString("Game", 25, 50);
        button1.render(g);
        //button2.render(g);
        if(transition !=null){
            transition.render(g);
        }
    }

    @Override
    public void init() {
        button1 = new ClickButton(100, 100, AssetStorage.images.get("russulaPaludosa")){
            @Override
            public void task(){
                State.setState(State.menuState);
            }
        };
        /*button2 = new ToggleButton(200, 100, AssetStorage.images.get("arrow_up")){
            @Override
            public void task(){
                System.out.println("press 1 ");
            }
        };*/
        if(!alreadyInitialized){
            // world = new World(World.FOREST);
            world = new World();
            alreadyInitialized = true;
        }
    }

    @Override
    public void updateOnceBetweenTransitions() {
        world.update();
    }
    
}
