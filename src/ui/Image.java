package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gfx.AssetStorage;

public class Image extends UIElement{
    public BufferedImage image;
    public Image(BufferedImage i,int x, int y) {
        super(x, y);

        this.image = AssetStorage.scaleImage(i, 2);
        bounds = new Rectangle(x,y,image.getWidth(),image.getHeight());
    }
    public void updateImage(BufferedImage i){
        this.image = AssetStorage.scaleImage(i, 2);
        bounds = new Rectangle(x,y,image.getWidth(),image.getHeight());
    }
    @Override
    public void render(Graphics g) {
        g.drawImage(image, bounds.x, bounds.y, null);
        // g.setColor(UIFactory.textData.borderColor);
        // g.drawRect(bounds.x,bounds.y,bounds.width,bounds.height);
    }
    
}
