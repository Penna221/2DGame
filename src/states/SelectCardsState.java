package states;

import java.awt.Graphics;
import java.util.ArrayList;

import cards.Card;
import entities.ai.PlayerAI;
import gfx.Transition;
import main.Game;
import save.SavedGame;
import ui.ClickButton;
import ui.Container;
import ui.PauseMenu;
import ui.ScrollableCardContainer;
import ui.Task;
import ui.Text;
import world.World;

public class SelectCardsState extends State{

    private Container container;
    private Container scroll;
    private Container infoContainer;
    private Text info;
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
        container = new Container(0,0,Game.w.getWidth(),Game.w.getHeight());
        Text title = new Text("Valitse Käytettävät kortit", 0, 0, 700, true,true);
        
        ArrayList<Card> allCards = new ArrayList<Card>();
        allCards.addAll(PlayerAI.weaponCards);
        allCards.addAll(PlayerAI.buffCards);
        ScrollableCardContainer scrollObj = new ScrollableCardContainer(0,0,(int)(container.bounds.getWidth()-200),(int)(250), allCards);
        scroll = scrollObj.getContainer();
        for(ClickButton b : scrollObj.choises){
            Task t = new Task(){
                public void perform(){
                    if(b.selected){
                        b.selected = false;
                        selectedCards.remove(b.card);
                    }else{
                        if(selectedCards.size()<selectAmount){
                            selectedCards.add(b.card);
                            b.selected = true;
                        }
                    }
                    updateInfo();
                }
            };
            b.setTask(t);
        }
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
        scroll.setPosition((int)(container.bounds.getWidth()/2- scroll.bounds.getWidth()/2), 200);
        okButton.setTask(t3);
        okButton.setPosition((int)(scroll.bounds.getX() + scroll.bounds.getWidth()/2 - okButton.bounds.getWidth()/2),(int)(scroll.bounds.getY() + scroll.bounds.getHeight()));
        container.addElement(okButton);
        container.addElement(title);
        
        infoContainer = new Container(0,(int)(okButton.bounds.getY()+okButton.bounds.getHeight()+50), Game.w.getWidth(),200);
        info = new Text("Valitut Kortit:", 0,0,(int)(infoContainer.bounds.getWidth()),false,true);
        // updateContent();
        infoContainer.addElement(info);
        container.addElement(infoContainer);
        container.addElement(scroll);
        container.updateBounds();
        PauseMenu.setContainer(container);
    
        Transition.canContinue = true;

    }
    private void updateInfo(){
        String text = "Valitut Kortit:";
        for(int i = 0; i < selectedCards.size(); i++){
            text += " <"+selectedCards.get(i).name + ">";
        }
        info.updateText(text);
    }
    
    @Override
    public void updateOnceBetweenTransitions() {

    }
    
}
