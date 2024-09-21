package ui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import entities.EntityInfo;
import entities.EntityManager;
import gfx.AssetStorage;
import gfx.Transition;
import loot.Market;
import loot.MarketItem;
import main.Game;
import states.GameState;
import states.State;
import world.World;

public class PauseMenu {
    
    public static Container currentContainer;
    public static HashMap<String,Container> containers = new HashMap<String,Container>();
    public PauseMenu(){}

    public static void loadPauseMenus() throws Exception{
        containers.put("basic", createBasicPauseMenu());
        containers.put("main", createMenuStateContainer());
        // containers.put("blacksmith",createBlacksmithContainer());
        // containers.put("witch",createWitchContainer());
        // containers.put("trader",createTraderContainer());


        // containers.put("enter_blacksmith", createBlacksmithEntryDialog());
        // containers.put("enter_witch", createWitchEntryDialog());
        // containers.put("enter_trader", createTraderEntryDialog());
        
    }
    public static void generateNewContainer(String text){
        switch (text) {
            case "basic":
                containers.put("basic", createBasicPauseMenu());
            break;
            case "main":
                containers.put("main", createMenuStateContainer()); 
            break;
            case "blacksmith":
                containers.put("blacksmith",createBlacksmithContainer());
            break;
            case "witch":
                containers.put("witch",createWitchContainer());
            break;
            case "trader":
                containers.put("trader",createTraderContainer());
            break;
            case "enter_witch":
                containers.put("enter_witch", createWitchEntryDialog());
            break;
            case "enter_trader":
                containers.put("enter_trader", createTraderEntryDialog());
            break;
            case "enter_blacksmith":
                containers.put("enter_blacksmith", createBlacksmithEntryDialog());
            break;
            default:
                break;
        }
    }
    private static Container createMenuStateContainer(){
        Container container = new Container(0, 0, Game.w.getWidth(),Game.w.getHeight());
        Text title = new Text("Game Title", 0, 50, 0, true);
        Task t = new Task(){
            public void perform(){
                World.ready = false;
                World.load("lobby");
                State.setState(State.gameState,false);
            }
        };
        ClickButton newGameButton = new ClickButton(100, 200, new Text("New Game",0,0,0,false));
        newGameButton.setTask(t);
        ClickButton loadGameButton = new ClickButton(100, 320,new Text("Load Game",0,0,0,false));
        Task t2 = new Task(){public void perform(){System.exit(0);}};
        ClickButton exitButton = new ClickButton(100, 440, new Text("Exit",0,0,0,false));
        exitButton.setTask(t2);
        int bottomRow = Game.w.getHeight()-100;
        BufferedImage gear = AssetStorage.images.get("gear");
        Task t3 = new Task(){
            public void perform(){
                State.setState(State.settingsState,true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
        ClickButton settingsButton = new ClickButton(60, bottomRow-gear.getHeight()/2, gear);
        settingsButton.setTask(t3);
        container.setHeader(title);
        container.addElement(newGameButton);
        container.addElement(loadGameButton);
        container.addElement(exitButton);
        container.centerElements();
        container.spaceOutVertically(35);

        container.addElement(settingsButton);
        return container;
    }


    private static Container createBasicPauseMenu(){
        int w = 600;
        int h = 700;
        int x = Game.w.getWidth() /2 - w/2;
        int y = Game.w.getHeight() /2 - h/2;
        Container c = new Container(x,y,w,h);
        Task t1 = new Task(){
            public void perform(){
                PauseMenu.setContainer(null);
                GameState.paused = false;
            }
        };
        ClickButton returnButton = new ClickButton(0,250,new Text("Return to game",w/2,0,0,false));
        returnButton.setTask(t1);
        ClickButton settingsButton = new ClickButton(0,250,new Text("Settings",w/2,0,0,false));
        
        
        Task t2 = new Task(){
            public void perform(){
                GameState.paused = false;
                State.setState(State.menuState,true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
        ClickButton exitButton = new ClickButton(0,250,new Text("Save and exit",w/2,0,0,false));
        exitButton.setTask(t2);
        c.addElement(returnButton);
        c.addElement(settingsButton);
        c.addElement(exitButton);
        c.centerElements();
        c.spaceOutVertically(35);
        return c;
    }
    private static Container createYesNoDialog(String question, Task t1,Task t2){
        ArrayList<ClickButton> options = new ArrayList<ClickButton>();
        ClickButton b1 = new ClickButton(0, 0, new Text("Yes",0,0,100,false));
        b1.setTask(t1);
        ClickButton b2 = new ClickButton(0, 0, new Text("No",0,0,100,false));
        b2.setTask(t2);
        options.add(b1);
        options.add(b2);

        Container c = createBasicDialog(question, options);
        return c;
    }
    private static Container createBasicDialog(String header,ArrayList<ClickButton> options){
        int w = Game.w.getWidth();
        int h = 500;
        int x = 0;
        int y = Game.w.getHeight()-h;
        Container c = new Container(x,y,w,h);
        Text title = new Text(header, 20, y, w, true);
        int lastY = title.y + title.bounds.height + 20;
        c.addElement(title);
        for(int i = 0; i < options.size(); i++){
            ClickButton ca = options.get(i);
            ca.setPosition(60, lastY);
            lastY += ca.bounds.height + 10;
            c.addElement(ca);
            
        }

        return c;
    }
    private static Container createEntryDialog(Task t1, String question, Image header){
        Task t2 = new Task(){
            public void perform(){
                PauseMenu.setContainer(null);
                GameState.paused = false;
            }
        };
        Container c = createYesNoDialog(question, t1, t2);
        c.fillBg = true;
        c.setHeader(header);
        return c;

    }
    private static Container createBlacksmithEntryDialog(){
        Task t1 = new Task(){
            public void perform(){
                State.setState(State.blacksmithState, true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
        Image i = new Image(EntityManager.entityInfos.get(19).texture, 0, 0);
        return createEntryDialog(t1,"Open Blacksmith shop?", i);
    }
    private static Container createWitchEntryDialog(){
        Task t1 = new Task(){
            public void perform(){
                State.setState(State.witchState, true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
        Image i = new Image(EntityManager.entityInfos.get(25).texture, 0, 0);
        return createEntryDialog(t1,"Open Witch shop?", i);
    }
    private static Container createTraderEntryDialog(){
        Task t1 = new Task(){
            public void perform(){
                State.setState(State.traderState, true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
        Image i = new Image(EntityManager.entityInfos.get(25).texture, 0, 0);
        return createEntryDialog(t1,"Would you like to buy something?", i);
    }
    public static Container createTunnelEntryDialog(Task t){
        Image i = new Image(EntityManager.entityInfos.get(11).texture, 0, 0);
        return createEntryDialog(t,"Go through?", i);
    }
    private static Container createBlacksmithContainer(){
        Container c = new Container(0,0,Game.w.getWidth(),Game.w.getHeight());
        Task returnTask = new Task(){
            public void perform(){
                State.setState(State.gameState, true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
        ClickButton returnButton = new ClickButton(0,0,new Text("Return", 0, 0, 0, false));
        returnButton.setTask(returnTask);
        c.addElement(returnButton);

        Container list =  new Container(0, 0, 1000,400);
        list.fillBg = true;
        int listX = (int) (Game.w.getWidth()/2 - list.bounds.getWidth()/2);
        int listY = (int) (Game.w.getHeight()/2 - list.bounds.getHeight()/2);
        list.setPosition(listX,listY);
        
        ArrayList<Market> market = Market.lists.get("blacksmith");


        int start = list.start;
        start--;
        if(start < 0){
            start =0;
        }
        list.start = start;
        int end = start +3;
        if(end > market.size()){
            end = market.size();
        }
        addToListWithRange(list,market,start,end);  
        list.update();


        c.addElement(list);
        c.updateBounds();
        Task t1 = new Task(){
            public void perform(){
                if(list.changing){
                    return;
                }
                int start = list.start;
                start--;
                if(start < 0){
                    start =0;
                }
                list.start = start;
                int end = start +3;
                if(end > market.size()){
                    end = market.size();
                }
                addToListWithRange(list,market, start,end);
                c.updateBounds();
            }
        };
        Task t2 = new Task(){
            public void perform(){
                if(list.changing){
                    return;
                }
                int start = list.start;
                start++;
                
                int max = market.size()-3;
                if(max<=1){
                    max = market.size();
                    start = 0;
                }
                if(start > max){
                    start = max;
                }
                list.start = start;
                int end = start +3;
                if(end > market.size()){
                    end = market.size();
                }
                addToListWithRange(list,market, start,end);
                c.updateBounds();

            }
        };
        ClickButton upButton = new ClickButton((int)(list.x+list.bounds.getWidth()), list.y, AssetStorage.images.get("arrow_up"));
        ClickButton downButton = new ClickButton((int)(list.x+list.bounds.getWidth()), (int)(list.y + list.bounds.getHeight()), AssetStorage.images.get("arrow_down"));
        upButton.setTask(t1);
        downButton.setPosition(downButton.x, (int)(list.y + list.bounds.getHeight()-downButton.bounds.height));
        downButton.setTask(t2);
        c.addElement(upButton);
        c.addElement(downButton);

        // list.updateList();
        
        return c;
    }
    private static void addToListWithRange(Container c, ArrayList<Market> list, int start, int end){

        c.change = new ArrayList<UIElement>();

        for(int i = start; i < end; i++){
            Market market = list.get(i);
            Container listItem = generateListItem(c.bounds.width,market);
            // listItem.setOffset(c.x,c.y);
            listItem.centerVertically();
            c.addLate(listItem);
        }
        c.swap();
        c.updateList();
    }
    private static Container generateListItem(int maxWidth,Market m){
        Container c = new Container(0,0,maxWidth,120);

        EntityInfo e1 = EntityManager.entityInfos.get(m.sellItem.id);
        int amount = m.sellItem.amount;
        

        Task t = new Task(){
            public void perform(){
                //Buy
                System.out.println("Buy " + e1.name);
            }
        };
        int lockWidth = maxWidth/3*2;
        Container leftSide = new Container(0,0,lockWidth,c.bounds.height);
        leftSide.fillBg = true;
        leftSide.overrideColor = Color.blue;
        //Create LeftSide
        
        //GENERATE IMAGE BOX
        
        Image icon = generateImageIcon(e1.texture,amount,150,150);
        


        leftSide.addElement(icon);
        int maxSizeForText1 = (int)(leftSide.bounds.getWidth()-icon.bounds.getWidth());
        Text itemText = new Text(e1.name,0,0,maxSizeForText1,false);
        // Text itemText2 = new Text(e1.name,0,0,500,false);
        leftSide.addElement(itemText);
        leftSide.spaceHorizintally(20);
        leftSide.calculateBounds();
        leftSide.bounds.setSize(lockWidth,leftSide.bounds.height);
        leftSide.centerVertically();
        c.addElement(leftSide);
        //RIGHT SIDE
        Container rightSide = new Container(0,0,(int)(c.bounds.getWidth()-leftSide.bounds.getWidth()),100);
        rightSide.fillBg = true;
        rightSide.overrideColor = Color.RED;
        for(MarketItem mi : m.priceItems){
            EntityInfo e2 = EntityManager.entityInfos.get(mi.id);
            int amount2 = mi.amount;
            Image icon1 = generateImageIcon(e2.texture,amount2,55,55);
            rightSide.addElement(icon1);
        }
        rightSide.spaceHorizintally(10);
        // rightSide.addElement(icon2);
        c.addElement(rightSide);
        


        c.spaceHorizintally(10);
        c.calculateBounds();
        
        c.fillBg = true;
        c.overrideColor = Color.GREEN;
        return c;
    }
    
    private static Image generateImageIcon(BufferedImage i, int amount, int w, int h){
        BufferedImage iconA = UIFactory.generateIconWithAmount(i,amount,w,h);
        return new Image(iconA, 0,0);

    }
    private static Container createWitchContainer(){
        Container c = new Container(0,0,Game.w.getWidth(),Game.w.getHeight());
        Task returnTask = new Task(){
            public void perform(){
                State.setState(State.gameState, true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
        ClickButton returnButton = new ClickButton(0,0,new Text("Return", 0, 0, 0, false));
        returnButton.setTask(returnTask);
        c.addElement(returnButton);

        


        return c;
    }
    private static Container createTraderContainer(){
        Container c = new Container(0,0,Game.w.getWidth(),Game.w.getHeight());
        Task returnTask = new Task(){
            public void perform(){
                State.setState(State.gameState, true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
        ClickButton returnButton = new ClickButton(0,0,new Text("Return", 0, 0, 0, false));
        returnButton.setTask(returnTask);
        c.addElement(returnButton);
        return c;
    }
    public static void setContainer(Container cc){
        currentContainer = cc;
    }
    
}
