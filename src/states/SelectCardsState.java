package states;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import cards.Card;
import entities.ai.PlayerAI;
import gfx.AssetStorage;
import gfx.Transition;
import main.Game;
import save.SavedGame;
import ui.ClickButton;
import ui.Container;
import ui.PauseMenu;
import ui.Task;
import ui.Text;
import world.World;

public class SelectCardsState extends State{

    private Container container;
    private Container scroll;
    private Container infoContainer;
    private Text info;
    private ArrayList<ClickButton> choises;
    private int startIndex;
    private int cardsToShow = 5;
    public static int selectAmount = 3;
    private ArrayList<Card> selectedCards = new ArrayList<Card>();
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
        container.render(g);
        if(transition !=null){
            transition.render(g);
        }
    }

    @Override
    public void init() {
        choises = new ArrayList<ClickButton>();
        container = new Container(0,0,Game.w.getWidth(),Game.w.getHeight());
        Text title = new Text("Valitse Käytettävät kortit", 0, 0, 700, true,true);
        
        scroll = new Container(0, 170, Game.w.getWidth(), 450);
        scroll.fillBg = true;
        scroll.overrideColor = Color.blue;
        ClickButton leftButton = new ClickButton(0,0,AssetStorage.images.get("arrow_left"));
        ClickButton rightButton = new ClickButton(0,0,AssetStorage.images.get("arrow_right"));
        leftButton.setPosition(0,(int)(scroll.bounds.getY()-leftButton.bounds.getHeight()));
        rightButton.setPosition((int)(leftButton.bounds.getX()+rightButton.bounds.getWidth()),(int)(leftButton.bounds.getY()));
        for(Card c : PlayerAI.buffCards){
            ClickButton ca = generateCardButton(c);
            // scroll.addElement(ca);
            choises.add(ca);
        }
        for(Card c : PlayerAI.weaponCards){
            ClickButton ca = generateCardButton(c);
            // scroll.addElement(ca);
            choises.add(ca);
        }
        

        Task t1 = new Task(){
            public void perform(){
                System.out.println("left");
                if(startIndex>0){
                    startIndex--;
                }
                updateContent();
            }
        };
        leftButton.setTask(t1);
        Task t2 = new Task(){
            public void perform(){
                System.out.println("right");
                if(startIndex<choises.size()-cardsToShow){
                    startIndex++;
                }
                updateContent();
            }
        };
        rightButton.setTask(t2);
        container.addElement(leftButton);
        container.addElement(rightButton);
        
        ClickButton okButton = new ClickButton(0, 0,new Text("OK", 0,0,0,false,false));
        Task t3 = new Task(){
            public void perform(){
                //APPLY EFFECTS
                
                SavedGame.currentSave.inventory.applyCards(selectedCards);
                GameState.paused = true;
                World.load("dungeon","");
                State.setState(gameState, false);

            }
        };
        okButton.setTask(t3);
        okButton.setPosition((int)(rightButton.bounds.getWidth()+100),(int)(rightButton.bounds.getY()));
        container.addElement(scroll);
        container.addElement(okButton);
        container.addElement(title);
        
        infoContainer = new Container(0,(int)(scroll.bounds.getY()+scroll.bounds.getHeight()), Game.w.getWidth(),200);
        info = new Text("Valitut Kortit:", 0,0,(int)(infoContainer.bounds.getWidth()),false,true);
        updateContent();
        infoContainer.addElement(info);
        container.addElement(infoContainer);
        container.updateBounds();

        


        PauseMenu.setContainer(container);
    
        Transition.canContinue = true;

    }
    private void updateContent(){
        // scroll.elements.clear();
        for(int i = startIndex; i < startIndex + cardsToShow; i++){
            if(i >choises.size()){
                break;
            }
            ClickButton c = choises.get(i);
            scroll.addLate(c);
        }
        scroll.swap();
        // scroll.spaceHorizintally(25);
        scroll.centerAndSpaceHorizontally(20);
        scroll.centerVertically();
        

    }
    private void updateInfo(){
        String text = "Valitut Kortit:";
        for(int i = 0; i < selectedCards.size(); i++){
            text += " <"+selectedCards.get(i).name + ">";
        }
        info.updateText(text);
    }
    private ClickButton generateCardButton(Card c){
        ClickButton b = new ClickButton(0, 0, c.texture);
        b.setTask(new Task(){
            public void perform(){
                if(b.selected){
                    b.selected = false;
                    selectedCards.remove(c);
                }else{
                    if(selectedCards.size()<selectAmount){
                        selectedCards.add(c);
                        b.selected = true;
                    }
                }
                updateInfo();
            }
        });
        return b;
    }
    @Override
    public void updateOnceBetweenTransitions() {

    }
    
}
