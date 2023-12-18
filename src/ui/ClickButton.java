package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
public class ClickButton extends FunctionalElement{
    private Text t;
    private BufferedImage texture;
    public ClickButton(int x, int y, Text t) {
        super(x, y);
        this.t = t;
        bounds = new Rectangle(x,y,t.textImage.getWidth(),t.textImage.getHeight());
    }
    public ClickButton(int x, int y, BufferedImage texture){
        super(x, y);
        this.texture = texture;
        bounds = new Rectangle(x,y,texture.getWidth(),texture.getHeight());
    }
    @Override
    public void updateAdditional() {
        
        
    }

    @Override
    public void task() {}

    @Override
    public void render(Graphics g) {
        g.setColor(bg);
        g.fillRect(bounds.x,bounds.y,bounds.width,bounds.height);

        if(t !=null){
            g.drawImage(t.textImage, bounds.x, bounds.y, null);
        }else{
            g.drawImage(texture, bounds.x, bounds.y, null);
        }
        g.setColor(brd);
        g.drawRect(bounds.x,bounds.y,bounds.width,bounds.height);
    }
    
}
