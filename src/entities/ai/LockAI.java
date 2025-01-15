package entities.ai;

import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Entity;
import gfx.AssetStorage;
import io.KeyManager;
import states.GameState;
import ui.ClickButton;
import ui.Container;
import ui.PauseMenu;
import ui.Task;
import ui.Text;
import world.Room;
import world.World;

public class LockAI extends AI{
    private Ellipse2D.Double interactCircle;
    private boolean talk = false;
    public LockAI(Entity e){
        super(e);
    }
    @Override
    public void lateInit() {
        interactCircle = e.generateSurroundingCircle(150);
    }

    @Override
    public void update() {
        if(interactCircle.intersects(World.player.bounds)){
        talk = true;
        if(KeyManager.interactKey){
            openTradingMenu();
        }
        }else{
            talk = false;
        }
    }
    private void openTradingMenu(){
        
        Entity neededKey;
        if(e.info.id==55){
            neededKey = World.entityManager.generateEntityWithID(58, -1, 0, 0);
        }else if(e.info.id==56){
            neededKey = World.entityManager.generateEntityWithID(59, -1, 0, 0);
        }else if(e.info.id==57){
            neededKey = World.entityManager.generateEntityWithID(60, -1, 0, 0);
        }else{
            neededKey = World.entityManager.generateEntityWithID(58, -1, 0, 0);

        }
        int amount = 1;
        Task unPause = new Task(){
            public void perform(){
                GameState.paused = false;
                for(Room r : World.entityManager.roomsToUpdate){
                    r.resetEntityClocks();
                }
            }
        };
        Task open = new Task(){
            public void perform(){
                PlayerAI.inv.pay(neededKey.info.id, amount);
                openLocks();
                GameState.paused = false;
            }
        };
        ClickButton yesButton = new ClickButton(0,250,new Text("Open",0,0,0,false));
        yesButton.setTask(open);
        
        boolean b = PlayerAI.inv.checkIfEnoughItemsWithID(neededKey.info.id, neededKey.subID, amount);
        if(!b){
            yesButton.disabled = true;
        }
        ClickButton noButton = new ClickButton(0,250,new Text("Leave",0,0,0,false));
        noButton.setTask(unPause);
        ArrayList<ClickButton> buttons = new ArrayList<ClickButton>();
        buttons.add(yesButton);
        buttons.add(noButton);
        String question = "You need "+amount +"x" +neededKey.info.name + " to open this lock.";
        Container c = PauseMenu.generateTradeContainer(neededKey,1, question, buttons);

        PauseMenu.setContainer(c);
        GameState.paused = true;
    }
    private void openLocks(){
        for(Entity locks : e.homeRoom.entities){
            if(locks.info.id==e.info.id){
                e.homeRoom.removeEntity(locks);
            }
        }
    }
    @Override
    public void render(Graphics g) {
        if(talk){
            drawTalkBox(g);
        }
    }
    private void drawTalkBox(Graphics g){
        BufferedImage img = AssetStorage.images.get("interact_e");
        g.drawImage(img,(int) (e.x +e.bounds.width/2 - World.camera.getXOffset()-img.getWidth()/2 ), (int)(e.y-World.camera.getYOffset() - img.getHeight()), null);
    }
    
}
