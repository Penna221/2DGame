package states;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import cards.Card;
import entities.ai.PlayerAI;
import gfx.Transition;
import main.Game;
import ui.ClickButton;
import ui.Container;
import ui.PauseMenu;
import ui.ScrollableCardContainer;
import ui.Task;
import ui.Text;
import world.World;
public class CardTraderState extends State{
    private Text questPoints;
    private Container container;
    private Text title;
    private ClickButton selectButton,returnButton;
    private Card chosenOne = null;
    private boolean changing = false;
    private Container scroll;
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
        container = new Container(0,0,Game.w.getWidth(),Game.w.getHeight());
        
        ArrayList<Card> allCards = getRandomSelection(5);

        ScrollableCardContainer scrollObj = new ScrollableCardContainer(0,0,(int)(container.bounds.getWidth()-200),(int)(250), allCards);
        scroll = scrollObj.getContainer();
        scroll.setPosition((int)(container.bounds.getWidth()/2- scroll.bounds.getWidth()/2), 200);
        
        for(ClickButton b : scrollObj.choises){
            Task t = new Task(){
                public void perform(){
                    
                    for(ClickButton cb : scrollObj.choises){
                        cb.selected = false;
                    }
                    if(World.playerLevel >= b.card.levelReq){
                        b.selected = true;
                        System.out.println("picked " +b.card.name);
                        chosenOne = b.card;
                    }
                }
            };
            b.setTask(t);
        }



        selectButton = new ClickButton(0, 0, new Text("Select", 0, 0, 0,false,false));
        selectButton.setPosition((int)(scroll.bounds.getX() + scroll.bounds.getWidth()/2 - selectButton.bounds.getWidth()/2),(int)(scroll.bounds.getY() + scroll.bounds.getHeight()));
        
        
        selectButton.setTask(new Task(){
            @Override
            public void perform(){
                confirmChoise();
            }
        });
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
        container.addElement(scroll);
        container.updateBounds();
        PauseMenu.setContainer(container);
        changing = false;
    }
    private ArrayList<Card> getRandomSelection(int amount){
        ArrayList<Card> cards = new ArrayList<Card>();
        Random r = new Random();

        int wl = Card.weapon_cards.size();
        int bl = Card.buff_cards.size();
        
        
        cards.add(Card.weapon_cards.get(r.nextInt(wl)));



        for(int i = 0; i < amount-1; i++){
            int id = r.nextInt(bl);
            Card c = Card.buff_cards.get(id);
            if(!cards.contains(c)){
                cards.add(c);
            }
        }

        return cards;
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
    
    @Override
    public void updateOnceBetweenTransitions() {}

    
}
