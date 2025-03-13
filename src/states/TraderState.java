package states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entities.EntityManager;
import gfx.Animation;
import gfx.AssetStorage;
import ui.Container;
import ui.PauseMenu;

public class TraderState extends State {
    private BufferedImage trader;
    private Animation an;
    private Container c;
    @Override
    public void update() {
        if(running){
            c.update();
            an.animate();
        }else{
            State.transition.update();
        }

    }

    @Override
    public void render(Graphics g) {
        trader = an.getFrame();
        trader = AssetStorage.scaleImage(trader, 2);
        c.render(g);
        g.setColor(Color.red);
        g.drawString("TraderState", 25, 25);
        g.drawImage(trader, 100, 100, null);
        if(transition !=null){transition.render(g);}
    }

    @Override
    public void init() {
        an = EntityManager.entityInfos.get(12).animations.get("trade");
        PauseMenu.generateNewContainer("trader");
        c = PauseMenu.containers.get("trader");
        PauseMenu.setContainer(c);
    }

    @Override
    public void updateOnceBetweenTransitions() {
    
    }

}
