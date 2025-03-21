package states;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import cards.Card;
import entities.ai.PlayerAI;
import entities.player.InfoPacket;
import gfx.Transition;
import main.Game;
import ui.ClickButton;
import ui.Container;
import ui.PauseMenu;
import ui.Task;
import ui.Text;
public class CardTraderState extends State{
    private ClickButton b1;
    private Text questPoints;
    private Container container;
    private Text title;
    private ClickButton selectButton,returnButton;
    private Card chosenOne = null;
    private Container infoTextContainer;
    private boolean changing = false;
    private ArrayList<ClickButton> choises = new ArrayList<ClickButton>();

    @Override
    public void update() {
        if(running){
            
            Container.focus = false;
            container.update();
        }else{
            State.transition.update();
        }
    }

    @Override
    public void render(Graphics g) {
        if(!changing){
            container.render(g);
        }

        if(transition !=null){
            transition.render(g);
        }
    }

    @Override
    public void init() {
        int cardY = 200;
        int wl = Card.weapon_cards.size();
        int bl = Card.buff_cards.size();
        Random r = new Random();
        container = new Container(0,0,Game.w.getWidth(),Game.w.getHeight());
        //Only one weapon card.
        b1 = generateCardButton(Card.weapon_cards.get(r.nextInt(wl)));
        b1.setPosition(100, cardY);
        choises.add(b1);
        container.addElement(b1);

        //Multiple buff choises.
        for(int i = 0; i < 2; i++){
            ClickButton b3 = generateCardButton(Card.buff_cards.get(r.nextInt(bl)));
            choises.add(b3);
            b3.setPosition(100, cardY);
            container.addElement(b3);
        }
        container.spaceHorizintally(10);
        
        int infoYStart = 500;
        infoTextContainer = new Container(0,infoYStart, container.bounds.width, Game.w.getHeight()-infoYStart);
        infoTextContainer.fillBg = true;
        infoTextContainer.overrideColor = Color.blue;
        selectButton = new ClickButton(0, infoTextContainer.bounds.y, new Text("Select", 0, 0, 0,false,false));
        selectButton.setTask(new Task(){
            @Override
            public void perform(){
                confirmChoise();
            }
        });
        selectButton.setPosition(infoTextContainer.bounds.width-selectButton.bounds.width, infoTextContainer.bounds.y-selectButton.bounds.height);
        container.addElement(infoTextContainer);
        container.addElement(selectButton);
        
        returnButton = new ClickButton(0,0,new Text("Return",0,0,0,false,false));
        returnButton.setTask(new Task(){
            @Override
            public void perform(){
                GameState.paused = false;
                State.setState(State.gameState, true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        });
        returnButton.setPosition(selectButton.bounds.x - returnButton.bounds.width-10, selectButton.bounds.y);
        container.addElement(returnButton);
        
        title = new Text("Pick a card", 0, 0, 0, true,false);
        container.addElement(title);
        questPoints = new Text("Available Quest points: " + PlayerAI.questPoints, title.bounds.width+20,0,0,false,false);
        container.addElement(questPoints);
        PauseMenu.setContainer(container);
        changing = false;
    }
    private void confirmChoise(){
        if(chosenOne==null){
            return;
        }
        System.out.println("You chose: " + chosenOne.name);
        if(PlayerAI.questPoints>0){
            if(chosenOne.type.equals("Weapon")){
                PlayerAI.weaponCards.add(chosenOne);
            }else{
                PlayerAI.buffCards.add(chosenOne);
            }
            PlayerAI.questPoints--;
            changing = true;
            State.setState(this, false);
            
        }else{
            System.out.println("Not enough Quest Points");
        }
        chosenOne = null;
        
    }
    private void updateText(){
        
        for(String s : chosenOne.info){
            Text t = new Text(s, 0, 0, infoTextContainer.bounds.width, false,false);

            infoTextContainer.addLate(t);
        }
        infoTextContainer.swap();
    }
    @Override
    public void updateOnceBetweenTransitions() {}

    private ClickButton generateCardButton(Card c){
        ClickButton b = new ClickButton(0, 0, c.texture);
        b.setTask(new Task(){
            public void perform(){
                for(ClickButton cb : choises){
                    cb.selected = false;
                }
                b.selected = true;
                System.out.println("picked " +c.name);
                chosenOne = c;
                updateText();
            }
        });
        InfoPacket info = new InfoPacket(c.info, 0, 0);
        b.setInfoPacket(info);
        return b;
    }
    
}
