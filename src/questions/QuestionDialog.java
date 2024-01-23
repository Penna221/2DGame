package questions;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

import main.Game;
import ui.ClickButton;
import ui.Container;
import ui.Text;
import ui.UIElement;

public class QuestionDialog {
    private Question q;
    private Container container;
    private Text qu;
    private ArrayList<ClickButton> answers;
    
    
    public QuestionDialog(Question q){
        this.q = q;
        generateUI();
    }
    private void generateUI(){
        answers = new ArrayList<ClickButton>();
        container = new Container(0, 0, Game.w.getWidth(), Game.w.getHeight());
        String t1 = q.question;
        String[] wrongs = q.wrongs;
        int l = wrongs.length;
        String correct = q.correct;
        qu = new Text(t1,0,0,255,false);
        container.addElement(qu);
        for(int i = 0; i < l; i++){
            ClickButton b = new ClickButton(0, 0, new Text(wrongs[i],0,0,100,false)){
                @Override
                public void task(){
                    wrongAnswer();
                }
            };
            answers.add(b);
        }
        ClickButton b2 = new ClickButton(0,0,new Text(correct,0,0,100,false)){
            @Override
            public void task(){
                correctAnswer();
            }
        };
        
        answers.add(b2);
        Collections.shuffle(answers);
        for(ClickButton e : answers){
            container.addElement(e);
        }
        container.centerElements();
        container.spaceOutVertically(10);

    }
    private void wrongAnswer(){

    }
    private void correctAnswer(){

    }
    public void update(){
        for(ClickButton e : answers){
            e.update();
        }
    }
    public void render(Graphics g){
        container.render(g);
    }
}
