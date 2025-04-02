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
    private Container swapContainer;
    private Container infoContainer;
    private Text info,title;
    public static int abilityAmount = 2;
    public static int buffAmount = 2;
    private ArrayList<Card> selectedCards;
    private ArrayList<Card> selectedBuffs;
    private ArrayList<Card> selectedAbilities;
    private Card selectedWeapon;
    public int selectedScroll = 0;
    public int availableMana = 0;
    public ScrollableCardContainer[] scrollSelect;
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
        availableMana = PlayerAI.mana;
        container = new Container(0,0,Game.w.getWidth(),Game.w.getHeight());
        title = new Text("Choose your Weapon", 0, 0, 700, true,true);
        selectedCards = new ArrayList<Card>();
        selectedAbilities = new ArrayList<Card>();
        selectedBuffs = new ArrayList<Card>();
        // ArrayList<Card> allCards = new ArrayList<Card>();
        // allCards.addAll(PlayerAI.weaponCards);
        // allCards.addAll(PlayerAI.buffCards);
        // allCards.addAll(PlayerAI.abilityCards);

        selectedScroll = 0;
        swapContainer = new Container(0,0,(int)(container.bounds.getWidth()),(int)(250));

        ScrollableCardContainer scrollObj = new ScrollableCardContainer(0,0,(int)(swapContainer.bounds.getWidth()),(int)(swapContainer.bounds.getHeight()), PlayerAI.weaponCards);
        ScrollableCardContainer scrollObj2 = new ScrollableCardContainer(0,0,(int)(swapContainer.bounds.getWidth()),(int)(swapContainer.bounds.getHeight()), PlayerAI.abilityCards);
        ScrollableCardContainer scrollObj3 = new ScrollableCardContainer(0,0,(int)(swapContainer.bounds.getWidth()),(int)(swapContainer.bounds.getHeight()), PlayerAI.buffCards);
        setTasks(scrollObj,scrollObj2,scrollObj3);
        swapContainer.addElement(scrollObj.container);
        scrollSelect = new ScrollableCardContainer[3];
        scrollSelect[0] = scrollObj;
        scrollSelect[1] = scrollObj2;
        scrollSelect[2] = scrollObj3;
        swapContainer.setPosition((int)(container.bounds.getWidth()/2- swapContainer.bounds.getWidth()/2), 200);
        scrollObj.updateContent();
        ClickButton nextButton = new ClickButton(0, 0,new Text("Next", 0,0,0,false,false));
        Task t3 = new Task(){
            public void perform(){
                //APPLY EFFECTS
                
                
                selectedScroll++;
                updateCardScroller();
            }
        };
        nextButton.setTask(t3);
        nextButton.setPosition((int)(swapContainer.bounds.getX() + swapContainer.bounds.getWidth() - nextButton.bounds.getWidth()),(int)(swapContainer.bounds.getY() + swapContainer.bounds.getHeight()));
        container.addElement(nextButton);

        ClickButton previousButton = new ClickButton(0, 0,new Text("Previous", 0,0,0,false,false));
        previousButton.setPosition((int)(swapContainer.bounds.getX()),(int)(swapContainer.bounds.getY() + swapContainer.bounds.getHeight()));
        
        Task t4 = new Task(){
            public void perform(){
                selectedScroll--;
                updateCardScroller();
            }
        };
        previousButton.setTask(t4);
        container.addElement(previousButton);
        container.addElement(title);
        
        ClickButton okButton = new ClickButton(0, 0,new Text("OK", 0,0,0,false,false));
        okButton.setPosition((int)(swapContainer.bounds.getX() + swapContainer.bounds.getWidth()/2 - okButton.bounds.getWidth()/2),(int)(swapContainer.bounds.getY() + swapContainer.bounds.getHeight()));
        Task t5 = new Task(){
            public void perform(){
                if(selectedWeapon!=null){
                    selectedCards.add(selectedWeapon);
                }
            selectedCards.addAll(selectedAbilities);
            selectedCards.addAll(selectedBuffs);


            PlayerAI.xp = 0;
            SavedGame.currentSave.inventory.applyCards(selectedCards);
            GameState.paused = true;
            World.load("dungeon","");
            State.setState(gameState, false);
            return;
            }
        };
        okButton.setTask(t5);
        
        container.addElement(okButton);

        infoContainer = new Container(0,(int)(nextButton.bounds.getY()+nextButton.bounds.getHeight()+50), Game.w.getWidth(),200);
        info = new Text("Available points:" + availableMana, 0,0,(int)(infoContainer.bounds.getWidth()),false,true);
        // updateContent();
        infoContainer.addElement(info);
        container.addElement(infoContainer);
        
        container.addElement(swapContainer);
        container.updateBounds();
        PauseMenu.setContainer(container);
    
        Transition.canContinue = true;

    }
    private void updateCardScroller(){
        if(selectedScroll<0){
            selectedScroll = 0;
            return;
        }
        if(selectedScroll>=scrollSelect.length){
            selectedScroll= scrollSelect.length-1;
            return;
        }
        if(selectedScroll==0){
            title.easeIn("Choose your Weapon");
        }else if(selectedScroll==1){
            title.easeIn("Choose your Abilities");
        }else if(selectedScroll==2){
            title.easeIn("Choose your Buffs");
        }
        System.out.println("selectedScroll" + selectedScroll);
        ScrollableCardContainer cc = scrollSelect[selectedScroll];
        swapContainer.changing = true;
        swapContainer.elements.clear();
        swapContainer.addElement(cc.container);
        cc.updateContent();
        
        container.setPosition(container.x,container.y);
    }
    private void updateInfo(){
        int amt = 0;
        if(selectedWeapon!=null){
            amt += selectedWeapon.cost;
        }
        for(Card c : selectedAbilities){
            amt += c.cost;
        }
        for(Card c : selectedBuffs){
            amt += c.cost;
        }
        availableMana = PlayerAI.mana - amt;
        String text = "Available Mana: "+ availableMana;

        info.updateText(text);
    }
    private void setTasks(ScrollableCardContainer scrollObj,ScrollableCardContainer scrollObj2,ScrollableCardContainer scrollObj3){
        //Weapons
        for(ClickButton b : scrollObj.choises){
            Task t = new Task(){
                public void perform(){
                    
                    for(ClickButton bb : scrollObj.choises){
                        bb.selected = false;
                    }
                    if(b.card.equals(selectedWeapon)){
                        b.selected = false;
                        selectedWeapon = null;
                    }else{
                        if(World.playerLevel >= b.card.levelReq&&(availableMana -b.card.cost)>=0){
                            
                            selectedWeapon = b.card;
                            b.selected = true;   
                            
                        }
                    }
                    
                    updateInfo();
                }
            };
            b.setTask(t);
        }
        //Abilities
        for(ClickButton b : scrollObj2.choises){
            Task t = new Task(){
                public void perform(){
                    if(b.selected){
                        b.selected = false;
                        selectedAbilities.remove(b.card);
                    }else{
                        if(selectedAbilities.size()<abilityAmount){

                            if(World.playerLevel >= b.card.levelReq&&(availableMana -b.card.cost)>=0){
                                b.selected = true;   
                                selectedAbilities.add(b.card);
                            }
                        }
                    }
                    updateInfo();
                }
            };
            b.setTask(t);
        }
        //Buffs
        for(ClickButton b : scrollObj3.choises){
            Task t = new Task(){
                public void perform(){
                    if(b.selected){
                        b.selected = false;
                        selectedBuffs.remove(b.card);
                    }else{
                        if(selectedBuffs.size()<buffAmount){

                            if(World.playerLevel >= b.card.levelReq&&(availableMana -b.card.cost)>=0){
                                b.selected = true;   
                                selectedBuffs.add(b.card);
                            }
                        }
                    }
                    updateInfo();
                }
            };
            b.setTask(t);
        }

    }
    @Override
    public void updateOnceBetweenTransitions() {

    }
    
}
