package ui;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import entities.EntityInfo;
import entities.EntityManager;
import gfx.AssetStorage;
import gfx.Transition;
import loot.Market;
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
        containers.put("blacksmith",createBlacksmithContainer());
        containers.put("witch",createWitchContainer());
        containers.put("trader",createTraderContainer());


        containers.put("enter_blacksmith", createBlacksmithEntryDialog());
        containers.put("enter_witch", createWitchEntryDialog());
        containers.put("enter_trader", createTraderEntryDialog());
        
    }

    private static Container createMenuStateContainer(){
        Container container = new Container(0, 0, Game.w.getWidth(),Game.w.getHeight());
        Text title = new Text("Game Title", 0, 50, 0, true);
        Task t = new Task(){
            public void perform(){
                World.load("lobby");
                State.setState(State.gameState,false);
            }
        };
        ClickButton newGameButton = new ClickButton(100, 200, new Text("New Game",0,0,100,false));
        newGameButton.setTask(t);
        ClickButton loadGameButton = new ClickButton(100, 320,new Text("Load Game",0,0,100,false));
        Task t2 = new Task(){public void perform(){System.exit(0);}};
        ClickButton exitButton = new ClickButton(100, 440, new Text("Exit",0,0,100,false));
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
        ClickButton returnButton = new ClickButton(0,250,new Text("Return to game",w/2,0,150,false));
        returnButton.setTask(t1);
        ClickButton settingsButton = new ClickButton(0,250,new Text("Settings",w/2,0,150,false));
        
        
        Task t2 = new Task(){
            public void perform(){
                GameState.paused = false;
                State.setState(State.menuState,true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
        ClickButton exitButton = new ClickButton(0,250,new Text("Save and exit",w/2,0,150,false));
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
        Text title = new Text(header, 20, y, 0, true);
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
    
    private static Container createBlacksmithEntryDialog(){
        Task t1 = new Task(){
            public void perform(){
                State.setState(State.blacksmithState, true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
        Task t2 = new Task(){
            public void perform(){
                GameState.paused = false;
            }
        };
        Container c = createYesNoDialog("Open Blacksmith shop?", t1, t2);
        c.fillBg = true;
        return c;
    }
    private static Container createWitchEntryDialog(){
        Task t1 = new Task(){
            public void perform(){
                State.setState(State.witchState, true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
        Task t2 = new Task(){
            public void perform(){
                GameState.paused = false;
            }
        };
        Container c = createYesNoDialog("Open Witch shop?", t1, t2);
        return c;
    }
    private static Container createTraderEntryDialog(){
        Task t1 = new Task(){
            public void perform(){
                State.setState(State.traderState, true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
        Task t2 = new Task(){
            public void perform(){
                GameState.paused = false;
            }
        };
        Container c = createYesNoDialog("Would you like to buy something?", t1, t2);
        return c;
    }
    public static Container createTunnelEntryDialog(Task t){
        Task t2 = new Task(){
            public void perform(){
                GameState.paused = false;
            }
        };
        Container c = createYesNoDialog("Would you like to enter the tunnel?", t, t2);
        return c;
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

        Container list = generateScrollableList(Market.lists.get("blacksmith"));        
        int listX = (int) (Game.w.getWidth()/2 - list.bounds.getWidth()/2);
        int listY = (int) (Game.w.getHeight()/2 - list.bounds.getHeight()/2);
        list.setPosition(listX,listY);
        list.updateList();
        c.addElement(list);
        return c;
    }
    private static Container generateScrollableList(ArrayList<Market> list){
        Container c = new Container(0, 0, 1000,400);
        c.isList = true;
        int currentY = 0;
        for(Market itemToSell : list){
            EntityInfo info = EntityManager.entityInfos.get(itemToSell.id);
            String name = info.name;
            int amount = itemToSell.amount;
            
            EntityInfo info2 = EntityManager.entityInfos.get(itemToSell.id2);
            String name2 = info2.name;
            int amount2 = itemToSell.amount2;
            System.out.println(name2 + " " + amount2 + " -> " + name + " " + amount); 
            Container listItem = generateListItem(info,amount,info2,amount2);
            listItem.centerVertically();
            currentY += listItem.bounds.getHeight();
            c.addElement(listItem);
        }
        
        Task t1 = new Task(){
            public void perform(){
                if(c.start>0){
                    c.start--;
                }
                c.updateList();
            }
        };
        Task t2 = new Task(){
            public void perform(){
                c.start++;
                c.updateList();
            }
        };
        ClickButton upButton = new ClickButton((int)(c.x+c.bounds.getWidth()), c.y, AssetStorage.images.get("arrow_up"));
        ClickButton downButton = new ClickButton((int)(c.x+c.bounds.getWidth()), (int)(c.y + c.bounds.getHeight()), AssetStorage.images.get("arrow_down"));
        upButton.setTask(t1);
        downButton.setPosition(downButton.x, (int)(c.y + c.bounds.getHeight()-downButton.bounds.height));
        downButton.setTask(t2);
        c.addElement(upButton);
        c.addElement(downButton);
        c.updateList();
        c.fillBg = true;
        return c;
    }
    private static Container generateListItem(EntityInfo e1, int amount, EntityInfo e2, int amount2){
        Container c = new Container(0,0,200,40);
        Task t = new Task(){
            public void perform(){
                //Buy
                System.out.println("Buy " + e1.name);
            }
        };
        ClickButton buy = new ClickButton(0,0,new Text("Buy",0,0,0,false));
        buy.setTask(t);
        c.addElement(buy);

        Text priceText = new Text(amount2+"x"+e2.name,0,0,100,false);
        c.addElement(priceText);
        Image icon1 = new Image(e2.texture,0,0);
        c.addElement(icon1);
        
        Image arrow = new Image(AssetStorage.images.get("arrow_right"),0,0);
        c.addElement(arrow);
        
        Text itemText = new Text(amount+"x"+e1.name,0,0,100,false);
        c.addElement(itemText);
        Image icon2 = new Image(e1.texture,0,0);
        c.addElement(icon2);
        
        c.spaceHorizintally(10);
        c.fillBg = true;
        c.calculateBounds();
        return c;
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
