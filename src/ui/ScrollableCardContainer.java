package ui;

import java.awt.Color;
import java.util.ArrayList;

import cards.Card;
import entities.ai.PlayerAI;
import entities.player.InfoPacket;
import gfx.AssetStorage;

public class ScrollableCardContainer{

    public ArrayList<Card> cards;
    public ArrayList<ClickButton> choises;
    public Container container;
    public Container cardContainer;
    public int startIndex;
    public int amount = 2;
    public int x, y,width, height;
    public ScrollableCardContainer(int x, int y,int w, int h, ArrayList<Card> cards) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.cards = cards;
        if(cards.size()<5){
            amount = cards.size();
        }
        generate();
    }
    public void generate(){
        container = new Container(x,y,width,height);
        container.fillBg = true;
        container.overrideColor = Color.green;
        cardContainer = new Container(0,0,(int)(width*0.85),height);
        cardContainer.fillBg = true;
        cardContainer.overrideColor = Color.pink;

        choises = generateChoises();
        updateContent();

        Task t1 = new Task(){
            public void perform(){
                System.out.println("left");
                if(startIndex>0){
                    startIndex--;
                }
                updateContent();
            }
        };
        Task t2 = new Task(){
            public void perform(){
                System.out.println("right");
                if(startIndex<cards.size()-amount){
                    startIndex++;
                }
                updateContent();
            }
        };
        ClickButton leftButton = new ClickButton(0,0,AssetStorage.images.get("arrow_left"));
        ClickButton rightButton = new ClickButton(0,0,AssetStorage.images.get("arrow_right"));
        leftButton.setTask(t1);
        rightButton.setTask(t2);
        container.addElement(leftButton);
        container.addElement(cardContainer);
        container.addElement(rightButton);
        container.centerAndSpaceHorizontally(20);
        container.centerVertically();
        // updateContent();
    }
    private ArrayList<ClickButton> generateChoises(){
        ArrayList<ClickButton> buttons = new ArrayList<ClickButton>();
        for(int i = 0; i < cards.size();i++){
            Card c = cards.get(i);
            ClickButton b = new ClickButton(0, 0, c.texture);
            b.card = c;
            int owned = calculateOwned(b.card);
            String[] info = new String[c.info.length+2];
            info[0] = "Required level: " + c.levelReq;
            info[1] = "You have: "+owned;
            for(int j = 0; j < c.info.length; j++){
                info[j+2] = c.info[j];
            }

            InfoPacket packet = new InfoPacket(info, 0, 0);
            b.setInfoPacket(packet);
            buttons.add(b);
        }
        return buttons;
    }
    private int calculateOwned(Card c){
        int amount = 0;
        if(c.type.equals("Weapon")){
            for(Card card : PlayerAI.weaponCards){
                if(card.id == c.id){
                    amount ++;
                }
            }
        }else if(c.type.equals("Buff")){
            for(Card card : PlayerAI.buffCards){
                if(card.id == c.id){
                    amount ++;
                }
            }
        }else {
            for(Card card : PlayerAI.abilityCards){
                if(card.id == c.id){
                    amount ++;
                }
            }
        }
        return amount;
    }
    public void updateContent(){
        cardContainer.changing = true;
        cardContainer.elements.clear();
        for(int i = startIndex; i < startIndex + amount; i++){
            if(i >cards.size()){
                break;
            }
            ClickButton c = choises.get(i);
            cardContainer.addElement(c);
        }
        cardContainer.centerAndSpaceHorizontally(20);
        cardContainer.centerVertically();
        container.setPosition(container.x, container.y);
    }
    public Container getContainer(){
        return container;
    }
}
