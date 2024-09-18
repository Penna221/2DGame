package states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import entities.EntityManager;
import gfx.Animation;
import gfx.AssetStorage;
import gfx.Factory;
import gfx.Transition;
import main.Game;
import ui.ClickButton;
import ui.Container;
import ui.PauseMenu;
import ui.Task;
import ui.Text;

public class TraderState extends State {
    private BufferedImage table, trader, sell_board;
    private Animation an;
    private Container c;
    @Override
    public void update() {
        if(running){
            c.update();
        }else{
            State.transition.update();
        }

    }

    @Override
    public void render(Graphics g) {
        trader = an.getFrame();
        c.render(g);
        g.setColor(Color.red);
        g.drawString("TraderState", 25, 25);
        int tableX = Game.w.getWidth()/2 -table.getWidth();
        int tableY = Game.w.getHeight()/2-table.getHeight();
        g.drawImage(table,tableX,tableY,table.getWidth()*2,table.getHeight()*2,null);
        // Factory.drawCenteredAt(g, table, new Point(tableX,tableY), 2);
        
        g.drawImage(trader,tableX-trader.getWidth()*2,tableY,trader.getWidth()*2,trader.getHeight()*2,null);
        g.drawImage(sell_board,Game.w.getWidth()-sell_board.getWidth()-20,tableY,null);
        
        
        an.animate();
        if(transition !=null){transition.render(g);}
    }

    @Override
    public void init() {
        System.out.println("TraderState init");
        an = EntityManager.entityInfos.get(12).animations.get("trade");
        sell_board = AssetStorage.images.get("sell_board");
        table = AssetStorage.images.get("trader_table");
        Task t1 = new Task(){
            public void perform(){
                State.setState(gameState, true);
                
                Transition.canContinue2 = true;
                Transition.canFinish = true;
            }
        };
        c = PauseMenu.containers.get("witch");
        PauseMenu.setContainer(c);
    }

    @Override
    public void updateOnceBetweenTransitions() {
    
    }

}
