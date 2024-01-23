package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import gfx.AssetStorage;
import questions.QuestionDialog;
import questions.QuestionStorage;
import ui.ClickButton;

public class QuestionState extends State{
    private ClickButton button1;
    private QuestionDialog d;
    private boolean alreadyInitialized = false;
    @Override
    public void update() {
        if(running){
            d.update();
            //button2.update();
        }else{
            State.transition.update();
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Serif",Font.BOLD,25));
        g.drawString("Question", 25, 50);
        button1.render(g);
        //button2.render(g);
        d.render(g);
        if(transition !=null){
            transition.render(g);
        }
    }

    @Override
    public void init() {
        button1 = new ClickButton(30, 100, AssetStorage.images.get("russulaPaludosa")){
            @Override
            public void task(){
                State.setState(State.menuState);
            }
        };
        d = new QuestionDialog(QuestionStorage.math_easy.get(0));
        if(!alreadyInitialized){
        }
    }

    @Override
    public void updateOnceBetweenTransitions() {
        
    }
    
}
