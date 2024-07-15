package states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entities.EntityManager;
import gfx.Animation;
import gfx.AssetStorage;
import main.Game;

public class TraderState extends State {
    private BufferedImage table, trader, sell_board;
    private Animation an;
    @Override
    public void update() {
        if(running){
            
        }else{
            State.transition.update();
        }

    }

    @Override
    public void render(Graphics g) {
        trader = an.getFrame();
        g.setColor(Color.black);
        g.fillRect(0, 0, Game.w.getWidth(), Game.w.getHeight());
        g.setColor(Color.red);
        g.drawString("TraderState", 25, 25);
        int tableX = Game.w.getWidth()/2 - table.getWidth()/2;
        int tableY = Game.w.getHeight()/2 - table.getHeight()/2;
        g.drawImage(table,tableX,tableY,null);
        g.drawImage(trader,tableX-trader.getWidth(),tableY,null);
        g.drawImage(sell_board,tableX+table.getWidth(),tableY,null);
        
        
        an.animate();
        if(transition !=null){transition.render(g);}
    }

    @Override
    public void init() {
        System.out.println("TraderState init");
        an = EntityManager.entityInfos.get(12).animations.get("trade");
        sell_board = AssetStorage.images.get("sell_board");
        table = AssetStorage.images.get("trader_table");
    }

    @Override
    public void updateOnceBetweenTransitions() {
    
    }

}
