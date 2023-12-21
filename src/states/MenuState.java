package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main.Game;
import main.Launcher;
import ui.ClickButton;
import ui.Container;
import ui.Text;

public class MenuState extends State{
    private boolean alreadyInitialized = false;
    
    private ClickButton newGameButton, loadGameButton,exitButton;
    
    private Container container;
    private Text title;
    @Override
    public void update() {
        if(running){
            newGameButton.update();
            loadGameButton.update();
            exitButton.update();
        }else{
            State.transition.update();
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Serif",Font.BOLD,25));
        g.drawString("MENU", 25, 50);
        container.render(g);
        if(transition !=null){
            transition.render(g);
        }
    }

    @Override
    public void init() {
        container = new Container(0, 0, Game.w.getWidth(),Game.w.getHeight());
        title = new Text("Game Title", 0, 50, 0, true);

        newGameButton = new ClickButton(100, 200, new Text("New Game",0,0,100,false)){
            @Override
            public void task(){
                //Not implemented new game
                State.setState(State.gameState);
            }
        };
        loadGameButton = new ClickButton(100, 320,new Text("Load Game",0,0,100,false)){
            @Override
            public void task(){
                //Not implemented load game
                State.setState(State.gameState);
            }
        };
        exitButton = new ClickButton(100, 440, new Text("Exit",0,0,100,false)){
            @Override
            public void task(){
                int x = Launcher.g.stop();
                System.exit(x);
            }
        };
        container.addElement(title);
        container.addElement(newGameButton);
        container.addElement(loadGameButton);
        container.addElement(exitButton);
        container.centerElements();
        if(!alreadyInitialized){
            alreadyInitialized = true;
        }
    }

    @Override
    public void updateOnceBetweenTransitions() {
        
    }
    
}
