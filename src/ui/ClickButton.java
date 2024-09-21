package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
public class ClickButton extends FunctionalElement{
    private BufferedImage texture;
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
        g.setColor(bg);
        g.fillRect(bounds.x,bounds.y,bounds.width,bounds.height);

        
        g.drawImage(texture, bounds.x, bounds.y, null);
        
        g.setColor(brd);
        g.drawRect(bounds.x,bounds.y,bounds.width,bounds.height);
    }
    
}
