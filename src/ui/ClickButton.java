package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import cards.Card;

public class ClickButton extends FunctionalElement{
    private BufferedImage texture;
    private int selectBuffer = 10;
    public boolean selected = false;
    public Card card;
    public ClickButton(int x, int y, Text t) {
        super(x, y);
        texture = UIFactory.generateBorder(t.textImage, UIFactory.buttonData.borderThickness);
        bounds = new Rectangle(x,y,texture.getWidth(),texture.getHeight());
    }
    public ClickButton(int x, int y, BufferedImage tex){
        super(x, y);
        this.texture = tex;
        bounds = new Rectangle(x,y,texture.getWidth(),texture.getHeight());
    }
    @Override
    public void updateAdditional() {
        if(!focused){
            if(infoPacket!=null){
                infoPacket.focused = false;
            }
        }
    }

    public void scaleWithFactor(float factor){
        float oldHeight = bounds.height;
        int newHeight = (int)(oldHeight*factor);
        BufferedImage i2 = UIFactory.scaleToHeight(texture,newHeight);
        texture = i2;
        bounds.setSize(texture.getWidth(),texture.getHeight());
        updateBounds();
    }
    @Override
    public void render(Graphics g) {
        if(selected){
            g.setColor(Color.yellow);
            g.fillRect(bounds.x-selectBuffer, bounds.y-selectBuffer, bounds.width+selectBuffer*2,bounds.height+selectBuffer*2);

        }
        g.setColor(bg);
        g.fillRect(bounds.x,bounds.y,bounds.width,bounds.height);

        
        g.drawImage(texture, bounds.x, bounds.y, null);
        
        g.setColor(brd);
        g.drawRect(bounds.x,bounds.y,bounds.width,bounds.height);
        if(focused && infoPacket!=null){
            infoPacket.render(g);
        }
    }
    
}
