package ui;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import entities.Entity;
import entities.EntityInfo;
import entities.EntityManager;
import entities.ai.PlayerAI;
import entities.bows.Bows;
import entities.player.InfoPacket;
import entities.potions.Potions;
import entities.projectiles.Projectiles;
import entities.staves.Staves;
import entities.swords.Swords;
import gfx.AssetStorage;
import gfx.Transition;
import loot.Market;
import loot.MarketItem;
import main.Game;
import states.GameState;
import states.State;

public class PauseMenu {
    
    public static Container currentContainer;
    public static HashMap<String,Container> containers = new HashMap<String,Container>();
    public PauseMenu(){}

    public static void loadPauseMenus() throws Exception{
        containers.put("basic", createBasicPauseMenu());
        containers.put("main", createMenuStateContainer());
        containers.put("dead",createDeadPauseMenu());
        
    }
    public static void generateNewContainer(String text){
        Task t1 = new Task(){
            public void perform(){
                State.setState(State.blacksmithState, true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
        Task t2 = new Task(){
            public void perform(){
                State.setState(State.witchState, true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
        Task t3 = new Task(){
            public void perform(){
                State.setState(State.traderState, true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
        Task t4 = new Task(){
            public void perform(){
                State.setState(State.traderState, true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
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
                containers.put("enter_witch", createNPCEntryDialog(t2,"Witch","Hi There","portrait_witch"));
                break;
            case "enter_trader":
                containers.put("enter_trader", createNPCEntryDialog(t3,"Trader","Hi There","portrait_test"));
                
                break;
            case "enter_blacksmith":
                
                containers.put("enter_blacksmith", createNPCEntryDialog(t1,"BlackSmith","Hi There","portrait_blacksmith"));
                break;
            case "enter_guide":
                containers.put("enter_guide", createNPCEntryDialog(t4,"Guide","Hi There","portrait_test"));
                break;
            default:
                break;
        }
    }
    
    private static Container createDeadPauseMenu(){
        int w = Game.w.getWidth()/2;
        int h = (int)(Game.w.getHeight()*0.75);
        int x = Game.w.getWidth() /2 - w/2;
        int y = Game.w.getWidth() /2 - h/3;

        Container c = new Container(x,y,w,h);
        c.fillBg = true;
        Text title = new Text("Game over!", 0, 0, 0, true);
        // c.setHeader(title);
        c.addElement(title);
        Task t = new Task(){
            public void perform(){
                State.setState(State.menuState, true);
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };

        ClickButton backToMenu = new ClickButton(0, 0, new Text("Back to Menu",0,0,0,false));
        backToMenu.setTask(t);
        backToMenu.setPosition(0,h-backToMenu.bounds.height-10);
        c.addElement(backToMenu);
        c.centerElements();
        // c.calculateBounds();
        return c;
    }
    private static Container createMenuStateContainer(){
        Container container = new Container(0, 0, Game.w.getWidth(),Game.w.getHeight());
        Text title = new Text("Game Title", 0, 50, 0, true);
        Task t = new Task(){
            public void perform(){
                GameState.newGame();
            }
        };
        Task load = new Task(){
            public void perform(){
                GameState.loadGame("save1");
            }
        };
        ClickButton newGameButton = new ClickButton(100, 200, new Text("New Game",0,0,0,false));
        newGameButton.setTask(t);
        ClickButton loadGameButton = new ClickButton(100, 320,new Text("Load Game",0,0,0,false));
        loadGameButton.setTask(load);
        
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
    public static Container generateTradeContainer(Entity item,int amount, String question, ArrayList<ClickButton> buttons){
        
        Container c = createBasicDialog(question, buttons);
        c.fillBg = true;
        return c;
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
    private static Container createBasicDialog(String question,ArrayList<ClickButton> options){
        
        int buffer = 100;
        int w = (Game.w.getWidth()/4*3)-buffer;
        int h = Game.w.getHeight()/3;
        int x = Game.w.getWidth()/2-w/2;
        int y = Game.w.getHeight()-h-50;
        Container c = new Container(x,y,w,h);
        
        int leftSideWidth = w/2;
        //CREATE LEFT AND RIGHT SIDE.
        //LEFT HAS QUESTION, RIGHT HAS ANSWER OPTIONS.
        Container leftSide = new Container(0,0,leftSideWidth, h-20);
        Text title = new Text(question, 0, 0, leftSideWidth, false);
        leftSide.addElement(title);
        //Center in container
        leftSide.centerAndSpaceHorizontally(10);
        c.addElement(leftSide);
        c.setPosition(x, y);
        leftSide.setPosition(0, 0);
        
        Container rightSide = new Container(leftSideWidth,0,w-leftSideWidth,h);
        for(int i = 0; i < options.size(); i++){
            ClickButton ca = options.get(i);
            rightSide.addElement(ca);            
        }
        rightSide.spaceOutVertically(10);
        rightSide.centerElements();
        c.addElement(rightSide);
        rightSide.setPosition(leftSideWidth, 0);
        rightSide.wrap();
        c.centerAndSpaceHorizontally(10);
        c.centerVertically();
        return c;
    }
    private static Container createEntryDialog(Task t1, String question){
        Task t2 = new Task(){
            public void perform(){
                PauseMenu.setContainer(null);
                GameState.paused = false;
            }
        };

        Container c = createYesNoDialog(question, t1, t2);
        c.fillBg = true;
        // c.addElement(portrait);
        return c;

    }
    private static Container createNPCEntryDialog(Task t1, String name, String question,String p_name){

        Container entryDialog = new Container(0, 0, Game.w.getWidth(), Game.w.getHeight());
        
        // Image i = new Image(EntityManager.entityInfos.get(19).texture, 0, 0);
        Container downSide = createEntryDialog(t1,question);
        Text nameText = new Text(name, 0, 0, downSide.bounds.width/2, false);
        nameText.fillBg = true;
        BufferedImage portrait = AssetStorage.images.get(p_name);
        Image i = new Image(portrait, 0,0);
        Container upSide = new Container(0, 0, downSide.bounds.width, i.image.getHeight());
        i.setPosition(upSide.bounds.width-i.bounds.width, upSide.bounds.height-i.bounds.height);
        nameText.setPosition(0,upSide.bounds.height-nameText.bounds.height);
        upSide.addElement(nameText);
        upSide.addElement(i);

        entryDialog.addElement(upSide);
        entryDialog.addElement(downSide);
        upSide.setPosition(downSide.bounds.x, downSide.bounds.y-upSide.bounds.height);
        return entryDialog;
    }
    
    
    public static Container createTunnelEntryDialog(Task t){
        // Image i = new Image(EntityManager.entityInfos.get(11).texture, 0, 0);
        return createEntryDialog(t,"Go through?");
    }
    private static Container createBlacksmithContainer(){
        return createShopContainer(Market.lists.get("blacksmith"), new Text("BlackSmith", 0, 0, 0, true));
    }
    private static Container createShopContainer(ArrayList<Market> market, UIElement header){
        Container c = new Container(0,0,Game.w.getWidth(),Game.w.getHeight());
        c.setHeader(header);
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

        Container list =  new Container(0, 0, 700,600);
        list.fillBg = true;
        int listX = (int) (Game.w.getWidth()/2 - list.bounds.getWidth()/2);
        int listY = (int) (c.header.bounds.y+ c.header.bounds.getHeight());
        list.setPosition(listX,listY);
        

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
        list.latestEnd = end;
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
                list.latestEnd = end;
                addToListWithRange(list,market, start,end);
                list.updateList();
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
                
                int max = market.size();
                if(max<=1){
                    max = market.size();
                    start = 0;
                }
                if(start > max-1){
                    start = max-1;
                }
                list.start = start;
                int end = start +3;
                if(end > market.size()){
                    end = market.size();
                }
                list.latestEnd = end;
                addToListWithRange(list,market, start,end);
                list.updateList();
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
            Container listItem = generateListItem(c.bounds.width,market,c, list);
            listItem.drawBounds = true;
            // listItem.setOffset(c.x,c.y);
            listItem.centerVertically();
            // listItem.fillBg = true;
            // listItem.overrideColor = Color.magenta;
            c.addLate(listItem);
        }
        c.swap();
        c.updateList();
        PlayerAI.inv.updateInventory();
    }
    private static Container generateListItem(int maxWidth,Market m, Container list, ArrayList<Market> parent){
        Container c = new Container(0,0,maxWidth,120);
        int sellItemID1 = m.sellItem.id;
        int sellItemID2 = m.sellItem.id2;
        final Entity ent;
        final String name;
        BufferedImage texture = null;
        if(sellItemID2!=-1){
            //Item has sub id
            //First ID is category. It is taken fron entities.json id. id 27 = sword, id 35 = projectiles.
            switch (sellItemID1) {
                case 26:
                    //Potion
                    ent = new Entity(EntityManager.entityInfos.get(26),0,0);
                    ent.potionInfo =Potions.potions.get(sellItemID2);
                    break;
                case 27:
                    //Sword
                    ent = new Entity(EntityManager.entityInfos.get(27),0,0);
                    ent.swordInfo = Swords.swords.get(sellItemID2);
                    break;
                case 35:
                    //Projectile
                    ent = new Entity(EntityManager.entityInfos.get(35),0,0);
                    ent.projectileInfo = Projectiles.projectiles.get(sellItemID2);
                    break;
                case 36:
                    //Staff
                    ent = new Entity(EntityManager.entityInfos.get(36),0,0);
                    ent.staffInfo = Staves.staves.get(sellItemID2);
                    break;
                case 37:
                    //Staff
                    ent = new Entity(EntityManager.entityInfos.get(37),0,0);
                    ent.bowInfo = Bows.bows.get(sellItemID2);
                    break;
                default:
                    ent = new Entity(EntityManager.entityInfos.get(2),0,0);
                    break;
            }
            
            ent.loadBasicInfo();
            name = ent.name;
            texture = ent.texture;
        }else{
            EntityInfo e1 = EntityManager.entityInfos.get(sellItemID1);
            ent = new Entity(e1,0,0);
            ent.loadBasicInfo();
            name = ent.name;
            texture = ent.texture;
        }
        int amount = m.sellItem.amount;
        

        Task t = new Task(){
            public void perform(){
                //Buy
                ArrayList<MarketItem> need = m.priceItems;
                
                System.out.println("You are trying to buy "+amount + " x " + name);
                System.out.println("You need :");
                boolean allGood = true;
                for(MarketItem needItem : need){
                    EntityInfo needInfo = EntityManager.entityInfos.get(needItem.id);
                    int needAmount = needItem.amount;
                    System.out.println("  "+needAmount+" x "+ needInfo.name);
                    boolean haveEnough = PlayerAI.inv.checkIfEnoughItemsWithID(needItem.id,needItem.id2,needAmount);
                    if(!haveEnough){
                        System.out.println("Not enough.");
                        allGood = false;
                        break;
                    }else{
                        System.out.println("You have enough "+ needInfo.name);
                    }
                }
                //Sell Item transaction
                if(allGood){
                    System.out.println("All Good");
                    for(MarketItem needItem : need){
                        PlayerAI.inv.pay(needItem.id, needItem.amount);
                    }
                    for(int i = 0; i < m.sellItem.amount; i++){
                        if(ent!=null){
                            PlayerAI.inv.addItem(ent);
                        }
                    }
                    addToListWithRange(list, parent, list.start, list.latestEnd);
                    list.updateList();
                    //Update list so backgrounds can be true colors
                }else{
                    System.out.println("You don't have everything needed to purchase this item.");
                }
                
                
            }
        };
        int lockWidth = maxWidth/3*2;
        Container leftSide = new Container(0,0,lockWidth,c.bounds.height);
        //Create LeftSide
        
        //GENERATE IMAGE BOX
        Image icon = generateImageIcon(texture,amount,150,150,true,Color.black);
        int maxSizeForText1 = (int)(leftSide.bounds.getWidth()-icon.bounds.getWidth());
        InfoPacket infoPacket = new InfoPacket(0,0);
        infoPacket.update(ent,0,1,maxSizeForText1,100,false);

        leftSide.addElement(icon);
        leftSide.addElement(infoPacket.c);
        // Text itemText = new Text(name,0,0,maxSizeForText1,false);
        // leftSide.addElement(itemText);
        leftSide.spaceHorizintally(20);
        leftSide.calculateBounds();
        leftSide.bounds.setSize(lockWidth,leftSide.bounds.height);
        // leftSide.centerVertically();
        
        c.addElement(leftSide);





        //RIGHT SIDE
        Container rightSide = new Container(0,0,(int)(c.bounds.getWidth()-leftSide.bounds.getWidth()),(int)(leftSide.bounds.getHeight()));
        // rightSide.fillBg = true;
        // rightSide.overrideColor = Color.red;

        Container uphalf = new Container(0,0,(int)(rightSide.bounds.getWidth()),(int)(rightSide.bounds.getHeight()/2));
        // uphalf.fillBg = true;
        // uphalf.overrideColor=Color.white;
        boolean enough = true;
        for(MarketItem mi : m.priceItems){
            EntityInfo e2 = EntityManager.entityInfos.get(mi.id);
            int amount2 = mi.amount;
            Color bgColor = Color.green;
            if(!PlayerAI.inv.checkIfEnoughItemsWithID(mi.id, mi.id2, amount2)){
                bgColor = Color.red;
                enough = false;
            }
            Image icon1 = generateImageIcon(e2.texture,amount2,55,55,true,bgColor);
            
            uphalf.addElement(icon1);

        }
        uphalf.centerVertically();
        uphalf.centerAndSpaceHorizontally(10);
        rightSide.addElement(uphalf);
        
        Container downHalf= new Container(0,0,(int)(rightSide.bounds.getWidth()),(int)(rightSide.bounds.getHeight()/2));
        
        ClickButton buyButton = new ClickButton(0,0,new Text("Buy",0,0,(int)downHalf.bounds.getWidth(),false));
        if(enough){
            buyButton.disabled = false;
            buyButton.setTask(t);
        }else{
            buyButton.disabled = true;
            // buyButton.setTask(t);
        }
        
        buyButton.scaleWithFactor(0.7f);
        downHalf.addElement(buyButton);
        downHalf.wrap();
        downHalf.centerElements();
        rightSide.addElement(downHalf);
        rightSide.updateBounds();
        rightSide.spaceOutVertically(0);
        rightSide.centerElements();
        rightSide.calculateBounds();
        c.addElement(rightSide);
        // rightSide.setPosition(0, 0);
        // leftSide.wrap();

        c.spaceHorizintally(0);
        c.calculateBounds();
        c.wrap();
        return c;
    }
    
    private static Image generateImageIcon(BufferedImage i, int amount, int w, int h, boolean borderFrame,Color bg){
        BufferedImage iconA = UIFactory.generateIconWithAmount(i,amount,w,h,borderFrame, bg);
        return new Image(iconA, 0,0);

    }
    private static Container createWitchContainer(){
        return createShopContainer(Market.lists.get("witch"), new Text("Witch", 0, 0, 0, true));
        
    }
    private static Container createTraderContainer(){
        return createShopContainer(Market.lists.get("trader"), new Text("Cave Trader", 0, 0, 0, true));
        
    }
    public static void setContainer(Container cc){
        currentContainer = cc;
    }
    
}
