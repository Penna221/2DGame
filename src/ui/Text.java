package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
public class Text extends UIElement{
    public BufferedImage textImage;
    private String text;
    private boolean border;
    public Text(String text, int x, int y, int maxWidth, boolean border) {
        super(x, y);
        this.border = border;
        this.text = text;
        textImage = UIFactory.generateText(text);
        if(border){
            textImage = UIFactory.generateBorder(textImage,UIFactory.textData.borderThickness);
        }
        bounds = new Rectangle(x,y,textImage.getWidth(),textImage.getHeight());
    }
    public void updateText(String text){
        this.text = text;
        textImage = UIFactory.generateText(text);
        if(border){
            textImage = UIFactory.generateBorder(textImage,UIFactory.textData.borderThickness);
        }
        bounds = new Rectangle(x,y,textImage.getWidth(),textImage.getHeight());
    }
    @Override
    public void render(Graphics g) {
        g.drawImage(textImage, bounds.x, bounds.y, null);
        g.setColor(UIFactory.textData.borderColor);
        g.drawRect(bounds.x,bounds.y,bounds.width,bounds.height);
    }
    
}
