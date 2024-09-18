package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
public class ToggleButton extends FunctionalElement{
    private BufferedImage texture;
    public ToggleButton(int x, int y, Text t) {
        super(x, y);
        texture = UIFactory.generateBorder(t.textImage, UIFactory.buttonData.borderThickness);
        bounds = new Rectangle(x,y,texture.getWidth(),texture.getHeight());
    }
    public ToggleButton(int x, int y, BufferedImage tex){
        super(x, y);
        this.texture = tex;
        bounds = new Rectangle(x,y,texture.getWidth(),texture.getHeight());
    }
    @Override
    public void updateAdditional() {
        
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
