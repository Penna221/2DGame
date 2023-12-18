package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
public class Text extends UIElement{
    public BufferedImage textImage;
    private String text;
    public Text(String text, int x, int y, int maxWidth) {
        super(x, y);
        textImage = UIFactory.generateText(text);
        bounds = new Rectangle(x,y,textImage.getWidth(),textImage.getHeight());
    }
    public void updateText(String text){
        this.text = text;
        textImage = UIFactory.generateText(text);
        bounds = new Rectangle(x,y,textImage.getWidth(),textImage.getHeight());
    }
    @Override
    public void render(Graphics g) {
        g.drawImage(textImage, bounds.x, bounds.y, null);
    }
    
}
